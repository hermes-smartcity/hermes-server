
-- Estos inserts dependen totalmente del orden en el que se insertan los datos para conservar correctamente las relaciones.
CREATE TEMP SEQUENCE tmp_hermes_network_element_id;

-- Materialized views for efficiency and debugging

-- Intersections with every street with another or with itself:
DROP MATERIALIZED VIEW IF EXISTS es_avi_intersections CASCADE;
CREATE MATERIALIZED VIEW es_avi_intersections AS 
	SELECT id, street, round(ST_Line_Locate_Point(street, node)::numeric, 3) node -- FIXME this rounding is a poor way to bypass problems that bad input data originate
	FROM (SELECT s.gid AS id, s.geom AS street, (ST_Dump(ST_Intersection(s.geom, i.geom))).geom AS node 
		FROM es_avi_streets s 
			JOIN es_avi_streets i ON ST_Intersects(s.geom, i.geom)) ways 
	WHERE GeometryType(ways.node) = 'POINT';

-- All the streets splitted into links:
DROP MATERIALIZED VIEW IF EXISTS es_avi_links CASCADE;
CREATE MATERIALIZED VIEW es_avi_links AS 
	SELECT id AS street_id, 2 + (SELECT count(*) FROM es_avi_intersections WHERE id = i.id AND node < i.node AND node > 0) AS "order", ST_Line_Substring(
		street, node, (SELECT COALESCE(min(node), 1) FROM es_avi_intersections WHERE id = i.id AND node > i.node)) AS geom 
	FROM es_avi_intersections i
	WHERE node > 0 AND node < 1
	UNION SELECT id, 1, ST_Line_Substring(street, 0, (SELECT COALESCE(min(node), 1) FROM es_avi_intersections WHERE id = i.id AND node > 0)) FROM es_avi_intersections i 
	UNION SELECT gid, 1, geom FROM es_avi_streets WHERE gid NOT IN (SELECT id FROM es_avi_intersections);

-- Map each street to a Transport Link Sequence:
INSERT INTO hermes_network_element (osm_id, type) SELECT gid, 'TransportLinkSequence' FROM es_avi_streets;
INSERT INTO hermes_transport_link_sequence (id) SELECT id FROM hermes_network_element;

-- Map each splitted link to a Transport Link within a Sequence:
SELECT setval('tmp_hermes_network_element_id', currval('hermes_network_element_id_seq')); -- Evitarme un join
INSERT INTO hermes_network_element (type) SELECT 'TransportLink' FROM es_avi_links;
INSERT INTO hermes_transport_link (id) SELECT id FROM hermes_network_element WHERE id > currval('tmp_hermes_network_element_id');
INSERT INTO hermes_transport_link_sequence_transport_link (link_sequence_id, link_id, "order") SELECT hne.id, nextval('tmp_hermes_network_element_id'), "order" 
	FROM es_avi_links eal
		JOIN hermes_network_element hne ON hne.type = 'TransportLinkSequence' AND hne.osm_id = eal.street_id;
UPDATE hermes_transport_link htl SET centerline_geometry = (
	SELECT eal.geom 
	FROM es_avi_links eal 
		JOIN hermes_network_element hne ON hne.type = 'TransportLinkSequence' AND hne.osm_id = eal.street_id
		JOIN hermes_transport_link_sequence_transport_link htlstl ON htlstl.link_sequence_id = hne.id AND htlstl."order" = eal."order"
	WHERE htlstl.link_id = htl.id);

-- Create a topology with Transport Nodes for our Links:
INSERT INTO hermes_network_element (type) SELECT 'TransportNode' FROM generate_series(1, (SELECT count(*)*2 FROM hermes_transport_link));  -- Dirty fix to comply with the foreign keys during pgr_createTopology
INSERT INTO hermes_transport_node (id) SELECT id FROM hermes_network_element;
DROP TABLE IF EXISTS hermes_transport_link_vertices_pgr CASCADE;
SELECT pgr_createTopology('hermes_transport_link', 1, 'centerline_geometry', source:='start_node', target:='end_node');
UPDATE hermes_transport_link SET start_node = currval('tmp_hermes_network_element_id') + start_node, end_node = currval('tmp_hermes_network_element_id') + end_node;
UPDATE hermes_transport_node htn SET geometry = (SELECT the_geom FROM hermes_transport_link_vertices_pgr WHERE id + currval('tmp_hermes_network_element_id') = htn.id);
DELETE FROM hermes_transport_node WHERE geometry IS NULL;
DELETE FROM hermes_network_element WHERE id > (SELECT max(id) FROM hermes_transport_node);
DROP TABLE hermes_transport_link_vertices_pgr CASCADE;

DROP SEQUENCE tmp_hermes_network_element_id;

-- TEMP: 
SELECT pgr_createVerticesTable('hermes_transport_link', 'centerline_geometry', source:='start_node', target:='end_node');
SELECT pgr_analyzeGraph('hermes_transport_link', 1.5, 'centerline_geometry', source:='start_node', target:='end_node');

-- Nodes that are (probably) in a gap:
DROP VIEW IF EXISTS bad_nodes;
CREATE OR REPLACE VIEW bad_nodes AS (SELECT id, the_geom FROM hermes_transport_link_vertices_pgr WHERE chk > 0);

-- Streets that intersect themselves in their definition:
DROP VIEW IF EXISTS bad_streets;
CREATE OR REPLACE VIEW bad_streets AS
	SELECT gid, geom FROM es_avi_streets WHERE NOT ST_IsSimple(geom);

-- Self intersection points of the above streets:
DROP VIEW IF EXISTS self_intersections;
CREATE OR REPLACE VIEW self_intersections AS
	SELECT row_number() OVER (), geom
	FROM (SELECT (ST_Dump(ST_Intersection(w1.geom, w2.geom))).geom geom
		FROM (SELECT gid, (ST_Dump(ST_Node(geom))).geom geom FROM es_avi_streets WHERE NOT ST_IsSimple(geom)) w1
		JOIN (SELECT gid, (ST_Dump(ST_Node(geom))).geom geom FROM es_avi_streets WHERE NOT ST_IsSimple(geom)) w2 ON w1.gid = w2.gid) intersections
	WHERE GeometryType(geom) = 'POINT'
	GROUP BY geom;

-- Useless links that are covered by others:
DROP VIEW IF EXISTS bad_links;
CREATE OR REPLACE VIEW bad_links AS 
	SELECT l2.id, l2.centerline_geometry
	FROM hermes_transport_link l1
		JOIN hermes_transport_link l2 ON l1.id != l2.id AND ST_Covers(l1.centerline_geometry, l2.centerline_geometry);

