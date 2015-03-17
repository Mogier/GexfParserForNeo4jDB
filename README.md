# GexfParserForNeo4jDB
This program read a .gexf file and push the graph to a Neo4j database.

# Configuration
Please edit the main class **GexfParserForNeo4jDB.java** with your personnal parameters.
You'll need to [download Neo4j](http://neo4j.com/download/) , unzip it and place it in your favorite repository.

Note that this program **can't** proceed if your neo4j server is already running.

#Warnings
If the database located with the variable **PATH_TO_NEO4JDB** doesn't exist it will be created at the beginning of the program. Please note that for the moment, Nodes aren't unique so if you run the same code several times, your nodes and edges will be duplicated.