-- script creacion - postgres --
-- usuario_movil --
drop table if exists usuario_movil cascade;
drop sequence if exists usuario_movil_id_seq cascade;
create sequence usuario_movil_id_seq;


CREATE TABLE usuario_movil(
  id bigint NOT NULL DEFAULT nextval('usuario_movil_id_seq'::regclass),
  sourceId VARCHAR(160),
  CONSTRAINT usuario_movil_id_pk PRIMARY KEY (id)
);

-- usuario_web --
drop table if exists usuario_web cascade;
drop sequence if exists usuario_web_id_seq cascade;
create sequence usuario_web_id_seq;


CREATE TABLE usuario_web(
  id bigint NOT NULL DEFAULT nextval('usuario_web_id_seq'::regclass),
  rol VARCHAR(32),
  email VARCHAR(160),
  password VARCHAR(160),
  id_usuario_movil bigint,
  CONSTRAINT usuario_web_u UNIQUE (email),
  CONSTRAINT usuario_web_pk PRIMARY KEY (id),
   CONSTRAINT usuario_movil_fk_usuario_web FOREIGN KEY (id_usuario_movil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;
-- measurement --
drop table if exists measurement cascade
;
drop sequence if exists measurement_id_seq cascade
;
create sequence measurement_id_seq
;

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
)
;


-- vehicleLocation --
drop table if exists vehicleLocation cascade
;
drop sequence if exists vehicleLocation_id_seq cascade
;
create sequence vehicleLocation_id_seq
;

CREATE TABLE vehicleLocation (
  id bigint NOT NULL DEFAULT nextval('vehicleLocation_id_seq'::regclass),
  timestamp timestamp without time zone,
  position geometry(POINT, 4326),
  eventId VARCHAR(50) NOT NULL,
  idUsuario BIGINT,
  CONSTRAINT idvehicleLocation_pk PRIMARY KEY (id),
  CONSTRAINT vehicleLocation_fk_usuario FOREIGN KEY (idUsuario) REFERENCES usuario(id) ON DELETE CASCADE
)
;


-- dataSection --
drop table if exists dataSection cascade
;
drop sequence if exists dataSection_id_seq cascade
;
create sequence dataSection_id_seq
;

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
)
;

-- driverFeatures --

drop table if exists driverFeatures cascade
;
drop sequence if exists driverFeatures_id_seq cascade
;
create sequence driverFeatures_id_seq
;

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
)
;

-- eventoProcesado --

drop table if exists eventoProcesado cascade
;
drop sequence if exists eventoProcesado_id_seq cascade
;
create sequence eventoProcesado_id_seq
;

CREATE TABLE eventoProcesado (
  id bigint NOT NULL DEFAULT nextval('eventoProcesado_id_seq'::regclass),
  timestamp timestamp without time zone,
  eventId VARCHAR(50) NOT NULL
)
;

--
-- SmartCitizen: Sleep Data
--
drop table if exists sleepdata cascade
;
drop sequence if exists sleepdata_id_seq cascade
;
create sequence sleepdata_id_seq
;

CREATE TABLE sleepdata (
  id bigint NOT NULL DEFAULT nextval('sleepdata_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  awakenings integer,
  minutesAsleep integer,
  minutesInBed integer,  
  startTime timestamp without time zone,
  endTime timestamp without time zone,
  idUsuario bigint,
  CONSTRAINT idsleepdata_pk PRIMARY KEY (id),
  CONSTRAINT idsleepdata_fk_usuario FOREIGN KEY (idUsuario) REFERENCES usuario(id) ON DELETE CASCADE
)
;

--
-- SmartCitizen: Steps Data
--
drop table if exists stepsdata cascade
;
drop sequence if exists stepsdata_id_seq cascade
;
create sequence stepsdata_id_seq
;

CREATE TABLE stepsdata (
  id bigint NOT NULL DEFAULT nextval('stepsdata_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  timelog timestamp without time zone,
  steps integer,
  idUsuario bigint,
  CONSTRAINT idstepsdata_pk PRIMARY KEY (id),
  CONSTRAINT idstepsdata_fk_usuario FOREIGN KEY (idUsuario) REFERENCES usuario(id) ON DELETE CASCADE
)
;

--
-- SmartCitizen: Heart Rate Data
--
drop table if exists heartratedata cascade
;
drop sequence if exists heartratedata_id_seq cascade
;
create sequence heartratedata_id_seq
;

CREATE TABLE heartratedata (
  id bigint NOT NULL DEFAULT nextval('heartratedata_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  timelog timestamp without time zone,
  heartRate integer,
  idUsuario bigint,
  CONSTRAINT idheartratedata_pk PRIMARY KEY (id),
  CONSTRAINT idheartratedata_fk_usuario FOREIGN KEY (idUsuario) REFERENCES usuario(id) ON DELETE CASCADE
)
;