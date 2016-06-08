1) Configure Eclipse: 
    - Windows - Preferences - General - Workspace - Text file encoding: UTF-8
    - Windows - Preferences - Java - Installed JREs: 1.7.0_80
    - Windows - Preferences - Java - Compiler: 1.7
  
2) Configure the system so that JAVA_HOME points to that JDK

3) Create the projects in Eclipse with 

File > Import > Existing Maven Projects

4) Create a database in postgres 9.x + postgis 2.X

5) Edit hermesManager/pom.xml to indicate the database connection information

6) Execute mvn sql:execute in hermesmanager/database

7) Execute mvn clean install in hermesmanager

8) Execute mvn jetty:run in hermesmanager/eventmanager

-- In a different terminal console

9) Install Node.js

10) Execute npm install in hermesmanager/viewlayer

11) Execute npm install gulp in hermesmanager/viewlayer

12) Execute gulp build in hermesmanager/viewlayer 

13) Open http://localhost:8888/ in a web browser 