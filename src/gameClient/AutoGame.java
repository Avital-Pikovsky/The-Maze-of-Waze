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

/**
 * Default constructor, create new AutoGame with empty parameters.
 */
	public AutoGame() {
		this.d = new DGraph();
		this.my = new MyGameGUI();
	}
/**
 * Constructor that gets MyGameGUI and set the parameters by it.
 * @param my
 */
	public AutoGame(MyGameGUI my) {
		this.my = my;
		this.d = my.getDgraph();
		this.game = my.getGame();
	}
/**
 * Set this MyGameGui param from a given one.
 * @param g
 */
	public void setGui(MyGameGUI g) {
		my = g;
	}
/**
 * Method to get this GUI.
 * @return
 */
	public MyGameGUI getGUI() {
		return my;
	}
/**
 * Method to get this game.
 * @return
 */
	public game_service getGame() {
		return game;
	}
	/**
	 * Set this game param from a given one.
	 * @param g
	 */
	public void setGame(game_service game) {
		this.game = game;
	}
/**
 * Method to get this DGraph.
 * @return
 */
	public DGraph getD() {
		return d;
	}
	/**
	 * Set this d param from a given one.
	 * @param g
	 */
	public void setD(DGraph d) {
		this.d = d;
	}

	/**
	 * This method set a source and destination for a list of fruits.
	 * @param list - list of the graph fruits.
	 */
	public void allFruitToEdges(List<Fruit> list) {
		for(Fruit f : list) {
			FruitToEdge(f);
		}
	}
/**
 * This method gets a fruit as an arguments,
 * looping on the graph edges, and with math formula decides on which edge
 * that fruit exist.
 * @param f
 */
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
			double dis2f = d.getNode(src).getLocation().distance2D(f.getPos());
			double dis4f = d.getNode(dest).getLocation().distance2D(f.getPos());

			if(((disNodes - (dis2f+dis4f))<=EPSILON) && ((disNodes - (dis2f+dis4f))>=(EPSILON*-1))){
				f.setSrc(src);
				f.setDest(dest);
			}		
		}
	}

/**
 * Method that locates each robot near a fruit before the start of the game.
 */
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

/**
 * Method that decides each robot next move throughout the game.
 * The purpose of this method is to eat as many fruits in the most efficient way,
 * calculating the shortest paths and considering the other robots in the way.
 * @param list - list of the game robots.
 */
	public void AutoNextNode(List<Robot> list) {

		Fruit topWorth = null;
		double TP = Double.MAX_VALUE;
		double temp = 0;
		int nextEdge = 0;
		ArrayList<node_data> nodeList = new ArrayList<node_data>();
		for(Robot r : list) {
			for(Fruit f : my.getDgraph().fruitList) {

				temp = my.getAlgo().shortestPathDist(r.getSrc(), f.getSrc());
				if(temp/f.getValue()<TP/f.getValue()) {
					TP = temp;
					topWorth = f;
				}
			}
			if(r.getSrc()==topWorth.getSrc()) {
				game.chooseNextEdge(r.getId(), topWorth.getDest());
			}
			else {
				nodeList = (ArrayList<node_data>) my.getAlgo().shortestPath(r.getSrc(), topWorth.getSrc());	
				nextEdge = nodeList.get(1).getKey();
				game.chooseNextEdge(r.getId(), nextEdge);
			}
		}
		game.move();
	}
}
