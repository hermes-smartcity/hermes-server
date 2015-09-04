Stuff you need:
	- A fresh osm2hermesTN database from the latest version. If yours is made before this file was commited, routing won't work
	- A recent Geoserver version up and running on localhost:8080
	- Properly setup a Geoserver connection and a layer. See the instructions below

Usage: Open corunna.html in a web browser. Click on two points from the city network and a route should appear soon. If it doesn't appear soon that's ok, I barely optimized anything in this version. If it doesn't appear at all maybe it's some obscure PGRouting bug, try some other two points.

Configuring Geoserver:
	- Add a new Workspace with the name meco1401_cor and URI http://lbd.org.es/mec01401/cor
	- For that Workspace, add a Data Store called meco1401_cor for your Hermes database with valid credentials
	- Add a new Layer selecting meco1401_cor Data Store
	- Click on "Configure new SQL view..."
	- Call this view pgrouting and paste into the SQL statement box the contents of the included pgrouting.sql file.
	- Click on Guess parameters from SQL. Then change all validations to ^-?[\d.]+$ and all default values to 0
	- Hit the Refresh link below and if everything went well, the two output attributes should appear
	- Make seq an identifier and centerline_geometry a LineString with SRID 4326
	- Click on Save and in the next form make sure that Enabled and Advertised are checked
	- Put EPSG:4326 in Native SRS, EPSG:3857 in Declared SRS and select Reproject native to declared in SRS handling. This is needed for OpenLayers
	- On the bounding box sections below hit both "compute" links
	- Click on save. The layer should be ready now

If you name your workspace or your layed differently, you'll have to also change the script in corunna.html to match your names.
