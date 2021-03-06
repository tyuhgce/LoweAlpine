LoweAlpine pet application
=============================

Thank you for looking LoweAlpine - a high-performance rest api application for booking place in cinema based on Relation and NoSQL decision.

[![Build Status](https://secure.travis-ci.org/yiisoft/yii.png)](http://travis-ci.org/yiisoft/yii)


INSTALLATION
------------

First you’ll need to build a Java project with Maven to get a jar application. 
You can execute "clean package" maven command to do that.

And after that, you can run RestApi server by command "java -cp ... RestApi".

Please make sure you specify correct classpath in the above command. 


REQUIREMENTS
------------

The minimum requirement by LoweAlpine is that your system supports
docker. LoweAlpine has been tested with docker on Windows and Linux 
operating systems.

On Windows operating system you should check environment varibales,
enables shared drives and expose daemon on tcp://localhost:2375 without TLS.

| Name | Value |
| --- | --- |
| DOCKER_COMPOSE_LOCATION | C:\Program Files\Docker\Docker\resources\bin\docker-compose.exe |
| DOCKER_LOCATION | C:\Program Files\Docker\Docker\resources\bin\docker.exe |
| DOCKER_HOST | localhost:2375 |


QUICK START
-----------

For quick look you can run tests in Test directory. 
It uses docker-compose for run several docker container with postgres and tarantool application,
for confugure database application and runs integration test scenarios.

Also there are initialaze files for database. The init scripts initialize 
the database for products being configured, creating schemas and 
importing initial data required for correct work of the application. 

Scripts are declared in 
- main/resources/postgresApp/init.sql and 
- main/resources/tarantoolApp/box.cfg.lua

FUNCTIONALITY
-----------

Rest Api contains following functionality:

- get/:cinema/:hall. <br />
This query returns all places of hall in the specific cinema.
Response in a format "0,1,0,0,1", where 0 means place is free, 1 means place is booked.
- check/:cinema/:hall/:place. <br />
This query return true if the place is booked, false otherwise.
- book/:cinema/:hall/:place. <br />
This query returns true if the place is booked succussfully, false otherwise.

ARCHITECTURE
-----------

There are following components in app:
- RestApi component: this component contains logic for get, check and book operations.
- Postgres DataBase component: instance of postgres to store data.
- Tarantool DataBase component: instance of tarantool key/value storage to store cache from postgres instance. 

<pre>
               |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
               |               ___________                 |
Client <--------------------->|  RestApi  |                |
               |              | Component |                |
               |               ‾‾|‾‾‾‾‾|‾‾                 |
               |                 |     |                   |
               |          |‾‾‾‾‾‾       ‾‾‾‾‾‾|            |
               |   _______|___              __|________    |
               |  |  Postgres |            | Tarantool |   |
               |  |  DataBase |            |  DataBase |   |
               |   ‾‾‾‾‾‾‾‾‾‾‾              ‾‾‾‾‾‾‾‾‾‾‾    |
               |___________________________________________|
</pre>

WHAT'S NEXT
-----------

- Implement centralized service for maintaining configuration information, naming, providing distributed synchronization, and providing group services like Zookeeper or anything like that..
- Implement system for automating deployment, scaling, and management of multiple containerized copies this application.

Created by
Marinich Roman
