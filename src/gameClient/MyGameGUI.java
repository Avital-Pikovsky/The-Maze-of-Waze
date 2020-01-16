package gameClient;


import java.awt.Color;
import java.awt.Font;

import java.util.Collection;


import javax.swing.JOptionPane;

import org.json.JSONObject;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.Fruit;
import elements.Robot;
import utils.StdDraw;
import gameClient.AutoGame;

public class MyGameGUI {
	private DGraph d = new DGraph();
	double EPSILON = 0.0005;
	private game_service game = Game_Server.getServer(17); // you have [0,23] games.
	private Graph_Algo gra;
	private graph g;
	private static double minX=0, maxX=0, minY=0, maxY=0;
	private static int scoreInt ;

	//***********************************Constructors********************************
	/**
	 * Default constructor, connects all the graphs(Graph_Algo and DGraph) to the StdDraw class.
	 */
	public MyGameGUI() {
		gra=new Graph_Algo();
		g=new DGraph();
		this.d = (DGraph) g;
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
		this.d = (DGraph) g;
		StdDraw.setGui(this);

	}
	/**
	 * Initialize the Graph_GUI from given graph.
	 * @param gr - the given graph.
	 */

	//*****************************Initializes********************************

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
		drawFirstGraph();
	}

	public void initAndPaint(){
		d.init(game);
		init(d);
		drawFirstGraph();
	}
	//***************************Draw Functions****************************
	/**
	 * The main paint function, drawing the whole graph.
	 */
	public void drawFirstGraph() {
		drawCanvas();
		drawEdges();
		drawNodes();
		drawFirstFruits();
	}

	/**
	 * This method paints the canvas,
	 * the size of the canvas is dynamic, as it always fit the nodes location.
	 */
	public void drawCanvas() {
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

	//*********************Draw First Fruits & Robots***********************
	public void drawFirstFruits() {
		for(Fruit f : d.fruitList) {
			System.out.println(f.getType());
			if(f.getType()==-1) {
				StdDraw.picture(f.getPos().x(), f.getPos().y(),"data\\banana.png" , 0.0003, 0.0001);
			}
			else if(f.getType()==1)	{
				StdDraw.picture(f.getPos().x(), f.getPos().y(),"data\\apple.png" , 0.0015, 0.001);
		}
		}
	}
	//***************************ReDraw**********************************
	private void reDrawFruits() {
		for(Fruit fru : d.fruitList) {
			if (fru.getType()==1)
				StdDraw.picture(fru.getPos().x(), fru.getPos().y(), "data\\apple.png" , 0.001, 0.001);

			else
				StdDraw.picture(fru.getPos().x(), fru.getPos().y(), "data\\banana.png" , 0.001, 0.001);
		}
	}

	private void reDrawRobots() {
		for (Robot ro : d.robotList) {
			StdDraw.picture(ro.getPos().x(), ro.getPos().y(), "data\\robot.png" , 0.001, 0.001);
		}
	}

	//***************************Redraw From JSON****************************************
	private void reDrawGraph() {
		Json_Updates ju = new Json_Updates(this);
		drawEdges();
		drawNodes();
		ju.updateFruits();
		ju.updateRobots();
		reDrawFruits();
		reDrawRobots();
	}

	//*************************Show Score****************************
	public void printScore() {
		String results = game.toString();
		long t = game.timeToEnd();
		try {
			scoreInt=0;
			JSONObject score = new JSONObject(results);
			JSONObject ttt = score.getJSONObject("GameServer");
			scoreInt = ttt.getInt("grade");

			String countDown = "Time: " + t/1000+"." + t%1000;
			String scoreStr = "Score: " + scoreInt;
			double tmp1 = maxX-minX;
			double tmp2 = maxY-minY;
			StdDraw.setPenRadius(0.08);
			StdDraw.setPenColor(Color.BLACK);
			StdDraw.text(minX+tmp1/1.05 , minY+tmp2/0.95, countDown);
			StdDraw.text(minX+tmp1/1.05 , minY+tmp2, scoreStr);

		}catch (Exception e) {
			System.out.println("Failed to print score");
		}
	}

	//****************************Enter Game**************************
	public void gui() {

		String[] chooseGame = {"Manual game", "Auto game"};

		Object selectedGame = JOptionPane.showInputDialog(null, "Choose a game mode", "Message",
				JOptionPane.INFORMATION_MESSAGE, null, chooseGame, chooseGame[0]);

		String level = JOptionPane.showInputDialog(null, "Choose a level 0-23");
		game_service game = Game_Server.getServer(Integer.parseInt(level));
		setGame(game);
		initAndPaint();

		if(selectedGame == "Manual game") {
			playManual();
		} 

		else if(selectedGame == "Auto game") {
			playAuto();
		}
	}

	//****************************************Threads***************************************
	private void playManual() {
		if(d.getNumRobot() == 1) {
			JOptionPane.showMessageDialog(null, "Choose place for the robot");
		}
		else {
			JOptionPane.showMessageDialog(null, "Choose place for "+d.getNumRobot()+" robots");
		}
		ManualGame mg = new ManualGame(this);
		mg.addManualRobots();
		game.startGame();
		while(game.isRunning()){

			mg.chooseRobot();
			StdDraw.clear();
			StdDraw.enableDoubleBuffering();
			reDrawGraph();
			printScore();
			StdDraw.show();

		}

		JOptionPane.showMessageDialog(null, "The final score is: "+scoreInt+"!","GAME OVER",1);
	}

	private void playAuto() {


		AutoGame ga = new AutoGame(this);

		ga.addAutoRobot();
		game.startGame();

		while(game.isRunning()) {
			ga.allFruitToEdges(d.fruitList);
			ga.AutoNextNode(d.robotList);
			StdDraw.clear();
			StdDraw.enableDoubleBuffering();
			reDrawGraph();
			printScore();
			StdDraw.show();

		}
		JOptionPane.showMessageDialog(null, "The final score is: "+scoreInt+"!","GAME OVER",1);

	}


	//***************************Getters & Setters***************************************
	/**
	 * @return the graph of the GUI_Graph.
	 */
	public graph getGraph() {
		return g;
	}
	public void setGraph(graph g) {
		this.g = g;
	}

	public DGraph getDgraph() {
		return d;
	}

	public void setDgraph(graph g) {
		this.d = (DGraph) g;
	}

	public game_service getGame() {
		return game;
	}

	public void setGame(game_service game) {
		this.game = game;
	}
	/**
	 * @return the Graph_Algo of the GUI_Graph.
	 */
	public Graph_Algo getAlgo() {
		return gra;
	}
	public void setAlgo(Graph_Algo ga) {
		this.gra = ga;
	}
}