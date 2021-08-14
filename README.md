WELCOME to CassandraUnit
========================

*Note that this project was forked from [jsevellec](https://github.com/jsevellec/cassandra-unit) and cassandra 4.x modifications made by [wakingrufus](https://github.com/wakingrufus/cassandra-unit)*

Everything is in the wiki : 
TODO: A new, updated wiki will be created to address modifications made to the original library.

What is it?
-----------
Like other *Unit projects, CassandraUnit is a Java utility test tool.
It helps you create your Java Application with [Apache Cassandra](http://cassandra.apache.org) Database backend.
CassandraUnit is for Cassandra what DBUnit is for Relational Databases.

CassandraUnit helps you writing isolated JUnit tests in a Test Driven Development style.

Main features :
---------------
- Start an embedded Cassandra.
- Create structure (keyspace and Column Families) and load data from an XML, JSON or YAML DataSet.
- Execute a CQL script.

Where to start :
----------------
You can start by reading the wiki : 
TODO: A new, updated wiki will be created to address modifications made to the original library.

Why fork the project? :
----------------
The purpose of the forking of this library is to address the following concerns:
* Last modification of cassandraunit (originally provided by jsevellec) was made in early 2020 and does not seem to be continually maintained
* Pull requests have been submitted against the original repository, but none have been merged by original contributors since early 2020
* cassandra 3.x can only be ran on Java 8, making it difficult for organizations moving to Java 11 or higher to upgrade their applications
* cassandra 4.x is not available, but requires additional work to run (cassandra 4.x work done by wakingrufus)

Alternatives to this library :
----------------
* [Testcontainers](https://www.testcontainers.org/modules/databases/cassandra/) - For applications and organizations that run on containers (e.g. Docker), Testcontainers is the best choice for spinning up and down new Cassandra instances
* [Embedded cassandra by nosan](https://github.com/nosan/embedded-cassandra) embedded cassandra approach that allows users to have fine grained control over starting and stopping instances of Cassandra including the version, where and how the files are stored, etc. Note that this dependency does not contain Cassandra dependencies, but instead, downloads Cassandra binaries from Apache and runs that instead.

License :
---------
This project is licensed under LGPL V3.0 :
http://www.gnu.org/licenses/lgpl-3.0-standalone.html

