package gameClient;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import dataStructure.DGraph;

class MyGameGUITest extends MyGameGUI {


	@Test
	void testPaint() {
	MyGameGUI my = new MyGameGUI();
	my.initAndPaint();
	}

}
