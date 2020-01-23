package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import algorithms.Graph_Algo;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

class Graph_AlgoTest {

	private Graph_Algo createAlgo() {
		Graph_Algo ga = new Graph_Algo();
		ga.g.addNode(new Node(1, new Point3D(0,0), 0, "", 0));
		ga.g.addNode(new Node(2, new Point3D(50,0), 0, "", 0));
		ga.g.addNode(new Node(3, new Point3D(0,50), 0, "", 0));
		ga.g.addNode(new Node(4, new Point3D(100,100), 0, "", 0));

		return ga;	
	}

	@Test
	void testInit() {
		Graph_Algo ga = createAlgo();
		ga.save("test1.txt");

		Graph_Algo in = new Graph_Algo();
		in.init("test1.txt");


	}

	@Test
	void testSave() {
		Graph_Algo ga = createAlgo();
		ga.save("test.txt");
	}

	@Test
	void testIsConnected() {
		Graph_Algo ga = createAlgo();
		ga.g.connect(1, 2, 1);
		ga.g.connect(2, 3, 1);
		ga.g.connect(3, 4, 1);
		ga.g.connect(4, 1, 1);

		assertTrue(ga.isConnected());

	}

	@Test
	void testShortestPathDist() {
		Graph_Algo ga = createAlgo();
		ga.g.connect(1, 2, 1);
		ga.g.connect(2, 3, 2);
		ga.g.connect(3, 4, 3);

		double weight = ga.shortestPathDist(1, 4);
		assertTrue(weight == 6);
	}

	@Test
	void testShortestPath() {
		Graph_Algo ga = createAlgo();

		ga.g.connect(1, 2, 1);
		ga.g.connect(2, 3, 2);
		ga.g.connect(3, 4, 3);

		List<node_data> expected = new ArrayList<>();
		expected.add(new Node(1, new Point3D(0,0), 0, "", 0));
		expected.add(new Node(2, new Point3D(50,0), 0, "", 0));
		expected.add(new Node(3, new Point3D(0,50), 0, "", 0));
		expected.add(new Node(4, new Point3D(100,100), 0, "", 0));

		List<node_data> result = ga.shortestPath(1, 4);

		boolean equal = true;
		for(int i=0; i<expected.size();i++) {
			if(expected.get(i).getKey()!=result.get(i).getKey()) 
				equal = false;	
		}
		assertTrue(equal);
	}

	@Test
	void testTSP() {
		boolean equal = true;
Graph_Algo ga = createAlgo();
ga.g.connect(1, 2, 1);
ga.g.connect(2, 3, 2);
ga.g.connect(3, 4, 3);
ga.g.connect(4, 1, 4);

ArrayList<Integer> list = new ArrayList<>();
list.add(1);
list.add(2);
list.add(3);
list.add(4);
System.out.println(list.toString());
System.out.println(ga.TSP(list).toString());

for (int i = 0; i < list.size(); i++) {
	if(ga.TSP(list).get(i).getKey()!=list.get(i)){
		equal = false;
	}
}
assertTrue(equal);





	}

	@Test
	void testCopy() {
		Graph_Algo expected = createAlgo();
		expected.g.connect(1, 2, 3);

		boolean equal = false;
		Graph_Algo copied = new Graph_Algo();
		copied.g =  expected.copy();
		for(edge_data edge : copied.g.getE(1)) {
			if(edge.getDest()==2 && edge.getSrc()==1 && edge.getWeight()==3)
				equal = true;
		}
		assertTrue(equal);

	}
}

