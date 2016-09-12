package server.dataStorage.alternates;

import java.util.HashMap;
import java.util.Map;

import server.game.Game;
import server.game.player.Player;
import server.game.world.Room;
import server.game.world.RoomEntrance;
import server.game.world.World;

/**
 * This class represents the an alternate version of the Game class, specifically for XML parsing.
 * 
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878
 *
 */
public class AltGame{

	/** 
	 * An alternate version of a World object.
	 */
    private AltWorld world = null;
	
    /**
     * keep track on each room with its entrance position
     */
    private Map<AltRoom, AltRoomEntrance> entrances;

    /**
     * An alternate version of a Player object.
     */
    AltPlayer player;

	/**
	* Only to be called by XML marshaller.
	**/
    public AltGame(){
        player = new AltPlayer();
		entrances = new HashMap<>();
    }
	
	/**
	*@param The object on which to base this object
	**/
    public AltGame(Game game){
		entrances = new HashMap<>();
		// Copies all entries from original entrances list, replacing values with alternative objects
		for(Map.Entry<Room, RoomEntrance> m: game.getEntrances().entrySet()){
			players.put(new AltRoom(m.getKey()), new AltRoomEntrance(m.getValue()));
		}
    }

	/** 
	* Returns a Game object, identical to that which this object was originally based.
	*@return The Game
	**/
	public Game getOriginal(){
		World world = new World(this.world);
		Map<Room, RoomEntrance> entrances = new HashMap<>();
		Player player  = new Player(this.player);
		for(Map.Entry<AltRoom, AltRoomEntrance> m: this.entrances.entrySet()){
			entrances.put(new Room(m.getKey()), new RoomEntrance(m.getValue()));
		}
		return new Game(world, entrances, player);
	}

  