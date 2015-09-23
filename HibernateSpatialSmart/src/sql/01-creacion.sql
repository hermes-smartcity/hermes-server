-- script creacion - postgres --
-- usuario --
drop table if exists usuario cascade;
drop sequence if exists usuario_id_seq cascade;
create sequence usuario_id_seq;

CREATE TABLE usuario (
  id bigint NOT NULL DEFAULT nextval('usuario_id_seq'::regclass),
  sourceId VARCHAR(160),
  version bigint not null default 0,
  CONSTRAINT usuario_id_pk PRIMARY KEY (id)
);

-- measurement --
drop table if exists measurement cascade;
drop sequence if exists measurement_id_seq cascade;
create sequence measurement_id_seq;

CREATE TABLE measurement (
  id bigint NOT NULL DEFAULT nextval('measurement_id_seq'::regclass),
  timestamp timestamp without time zone,
  position geometry(POINT, 4258),
  eventId VARCHAR(50) NOT NULL,
  tipo VARCHAR(20) NOT NULL,
  value integer NOT NULL,
  idUsuario BIGINT,
  version bigint not null default 0,
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
  position geometry(POINT, 4258),
  eventId VARCHAR(50) NOT NULL,
  idUsuario BIGINT,
  version bigint not null default 0,
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
  dataSection geometry(LineString,4258), 
  eventId VARCHAR(50) NOT NULL,
   minHeartRate integer,
   maxBeatBeat integer,
   maxHeartRate integer,
   standardDeviationSpeed double precision,
   minBeatBeat integer,
   minSpeed double precision,
   averageSpeed  double precision,
   standardDeviationBeatBeat  double precision,
   heartRate double precision,
   medianSpeed  double precision,
   standardDeviationHeartRate double precision,
   maxSpeed double precision,
   pke double precision,
   medianHeartRate integer,
   meanBeatBeat double precision,
   medianBeatBeat integer,
   idUsuario BIGINT,
   version bigint not null default 0,
  CONSTRAINT iddataSection_pk PRIMARY KEY (id),
  CONSTRAINT dataSection_fk_usuario FOREIGN KEY (idUsuario) REFERENCES usuario(id) ON DELETE CASCADE
);

-- eventoProcesado --
drop table if exists eventoProcesado cascade;
drop sequence if exists eventoProcesado_id_seq cascade;
create sequence eventoProcesado_id_seq;

CREATE TABLE eventoProcesado (
  id bigint NOT NULL DEFAULT nextval('eventoProcesado_id_seq'::regclass),
  timestamp timestamp without time zone,
  eventId VARCHAR(50) NOT NULL,
  version bigint not null default 0
);

-- menu --
drop table if exists menu cascade;
drop sequence if exists menu_id_seq cascade;
create sequence menu_id_seq;

CREATE TABLE menu (
  id bigint NOT NULL DEFAULT nextval('menu_id_seq'::regclass),
  nombre TEXT NOT NULL, 
  version bigint not null default 0,
  CONSTRAINT idMenu_pk PRIMARY KEY (id)
);

-- entradaMenu --
drop table if exists entradaMenu cascade;
drop sequence if exists entradaMenu_id_seq cascade;
create sequence entradaMenu_id_seq;

CREATE TABLE entradaMenu (
  id bigint NOT NULL DEFAULT nextval('entradaMenu_id_seq'::regclass),
  texto TEXT NOT NULL,
  url VARCHAR(255),
  orden integer not null default 0,
  idMenu bigint,
  idEntradaMenuPadre bigint,
  version bigint not null default 0,
  CONSTRAINT idEntradaMenu_pk PRIMARY KEY (id),
  CONSTRAINT menu_fk_entradaMenu FOREIGN KEY (idMenu) REFERENCES menu(id) ON DELETE CASCADE,
   CONSTRAINT padre_fk_entradaMenu FOREIGN KEY (idEntradaMenuPadre) REFERENCES entradaMenu(id) ON DELETE CASCADE
);

-- estatica --
drop table if exists estatica cascade;
drop sequence if exists estatica_id_seq cascade;
create sequence estatica_id_seq;

CREATE TABLE estatica (
  id bigint NOT NULL DEFAULT nextval('estatica_id_seq'::regclass),
  titulo VARCHAR(255) NOT NULL,
  contenido TEXT,
  version bigint not null default 0,
  CONSTRAINT idEstatica_pk PRIMARY KEY (id)
);