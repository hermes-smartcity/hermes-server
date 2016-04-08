
-- HERMES TRANSPORT NETWORK:

DROP TABLE IF EXISTS network_element CASCADE;
CREATE TABLE network_element (
	id bigserial,
	osm_id bigint,		-- Maybe someday this won't be needed anymore
	type varchar(255)
);

DROP TABLE IF EXISTS transport_node CASCADE;
CREATE TABLE transport_node (
	id bigint,
	"geometry" geometry(Point)
);

DROP TABLE IF EXISTS transport_link CASCADE;
CREATE TABLE transport_link (
	id bigint,
	centerline_geometry geometry(LineString),
	ficticious boolean NOT NULL DEFAULT false,
	start_node bigint,
	end_node bigint,
	traffic_direction varchar(255) NOT NULL DEFAULT 'bothDirections',
	CONSTRAINT transport_link_pk PRIMARY KEY(id)
);

DROP TABLE IF EXISTS transport_link_sequence CASCADE;
CREATE TABLE transport_link_sequence (
	id bigint 
	-- TODO more data?
);

DROP TABLE IF EXISTS transport_link_sequence_transport_link CASCADE;
CREATE TABLE transport_link_sequence_transport_link (
	link_sequence_id bigint,
	link_id bigint,
	"order" integer,
	direction varchar(255)
);
DROP TABLE IF EXISTS transport_link_set CASCADE;
CREATE TABLE transport_link_set (
	id bigint
	-- TODO more data?
);

DROP TABLE IF EXISTS transport_link_set_element CASCADE;
CREATE TABLE transport_link_set_element (
	id bigserial,
	link_set_id bigint NOT NULL,
	link_id bigint,
	link_sequence_id bigint,
	type varchar(255),
	"order" integer
);

-- Turn restrictions table for routing:
DROP TABLE IF EXISTS turn_restriction CASCADE;
CREATE TABLE turn_restriction (
	from_link bigint,
	to_link bigint,
	CONSTRAINT turn_restriction_pk PRIMARY KEY (from_link, to_link),
	CONSTRAINT turn_restriction_fk_from FOREIGN KEY (from_link)
		REFERENCES transport_link(id),
	CONSTRAINT turn_restriction_fk_to FOREIGN KEY (to_link)
		REFERENCES transport_link(id)
);

-- HERMES TRANSPORT NETWORK PKS: 

ALTER TABLE network_element ADD CONSTRAINT network_element_pk PRIMARY KEY(id);
ALTER TABLE transport_node ADD CONSTRAINT transport_node_pk PRIMARY KEY(id);
ALTER TABLE transport_link_sequence ADD CONSTRAINT transport_link_sequence_pk PRIMARY KEY(id);
ALTER TABLE transport_link_sequence_transport_link ADD CONSTRAINT transport_link_sequence_transport_link_pk PRIMARY KEY(link_sequence_id, link_id);
ALTER TABLE transport_link_set ADD CONSTRAINT transport_link_set_pk PRIMARY KEY(id);
ALTER TABLE transport_link_set_element ADD CONSTRAINT transport_link_set_element_pk PRIMARY KEY(id);

-- HERMES TRANSPORT PROPERTIES:

DROP TABLE IF EXISTS network_reference CASCADE;
CREATE TABLE network_reference (
	id bigserial,
	network_element_id bigint NOT NULL,
	type varchar(255),
	atPosition decimal,
	fromPosition decimal,
	toPosition decimal,
	"offset" decimal
);

DROP TABLE IF EXISTS transport_property CASCADE;
CREATE TABLE transport_property (
	id bigserial,
	type varchar(255)
);

DROP TABLE IF EXISTS network_reference_transport_property CASCADE;
CREATE TABLE network_reference_transport_property (
	network_reference_id bigint,
	transport_property_id bigint
);

DROP TABLE IF EXISTS number_of_lanes CASCADE;
CREATE TABLE number_of_lanes (
	id bigint NOT NULL,
	numberOfLanes integer NOT NULL,
	direction varchar(255),
	minMaxNumberOfLanes varchar(255)
);

DROP TABLE IF EXISTS road_name CASCADE;
CREATE TABLE road_name (
	id bigint NOT NULL,
	name varchar -- TODO Geographical Name (D2.8.1.3 INSPIRE Data Specification on Geographical names)
);

DROP TABLE IF EXISTS vertical_position CASCADE;
CREATE TABLE vertical_position (
	id bigint NOT NULL,
	verticalPosition varchar(255)
);

DROP TABLE IF EXISTS form_of_way CASCADE;
CREATE TABLE form_of_way (
	id bigint NOT NULL,
	formOfWay varchar(255)
);

-- Deprecated, use traffic_direction from transport_link instead
DROP TABLE IF EXISTS traffic_flow_direction CASCADE;
CREATE TABLE traffic_flow_direction (
	id bigint NOT NULL,
	direction varchar(255)
);

