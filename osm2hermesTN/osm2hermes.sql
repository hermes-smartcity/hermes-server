DROP TABLE IF EXISTS hermes_network_element CASCADE;
CREATE TABLE hermes_network_element (
	id bigserial,
	osm_id bigint,
	type varchar(255)
);

ALTER TABLE hermes_network_element ADD CONSTRAINT hermes_network_element_pk PRIMARY KEY(id);
CREATE INDEX hermes_network_element_i_osm_id 
	ON hermes_network_element(osm_id);

DROP TABLE IF EXISTS hermes_transport_node CASCADE;
CREATE TABLE hermes_transport_node (
	id bigserial,
	network_element_id bigint NOT NULL,
	"geometry" geometry(Point,4326)
);

DROP TABLE IF EXISTS hermes_transport_link CASCADE;
CREATE TABLE hermes_transport_link (
	id bigserial,
	network_element_id bigint NOT NULL,
	centerline_geometry geometry(LineString,4326),
	ficticious boolean NOT NULL DEFAULT false,
	start_node bigint,
	end_node bigint
);

DROP TABLE IF EXISTS hermes_transport_link_sequence CASCADE;
CREATE TABLE hermes_transport_link_sequence (
	id bigserial,
	network_element_id bigint NOT NULL 
);

DROP TABLE IF EXISTS hermes_transport_link_sequence_transport_link CASCADE;
CREATE TABLE hermes_transport_link_sequence_transport_link (
	link_sequence_id bigint NOT NULL,
	link_id bigint NOT NULL,
	"order" integer,
	direction varchar(255)
);

DROP TABLE IF EXISTS hermes_transport_link_set CASCADE;
CREATE TABLE hermes_transport_link_set (
	id bigserial,
	network_element_id bigint NOT NULL
);

DROP TABLE IF EXISTS hermes_transport_link_set_element CASCADE;
CREATE TABLE hermes_transport_link_set_element (
	id bigserial,
	link_set_id bigint NOT NULL,
	link_id bigint,
	link_sequence_id bigint,
	type varchar(255),
	"order" integer
);

-- Estos inserts dependen totalmente del orden en el que se insertan los datos para conservar correctamente las relaciones.
CREATE TEMP SEQUENCE tmp_hermes_network_element_id;
INSERT INTO hermes_network_element (osm_id, type) 
	SELECT osm_source_id, 'TransportNode' FROM es_cor_2po_4pgr UNION 
	SELECT osm_target_id, 'TransportNode' FROM es_cor_2po_4pgr;
SELECT setval('tmp_hermes_network_element_id', currval('hermes_network_element_id_seq')); -- Evitarme un join
INSERT INTO hermes_network_element (osm_id, type) 
	SELECT osm_id, t FROM (SELECT id, osm_id, 'TransportLink' AS t FROM es_cor_2po_4pgr UNION ALL 
	SELECT NULL, osm_id, 'TransportLinkSet' AS t FROM es_cor_2po_4pgr GROUP BY osm_id) links ORDER BY id;
INSERT INTO hermes_transport_node (network_element_id, geometry) SELECT hne.id, n.geom FROM hermes_network_element hne JOIN nodes n ON hne.osm_id = n.id;
INSERT INTO hermes_transport_link (network_element_id, centerline_geometry, start_node, end_node) 
	SELECT 0, w.geom_way, s.id, t.id 
	FROM es_cor_2po_4pgr w 
		JOIN hermes_network_element hnes ON hnes.osm_id = w.osm_source_id 
		JOIN hermes_network_element hnet ON hnet.osm_id = w.osm_target_id 
		JOIN hermes_transport_node s ON s.network_element_id = hnes.id 
		JOIN hermes_transport_node t ON t.network_element_id = hnet.id
	ORDER BY w.id;
UPDATE hermes_transport_link SET network_element_id = id + currval('tmp_hermes_network_element_id');
INSERT INTO hermes_transport_link_set (network_element_id) SELECT id FROM hermes_network_element WHERE type = 'TransportLinkSet';
INSERT INTO hermes_transport_link_set_element (link_set_id, link_id, type, "order") 
	SELECT htls.id, htl.id, 'TransportLink', (SELECT count(*) FROM hermes_network_element WHERE osm_id = htlhne.osm_id AND id < htlhne.id) 
	FROM hermes_transport_link htl 
		JOIN hermes_network_element htlhne ON htlhne.id = htl.network_element_id AND htlhne.type = 'TransportLink' 
		JOIN hermes_network_element htlshne ON htlshne.type = 'TransportLinkSet' AND htlshne.osm_id = htlhne.osm_id 
		JOIN hermes_transport_link_set htls ON htls.network_element_id = htlshne.id;
