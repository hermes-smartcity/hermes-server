set schema 'osmimport';

drop table if exists execution cascade;
drop sequence if exists execution_id_seq cascade;
create sequence execution_id_seq;

CREATE TABLE execution (
  id bigint NOT NULL DEFAULT nextval('execution_id_seq'::regclass),
  status VARCHAR(50) NOT NULL,
  timestamp timestamp without time zone NOT NULL,
  idJob bigint NOT NULL,
  CONSTRAINT execution_pk PRIMARY KEY (id),
  CONSTRAINT execution_fk_job FOREIGN KEY (idJob) REFERENCES job(id) ON DELETE CASCADE
)
;

-- Table Message
drop table if exists message cascade;
drop sequence if exists message_id_seq cascade;
create sequence message_id_seq;

CREATE TABLE message (
  id bigint NOT NULL DEFAULT nextval('message_id_seq'::regclass),
  text VARCHAR(1000) NOT NULL,
  timestamp timestamp without time zone NOT NULL,
  idExecution bigint,
  CONSTRAINT message_pk PRIMARY KEY (id),
  CONSTRAINT message_fk_execution FOREIGN KEY (idExecution) REFERENCES execution(id) ON DELETE CASCADE
)
;

set schema 'public';