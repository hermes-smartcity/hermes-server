drop table if exists sensordata cascade;
drop sequence if exists sensordata_id_seq cascade;
create sequence sensordata_id_seq;

CREATE TABLE sensordata (
	id bigint NOT NULL DEFAULT nextval('sensordata_id_seq'::regclass),
  	typesensor varchar NOT NULL,
	startime timestamp without time zone NOT NULL,
	enditme timestamp without time zone,
  	values numeric[] NOT NULL,
	idUsuarioMovil BIGINT,
  	CONSTRAINT idssensordata_pk PRIMARY KEY (id),
  	CONSTRAINT sensordata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;