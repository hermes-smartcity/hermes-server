
-- HERMES TRANSPORT NETWORK:

DROP TABLE IF EXISTS hermes_network_element CASCADE;
CREATE TABLE hermes_network_element (
	id bigserial,
	osm_id bigint,		-- Maybe someday this won't be needed anymore
	type varchar(255)
);

DROP TABLE IF EXISTS hermes_transport_node CASCADE;
CREATE TABLE hermes_transport_node (
	id bigint,
	"geometry" geometry(Point)
);

DROP TABLE IF EXISTS hermes_transport_link CASCADE;
CREATE TABLE hermes_transport_link (
	id bigint,
	centerline_geometry geometry(LineString),
	ficticious boolean NOT NULL DEFAULT false,
	start_node bigint,
	end_node bigint
);

DROP TABLE IF EXISTS hermes_transport_link_sequence CASCADE;
CREATE TABLE hermes_transport_link_sequence (
	id bigint 
	-- TODO more data?
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
	-- TODO more data?
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

-- HERMES TRANSPORT NETWORK PKS: 

ALTER TABLE hermes_network_element ADD CONSTRAINT hermes_network_element_pk PRIMARY KEY(id);
ALTER TABLE hermes_transport_node ADD CONSTRAINT hermes_transport_node_pk PRIMARY KEY(id);
ALTER TABLE hermes_transport_link ADD CONSTRAINT hermes_transport_link_pk PRIMARY KEY(id);
ALTER TABLE hermes_transport_link_sequence ADD CONSTRAINT hermes_transport_link_sequence_pk PRIMARY KEY(id);
ALTER TABLE hermes_transport_link_sequence_transport_link ADD CONSTRAINT hermes_transport_link_sequence_transport_link_pk PRIMARY KEY(link_sequence_id, link_id);
ALTER TABLE hermes_transport_link_set ADD CONSTRAINT hermes_transport_link_set_pk PRIMARY KEY(id);
ALTER TABLE hermes_transport_link_set_element ADD CONSTRAINT hermes_transport_link_set_element_pk PRIMARY KEY(id);


-- HERMES TRANSPORT PROPERTIES:

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

-- HERMES TRANSPORT PROPERTIES PKS:

ALTER TABLE hermes_network_reference ADD CONSTRAINT hermes_network_reference_pk PRIMARY KEY(id);
ALTER TABLE hermes_transport_property ADD CONSTRAINT hermes_transport_property_pk PRIMARY KEY(id);
ALTER TABLE hermes_network_reference_transport_property ADD CONSTRAINT hnrtp_pk PRIMARY KEY(network_reference_id, transport_property_id);
ALTER TABLE hermes_number_of_lanes ADD CONSTRAINT hermes_number_of_lanes_pk PRIMARY KEY(id);
ALTER TABLE hermes_road_name ADD CONSTRAINT hermes_road_name_pk PRIMARY KEY(id);
ALTER TABLE hermes_vertical_position ADD CONSTRAINT hermes_vertical_position_pk PRIMARY KEY(id);
ALTER TABLE hermes_form_of_way ADD CONSTRAINT hermes_form_of_way_pk PRIMARY KEY(id);


-- GLOBAL FKS:

ALTER TABLE hermes_transport_node ADD CONSTRAINT hermes_transport_node_fk_network_element 
	FOREIGN KEY (id) REFERENCES hermes_network_element(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link ADD CONSTRAINT hermes_transport_link_fk_network_element 
	FOREIGN KEY (id) REFERENCES hermes_network_element(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link ADD CONSTRAINT hermes_transport_link_fk_start_node 
	FOREIGN KEY (start_node) REFERENCES hermes_transport_node(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link ADD CONSTRAINT hermes_transport_link_fk_end_node 
	FOREIGN KEY (end_node) REFERENCES hermes_transport_node(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link_sequence ADD CONSTRAINT htls_fk_ne 
	FOREIGN KEY (id) REFERENCES hermes_network_element(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link_sequence_transport_link ADD CONSTRAINT htlstl_fk_tls
	FOREIGN KEY (link_sequence_id) REFERENCES hermes_transport_link_sequence(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link_sequence_transport_link ADD CONSTRAINT htlstl_fk_tl
	FOREIGN KEY (link_id) REFERENCES hermes_transport_link(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE hermes_transport_link_set ADD CONSTRAINT hermes_transport_link_set_fk_network_element 
	FOREIGN KEY (id) REFERENCES hermes_network_element(id) 
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


-- GLOBAL INDEXES:

CREATE INDEX hermes_network_element_i_osm_id 
	ON hermes_network_element(osm_id);
CREATE INDEX hermes_network_reference_i_network_element_id 
	ON hermes_network_reference(network_element_id);
CREATE INDEX hermes_transport_link_i_centerline_geometry 
  ON hermes_transport_link USING gist (centerline_geometry);
CREATE INDEX hermes_transport_link_i_start_node 
  ON hermes_transport_link (start_node);
CREATE INDEX hermes_transport_link_i_end_node 
  ON hermes_transport_link (end_node);
CREATE INDEX hermes_transport_node_i_geometry  
  ON hermes_transport_node USING gist (geometry);


-- UTILITY FUNCTIONS:

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


-- VIEWS FOR DEBUGING:

DROP VIEW IF EXISTS h_node;
CREATE OR REPLACE VIEW h_node AS 
	SELECT htn.id id,
		hne.osm_id osm_id, 
		htn."geometry" node_geometry

	FROM hermes_transport_node htn
		JOIN hermes_network_element hne ON hne.id = htn.id;

DROP VIEW IF EXISTS h_link;
CREATE OR REPLACE VIEW h_link AS 
	SELECT htl.id id,
		hne.osm_id osm_id,
		htl.centerline_geometry link_geometry, 
		htl.ficticious, 
		s.id s_id,
		s.osm_id s_osm_id,
		s.node_geometry s_geometry,
		t.id t_id,
		t.osm_id t_osm_id, 
		t.node_geometry t_geometry

	FROM hermes_transport_link htl
		JOIN hermes_network_element hne ON hne.id = htl.id
		JOIN h_node s ON s.id = htl.start_node
		JOIN h_node t ON t.id = htl.end_node;

DROP VIEW IF EXISTS h_link_seq;
CREATE OR REPLACE VIEW h_link_seq AS 
	SELECT hne.id seq_id,
		hne.osm_id seq_osm_id,
		h_link.id link_id,
		h_link.osm_id link_osm_id, 
		"order" link_order,
		direction link_direction,
		link_geometry,
		ficticious link_ficticious,
		s_id,
		s_osm_id,
		s_geometry,
		t_id,
		t_osm_id,
		t_geometry,
		hnr.id ref_id,
		hnr.atPosition ref_atPosition,
		hnr.fromPosition ref_fromPosition,
		hnr.toPosition ref_toPosition,
		hnr."offset" ref_offset,

		(SELECT first(name) 
			FROM hermes_road_name hrn 
				JOIN hermes_network_reference_transport_property hnrtp ON hnrtp.transport_property_id = hrn.id 
			WHERE hnrtp.network_reference_id = hnr.id) AS name,
		(SELECT array_agg(numberOfLanes) 
			FROM hermes_number_of_lanes hnol
				JOIN hermes_network_reference_transport_property hnrtp ON hnrtp.transport_property_id = hnol.id 
			WHERE hnrtp.network_reference_id = hnr.id) AS numberOfLanes,
		(SELECT first(verticalPosition) 
			FROM hermes_vertical_position hvp 
				JOIN hermes_network_reference_transport_property hnrtp ON hnrtp.transport_property_id = hvp.id 
			WHERE hnrtp.network_reference_id = hnr.id) AS verticalPosition,
		(SELECT array_agg(formOfWay) 
			FROM hermes_form_of_way hfow
				JOIN hermes_network_reference_transport_property hnrtp ON hnrtp.transport_property_id = hfow.id 
			WHERE hnrtp.network_reference_id = hnr.id) AS formOfWay

	FROM h_link 
		JOIN hermes_transport_link_sequence_transport_link htlstl ON htlstl.link_id = h_link.id
		JOIN hermes_network_element hne ON hne.id = htlstl.link_sequence_id 
		LEFT JOIN hermes_network_reference hnr ON hnr.network_element_id = hne.id;
