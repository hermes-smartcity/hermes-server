
-- p: starting point
-- h: triangle's height from p
-- a: azimuth of one of the sides from p in grades
-- b: angle between the two sides of p in grades. Negative means counter-clockwise
CREATE OR REPLACE FUNCTION makeTriangle(p Geometry(Point), h float, a float, b float) RETURNS Geometry AS $$
DECLARE
	b float := radians(b);
	a float := -radians(a-90);
	hi float := h*cos(b/2); -- length of the sides of p, or the hipothenuse of the semi-triangles if divided by its height
BEGIN
	RETURN ST_MakePolygon(ST_MakeLine(ARRAY[p, ST_Translate(p, hi*cos(a), hi*sin(a)), ST_Translate(p, hi*cos(a+b), hi*sin(a+b))::Geometry, p]));
END;
$$ LANGUAGE plpgsql;


-- Traffic signs:
DROP VIEW IF EXISTS h_traffic_sign;
CREATE OR REPLACE VIEW h_traffic_sign AS
	SELECT gid,
		tipo,
		geom,
		centroidez,
		ref_id, 
		ref_pos, 
		ref_dist, 
		ref_type, 
		ref_score 
	FROM es_avi_signs eas,
		LATERAL (
			(SELECT id ref_id, 
				ST_LineLocatePoint(htl.centerline_geometry, ST_ClosestPoint(htl.centerline_geometry, eas.geom)) ref_pos,
				ST_Distance(eas.geom, htl.centerline_geometry) ref_dist, 
				'origin' ref_type, 
				ST_Distance(eas.geom, centerline_geometry) -- Close
					+ abs(compareSlope(degrees(ST_Azimuth(ST_StartPoint(centerline_geometry), ST_EndPoint(centerline_geometry))), azimut)) -- With a parallel slope
					+ abs(normalizeAngle(degrees(ST_Azimuth(eas.geom, ST_ClosestPoint(centerline_geometry, eas.geom))) - azimut) - 90) -- To the left of the sign
					ref_score
			FROM hermes_transport_link htl
			WHERE tipo = 'R101' AND ST_Intersects(makeTriangle(eas.geom, 50, azimut+135, 90), centerline_geometry)
			ORDER BY ref_score
			LIMIT 1)
			UNION ALL
			(SELECT id ref_id, 
				ST_LineLocatePoint(htl.centerline_geometry, ST_ClosestPoint(htl.centerline_geometry, eas.geom)) ref_pos,
				ST_Distance(eas.geom, htl.centerline_geometry) ref_dist, 
				'origin' ref_type, 
				ST_Distance(eas.geom, centerline_geometry) -- Close
					+ abs(compareSlope(degrees(ST_Azimuth(ST_StartPoint(centerline_geometry), ST_EndPoint(centerline_geometry))), azimut)) -- With a parallel slope
					ref_score
			FROM hermes_transport_link htl
			WHERE tipo = 'R302' AND ST_Intersects(makeTriangle(eas.geom, 50, azimut-45, 90), centerline_geometry)
			ORDER BY ref_score
			LIMIT 1)
			UNION ALL
			(SELECT id ref_id, 
				ST_LineLocatePoint(htl.centerline_geometry, ST_ClosestPoint(htl.centerline_geometry, eas.geom)) ref_pos,
				ST_Distance(eas.geom, htl.centerline_geometry) ref_dist, 
				'dest1' ref_type, 
				ST_Distance(eas.geom, centerline_geometry) -- Close
					- abs(compareSlope(degrees(ST_Azimuth(ST_StartPoint(centerline_geometry), ST_EndPoint(centerline_geometry))), azimut)) -- With a perpendicular slope
					--+ abs(compareSlope(degrees(ST_Azimuth(eas.geom, ST_Centroid(centerline_geometry))), azimut) - 90) -- To the right of the sign
					ref_score
				
			FROM hermes_transport_link htl
			WHERE tipo = 'R302' AND ST_Intersects(makeTriangle(eas.geom, 50, azimut+180, 90), centerline_geometry)
			ORDER BY ref_score
			LIMIT 1)
			UNION ALL
			(SELECT hne.id ref_id, 
				ST_LineLocatePoint(streets.geom, eas.geom) ref_pos,
				ST_Distance(eas.geom, htl.centerline_geometry) ref_dist, 
				'origin' ref_type, 
				ST_Distance(eas.geom, htl.centerline_geometry) -- Close
					+ abs(compareSlope(degrees(ST_Azimuth(ST_StartPoint(htl.centerline_geometry), ST_EndPoint(htl.centerline_geometry))), eas.azimut)) -- With a parallel slope
					ref_score
				
			FROM hermes_transport_link htl
				JOIN hermes_transport_link_sequence_transport_link htlstl ON htlstl.link_id = htl.id
				JOIN hermes_network_element hne ON hne.id = htlstl.link_sequence_id
				JOIN es_avi_streets streets ON streets.gid = hne.osm_id
			WHERE tipo = 'R301-30' AND ST_Intersects(makeTriangle(eas.geom, 50, azimut+135, 90), centerline_geometry)
			ORDER BY ref_score
			LIMIT 1)
		) ref;

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