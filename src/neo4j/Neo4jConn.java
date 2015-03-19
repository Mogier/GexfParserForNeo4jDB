package neo4j;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.EdgeIterator;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.NodeIterator;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.Schema;

public class Neo4jConn {

	public Neo4jConn() {
		
	}

	public GraphDatabaseService startDB(String DBname) {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DBname );
		registerShutdownHook( graphDb );
		return graphDb;
	}
	
	public GraphDatabaseService startDBPlusConfiguration(String DBname) {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DBname );
		registerShutdownHook( graphDb );
		Transaction tx = graphDb.beginTx();
		try
		{
		    Schema schema = graphDb.schema();
		  //Create unicity constraint on uris
		    schema.constraintFor( DynamicLabel.label( "Concept" ) )
            		.assertPropertyIsUnique( "uri" )
        			.create();
		    tx.success();
		} finally {
			tx.close();
		}
		
		
		
		return graphDb;
	}
	
	public void putGexfInDB(GraphDatabaseService graphDb, Graph GEXFgraph) {
		Transaction tx = graphDb.beginTx();
		try 
		{
			int nodeSuccess =0;
			int nodeFail = 0;
			Label label = DynamicLabel.label( "Concept" );
		    //Generate Nodes
		    NodeIterator allGexfNodes = GEXFgraph.getNodes().iterator();		    
		    while(allGexfNodes.hasNext()) {
		    	//get the Node from the graph
		    	org.gephi.graph.api.Node currentGexfNode = allGexfNodes.next();
		    	
		    	if(findConceptByURI((String) currentGexfNode.getAttributes().getValue("url"), graphDb)==null){
		    		//create and set attributes to the node pushed in the DB
			    	org.neo4j.graphdb.Node currentNeoNode = graphDb.createNode(label);
			    	currentNeoNode.setProperty("uri", currentGexfNode.getAttributes().getValue("uri"));
			    	currentNeoNode.setProperty("startingConcept", currentGexfNode.getAttributes().getValue("startingConcept"));
			    	
			    	nodeSuccess++;
		    	} 
		    	else {
		    		nodeFail++;
		    	}		    			    	
		    }
		    System.out.println("Nodes creation completed.");
		    System.out.println("Created : " + nodeSuccess);
		    System.out.println("Already existing : " + nodeFail);
		    
		    //Generate Edges/Relationships
	    	EdgeIterator allRelationships = GEXFgraph.getEdges().iterator();
	    	int edgesCreated=0;
	    	int edgesExists=0;
		    while(allRelationships.hasNext()) {
		    	
		    	//Get the Edge from the graph
		    	Edge currentGexfEdge = allRelationships.next();
		    	String sourceURI = currentGexfEdge.getSource().getNodeData().getLabel();
		    	String targetURI = currentGexfEdge.getTarget().getNodeData().getLabel();
		    	RelTypes edgeType = currentGexfEdge.getEdgeData().getLabel().equals("equiv") ? RelTypes.EQUIV : RelTypes.PARENT;
		    	
		    	//create the Neo4j relationship
		    	org.neo4j.graphdb.Node sourceNode = findConceptByURI(sourceURI, graphDb);
		    	org.neo4j.graphdb.Node targetNode = findConceptByURI(targetURI, graphDb);
		    	if(!relationExists(sourceNode, targetNode, edgeType)){
		    		sourceNode.createRelationshipTo(targetNode, edgeType);
		    		edgesCreated++;
		    	} else {
		    		edgesExists++;
		    	}
		    	
		    }
		    System.out.println("Edges creation completed.");
		    System.out.println("Created : " + edgesCreated);
		    System.out.println("Already existing : " + edgesExists);
			
		    tx.success();
		} finally {
			tx.close();
		}
		
	}
	
	private static Node findConceptByURI(String URI, GraphDatabaseService graphDb) {
		Node nodeResult=null;
		ExecutionEngine engine = new ExecutionEngine( graphDb );
		String query = "MATCH (concept {uri:\""+URI+"\"})" +
						"RETURN concept "+
						"LIMIT 1";
		ExecutionResult result = (ExecutionResult) engine.execute( query);
		
		ResourceIterator<Node> it = result.columnAs("concept");
		while (it.hasNext()){
			nodeResult = it.next();
		}
		return nodeResult;
	}
	
	private static boolean relationExists(Node sourceNode, Node targetNode, RelTypes type) {
		boolean exist = false;
		if(sourceNode.getRelationships(type)!=null){
			for(Relationship r : sourceNode.getRelationships(type)){
				if(r.getOtherNode(sourceNode).equals(targetNode)){
					exist = true;
					break;
				}
			}
		}	
		return exist;
	}
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
}
