#!/bin/bash

filesDirectory='/home/mael/Documents/testScript/*'
dbDirectory='/home/mael/Documents/neo4j-community-2.1.7/data/test.db'
for file in $filesDirectory
	do java -jar gexfparserforneo4jdb.jar "$file" $dbDirectory
done
