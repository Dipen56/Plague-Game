package dataStorage.alternates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import server.game.Game;
import server.game.items.Antidote;
import server.game.items.Item;
import server.game.items.Torch;
import server.game.player.Player;
import server.game.player.Virus;
import server.game.world.Area;
import server.game.world.Chest;
import server.game.world.Obstacle;
import server.game.world.Room;
import server.game.world.TransitionSpace;

/**
 * This class represents the an alternate version of the Game class, specifically for XML parsing.
 *
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AltGame{
	
	/**
	 * Whatever you do don't delete either this altObstTypeProtector or altChestTypeProtector. 
	 * I don't know why but them being here allows the parser to put objects of their types into the xml file.
	 * Without it, the parser does not recognise these types of object, and they will not be written to the game save. 
	 */
	@XmlElement
	private AltObstacle altObstTypeProtector = new AltObstacle();
	@XmlElement
	private AltChest altChestTypeProtector = new AltChest();
	@XmlElement
	private AltAntidote altAntidote = new AltAntidote();
	@XmlElement
	private AltKey aKey = new AltKey();
	@XmlElement
	private AltTorch aTorch = new AltTorch();
	
	/**
	 * An alternate version of a World object.
	 */
	@XmlElement
    private AltArea world = null;

    /**
     * Keep track on each room with its entrance position.
     */
	@XmlElement
    private Map<Integer, AltArea> areas;

	
	/**
     * Players and their id. Server can find player easily by looking by id.
     */
	@XmlElement
    private Map<Integer, AltPlayer> players;
	
	/**
     * All torches in this world. It is used to track torch burning status in timer.
     */
    private AltTorch[] torches;

	/**
	* Only to be called by XML marshaller.
	**/
    AltGame(){

    }

	/**
	*@param The object on which to base this object
	**/
    public AltGame(Game game){
    	if(game == null)
			throw new IllegalArgumentException("Argument is null");
    	this.world = new AltWorld(game.getWorld());

		areas = new HashMap<>();
		Area area = null;
		// Copies all entries from original entrances list, replacing values with alternative objects
		for(Map.Entry<Integer, Area> m: game.getAreas().entrySet()){
			Integer i = m.getKey();	//The key from the entry.
			area = m.getValue();	//The value from the entry.
			AltArea altArea = null;
			if(area == null){
				throw new RuntimeException("Integer mapped to null");
			}
			else if(area instanceof Room){
				altArea = new AltRoom(area);
			}
			else
				altArea = new AltArea(area);
			areas.put(i, altArea);
		}
		//Copies all players to the player array.
		this.players = new HashMap<>();
		Player p = null;
		Integer myInt = -1;
		for(Map.Entry<Integer, Player> m: game.getPlayers().entrySet().size()){
			myInt = m.getKey();	//The key from the entry.
			p = m.getValue();	//The value from the entry.
			if(p == null){
				throw new RuntimeException("Integer mapped to null");
			this.players.put(myInt, new AltPlayer(p));
		}
		
		
		// Copies all torches to the torches array. Field cannot be list due to xml complications.
		List<Torch> gameTorches = game.getTorches();
		AltTorch[] torches = new AltTorch[gameTorches.size()];
		for(int i = 0; i < gameTorches.size();i++){
			torches[i] = new AltTorch(gameTorches.get(i));
		}
    }

	/**
	* Returns a Game object, identical to that which this object was originally based.
	*@return The Game
	**/
	public Game getOriginal(){
		Area world = this.world.getOriginal();
		Map<Integer, Area> areas = new HashMap<>();
		for(Map.Entry<Integer, AltArea> m: this.areas.entrySet()){
			Area area = ((AltArea)m.getValue()).getOriginal();
			areas.put(m.getKey(), area);
		}
		
		//Restores Players
		Map<Integer, Player> players = new HashMap<>();
		for(Map.Entry<Integer, AltPlayer> m: this.players.entrySet()){
			Player p = ((AltPlayer)m.getValue()).getOriginal();
			players.put(m.getKey(), p);
		}
		//Restores Torches
		List<Torch>torches = new ArrayList<>();
		for(int i = 0; i < this.torches.length;i++){
			torches.add(this.torches[i].getOriginal());
		}
		
		Game game =  new Game(world, areas, players, torches);	//NNNNNNNNNNNEED to wait for player load solution decision.
		
		return game;
	}
	

}

