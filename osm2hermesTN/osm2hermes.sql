

-- Estos inserts dependen totalmente del orden en el que se insertan los datos para conservar correctamente las relaciones.
CREATE TEMP SEQUENCE tmp_hermes_network_element_id;

-- Nodos a partir de las columnas source y target de la tabla generada por osm2po:
INSERT INTO hermes_network_element (osm_id, type) 
	SELECT osm_source_id, 'TransportNode' FROM es_cor_2po_4pgr UNION 
	SELECT osm_target_id, 'TransportNode' FROM es_cor_2po_4pgr;

-- Un Transport Link por cada fila de osm2po, un Transport Link Sequence por cada calle
INSERT INTO hermes_network_element (osm_id, type) 
	SELECT osm_id, 'TransportLinkSequence' FROM es_cor_2po_4pgr GROUP BY osm_id;
SELECT setval('tmp_hermes_network_element_id', currval('hermes_network_element_id_seq')); -- Evitarme un join
INSERT INTO hermes_network_element (id, osm_id, type) 
	SELECT id + currval('tmp_hermes_network_element_id'), osm_id, 'TransportLink' FROM es_cor_2po_4pgr;

INSERT INTO hermes_transport_node (id, geometry) SELECT hne.id, n.geom FROM hermes_network_element hne JOIN nodes n ON hne.osm_id = n.id;

INSERT INTO hermes_transport_link (id, centerline_geometry, start_node, end_node) 
	SELECT w.id + currval('tmp_hermes_network_element_id'), w.geom_way, hnes.id, hnet.id 
	FROM es_cor_2po_4pgr w 
		JOIN hermes_network_element hnes ON hnes.osm_id = w.osm_source_id 
		JOIN hermes_network_element hnet ON hnet.osm_id = w.osm_target_id;

INSERT INTO hermes_transport_link_sequence (id) SELECT id FROM hermes_network_element WHERE type = 'TransportLinkSequence';
INSERT INTO hermes_transport_link_sequence_transport_link (link_sequence_id, link_id, "order", direction) 
	SELECT htls.id, htl.id, (SELECT count(*) FROM hermes_network_element WHERE osm_id = htl.osm_id AND id < htl.id), CASE
			WHEN w.tags->'oneway' = 'no' THEN 'bothDirections'	-- FIXME I shouldn't use this field to store the direction of the traffic flow...
			WHEN w.tags->'oneway' = 'yes' OR w.tags->'oneway'='1' OR w.tags->'junction' = 'roundabout' 
				OR w.tags->'highway' = 'motorway' OR w.tags->'highway' = 'motorway_link' THEN 'inDirection' 
			WHEN w.tags->'oneway' = '-1' THEN 'inOppositeDirection'
			ELSE 'bothDirections'
		END 
	FROM hermes_network_element htls 
		JOIN hermes_network_element htl ON htl.type = 'TransportLink' AND htl.osm_id = htls.osm_id
		JOIN ways w ON w.id = htls.osm_id
	WHERE htls.type = 'TransportLinkSequence';

DROP SEQUENCE tmp_hermes_network_element_id;


-- Una referencia para cada TransportLinkSequence que tenga algún tag de los que nos interesan
INSERT INTO hermes_network_reference (network_element_id, type, fromPosition, toPosition) 
	SELECT hne.id, 'SimpleLinearReference', 0, ST_Length(w.linestring)::decimal 
	FROM hermes_network_element hne 
		JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSequence';
		-- FIXME al final es una ref por cada TransportLinkSequence equivalente a su longitud
	--WHERE w.tags ?| ARRAY['name', 'lanes', 'lanes:forward', 'lanes:backward', 'layer', 'level', 'tunnel', 'bridge', 'highway', 'junction'];