DROP SEQUENCE tmp_hermes_network_element_id;

ALTER TABLE hermes_transport_node ADD CONSTRAINT hermes_transport_node_pk PRIMARY KEY(id);
ALTER TABLE hermes_transport_link ADD CONSTRAINT hermes_transport_link_pk PRIMARY KEY(id);
ALTER TABLE hermes_transport_link_sequence ADD CONSTRAINT hermes_transport_link_sequence_pk PRIMARY KEY(id);
ALTER TABLE hermes_transport_link_set ADD CONSTRAINT hermes_transport_link_set_pk PRIMARY KEY(id);
ALTER TABLE hermes_transport_link_set_element ADD CONSTRAINT hermes_transport_link_set_element_pk PRIMARY KEY(id);

-- CREATE INDEX hermes_network_element_i_type 
-- 	ON hermes_network_element(type);
-- CREATE INDEX hermes_transport_link_i_network_element_id 
-- 	ON hermes_transport_link(network_element_id);
-- CREATE INDEX hermes_transport_link_set_i_network_element_id 
-- 	ON hermes_transport_link_set(network_element_id);
-- CREATE INDEX hermes_transport_link_set_element_i_transport_link_set 
-- 	ON hermes_transport_link_set_element(link_set_id);

DROP TABLE IF EXISTS hermes_network_reference CASCADE;
CREATE TABLE hermes_network_reference (
	id bigserial,
	network_element_id bigint NOT NULL,
	type varchar(255),
	atPosition decimal,
	fromPosition decimal,
	toPosition decimal,
	"offset" decimal
);

CREATE INDEX hermes_network_reference_i_network_element_id 
	ON hermes_network_reference(network_element_id);

DROP TABLE IF EXISTS hermes_transport_property CASCADE;
CREATE TABLE hermes_transport_property (
	id bigserial,
	type varchar(255)
);

DROP TABLE IF EXISTS hermes_network_reference_transport_property CASCADE;
CREATE TABLE hermes_network_reference_transport_property (
	network_reference_id bigint,
	transport_property_id bigint
);

DROP TABLE IF EXISTS hermes_number_of_lanes CASCADE;
CREATE TABLE hermes_number_of_lanes (
	id bigint NOT NULL,
	numberOfLanes integer NOT NULL,
	direction varchar(255),
	minMaxNumberOfLanes varchar(255)
);

DROP TABLE IF EXISTS hermes_road_name CASCADE;
CREATE TABLE hermes_road_name (
	id bigint NOT NULL,
	name varchar -- TODO Geographical Name (D2.8.1.3 INSPIRE Data Specification on Geographical names)
);

DROP TABLE IF EXISTS hermes_vertical_position CASCADE;
CREATE TABLE hermes_vertical_position (
	id bigint NOT NULL,
	verticalPosition varchar(255)
);

DROP TABLE IF EXISTS hermes_form_of_way CASCADE;
CREATE TABLE hermes_form_of_way (
	id bigint NOT NULL,
	formOfWay varchar(255)
);

-- Una referencia para cada TransportLinkSet que tenga algún tag de los que nos interesan
INSERT INTO hermes_network_reference (network_element_id, type, fromPosition, toPosition) 
	SELECT hne.id, 'SimpleLinearReference', 0, ST_Length(w.linestring)::decimal 
	FROM hermes_network_element hne 
		JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSet';
		-- FIXME al final es una ref por cada TransportLinkSet equivalente a su longitud
	--WHERE w.tags ?| ARRAY['name', 'lanes', 'lanes:forward', 'lanes:backward', 'layer', 'level', 'tunnel', 'bridge', 'highway', 'junction'];


-- Utilidades

-- Create a function that always returns the first non-NULL item
CREATE OR REPLACE FUNCTION public.first_agg ( anyelement, anyelement )
RETURNS anyelement LANGUAGE SQL IMMUTABLE STRICT AS $$
        SELECT $1;
$$;
 
-- And then wrap an aggregate around it
DROP AGGREGATE IF EXISTS public.FIRST(anyelement);
CREATE AGGREGATE public.FIRST (
        sfunc    = public.first_agg,
        basetype = anyelement,
        stype    = anyelement
);
 
-- Create a function that always returns the last non-NULL item
CREATE OR REPLACE FUNCTION public.last_agg ( anyelement, anyelement )
RETURNS anyelement LANGUAGE SQL IMMUTABLE STRICT AS $$
        SELECT $2;
