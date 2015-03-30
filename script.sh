#!/bin/bash

filesDirectory='/home/mael/Documents/WorkplaceEclipse/ProjetSpecifique5IF/generatedFilesDog/*'
dbDirectory='/home/mael/Documents/Neo4j/neo4j-community-2.2.0-RC01/data/test.db'
for file in $filesDirectory
	do java -jar gexfparserforneo4jdb.jar "$file" $dbDirectory 
	echo " "
done
