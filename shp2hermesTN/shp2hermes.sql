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
	id bigint,
	"geometry" geometry(Point,25830)
);

DROP TABLE IF EXISTS hermes_transport_link CASCADE;
CREATE TABLE hermes_transport_link (
	id bigint,
	centerline_geometry geometry(LineString,25830),
	ficticious boolean NOT NULL DEFAULT false,
	start_node bigint,
	end_node bigint
);

DROP TABLE IF EXISTS hermes_transport_link_sequence CASCADE;
CREATE TABLE hermes_transport_link_sequence (
	id bigint 
);

DROP TABLE IF EXISTS hermes_transport_link_sequence_transport_link CASCADE;
CREATE TABLE hermes_transport_link_sequence_transport_link (
	link_sequence_id bigint,
	link_id bigint,
	"order" integer,
	direction varchar(255)
);

DROP TABLE IF EXISTS hermes_transport_link_set CASCADE;
CREATE TABLE hermes_transport_link_set (
	id bigint
);

DROP TABLE IF EXISTS hermes_transport_link_set_element CASCADE;
CREATE TABLE hermes_transport_link_set_element (
	id bigint,
	link_set_id bigint NOT NULL,
	link_id bigint,
	link_sequence_id bigint,
	type varchar(255),
	"order" integer
);

-- Estos inserts dependen totalmente del orden en el que se insertan los datos para conservar correctamente las relaciones.
CREATE TEMP SEQUENCE tmp_hermes_network_element_id;

DROP MATERIALIZED VIEW IF EXISTS es_avi_intersections CASCADE;
CREATE MATERIALIZED VIEW es_avi_intersections AS 
	SELECT id, street, round(ST_Line_Locate_Point(street, node)::numeric, 3) node 
	FROM (SELECT s.gid AS id, s.geom AS street, (ST_Dump(ST_Intersection(s.geom, i.geom))).geom AS node 
		FROM es_avi_streets s 
			JOIN es_avi_streets i ON ST_Intersects(s.geom, i.geom)) ways 
	WHERE GeometryType(ways.node) = 'POINT';

DROP MATERIALIZED VIEW IF EXISTS es_avi_links CASCADE;
CREATE MATERIALIZED VIEW es_avi_links AS 
	SELECT id AS street_id, 2 + (SELECT count(*) FROM es_avi_intersections WHERE id = i.id AND node < i.node AND node > 0) AS "order", ST_Line_Substring(
		street, node, (SELECT COALESCE(min(node), 1) FROM es_avi_intersections WHERE id = i.id AND node > i.node)) AS geom 
	FROM es_avi_intersections i
	WHERE node > 0 AND node < 1
	UNION SELECT id, 1, ST_Line_Substring(street, 0, (SELECT COALESCE(min(node), 1) FROM es_avi_intersections WHERE id = i.id AND node > 0)) FROM es_avi_intersections i 
	UNION SELECT gid, 1, geom FROM es_avi_streets WHERE gid NOT IN (SELECT id FROM es_avi_intersections);

INSERT INTO hermes_network_element (osm_id, type) SELECT gid, 'TransportLinkSequence' FROM es_avi_streets;
INSERT INTO hermes_transport_link_sequence (id) SELECT id FROM hermes_network_element;

SELECT setval('tmp_hermes_network_element_id', currval('hermes_network_element_id_seq')); -- Evitarme un join
INSERT INTO hermes_transport_link_sequence_transport_link (link_sequence_id, link_id, "order") SELECT street_id, nextval('tmp_hermes_network_element_id'), "order" FROM es_avi_links;
INSERT INTO hermes_transport_link (id, centerline_geometry) 
	SELECT htlstl.link_id, eal.geom 
	FROM es_avi_links eal 
		JOIN hermes_transport_link_sequence_transport_link htlstl ON htlstl.link_sequence_id = eal.street_id AND htlstl."order" = eal."order";
INSERT INTO hermes_network_element (type) SELECT 'TransportLink' FROM hermes_transport_link;

DROP TABLE IF EXISTS hermes_transport_link_vertices_pgr;
SELECT pgr_createTopology('hermes_transport_link', 1, 'centerline_geometry', source:='start_node', target:='end_node');
UPDATE hermes_transport_link SET start_node = currval('hermes_network_element_id_seq') + start_node, end_node = currval('hermes_network_element_id_seq') + end_node;
INSERT INTO hermes_transport_node (id, geometry) SELECT currval('hermes_network_element_id_seq') + id, the_geom FROM hermes_transport_link_vertices_pgr;
INSERT INTO hermes_network_element (type) SELECT 'TransportNode' FROM hermes_transport_node;
DROP TABLE hermes_transport_link_vertices_pgr;

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

-- TODO fks