$$;
 
-- And then wrap an aggregate around it
DROP AGGREGATE IF EXISTS public.LAST(anyelement);
CREATE AGGREGATE public.LAST (
        sfunc    = public.last_agg,
        basetype = anyelement,
        stype    = anyelement
);

CREATE OR REPLACE FUNCTION castToInt(text) RETURNS integer AS $$
BEGIN
    -- Note the double casting to avoid infinite recursion.
    RETURN cast($1::varchar AS integer);
exception
    WHEN invalid_text_representation THEN
        RETURN 0;
END;
$$ LANGUAGE plpgsql immutable;

DROP CAST IF EXISTS (text AS integer);
CREATE cast (text AS integer) WITH FUNCTION castToInt(text);


-- TODO hacer la inserción de properties más eficiente (?)

CREATE OR REPLACE FUNCTION addProperties(matchTags varchar[], property varchar) RETURNS void AS $$
BEGIN
	INSERT INTO hermes_transport_property (type) 
		SELECT property 
		FROM hermes_network_element hne 
			JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSet'
		WHERE w.tags ?| matchTags;

	INSERT INTO hermes_network_reference_transport_property 
		SELECT hnr.id, nextval('tmp_hermes_transport_property_id')  
		FROM hermes_network_reference hnr 
			JOIN hermes_network_element hne ON hne.id = hnr.network_element_id 
			JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSet'
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
			JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSet'
		WHERE w.tags -> 'highway' IN ('cycleway', 'motorway', 'trunk', 'pedestrian', 'footway', 'path', 'steps', 'track') OR w.tags->'highway' LIKE '%\_link';

	INSERT INTO hermes_network_reference_transport_property 
		SELECT hnr.id, nextval('tmp_hermes_transport_property_id')  
		FROM hermes_network_reference hnr 
			JOIN hermes_network_element hne ON hne.id = hnr.network_element_id 
			JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSet'
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
			JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSet'
		WHERE w.tags -> 'junction' = 'roundabout';

	INSERT INTO hermes_network_reference_transport_property 
		SELECT hnr.id, nextval('tmp_hermes_transport_property_id')  
		FROM hermes_network_reference hnr 
			JOIN hermes_network_element hne ON hne.id = hnr.network_element_id 
			JOIN ways w ON w.id = hne.osm_id AND hne.type = 'TransportLinkSet'
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

ALTER TABLE hermes_network_reference ADD CONSTRAINT hermes_network_reference_pk PRIMARY KEY(id);
ALTER TABLE hermes_transport_property ADD CONSTRAINT hermes_transport_property_pk PRIMARY KEY(id);
ALTER TABLE hermes_number_of_lanes ADD CONSTRAINT hermes_number_of_lanes_pk PRIMARY KEY(id);
ALTER TABLE hermes_road_name ADD CONSTRAINT hermes_road_name_pk PRIMARY KEY(id);
ALTER TABLE hermes_vertical_position ADD CONSTRAINT hermes_vertical_position_pk PRIMARY KEY(id);
ALTER TABLE hermes_form_of_way ADD CONSTRAINT hermes_form_of_way_pk PRIMARY KEY(id);

