#!/bin/sh

# Installation parameters
DB_HOST=localhost
DB_PORT=5432
DB_USER=postgres
PGPASSWORD=phoenix
DB_NAME=hermes

# Download osmosis
wget http://bretth.dev.openstreetmap.org/osmosis-build/osmosis-latest.zip
unzip osmosis-latest.zip
rm -f osmosis-latest.zip

# Initialize our DB
createdb -h $DB_HOST -p $DB_PORT -U $DB_USER $DB_NAME
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c 'CREATE EXTENSION postgis; CREATE EXTENSION hstore;'
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f script/pgsnapshot_schema_0.6.sql
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f script/pgsnapshot_schema_0.6_linestring.sql

# Download OSM data
wget https://s3.amazonaws.com/metro-extracts.mapzen.com/acoruna_spain.osm.bz2
bzip2 -d acoruna_spain.osm.bz2
rm -f acoruna_spain.osm.bz2

# Import OSM data to our DB's osmosis schema
./bin/osmosis --read-xml acoruna_spain.osm --write-pgsql host=$DB_HOST database=$DB_NAME user=$DB_USER password=$PGPASSWORD
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c 'CREATE INDEX ways_i_tags ON ways USING gist (tags);'

# Download osm2po
wget http://osm2po.de/dld/osm2po-5.0.0.zip
unzip osm2po-5.0.0.zip -x osm2po.config
rm -f osm2po-5.0.0.zip

# Split OSM ways into a PGRouting table
java -Xmx1408m -jar osm2po-core-5.0.0-signed.jar prefix=es_cor tileSize=x,c cmd=c acoruna_spain.osm
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f es_cor/es_cor_2po_4pgr.sql

# Execute our mapping SQL script
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f hermes.sql

unset PGPASSWORD