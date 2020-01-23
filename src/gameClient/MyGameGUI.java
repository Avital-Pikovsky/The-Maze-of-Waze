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


/**
 * This is the main GUI class.
 * All the painting methods take place here.
 * After updating the graph from json file throu the JSON_UPDATES class,
 * it draws all the aspects of the game: Robots, fruits, canvas, edges and nodes.
 * It also shows the current time, score and details of the game.
 * @author Avital Pikovsky && Omer Katz
 *
 */
public class MyGameGUI {
	private DGraph d = new DGraph();
	double EPSILON = 0.0005;
	private game_service game = Game_Server.getServer(Json_Updates.mu); // you have [0,23] games.
	private Graph_Algo gra;
	private graph g;
	private static double minX=0, maxX=0, minY=0, maxY=0;
	private static int scoreInt = 0, movesInt = 0, levelInt = 0;
	private String[] RobotsImg = {"pics\\assaf.png","pics\\yossef.png","pics\\moshik.png"};
	private String[] FruitImg = {"pics\\donut.png","pics\\pizza.png"};
	private String[] GameImg = {"pics\\x.png","pics\\y.png"};
	private static KML_Logger kl;
	private static int game_num;
	Thread r;
	public static int sleepTime = 105;


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
	//*****************************Initializes********************************

	/**
	 * Initialize the Graph_GUI from given graph.
	 * @param gr - the given graph.
	 */

	public void init(graph gr) {
		this.g = gr;
		this.gra.g = gr;
	}
	/**
	 * Initialize this GUI from a Json_Updates object,
	 * mirroring this graph from the game server.
	 * @param ju
	 */
	public void initFromJson(Json_Updates ju){
		ju.init(game);
		init(d);
	}

	//***************************Draw Functions****************************
	/**
	 * The main paint function, drawing the whole graph, with edges, nodes and fruits.
	 */
	public void drawFirstGraph(Json_Updates ju) {
		drawCanvas();
		drawEdges();
		drawNodes();
		ju.updateFruits();
		reDrawFruits();
	}

	/**
	 * This method paints the canvas,
	 * the size of the canvas is dynamic, as it always fit the nodes location.
	 */
	public void drawCanvas() {
		Collection<node_data> points = g.getV();
		if(points.isEmpty()) {
			StdDraw.setCanvasSize(1000,550);
			StdDraw.setXscale(-150,150);
			StdDraw.setYscale(-150,150);
		}
		else {
			//setting the first node location as max\min x and max\min y.
			node_data n = points.iterator().next();
			minX = n.getLocation().x();
			maxX = n.getLocation().x();

			minY = n.getLocation().y();
			maxY = n.getLocation().y();

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
			StdDraw.picture((minX+maxX)/2, (minY+maxY)/2, GameImg[1]);
		}
	}
	/**
	 * This method paint all the nodes of the graph, with their key.
	 */
	public void drawNodes() {
		StdDraw.setPenRadius(0.025);
		Collection<node_data> points = g.getV();
		for (node_data nodes : points) {
			StdDraw.setPenColor(Color.BLUE);

			StdDraw.point(nodes.getLocation().x(), nodes.getLocation().y());
			StdDraw.setFont(new Font("Ariel", Font.BOLD, 17));
			StdDraw.setPenColor(Color.BLACK);

			StdDraw.text(nodes.getLocation().x(), nodes.getLocation().y()+0.0002, ""+ nodes.getKey());
		}
	}
	/**
	 * This method paints all the edges of the graph, with their weights.
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

	//***************************Redraw From JSON****************************************
	/**
	 * The main reDraw method.
	 * updating all the aspects of the game on the gui.
	 * @param ju
	 */
	private void reDrawGraph(Json_Updates ju) {
		StdDraw.picture((minX+maxX)/2, (minY+maxY)/2, GameImg[1]);
		drawEdges();
		drawNodes();
		ju.updateFruits();
		ju.updateRobots();
		reDrawFruits();
		reDrawRobots();
	}	
	/**
	 * Method that is used to update the fruits situation on the graph during the game.
	 */
	private void reDrawFruits() {
		for(Fruit fru : d.fruitList) {
			if (fru.getType()==1) {
				kl.Place_Mark("fruit_1",fru.getPos().toString());
				StdDraw.picture(fru.getPos().x(), fru.getPos().y(), FruitImg[0], 0.00075, 0.00075);
			}
			else {
				kl.Place_Mark("fruit_-1",fru.getPos().toString());
				StdDraw.picture(fru.getPos().x(), fru.getPos().y(), FruitImg[1] , 0.00075, 0.00075);
			}
		}
	}
	/**
	 * Method that is used to update the robots situation on the graph during the game.
	 */
	private void reDrawRobots() {
		int i=0;
		for (Robot ro : d.robotList) {
			kl.Place_Mark("data/assaf.png",ro.getPos().toString());
			StdDraw.picture(ro.getPos().x(), ro.getPos().y(), RobotsImg[i] , 0.002, 0.001);
			i++;
		}
	}

