package gameClient;

import java.util.ArrayList;
import java.util.List;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.edge_data;
import dataStructure.node_data;
import elements.Fruit;
import elements.Robot;
import utils.Point3D;

public class Game_Algo {
	private MyGameGUI my = new MyGameGUI();
	double EPSILON = 0.000001;
	private game_service game = Game_Server.getServer(17);
	private DGraph d = new DGraph();


	//*********************Constructors*************************
	public Game_Algo(MyGameGUI my) {
		this.my = my;
		this.d = my.getDgraph();
	}

	public void setGui(MyGameGUI g) {
		my = g;
	}

	public MyGameGUI getMy() {
		return my;
	}

	public void setMy(MyGameGUI my) {
		this.my = my;
	}

	public game_service getGame() {
		return game;
	}

	public void setGame(game_service game) {
		this.game = game;
	}

	public DGraph getD() {
		return d;
	}

	public void setD(DGraph d) {
		this.d = d;
	}
	
	public void allFruitToEdges(List<Fruit> list) {
		for(Fruit f : list) {
			FruitToEdge(f);
		}
	}
	
	public void FruitToEdge(Fruit f) {
		for(edge_data edge : my.getDgraph().allEdges) {
			int src = edge.getSrc();
			int dest = edge.getDest();
			double disNodes = my.getDgraph().getNode(src).getLocation().distance2D(my.getDgraph().getNode(dest).getLocation());

			double dis2f = my.getDgraph().getNode(src).getLocation().distance2D(f.getPos());
			double dis4f = my.getDgraph().getNode(dest).getLocation().distance2D(f.getPos());

			if(disNodes >= (dis2f+dis4f)+EPSILON) {
				f.setSrc(src);
				f.setDest(dest);
			}		
		}
	}

	public void addAutoRobot() {

		allFruitToEdges(d.fruitList);
		for(int i=0; i< d.getNumRobot();i++) {
			game.addRobot(d.fruitList.get(i).getSrc());

			i++;
		}
	}


	public void AutoNextNode(List<Robot> list) {
		Fruit closest = null;
		double SP = Double.MAX_VALUE;
		double temp = 0;
		int nextEdge = 0;
		ArrayList<node_data> nodeList = new ArrayList<node_data>();
		for(Robot r : list) {
			for(Fruit f : my.getDgraph().fruitList) {

				temp = my.getAlgo().shortestPathDist(r.getSrc(), f.getSrc());
				if(temp<SP) {
					SP = temp;
					closest = f;
				}
			}
			nodeList = (ArrayList<node_data>) my.getAlgo().shortestPath(r.getSrc(), closest.getSrc());
			if(nodeList.size()>1) {
				nextEdge = nodeList.get(1).getKey();
				game.chooseNextEdge(r.getId(), nextEdge);
			}
			if(nodeList.size()==1) {
				game.chooseNextEdge(r.getId(), closest.getDest());
			}
		}
		game.move();
	}
}
