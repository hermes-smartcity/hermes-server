-- script update - postgres --

-- usuario_movil --
DROP TABLE if exists usuario_movil cascade;
DROP sequence if exists usuario_movil_id_seq cascade;
create sequence usuario_movil_id_seq;


CREATE TABLE usuario_movil(
  id bigint NOT NULL DEFAULT nextval('usuario_movil_id_seq'::regclass),
  sourceId VARCHAR(160),
  CONSTRAINT usuario_movil_id_pk PRIMARY KEY (id)
);

-- Copiamos los valores de usuario a usuario_movil
-- usuario_movil debe estar vacia

INSERT INTO usuario_movil (sourceId) 
SELECT sourceId FROM usuario;

-- usuario_web --
DROP TABLE if exists usuario_web cascade;
DROP sequence if exists usuario_web_id_seq cascade;
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
-- LOGS: Visualizar el log de errores en la BD
--

CREATE TABLE logs
   (id bigserial primary key,
	dated   timestamp without time zone NOT NULL,
    logger  VARCHAR(50)    NOT NULL,
    level   VARCHAR(10)    NOT NULL,
    message VARCHAR(1000)  NOT NULL
   );
   
--
-- measurement 
--
ALTER TABLE measurement DROP CONSTRAINT if exists measurement_fk_usuario;
ALTER TABLE measurement RENAME COLUMN idUsuario to idUsuarioMovil;
ALTER TABLE measurement ADD CONSTRAINT measurement_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;


--
-- vehicleLocation
--
ALTER TABLE vehicleLocation DROP CONSTRAINT vehiclelocation_fk_usuario;
ALTER TABLE vehicleLocation RENAME COLUMN idUsuario to idUsuarioMovil;
ALTER TABLE vehicleLocation ADD CONSTRAINT vehicleLocation_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;

-- 
-- dataSection
--
ALTER TABLE dataSection DROP CONSTRAINT dataSection_fk_usuario;
ALTER TABLE dataSection RENAME COLUMN idUsuario to idUsuarioMovil;
ALTER TABLE dataSection ADD CONSTRAINT dataSection_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;

--
-- driverFeatures
--
ALTER TABLE driverFeatures DROP CONSTRAINT driverfeatures_fk_usuario;
ALTER TABLE driverFeatures RENAME COLUMN idUsuario to idUsuarioMovil;
ALTER TABLE driverFeatures ADD CONSTRAINT driverFeatures_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;

--
-- eventoProcesado 
--
ALTER TABLE eventoProcesado ADD COLUMN tipo VARCHAR(20);

--
-- SmartCitizen: Sleep Data
--
ALTER TABLE sleepdata DROP CONSTRAINT idsleepdata_fk_usuario;
ALTER TABLE sleepdata RENAME COLUMN idUsuario to idUsuarioMovil;
ALTER TABLE sleepdata ADD CONSTRAINT idsleepdata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;


--
-- SmartCitizen: Steps Data
--
ALTER TABLE stepsdata DROP CONSTRAINT idstepsdata_fk_usuario;
ALTER TABLE stepsdata RENAME COLUMN idUsuario to idUsuarioMovil;
ALTER TABLE stepsdata ADD CONSTRAINT idstepsdata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;


--
-- SmartCitizen: Heart Rate Data
--
ALTER TABLE heartratedata DROP CONSTRAINT idheartratedata_fk_usuario;
ALTER TABLE heartratedata RENAME COLUMN idUsuario to idUsuarioMovil;
ALTER TABLE heartratedata ADD CONSTRAINT idheartratedata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE;

--
-- Eliminar tabla usuario si existe --
--

DROP TABLE if exists usuario cascade;
DROP sequence if exists usuario_id_seq cascade;

--
-- UsuarioWeb columna activado - spring security
--

ALTER TABLE usuario_web ADD COLUMN activado boolean DEFAULT true;