	//*************************Show Texts****************************
	/**
	 * Show description on the gui.
	 */
	private void showDescription() {
		if(d.getNumRobot()>1)
			StdDraw.text(minX+(maxX-minX)/1.1 , minY+(maxY-minY)/1, "Pick a player with the keyboard");
	}

	/**
	 * Prints the current left time and score during the game.
	 */
	public void printOnCanvas() {
		String results = game.toString();
		long t = game.timeToEnd();
		try {

			JSONObject score = new JSONObject(results);
			JSONObject s = score.getJSONObject("GameServer");
			scoreInt = s.getInt("grade");

			JSONObject moves = new JSONObject(results);
			JSONObject m = moves.getJSONObject("GameServer");
			movesInt = m.getInt("moves");

			String countDown = "Time: " + t/1000+"." + t%1000;
			String scoreStr = "Score: " + scoreInt;
			String moveStr = "Moves: " + movesInt;
			String levelStr = "Game level: " + levelInt;

			double tmp1 = maxX-minX;
			double tmp2 = maxY-minY;
			StdDraw.setPenRadius(0.08);
			StdDraw.setPenColor(Color.BLACK);
			StdDraw.text(minX+tmp1/1.25 , minY+tmp2/0.97, countDown);
			StdDraw.text(minX+tmp1/1.05 , minY+tmp2/0.94, scoreStr);
			StdDraw.text(minX+tmp1/1.05 , minY+tmp2/0.97, moveStr);
			StdDraw.text(minX+tmp1/1.25 , minY+tmp2/0.94, levelStr);



		}catch (Exception e) {
			System.out.println("Failed to print on canvas");
		}
	}

	//****************************Enter Game**************************
	/**
	 * The main GAME method.
	 * asks the player which game mode a game level he wants to play,
	 * setting the graph by his choice, and calls the Manual\Auto game methods.
	 */
	public static int Id;

	public void gui(){

		Object currentId = JOptionPane.showInputDialog("Please enter your id");

		try {

			Id = Integer.parseInt(currentId.toString());
			Game_Server.login(Id); 
		}
		catch (Exception e) {
			drawCanvas();
			StdDraw.picture((minX+maxX)/2, (minY+maxY)/2, GameImg[0]);	

			return;
		}

		String[] chooseGame = {"Manual game", "Auto game"};

		Object selectedGame = JOptionPane.showInputDialog(null, "Choose a game mode", "Message",
				JOptionPane.INFORMATION_MESSAGE, null, chooseGame, chooseGame[0]);
		if(selectedGame==null) { 
			drawCanvas();
			StdDraw.picture((minX+maxX)/2, (minY+maxY)/2, GameImg[0]);	
			return;
		}
		String[] arr = new String[24];
		for(int j=0; j<24;j++) {
			arr[j] = j+"";
		}
		Object cGame = JOptionPane.showInputDialog(null, "Choose a level 0-23", "Message",
				JOptionPane.INFORMATION_MESSAGE, null, arr, arr[0]);
		if(cGame==null) {
			drawCanvas();
			StdDraw.picture((minX+maxX)/2, (minY+maxY)/2, GameImg[0]);	
			return;
		}
		game_num = Integer.parseInt(cGame.toString());

		game_service game = Game_Server.getServer(Integer.parseInt((String) cGame));
		setGame(game);
		kl = new KML_Logger(game_num,game);
		//	kl.UploadKMLfile(game);
		levelInt = Integer.parseInt(cGame.toString());

		Json_Updates ju = new Json_Updates(this);
		initFromJson(ju);
		drawFirstGraph(ju);

		if(selectedGame == "Manual game") {

			playManual(ju);
		} 

		else if(selectedGame == "Auto game") {
			playAuto(ju);
			kl.KML_Stop();
		}
		//	System.out.println(kl.str);
	}

