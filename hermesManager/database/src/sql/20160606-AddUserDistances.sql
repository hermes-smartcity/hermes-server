drop table if exists userdistances cascade;
drop sequence if exists userdistances_id_seq cascade;
create sequence userdistances_id_seq;

CREATE TABLE userdistances (
  id bigint NOT NULL DEFAULT nextval('userdistances_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  starttime timestamp without time zone,
  endtime timestamp without time zone,
  distance decimal,
  idUsuarioMovil bigint,
  CONSTRAINT iduserdistances_pk PRIMARY KEY (id),
  CONSTRAINT iduserdistances_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;