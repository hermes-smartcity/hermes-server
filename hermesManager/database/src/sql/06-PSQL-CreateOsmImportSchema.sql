create schema osmimport;

set schema 'osmimport';

-- Table DBConnection
drop table if exists dbconnection cascade;
drop sequence if exists dbconnection_id_seq cascade;
create sequence dbconnection_id_seq;

CREATE TABLE dbconnection (
  id bigint NOT NULL DEFAULT nextval('dbconnection_id_seq'::regclass),
  name VARCHAR(100) NOT NULL,
  type VARCHAR(50) NOT NULL,
  host VARCHAR(150) NOT NULL,
  port int NOT NULL,
  dbName VARCHAR(100) NOT NULL,
  CONSTRAINT iddbconnection_pk PRIMARY KEY (id)
)
;

-- Table DBConcept
drop table if exists dbconcept cascade;
drop sequence if exists dbconcept_id_seq cascade;
create sequence dbconcept_id_seq;

CREATE TABLE dbconcept (
  id bigint NOT NULL DEFAULT nextval('dbconcept_id_seq'::regclass),
  name VARCHAR(100) NOT NULL,
  schemaName VARCHAR(150),
  tableName VARCHAR(150) NOT NULL,
  idDbConnection bigint,
  CONSTRAINT iddbconcept_pk PRIMARY KEY (id),
  CONSTRAINT dbconcept_fk_dbconnection FOREIGN KEY (idDbConnection) REFERENCES dbconnection(id) ON DELETE CASCADE
)
;

-- Table DBAttribute
drop table if exists dbattribute cascade;
drop sequence if exists dbattribute_id_seq cascade;
create sequence dbattribute_id_seq;

CREATE TABLE dbattribute (
  id bigint NOT NULL DEFAULT nextval('dbattribute_id_seq'::regclass),
  attributeName VARCHAR(150) NOT NULL,
  attributeType VARCHAR(150) NOT NULL,
  idDbConcept bigint,
  CONSTRAINT iddbattribute_pk PRIMARY KEY (id),
  CONSTRAINT dbattribute_fk_dbconcept FOREIGN KEY (idDbConcept) REFERENCES dbconcept(id) ON DELETE CASCADE
)
;

-- Table OSMConcept
drop table if exists osmconcept cascade;
drop sequence if exists osmconcept_id_seq cascade;
create sequence osmconcept_id_seq;

CREATE TABLE osmconcept (
  id bigint NOT NULL DEFAULT nextval('osmconcept_id_seq'::regclass),
  name VARCHAR(150) NOT NULL,
  CONSTRAINT idosmconcept_pk PRIMARY KEY (id)
)
;

-- Table OSMFilter
drop table if exists osmfilter cascade;
drop sequence if exists osmfilter_id_seq cascade;
create sequence osmfilter_id_seq;

CREATE TABLE osmfilter (
  id bigint NOT NULL DEFAULT nextval('osmfilter_id_seq'::regclass),
  name VARCHAR(150) NOT NULL,
  operation VARCHAR(150) NOT NULL,
  value VARCHAR(150) NOT NULL,
  idOsmConcept bigint,
  CONSTRAINT idosmfilter_pk PRIMARY KEY (id),
  CONSTRAINT osmfilter_fk_osmconcept FOREIGN KEY (idOsmConcept) REFERENCES osmconcept(id) ON DELETE CASCADE
)
;

-- Table OSMAttribute
drop table if exists osmattribute cascade;
drop sequence if exists osmattribute_id_seq cascade;
create sequence osmattribute_id_seq;

CREATE TABLE osmattribute (
  id bigint NOT NULL DEFAULT nextval('osmattribute_id_seq'::regclass),
  name VARCHAR(150) NOT NULL, 
  idOsmConcept bigint,
  CONSTRAINT idosmattribute_pk PRIMARY KEY (id),
  CONSTRAINT osmattribute_fk_osmconcept FOREIGN KEY (idOsmConcept) REFERENCES osmconcept(id) ON DELETE CASCADE
)
;

-- Table Job
drop table if exists job cascade;
drop sequence if exists job_id_seq cascade;
create sequence job_id_seq;

CREATE TABLE job (
  id bigint NOT NULL DEFAULT nextval('job_id_seq'::regclass),
  name VARCHAR(150) NOT NULL,
  bbox public.geometry(POLYGON, 4326) NOT NULL,
  CONSTRAINT idjob_pk PRIMARY KEY (id)
)
;

-- Table ConceptTransformation
drop table if exists concepttransformation cascade;
drop sequence if exists concepttransformation_id_seq cascade;
create sequence concepttransformation_id_seq;

CREATE TABLE concepttransformation (
  id bigint NOT NULL DEFAULT nextval('concepttransformation_id_seq'::regclass),
  bbox public.geometry(POLYGON, 4326),
  idJob bigint,
  idDbConcept bigint,
  idOsmConcept bigint,
  CONSTRAINT idconcepttransformation_pk PRIMARY KEY (id),
  CONSTRAINT concepttransformation_fk_job FOREIGN KEY (idJob) REFERENCES job(id) ON DELETE CASCADE,
  CONSTRAINT concepttransformation_fk_dbconcept FOREIGN KEY (idDbConcept) REFERENCES dbconcept(id) ON DELETE CASCADE,
  CONSTRAINT concepttransformation_fk_osmconcept FOREIGN KEY (idOsmConcept) REFERENCES osmconcept(id) ON DELETE CASCADE
)
;

-- Table AttributeMapping
drop table if exists attributemapping cascade;
drop sequence if exists attributemapping_id_seq cascade;
create sequence attributemapping_id_seq;

CREATE TABLE attributemapping (
  id bigint NOT NULL DEFAULT nextval('attributemapping_id_seq'::regclass),
  idConceptTransformation bigint,
  idDbAttribute bigint,
  idOsmAttribute bigint,
  CONSTRAINT idattributemapping_pk PRIMARY KEY (id),
  CONSTRAINT attributemapping_fk_concepttransformation FOREIGN KEY (idConceptTransformation) REFERENCES concepttransformation(id) ON DELETE CASCADE,
  CONSTRAINT attributemapping_fk_dbattribute FOREIGN KEY (idDbAttribute) REFERENCES dbattribute(id) ON DELETE CASCADE,
  CONSTRAINT attributemapping_fk_osmattribute FOREIGN KEY (idOsmAttribute) REFERENCES osmattribute(id) ON DELETE CASCADE
)
;

-- Table Execution
drop table if exists execution cascade;
drop sequence if exists execution_id_seq cascade;
create sequence execution_id_seq;

CREATE TABLE execution (
  id bigint NOT NULL DEFAULT nextval('execution_id_seq'::regclass),
  status VARCHAR(50) NOT NULL,
  timestamp timestamp without time zone NOT NULL,
  idJob bigint NOT NULL,
  CONSTRAINT execution_pk PRIMARY KEY (id),
  CONSTRAINT execution_fk_job FOREIGN KEY (idJob) REFERENCES job(id) ON DELETE CASCADE
)
;

-- Table Message
drop table if exists message cascade;
drop sequence if exists message_id_seq cascade;
create sequence message_id_seq;

CREATE TABLE message (
  id bigint NOT NULL DEFAULT nextval('message_id_seq'::regclass),
  text VARCHAR(1000) NOT NULL,
  timestamp timestamp without time zone NOT NULL,
  idExecution bigint,
  CONSTRAINT message_pk PRIMARY KEY (id),
  CONSTRAINT message_fk_execution FOREIGN KEY (idExecution) REFERENCES execution(id) ON DELETE CASCADE
)
;

set schema 'public';