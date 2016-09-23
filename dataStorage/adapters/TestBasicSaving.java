package dataStorage.adapters;

import java.util.HashMap;
import java.util.Map;

import dataStorage.XmlFunctions;
import server.game.Game;
import server.game.TestConst;
import server.game.TextUI;
import server.game.player.Avatar;
import server.game.player.Player;
import server.game.world.Area;

public class TestBasicSaving {

	public TestBasicSaving(){
		for(Area a: TestConst.areas.values())
        	a.registerPortals();
		Game gameA = new Game(TestConst.world, TestConst.areas);
		GameAdapter altGame = new GameAdapter(gameA);
		XmlFunctions.saveFile(altGame);
		altGame = XmlFunctions.loadFile();

		
		Game gameB = altGame.getOriginal();
		int i = 0;
		
		
		
	}



	public static void main(String[] args) {

        new TestBasicSaving();
    }

}
