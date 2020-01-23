package gameClient;

import java.util.ArrayList;
import java.util.List;


import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import elements.Fruit;
import elements.Fruit_Comperator;
import elements.Robot;
/**
 * This class represent the auto game mode,
 * it contains algorithms that suppose to maximize the score on each stage of the game.
 * It places the robots on the best starters positions, near the fruits,
 * and move them on the graph towards the next fruits.
 * @author Avital Pikovsky & Omer Katz
 *
 */
public class AutoGame {
	private MyGameGUI my = new MyGameGUI();
	private Fruit_Comperator fc = new Fruit_Comperator();
	double EPSILON = 0.00000001;
	private game_service game = Game_Server.getServer(Json_Updates.mu);
	private DGraph d = new DGraph();


	//********Constructors********

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
	public void addAutoRobot(){
		int src =0;
		int dest =0;
		allFruitToEdges(d.fruitList);
		d.fruitList.sort(fc);
		for(int i=0; i< d.getNumRobot();i++) {

			src = d.fruitList.get(i).getSrc();
			dest = d.fruitList.get(i).getDest();
			game.addRobot(src);
			d.addRobot(new Robot(1, src, dest, i,d.getNode(src).getLocation()));

		}
	}
/**
 * This method gets a robot and a list of fruit,
 * and decides for the robot the best fruit to go after,
 * and removes this fruit from the list.
 * @param r- the current robot.
 * @param list = the list of the graph fruits.
 * @return
 */
	private Fruit closestFruit(Robot r, List<Fruit> list) {
		list.sort(fc);
		Fruit closest = list.get(0);
		double temp =0;
		double tmpFruit = Double.MAX_VALUE;
		for(int i=0; i<list.size()-1;i++) {
			temp = my.getAlgo().shortestPathDist(r.getSrc(), list.get(i).getSrc());

			if(temp<tmpFruit){

				tmpFruit = temp;

				if(!(list.contains(closest)))
					list.add(closest);

				closest = list.get(i);
				list.remove(list.get(i));
			}
		}
		return closest;
	}
/**
 * This method gets a list of the graph fruits,
 * creating a new list of fruits that will contain only 
 * the fruits that positioned on the left side of the graph.
 * 
 * @param list
 * @return the left list
 */
	private List<Fruit> leftZone(List<Fruit> list){
		ArrayList<Fruit> left = new ArrayList<Fruit>();
		for(Fruit f: list) {
			if(f.getPos().x()<35.200160479418884) 
				left.add(f);	
		}
		return left;
	}
	/**
	 * This method gets a list of the graph fruits,
	 * creating a new list of fruits that will contain only 
	 * the fruits that positioned on the right side of the graph.
	 * 
	 * @param list
	 * @return the right list
	 */
	private List<Fruit> rightZone(List<Fruit> list){
		ArrayList<Fruit> right = new ArrayList<Fruit>();
		for(Fruit f: list) {
			if(f.getPos().x()>=35.200160479418884) 
				right.add(f);	
		}
		return right;
	}
	/**
	 * Method that decides each robot next move throughout the game.
	 * The purpose of this method is to eat as many fruits in the most efficient way,
	 * calculating the shortest paths and considering the other robots in the way.
	 * @param list - list of the game robots.
	 */
	public void AutoNextNode(List<Robot> list) {
		Fruit closest = null;
		ArrayList<Fruit> left = (ArrayList<Fruit>) leftZone(d.fruitList);
		ArrayList<Fruit> right = (ArrayList<Fruit>) rightZone(d.fruitList);

		int nextEdge = 0;
		ArrayList<node_data> nodeList = new ArrayList<node_data>();
		for(Robot r : list) {
			if(list.size()>1) {
			if(r.getId()==1) {
				if(!left.isEmpty())
					closest= closestFruit(r, left);
			}
			else if(r.getId()==2) {
				if(!right.isEmpty())
					closest= closestFruit(r, right);
			}
			else
				closest = closestFruit(r,d.fruitList);
			}
			else
			closest = closestFruit(r,d.fruitList);
			
			double fromFruit = r.getPos().distance2D(closest.getPos());
			if(fromFruit<0.0013) 
				MyGameGUI.sleepTime = 60; //near the fruit

			else
				MyGameGUI.sleepTime = 105;

			if(r.getSrc()==closest.getSrc()) {
				game.chooseNextEdge(r.getId(), closest.getDest());
			}
			else {
				nodeList = (ArrayList<node_data>) my.getAlgo().shortestPath(r.getSrc(), closest.getSrc());	
				nextEdge = nodeList.get(1).getKey();
				game.chooseNextEdge(r.getId(), nextEdge);
			}
		}
	}
}