CREATE OR REPLACE FUNCTION addProperties(matchTags varchar[], property varchar) RETURNS void AS $$
BEGIN
	INSERT INTO hermes_transport_property (type) 
		SELECT property 
		FROM hermes_network_element hne 
			JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSequence'
		WHERE w.tags ?| matchTags;

	INSERT INTO hermes_network_reference_transport_property 
		SELECT hnr.id, nextval('tmp_hermes_transport_property_id')  
		FROM hermes_network_reference hnr 
			JOIN hermes_network_element hne ON hne.id = hnr.network_element_id 
			JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSequence'
		WHERE w.tags ?| matchTags;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION importNumberOfLanes(tag varchar, direction varchar) RETURNS void AS $$
DECLARE
	htp_0 bigint := (SELECT CASE WHEN is_called THEN last_value + 1 ELSE last_value END FROM hermes_transport_property_id_seq);
BEGIN
	PERFORM addProperties(ARRAY[tag], 'NumberOfLanes');

	INSERT INTO hermes_number_of_lanes (id, numberOfLanes, direction) 
		SELECT hnrtp.transport_property_id, (w.tags->tag)::integer, direction 
		FROM hermes_network_reference_transport_property hnrtp 
			JOIN hermes_network_reference hnr ON hnr.id = hnrtp.network_reference_id 
			JOIN hermes_network_element hne ON hne.id = hnr.network_element_id 
			JOIN ways w ON w.id = hne.osm_id 
		WHERE hnrtp.transport_property_id >= htp_0;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION importRoadName(tag varchar) RETURNS void AS $$
DECLARE
	htp_0 bigint := (SELECT CASE WHEN is_called THEN last_value + 1 ELSE last_value END FROM hermes_transport_property_id_seq);
BEGIN
	PERFORM addProperties(ARRAY[tag], 'RoadName');

	INSERT INTO hermes_road_name (id, name) 
		SELECT hnrtp.transport_property_id, w.tags->tag 
		FROM hermes_network_reference_transport_property hnrtp 
			JOIN hermes_network_reference hnr ON hnr.id = hnrtp.network_reference_id 
			JOIN hermes_network_element hne ON hne.id = hnr.network_element_id 
			JOIN ways w ON w.id = hne.osm_id 
		WHERE hnrtp.transport_property_id >= htp_0;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION importVerticalPosition() RETURNS void AS $$
DECLARE
	htp_0 bigint := (SELECT CASE WHEN is_called THEN last_value + 1 ELSE last_value END FROM hermes_transport_property_id_seq);
BEGIN
	PERFORM addProperties(ARRAY['layer', 'level', 'tunnel', 'bridge'], 'VerticalPosition');

	INSERT INTO hermes_vertical_position (id, verticalPosition) 
		SELECT hnrtp.transport_property_id, CASE 
				WHEN (w.tags->'layer')::integer < 0 OR (w.tags->'level')::integer < 0 OR w.tags ? 'tunnel' THEN 'underground' 
				WHEN (w.tags->'layer')::integer > 0 OR (w.tags->'level')::integer > 0 OR w.tags ? 'bridge' THEN 'suspendedOrElevated' 
				ELSE 'onGroundSurface' 
			END
		FROM hermes_network_reference_transport_property hnrtp 
			JOIN hermes_network_reference hnr ON hnr.id = hnrtp.network_reference_id 
			JOIN hermes_network_element hne ON hne.id = hnr.network_element_id 
			JOIN ways w ON w.id = hne.osm_id 
		WHERE hnrtp.transport_property_id >= htp_0;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION importFormOfWay() RETURNS void AS $$
DECLARE
	htp_0 bigint := (SELECT CASE WHEN is_called THEN last_value + 1 ELSE last_value END FROM hermes_transport_property_id_seq);
