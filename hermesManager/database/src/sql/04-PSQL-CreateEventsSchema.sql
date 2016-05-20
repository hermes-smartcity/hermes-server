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

drop table if exists usuario_web cascade;
drop sequence if exists usuario_web_id_seq cascade;
create sequence usuario_web_id_seq;

-- usuario_web --
CREATE TABLE usuario_web(
  id bigint NOT NULL DEFAULT nextval('usuario_web_id_seq'::regclass),
  rol VARCHAR(32),
  email VARCHAR(160),
  password VARCHAR(160),
  id_usuario_movil bigint,
  activado boolean DEFAULT true,
  CONSTRAINT usuario_web_u UNIQUE (email),
  CONSTRAINT usuario_web_pk PRIMARY KEY (id),
  CONSTRAINT usuario_movil_fk_usuario_web FOREIGN KEY (id_usuario_movil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;

-- measurement --
drop table if exists measurement cascade;
drop sequence if exists measurement_id_seq cascade;
create sequence measurement_id_seq;

CREATE TABLE measurement (
  id bigint NOT NULL DEFAULT nextval('measurement_id_seq'::regclass),
  timestamp timestamp without time zone,
  position geometry(POINT, 4326),
  accuracy double precision,
  speed double precision,
  eventId VARCHAR(50) NOT NULL,
  tipo VARCHAR(20) NOT NULL,
  value double precision,
  rrlast10seconds double precision[],
  idUsuarioMovil BIGINT,
  CONSTRAINT idmeasurement_pk PRIMARY KEY (id),
  CONSTRAINT measurement_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;

-- vehicleLocation --
drop table if exists vehicleLocation cascade;
drop sequence if exists vehicleLocation_id_seq cascade;
create sequence vehicleLocation_id_seq;

CREATE TABLE vehicleLocation (
  id bigint NOT NULL DEFAULT nextval('vehicleLocation_id_seq'::regclass),
  timestamp timestamp without time zone,
  position geometry(POINT, 4326),
  accuracy double precision,
  speed double precision,
  eventId VARCHAR(50) NOT NULL,
  idUsuarioMovil BIGINT,
  CONSTRAINT idvehicleLocation_pk PRIMARY KEY (id),
  CONSTRAINT vehicleLocation_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;


-- dataSection --
drop table if exists dataSection cascade;
drop sequence if exists dataSection_id_seq cascade;
create sequence dataSection_id_seq;

CREATE TABLE dataSection (
  id bigint NOT NULL DEFAULT nextval('dataSection_id_seq'::regclass),
  timestamp timestamp without time zone,
  roadSection geometry(LineString,4326),
  accuracy double precision[],
  speed double precision[],
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
   numhighaccelerations double precision,
   numhighdecelerations double precision,
   averageacceleration double precision,
   averagedeceleration double precision,
   rrsection double precision[],   
   idUsuarioMovil BIGINT,
  CONSTRAINT iddataSection_pk PRIMARY KEY (id),
  CONSTRAINT dataSection_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;

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
  timestamp timestamp without time zone,
  idUsuarioMovil BIGINT,
  CONSTRAINT idDriverFeatures_pk PRIMARY KEY (id),
  CONSTRAINT driverFeatures_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;

-- eventoProcesado --

drop table if exists eventoProcesado cascade;
drop sequence if exists eventoProcesado_id_seq cascade;
create sequence eventoProcesado_id_seq;

CREATE TABLE eventoProcesado (
  id bigint NOT NULL DEFAULT nextval('eventoProcesado_id_seq'::regclass),
  timestamp timestamp without time zone,
  eventId VARCHAR(50) NOT NULL, 
  tipo VARCHAR(20))
;

--
-- SmartCitizen: Sleep Data
--
drop table if exists sleepdata cascade;
drop sequence if exists sleepdata_id_seq cascade;
create sequence sleepdata_id_seq;

CREATE TABLE sleepdata (
  id bigint NOT NULL DEFAULT nextval('sleepdata_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  awakenings integer,
  minutesAsleep integer,
  minutesInBed integer,  
  startTime timestamp without time zone,
  endTime timestamp without time zone,
  idUsuarioMovil bigint,
  CONSTRAINT idsleepdata_pk PRIMARY KEY (id),
  CONSTRAINT idsleepdata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;

--
-- SmartCitizen: Steps Data
--
drop table if exists stepsdata cascade;
drop sequence if exists stepsdata_id_seq cascade;
create sequence stepsdata_id_seq;

CREATE TABLE stepsdata (
  id bigint NOT NULL DEFAULT nextval('stepsdata_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  timelog timestamp without time zone,
  steps integer,
  idUsuarioMovil bigint,
  CONSTRAINT idstepsdata_pk PRIMARY KEY (id),
  CONSTRAINT idstepsdata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;

--
-- SmartCitizen: Heart Rate Data
--
drop table if exists heartratedata cascade;
drop sequence if exists heartratedata_id_seq cascade;
create sequence heartratedata_id_seq;

CREATE TABLE heartratedata (
  id bigint NOT NULL DEFAULT nextval('heartratedata_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  timelog timestamp without time zone,
  heartRate integer,
  idUsuarioMovil bigint,
  CONSTRAINT idheartratedata_pk PRIMARY KEY (id),
  CONSTRAINT idheartratedata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;

--
-- LOGS: Visualizar el log de errores en la BD
--

CREATE TABLE logs
   (id bigserial primary key,
	dated   timestamp without time zone NOT NULL,
    logger  VARCHAR(500)    NOT NULL,
    level   VARCHAR(10)    NOT NULL,
    message VARCHAR(1000)  NOT NULL
   );

   
--
-- SmartCitizen: Context Data
--
drop table if exists contextdata cascade;
drop sequence if exists contextdata_id_seq cascade;
create sequence contextdata_id_seq;

CREATE TABLE contextdata (
  id bigint NOT NULL DEFAULT nextval('contextdata_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  position geometry(POINT, 4326),
  timelog timestamp without time zone,
  detectedActivity VARCHAR(50),
  accuracy integer,
  idUsuarioMovil bigint,
  CONSTRAINT idcontextdata_pk PRIMARY KEY (id),
  CONSTRAINT idcontextdata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;

-- DataServices
drop table if exists dataservices cascade;
drop sequence if exists dataservices_id_seq cascade;
create sequence dataservices_id_seq;

CREATE TABLE dataservices (
	id bigint NOT NULL DEFAULT nextval('dataservices_id_seq'::regclass),
  	service varchar NOT NULL,
	method varchar NOT NULL,
	timelog timestamp without time zone NOT NULL,
  	CONSTRAINT idsdataservices_pk PRIMARY KEY (id)
)
;

-- NetworkLink
CREATE OR REPLACE VIEW network.link as select * from network.es_cor_2po_4pgr;

-- SensorData
drop table if exists sensordata cascade;
drop sequence if exists sensordata_id_seq cascade;
create sequence sensordata_id_seq;

CREATE TABLE sensordata (
	id bigint NOT NULL DEFAULT nextval('sensordata_id_seq'::regclass),
  	typesensor varchar NOT NULL,
	startime timestamp without time zone NOT NULL,
	endtime timestamp without time zone,
  	values numeric[] NOT NULL,
	idUsuarioMovil BIGINT,
  	CONSTRAINT idssensordata_pk PRIMARY KEY (id),
  	CONSTRAINT sensordata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;

-- Gps
drop table if exists gps cascade;
drop sequence if exists gps_id_seq cascade;
create sequence gps_id_seq;

CREATE TABLE gps (
	id bigint NOT NULL DEFAULT nextval('gps_id_seq'::regclass),
  	provider varchar NOT NULL,
	time timestamp without time zone NOT NULL,
	position geometry(POINT, 4326),
	altitude double precision,
  	speed double precision,
  	bearing double precision,
	accuracy double precision,
	idUsuarioMovil BIGINT,
  	CONSTRAINT idsgps_pk PRIMARY KEY (id),
  	CONSTRAINT gps_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;

-- User activities
drop table if exists useractivities cascade;
drop sequence if exists useractivities_id_seq cascade;
create sequence useractivities_id_seq;

CREATE TABLE useractivities (
  id bigint NOT NULL DEFAULT nextval('useractivities_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  starttime timestamp without time zone,
  endtime timestamp without time zone,
  name VARCHAR(100),
  idUsuarioMovil bigint,
  CONSTRAINT iduseractivities_pk PRIMARY KEY (id),
  CONSTRAINT iduseractivities_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;

-- User locations
drop table if exists userlocations cascade;
drop sequence if exists userlocations_id_seq cascade;
create sequence userlocations_id_seq;

CREATE TABLE userlocations (
  id bigint NOT NULL DEFAULT nextval('userlocations_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  position geometry(POINT, 4326),
  starttime timestamp without time zone,
  endtime timestamp without time zone,
  accuracy integer,
  idUsuarioMovil bigint,
  CONSTRAINT iduserlocations_pk PRIMARY KEY (id),
  CONSTRAINT iduserlocations_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;

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

--
--
--
drop table if exists efficiencytest cascade;
drop sequence if exists efficiencytest_id_seq cascade;
create sequence efficiencytest_id_seq;

CREATE TABLE efficiencytest (
  id bigint NOT NULL DEFAULT nextval('efficiencytest_id_seq'::regclass),
  eventtype varchar,
  eventsize integer,
  time timestamp without time zone,
  delay integer,
  parsetime integer,
  totaltime integer,
  result boolean,
  CONSTRAINT idsetting_pk PRIMARY KEY (id)
)
;

CREATE OR REPLACE FUNCTION public.makegrid_2d(bound_polygon public.geometry, number_of_cells double precision)
RETURNS public.geometry AS
$body$
DECLARE
  BoundM public.geometry; --Bound polygon transformed to the metric projection (with metric_srid SRID)
  Xmin DOUBLE PRECISION;
  Xmax DOUBLE PRECISION;
  Ymin DOUBLE PRECISION;
  Ymax DOUBLE PRECISION;
  X DOUBLE PRECISION;
  Y DOUBLE PRECISION;
  sectors public.geometry[];
  i INTEGER;
  srid INTEGER;
  size DOUBLE PRECISION;
BEGIN
  Xmin := ST_XMin($1);
  Xmax := ST_XMax($1);
  Ymin := ST_YMin($1);
  Ymax := ST_YMax($1);
  srid := ST_srid($1);
  Y := Ymin; --current sector's corner coordinate
  i := -1;
  size := (Ymax - Ymin) / $2;
  <<yloop>>
  LOOP
    IF (Y > Ymax) THEN  --Better if generating polygons exceeds the bound for one step. You always can crop the result. But if not you may get not quite correct data for outbound polygons (e.g. if you calculate frequency per sector)
        EXIT;
    END IF;
    X := Xmin;
    <<xloop>>
    LOOP
      IF (X > Xmax) THEN
          EXIT;
      END IF;
      i := i + 1;
      sectors[i] := ST_GeomFromText('POLYGON(('||X||' '||Y||', '||(X+size)||' '||Y||', '||(X+size)||' '||(Y+size)||', '||X||' '||(Y+size)||', '||X||' '||Y||'))', srid);
      X := X + size;
    END LOOP xloop;
    Y := Y + size;
  END LOOP yloop;
  RETURN ST_Transform(ST_Collect(sectors), ST_SRID($1));
END;
$body$
LANGUAGE 'plpgsql';