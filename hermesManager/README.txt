1) Descargar de https://git.enxenio.net/Proxectos/MECO1401.git la rama master

2) Configurar el eclipse 
    - Windows - Preferences - General - Workspace - Text file encoding: UTF-8
    - Windows - Preferences - Java - Installed JREs: 1.7.0_80
    - Windows - Preferences - Java - Compiler: 1.7
   
Nota: Si no se tiene esa versión de JDk bajarla desde http://www.oracle.com/technetwork/es/java/javase/downloads/jdk7-downloads-1880260.html

3) Configurar las variables de entorno para que el JAVA_HOME apunte a esa jdk

4) Crear los proyectos (hermesManager) en un eclipse mediante 

File > Import > Existing Maven Projects

5) Crear una base de datos en un postgres 9.x + postgis 2.X local llamada smartcity

6) Editar el pom.xml de hermesManager para indicar el puerto de la instalación de postgres

7) Ejecutar mvn sql:execute en hermesmanager/database

8) Ejecutar mvn clean install en hermesmanager

9) Ejecutar mvn jetty:run en hermesmanager/eventmanager

-- En otra consola de comandos

10) Instalar Node.js desde https://nodejs.org/en/

11) Ejecutar npm install en hermesmanager/viewlayer

12) Ejecutar npm install -g gulp en hermesmanager/viewlayer

13) Ejecutar gulp build en hermesmanager/viewlayer 

14) Abrir en un navegador http://localhost:8888/