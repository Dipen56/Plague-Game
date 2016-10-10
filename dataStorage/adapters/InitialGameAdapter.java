package dataStorage.adapters;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import server.game.Game;
import server.game.items.Torch;
import server.game.player.Player;
import server.game.world.Area;

/**
 * This class represents the an alternate version of the Game class, specifically for saving/loading an XML version of a hard-coded game world.
 *
 * @author Daniel Anastasi (anastadani 300145878)
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class InitialGameAdapter{

	/**
	 * Whatever you do don't delete either this altObstTypeProtector or altChestTypeProtector.
	 * I don't know why but them being here allows the parser to put objects of their types into the xml file.
	 * Without it, the parser does not recognise these types of object, and they will not be written to the game save.
	 */

	/**
	 * Included to allow the XML parser to recognise the type of object.
	 */
	@XmlElement
	private ObstacleAdapter ObstTypeProtector = new ObstacleAdapter();

	/**
	 * Included to allow the XML parser to recognise the type of object.
	 */
	@XmlElement
	private ChestAdapter ChestTypeProtector = new ChestAdapter();

	/**
	 * Included to allow the XML parser to recognise the type of object.
	 */
	@XmlElement
	private AntidoteAdapter AntidoteTypeProtector = new AntidoteAdapter();

	/**
	 * Included to allow the XML parser to recognise the type of object.
	 */
	@XmlElement
	private KeyAdapter KeyTypeProtector = new KeyAdapter();

	/**
	 * Included to allow the XML parser to recognise the type of object.
	 */
	@XmlElement
	private CupboardAdapter CupboardTypeProtector = new CupboardAdapter();

	/**
	 * Included to allow the XML parser to recognise the type of object.
	 */
	@XmlElement
	private ScrapPileAdapter ScrapPileTypeProtector = new ScrapPileAdapter();

	/**
	 * Included to allow the XML parser to recognise the type of object.
	 */
	@XmlElement
	private TransitionSpaceAdapter TransitionSpaceTypeProtector = new TransitionSpaceAdapter();

	/**
	 * An alternate version of a World object.
	 */
	@XmlElement
	private AreaAdapter world = null;

	/**
	 * Keep track on each room with its entrance position.
	 */
	@XmlElement
	private Map<Integer, AreaAdapter> areas;

	/**
	 * An ID number to identify a loaded game against a running game.
	 */
	@XmlElement
	private int gameID;

	/**
	 * Only to be called by XML marshaller.
	 **/
	InitialGameAdapter(){

	}

	/**
	 * @param game	The object on which to base this object
	 **/
	public InitialGameAdapter(Game game){
		if(game == null)
			throw new IllegalArgumentException("Argument is null");

		this.world = new AreaAdapter(game.getWorld());
		areas = new HashMap<>();
		Area area = null;
		Integer myInt = -1;

		AreaAdapter altArea = null;
		// Copies all entries from original area list, replacing values with alternative objects
		for(Map.Entry<Integer, Area> m: game.getAreas().entrySet()){
			myInt = m.getKey();	//The key from the entry.
			area = m.getValue();	//The value from the entry.

			if(area == null){
				throw new RuntimeException("Integer mapped to null");
			}
			else
				altArea = new AreaAdapter(area);
			areas.put(myInt, altArea);
			altArea = null;
		}

		gameID = game.getGameID();
	}


	/**
	 * Returns a Game object, identical to that which this object was originally based.
	 *@return The Game
	 **/
	public Game getOriginal(){
		Area world = this.world.getOriginal();
		Map<Integer, Area> areas = new HashMap<>();
		for(Map.Entry<Integer, AreaAdapter> m: this.areas.entrySet()){
			AreaAdapter adapter = m.getValue();
			Area area = adapter.getOriginal();
			areas.put(m.getKey(), area);
		}

		Game game =  new Game(world, areas, new HashMap<Integer, Player>(), gameID);

		return game;
	}





}
