package gameClient;


import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;
import dataStructure.Node;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.Fruit;
import elements.Robot;
import utils.Point3D;
import utils.StdDraw;

public class MyGameGUI {
	private DGraph d = new DGraph();
	double EPSILON = 0.0002;
	private game_service game = Game_Server.getServer(17); // you have [0,23] games.
	private Graph_Algo gra;
	private graph g;

	public void paint(){
		d.init(game);
		init(d);
		drawGraph();

	}

	public game_service getGame() {
		return game;
	}

	public void setGame(game_service game) {
		this.game = game;
	}

	public graph getG() {
		return d;
	}
	public void setG(graph g) {
		this.d = (DGraph) g;
	}



	/**
	 * Default constructor, connects all the graphs(Graph_Algo and DGraph) to the StdDraw class.
	 */
	public MyGameGUI() {
		gra=new Graph_Algo();
		g=new DGraph();
		StdDraw.setGui(this);
	}
	/**
	 * A constructor that gets a graph as an argument and setting all the inner graphs by him.
	 * @param g - the given graph.
	 */
	public MyGameGUI(graph g) {
		this.g = g;	
		gra=new Graph_Algo();
		gra.init(this.g);
		StdDraw.setGui(this);

	}
	/**
	 * Initialize the Graph_GUI from given graph.
	 * @param gr - the given graph.
	 */
	public void init(graph gr) {
		this.g = gr;
		this.gra.g = gr;
	}
	/**
	 * Initialize the Graph_GUI from a string.
	 * @param name - the given string, represent a saved graph.
	 */
	public void init(String name) {
		this.gra.init(name);
		this.g=gra.g;
		drawGraph();
	}
	/**
	 * The main paint function, drawing the whole graph.
	 */
	public void drawGraph() {
		drawCanvas();
		drawEdges();
		drawNodes();
		addFirstFruits();
	}
	/**
	 * This method paints the canvas,
	 * the size of the canvas is dynamic, as it always fit the nodes location.
	 */
	public void drawCanvas() {

		double minX=0, maxX=0, minY=0, maxY=0;

		Collection<node_data> points = g.getV();
		if(points.isEmpty()) {
			StdDraw.setCanvasSize(600,600);
			StdDraw.setXscale(-150,150);
			StdDraw.setYscale(-150,150);
		}
		else {
			//setting the first node location as max\min x and max\min y.
			node_data n = points.iterator().next();
			minX = n.getLocation().x();
			maxX = n.getLocation().x();

			minY =n.getLocation().y();
			maxY =n.getLocation().y();

			//looping on all the nodes searching for min\max points.
			for (node_data nodes : points) {
				if(nodes.getLocation().x()>maxX)
					maxX = nodes.getLocation().x();

				if(nodes.getLocation().x()<minX)
					minX = nodes.getLocation().x();

				if(nodes.getLocation().y()>maxY)
					maxY = nodes.getLocation().y();

				if(nodes.getLocation().y()<minY)
					minY = nodes.getLocation().y();
			}

			StdDraw.setCanvasSize((int)(Math.abs(minX)+Math.abs(maxX))+1000,(int)(Math.abs(minY)+Math.abs(maxY))+600);
			StdDraw.setXscale(minX-0.001, maxX+0.001);
			StdDraw.setYscale(minY-0.001, maxY+0.001);
		}
	}
	/**
	 * This method paint all the nodes of the graph, with their key.
	 */
	public void drawNodes() {
		StdDraw.setPenRadius(0.03);
		Collection<node_data> points = g.getV();
		for (node_data nodes : points) {
			StdDraw.setPenColor(Color.BLUE);

			StdDraw.point(nodes.getLocation().x(), nodes.getLocation().y());
			StdDraw.setFont(new Font("Ariel", Font.ROMAN_BASELINE, 15));
			StdDraw.setPenColor(Color.BLACK);

			StdDraw.text(nodes.getLocation().x(), nodes.getLocation().y()+0.0002, ""+ nodes.getKey());
		}
	}
	public void addFirstFruits() {
		for(elements.Fruit f : d.fruitList) {
			if(f.getType()==1)
				StdDraw.picture(f.getPos().x(), f.getPos().y(),"data\\apple.png" , 0.001, 0.0005);

			else	
				StdDraw.picture(f.getPos().x(), f.getPos().y(),"data\\banana.png" , 0.0015, 0.001);
		}
	}
	public void addRobots() {
		int i=1;
		int num = d.getNumRobot();
		System.out.println(num);
		while(i<=num) {
			if(StdDraw.isMousePressed()) {
				StdDraw.isMousePressed = false;

				Collection<node_data> point = g.getV();


				for (node_data nodes : point) {

					double x = nodes.getLocation().x();
					double y = nodes.getLocation().y();

					if((StdDraw.mouseX()-EPSILON <= x)&&(x<= StdDraw.mouseX()+EPSILON) 
							&& (StdDraw.mouseY()-EPSILON<=y)&&(y<=StdDraw.mouseY()+EPSILON)) {

						//StdDraw.point(x, y);
						StdDraw.picture(x, y, "data\\robot.png" , 0.001, 0.001);
						Point3D p = new Point3D(x,y);
						Robot r = new Robot(1, nodes.getKey(), -1, i, p);
						game.addRobot(r.getSrc());
						d.addRobot(r);
						i++;

					}
				}
			}		
		}
	}

