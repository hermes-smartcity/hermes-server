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