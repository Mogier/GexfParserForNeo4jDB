#!/bin/bash

<<<<<<< HEAD
directoryfile='/home/mael/Documents/testScript/*'
for file in $directoryfile
	do java -jar gexfparserforneo4jdb.jar "$file"
=======
filesDirectory='/home/mael/Documents/WorkplaceEclipse/ProjetSpecifique5IF/generatedFiles/*'
dbDirectory='/home/mael/Documents/Neo4j/neo4j-community-2.1.7/data/test.db'
for file in $filesDirectory
	do java -jar gexfparserforneo4jdb.jar "$file" $dbDirectory 
	echo " "
>>>>>>> refs/heads/master
done
