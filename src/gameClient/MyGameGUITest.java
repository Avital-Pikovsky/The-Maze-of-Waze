package gameClient;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;

class MyGameGUITest extends MyGameGUI {
	private game_service game = Game_Server.getServer(17); // you have [0,23] games.

	@Test
	void testPaint() {
	MyGameGUI my = new MyGameGUI();
	Json_Updates ju = new Json_Updates(my);
	ju.init(game);
	my.drawFirstGraph(ju);
	}

}
