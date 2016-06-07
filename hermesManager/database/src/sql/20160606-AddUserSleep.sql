drop table if exists usersleep cascade;
drop sequence if exists usersleep_id_seq cascade;
create sequence usersleep_id_seq;

CREATE TABLE usersleep (
  id bigint NOT NULL DEFAULT nextval('usersleep_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  starttime timestamp without time zone,
  endtime timestamp without time zone,
  name VARCHAR(100),
  idUsuarioMovil bigint,
  CONSTRAINT idusersleep_pk PRIMARY KEY (id),
  CONSTRAINT idusersleep_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;