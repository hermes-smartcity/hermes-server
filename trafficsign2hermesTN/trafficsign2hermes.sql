-- Traffic signs:
-- TODO Do we need TransportLinkSets for this?
INSERT INTO hermes_network_element (osm_id, type) SELECT htl.id, 'TransportLinkSet'
	FROM es_avi_signs eas, 
		LATERAL (SELECT id FROM hermes_transport_link WHERE (eas.geom <#> centerline_geometry) < 20 LIMIT 1) htl 
	WHERE tipo = 'R101'; -- Dirección prohibida
INSERT INTO hermes_transport_link_set (id) SELECT id FROM hermes_network_element WHERE type = 'TransportLinkSet';
INSERT INTO hermes_transport_link_set_element (link_set_id, link_id, type, "order") SELECT id, osm_id, 'TransportLink', 1 
	FROM hermes_network_element WHERE type = 'TransportLinkSet';

-- TODO Do we need TransportNodes for this?
INSERT INTO hermes_network_element (osm_id, type) SELECT gid, 'TransportNode' FROM es_avi_signs WHERE tipo = 'R101';
INSERT INTO hermes_transport_node (id, geometry) SELECT hne.id, eas.geom 
	FROM es_avi_signs eas
		JOIN hermes_network_element hne ON hne.osm_id = eas.gid 
	WHERE eas.tipo = 'R101';

INSERT INTO hermes_network_reference (network_element_id, type, atPosition, "offset") 
	SELECT htl.id, 'SimplePointReference', 
		ST_LineLocatePoint(htl.centerline_geometry, ST_ClosestPoint(htl.centerline_geometry, eas.geom)), 
		eas.geom <#> htl.centerline_geometry 
	FROM es_avi_signs eas, 
		LATERAL (SELECT id, centerline_geometry 
			FROM hermes_transport_link
			WHERE (eas.geom <#> centerline_geometry) < 20
			ORDER BY (eas.geom <#> centerline_geometry) + abs(compareSlope(degrees(ST_Azimuth(ST_StartPoint(centerline_geometry), ST_EndPoint(centerline_geometry))), azimut))
			LIMIT 1) htl
	WHERE tipo = 'R101'; -- Dirección prohibida;
-- TODO optimize
INSERT INTO hermes_network_reference (network_element_id, type, fromPosition, toPosition) 
	SELECT network_element_id, 'SimpleLinearReference', atPosition, 
		CASE WHEN atPosition > 0.5 THEN 0 ELSE 1 END 
	FROM hermes_network_reference hnr 
	WHERE type = 'SimplePointReference' 
		AND NOT EXISTS(SELECT 1 FROM hermes_traffic_information WHERE position = hnr.id);
-- TODO this is getting redundant...
INSERT INTO hermes_traffic_information (link_set_id, position, effect, class, function, usage, trafficGroup, lod0Point)
	SELECT link_set_id, pos_id, eff_id,  -- link set, position, effect
		'1000', ARRAY['1070'], '1070', tipo, eas.geom --traffic, road sign, road sign, sign type, location
	FROM es_avi_signs eas, 
		LATERAL (SELECT htlse.link_set_id, pos.id pos_id, eff.id eff_id
			FROM hermes_transport_link htl
				JOIN hermes_transport_link_set_element htlse ON htlse.link_id = htl.id
				JOIN hermes_network_reference pos ON pos.type = 'SimplePointReference' AND pos.network_element_id = htl.id
				JOIN hermes_network_reference eff ON eff.type = 'SimpleLinearReference' AND eff.network_element_id = htl.id
			WHERE (eas.geom <#> htl.centerline_geometry) < 20
			ORDER BY (eas.geom <#> centerline_geometry) + abs(compareSlope(degrees(ST_Azimuth(ST_StartPoint(centerline_geometry), ST_EndPoint(centerline_geometry))), azimut))
			LIMIT 1) links
	WHERE tipo = 'R101'; -- Dirección prohibida
INSERT INTO hermes_traffic_sign (id, height, orientation) SELECT hti.id, eas.centroidez, ARRAY[cos(azimut), -sin(azimut), sin(azimut), cos(azimut)] -- this matrix is pointless
	FROM es_avi_signs eas JOIN hermes_traffic_information hti ON hti.trafficGroup = eas.tipo AND hti.lod0Point = eas.geom
	WHERE tipo = 'R101'; -- Dirección prohibida
	