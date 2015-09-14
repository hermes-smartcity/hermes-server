
-- p: starting point
-- h: triangle's height from p
-- a: azimuth of height segment
-- b: angle of the height with the a side
-- b: angle of the height with the b side
CREATE OR REPLACE FUNCTION makeTriangle(p Geometry(Point), h float, a float, b float, c float) RETURNS Geometry AS $$
DECLARE
	a float := -radians(a-90);
	b float := radians(b);
	c float := -radians(c);
	hb float := h/cos(b); -- length of the b side
	hc float := h/cos(c); -- length of the c side
BEGIN
	RETURN ST_MakePolygon(ST_MakeLine(ARRAY[p, ST_Translate(p, hb*cos(a+b), hb*sin(a+b)), ST_Translate(p, hc*cos(a+c), hc*sin(a+c))::Geometry, p]));
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION makeSignTriangle(eas es_avi_signs, t varchar) RETURNS Geometry AS $$
	SELECT CASE
		WHEN t = 'reverse' THEN makeTriangle(eas.geom, 50, eas.azimut+180, 50, 10)
		WHEN t = 'forward' THEN makeTriangle(eas.geom, 50, eas.azimut, 20, 50)
		WHEN t = 'right' THEN makeTriangle(eas.geom, 50, eas.azimut+225, 45, 45)
		ELSE NULL END;
$$ LANGUAGE SQL;

CREATE OR REPLACE FUNCTION mapSign(eas es_avi_signs, t varchar) RETURNS 
	TABLE(gid int, tipo varchar(7), geom geometry(Point), centroidez double precision, ref_id bigint, ref_pos float, ref_dist float, ref_type varchar(255), ref_score float) AS $$
DECLARE
	triangle Geometry := (SELECT CASE
		WHEN eas.tipo = 'R101' AND t = 'origin' THEN makeSignTriangle(eas, 'reverse')
		WHEN eas.tipo = 'R302' AND t = 'origin' THEN makeSignTriangle(eas, 'forward')
		WHEN eas.tipo = 'R302' AND t = 'dest1' THEN makeSignTriangle(eas, 'right')
		WHEN eas.tipo = 'R301-30' AND t = 'origin' THEN makeSignTriangle(eas, 'reverse')
		ELSE NULL END);
BEGIN
	IF triangle IS NULL THEN RETURN;
	ELSE
		RETURN QUERY (
			SELECT eas.gid,
				eas.tipo,
				eas.geom,
				eas.centroidez, 
				htl.id ref_id, 
				ST_LineLocatePoint(htl.centerline_geometry, ST_ClosestPoint(htl.centerline_geometry, eas.geom)) ref_pos,
				ST_Distance(eas.geom, htl.centerline_geometry) ref_dist, 
				t ref_type, 
				CASE
					WHEN eas.tipo = 'R101' THEN 
						ST_Distance(eas.geom, htl.centerline_geometry) -- Close
							+ abs(compareSlope(degrees(ST_Azimuth(ST_StartPoint(htl.centerline_geometry), ST_EndPoint(htl.centerline_geometry))), eas.azimut)) -- With a parallel slope
							+ abs(normalizeAngle(degrees(ST_Azimuth(eas.geom, ST_ClosestPoint(htl.centerline_geometry, eas.geom))) - eas.azimut) - 90) -- To the right of the sign
					WHEN eas.tipo = 'R302' AND t = 'origin' THEN 
						ST_Distance(eas.geom, htl.centerline_geometry) -- Close
							+ abs(compareSlope(degrees(ST_Azimuth(ST_StartPoint(htl.centerline_geometry), ST_EndPoint(htl.centerline_geometry))), eas.azimut)) -- With a parallel slope
					WHEN eas.tipo = 'R302' AND t = 'dest1' THEN 
						ST_Distance(eas.geom, htl.centerline_geometry) -- Close
							+ abs(compareSlope(degrees(ST_Azimuth(ST_StartPoint(htl.centerline_geometry), ST_EndPoint(htl.centerline_geometry))) - 90, eas.azimut)) -- With a perpendicular slope
					WHEN eas.tipo = 'R301-30' THEN 
						ST_Distance(eas.geom, htl.centerline_geometry) -- Close
							+ abs(compareSlope(degrees(ST_Azimuth(ST_StartPoint(htl.centerline_geometry), ST_EndPoint(htl.centerline_geometry))), eas.azimut)) -- With a parallel slope
					ELSE NULL END ref_score
			FROM hermes_transport_link htl
			WHERE ST_Intersects(triangle, htl.centerline_geometry)
			ORDER BY ref_score
			LIMIT 1);
	END IF;
END;
$$ LANGUAGE plpgsql;

-- Traffic signs:
DROP VIEW IF EXISTS h_traffic_sign;
CREATE OR REPLACE VIEW h_traffic_sign AS
	SELECT (mapSign(eas, 'origin')).*
	FROM es_avi_signs eas
	WHERE eas.tipo != 'R301-30'
	UNION ALL
	SELECT (mapSign(eas, 'dest1')).*
	FROM es_avi_signs eas
	UNION ALL
	SELECT 
		m.gid,
		m.tipo,
		m.geom,
		m.centroidez,
		hne.id ref_id,
		ST_LineLocatePoint(streets.geom, eas.geom) ref_pos,
		m.ref_dist,
		m.ref_type,
		m.ref_score
	FROM es_avi_signs eas
		JOIN mapSign(eas, 'origin') m ON eas.tipo = 'R301-30' AND m.gid = eas.gid
		JOIN hermes_transport_link_sequence_transport_link htlstl ON htlstl.link_id = m.ref_id
		JOIN hermes_network_element hne ON hne.id = htlstl.link_sequence_id
		JOIN es_avi_streets streets ON streets.gid = hne.osm_id;



