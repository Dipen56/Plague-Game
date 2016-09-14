package dataStorage.alternates;

import dataStorage.XmlFunctions;
import server.game.ClockThread;
import server.game.Game;
import server.game.TestConst;
import server.game.TextUI;

public class TestBasicSaving {

	public TestBasicSaving(){

		Game gameA = new Game(TestConst.world, TestConst.entrances);
		AltGame altGame = new AltGame(gameA);
		XmlFunctions.saveFile(altGame);
		altGame = XmlFunctions.loadFile();
		Game gameB = altGame.getOriginal();
		int i = 0;

	}



	public static void main(String[] args) {

        new TestBasicSaving();
    }

}
