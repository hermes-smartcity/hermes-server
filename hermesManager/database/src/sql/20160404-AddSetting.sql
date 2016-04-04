drop table if exists setting cascade;
drop sequence if exists setting_id_seq cascade;
create sequence setting_id_seq;

CREATE TABLE setting (
  id bigint NOT NULL DEFAULT nextval('setting_id_seq'::regclass),
  name VARCHAR(50) NOT NULL,
  valueChar VARCHAR(100),
  valueNumber decimal,
  type VARCHAR(20) NOT NULL,
  CONSTRAINT idsetting_pk PRIMARY KEY (id)
)
;
		
INSERT INTO setting(name,valueNumber,type) VALUES ('limitQuery',1000, 'number');