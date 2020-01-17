package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.game_service;
import elements.*;
import utils.Point3D;
public class DGraph implements Serializable,graph {

	private int edgeSize = 0;
	private int mc = 0;
	private int numRobot = 0;

	/**
	 * A hash map that contains all the nodes in this DGraph as values,
	 *  and their key's are the map keys.
	 */
	public HashMap<Integer, node_data> nodes = new HashMap<>();
	/**
	 * An arraylist thats contain all the fruits.
	 */
	public ArrayList<Fruit> fruitList = new ArrayList<Fruit>();
	/**
	 * An arraylist thats contain all the robots.
	 */
	public ArrayList<Robot> robotList = new ArrayList<Robot>();

	public ArrayList<edge_data> allEdges = new ArrayList<edge_data>();
	
	@Override
	public node_data getNode(int key) {
		if(nodes.get(key)!=null) {
			return nodes.get(key);
		}
		else return null;
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if((nodes.get(src)!=null)&&(nodes.get(dest)!=null)){
			return ((Node) getNode(src)).neighbours.get(dest);
		}
		else return null;
	}

	@Override
	public void addNode(node_data n) {

		if(nodes.containsKey(n.getKey())) {
			throw new RuntimeException("node with this key is already exist");
		}
		else {
			nodes.put(n.getKey(), (Node) n);
			mc++;
		}				
	}

	@Override
	public void connect(int src, int dest, double w) {
		if((nodes.get(src)==null) || (nodes.get(dest)==null)) {
			throw new RuntimeException("Invalid input");
		}
		if(((Node)nodes.get(src)).neighbours.containsKey(dest)) {
			throw new RuntimeException("There is already an edge");
		}

		Edge e = new Edge(src, dest, 0, w , null);
		edgeSize++;
		allEdges.add(e);
		((Node)nodes.get(src)).neighbours.put(dest, e);
		mc++;
	}

	@Override
	public Collection<node_data> getV() {
		return nodes.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		return ((Node) nodes.get(node_id)).neighbours.values();
	}

	@Override
	public node_data removeNode(int key) {
		if(nodes.containsKey(key)) {

			edgeSize-=((Node)nodes.get(key)).neighbours.values().size();

			((Node)nodes.get(key)).neighbours.clear();
			Iterator<Integer> it = nodes.keySet().iterator();
			while(it.hasNext()) {
				removeEdge(it.next(), key);
			}	
			mc++;
			return nodes.remove(key);

		}
		else
			return null;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		if(((Node)nodes.get(src)).neighbours.containsKey(dest)) {
			edgeSize--;
			allEdges.remove(((Node)nodes.get(src)).neighbours.get(dest));
			mc++;
			return ((Node)nodes.get(src)).neighbours.remove(dest);
		}
		else
			return null;
	}

	@Override
	public int nodeSize() {
		return nodes.size();
	}

	@Override
	public int edgeSize() {
		return edgeSize;
	}

	@Override
	public int getMC() {
		return mc;
	}
	/**
	 * This method adds a fruit to the graph.
	 * @param f
	 */
	public void addFruit(Fruit f) {
		fruitList.add(f);
	}
	/**
	 * This method adds a fruit to the graph.
	 * @param r
	 */
	public void addRobot(Robot r) {
		robotList.add(r);
	}

	public int getNumRobot() {
		return numRobot;
	}

	public void setNumRobot(int numRobot) {
		this.numRobot = numRobot;
	}

	/**
	 * An iterator that loop on the nodes of a DGraph.
	 * @return the iterator.
	 */
	public Iterator<node_data> nodeitr() {
		return nodes.values().iterator();
	}
}