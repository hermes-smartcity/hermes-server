-- script update - postgres --

--
-- measurement 
--

delete from measurement;
alter table measurement drop CONSTRAINT measurement_fk_usuario;
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
