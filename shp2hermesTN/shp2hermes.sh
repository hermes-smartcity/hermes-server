#!/bin/sh

# Installation parameters
DB_HOST=localhost
DB_PORT=5432
DB_USER=***REMOVED***
PGPASSWORD=***REMOVED***
DB_NAME=meco1401

export PGPASSWORD

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
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c 'CREATE EXTENSION postgis; CREATE EXTENSION pgrouting;'

# Import SHP data to a default schema
echo '==='
echo '=== Import SHP data'
echo '==='
shp2pgsql -s 25830 -d -I -S eje_vial.shp es_avi_streets | psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME

# Execute our mapping SQL script
echo '==='
echo '===  SHP2HermesTN'
echo '==='
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f shp2hermes.sql

unset PGPASSWORD