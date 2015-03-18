package main;


import java.io.File;

import org.gephi.graph.api.Graph;
import org.neo4j.graphdb.GraphDatabaseService;

public class GexfParserForNeo4jDB {

	static String PATH_TO_FILE = "";
	///home/mael/Documents/WorkplaceEclipse/ProjetSpecifique5IF/generatedFiles/im1.gexf;
	static final String PATH_TO_NEO4JDB = "/home/mael/Documents/neo4j-community-2.1.7/data/test.db";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length>0){
			PATH_TO_FILE = args[0];
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
			System.err.println("Please indicate the path to .gexf file as 1st argument");
		}
	}

}
