-- script creacion - postgres --
-- usuario --
drop table if exists usuario cascade;
drop sequence if exists usuario_id_seq cascade;
create sequence usuario_id_seq;

CREATE TABLE usuario (
  id bigint NOT NULL DEFAULT nextval('usuario_id_seq'::regclass),
  sourceId VARCHAR(160),
  CONSTRAINT usuario_id_pk PRIMARY KEY (id)
);

-- measurement --
drop table if exists measurement cascade;
drop sequence if exists measurement_id_seq cascade;
create sequence measurement_id_seq;

CREATE TABLE measurement (
  id bigint NOT NULL DEFAULT nextval('measurement_id_seq'::regclass),
  timestamp timestamp without time zone,
  position geometry(POINT, 4326),
  eventId VARCHAR(50) NOT NULL,
  tipo VARCHAR(20) NOT NULL,
  value double precision,
  idUsuario BIGINT,
  CONSTRAINT idmeasurement_pk PRIMARY KEY (id),
  CONSTRAINT measurement_fk_usuario FOREIGN KEY (idUsuario) REFERENCES usuario(id) ON DELETE CASCADE
);


-- vehicleLocation --
drop table if exists vehicleLocation cascade;
drop sequence if exists vehicleLocation_id_seq cascade;
create sequence vehicleLocation_id_seq;

CREATE TABLE vehicleLocation (
  id bigint NOT NULL DEFAULT nextval('vehicleLocation_id_seq'::regclass),
  timestamp timestamp without time zone,
  position geometry(POINT, 4326),
  eventId VARCHAR(50) NOT NULL,
  idUsuario BIGINT,
  CONSTRAINT idvehicleLocation_pk PRIMARY KEY (id),
  CONSTRAINT vehicleLocation_fk_usuario FOREIGN KEY (idUsuario) REFERENCES usuario(id) ON DELETE CASCADE
);


-- dataSection --
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

-- driverFeatures --

drop table if exists driverFeatures cascade;
drop sequence if exists driverFeatures_id_seq cascade;
create sequence driverFeatures_id_seq;

CREATE TABLE driverFeatures (
  id bigint NOT NULL DEFAULT nextval('driverFeatures_id_seq'::regclass),
  awakeFor integer,
  inBed integer,
  workingTime integer,
  lightSleep integer,
  deepSleep integer,
  previousStress integer,
  idUsuario BIGINT,
  CONSTRAINT idDriverFeatures_pk PRIMARY KEY (id),
  CONSTRAINT driverFeatures_fk_usuario FOREIGN KEY (idUsuario) REFERENCES usuario(id) ON DELETE CASCADE
);

-- eventoProcesado --

drop table if exists eventoProcesado cascade;
drop sequence if exists eventoProcesado cascade;
create sequence eventoProcesado_id_seq;

CREATE TABLE eventoProcesado (
  id bigint NOT NULL DEFAULT nextval('eventoProcesado_id_seq'::regclass),
  timestamp timestamp without time zone,
  eventId VARCHAR(50) NOT NULL
);