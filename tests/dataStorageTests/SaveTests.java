package tests.dataStorageTests;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import static org.junit.Assert.*;

import dataStorage.InitialGameLoader;
import dataStorage.XmlFunctions;
import dataStorage.adapters.GameAdapter;
import server.game.Game;
import server.game.TestConst;
import server.game.world.Area;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaveTests {

	private static Game gameA;
	private static GameAdapter altGame;

	static{
		gameA = InitialGameLoader.makeGame();
		altGame = new GameAdapter(gameA);
	}

	@Test
	public void test1(){
		//Tests  for errors during saveFile method.
		try{
			XmlFunctions.saveFile(altGame,"");
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
			XmlFunctions.saveFile(altGame,"");
			XmlFunctions.loadFile("");
		}catch(RuntimeException e){
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void test3(){
		//tests that the adaptation from AltGame to Game processes without throwing an error.
		try{
			XmlFunctions.saveFile(altGame,"");
			XmlFunctions.loadFile("");
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
