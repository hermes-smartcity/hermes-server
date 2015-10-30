Usage:
  mvn clean drops the database
  mvn install creates the database and the schemas 
  
To do:
  mvn install over an existing database fails because the utility functions cannot be dropped as they are already used