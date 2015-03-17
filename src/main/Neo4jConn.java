package main;

import java.util.Vector;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.EdgeIterator;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.NodeIterator;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.ConstraintViolationException;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;

import scala.collection.Iterator;

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
		
		try ( Transaction tx = graphDb.beginTx() )
		{
		    Schema schema = graphDb.schema();
		  //Create unicity constraint on uris
		    schema.constraintFor( DynamicLabel.label( "Concept" ) )
            		.assertPropertyIsUnique( "uri" )
        			.create();
		    
		    tx.success();
		}
		
		
		return graphDb;
	}
	
	public void putGexfInDB(GraphDatabaseService graphDb, Graph GEXFgraph) {

		try ( Transaction tx = graphDb.beginTx() )
		{
			Label label = DynamicLabel.label( "Concept" );
		    //Generate Nodes
		    NodeIterator allGexfNodes = GEXFgraph.getNodes().iterator();		    
		    while(allGexfNodes.hasNext()) {
		    	//get the Node from the graph
		    	org.gephi.graph.api.Node currentGexfNode = allGexfNodes.next();
		    	
		    	if(findConceptByURI((String) currentGexfNode.getAttributes().getValue("url"), graphDb)==null){
		    		//create and set attributes to the node pushed in the DB
			    	org.neo4j.graphdb.Node currentNeoNode = graphDb.createNode(label);
			    	currentNeoNode.setProperty("uri", currentGexfNode.getAttributes().getValue("url"));
			    	currentNeoNode.setProperty("startingConcept", currentGexfNode.getAttributes().getValue("startingConcept"));
			    	
			    	System.out.println("Node created : " + currentNeoNode.toString());
		    	} 
		    	else {
		    		System.out.println("Constraint violation.");
		    	}		    			    	
		    }
		    
		    //Generate Edges/Relationships
	    	EdgeIterator allRelationships = GEXFgraph.getEdges().iterator();
		    while(allRelationships.hasNext()) {
		    	//Get the Edge from the graph
		    	Edge currentGexfEdge = allRelationships.next();
		    	String sourceURI = currentGexfEdge.getSource().getNodeData().getLabel();
		    	String targetURI = currentGexfEdge.getTarget().getNodeData().getLabel();
		    	String edgeLabel = currentGexfEdge.getEdgeData().getLabel();
		    	
		    	//create the Neo4j relationship
		    	org.neo4j.graphdb.Node sourceNode = findConceptByURI(sourceURI, graphDb);
		    	org.neo4j.graphdb.Node targetNode = findConceptByURI(targetURI, graphDb);
    	
		    	sourceNode.createRelationshipTo(targetNode, edgeLabel.equals("equiv") ? RelTypes.EQUIV : RelTypes.PARENT);
		    }
			
		    tx.success();
		}
		
	}
	
	private static Node findConceptByURI(String URI, GraphDatabaseService graphDB) {
		Node nodeResult=null;
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		String query = "MATCH (concept {uri:\""+URI+"\"})" +
						"RETURN concept "+
						"LIMIT 1";
		ExecutionResult result = engine.execute( query);
		
		ResourceIterator<Node> it = result.columnAs("concept");
		while (it.hasNext()){
			nodeResult = it.next();
		}
		return nodeResult;
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
