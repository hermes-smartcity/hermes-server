Stuff you need:
	- PostgreSQL version 9.1 or above
	- PostGIS extension
	- hstore extension (should come by default)
	- Java version 1.6 or above

Usage: ./osm2hermes.sh

In case you don't have execution permission to the script, you can set them with chmod u+x osm2hermes.sh

The script should by itself download all the needed tools, some OSM data and populate our Hermes model with it.

By default, it assumes there is a PostgreSQL server at localhost:5432 with a postgres admin user that does not require a password, and it creates and populates a database names hermes. You should be able to change any of this simply by modifying the variables declared at the beginning of the script.

TODO:
	- Replace the script for a makefile with a clean target.
	- Actually test it with psql passwords: it might not work at all!

