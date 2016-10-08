package dataStorage.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import server.game.Game;
import server.game.items.Torch;
import server.game.player.Player;
import server.game.world.Area;

/**
 * This class represents the an alternate version of the Game class, specifically for XML
 * parsing.
 *
 * @author Daniel Anastasi (anastadani 300145878)
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GameAdapter{


	/**
	 * Whatever you do don't delete either this altObstTypeProtector or altChestTypeProtector.
	 * I don't know why but them being here allows the parser to put objects of their types into the xml file.
	 * Without it, the parser does not recognise these types of object, and they will not be written to the game save.
	 */
	@XmlElement
	private ObstacleAdapter ObstTypeProtector = new ObstacleAdapter();
	@XmlElement
	private ChestAdapter ChestTypeProtector = new ChestAdapter();
	@XmlElement
	private AntidoteAdapter AntidoteTypeProtector = new AntidoteAdapter();
	@XmlElement
	private KeyAdapter KeyTypeProtector = new KeyAdapter();
	@XmlElement
	private CupboardAdapter CupboardTypeProtector = new CupboardAdapter();
	@XmlElement
	private ScrapPileAdapter ScrapPileTypeProtector = new ScrapPileAdapter();
	@XmlElement
	private TransitionSpaceAdapter TransitionSpaceTypeProtector = new TransitionSpaceAdapter();
	@XmlElement
	private TorchAdapter torchTypeProtector = new TorchAdapter();



	public static final GroundSpaceAdapter groundSpaceAdapter= new GroundSpaceAdapter();

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
	 * Players and their id. Server can find player easily by looking by id.
	 */
	@XmlElement
	private Map<Integer, PlayerAdapter> players;


	/**
	 * An ID number to identify a loaded game against a running game.
	 */
	@XmlElement
	private int gameID;

	/**
	 * Only to be called by XML marshaller.
	 **/
	GameAdapter(){

	}

	/**
	 *@param The object on which to base this object
	 **/
	public GameAdapter(Game game){
		if(game == null)
			throw new IllegalArgumentException("Argument is null");

		this.world = new AreaAdapter(game.getWorld());
		areas = new HashMap<>();
		this.players = new HashMap<>();
		Area area = null;
		Player p = null;
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

		//Copies all players to the players map.
		for(Map.Entry<Integer, Player> m: game.getPlayers().entrySet()){
			myInt = m.getKey();	//The key from the entry.
			p = m.getValue();	//The value from the entry.
			if(p == null){
				throw new RuntimeException("Integer mapped to null");
			}
			this.players.put(myInt, new PlayerAdapter(p));
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

		//Restores Players
		Map<Integer, Player> players = new HashMap<>();
		for(Map.Entry<Integer, PlayerAdapter> m: this.players.entrySet()){
			Player p = ((PlayerAdapter)m.getValue()).getOriginal();
			players.put(m.getKey(), p);
		}

		Game game =  new Game(world, areas, players, gameID);

		return game;
	}

}
