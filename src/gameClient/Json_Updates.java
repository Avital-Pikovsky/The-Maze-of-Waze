package gameClient;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Node;
import elements.Fruit;
import elements.Robot;
import utils.Point3D;

public class Json_Updates {
	
	private DGraph d = new DGraph();
	private game_service game = Game_Server.getServer(17); // you have [0,23] games.
	
	
	//******************constructors*************************
	/**
	 * A constructor thats get MyGameGUI object and set the parameters by it.
	 * @param my
	 */
public Json_Updates(MyGameGUI my) {
	this.d = my.getDgraph();
	this.game = my.getGame();
}

/**
 * This method initializes a Graph from a game json file,
 * extracting the edges, nodes and the number of robots.
 * @param game
 */
public void init(game_service game) {
	String info = game.toString();//Game stats.
	String g = game.getGraph();//The graph of this game.
	JSONObject line;
	try {
		line = new JSONObject(g);
		//adding nodes.
		JSONArray nodeArray =  line.getJSONArray("Nodes");
		for (int i = 0; i < nodeArray.length(); i++) {
			JSONObject jsoNode = nodeArray.getJSONObject(i);
			Object p = jsoNode.getString("pos");
			Point3D pos= new Point3D(p.toString());
			int id = jsoNode.getInt("id");

			d.addNode(new Node(id, pos, 0, "", 0));
		}

		//adding edges.
		JSONArray edgeArray = line.getJSONArray("Edges");
		for (int i = 0; i < edgeArray.length(); i++) {
			JSONObject jsonEdge = edgeArray.getJSONObject(i);
			int src = jsonEdge.getInt("src");
			int dest = jsonEdge.getInt("dest");
			double w = jsonEdge.getDouble("w");
			d.connect(src, dest, w);
		}
		//get number of robots
		JSONObject line2 = new JSONObject(info);
		JSONObject gameServerLine = line2.getJSONObject("GameServer");
		int rs = gameServerLine.getInt("robots");
		d.setNumRobot(rs);
		
	}
	catch (JSONException e){
	e.printStackTrace();
	}
}

/**
 * This method extracts the current robots situation from the game json
 *  and adding it to the graph.
 */
public void updateRobots() {
	d.robotList.clear();
	List<String> r = game.getRobots();
	int i=0;
	for(String rob : r) {
		JSONObject line;
		try {
			line = new JSONObject(rob);
			JSONObject jsonRob = line.getJSONObject("Robot");
			Object p = jsonRob.getString("pos");
			Point3D pos= new Point3D(p.toString());	
			int src = jsonRob.getInt("src");
			int dest = jsonRob.getInt("dest");
			Robot roby = new Robot(1, src, dest, i, pos);
			d.addRobot(roby);
			i++;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
/**
 * This method extracts the current fruits situation from the game json
 *  and adding it to the graph.
 */
public void updateFruits() {
	d.fruitList.clear();
	List<String> f = game.getFruits();
	for(String fruit : f) {
		JSONObject line;
		try {
			line = new JSONObject(fruit);
			JSONObject jsonRob = line.getJSONObject("Fruit");
			Object p = jsonRob.getString("pos");
			Point3D pos= new Point3D(p.toString());
			int type = jsonRob.getInt("type");
			double value = jsonRob.getInt("value");

			Fruit fr = new Fruit(value, type, pos);
			d.addFruit(fr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
}

