package main;


import gexf.GexfParser;

import java.io.File;

import neo4j.Neo4jConn;

import org.gephi.graph.api.Graph;
import org.neo4j.graphdb.GraphDatabaseService;

public class GexfParserForNeo4jDB {

	static String PATH_TO_FILE = "";
	static String PATH_TO_NEO4JDB = "";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length>1){
			PATH_TO_FILE = args[0];
			PATH_TO_NEO4JDB = args[1];
			GraphDatabaseService graphDb;

			GexfParser geParser = new GexfParser(PATH_TO_FILE);		
			Neo4jConn conn = new Neo4jConn();
			
			Graph graph = geParser.getGraph();
			
			
			File f = new File(PATH_TO_NEO4JDB);
			//Test if DB already exist
			//if so, nothing more to do
			if (f.exists() && f.isDirectory()) { 
				System.out.println("DB already exists, starting ...");
				graphDb = conn.startDB(PATH_TO_NEO4JDB);
			}
			//Else, we need to configure the DB (indexes, constraints...)
			else {
				System.out.println("DB has to be configured, processing ...");
				graphDb = conn.startDBPlusConfiguration(PATH_TO_NEO4JDB);
			}		
			
			conn.putGexfInDB(graphDb, graph);
			
			graphDb.shutdown();
		}
		else {
			System.err.println("Please indicate the path to .gexf file as 1st argument and path to neo4j db as 2nd.");
		}
	}

}
