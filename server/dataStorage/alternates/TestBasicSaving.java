package server.dataStorage.alternates;

import server.dataStorage.XmlFunctions;
import server.game.ClockThread;
import server.game.Game;
import server.game.TextUI;

public class TestBasicSaving {

	public TestBasicSaving(){
		Game game = TextUI.setupGame();
		//ClockThread ct = new ClockThread(20, game);
		//ct.start();
		AltGame agame = new AltGame(game);
		
		game = agame.getOriginal();
		//XmlFunctions.saveFile(agame);
		
		
		int i = 1;
		agame = null;
	}



	public static void main(String[] args) {

        new TestBasicSaving();
    }

}