CREATE OR REPLACE FUNCTION importTrafficFlow() RETURNS void AS $$
DECLARE
	htr_0 bigint := (SELECT COALESCE(max(id), 0) FROM hermes_network_reference);
BEGIN
	INSERT INTO hermes_network_reference (network_element_id, type, atPosition, "offset") 
		SELECT ref_id, 'SimplePointReference', ref_pos, ref_dist FROM h_traffic_sign WHERE tipo = 'R101';
	INSERT INTO hermes_network_reference (network_element_id, type, fromPosition, toPosition) 
		SELECT ref_id, 'SimpleLinearReference', ref_pos, 
			CASE WHEN ref_pos > 0.5 THEN 0 ELSE 1 END
		FROM h_traffic_sign 
		WHERE tipo = 'R101';
	INSERT INTO hermes_traffic_information (position, effect, class, function, usage, trafficGroup, lod0Point)
		SELECT pos.id, eff.id, '1000', ARRAY['1070'], '1070', hts.tipo, hts.geom -- position, effect, traffic, road sign, road sign, sign type, location
		FROM h_traffic_sign hts 
			JOIN hermes_network_reference pos ON pos.type = 'SimplePointReference' AND pos.network_element_id = hts.ref_id
				AND pos.atPosition = hts.ref_pos AND pos.id > htr_0
			JOIN hermes_network_reference eff ON eff.type = 'SimpleLinearReference' AND eff.network_element_id = hts.ref_id 
				AND eff.fromPosition = hts.ref_pos AND eff.id > htr_0
		WHERE tipo = 'R101';
	INSERT INTO hermes_transport_property (type) SELECT 'TrafficFlowDirection' FROM h_traffic_sign WHERE tipo = 'R101';
	INSERT INTO hermes_network_reference_transport_property(network_reference_id, transport_property_id) SELECT id, nextval('tmp_hermes_transport_property_id') 
		FROM hermes_network_reference WHERE type = 'SimpleLinearReference' AND id > htr_0; 
	INSERT INTO hermes_traffic_flow_direction (id, direction) SELECT hnrtp.transport_property_id, CASE 
			WHEN hnr.fromPosition > hnr.toPosition THEN 'inDirection'
			ELSE 'inOppositeDirection' END
		FROM hermes_network_reference_transport_property hnrtp
			JOIN hermes_network_reference hnr ON hnr.id = hnrtp.network_reference_id
		WHERE network_reference_id > htr_0;
END;
$$ LANGUAGE plpgsql;

-- TODO refactor redundant code
CREATE OR REPLACE FUNCTION importSpeedLimit() RETURNS void AS $$
DECLARE
	htr_0 bigint := (SELECT COALESCE(max(id), 0) FROM hermes_network_reference);
BEGIN
	INSERT INTO hermes_network_reference (network_element_id, type, atPosition, "offset") 
		SELECT ref_id, 'SimplePointReference', ref_pos, ref_dist FROM h_traffic_sign WHERE tipo = 'R301-30';
	INSERT INTO hermes_network_reference (network_element_id, type, fromPosition, toPosition) 
		SELECT ref_id, 'SimpleLinearReference', ref_pos, 
			CASE WHEN ref_pos > 0.5 THEN 0 ELSE 1 END
		FROM h_traffic_sign 
		WHERE tipo = 'R301-30';
	INSERT INTO hermes_traffic_information (position, effect, class, function, usage, trafficGroup, lod0Point)
		SELECT pos.id, eff.id, '1000', ARRAY['1070'], '1070', hts.tipo, hts.geom -- position, effect, traffic, road sign, road sign, sign type, location
		FROM h_traffic_sign hts 
			JOIN hermes_network_reference pos ON pos.type = 'SimplePointReference' AND pos.network_element_id = hts.ref_id
				AND pos.atPosition = hts.ref_pos AND pos.id > htr_0
			JOIN hermes_network_reference eff ON eff.type = 'SimpleLinearReference' AND eff.network_element_id = hts.ref_id 
				AND eff.fromPosition = hts.ref_pos AND eff.id > htr_0
		WHERE tipo = 'R301-30';
	INSERT INTO hermes_transport_property (type) SELECT 'SpeedLimit' FROM h_traffic_sign WHERE tipo = 'R301-30';
	INSERT INTO hermes_network_reference_transport_property(network_reference_id, transport_property_id) SELECT id, nextval('tmp_hermes_transport_property_id') 
		FROM hermes_network_reference WHERE type = 'SimpleLinearReference' AND id > htr_0; 
	INSERT INTO hermes_speed_limit (id, speedLimitValue) SELECT hnrtp.transport_property_id, 30
		FROM hermes_network_reference_transport_property hnrtp
		WHERE network_reference_id > htr_0;
END;
$$ LANGUAGE plpgsql;

CREATE TEMP SEQUENCE tmp_hermes_transport_property_id;
SELECT setval('tmp_hermes_transport_property_id', (SELECT last_value FROM hermes_transport_property_id_seq), false);
SELECT importTrafficFlow();
SELECT importSpeedLimit();
INSERT INTO hermes_traffic_sign (id, height, orientation) SELECT hti.id, eas.centroidez, ARRAY[cos(azimut), -sin(azimut), sin(azimut), cos(azimut)] -- this matrix is pointless
	FROM es_avi_signs eas JOIN hermes_traffic_information hti ON hti.trafficGroup = eas.tipo AND hti.lod0Point = eas.geom;
DROP SEQUENCE tmp_hermes_transport_property_id;