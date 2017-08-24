# Showcase Atomikos Problems

This repository contains test cases and an isolated docker environment to showcase problems with Atomikos.


## Launching the databases
This project contains a `docker-compose` file, which defines a DB2 Express and a Postgresql Database.
To run the tests start the docker environment using:
`docker-compose up`

To stop the docker environment use
`docker-compose down`

The initial structures are created from SQL scripts in the directories initdb-postgres.d for postgresql and init.db for db2.


# Scenarios

## Open-Close-Connection

* begin tx
* open connection, write data, close connection
* open connection, close connection
* open connection, read data
* end tx

Exception is thrown / error from Postgresq.