DROP TABLE IF EXISTS speed_limit CASCADE;
CREATE TABLE speed_limit (
	id bigint NOT NULL,
	speedLimitMinMaxType varchar(255) NOT NULL DEFAULT 'maximum',
	speedLimitValue numeric NOT NULL,
	areaCondicion varchar(255),
	direction varchar(255),
	laneExtension integer,
	speedLimitSource varchar(255),
	startLane integer,
	validityPeriod varchar(255),
	vehicleType varchar(255),
	weatherCondicion varchar(255)
);

-- HERMES TRANSPORT PROPERTIES PKS:

ALTER TABLE network_reference ADD CONSTRAINT network_reference_pk PRIMARY KEY(id);
ALTER TABLE transport_property ADD CONSTRAINT transport_property_pk PRIMARY KEY(id);

ALTER TABLE network_reference_transport_property ADD CONSTRAINT hnrtp_pk PRIMARY KEY(network_reference_id, transport_property_id);
ALTER TABLE number_of_lanes ADD CONSTRAINT number_of_lanes_pk PRIMARY KEY(id);
ALTER TABLE road_name ADD CONSTRAINT road_name_pk PRIMARY KEY(id);
ALTER TABLE vertical_position ADD CONSTRAINT vertical_position_pk PRIMARY KEY(id);
ALTER TABLE form_of_way ADD CONSTRAINT form_of_way_pk PRIMARY KEY(id);
ALTER TABLE traffic_flow_direction ADD CONSTRAINT traffic_flow_direction_pk PRIMARY KEY(id);
ALTER TABLE speed_limit ADD CONSTRAINT speed_limit_pk PRIMARY KEY(id);

-- GLOBAL FKS:

