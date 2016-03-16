drop table if exists contextdata cascade;
drop sequence if exists contextdata_id_seq cascade;
create sequence contextdata_id_seq;

CREATE TABLE contextdata (
  id bigint NOT NULL DEFAULT nextval('contextdata_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  position geometry(POINT, 4326),
  timelog timestamp without time zone,
  detectedActivity VARCHAR(50),
  accuracy integer,
  idUsuarioMovil bigint,
  CONSTRAINT idcontextdata_pk PRIMARY KEY (id),
  CONSTRAINT idcontextdata_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;