ALTER TABLE hermes_transport_node ADD CONSTRAINT hermes_transport_node_fk_network_element 
	FOREIGN KEY (network_element_id) REFERENCES hermes_network_element(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link ADD CONSTRAINT hermes_transport_link_fk_network_element 
	FOREIGN KEY (network_element_id) REFERENCES hermes_network_element(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link_sequence_transport_link ADD CONSTRAINT htlstl_fk_tls
	FOREIGN KEY (link_sequence_id) REFERENCES hermes_transport_link_sequence(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link_sequence_transport_link ADD CONSTRAINT htlstl_fk_tl
	FOREIGN KEY (link_sequence_id) REFERENCES hermes_transport_link(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link_set ADD CONSTRAINT hermes_transport_link_set_fk_network_element 
	FOREIGN KEY (network_element_id) REFERENCES hermes_network_element(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link_set_element ADD CONSTRAINT htlse_fk_tlset 
	FOREIGN KEY (link_set_id) REFERENCES hermes_transport_link_set(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link_set_element ADD CONSTRAINT htlse_fk_tl 
	FOREIGN KEY (link_id) REFERENCES hermes_transport_link(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link_set_element ADD CONSTRAINT htlse_fk_tlseq 
	FOREIGN KEY (link_sequence_id) REFERENCES hermes_transport_link_sequence(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_network_reference ADD CONSTRAINT hermes_network_reference_fk_network_element 
	FOREIGN KEY (network_element_id) REFERENCES hermes_network_element(id) 	
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_network_reference_transport_property ADD CONSTRAINT hnrtp_fk_nr
	FOREIGN KEY (network_reference_id) REFERENCES hermes_network_reference(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_network_reference_transport_property ADD CONSTRAINT hnrtp_fk_tp
	FOREIGN KEY (transport_property_id) REFERENCES hermes_transport_property(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_number_of_lanes ADD CONSTRAINT hermes_number_of_lanes_fk_transport_property 
	FOREIGN KEY (id) REFERENCES hermes_transport_property(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_road_name ADD CONSTRAINT hermes_road_name_fk_transport_property 
	FOREIGN KEY (id) REFERENCES hermes_transport_property(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_vertical_position ADD CONSTRAINT hermes_vertical_position_fk_transport_property 
	FOREIGN KEY (id) REFERENCES hermes_transport_property(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_form_of_way ADD CONSTRAINT hermes_form_of_way_fk_transport_property 
	FOREIGN KEY (id) REFERENCES hermes_transport_property(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;


-- De momento, estas vistas son sólo usadas para depuración:
DROP VIEW IF EXISTS h_node;
CREATE OR REPLACE VIEW h_node AS 
	SELECT htn.id node_id,
		htn."geometry" node_geometry, 
		hne.id e_id,
		hne.osm_id osm_id

	FROM hermes_transport_node htn
		JOIN hermes_network_element hne ON hne.id = htn.network_element_id;

DROP VIEW IF EXISTS h_link;
CREATE OR REPLACE VIEW h_link AS 
	SELECT htl.id link_id,
		htl.centerline_geometry link_geometry, 
		htl.ficticious, 
		hne.id e_id,
		hne.osm_id osm_id,
		s.node_id s_id,
		s.node_geometry s_geometry,
		s.e_id s_e_id,
		s.osm_id s_osm_id,
		t.node_id t_id,
		t.node_geometry t_geometry,
		t.e_id t_e_id,
		t.osm_id t_osm_id

	FROM hermes_transport_link htl
		JOIN hermes_network_element hne ON hne.id = htl.network_element_id
		JOIN h_node s ON s.node_id = htl.start_node
		JOIN h_node t ON t.node_id = htl.end_node;

DROP VIEW IF EXISTS h_link_set;
CREATE OR REPLACE VIEW h_link_set AS 
	SELECT htlse.id id,
		htlse.type "type",
		htlse."order" "order",
		htls.id link_set_id,
		hne.id link_set_e_id,
		hne.osm_id link_set_osm_id,
		h_link.link_id,
		link_geometry,
		ficticious link_ficticious,
		e_id link_e_id,
		h_link.osm_id link_osm_id,
		s_id,
		s_geometry,
		s_e_id,
		s_osm_id,
		t_id,
		t_geometry,
		t_e_id,
		t_osm_id,
		hnr.id ref_id,
		hnr.atPosition ref_atPosition,
		hnr.fromPosition ref_fromPosition,
		hnr.toPosition ref_toPosition,
		hnr."offset" ref_offset,
		(SELECT hstore(ARRAY['name', 'numberOfLanes', 'direction', 'minMaxNumberOfLanes', 'verticalPosition', 'formOfWay'], 
					ARRAY[first(name), first(numberOfLanes)::text, first(direction), first(minMaxNumberOfLanes)::text, first(verticalPosition), first(formOfWay)]) 
				FROM hermes_network_reference_transport_property hnrtp 
				LEFT JOIN hermes_number_of_lanes hnol ON hnol.id = hnrtp.transport_property_id
				LEFT JOIN hermes_road_name hrn ON hrn.id = hnrtp.transport_property_id
				LEFT JOIN hermes_vertical_position hvp ON hvp.id = hnrtp.transport_property_id
				LEFT JOIN hermes_form_of_way hfow ON hfow.id = hnrtp.transport_property_id
				WHERE hnrtp.network_reference_id = hnr.id) props

	FROM hermes_transport_link_set_element htlse 
		JOIN hermes_transport_link_set htls ON htls.id = htlse.link_set_id 
		JOIN hermes_network_element hne ON hne.id = htls.network_element_id
		JOIN h_link ON h_link.link_id = htlse.link_id
		LEFT JOIN hermes_network_reference hnr ON hnr.network_element_id = hne.id;
