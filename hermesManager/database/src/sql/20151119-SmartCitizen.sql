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