package gameClient;

import java.util.ArrayList;
import java.util.List;


import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import elements.Fruit;
import elements.Robot;

public class AutoGame {
	private MyGameGUI my = new MyGameGUI();
	double EPSILON = 0.00000001;
	private game_service game = Game_Server.getServer(17);
	private DGraph d = new DGraph();


	//*********************Constructors*************************


	public AutoGame() {
		this.d = new DGraph();
		this.my = new MyGameGUI();
	}

	public AutoGame(MyGameGUI my) {
		this.my = my;
		this.d = my.getDgraph();
		this.game = my.getGame();
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


		for(edge_data edge : d.allEdges) {
			int src=-2;
			int dest = -2;
			if((f.getType()==-1)) {//Banana
				if(edge.getSrc()>edge.getDest()) {
					src = edge.getSrc();
					dest = edge.getDest();
				}else {
					dest = edge.getSrc();
					src = edge.getDest();
				}
			}
			else{ //Apple
				if(edge.getSrc()<edge.getDest()) {
					src = edge.getSrc();
					dest = edge.getDest();
				}else {
					dest = edge.getSrc();
					src = edge.getDest();
				}
			}
			double disNodes = d.getNode(src).getLocation().distance2D(d.getNode(dest).getLocation());
			//System.out.println(disNodes+"edge");
			double dis2f = d.getNode(src).getLocation().distance2D(f.getPos());
			//	System.out.println(dis2f+"src to f");
			double dis4f = d.getNode(dest).getLocation().distance2D(f.getPos());
			//	System.out.println(dis4f+"dest to f");

			if(((disNodes - (dis2f+dis4f))<=EPSILON) && ((disNodes - (dis2f+dis4f))>=(EPSILON*-1))){
				f.setSrc(src);
				f.setDest(dest);
			}		
		}
	}


	public void addAutoRobot() {
		int src =0;
		int dest =0;
		allFruitToEdges(d.fruitList);
		for(int i=0; i< d.getNumRobot();i++) {
			src = d.fruitList.get(i).getSrc();
			dest = d.fruitList.get(i).getDest();
			game.addRobot(src);
			d.addRobot(new Robot(1, src, dest, i,d.getNode(src).getLocation()));
		}
	}


	public void AutoNextNode(List<Robot> list) {
		for(Fruit f : d.fruitList) {
			System.out.println(f.getSrc());
			System.out.println(f.getDest());
		}
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
			if(r.getSrc()==closest.getSrc()) {
				game.chooseNextEdge(r.getId(), closest.getDest());
			}
			else {
				nodeList = (ArrayList<node_data>) my.getAlgo().shortestPath(r.getSrc(), closest.getSrc());	
				nextEdge = nodeList.get(1).getKey();
				game.chooseNextEdge(r.getId(), nextEdge);
			}
		}
		game.move();
	}
}