ALTER TABLE transport_node ADD CONSTRAINT transport_node_fk_network_element 
	FOREIGN KEY (id) REFERENCES network_element(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE transport_link ADD CONSTRAINT transport_link_fk_network_element 
	FOREIGN KEY (id) REFERENCES network_element(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE transport_link ADD CONSTRAINT transport_link_fk_start_node 
	FOREIGN KEY (start_node) REFERENCES transport_node(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE transport_link ADD CONSTRAINT transport_link_fk_end_node 
	FOREIGN KEY (end_node) REFERENCES transport_node(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE transport_link_sequence ADD CONSTRAINT htls_fk_ne 
	FOREIGN KEY (id) REFERENCES network_element(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE transport_link_sequence_transport_link ADD CONSTRAINT htlstl_fk_tls
	FOREIGN KEY (link_sequence_id) REFERENCES transport_link_sequence(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE transport_link_sequence_transport_link ADD CONSTRAINT htlstl_fk_tl
	FOREIGN KEY (link_id) REFERENCES transport_link(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE transport_link_set ADD CONSTRAINT transport_link_set_fk_network_element 
	FOREIGN KEY (id) REFERENCES network_element(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE transport_link_set_element ADD CONSTRAINT htlse_fk_tlset 
	FOREIGN KEY (link_set_id) REFERENCES transport_link_set(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE transport_link_set_element ADD CONSTRAINT htlse_fk_tl 
	FOREIGN KEY (link_id) REFERENCES transport_link(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE transport_link_set_element ADD CONSTRAINT htlse_fk_tlseq 
	FOREIGN KEY (link_sequence_id) REFERENCES transport_link_sequence(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE network_reference ADD CONSTRAINT network_reference_fk_network_element 
	FOREIGN KEY (network_element_id) REFERENCES network_element(id) 	
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE network_reference_transport_property ADD CONSTRAINT hnrtp_fk_nr
	FOREIGN KEY (network_reference_id) REFERENCES network_reference(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE network_reference_transport_property ADD CONSTRAINT hnrtp_fk_tp
	FOREIGN KEY (transport_property_id) REFERENCES transport_property(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE number_of_lanes ADD CONSTRAINT number_of_lanes_fk_transport_property 
	FOREIGN KEY (id) REFERENCES transport_property(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE road_name ADD CONSTRAINT road_name_fk_transport_property 
	FOREIGN KEY (id) REFERENCES transport_property(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE vertical_position ADD CONSTRAINT vertical_position_fk_transport_property 
	FOREIGN KEY (id) REFERENCES transport_property(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE form_of_way ADD CONSTRAINT form_of_way_fk_transport_property 
	FOREIGN KEY (id) REFERENCES transport_property(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE traffic_flow_direction ADD CONSTRAINT traffic_flow_direction_fk_transport_property 
	FOREIGN KEY (id) REFERENCES transport_property(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE speed_limit ADD CONSTRAINT speed_limit_fk_transport_property 
	FOREIGN KEY (id) REFERENCES transport_property(id) 
	ON UPDATE CASCADE ON DELETE CASCADE;

-- TRAFFIC SIGNALS:

DROP TABLE IF EXISTS traffic_information CASCADE;

CREATE TABLE traffic_information (
	id bigserial,
	link_set_id bigint, -- TODO remove this
	position bigint,
	effect bigint,
	class varchar(255),
	function varchar(255)[],
	usage varchar(255),
	direction varchar(255),
	yearOfInstalation int,
	trafficGroup varchar(255),
	lod0Point geometry(Point),
	-- TODO more geometries
	CONSTRAINT traffic_information_pk PRIMARY KEY (id),
	CONSTRAINT traffic_information_fk_transport_link_set FOREIGN KEY (link_set_id) 
		REFERENCES transport_link_set(id) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT traffic_information_fk_position FOREIGN KEY (position) 
		REFERENCES network_reference(id) ON UPDATE SET NULL ON DELETE SET NULL,
	CONSTRAINT traffic_information_fk_effect FOREIGN KEY (effect) 
		REFERENCES network_reference(id) ON UPDATE SET NULL ON DELETE SET NULL
);

DROP TABLE IF EXISTS traffic_sign CASCADE;
CREATE TABLE traffic_sign (
	id bigint,
	height numeric,
	characteristicLength numeric,
	orientation numeric[4],
	CONSTRAINT traffic_sign_pk PRIMARY KEY(id),
	CONSTRAINT traffic_sign_fk_traffic_information FOREIGN KEY (id) 
		REFERENCES traffic_information(id) ON UPDATE CASCADE ON DELETE CASCADE
);

-- GLOBAL INDEXES:

CREATE INDEX network_element_i_osm_id 
	ON network_element(osm_id);
	
CREATE INDEX network_reference_i_network_element_id 
	ON network_reference(network_element_id);
	
CREATE INDEX transport_link_i_centerline_geometry 
	ON transport_link USING gist (centerline_geometry);
	
CREATE INDEX transport_link_i_start_node 
	ON transport_link (start_node);
	
CREATE INDEX transport_link_i_end_node 
	ON transport_link (end_node);
	
CREATE INDEX transport_node_i_geometry  
	ON transport_node USING gist(geometry);
	
CREATE INDEX hnrtp_i_network_reference
	ON network_reference_transport_property (network_reference_id);
	
CREATE INDEX hnrtp_i_transport_property
	ON network_reference_transport_property (transport_property_id);
	
CREATE INDEX transport_link_set_element_i_link_set
	ON transport_link_set_element (link_set_id);
	
CREATE INDEX transport_link_set_element_i_link_sequence
	ON transport_link_set_element (link_sequence_id);
	
CREATE INDEX transport_link_set_element_i_link
	ON transport_link_set_element (link_id);

-- VIEWS FOR DEBUGING:

DROP VIEW IF EXISTS h_node;

CREATE OR REPLACE VIEW h_node AS 
	SELECT htn.id id,
		hne.osm_id osm_id, 
		htn."geometry" node_geometry
	FROM transport_node htn
		JOIN network_element hne ON hne.id = htn.id;

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
	FROM transport_link htl
		JOIN network_element hne ON hne.id = htl.id
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
		(SELECT first(name) 
			FROM road_name hrn 
				JOIN network_reference_transport_property hnrtp ON hnrtp.transport_property_id = hrn.id 
				JOIN network_reference hnr ON hnr.network_element_id = hne.id AND hnr.id = hnrtp.network_reference_id) AS name,
		(SELECT array_agg(numberOfLanes) 
			FROM number_of_lanes hnol
				JOIN network_reference_transport_property hnrtp ON hnrtp.transport_property_id = hnol.id 
				JOIN network_reference hnr ON hnr.network_element_id = hne.id AND hnr.id = hnrtp.network_reference_id) AS numberOfLanes,
		(SELECT first(verticalPosition) 
			FROM vertical_position hvp 
				JOIN network_reference_transport_property hnrtp ON hnrtp.transport_property_id = hvp.id 
				JOIN network_reference hnr ON hnr.network_element_id = hne.id AND hnr.id = hnrtp.network_reference_id) AS verticalPosition,
		(SELECT array_agg(formOfWay) 
			FROM form_of_way hfow
				JOIN network_reference_transport_property hnrtp ON hnrtp.transport_property_id = hfow.id 
				JOIN network_reference hnr ON hnr.network_element_id = hne.id AND hnr.id = hnrtp.network_reference_id) AS formOfWay,
		(SELECT first(SpeedLimitValue) 
			FROM speed_limit hsl 
				JOIN network_reference_transport_property hnrtp ON hnrtp.transport_property_id = hsl.id 
				JOIN transport_link_set_element htlse ON htlse.link_id = h_link.id
				JOIN network_reference hnr ON hnr.network_element_id = htlse.link_set_id AND hnr.id = hnrtp.network_reference_id) AS speedLimit
	FROM h_link 
		JOIN transport_link_sequence_transport_link htlstl ON htlstl.link_id = h_link.id
		JOIN network_element hne ON hne.id = htlstl.link_sequence_id;
		
		
drop table if exists setting cascade;
drop sequence if exists setting_id_seq cascade;
create sequence setting_id_seq;

CREATE TABLE setting (
  id bigint NOT NULL DEFAULT nextval('setting_id_seq'::regclass),
  name VARCHAR(50) NOT NULL,
  valueChar VARCHAR(100),
  valueNumber decimal,
  type VARCHAR(20) NOT NULL,
  CONSTRAINT idsetting_pk PRIMARY KEY (id)
)
;
		
INSERT INTO setting(name,valueNumber,type) VALUES ('limitQuery',1000, 'number');