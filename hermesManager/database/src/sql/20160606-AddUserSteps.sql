drop table if exists usersteps cascade;
drop sequence if exists usersteps_id_seq cascade;
create sequence usersteps_id_seq;

CREATE TABLE usersteps (
  id bigint NOT NULL DEFAULT nextval('usersteps_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  starttime timestamp without time zone,
  endtime timestamp without time zone,
  steps integer,
  idUsuarioMovil bigint,
  CONSTRAINT idusersteps_pk PRIMARY KEY (id),
  CONSTRAINT idusersteps_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;