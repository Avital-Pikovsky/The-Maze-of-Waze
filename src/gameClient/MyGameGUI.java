package gameClient;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.graph;
import gui.Graph_GUI;
import utils.Point3D;

public class MyGameGUI {
	private graph g;
	private Graph_GUI gui;
	int scenario_num = 2;//game number, change LATERRR
	game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games

	public graph getG() {
		return g;
	}
	public void setG(graph g) {
		this.g = g;
	}
	public Graph_GUI getGui() {
		return gui;
	}
	public void setGui(Graph_GUI gui) {
		this.gui = gui;
	}

	private void init(String s) {
		String info = game.toString();//Game stats.
		String g = game.getGraph();//The graph of this game.
		JSONObject line;
		DGraph d = new DGraph();
		try {
			line = new JSONObject(g);
			JSONArray nodearray = new JSONArray("Nodes");
			for (int i = 0; i < nodearray.length(); i++) {
				JSONObject jsonode = nodearray.getJSONObject(i);
				Object p = jsonode.getString("pos");
				Point3D pos= new Point3D(p.toString());
				int id = jsonode.getInt("id");

				d.addNode(new Node(id, pos, 0, "", 0));
			}
			JSONArray edgearray = new JSONArray("Edges");
			for (int i = 0; i < edgearray.length(); i++) {
				JSONObject jsonedge = edgearray.getJSONObject(i);
				int src = jsonedge.getInt("src");
				int dest = jsonedge.getInt("dest");
				double w = jsonedge.getDouble("w");

				d.connect(src, dest, w);
			}
			// the list of fruits should be considered in your solution
			Iterator<String> f_iter = game.getFruits().iterator();
			while(f_iter.hasNext()) {
				System.out.println(f_iter.next());
				}	

		}
		catch (JSONException e) {e.printStackTrace();}

	}

}
