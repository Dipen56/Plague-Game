package tests.dataStorageTests;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import static org.junit.Assert.*;

import java.util.List;

import dataStorage.XmlFunctions;
import dataStorage.adapters.GameAdapter;
import server.game.Game;
import server.game.TestConst;
import server.game.player.Player;
import server.game.player.Virus;
import server.game.world.Area;
import server.game.world.MapElement;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaveTests {

	private static Game gameA;
	private static GameAdapter altGame;

	static{
		for(Area a: TestConst.areas.values())
			a.registerPortals();
		gameA = new Game(TestConst.world, TestConst.areas);

		altGame = new GameAdapter(gameA);
	}

	@Test
	public void test1(){
		//Tests  for errors during saveFile method.
		try{
			XmlFunctions.saveFile(altGame);
		}
		catch(RuntimeException e){
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void test2(){
		// tests loading game is done without throwing an error.
		try{
			XmlFunctions.saveFile(altGame);
			altGame = XmlFunctions.loadFile();
		}catch(RuntimeException e){
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void test3(){
		//tests that the adaptation from AltGame to Game processes without throwing an error.
		try{
			XmlFunctions.saveFile(altGame);
			altGame = XmlFunctions.loadFile();
			Game gameB = altGame.getOriginal();
		}catch(RuntimeException e){
			e.printStackTrace();
			fail();
		}
	}


	/*


	@Test
	public void test(){

	}



	try{

	}catch(RuntimeException e){
			e.printStackTrace();
			fail();
	}

	 */


}
