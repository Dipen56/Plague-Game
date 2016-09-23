package tests.dataStorageTests;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import dataStorage.XmlFunctions;
import dataStorage.adapters.GameAdapter;
import server.game.Game;
import server.game.TestConst;
import server.game.world.Area;
import server.game.world.MapElement;
import server.game.world.Room;

import org.junit.FixMethodOrder;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * All tests in this class assume that saving and loading compute without exceptions thrown.
 * @author Daniel Anastasi
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoadContentTests {
	private static Game gameA, gameB;
	private static GameAdapter altGame;

	static{
		for(Area a: TestConst.areas.values())
			a.registerPortals();
		gameA = new Game(TestConst.world, TestConst.areas);
		altGame = new GameAdapter(gameA);
		XmlFunctions.saveFile(altGame);
		altGame = XmlFunctions.loadFile();
		gameB = altGame.getOriginal();
	}



	@Test
	public void test1(){
		//Game torches
		assertTrue(gameA.getTorches().equals(gameB.getTorches()));	
	}

	@Test
	public void test2(){
		//Game players
		assertTrue(gameA.getPlayers().equals(gameB.getPlayers()));
	}

	@Test
	public void test3(){
		//Game world map null?
		MapElement[][] mapA = gameA.getWorld().getMap();
		MapElement[][] mapB = gameB.getWorld().getMap();
		assert(mapA != null && mapB != null);
	}

	@Test
	public void test3b(){
		//Game world map
		MapElement[][] mapA = gameA.getWorld().getMap();
		MapElement[][] mapB = gameB.getWorld().getMap();
		MapElement a = null, b = null;

		//Neither map should ever be null,as checked in test 3.
		for(int i = 0; i < mapA.length; i++){ 
			for(int j = 0; j < mapA[0].length; j++){
				a = mapA[i][j];
				b = mapB[i][j];
				assert(a.equals(b));
			}

		}
		//assertTrue(gameA.getWorld().equals(gameB.getWorld()));
	}

	@Test
	public void test3c(){
		//Game world player portals: Note that .equals will fail, as list sizes may vary.
		List<int[]> portalsA = gameA.getWorld().getPlayerPortals();
		List<int[]> portalsB = gameB.getWorld().getPlayerPortals();
		int[] a = null, b = null;
		//Neither map should ever be null,as checked in test 6.
		assert(portalChecker(portalsA, portalsB));
	}

	@Test
	public void test4(){
		//Game areas: keys
		Set<Integer> keysA = gameA.getAreas().keySet();
		Set<Integer> keysB = gameB.getAreas().keySet();
		assert(keysA.equals(keysB));
	}

	@Test
	public void test4b(){
		/*Game areas: values: 
		.equals is not appropriate as player portal list can have the same pairs in the same indeces, but fail an equals check.
		We are not interested in a perfect equals check.
		*/
		Map<Integer, Area> areasA = gameA.getAreas();
		Map<Integer, Area> areasB = gameB.getAreas();
		Set<Integer> keysA = areasA.keySet();
		Area a = null, b = null;
		//Each value is compared to its respective value in the other game.
		for(Integer i : keysA){
			a = areasA.get(i);
			b = areasB.get(i);

			if (a == null || b == null
					|| a.getClass() != b.getClass()
					|| a.getAreaID() != b.getAreaID()){
				fail();
				return;
			}
			//Some areas are Rooms
			if(a instanceof Room){
				Room rA = (Room)a;
				if(!(b instanceof Room)){
					fail();
					return;
				}
				Room rB = (Room)b;
				if(rA.getKeyID() != rB.getKeyID() 
						|| rA.isLocked() != rB.isLocked()){
					fail();
					return;
				}	
			}
			//Checks portals pairs are the same.
			assert(portalChecker(gameA.getWorld().getPlayerPortals(), gameB.getWorld().getPlayerPortals()));
		}

	}

	@Test
	public void test4c(){
		

	}
	
	/**
	 * Returns true if all position pairs match.
	 * @param A list of duple integer arrays.
	 * @param A list of duple integer arrays.
	 * @return
	 */
	public boolean portalChecker(List<int[]> portalsA, List<int[]> portalsB){
		int[] a = null, b = null;
		for(int i = 0; i < portalsA.size(); i++){ 
			a = portalsA.get(i);
			b = portalsB.get(i);
			if(a[0] != b[0] || a[1] != b[1])
				return false;
		}
		return true;
	}

}
