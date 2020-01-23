package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import Server.Game_Server;
import Server.game_service;
import gameClient.Json_Updates;

public class Json_UpdatesTest {
	Json_Updates ju = new Json_Updates(0);
	private game_service game = Game_Server.getServer(0); 

@Test
public void initFromGame() {
	ju.init(game);
	assertEquals(ju.getD().edgeSize(), 22);
	assertEquals(ju.getD().getV().size(), 11);
}

@Test
public void initFruit() {
	ju.init(game);
	ju.updateFruits();
	assertEquals(ju.getD().fruitList.size(), 1);
}

@Test
public void initRobot() {
	ju.init(game);
	ju.updateRobots();
	assertEquals(ju.getD().robotList.size(), 1);
}
}

