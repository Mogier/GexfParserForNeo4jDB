#!/bin/bash

directoryfile='/home/mael/Documents/testScript/*'
for file in $directoryfile
	do java -jar gexfparserforneo4jdb.jar "$file"
done
