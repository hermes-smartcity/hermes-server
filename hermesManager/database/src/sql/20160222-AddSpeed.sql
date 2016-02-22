alter table measurement add column speed double precision;
alter table vehiclelocation add column speed double precision;
alter table dataSection add column speed double precision[];

alter table dataSection add column numhighaccelerations double precision;
alter table dataSection add column numhighdecelerations double precision;
alter table dataSection add column averageacceleration double precision;
alter table dataSection add column averagedeceleration double precision;
alter table dataSection add column rrsection double precision[];

alter table measurement add column rrlast10seconds double precision[];