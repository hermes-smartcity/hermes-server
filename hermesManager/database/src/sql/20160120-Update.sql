-- script update - postgres --

-- eliminar tabla usuario si existe --
drop table if exists usuario cascade;
drop sequence if exists usuario_id_seq cascade;


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
--
-- measurement 
--

delete from measurement;
alter table measurement drop CONSTRAINT if exists measurement_fk_usuario;
ALTER TABLE measurement RENAME COLUMN idUsuario to idUsuarioMovil;
alter table measurement add  CONSTRAINT measurement_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;


--
-- vehicleLocation
--

delete from vehicleLocation;
alter table vehicleLocation drop CONSTRAINT vehiclelocation_fk_usuario;
ALTER TABLE vehicleLocation RENAME COLUMN idUsuario to idUsuarioMovil;
alter table vehicleLocation add  CONSTRAINT vehicleLocation_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;

-- 
-- dataSection
--

delete from dataSection;
alter table dataSection drop CONSTRAINT dataSection_fk_usuario;
ALTER TABLE dataSection RENAME COLUMN idUsuario to idUsuarioMovil;
alter table dataSection add  CONSTRAINT dataSection_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;

--
-- driverFeatures
--

delete from driverFeatures;
alter table driverFeatures drop CONSTRAINT driverfeatures_fk_usuario;
ALTER TABLE driverFeatures RENAME COLUMN idUsuario to idUsuarioMovil;
alter table driverFeatures add  CONSTRAINT driverFeatures_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;

--
-- eventoProcesado 
--
alter table eventoProcesado ADD COLUMN tipo VARCHAR(20);

--
-- SmartCitizen: Sleep Data
--

delete from sleepdata;
alter table sleepdata drop CONSTRAINT idsleepdata_fk_usuario;
ALTER TABLE sleepdata RENAME COLUMN idUsuario to idUsuarioMovil;
alter table sleepdata add  CONSTRAINT idsleepdata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;


--
-- SmartCitizen: Steps Data
--

delete from stepsdata;
alter table stepsdata drop CONSTRAINT idstepsdata_fk_usuario;
ALTER TABLE stepsdata RENAME COLUMN idUsuario to idUsuarioMovil;
alter table stepsdata add  CONSTRAINT idstepsdata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;


--
-- SmartCitizen: Heart Rate Data
--

delete from heartratedata;
alter table heartratedata drop CONSTRAINT idheartratedata_fk_usuario;
ALTER TABLE heartratedata RENAME COLUMN idUsuario to idUsuarioMovil;
alter table heartratedata add  CONSTRAINT idheartratedata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;
