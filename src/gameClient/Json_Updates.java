package gameClient;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import elements.Fruit;
import elements.Robot;
import utils.Point3D;

public class Json_Updates {
	
	private DGraph d = new DGraph();
	private game_service game = Game_Server.getServer(17); // you have [0,23] games.
	
public Json_Updates(MyGameGUI my) {
	this.d = my.getDgraph();
	this.game = my.getGame();
}

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

