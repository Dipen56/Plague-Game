package server.game;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import dataStorage.alternates.AltPlayer;
import server.game.items.Antidote;
import server.game.items.Destroyable;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
import server.game.player.Direction;
import server.game.player.Player;
import server.game.player.Virus;
import server.game.world.Area;
import server.game.world.Chest;
import server.game.world.GroundSquare;
import server.game.world.MapElement;
import server.game.world.Obstacle;
import server.game.world.Position;
import server.game.world.Room;
import server.game.world.RoomEntrance;
import server.game.world.RoomExit;
import server.game.world.TransitionSpace;
import server.game.world.World;

/**
 * This class represents the game.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Game {

	/**
	 * A new player has this chance of spawning in world map. If not spawned in world, the
	 * player will be spawned in a random room.
	 */
	private static final float SPAWN_IN_WORLD_CHANCE = 0.6f;

	private World world;

	/**
	 * Maps each transition space to the area which contains it.
	 */
	private Map<TransitionSpace, Area> entrances;

	/**
	 * players and their id. Server can find player easily by looking by id.
	 */
	private Map<Integer, Player> players;


	/**
	 * The player
	 */
	private Player player;

	/**
	 * All torches in this world. used to track their burning status.
	 */
	private List<Torch> torches;

	/**
	 * A timer for world clock. It starts when the Game object is constructed.
	 */
	private Timer timer;

	// the world clock starts from 00:00
	private LocalTime clock = LocalTime.of(0, 0, 0);

	/*
	 * FOR TEAM
	 *
	 * Ideally, other packages who want to visit game states should only interact with
	 * this class. This class wraps all game-world-related and logic-related classes
	 * inside, and provides getters, setters and reasonable functions for external
	 * packages to interact with game. If you guys need any access point for some stuff,
	 * like player, item, map, or time, and it is not provided within this class yet, let
	 * me know.
	 */

	/**
	 * Constructor, take in an XML file that describes the game world, and construct it
	 * with details in the file.
	 *
	 * @param file
	 */
	public Game(File file) {

		players = new HashMap<>();
		torches = new ArrayList<>();

		// TODO parse the file and construct world

		// start the world clock
		startTiming();
	}

	/**
	 * Constructor for testing
	 *
	 * @param file
	 */
	public Game(World world, Map<TransitionSpace, Area> entrances) {

		players = new HashMap<>();
		torches = new ArrayList<>();

		this.world = world;
		this.entrances = entrances;

		this.player = new Player(5, "John Doe", Virus.T_Veronica,world);
		joinPlayer(this.player);
		// start the world clock
		//startTiming();
	}


	/**
	 * @param The world
	 * @param Transition spaces that lead to other areas.
	 * @param The player.
	 */
	public Game(World world, Map<TransitionSpace, Area> entrances, Player player) {

		this.world = world;
		this.entrances = entrances;
		this.player = player;
		this.players = new HashMap<>();
		this.torches = new ArrayList<>();
		joinPlayer(this.player);

		// start the world clock
		startTiming();
	}


	public Map<TransitionSpace, Area> getEntrances(){
		return this.entrances;
	}

	private void startTiming() {
		timer = new Timer();
		// this timer will constantly decrease every player's life by 1 minute.
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				// only for testing time lapse in text-client
				System.out.println("Current time:" + clock.getHour() + ":"
						+ clock.getMinute() + ":" + clock.getSecond());

				// decrease every player's life
				for (Player p : players.values()) {
					p.increaseHealth(-1);

					// only for testing time lapse in text-client
					System.out.println("Your life: " + p.getHealthLeft());
				}

				// decrease every torch's time left
				for (Torch t : torches) {
					if (t.isFlaming()) {
						t.Burn();

						// only for testing time lapse in text-client
						System.out.println("The torch's time: " + t.getTimeLeft());
					}
				}

				// time advance by 1 second
				clock = clock.plusSeconds(1);
			}
		}, 1000, 1000);
	}

	public World getWorld() {
		return world;
	}

	/**
	 * This method returns the clock of the world. The world time is constantly advancing.
	 *
	 * @return
	 */
	public LocalTime getClock() {
		return clock;
	}

	public Player getPlayer() {
		return player;
	}

	public void joinPlayer(Player player) {


		// =======================================
		/*
		 * this commented code randomely spawn player in World or in Room. Sounds fun. But
		 * that brings a problem of level design, which is where the key should be
		 * located. For example, if a player is spawned inside a room, and it's locked,
		 * and the key is not located inside room, the player is doomed. The key cannot
		 * simply placed inside room as well, cos outside players can't get the key to
		 * unlock room and get in.
		 */

		// // more chance to spawn in world map, less chance to spawn in room
		// Random ran = new Random();
		// if (ran.nextFloat() < SPAWN_IN_WORLD_CHANCE) {
		// // let's spawn the player in world
		// gs = world.getPlayerSpawnPos(this);
		// } else {
		// // let's spawn the player in a random room
		// List<Room> roomsList = new ArrayList<>(rooms.values());
		// int index = ran.nextInt(roomsList.size());
		// gs = roomsList.get(index).getPlayerSpawnPos(this);
		// }

		// ================================================================
		players.put(player.getId(), player);
		//If player has a position, then it has been loaded from a previous game, and does not need a new position.
		if(player.getPosition() != null){
			return;
		}
		Position pos = world.getPlayerSpawnPos(this);
		if (pos == null) {
			/*
			 * This should never happen. In theory, if the whole world doesn't have even
			 * one empty position left, this could happen. But that is almost impossible.
			 */
			throw new GameError("In theory: World full. More likely, there is a bug.");
		}

		player.setPosition(pos);


	}

	public void disconnectPlayer(Player player) {
		// TODO
		/*
		 * 1. delete player from player list. 2. delete his torch from torch list. 3. put
		 * the key(s) in his inventory on ground
		 */
	}

	public Player getPlayerById(int id) {
		Player player = players.get(id);
		if (player == null) {
			throw new GameError("Unknown player Id.");
		}

		return player;
	}

	/**
	 * This method check if the given position is occupied by other player.
	 *
	 * @param position
	 * @return
	 */
	public boolean isEmptyPosition(Position position) {

		for (Player p : players.values()) {
			if (p.getPosition() != null && p.getPosition().equals(position)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * This method tries to move the given player one step forward.
	 *
	 * @param player
	 * @return --- true if successful, or false if the player cannot move forward for some
	 *         reason, e.g. blocked by obstacle.
	 */
	public boolean playerMoveForward(Player player) {
		Position position = player.getPosition();
		Position forwardPos = player.getArea().getFrontPos(player);

		// check if the forward position is a valid one to move to
		if (!canMoveTo(player, forwardPos)) {
			return false;
		}

		// OK we can move him forward
		player.setPosition(forwardPos);
		return true;
	}

	/**
	 * This method tries to move the given player one step backward.
	 *
	 * @param player
	 * @return --- true if successful, or false if the player cannot move backward for
	 *         some reason, e.g. blocked by obstacle.
	 */
	public boolean playerMoveBackward(Player player) {
		Area currentArea = player.getArea();
		Position backPos = currentArea.getBackPos(player);

		// check if the back position is a valid one to move to
		if (!canMoveTo(player, backPos)) {
			return false;
		}

		// OK we can move him forward
		player.setPosition(backPos);
		return true;
	}

	/**
	 * This method tries to move the given player one step to the left.
	 *
	 * @param player
	 * @return --- true if successful, or false if the player cannot move left for some
	 *         reason, e.g. blocked by obstacle.
	 */
	public boolean playerMoveLeft(Player player) {
		Area currentArea = player.getArea();
		Position leftPos = currentArea.getLeftPos(player);

		// check if the left position is a valid one to move to
		if (!canMoveTo(player, leftPos)) {
			return false;
		}

		// OK we can move him forward
		player.setPosition(leftPos);
		return true;
	}

	/**
	 * This method tries to move the given player one step to the right.
	 *
	 * @param player
	 * @return --- true if successful, or false if the player cannot move right for some
	 *         reason, e.g. blocked by obstacle.
	 */
	public boolean playerMoveRight(Player player) {
		Area currentArea = player.getArea();
		Position rightPos = currentArea.getRightPos(player);

		// check if the right position is a valid one to move to
		if (!canMoveTo(player, rightPos)) {
			return false;
		}

		// OK we can move him forward
		player.setPosition(rightPos);
		return true;
	}

	/**
	 * Whether the given position is empty, i.e. a player can move into. Note that this
	 * method is meant to check only one step away.
	 *
	 * @param player
	 * @param position
	 * @return
	 */
	private boolean canMoveTo(Player player, Position position) {
		MapElement element = player.getArea().getMapElementAtIndex(position.x, position.y);
		// we cannot let him move out of board or move into obstacles
		if (position == null || (element == null && element instanceof Obstacle)) {
			return false;
		}

		// we cannot let him move into other players
		for (Player p : players.values()) {
			if (!p.equals(player) && p.getPosition().equals(player.getPosition())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * This method let the given player turn left.
	 *
	 * @param player
	 */
	public void playerTurnLeft(Player player) {
		player.turnLeft();
	}

	/**
	 * This method let the given player turn right.
	 *
	 * @param player
	 */
	public void playerTurnRight(Player player) {
		player.turnRight();
	}

	/**
	 * This method let the given player try to unlock a chest in front.
	 *
	 * @param player
	 * @return --- true if the chest is unlocked, or false if this action failed. Failure
	 *         can be caused by many reasons, for example it's not a chest in front, or
	 *         the player doesn't have a right key to open it.
	 */
	public boolean playerUnlockChest(Player player) {
		Area currentArea = player.getArea();
		Position p = player.getPosition();
		MapElement elementInFront = currentArea.getMapElementAtIndex(p.x, p.y);

		// if it's not a chest
		if (!(elementInFront instanceof Chest)) {
			return false;
		}

		return player.tryUnlockChest((Chest) elementInFront);
	}

	/**
	 * This method let the player try to take items from the chest in front. If the
	 * player's inventory can contain all items, he will take them all; otherwise he will
	 * take as many as he can until his inventory is full.
	 *
	 * @param player
	 * @return --- true if he has taken at least one item from the chest, or false if he
	 *         has taken none from the chest.
	 */
	public boolean playerTakeItemsInChest(Player player) {
		Area currentArea = player.getArea();
		Position position = player.getPosition();
		MapElement elementInFront = currentArea.getMapElementAtIndex(position.x, position.y);

		// if it's not a chest
		if (!(elementInFront instanceof Chest)) {
			return false;
		}

		return player.tryTakeItemsInChest((Chest) elementInFront);
	}

	/**
	 * This method let the given player try to unlock a room in front.
	 *
	 * @param player
	 * @return --- true if the room is unlocked, or false if this action failed. Failure
	 *         can be caused by many reasons, for example it's not a room in front, or the
	 *         player doesn't have a right key to open it.
	 */
	public boolean playerUnlockRoom(Player player) {
		Position position = player.getPosition();
		MapElement element = player.getArea().getMapElementAtIndex(position.x, position.y);

		// if the player is not standing on an entrance tile
		if (!(element instanceof TransitionSpace)) {
			return false;
		}

		TransitionSpace entrance = (TransitionSpace) element;

		// if the player is not facing the room door
		if (player.getDirection() != entrance.direction) {
			return false;
		}

		TransitionSpace rt = (TransitionSpace)entrance;
		Room room = (Room)this.entrances.get(rt);
		return player.tryUnlockDoor(room);
	}

	/**
	 * This method let the given player try to enter or exit a room depending on the
	 * player's current position.
	 *
	 * @param player
	 * @return --- true if the player changed to another area, or false if this action
	 *         failed for some reason, for example the player is not facing the door, or
	 *         he is too far from it.
	 */
	public boolean playerEnterExitRoom(Player player) {
		Position pos = player.getPosition();
		MapElement currentPos =player.getArea().getMapElementAtIndex(pos.x, pos.y);
		TransitionSpace entrance = null;
		if (currentPos instanceof TransitionSpace) {
			// the player is standing in front of a room ready to enter
			entrance = (TransitionSpace) currentPos;
			// if the player is not facing the room door
			if (player.getDirection() != entrance.direction) {
				return false;
			}
		}
		Room room = (Room)this.entrances.get(entrance);
		return player.tryEnterRoom(room);
	}

	/**
	 * Get the player's health left in seconds.
	 *
	 * @param player
	 * @return
	 */
	public int getPlayerHealth(Player player) {
		return player.getHealthLeft();
	}

	/**
	 * Get all items in the player's inventory as a list.
	 *
	 * @param player
	 * @return
	 */
	public List<Item> getPlayerInventory(Player player) {
		return player.getInventory();
	}

	/**
	 * This method let the player use an item.
	 *
	 * @param player
	 * @param item
	 */
	public void playerUseItem(Player player, Item item) {

		if (item instanceof Antidote) {
			// antidote
			Antidote ant = (Antidote) item;
			player.drinkAntidote(ant);
		} else if (item instanceof Torch) {
			// Torch
			player.lightUpTorch((Torch) item);
		} else if (item instanceof Key) {
			// Key

			/*
			 * XXX I don't want the key to be directly used. A key should be in player's
			 * inventory waiting to be automatically consumed when the player unlocks a
			 * chest or room.
			 */

		}

		// could have more else if clause if there are more types
	}

	/**
	 * This method let the player try to destroy an item.
	 *
	 * @param player
	 * @param item
	 * @return --- true if the item is destroyed, or false if the action failed.
	 */
	public boolean playerDestroyItem(Player player, Item item) {
		if (item instanceof Destroyable) {
			return player.destroyItem((Destroyable) item);
		}
		return false;
	}

	/**
	 * This method is used to automatically update the game status.
	 *
	 */
	public synchronized void update() {
		// TODO Auto-generated method stub
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;

		//Players map comparisons
		if(players.size() != other.players.size())
			return false;
		for(Map.Entry<Integer, Player> e : players.entrySet()){
			if(!other.players.containsKey(e.getKey()))
				return false;
			if(!e.getValue().equals(other.players.get(e.getKey())))
				return false;
		}
		for(Map.Entry<Integer, Player> e : other.players.entrySet()){
			if(!players.containsKey(e.getKey()))
				return false;
			if(!e.getValue().equals(players.get(e.getKey())))
				return false;
		}

		if (players == null) {
			if (other.players != null)
				return false;
		} else if (!players.equals(other.players))
			return false;

		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;

		//Entrances//
		if (entrances == null) {
			if (other.entrances != null)
				return false;
		if(entrances.size() != other.entrances.size())
			return false;
		//values
		List<Area> valA = new ArrayList<Area>(entrances.values());
		List<Area> valB = new ArrayList<Area>(other.entrances.values());
		//compare class types and then content
		for(Area a : valA){
			for(Area b: valB){
				if(a.getClass() == b.getClass()){
					if(!a.equals(b))
						return false;
				}
			}
		}
		//keys
		List<TransitionSpace> keyA = new ArrayList<TransitionSpace>(entrances.keySet());
		List<TransitionSpace> keyB = new ArrayList<TransitionSpace>(other.entrances.keySet());
		
		boolean matchFound = true;
		//compare class types and then content
		for(TransitionSpace a : keyA){
			for(TransitionSpace b: keyB){
				//if the spaces are equal then the values should also be. Also boolean should be set
				if(a.equals(b)){
					matchFound = true;
					Area v1 = entrances.get(a);
					Area v2 = other.entrances.get(b);
					if(!v1.equals(v2))
						return false;
				}
			}
			if(!matchFound)
				return false;
			matchFound = false;
		}	
	}/* else if (!entrances.equals(other.entrances))		//this check returns false even if all key value pairs match and arent null. Substitute checks above.
		return false;
		*/
	return true;
}




}
