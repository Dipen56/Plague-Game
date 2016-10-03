package dataStorage.adapters;

import dataStorage.InitialGameLoader;
import dataStorage.XmlFunctions;
import server.game.Game;
import server.game.TestConst;
import server.game.world.Area;

public class TestBasicSaving {

	public TestBasicSaving(){
		Game gameA = InitialGameLoader.makeGame();
		GameAdapter altGame = new GameAdapter(gameA);
		XmlFunctions.saveFile(altGame,"");
		Game gameB = XmlFunctions.loadFile("");
		int i = 0;



	}



	public static void main(String[] args) {

        new TestBasicSaving();
    }

}
