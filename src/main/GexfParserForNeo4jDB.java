package main;


import org.gephi.graph.api.Graph;
import org.neo4j.graphdb.GraphDatabaseService;

public class GexfParserForNeo4jDB {

	static final String PATH_TO_FILE = "/home/mael/git/terms-analysis/generatedFiles/im10.gexf";
	static final String PATH_TO_NEO4JDB = "/home/mael/Documents/neo4j-community-2.1.7/data/test.db";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GraphDatabaseService graphDb;

		GexfParser geParser = new GexfParser(PATH_TO_FILE);
		
		Neo4jConn conn = new Neo4jConn();
		//Neo4jConn conn = new Neo4jConn(uri, port, username, password);
		
		Graph graph = geParser.getGraph();
		
		graphDb = conn.startDB(PATH_TO_NEO4JDB);
		
		conn.putGexfInDB(graphDb, graph);
		
		graphDb.shutdown();
		
		
		
	}

}