	/**
	 * This method paints all the edges of the graph, with their weight and destination.
	 */
	public void drawEdges() {

		StdDraw.setPenRadius(0.008);
		Collection<node_data> points = g.getV();
		for(node_data nodes: points) {
			Collection<edge_data> e = g.getE(nodes.getKey());
			for (edge_data edge : e) {
				double x0= nodes.getLocation().x();
				double y0= nodes.getLocation().y();
				double x1= g.getNode(edge.getDest()).getLocation().x();
				double y1= g.getNode(edge.getDest()).getLocation().y();
				StdDraw.setPenRadius(0.005);

				StdDraw.setPenColor(Color.RED);
				StdDraw.line(x0, y0, x1, y1);

				StdDraw.setFont(new Font("Ariel", Font.ROMAN_BASELINE, 15));

				StdDraw.setPenColor(Color.CYAN);
				StdDraw.setPenRadius(0.02);
				StdDraw.point((x0+x1*3)/4, (y0+y1*3)/4);

				StdDraw.setPenColor(Color.black);
				StdDraw.text((x0+x1*3)/4, (y0+y1*3)/4, String.format("%.3f", edge.getWeight()));
			}
		}
	}


	/**
	 * 
	 * @return the Graph_Algo of the GUI_Graph.
	 */
	public Graph_Algo getAlgo() {
		return gra;
	}

	/**
	 * 
	 * @return the graph of the GUI_Graph.
	 */
	public graph getGraph() {
		return g;
	}
	private void gui() {

		String[] chooseGame = {"Manual game", "Auto game"};

		Object selectedGame = JOptionPane.showInputDialog(null, "Choose a game mode", "Message",
				JOptionPane.INFORMATION_MESSAGE, null, chooseGame, chooseGame[0]);

		String level = JOptionPane.showInputDialog(null, "Choose a level 0-23");
		game_service game = Game_Server.getServer(Integer.parseInt(level));
		setGame(game);
		paint();


		if(selectedGame == "Manual game") {

			if(d.getNumRobot() == 1) {
				JOptionPane.showMessageDialog(null, "Choose place for the robot");
			}
			else {
				JOptionPane.showMessageDialog(null, "Choose place for "+d.getNumRobot()+" robots");
			}

			addRobots();

			game.startGame();
			while(game.isRunning()) {

				moveRobots(game);
				StdDraw.clear();
				StdDraw.enableDoubleBuffering();
				updateGraph(game);
				StdDraw.show();
			}

		}

		if(selectedGame == "Auto game") {



			//	game.chooseNextEdge(arg0, arg1);

		}

	}
	private void moveRobots(game_service game) {
		int i=0;
		while(i<d.getNumRobot()) {

			if(StdDraw.isMousePressed()) {
				StdDraw.isMousePressed = false;
				for (Robot r : d.robotList) {

					if(r.getDest()==-1) {	

						double x = r.getPos().x();
						double y = r.getPos().y();

						if((StdDraw.mouseX()-EPSILON <= x)&&(x<= StdDraw.mouseX()+EPSILON) 
								&& (StdDraw.mouseY()-EPSILON<=y)&&(y<=StdDraw.mouseY()+EPSILON)) {
							int newDest = checkIfNext(r.getSrc());
							game.chooseNextEdge(r.getId(), newDest);
							i++;
						}
					}
					game.move();

				}
			}
		}
	}

	private int checkIfNext(int src) {
		int dest=-2;
		if(StdDraw.isMousePressed()) {
			StdDraw.isMousePressed = false;

			for (edge_data edge : (d.getE(src))) {

				double x = d.getNode(edge.getDest()).getLocation().x();
				double y = d.getNode(edge.getDest()).getLocation().y();

				if((StdDraw.mouseX()-EPSILON <= x)&&(x<= StdDraw.mouseX()+EPSILON) 
						&& (StdDraw.mouseY()-EPSILON<=y)&&(y<=StdDraw.mouseY()+EPSILON)) {

					dest = d.getNode(edge.getDest()).getKey();
				}
			}
		}

		return dest;

	}

	private void updateRobots(game_service game) {
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
			for (Robot ro : d.robotList) {
				StdDraw.picture(ro.getPos().x(), ro.getPos().y(), "data\\robot.png" , 0.001, 0.001);
			}
		}
	}

	private void updateFruits(game_service game) {
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
			for(Fruit fru : d.fruitList) {
				if (fru.getType()==1)
					StdDraw.picture(fru.getPos().x(), fru.getPos().y(), "data\\apple.png" , 0.001, 0.001);

				else
					StdDraw.picture(fru.getPos().x(), fru.getPos().y(), "data\\banana.png" , 0.001, 0.001);
			}
		}
	}

	private void updateGraph(game_service game) {
		drawEdges();
		drawNodes();
		updateFruits(game);
		updateRobots(game);
	}


	public static void main(String[] a) {

		MyGameGUI my = new MyGameGUI();
		my.gui();



	}
}