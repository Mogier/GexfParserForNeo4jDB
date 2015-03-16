package main;

import org.gephi.graph.api.Graph;

public class GexfParserForNeo4jDB {

	static final String PATH_TO_FILE = "/home/mael/git/terms-analysis/generatedFiles/im2.gexf";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GexfParser geParser = new GexfParser(PATH_TO_FILE);
		
		Graph graph = geParser.getGraph();
		
	}

}
