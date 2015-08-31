#!/bin/sh

# Installation parameters
DB_HOST=localhost
DB_PORT=5432
DB_USER=***REMOVED***
PGPASSWORD=***REMOVED***
DB_NAME=meco1401

export PGPASSWORD

# Create target
mkdir target

# Download osmosis
echo '==='
echo '=== Download osmosis'
echo '==='
wget http://bretth.dev.openstreetmap.org/osmosis-build/osmosis-latest.zip
mv osmosis-latest.zip target
rm -rf target/osmosis
unzip target/osmosis-latest.zip -d target/osmosis
rm -f target/osmosis-latest.zip

# Initialize our DB
echo '==='
echo '=== Drop DB' $DB_NAME
echo '==='
dropdb -h $DB_HOST -p $DB_PORT -U $DB_USER $DB_NAME

echo '==='
echo '=== Create DB' $DB_NAME
echo '==='
createdb -h $DB_HOST -p $DB_PORT -U $DB_USER $DB_NAME

echo '==='
echo '=== Create extensions'
echo '==='
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c 'CREATE EXTENSION postgis; CREATE EXTENSION hstore;'

echo '==='
echo '=== pgsnapshot_schema'
echo '==='
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f target/osmosis/script/pgsnapshot_schema_0.6.sql

echo '==='
echo '=== pgsnapshot_schema_linestring'
echo '==='
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f target/osmosis/script/pgsnapshot_schema_0.6_linestring.sql

# Download OSM data
echo '==='
echo '=== Download OSM data'
echo '==='
wget https://s3.amazonaws.com/metro-extracts.mapzen.com/acoruna_spain.osm.bz2
mv acoruna_spain.osm.bz2 target
bzip2 -d target/acoruna_spain.osm.bz2
rm -f acoruna_spain.osm.bz2

# Import OSM data to our DB's osmosis schema
echo '==='
echo '=== Import OSM Data to DB'
echo '==='
./target/osmosis/bin/osmosis --read-xml target/acoruna_spain.osm --write-pgsql host=$DB_HOST database=$DB_NAME user=$DB_USER password=$PGPASSWORD

echo '==='
echo '=== Create index'
echo '==='
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c 'CREATE INDEX ways_i_tags ON ways USING gist (tags);'

# Download osm2po
echo '==='
echo '=== Download osm2po'
echo '==='
wget http://osm2po.de/dld/osm2po-5.0.0.zip
mv osm2po-5.0.0.zip target
rm -rf target/osm2po
unzip target/osm2po-5.0.0.zip -x osm2po.config -d target/osm2po
rm -f target/osm2po-5.0.0.zip

# Split OSM ways into a PGRouting table
echo '==='
echo '===  Split OSM ways'
echo '==='
java -Xmx1408m -jar target/osm2po/osm2po-core-5.0.0-signed.jar workDir=target/es_cor prefix=es_cor tileSize=x,c cmd=c target/acoruna_spain.osm
echo '==='
echo '===  Insert into PSQL'
echo '==='
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f target/es_cor/es_cor_2po_4pgr.sql

# Execute our mapping SQL script
echo '==='
echo '===  OSM2HermesTN'
echo '==='
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f hermes.sql

unset PGPASSWORD
