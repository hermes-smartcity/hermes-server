Stuff you need:
	- PostgreSQL version 9.1 or above
	- PostGIS extension
	- PGRouting
	- shp2pgsql
	- SHP files to import

Usage: ./shp2hermes.sh

Notice that for efficiency reasons two materialized views are created in shp2hermes.sql: es_avi_intersections and es_avi_links. Both can be removed after the script is finished, they are only kept around for debugging.

TODO:
	- Replace the script for a makefile with a clean target.
	- Refactor the SQL script, separate schema from data and make the schema common.
