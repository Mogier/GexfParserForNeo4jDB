package gexfparserforneo4jdb.gexf;

import java.io.File;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;


public class GexfParser {
	
	protected String filePath;	

	public GexfParser(String filePath) {
		super();
		this.filePath = filePath;
	}

	public Graph getGraph() {
		//Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		//Get models and controllers for this new workspace - will be useful later
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);

		//Import file       
		Container container;
		try {
		    File file = new File(filePath);
		    container = importController.importFile(file);
		    container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);   //Force DIRECTED
		    System.out.println("File open : " + file.getCanonicalPath());
		} catch (Exception ex) {
		    ex.printStackTrace();
		    return null;
		}
		
		//Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		//See if graph is well imported
		DirectedGraph graph = graphModel.getDirectedGraph();
		System.out.println("Nodes: " + graph.getNodeCount());
		System.out.println("Edges: " + graph.getEdgeCount());
		
		return graph;
	}
	
}
