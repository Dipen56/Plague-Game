package dataStorage.adapters;

import java.util.HashMap;
import java.util.Map;

import dataStorage.InitialGameLoader;
import dataStorage.XmlFunctions;
import server.game.Game;
import server.game.TestConst;
import server.game.player.Avatar;
import server.game.player.Player;
import server.game.world.Area;

public class TestBasicSaving {

	public TestBasicSaving(){
		Game gameA = InitialGameLoader.makeGame();
		//XmlFunctions.saveFile(gameA,"");
		//Game gameB = XmlFunctions.loadFile("");

		//add player to game, save it, then start new game and load the old.
		Player p = new Player(100, Avatar.Avatar_1, "Bob");
		gameA.joinPlayer(p);
		XmlFunctions.saveFile(gameA,"Bob");



		//A brand new game

		Player pb = new Player(200, Avatar.Avatar_1, "Bob");
		Map<Integer, Player> newPlayers = new HashMap<>();
		newPlayers.put(200, pb);


		Game gameC = XmlFunctions.loadFile("Bob");
		gameC.resetPlayers(newPlayers);
		String derp = "dfsdf";



	}



	public static void main(String[] args) {

        new TestBasicSaving();
    }

}
