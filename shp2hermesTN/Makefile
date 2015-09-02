DB_HOST=localhost
DB_PORT=5432
DB_USER=***REMOVED***
DB_PASS=***REMOVED***
DB_NAME=meco1401_avi
PSQL_FLAGS=-h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) $(DB_NAME)
SHP_FILES=eje_vial.shp eje_vial.dbf eje_vial.shx

all: shp2HermesTN

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
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -c 'CREATE EXTENSION postgis; CREATE EXTENSION pgrouting;'

shp: initdb $(SHP_FILES)
	@echo '==='
	@echo '=== Import SHP data'
	@echo '==='
	shp2pgsql -s 25830 -d -I -S eje_vial.shp es_avi_streets | psql $(PSQL_FLAGS)

shp2HermesTN: initdb shp shp2hermes.sql
	@echo '==='
	@echo '===  SHP2HermesTN'
	@echo '==='
	PGPASSWORD=$(DB_PASS) psql $(PSQL_FLAGS) -f shp2hermes.sql
