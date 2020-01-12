package algorithms;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.text.html.HTMLDocument.Iterator;

import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import dataStructure.DGraph;
import dataStructure.Node;
/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author Avital Pikovsky, Omer Katz
 *
 */
public class Graph_Algo implements graph_algorithms,Serializable{

	public graph g;

	//*************constructors**************
	/**
	 * Default constructor, create a new reset Graph_Algo.
	 */
	public Graph_Algo() {
		g = new DGraph();
	}

	/**
	 * Constructor that gets a graph and create a new Graph_Algo with him.
	 * @param g.
	 */
	public Graph_Algo(graph g) {
		this.g = g;
	}

	@Override
	public void init(graph ga) {
		this.g = ga;
	}

	@Override
	public void init(String file_name) {

		try
		{    
			FileInputStream file = new FileInputStream(file_name); 
			ObjectInputStream in = new ObjectInputStream(file); 

			g = (DGraph) in.readObject(); 

			in.close(); 
			file.close(); 

			System.out.println("Object has been deserialized"); 
		} 

		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 

		catch(ClassNotFoundException ex) 
		{ 
			System.out.println("ClassNotFoundException is caught"); 
		}
	}


	@Override
	public void save(String file_name) {

		try
		{    
			FileOutputStream file = new FileOutputStream(file_name); 
			ObjectOutputStream out = new ObjectOutputStream(file); 

			out.writeObject(g);  

			out.close(); 
			file.close(); 

			System.out.println("Object has been serialized"); 
		}   
		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		}
	}

	/**
	 * This method resets all the nodes tags to 0.
	 */
	private void setTag() {
		for (node_data nodes : g.getV()) {
			nodes.setTag(0);
		}
	}
	@Override
	public boolean isConnected() {
		if(g.nodeSize()<=1) {
			return true;
		}

		Queue<Node> q=new ArrayBlockingQueue<Node>(g.nodeSize());
		setTag();

		for (node_data node : g.getV() ) {
			Node n=(Node) node;
			if (n.neighbours.values()== null) return false;
			q.add(n);
			n.setTag(1);
			while (!q.isEmpty()) {
				for (edge_data edge : q.peek().neighbours.values()) {
					Node dest=(Node) g.getNode(edge.getDest());
					if(dest.getTag()==0) {
						dest.setTag(1);
						q.add(dest);
					}
				}
				q.remove();
			} 
			for (node_data nodes : g.getV()) {
				if (nodes.getTag()==0) return false;
				else nodes.setTag(0);
			}
		}
		return true;
	}


	@Override
	public double shortestPathDist(int src, int dest) {
		if(src==dest)
			return 0;
		String info = "";

		for (node_data nodes : g.getV()) {
			nodes.setWeight(Double.POSITIVE_INFINITY);
			nodes.setTag(0);
		}
		g.getNode(src).setWeight(0);

		STPD(src, dest, info);
		return g.getNode(dest).getWeight();

	}

	/**
	 * This is the recursive method thats get a src of a node and dest of another node,
	 * and calculating the shortest path from the src node to his dest.
	 * @param src - represent the start node.
	 * @param dest - represent the final node.
	 * @param info - a string that helps to store the path that have past at each point of the way.
	 */
	private void STPD(int src, int dest, String info) {
		if(g.getNode(src).getTag() == 1 && g.getNode(src) == g.getNode(dest)) {
			return;
		}
		for (edge_data edge : g.getE(src)) {

			double neWeight = edge.getWeight() + g.getNode(edge.getSrc()).getWeight();
			double oldWeight = g.getNode(edge.getDest()).getWeight();
			if(neWeight < oldWeight) {
				g.getNode(edge.getDest()).setWeight(neWeight);
				g.getNode(edge.getDest()).setInfo(info + "->" + src);

				g.getNode(edge.getSrc()).setTag(1);

				STPD(edge.getDest(), dest, info + "->" + src);
			}	
		}
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		if(shortestPathDist(src, dest) == Double.POSITIVE_INFINITY)
			return null;

		List<node_data> list = new ArrayList<>();

		String s = g.getNode(dest).getInfo();
		s = s.substring(2);
		String[] arr =s.split("->");
		for (int i = 0; i < arr.length; i++) {
			list.add(g.getNode(Integer.parseInt(arr[i])));
		}
		list.add(g.getNode(dest));
		return list;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		if(targets.isEmpty()) return null;

		List<node_data> targetsToNode = new ArrayList<>();
		for(Integer tar : targets) {
			if(!(targetsToNode.contains(g.getNode(tar))))
			targetsToNode.add(g.getNode(tar));
		}

		if(targets.size()==1) return targetsToNode;

		Queue<Node> q=new ArrayBlockingQueue<Node>(targets.size());
		for (node_data nodes : targetsToNode) {
			nodes.setTag(0);
		}

		for (node_data node : targetsToNode) {
			Node n=(Node) node;
			if (n.neighbours.values()== null) return null;
			q.add(n);
			n.setTag(1);
			while (!q.isEmpty()) {
				for (edge_data edge : q.peek().neighbours.values()) {
					Node dest=(Node) g.getNode(edge.getDest());
					if(dest.getTag()==0) {
						dest.setTag(1);
						q.add(dest);
					}
				}
				q.remove();
			} 
			for (node_data nodes : targetsToNode) {
				if (nodes.getTag()==0) return null;
				else {nodes.setTag(0);
				}
			}
			setTag();
		}

		List<node_data> finaList = new ArrayList<>();

		while(targetsToNode.size()>1) {
			int src =targetsToNode.get(0).getKey();
			int dest = targetsToNode.get(1).getKey();
			List<node_data> list = shortestPath(src, dest);

			for(node_data node : list) {
				if(targetsToNode.contains(node)&&targetsToNode.size()>1) {
					if(targetsToNode.size()!=1) {					
						targetsToNode.remove(node);					
					}
				}
				finaList.add(node);
			}
		}
		if(!finaList.contains(targetsToNode.get(targetsToNode.size()-1)))
			finaList.add(targetsToNode.get(targetsToNode.size()-1));

		setTag();
		return finaList;
	}

	@Override
	public graph copy() {
		String file="copy.txt";
		save(file);
		Graph_Algo newGraph =new Graph_Algo();
		newGraph.init(file);
		return newGraph.g;
	}

}