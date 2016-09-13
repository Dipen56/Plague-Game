package server.dataStorage.alternates;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import server.game.Game;
import server.game.player.Player;
import server.game.world.GroundSquare;
import server.game.world.Room;
import server.game.world.RoomEntrance;
import server.game.world.TransitionSpace;
import server.game.world.World;

/**
 * This class represents the an alternate version of the Game class, specifically for XML parsing.
 *
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878
 *
 */
@XmlRootElement
public class AltGame{

	/**
	 * An alternate version of a World object.
	 */
	@XmlElement
    private AltWorld world = null;

    /**
     * keep track on each room with its entrance position
     */
	@XmlElement
    private Map<AltRoom, AltTransitionSpace> entrances;

    /**
     * An alternate version of a Player object.
     */
	@XmlElement
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
		for(Map.Entry<Room, TransitionSpace> m: game.getEntrances().entrySet()){
			entrances.put(new AltRoom(m.getKey()), new AltTransitionSpace(m.getValue()));
		}
    }

	/**
	* Returns a Game object, identical to that which this object was originally based.
	*@return The Game
	**/
	public Game getOriginal(){
		World world = this.world.getOriginal();
		Map<Room, TransitionSpace> entrances = new HashMap<>();
		Player player = this.player.getOriginal();
		for(Map.Entry<AltRoom, AltTransitionSpace> m: this.entrances.entrySet()){
			entrances.put(((AltRoom)m.getKey()).getOriginal(), ((AltTransitionSpace)m.getValue()).getOriginal());
		}
		return new Game(world, entrances, player);
	}
}

