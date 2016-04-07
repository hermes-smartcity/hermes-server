drop table if exists dataservices cascade;
drop sequence if exists dataservices_id_seq cascade;
create sequence dataservices_id_seq;

CREATE TABLE dataservices (
	id bigint NOT NULL DEFAULT nextval('dataservices_id_seq'::regclass),
  	service varchar NOT NULL,
	method varchar NOT NULL,
	timelog timestamp without time zone NOT NULL,
  	CONSTRAINT idsdataservices_pk PRIMARY KEY (id)
)
;