# GexfParserForNeo4jDB
This program reads a .gexf file and pushes the graph to a Neo4j database.

## Configuration
You'll need to :
- [Download Neo4j](http://neo4j.com/download/) , unzip it and place it in your favorite repository.
- Open the project with Eclipse
- Run the main class **GexfParserForNeo4jDB.java** with 2 parameters :
  - args[0] : *Path to the gexf file* 
  - args[1] : *Path to the Neo4j database*

Note that this program **can't** proceed if your neo4j server is already running.

Example : "/home/mael/Documents/WorkplaceEclipse/TreeGenerator/generatedFiles/chicken.gexf" "/home/mael/Documents/Neo4j/neo4j-community-2.1.7/data/test.db"

## Warnings
If the database located with the variable **PATH_TO_NEO4JDB** doesn't exist it will be created at the beginning of the program.

## Script and Runnable
The script **script.sh** is usable in order to execute the runnable **gexfparserforneo4jdb.jar** to multiple files.
Please edit it with the path to your files folder.