BEGIN
	INSERT INTO hermes_transport_property (type) 
		SELECT 'FormOfWay' 
		FROM hermes_network_element hne 
			JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSequence'
		WHERE w.tags -> 'highway' IN ('cycleway', 'motorway', 'trunk', 'pedestrian', 'footway', 'path', 'steps', 'track') OR w.tags->'highway' LIKE '%\_link';

	INSERT INTO hermes_network_reference_transport_property 
		SELECT hnr.id, nextval('tmp_hermes_transport_property_id')  
		FROM hermes_network_reference hnr 
			JOIN hermes_network_element hne ON hne.id = hnr.network_element_id 
			JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSequence'
		WHERE w.tags -> 'highway' IN ('cycleway', 'motorway', 'trunk', 'pedestrian', 'footway', 'path', 'steps', 'track') OR w.tags->'highway' LIKE '%\_link';

	INSERT INTO hermes_form_of_way (id, formOfWay) 
		SELECT hnrtp.transport_property_id, CASE 
				WHEN w.tags->'highway' = 'track' THEN 'tractorRoad' 
				WHEN w.tags->'highway' IN ('pedestrian', 'footway', 'path', 'steps') THEN 'pedestrianZone' 
				WHEN w.tags->'highway' = 'cycleway' THEN 'bicycleRoad' 
				WHEN w.tags->'highway' IN ('motorway', 'trunk') THEN 'motorway' --revisar
				WHEN w.tags->'highway' LIKE '%\_link' THEN 'slipRoad'
			END
		FROM hermes_network_reference_transport_property hnrtp 
			JOIN hermes_network_reference hnr ON hnr.id = hnrtp.network_reference_id 
			JOIN hermes_network_element hne ON hne.id = hnr.network_element_id 
			JOIN ways w ON w.id = hne.osm_id 
		WHERE hnrtp.transport_property_id >= htp_0;

	-- TODO amenity:parking_entrance, suele ser un nodo
	-- TODO serviceRoad, según su espeficicación se correspondería bien con highway:secondary, pero en la práctica (Coruña) eso no pasa
	-- TODO walkway. ¿Qué es una walkway? Unas escaleras serían una walkway?
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION importRoundAbouts() RETURNS void AS $$
DECLARE
	htp_0 bigint := (SELECT CASE WHEN is_called THEN last_value + 1 ELSE last_value END FROM hermes_transport_property_id_seq);
BEGIN
	INSERT INTO hermes_transport_property (type) 
		SELECT 'FormOfWay' 
		FROM hermes_network_element hne 
			JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSequence'
		WHERE w.tags -> 'junction' = 'roundabout';

	INSERT INTO hermes_network_reference_transport_property 
		SELECT hnr.id, nextval('tmp_hermes_transport_property_id')  
		FROM hermes_network_reference hnr 
			JOIN hermes_network_element hne ON hne.id = hnr.network_element_id 
			JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSequence'
		WHERE w.tags -> 'junction' = 'roundabout';

	INSERT INTO hermes_form_of_way (id, formOfWay) 
		SELECT hnrtp.transport_property_id, 'roundabout' 
		FROM hermes_network_reference_transport_property hnrtp 
			JOIN hermes_network_reference hnr ON hnr.id = hnrtp.network_reference_id 
			JOIN hermes_network_element hne ON hne.id = hnr.network_element_id 
			JOIN ways w ON w.id = hne.osm_id 
		WHERE hnrtp.transport_property_id >= htp_0;

END;
$$ LANGUAGE plpgsql;


CREATE TEMP SEQUENCE tmp_hermes_transport_property_id;
SELECT setval('tmp_hermes_transport_property_id', (SELECT last_value FROM hermes_transport_property_id_seq), false);
SELECT importNumberOfLanes('lanes', 'bothDirections');
SELECT importNumberOfLanes('lanes:forward', 'inDirection');
SELECT importNumberOfLanes('lanes:backward', 'inOppositeDirection');
SELECT importRoadName('name');
SELECT importVerticalPosition();
SELECT importFormOfWay();
SELECT importRoundAbouts();
DROP SEQUENCE tmp_hermes_transport_property_id;
