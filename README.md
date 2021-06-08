# pdo

A general data organizer app

## Prerequisites

* Install tomcat
* Download maven (apache-maven-3.5.3-bin.zip) from http://maven.apache.org/download.cgi
* Unzip it then add the apache folder to C:\
* Add M2_HOME and MAVEN_HOME system variables that points to the maven dir
* Update PATH variable, append Maven bin folder – %M2_HOME%\bin, so that you can run the Maven’s command everywhere.
* Done, to verify it, run mvn –version in the command prompt.
* Install postgresql
* Create a new database ( centralpointdb ) and a 2 new schemas ( ms_main and ms_test ) by running the scripts.
* Install DBeaver
* Execute the setup SQL scripts from "src\main\resources\scripts"
* In "application.properties" add the line referring to the database resource path. Something like this: imageResource = D:/Home/dataset/db_resources/
* Install node and npm from https://nodejs.org/en/
* Install angular: npm install -g @angular/cli

## Script

* Review the global variables in "startup.py" script
* Run it: python startup.py

## Build backend

* To build: mvn clean install
* To clean the project: mvn clean


## Run http server

* npm install -g http-server
* Go to the root folder that you want to serve you files and type: http-server ./


## Run backend

* Take the war file and place it in tomcat WEB-INF folder.
* Start the server: startup
* Start the server in debug: catalina.bat jpda start
* To use other port open server.xml and change the 8080 port to any other port you want.
* To access the site go to: http://localhost:8080/centralpoint/


## Start frontend

* First, run a npm install
* npm install select2
* npm install jquery --save
* To start the frontend run: ng serve --host 0.0.0.0 --port 3000
* Run: http://localhost:3100
* To generate a new service run: ng generate service <service name>
* To generate a new component run: ng generate component <component name>
* If you want to download a new module you can run: npm install <module name> --save