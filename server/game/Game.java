package server.game;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import server.game.items.Antidote;
import server.game.items.Destroyable;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
import server.game.player.Player;
import server.game.player.Position;
import server.game.world.Area;
import server.game.world.Container;
import server.game.world.GroundSpace;
import server.game.world.Lockable;
import server.game.world.MapElement;
import server.game.world.Obstacle;
import server.game.world.Room;
import server.game.world.TransitionSpace;

/**
 * This class represents the game.
 * 
 * TODO <br>
 * 1. the game is not detecting win condition<br>
 * 2. it doesn't support save/load yet<br>
 * 3. it doesn't support trading system yet<br>
 * 4. it has no npc or enemy.<br>
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Game {

    /**
     * The visibility in daytime. This number indicates that everything within this
     * distance on world grid is visible.
     */
    public static final int DAY_VISIBLIITY = 8;
    /**
     * The visibility in night time. This number indicates that everything within this
     * distance on world grid is visible.
     */
    public static final int NIGHT_VISIBILITY = 2;
    /**
     * The visibility if the player is holding a torch in night time. This number
     * indicates that everything within this distance on world grid is visible.
     */
    public static final int TORCH_VISIBILITY = 5;

    /**
     * A new player has this chance of spawning in world map. If not spawned in world, the
     * player will be spawned in a random room.
     */
    // private static final float SPAWN_IN_WORLD_CHANCE = 0.6f;

    public static final GroundSpace groundSpace = new GroundSpace();
    
    /**
     * World map
     */
    private Area world;
    /**
     * Each area has its unique area id number. All areas and their corresponding id
     * number is recorded in this map.
     */
    private Map<Integer, Area> areas;
    /**
     * players and their id. Server can find player easily by looking by id.
     */
    private Map<Integer, Player> players;
    /**
     * For testing. Will be removed.
     */
    private Player player;
    /**
     * All torches in this world. It is used to track torch burning status in timer.
     */
    private List<Torch> torches;
    /**
     * A timer for world clock. It starts when the Game object is constructed.
     */
    private Timer timer;
    /**
     * The world clock. It starts from a random time from 00:00:00 to 23:59:59
     */
    private LocalTime clock;

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

        // TODO scan the world, so some initialisation job:
        // 1. remember all containers (for key re-distribution, and for open/close status
        // update for rendering)
        // 2. remember all torches and put them into torches list (for torch track)
        // 3. join in players

        // last, start timing.

        // these could be integrated into one method initialise();
    }

    /**
     * Constructor for text-based UI. Will not exist in final version.
     * 
     * @param world
     * @param entrances
     */
    public Game(Area world, Map<Integer, Area> areas) {

        players = new HashMap<>();
        torches = new ArrayList<>();

        this.world = world;
        this.areas = areas;
    }

    /**
	 * Constructor used for data storage. 
	 * @param The main game world.
	 * @param A map from areaID's to areas.
	 * @param A map from playerID's to players.
	 * @param A list of torches. This can be null.
	 */
	public Game(Area world, Map<Integer, Area> areas, Map<Integer, Player> players, List<Torch> torches) {
		//torhces 

		this.world = world;
		this.areas = areas;
		this.players = players;
		this.players = new HashMap<>();
		if(torches == null){
			this.torches = new ArrayList<>();
		}
		else{
			this.torches = torches;
		}
		for(Player p : players.values()){
			joinPlayer(p);
		}
	}

	public List<Torch> getTorches() {
		return this.torches;
	}

    /**
     * Joins a player in game.
     * 
     * @param player
     */
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

        /*
         * If player has a position, then it has been loaded from a previous game, and
         * does not need a new position.
         */
        if (player.getPosition() != null) {
            return;
        }

        // Let's spawn the player in a random location.
        Position pos = world.getPlayerSpawnPos(this);

        /*
         * This should never happen. In theory, if the whole world doesn't have even one
         * empty position left, this could happen. But that is almost impossible.
         */
        if (pos == null) {
            throw new GameError("In theory: World full. More likely, there is a bug.");
        }

        player.setPosition(pos);
    }

    /**
     * Start the world time. The world time is constantly advancing. As long as the server
     * is running, no other events will stop it.
     */
    public void startTiming() {
        // the world clock starts from a random time from 00:00:00 to 23:59:59
        Random ran = new Random();
        int hour = ran.nextInt(24);
        int minute = ran.nextInt(60);
        int second = ran.nextInt(60);
        clock = LocalTime.of(hour, minute, second);

        // start ticking
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // decrease every player's life
                for (Player p : players.values()) {
                    p.increaseHealth(-1);
                }

                // decrease every torch's time left
                for (Torch t : torches) {
                    if (t.isFlaming()) {
                        t.Burn();
                    }
                }

                // time advance by 1 second
                clock = clock.plusSeconds(1);
            }
        }, 1000, 1000);
    }

    /**
     * Disconnect the player, and re-distribute all his keys to locked containers.
     * 
     * @param player
     */
    public void disconnectPlayer(int playerId) {
        // delete player from player list.
        Player player = players.remove(playerId);

        // delete his torch from torch list.
        List<Torch> hisTorches = player.getAllTorches();
        torches.removeAll(hisTorches);

        // TODO need to deal with keys in his inventory. Probably re-distribute them
        List<Key> hisKeys = player.getAllKeys();

    }

    /**
     * This method check if the given position is occupied by other player.
     *
     * @param position
     * @return --- true if there is another player in that position; or false if not.
     */
    public boolean isOccupiedByOtherPlayer(Position position) {
        if (position == null) {
            return true;
        }

        for (Player p : players.values()) {
            Position pos = p.getPosition();
            if (pos == null) {
                continue;
            }

            if (pos.areaId == position.areaId && pos.x == position.x
                    && pos.y == position.y) {
                return true;
            }

        }
        return false;
    }

    /**
     * This method tries to move the given player one step forward.
     *
     * @param uid
     * @return --- true if successful, or false if the player cannot move forward for some
     *         reason, e.g. blocked by obstacle.
     */
    public boolean playerMoveForward(int uid) {
        Player player = players.get(uid);
        Position currentPosition = player.getPosition();
        Area currentArea = areas.get(currentPosition.areaId);
        MapElement frontMapElement = currentArea.getFrontMapElement(player);

        // check if there is obstacles in front
        if (frontMapElement == null || frontMapElement instanceof Obstacle) {
            return false;
        }

        Position forwardPosition = currentPosition.frontPosition();

        // check if it's out of board
        if (!currentArea.isInBoard(forwardPosition)) {
            return false;
        }

        // check if there are other players in front
        if (isOccupiedByOtherPlayer(forwardPosition)) {
            return false;
        }

        // OK we can move him forward
        player.setPosition(forwardPosition);
        return true;
    }

    /**
     * This method tries to move the given player one step backward.
     *
     * @param uid
     * @return --- true if successful, or false if the player cannot move backward for
     *         some reason, e.g. blocked by obstacle.
     */
    public boolean playerMoveBackward(int uid) {
        Player player = players.get(uid);
        Position currentPosition = player.getPosition();
        Area currentArea = areas.get(currentPosition.areaId);
        MapElement backMapElement = currentArea.getBackMapElement(player);

        // check if there is obstacles in front
        if (backMapElement == null || backMapElement instanceof Obstacle) {
            return false;
        }

        Position backPosition = currentPosition.backPosition();

        // check if it's out of board
        if (!currentArea.isInBoard(backPosition)) {
            return false;
        }

        // check if there are other players in front
        if (isOccupiedByOtherPlayer(backPosition)) {
            return false;
        }

        // OK we can move him forward
        player.setPosition(backPosition);
        return true;
    }

    /**
     * This method tries to move the given player one step to the left.
     *
     * @param uid
     * @return --- true if successful, or false if the player cannot move left for some
     *         reason, e.g. blocked by obstacle.
     */
    public boolean playerMoveLeft(int uid) {
        Player player = players.get(uid);
        Position currentPosition = player.getPosition();
        Area currentArea = areas.get(currentPosition.areaId);
        MapElement leftMapElement = currentArea.getLeftMapElement(player);

        // check if there is obstacles in front
        if (leftMapElement == null || leftMapElement instanceof Obstacle) {
            return false;
        }

        Position leftPosition = currentPosition.leftPosition();

        // check if it's out of board
        if (!currentArea.isInBoard(leftPosition)) {
            return false;
        }

        // check if there are other players in front
        if (isOccupiedByOtherPlayer(leftPosition)) {
            return false;
        }

        // OK we can move him forward
        player.setPosition(leftPosition);
        return true;
    }

    /**
     * This method tries to move the given player one step to the right.
     *
     * @param uid
     * @return --- true if successful, or false if the player cannot move right for some
     *         reason, e.g. blocked by obstacle.
     */
    public boolean playerMoveRight(int uid) {
        Player player = players.get(uid);
        Position currentPosition = player.getPosition();
        Area currentArea = areas.get(currentPosition.areaId);
        MapElement rightMapElement = currentArea.getRightMapElement(player);

        // check if there is obstacles in front
        if (rightMapElement == null || rightMapElement instanceof Obstacle) {
            return false;
        }

        Position rightPosition = currentPosition.rightPosition();

        // check if it's out of board
        if (!currentArea.isInBoard(rightPosition)) {
            return false;
        }

        // check if there are other players in front
        if (isOccupiedByOtherPlayer(rightPosition)) {
            return false;
        }

        // OK we can move him forward
        player.setPosition(rightPosition);
        return true;
    }

    /**
     * This method let the given player turn left.
     *
     * @param uid
     */
    public void playerTurnLeft(int uid) {
        Player player = players.get(uid);

        // This patch code fix the bug that when transit player's direction is changed.
        Position currentPosition = player.getPosition();
        Area currentArea = areas.get(currentPosition.areaId);
        MapElement currentMapElement = currentArea.getMapElementAt(currentPosition.x,
                currentPosition.y);
        if (currentMapElement instanceof TransitionSpace) {
            player.setPosition(new Position(currentPosition.x, currentPosition.y,
                    currentPosition.areaId, player.getPosition().getDirection().left()));
            return;
        }

        player.turnLeft();
    }

    /**
     * This method let the given player turn right.
     *
     * @param uid
     */
    public void playerTurnRight(int uid) {
        Player player = players.get(uid);

        // This patch code fix the bug that when transit player's direction is changed.
        Position currentPosition = player.getPosition();
        Area currentArea = areas.get(currentPosition.areaId);
        MapElement currentMapElement = currentArea.getMapElementAt(currentPosition.x,
                currentPosition.y);
        if (currentMapElement instanceof TransitionSpace) {
            player.setPosition(new Position(currentPosition.x, currentPosition.y,
                    currentPosition.areaId, player.getPosition().getDirection().right()));
            return;
        }

        player.turnRight();
    }

    /**
     * This method let the given player try to unlock a chest, room, or other lockable
     * object in front.
     * 
     * @param uid
     * @return --- true if the loackable is unlocked, or false if this action failed.
     *         Failure can be caused by many reasons, for example it's not a lockable in
     *         front, or the player doesn't have a right key to open it.
     */
    public boolean playerUnlockLockable(int uid) {
        Player player = players.get(uid);
        Position currentPosition = player.getPosition();
        Area currentArea = areas.get(currentPosition.areaId);
        MapElement currentMapElement = currentArea.getMapElementAt(currentPosition.x,
                currentPosition.y);
        MapElement frontMapElement = currentArea.getFrontMapElement(player);
        Lockable lockable = null;

        if (currentMapElement instanceof TransitionSpace) {
            // is the player standing on a TransitionSpace?
            TransitionSpace currentTransition = (TransitionSpace) currentMapElement;
            Area destArea = areas.get(currentTransition.getDestination().areaId);

            // is the player facing the room?
            if (player.getDirection() == currentTransition.getFacingDirection()
                    && destArea instanceof Room) {
                lockable = (Room) destArea;
            }
        }

        if (frontMapElement != null && frontMapElement instanceof Lockable) {
            // is the player facing a lockable container?
            lockable = (Lockable) frontMapElement;
        }

        // ok let's try to unlock it.
        if (lockable != null) {
            return player.tryUnlock(lockable);
        } else {
            return false;
        }
    }

    /**
     * This method let the given player try to transit between areas (enter or exit a
     * room).
     * 
     * @param uid
     * @return --- true if the player changed to another area, or false if this action
     *         failed for some reason, for example the player is not facing the door, or
     *         he is too far from it.
     */
    public boolean playerTransit(int uid) {
        Player player = players.get(uid);
        Position currentPosition = player.getPosition();
        Area currentArea = areas.get(currentPosition.areaId);
        MapElement currentMapElement = currentArea.getMapElementAt(currentPosition.x,
                currentPosition.y);

        // no it's not a TransitionSpace
        if (!(currentMapElement instanceof TransitionSpace)) {
            return false;
        }

        TransitionSpace currentTransition = (TransitionSpace) currentMapElement;
        Area destArea = areas.get(currentTransition.getDestination().areaId);
        // no it's a room in the other end and it's locked
        if (destArea instanceof Room && ((Room) destArea).isLocked()) {
            return false;
        }

        // no the player is not facing the right direction
        if (player.getDirection() != currentTransition.getFacingDirection()) {
            return false;
        }

        // OK, time for space travel
        player.setPosition(currentTransition.getDestination());
        return true;
    }

    /**
     * This method let the player try to take items from the container in front. If the
     * player's inventory can take in all items, he will take them all; otherwise he will
     * take as many as he can until his inventory is full.
     *
     * @param uid
     * @return --- true if he has taken at least one item from the container, or false if
     *         he has taken none from the container.
     */
    public boolean playerTakeItemsFromContainer(int uid) {
        Player player = players.get(uid);
        Position currentPosition = player.getPosition();
        Area currentArea = areas.get(currentPosition.areaId);
        MapElement frontMapElement = currentArea.getFrontMapElement(player);

        // no it's not a container
        if (!(frontMapElement instanceof Container)) {
            return false;
        }

        return player.tryTakeItemsFromContainer((Container) frontMapElement);
    }

    /**
     * This method let the player try to put an item into a container (chest or cupboard,
     * etc.) in front.
     *
     * @param uid
     * @param index
     *            --- the index in inventory of item to be put in
     * @return --- true if the action succeeded; or false if failed (most likely when the
     *         container is full or locked).
     */
    public boolean playerPutItemIntoContainer(int uid, int index) {
        Player player = players.get(uid);
        Position currentPosition = player.getPosition();
        Area currentArea = areas.get(currentPosition.areaId);
        MapElement frontMapElement = currentArea.getFrontMapElement(player);

        // no it's not a container
        if (!(frontMapElement instanceof Container)) {
            return false;
        }

        return player.tryPutItemsIntoContainer((Container) frontMapElement, index);
    }

    /**
     * This method let the player use an item at index in inventory.
     *
     * @param uid
     * @param index
     * @return --- true if the item is used, or false if the action failed.
     */
    public boolean playerUseItem(int uid, int index) {
        Player player = players.get(uid);

        List<Item> inventory = player.getInventory();

        if (index < 0 || index >= inventory.size()) {
            return false;
        }

        Item item = inventory.get(index);

        if (item instanceof Antidote) {
            // antidote
            Antidote ant = (Antidote) item;
            player.drinkAntidote(ant);
            return true;
        } else if (item instanceof Torch) {
            // Torch
            player.lightUpTorch((Torch) item);
            return true;
        } else if (item instanceof Key) {
            // Key
            return false;
            /*
             * XXX We can but I don't really want the key to be directly used. A key
             * should be in player's inventory waiting to be automatically consumed when
             * the player unlocks a chest or room.
             */

        }

        // could have more else if clause if there are more types

        return false;
    }

    /**
     * This method let the player try to destroy an item.
     *
     * @param uid
     * @param index
     * @return --- true if the item is destroyed, or false if the action failed.
     */
    public boolean playerDestroyItem(int uid, int index) {
        Player player = players.get(uid);

        List<Item> inventory = player.getInventory();

        if (index < 0 || index >= inventory.size()) {
            return false;
        }

        Item item = inventory.get(index);

        if (item instanceof Destroyable) {
            return player.destroyItem((Destroyable) item);
        }
        return false;
    }

    /**
     * Gets the specified player's visibility according to current time.
     * 
     * @param uid
     * @return
     */
    public int getPlayerVisibility(int uid) {
        Player player = players.get(uid);

        if (clock.getHour() >= 6 && clock.getHour() < 18) {
            // it's day time
            return DAY_VISIBLIITY;
        } else {
            // it's night time
            if (player.isHoldingTorch()) {
                return TORCH_VISIBILITY;
            } else {
                return NIGHT_VISIBILITY;
            }
        }
    }

    public Area getWorld() {
        return world;
    }

    public Map<Integer, Area> getAreas() {
        return areas;
    }

    /**
     * This method returns the clock of the world. The world time is constantly advancing.
     *
     * @return
     */
    public LocalTime getClock() {
        return clock;
    }

    /**
     * This method is used to generate the string for broadcasting world time to clients.
     * The String has the following format:
     * 
     * <p>
     * Say current time is hh:mm:ss <i>10:20:30</i>:
     * 
     * <p>
     * The string will be <i>"10,20,30"</i>
     * 
     * @return
     */
    public String getClockString() {
        int hour = clock.getHour();
        int minute = clock.getMinute();
        int second = clock.getSecond();
        return hour + "," + minute + "," + second;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    /**
     * For testing, will be deleted.
     * 
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    public Player getPlayerById(int uid) {
        Player player = players.get(uid);
        if (player == null) {
            throw new GameError("Unknown player Id.");
        }

        return player;
    }

    /**
     * Get the player's health left in seconds.
     *
     * @param uid
     * @return
     */
    public int getPlayerHealth(int uid) {
        Player player = players.get(uid);
        return player.getHealthLeft();
    }

    /**
     * Get all items in the player's inventory as a list.
     *
     * @param uid
     * @return
     */
    public List<Item> getPlayerInventory(int uid) {
        Player player = players.get(uid);
        return player.getInventory();
    }

    /**
     * This method is used to generate the string for broadcasting player's inventory to
     * clients. The String has the following format:
     * 
     * <p>
     * Say an item (type A, description B), its string representation will be
     * <i>"A|B"</i>, where A is a single character, B is the return of <i>toString()</i>.
     * Every two items are separated with a line separator.
     * 
     * <p>
     * For example, this player has an Antidote (description: "foofoo"), and a Key
     * (description: "barbar").
     * 
     * <p>
     * The string representation of his inventory will be <i>"A|foofoo\nB|barbar"</i>
     * 
     * <p>
     * Character abbreviation table:<br>
     * 
     * <li>A: Antidote<br>
     * <li>K: Key<br>
     * <li>T: Torch<br>
     * <br>
     * 
     * @param uid
     * 
     * @return
     */
    public String getPlayerInventoryString(int uid) {
        Player player = players.get(uid);
        List<Item> inv = player.getInventory();

        StringBuilder sb = new StringBuilder();

        for (Item i : inv) {
            if (i instanceof Antidote) {
                sb.append("A|");
            } else if (i instanceof Key) {
                sb.append("K|");
            } else if (i instanceof Torch) {
                sb.append("T|");
            }
            sb.append(i.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((areas == null) ? 0 : areas.hashCode());
        result = prime * result + ((clock == null) ? 0 : clock.hashCode());
        result = prime * result + ((player == null) ? 0 : player.hashCode());
        result = prime * result + ((players == null) ? 0 : players.hashCode());
        result = prime * result + ((torches == null) ? 0 : torches.hashCode());
        result = prime * result + ((world == null) ? 0 : world.hashCode());
        return result;
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
        
        //experiment to check map contents
        System.out.println("areas");
        for(Map.Entry<Integer, Area>e :areas.entrySet()){
        	System.out.printf("Key: %s\n",e.getKey());
        	System.out.printf("Value: %s\n",e.getValue());
        }
        System.out.println("\n other.areas");
        for(Map.Entry<Integer, Area>e :other.areas.entrySet()){
        	System.out.printf("Key: %s\n",e.getKey());
        	System.out.printf("Value: %s\n",e.getValue());
        }
        
        
        if(!areas.entrySet().equals(other.areas.entrySet())){
        	return false;
        }
        
        if(!areas.keySet().equals(other.areas.keySet())){
        	return false;
        }
        
        
        if (areas == null) {
        	
            if (other.areas != null)
                return false;
        } else if (!areas.equals(other.areas))
            return false;
        if (clock == null) {
            if (other.clock != null)
                return false;
        } else if (!clock.equals(other.clock))
            return false;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        if (players == null) {
            if (other.players != null)
                return false;
        } else if (!players.equals(other.players))
            return false;
        if (torches == null) {
            if (other.torches != null)
                return false;
        } else if (!torches.equals(other.torches))
            return false;
        if (world == null) {
            if (other.world != null)
                return false;
        } else if (!world.equals(other.world))
            return false;
        return true;
    }

}
