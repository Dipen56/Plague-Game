package dataStorage.adapters;

import dataStorage.XmlFunctions;
import server.game.Game;
import server.game.TestConst;
import server.game.world.Area;

public class TestBasicSaving {

	public TestBasicSaving(){
		for(Area a: TestConst.areas.values())
        	a.registerPortals();
		Game gameA = new Game(TestConst.world, TestConst.areas);
		GameAdapter altGame = new GameAdapter(gameA);
		XmlFunctions.saveFile(altGame,"");
		Game gameB = XmlFunctions.loadFile("");
		int i = 0;
		
		

	}



	public static void main(String[] args) {

        new TestBasicSaving();
    }

}
