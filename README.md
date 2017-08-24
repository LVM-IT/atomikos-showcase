# atomikos_connection_problem

This repository contains some JUnit Test demonstrating current problems with atomikos and Datasource Connections

## Launching the databases
This project contains a docker-compose file, which defines a
DB2 Express and a Postgresql Database. To run the tests, both
databases have to be started with:
`docker-compose up -d`

To stop the docker containers, execute
`docker-compose down`

You can remove the containers with
`docker-compose rm -v`

## Initial Tables 
The initial structures are created from SQL scripts in the directories initdb-postgres.d for postgresql and init.db for db2.


