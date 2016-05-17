drop table if exists efficiencytest cascade;
drop sequence if exists efficiencytest_id_seq cascade;
create sequence efficiencytest_id_seq;

CREATE TABLE efficiencytest (
  id bigint NOT NULL DEFAULT nextval('efficiencytest_id_seq'::regclass),
  eventtype varchar,
  eventsize integer, 
  time timestamp without time zone,
  parsetime integer,
  totaltime integer,
  result boolean,
  CONSTRAINT idefficiencytest_pk PRIMARY KEY (id)
)
;
