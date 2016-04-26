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