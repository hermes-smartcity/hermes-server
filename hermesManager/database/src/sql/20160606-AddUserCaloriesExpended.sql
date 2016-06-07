drop table if exists usercaloriesexpended cascade;
drop sequence if exists usercaloriesexpended_id_seq cascade;
create sequence usercaloriesexpended_id_seq;

CREATE TABLE usercaloriesexpended (
  id bigint NOT NULL DEFAULT nextval('usercaloriesexpended_id_seq'::regclass),
  eventId VARCHAR(50) NOT NULL,
  starttime timestamp without time zone,
  endtime timestamp without time zone,
  calories decimal,
  idUsuarioMovil bigint,
  CONSTRAINT idusercaloriesexpended_pk PRIMARY KEY (id),
  CONSTRAINT idusercaloriesexpended_fk_usuario FOREIGN KEY (idUsuarioMovil) REFERENCES usuario_movil(id) ON DELETE CASCADE
)
;