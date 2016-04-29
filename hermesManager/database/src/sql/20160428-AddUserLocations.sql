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