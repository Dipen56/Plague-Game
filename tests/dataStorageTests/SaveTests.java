package tests.dataStorageTests;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import dataStorage.alternates.AltGame;

import org.junit.FixMethodOrder;
import static org.junit.Assert.*;

import dataStorage.XmlFunctions;
import server.game.Game;
import server.game.TestConst;
import server.game.player.Player;
import server.game.player.Virus;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaveTests {

	private static Game gameA;
	private static AltGame altGame;

	static{
		gameA = new Game(TestConst.world, TestConst.entrances);
		altGame = new AltGame(gameA);
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

	@Test
	public void test4(){
		Game gameB = null;
		try{
			XmlFunctions.saveFile(altGame);
			altGame = XmlFunctions.loadFile();
			gameB = altGame.getOriginal();
			int i = 0;
		}catch(RuntimeException e){
				e.printStackTrace();
				fail();
		}

		assertTrue(gameA.equals(gameB));		//this is not enough.


	}

	@Test
	public void test5(){
		// IS .equals the way to go ?

	}

	@Test
	public void test6(){

	}

	@Test
	public void test7(){

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
