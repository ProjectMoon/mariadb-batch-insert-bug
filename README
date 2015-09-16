Demonstration of allowMultiQueries batch insert bug with the MariaDB connector.

Works fine with the MySQL connector. If allowMultiQueries is turned on, MariaDB
cannot execute batch inserts properly and also doesn't seem to handle generated
keys.

To run:

    mvn clean install -P <mysql/mariadb>
    cd target/
    java -jar batch-insert-bug-1.0-SNAPSHOT.jar <mysql/mariadb> <iterations>

Example:

    java -jar batch-insert-bug-1.0-SNAPSHOT.jar mysql 1


Changing DB connection info: change the lines in the Java code.
