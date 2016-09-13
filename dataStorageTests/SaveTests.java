package dataStorageTests;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import static org.junit.Assert.*;

import server.dataStorage.XmlFunctions;
import server.dataStorage.alternates.AltGame;
import server.game.Game;
import server.game.TestConst;
import server.game.player.Player;
import server.game.player.Virus;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaveTests {


	
	//considering execution orde, I can use a helper method to create initial game and pass it in constructors from external class
	
	
	@Test
	public void test1(){
		//Initiates initial game and saves it. Makes sure that nothing crashes while it happens.
		try{
			Game gameA  = new Game(TestConst.world, TestConst.entrances);
			AltGame altGame = new AltGame(gameA);
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
			Game gameA  = new Game(TestConst.world, TestConst.entrances);
			AltGame altGame = new AltGame(gameA);
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
			this.gameB = this.altgame.getOriginal();
		}catch(RuntimeException e){
				e.printStackTrace();
				fail();
		}
	}

	@Test
	public void test4(){
		
	}
	
	@Test
	public void test5(){
		// IS .equals the way to go ?
		assertTrue(gameA.equals(gameB));
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
