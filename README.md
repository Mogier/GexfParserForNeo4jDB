# GexfParserForNeo4jDB
This program read a .gexf file and push the graph to a Neo4j database.

## Configuration
Please call the main class **GexfParserForNeo4jDB.java** with 2 parameters :
- Path to the gexf file 
- Path to the Neo4j database
You'll need to [download Neo4j](http://neo4j.com/download/) , unzip it and place it in your favorite repository.

Note that this program **can't** proceed if your neo4j server is already running.

## Warnings
If the database located with the variable **PATH_TO_NEO4JDB** doesn't exist it will be created at the beginning of the program.

## Script and Runnable
The script **script.sh** is usable in order to execute the runnable **gexfparserforneo4jdb.jar** to multiple files.
Please edit it with the path to your files folder.