package eu.sisob.components.fruchtermanreingold;

import java.awt.Dimension;
import java.util.Iterator;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import eu.sisob.api.parser.sisob.SGFParser;
import eu.sisob.api.visualization.format.graph.fields.Edge;
import eu.sisob.api.visualization.format.graph.fields.EdgeSet;
import eu.sisob.api.visualization.format.graph.fields.Node;
import eu.sisob.api.visualization.format.graph.fields.NodeSet;
import eu.sisob.api.visualization.VisualizationTechnique;
import eu.sisob.api.visualization.technique.interfaces.NodeSetUpdate;
import eu.sisob.api.visualization.technique.interfaces.ServerSideCalculation;
import eu.sisob.components.framework.json.util.JSONFile;

public class FruchtermanReingoldTechnique extends VisualizationTechnique implements ServerSideCalculation, NodeSetUpdate{

	private NodeSet nodeset;
	private EdgeSet edgeset;
	
	public FruchtermanReingoldTechnique(JSONFile network) {
		super(network);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void applyLayout(int w, int h)	throws Exception {
		// TODO Auto-generated method stub
		
		FRLayout<Node, Edge> fr = new FRLayout <Node, Edge> (convertJSONDataToGraph(super.getNetwork()));
		fr.setSize(new Dimension(w,h));
		fr.setMaxIterations(Integer.MAX_VALUE);		
		fr.setRepulsionMultiplier(0.50);
		fr.setAttractionMultiplier(0.50);
	    
	    final VisualizationModel<Node,Edge> visualizationModel =  new DefaultVisualizationModel<Node,Edge>(fr, new Dimension(w,h));
	    VisualizationViewer<Node,Edge> vv =  new VisualizationViewer<Node,Edge>(visualizationModel, new Dimension(w,h));	    

	    Iterator <Node> vertexIterator = fr.getGraph().getVertices().iterator();
	    while(vertexIterator.hasNext()){			
	    	Node n = vertexIterator.next();
	    	n.setCoordinates(fr.transform(n).getX()+"", fr.transform(n).getY()+"");			
		}    
	    
	}	
	
	private Graph<Node,Edge> convertJSONDataToGraph(JSONFile network)throws Exception{		
		Graph<Node, Edge> graph  = new SparseGraph<Node,Edge>();
		SGFParser parser = new SGFParser();
		parser.setNetwork(network);
		parser.parse();
		
		this.nodeset = parser.getParsedNodeSet();
		
		for(Node n:nodeset.getValues()){
			graph.addVertex(n);
		}
		
		this.edgeset = parser.getParsedEdgeSet();
		
		for(Edge e:edgeset.getValues()){
			 Node source = locateNode(e.getSource(),nodeset);
			 Node target = locateNode(e.getTarget(),nodeset);
			
			graph.addEdge(e, source,target);
		}
		
		return graph;
	}

	private Node locateNode(String source, NodeSet nodeset) {
		// TODO Auto-generated method stub		
		for(Node n:nodeset.getValues()){
			if(n.getId().trim().equals(source.trim()))
				return n;
		}
		
		return null;
	}


	@Override
	public NodeSet updatedNodeSet() {
		// TODO Auto-generated method stub
		return this.nodeset;
	}

}
