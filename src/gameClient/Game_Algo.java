package gameClient;

import java.util.List;

import dataStructure.Edge;
import dataStructure.edge_data;
import elements.Fruit;
import elements.Robot;

public class Game_Algo {
	private MyGameGUI my = new MyGameGUI();
	double EPSILON = 0.000001;

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
		
		public void AutoNextNode(List<Robot> list) {
		for(Robot r : list) {
			for(Fruit f : my.getDgraph().fruitList) {
				
			}
		}
	}
}
