Stuff you need:
	- Java SE 1.7 or above
	- Maven
	- Eclipse (Recommended)
	- PostgreSQL 9.3 with PostGIS extension. If you have a different version, you may have to change the JDBC connector version in pom.xml
	- A database imported from osm2HermesTN for A Coruña

Setup:
	- I added some traffic signs manually in src/main/sql/es_cor_signs.sql. Execute the file and it should make a new table called es_cor_signs.
	- Modify the parameters from src/main/resources/postgresql.properties to match your database.
	- (Optional) Modify the starting node ID constant in RouterMain.START_NODE
	- (Optional) Modify the maximum number of iterations in BreadthFirstNavigator.MAX_ITERATIONS

Usage: Open the project in Eclipse, download any Maven dependencies and launch it. The main method is located in RouterMain.java.


TODO:
	- Test with more data!
	- Support for more signs, not just R101 (dir prohibida).
	- Make it independent from SRID, or at least extract it into a configuration.
	- (Maybe) use actual entities from our model instead of a Node and Edges abstraction.
