drop table if exists userheartrates cascade;
drop sequence if exists userheartrates_id_seq cascade;
create sequence userheartrates_id_seq;

CREATE TABLE userheartrates (
  id bigint NOT NULL DEFAULT nextval('userheartrates_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  starttime timestamp without time zone,
  endtime timestamp without time zone,
  bpm decimal,
  idUsuarioMovil bigint,
  CONSTRAINT iduserheartrates_pk PRIMARY KEY (id),
  CONSTRAINT iduserheartrates_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;