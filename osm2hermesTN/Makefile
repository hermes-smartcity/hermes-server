DB_HOST=localhost
DB_PORT=5432
DB_USER=***REMOVED***
DB_PASS=***REMOVED***
DB_NAME=meco1401_prod
DB_SCHEMA=network
DB_SCHEMA_PUBLIC=public
PSQL_FLAGS=-h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) "dbname=$(DB_NAME) options=--search_path=$(DB_SCHEMA),$(DB_SCHEMA_PUBLIC)"

all: download_sw download_spa osmosis osm2po hermes osm2HermesTN

bin:
	mkdir bin

data:
	mkdir data

download_sw: download_osmosis download_osm2po

download_osmosis: bin
	@echo '==='
	@echo '=== Download osmosis'
	@echo '==='
	wget http://bretth.dev.openstreetmap.org/osmosis-build/osmosis-latest.zip
	mv osmosis-latest.zip bin
	rm -rf bin/osmosis
	unzip bin/osmosis-latest.zip -d bin/osmosis
	rm -f bin/osmosis-latest.zip

download_osm2po: bin
	@echo '==='
	@echo '=== Download osm2po'
	@echo '==='
	wget http://osm2po.de/releases/osm2po-5.1.0.zip
	mv osm2po-5.1.0.zip bin
	rm -rf bin/osm2po
	unzip bin/osm2po-5.1.0.zip -x osm2po.config -d bin/osm2po
	rm -f bin/osm2po-5.1.0.zip
	cp osm2po.config bin/osm2po

download_cor: data
	@echo '==='
	@echo '=== Download OSM data'
	@echo '==='
	wget https://s3.amazonaws.com/metro-extracts.mapzen.com/acoruna_spain.osm.bz2
	mv acoruna_spain.osm.bz2 data
	bzip2 -f -d data/acoruna_spain.osm.bz2
	rm -f acoruna_spain.osm.bz2

download_spa: data
	@echo '==='
	@echo '=== Download OSM data'
	@echo '==='
	wget http://download.geofabrik.de/europe/spain-latest.osm.bz2
	mv spain-latest.osm.bz2 data
	bzip2 -f -d data/spain-latest.osm.bz2
	rm -f spain-latest.osm.bz2

osmosis: 
	@echo '==='
	@echo '=== pgsnapshot_schema'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f bin/osmosis/script/pgsnapshot_schema_0.6.sql

	@echo '==='
	@echo '=== pgsnapshot_schema_bbox'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f bin/osmosis/script/pgsnapshot_schema_0.6_bbox.sql

	@echo '==='
	@echo '=== pgsnapshot_schema_linestring'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f bin/osmosis/script/pgsnapshot_schema_0.6_linestring.sql

	@echo '==='
	@echo '=== Import OSM Data to DB'
	@echo '==='
	./bin/osmosis/bin/osmosis --read-xml data/acoruna_spain.osm --write-pgsql-dump directory=data enableLinestringBuilder=yes enableBboxBuilder=yes
	cd data; PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f ../bin/osmosis/script/pgsnapshot_load_0.6.sql
	cd ..

	@echo '==='
	@echo '=== Create index'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS)  -c 'CREATE INDEX ways_i_tags ON ways USING gist (tags);'

osm2po: 
	@echo '==='
	@echo '===  Split OSM ways'
	@echo '==='
	java -Xmx1408m -jar bin/osm2po/osm2po-core-5.1.0-signed.jar workDir=data/es_cor prefix=es_cor tileSize=x cmd=tjsp data/acoruna_spain.osm

	@echo '==='
	@echo '===  Insert into PSQL'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f data/es_cor/es_cor_2po_4pgr.sql

	@echo '==='
	@echo '=== Create index'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS)  -c 'CREATE INDEX idx_es_cor_2po_4pgr_geom_way ON network.es_cor_2po_4pgr USING gist(geom_way);'

hermes: 
	@echo '==='
	@echo '===  Creating Hermes Utility'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f ./hermes_utility.sql

	@echo '==='
	@echo '===  Creating Hermes Schema'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f ./hermes_schema.sql

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
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -c 'DROP SCHEMA IF EXISTS $(DB_SCHEMA) CASCADE'

	@echo '==='
	@echo '=== Create Schema' $(DB_NAME) '.' $(DB_SCHEMA)
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -c 'CREATE SCHEMA $(DB_SCHEMA)'
	
