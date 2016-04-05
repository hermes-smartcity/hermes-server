DB_HOST=localhost
DB_PORT=5432
DB_USER=***REMOVED***
DB_PASS=***REMOVED***
DB_NAME=meco1401_prod
DB_SCHEMA=network
PSQL_FLAGS=-h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) $(DB_NAME)

all: download osmosis osm2po hermes osm2HermesTN

target:
	mkdir target

download_osmosis: target
	@echo '==='
	@echo '=== Download osmosis'
	@echo '==='
	wget http://bretth.dev.openstreetmap.org/osmosis-build/osmosis-latest.zip
	mv osmosis-latest.zip target
	rm -rf target/osmosis
	unzip target/osmosis-latest.zip -d target/osmosis
	rm -f target/osmosis-latest.zip

download_osm: target
	@echo '==='
	@echo '=== Download OSM data'
	@echo '==='
	wget https://s3.amazonaws.com/metro-extracts.mapzen.com/acoruna_spain.osm.bz2
	mv acoruna_spain.osm.bz2 target
	bzip2 -f -d target/acoruna_spain.osm.bz2
	rm -f acoruna_spain.osm.bz2

download_osm2po: target
	@echo '==='
	@echo '=== Download osm2po'
	@echo '==='
	wget http://osm2po.de/dld/osm2po-5.0.0.zip
	mv osm2po-5.0.0.zip target
	rm -rf target/osm2po
	unzip target/osm2po-5.0.0.zip -x osm2po.config -d target/osm2po
	rm -f target/osm2po-5.0.0.zip
	cp osm2po.config target/osm2po

download: download_osmosis download_osm download_osm2po

osmosis: 
	@echo '==='
	@echo '=== pgsnapshot_schema'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f target/osmosis/script/pgsnapshot_schema_0.6.sql

	@echo '==='
	@echo '=== pgsnapshot_schema_linestring'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f target/osmosis/script/pgsnapshot_schema_0.6_linestring.sql

	@echo '==='
	@echo '=== Import OSM Data to DB'
	@echo '==='
	./target/osmosis/bin/osmosis --read-xml target/acoruna_spain.osm --write-pgsql host=$(DB_HOST) database=$(DB_NAME) user=$(DB_USER) password=$(DB_PASS)

	@echo '==='
	@echo '=== Create index'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -c 'CREATE INDEX ways_i_tags ON ways USING gist (tags);'

osm2po: 
	@echo '==='
	@echo '===  Split OSM ways'
	@echo '==='
	java -Xmx1408m -jar target/osm2po/osm2po-core-5.0.0-signed.jar workDir=target/es_cor prefix=es_cor tileSize=x,c cmd=c target/acoruna_spain.osm

	@echo '==='
	@echo '===  Insert into PSQL'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f target/es_cor/es_cor_2po_4pgr.sql

hermes: 
	@echo '==='
	@echo '===  Creating Hermes Schema'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f ../common/hermes_schema.sql

osm2HermesTN: osm2hermes.sql
	@echo '==='
	@echo '===  OSM2HermesTN'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f osm2hermes.sql

clean: 
	rm -rf target

dropdb:
	@echo '==='
	@echo '=== Drop DB' $(DB_NAME)
	@echo '==='
	PGPASSWORD=$(DB_PASS) dropdb $(PSQL_FLAGS) --if-exists

initdb: dropdb
	@echo '==='
	@echo '=== Create DB' $(DB_NAME)
	@echo '==='
	PGPASSWORD=$(DB_PASS) createdb $(PSQL_FLAGS)

	@echo '==='
	@echo '=== Create extensions'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -c 'CREATE EXTENSION postgis; CREATE EXTENSION hstore; CREATE EXTENSION pgRouting;'

initschema:
	@echo '==='
	@echo '=== Drop Schema' $(DB_NAME) '.' $(DB_SCHEMA)
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -c 'DROP SCHEMA IF EXISTS $(DB_SCHEMA)'

	@echo '==='
	@echo '=== Create Schema' $(DB_NAME) '.' $(DB_SCHEMA)
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -c 'CREATE SCHEMA $(DB_SCHEMA)'
	