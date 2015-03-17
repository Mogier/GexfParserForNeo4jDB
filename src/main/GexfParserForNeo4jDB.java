package main;


import java.io.File;
import java.nio.file.Path;

import org.gephi.graph.api.Graph;
import org.neo4j.graphdb.GraphDatabaseService;

public class GexfParserForNeo4jDB {

	static final String PATH_TO_FILE = "/home/mael/git/terms-analysis/generatedFiles/obamaAndJaguar.gexf";
	static final String PATH_TO_NEO4JDB = "/home/mael/Documents/neo4j-community-2.1.7/data/test.db";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
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

}
