package dataStorage.alternates;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import server.game.Game;
import server.game.items.Antidote;
import server.game.items.Item;
import server.game.player.Player;
import server.game.player.Virus;
import server.game.world.Area;
import server.game.world.Chest;
import server.game.world.GroundSquare;
import server.game.world.Obstacle;
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
@XmlAccessorType(XmlAccessType.FIELD)
public class AltGame{
	
	
	/**
	 * Whatever you do don't delete either this altObstTypeProtector or altChestTypeProtector. I don't know why but them being here allows the parser to recognise AltObstacle and AltChest objects.
	 * Without it, the parser does not recognise this type of object, and they will not be written to the game save. 
	 */
	@XmlElement
	private AltObstacle altObstTypeProtector = new AltObstacle(new Obstacle("My Test Obstacle"));
	@XmlElement
	private AltChest altChestTypeProtector = new AltChest(new Chest("Plerp", -2, true, new ArrayList<Item>()));
	
	
	
	/**
	 * An alternate version of a World object.
	 */
	@XmlElement
    private AltWorld world = null;

    /**
     * keep track on each room with its entrance position
     */
	@XmlElement
    private Map<AltTransitionSpace, AltArea> entrances;

    /**
     * An alternate version of a Player object.
     */
	@XmlElement
    AltPlayer player;

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

		entrances = new HashMap<>();


		// Copies all entries from original entrances list, replacing values with alternative objects
		for(Map.Entry<TransitionSpace, Area> m: game.getEntrances().entrySet()){
			TransitionSpace ts = m.getKey();
			Area area = m.getValue();
			AltArea altArea = new AltWorld(area);
			AltTransitionSpace ats = new AltTransitionSpace(ts);
			entrances.put(ats, altArea);
		}
		player = new AltPlayer(game.getPlayer());

    }

	/**
	* Returns a Game object, identical to that which this object was originally based.
	*@return The Game
	**/
	public Game getOriginal(){
		World world = this.world.getOriginal();
		Map<TransitionSpace, Area> entrances = new HashMap<>();
		Player player = this.player.getOriginal();
		for(Map.Entry<AltTransitionSpace, AltArea> m: this.entrances.entrySet()){
			entrances.put(((AltTransitionSpace)m.getKey()).getOriginal(), ((AltArea)m.getValue()).getOriginal());
		}
		return new Game(world, entrances, player);
	}
}

