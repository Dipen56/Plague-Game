package tests.dataStorageTests;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import dataStorage.XmlFunctions;
import dataStorage.adapters.GameAdapter;
import server.game.Game;
import server.game.TestConst;
import server.game.player.Player;
import server.game.world.Area;
import server.game.world.MapElement;
import server.game.world.Room;

import org.junit.FixMethodOrder;
import static org.junit.Assert.*;

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
		XmlFunctions.saveFile(altGame,"");
		gameB = altGame.getOriginal();
	}


	@Test
	public void test2(){
		//Game players: keys
		Map<Integer, Player> playersA = gameA.getPlayers();
		Map<Integer, Player> playersB = gameB.getPlayers();
		//Player lists must be the same size, and each keyset must match.
		if(playersB == null
				|| playersA.size() != playersB.size()
				|| playersA.keySet().equals(playersB.keySet())){
			fail();
			return;
		}
	}

	@Test
	public void test2b(){
		//Game players: values. Note that player isAlive is not checked, as player could die immediately after loading.
		Map<Integer, Player> playersA = gameA.getPlayers();
		Map<Integer, Player> playersB = gameB.getPlayers();
		if(playersB == null
				|| playersA.size() != playersB.size()){
			fail();
			return;
		}
		Set<Integer> keysA = playersA.keySet();
		Player a = null, b = null;
		//Iterates over all keys comparing the associated values with the relevant value in the other map.
		for(Integer i : keysA){
			a = playersA.get(i);
			b = playersB.get(i);

			if(a == null
					|| b == null
					|| a.getClass() != b.getClass()
					|| a.getAvatar() != b.getAvatar()
					|| a.getHealthFromSave() != b.getHealthFromSave()	//The health at the time of saving is compared rather than the dynamic "health" field.
					|| a.getVirus() != b.getVirus()
					|| a.getId() != b.getId()
					|| a.getInventory() != b.getInventory()
					|| a.getPosition() != b.getPosition()
					|| a.isHoldingTorch() != b.isHoldingTorch()
					|| !a.getName().equals(b.getName())
					){
				fail();
				return;
			}
		}
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
