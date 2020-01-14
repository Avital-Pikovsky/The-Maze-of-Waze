package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import elements.*;
import utils.Point3D;
import Server.Game_Server;
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
		((Node)nodes.get(src)).neighbours.put(dest, e);
		mc++;
	}

	public void init(game_service game) {
		String info = game.toString();//Game stats.
		String g = game.getGraph();//The graph of this game.
		JSONObject line;
		System.out.println("start");
		try {
			line = new JSONObject(g);
			System.out.println("try");
			//adding nodes.
			JSONArray nodeArray =  line.getJSONArray("Nodes");
			for (int i = 0; i < nodeArray.length(); i++) {
				JSONObject jsoNode = nodeArray.getJSONObject(i);
				Object p = jsoNode.getString("pos");
				Point3D pos= new Point3D(p.toString());
				int id = jsoNode.getInt("id");
				System.out.println(pos.toString()+"node pos");

				this.addNode(new Node(id, pos, 0, "", 0));
			}

			//adding edges.
			JSONArray edgeArray = line.getJSONArray("Edges");
			for (int i = 0; i < edgeArray.length(); i++) {
				JSONObject jsonEdge = edgeArray.getJSONObject(i);
				int src = jsonEdge.getInt("src");
				int dest = jsonEdge.getInt("dest");
				double w = jsonEdge.getDouble("w");
				System.out.println();

				this.connect(src, dest, w);
			}

			//adding Fruits.
			Iterator<String> f_iter = game.getFruits().iterator();
			while(f_iter.hasNext()) {
				String f = f_iter.next();
				JSONObject fruitLine = new JSONObject(f);
				JSONObject currentFruit = fruitLine.getJSONObject("Fruit");
				double value = currentFruit.getDouble("value");
				int type = currentFruit.getInt("type");
				Object p = currentFruit.getString("pos");
				Point3D pos= new Point3D(p.toString());

				this.addFruit(new Fruit(value, type, pos));
			}

			//get number of robots
			JSONObject line2 = new JSONObject(info);
			JSONObject gameServerLine = line2.getJSONObject("GameServer");
			int rs = gameServerLine.getInt("robots");
			this.setNumRobot(rs);
			
			

		}
		catch (JSONException e){
		e.printStackTrace();
		}
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