	//****************************************Play Modes***************************************
	/**
	 * This method gives the player the ability to play manually,
	 * place the robots and moving them as he wish.
	 * @param ju
	 */
	private void playManual(Json_Updates ju) {
		if(d.getNumRobot() == 1) {
			JOptionPane.showMessageDialog(null, "Choose place for the player");
		}
		else {
			JOptionPane.showMessageDialog(null, "Choose place for "+d.getNumRobot()+" robots");
		}
		JOptionPane.showMessageDialog(null, "Choose player with the keyboard \n and click on the wanted node to make a move.");
		ManualGame mg = new ManualGame(this);
		mg.addManualRobots();
		game.startGame();
		while(game.isRunning()){

			mg.chooseRobot();
			StdDraw.clear();
			StdDraw.enableDoubleBuffering();
			reDrawGraph(ju);
			printOnCanvas();
			showDescription();
			StdDraw.show();
		}
		JOptionPane.showMessageDialog(null, "The final score is: "+scoreInt+
				"! \n and the final moves is: "+movesInt+"!","GAME OVER",1);	
	}
	/**
	 * This method allows the player to see the game played automatically.
	 * @param ju
	 */
	private void playAuto(Json_Updates ju) {

		AutoGame ga = new AutoGame(this);

		ga.addAutoRobot();
		ThreadMove();
		game.startGame();

		while(game.isRunning()) {
			ga.allFruitToEdges(d.fruitList);
			ga.AutoNextNode(d.robotList);
			StdDraw.clear();
			StdDraw.enableDoubleBuffering();
			reDrawGraph(ju);
			printOnCanvas();
			StdDraw.show();
		}
		JOptionPane.showMessageDialog(null, "The final score is: "+scoreInt+
				"! \n and the final moves is: "+movesInt+"!","GAME OVER",1);	
	}
	/**
	 * This Thread will move the robots on the graph with a dynamic sleep time,
	 * that will be decided by the distance between the robot and his closest fruit.
	 */
	public synchronized void ThreadMove()
	{
		r = new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				while(game.isRunning())
				{
					game.move();

					try {
						Thread.sleep(sleepTime);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				r.interrupt();
			}
		});
		r.start();
	}
	//***************************Getters & Setters***************************************
	/**
	 * @return the graph of the GUI_Graph.
	 */
	public graph getGraph() {
		return g;
	}
	/**
	 * set the graph of the GUI_Graph.
	 */
	public void setGraph(graph g) {
		this.g = g;
	}
	/**
	 * @return the DGraph of the GUI_Graph.
	 */
	public DGraph getDgraph() {
		return d;
	}
	/**
	 * set the DGraph of the GUI_Graph.
	 */
	public void setDgraph(graph g) {
		this.d = (DGraph) g;
	}
	/**
	 * @return the game of the GUI_Graph.
	 */
	public game_service getGame() {
		return game;
	}
	/**
	 * set the game of the GUI_Graph.
	 */
	public void setGame(game_service game) {
		this.game = game;
	}
	/**
	 * @return the Graph_Algo of the GUI_Graph.
	 */
	public Graph_Algo getAlgo() {
		return gra;
	}
	/**
	 * set the Game_Algo of the GUI_Graph.
	 */
	public void setAlgo(Graph_Algo ga) {
		this.gra = ga;
	}
	/**
	 * @return the Kml of the GUI_Graph.
	 */
	public KML_Logger getK() {
		return kl;
	}
	/**
	 * set the Kml of the GUI_Graph.
	 */
	public void setK(KML_Logger k) {
		this.kl = k;
	}


}



