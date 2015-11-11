 -- dataSections
drop table if exists dataSection cascade;
drop sequence if exists dataSection_id_seq cascade;
create sequence dataSection_id_seq;

CREATE TABLE dataSection (
  id bigint NOT NULL DEFAULT nextval('dataSection_id_seq'::regclass),
  timestamp timestamp without time zone,
  roadSection geometry(LineString,4326), 
  eventId VARCHAR(50) NOT NULL,
   minSpeed double precision,
   maxSpeed double precision,
   medianSpeed  double precision,
   averageSpeed double precision,
   averageRR double precision,
   averageHeartRate  double precision,
   standardDeviationSpeed  double precision,
   standardDeviationRR double precision,
   standardDeviationHeartRate double precision,   
   pke double precision,
   idUsuario BIGINT,
  CONSTRAINT iddataSection_pk PRIMARY KEY (id),
  CONSTRAINT dataSection_fk_usuario FOREIGN KEY (idUsuario) REFERENCES usuario(id) ON DELETE CASCADE
);

-- measurement
ALTER TABLE measurement ALTER COLUMN value TYPE double precision;
