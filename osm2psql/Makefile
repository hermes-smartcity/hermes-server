DB_HOST=localhost
DB_PORT=5432
DB_USER=***REMOVED***
DB_PASS=***REMOVED***
DB_NAME=meco1401_imposm
#OSM_FILE=avila_spain.osm
OSM_FILE=acoruna_spain.osm
PSQL_FLAGS=-h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) $(DB_NAME)

all: download osm2psql

target:
	mkdir target

download: target
	@echo '==='
	@echo '=== Download OSM data'
	@echo '==='
	wget https://s3.amazonaws.com/metro-extracts.mapzen.com/$(OSM_FILE).bz2
	mv $(OSM_FILE).bz2 target
	bzip2 -f -d target/$(OSM_FILE).bz2
	rm -f $(OSM_FILE).bz2

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
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -c 'CREATE EXTENSION postgis; CREATE EXTENSION hstore;'

imposm:
	@echo '==='
	@echo '=== imposm'
	@echo '==='
	imposm --read target/$(OSM_FILE) --cache-dir=target --merge-cache --mapping-file=hermesmapping.py 
	imposm --write --cache-dir=target --connection postgis://$(DB_USER):$(DB_PASS)@$(DB_HOST)/$(DB_NAME) --mapping-file=hermesmapping.py

osm2psql: initdb imposm

clean: 
	rm -rf target
