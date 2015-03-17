package main;

import java.util.Vector;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.EdgeIterator;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.NodeIterator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;

public class Neo4jConn {

	public Neo4jConn() {
		// TODO Auto-generated constructor stub
	}

	public GraphDatabaseService startDB(String DBname) {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DBname );
		registerShutdownHook( graphDb );
		return graphDb;
	}
	
	public void putGexfInDB(GraphDatabaseService graphDb, Graph GEXFgraph) {
		IndexDefinition indexDefinition;
		Vector<org.neo4j.graphdb.Node> allNeoNodes = new Vector<Node>();
//		Schema schema = graphDb.schema();
//		schema.getIndexes().iterator()
//		try ( Transaction tx = graphDb.beginTx() )
//		{
//			Schema schema = graphDb.schema();
//		    indexDefinition = schema.indexFor( DynamicLabel.label( "Concept" ) )
//		            .on( "uri" )
//		            .create();
//		    tx.success();
//		}
		try ( Transaction tx = graphDb.beginTx() )
		{
		    //Generate Nodes
		    NodeIterator allGexfNodes = GEXFgraph.getNodes().iterator();		    
		    while(allGexfNodes.hasNext()) {
		    	//get the Node from the graph
		    	org.gephi.graph.api.Node currentGexfNode = allGexfNodes.next();
		    	
		    	//create and set attributes to the node pushed in the DB
		    	org.neo4j.graphdb.Node currentNeoNode = graphDb.createNode();
		    	currentNeoNode.setProperty("uri", currentGexfNode.getAttributes().getValue("uri"));
		    	currentNeoNode.setProperty("startingConcept", currentGexfNode.getAttributes().getValue("startingConcept"));
		    	
		    	//Store DB-Nodes at the same place(id) that the graph-Node in order to re-create the relationships
		    	allNeoNodes.add(currentGexfNode.getId()-1 , currentNeoNode);		    	
		    }
		    
		    //Generate Edges/Relationships
		    EdgeIterator allRelationships = GEXFgraph.getEdges().iterator();
		    while(allRelationships.hasNext()) {
		    	//Get the Edge from the graph
		    	Edge currentGexfEdge = allRelationships.next();
		    	int sourceID = currentGexfEdge.getSource().getId()-1;
		    	int targetID = currentGexfEdge.getTarget().getId()-1;
		    	String edgeLabel = currentGexfEdge.getEdgeData().getLabel();
		    	
		    	//create the Neo4j relationship
		    	org.neo4j.graphdb.Node sourceNode = allNeoNodes.get(sourceID);
		    	org.neo4j.graphdb.Node targetNode = allNeoNodes.get(targetID);
    	
		    	sourceNode.createRelationshipTo(targetNode, edgeLabel.equals("equiv") ? RelTypes.EQUIV : RelTypes.PARENT);
		    }
			
		    tx.success();
		}
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
