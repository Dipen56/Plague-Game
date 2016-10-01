package server.game;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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
import server.game.player.Virus;
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
     * A static instance used in board grid to represent the ground space (no map object).
     */
    public static final GroundSpace GROUND_SPACE = new GroundSpace();

    /**
     * A means by which to simply compare game saves.
     */
    private int gameID = 0;

    /**
     * World map.
     * 
     * <p>
     * This field is actually redundant, because the world map is contained in
     * <i>areas</i> field as well. I keep it here just for not bringing major changes to
     * data storage component.
     */
    private Area world;

    /**
     * A table of all areas. Each area has its unique area id number. All areas and their
     * corresponding id number is recorded in this table.
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
     * All containers in the world. This is used for key re-distribution.
     */
    private List<Container> containers;

    /**
     * A timer for world clock. It starts when the Game object is constructed.
     */
    private Timer timer;

    /**
     * The world clock. It starts from a random time from 00:00:00 to 23:59:59
     */
    private LocalTime clock;

    /**
     * Constructor a game world without any players inside. Note that the first parameter
     * is not really necessary, as the second parameter will contain the world map. It is
     * a legacy parameter.
     *
     * @param world
     *            --- The main game world.
     * @param areas
     *            --- A map from areaID's to areas.
     */
    public Game(Area world, Map<Integer, Area> areas) {
        players = new HashMap<>();
        containers = new ArrayList<>();

        this.world = world;
        this.areas = areas;

        // a temporary random generator
        Random ran = new Random();

        this.gameID = ran.nextInt((5000 - 0) + 1);

        // the world clock starts from a random time from 00:00:00 to 23:59:59
        int hour = ran.nextInt(24);
        int minute = ran.nextInt(60);
        int second = ran.nextInt(60);
        clock = LocalTime.of(hour, minute, second);

        // scan the world map by map, remember all containers and torches.
        for (Area area : areas.values()) {
            MapElement[][] map = area.getMap();
            for (MapElement[] row : map) {
                for (MapElement col : row) {
                    // remember containers
                    if (col instanceof Container) {
                        Container container = (Container) col;
                        containers.add(container);
                    }
                }
            }
        }
    }

    /**
     * Constructor used for data storage.
     * 
     * @param world
     *            --- The main game world.
     * @param areas
     *            --- A map from areaID's to areas.
     * @param players
     *            --- A map from playerID's to players.
     * @param torches
     *            --- A list of torches. This can be null.
     * @param gameID
     *            --- A unique hash number for each game instance.
     */
    public Game(Area world, Map<Integer, Area> areas, Map<Integer, Player> players,
            List<Torch> torches, int gameID) {

        this.world = world;
        this.areas = areas;
        this.players = players;
        this.players = new HashMap<>();

        for (Player p : players.values()) {
            joinPlayer(p);
        }
        this.gameID = gameID;

        // the world clock starts from a random time from 00:00:00 to 23:59:59
        Random ran = new Random();
        int hour = ran.nextInt(24);
        int minute = ran.nextInt(60);
        int second = ran.nextInt(60);
        clock = LocalTime.of(hour, minute, second);
    }

    /**
     * Joins a player in game.
     *
     * @param player
     *            --- the player to be joined in
     */
    public void joinPlayer(Player player) {

        for (Player p : players.values()) {
            if (p.getId() == player.getId()) {
                // we cannot let another player joining in from same port
                return;
            }
        }

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
        // start ticking
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // decrease every player's life
                for (Player p : players.values()) {
                    p.increaseHealth(-1);

                    // if the player is holding a torch, decrease torch's life
                    if (p.isHoldingTorch()) {
                        Torch t = p.getTorchInHand();
                        if (t != null && t.isFlaming()) {
                            t.Burn();
                        }
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
     * @param playerId
     *            --- the id number of the disconnected player
     */
    public void disconnectPlayer(int playerId) {
        // delete player from player list.
        Player player = players.remove(playerId);

        // randomly re-distribute all keys in his inventory
        List<Key> hisKeys = player.getAllKeys();
        if (!hisKeys.isEmpty()) {
            for (Key k : hisKeys) {
                Collections.shuffle(containers);
                containers.get(0).getLoot().add(k);
            }
        }

    }

    /**
     * This method check if the given position is occupied by other player.
     *
     * @param position
     *            --- a coordinate to be checked
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
     *            --- the id number of the player
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
     *            --- the id number of the player
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
     *            --- the id number of the player
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
     *            --- the id number of the player
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
     *            --- the id number of the player
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
     *            --- the id number of the player
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
     * This method let the given player try to transit between areas (enter or exit a
     * room).
     *
     * @param uid
     *            --- the id number of the player
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

        // if the destPosition of this TransitionSpace is blocked
        Position destPos = currentTransition.getDestination();
        if (isOccupiedByOtherPlayer(destPos)) {
            return false;
        }

        // OK, time for space travel
        player.setPosition(destPos);
        return true;
    }

    /**
     * This method let the given player try to unlock a chest, room, or other lockable
     * object in front.
     *
     * @param uid
     *            --- the id number of the player
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
     * This method let the player try to take items from the container in front. If the
     * player's inventory can take in all items, he will take them all; otherwise he will
     * take as many as he can until his inventory is full.
     *
     * @param uid
     *            --- the id number of the player
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
     *            --- the id number of the player
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
     *            --- the id number of the player
     * @param index
     *            --- the index in inventory of item to be used
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
            Torch torch = (Torch) item;
            if (torch.isFlaming()) {
                player.extinguishTorch(torch);
            } else {
                player.lightUpTorch(torch);
            }
            return true;
        } else if (item instanceof Key) {
            // Key is not to be used manually
            return false;
        }

        // could have more else if clause if there are more types

        return false;
    }

    /**
     * This method let the player try to destroy an item.
     *
     * @param uid
     *            --- the id number of the player
     * @param index
     *            --- the index in inventory of item to be destroyed
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
     *            --- the id number of the player
     * @return --- the visibility of this player. In particular, it would be
     *         {@link server.game.Game #DAY_VISIBLIITY DAY_VISIBLIITY} at day time,
     *         {@link server.game.Game #NIGHT_VISIBILITY NIGHT_VISIBILITY} at night time
     *         if the player isn't holding a burning torch, and
     *         {@link server.game.Game #TORCH_VISIBILITY TORCH_VISIBILITY} at night time
     *         if the player is holding a burning torch.
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

    /**
     * Get the world map
     * 
     * @return --- the world map
     */
    public Area getWorld() {
        return world;
    }

    /**
     * Get the table of areas
     * 
     * @return --- the table of areas as a Map where the key is areaId, and the value is
     *         area.
     */
    public Map<Integer, Area> getAreas() {
        return areas;
    }

    /**
     * This method returns the clock of the world. The world time is constantly advancing.
     *
     * @return --- current time in the world.
     */
    public LocalTime getClock() {
        return clock;
    }

    /**
     * For testing, will be deleted.
     * 
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get all players currently logged in game.
     * 
     * @return --- all players as a map, where the key is player id, and the value is the
     *         player.
     */
    public Map<Integer, Player> getPlayers() {
        return players;
    }

    /**
     * Get the player by the player id.
     * 
     * @param uid
     *            --- the id number of the player
     * @return --- the corresponding player.
     */
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
     *            --- the id number of the player
     * @return --- the current health of this player.
     */
    public int getPlayerHealth(int uid) {
        Player player = players.get(uid);
        return player.getHealthLeft();
    }

    /**
     * Get the player's Virus type
     *
     * @param uid
     *            --- the id number of the player
     * @return --- the player's virus type.
     */
    public Virus getPlayerVirus(int uid) {
        Player player = players.get(uid);
        return player.getVirus();
    }

    /**
     * Get the unique game instance id.
     * 
     * @return --- the unique game instance id
     */
    public int getGameID() {
        return this.gameID;
    }

    /**
     * This method is used to generate the string for broadcasting world time to clients.
     * The String has the following format:
     * 
     * <p>
     * Say current time is hh:mm:ss <i>10:20:30</i>:
     * 
     * <p>
     * The string will be <i>"10:20:30"</i>
     * 
     * @return --- a string representation of the world time. This is used for network
     *         transmission.
     */
    public synchronized String getClockString() {
        int hour = clock.getHour();
        int minute = clock.getMinute();
        int second = clock.getSecond();
        return hour + ":" + minute + ":" + second;
    }

    /**
     * Generate a String of all players' avatars so the client who get this string will
     * know other player's avatar. The String has the following format:
     * <i>"uId_1,avatar_index_1|uId_2,avatar_index_2"</i>
     * 
     * <p>
     * Say 2 players currently in game:
     * <li>player 1, id 111, avatar index 0
     * <li>player 2, id 222, avatar index 1<br>
     * <br>
     * <p>
     * The string representation will be <i>"111,0|222,1"</i>
     * 
     * @return --- a string representation of all players and their chosen avatars. This
     *         is used for network transmission.
     */
    public String getAvatarsString() {
        StringBuilder sb = new StringBuilder();
        for (Player p : players.values()) {
            sb.append(p.getId());
            sb.append(",");
            sb.append(p.getAvatar().ordinal());
            sb.append("|");
        }
        return sb.toString();
    }

    /**
     * Generate a String of the status of all players' torch, i.e. whether he is holding a
     * (lighted) torch or not. This is used for the renderer at client side. The String
     * has the following format: <i>"uId_1,true/false|uId_2,true/false"</i>, where true or
     * false is represented as 1 or 0.
     * 
     * <p>
     * Say 2 players currently in game:
     * <li>player 1, id 111, is holding torch
     * <li>player 2, id 222, is not holding torch<br>
     * <br>
     * <p>
     * The string representation will be <i>"111,1|222,0"</i>
     * 
     * @return --- a string representation of the status of all players' torch. This is
     *         used for network transmission.
     */
    public String getTorchStatusString() {
        StringBuilder sb = new StringBuilder();
        for (Player p : players.values()) {
            sb.append(p.getId());
            sb.append(",");
            sb.append(p.isHoldingTorch() ? '0' : '1');
            sb.append("|");
        }
        return sb.toString();
    }

    /**
     * This method is used to generate the string for broadcasting player's inventory to
     * clients. The String has the following format:
     *
     * <p>
     * Say an item (type A, description B), its string representation will be
     * <i>"A@B"</i>, where A is a single character, B is the return of <i>toString()</i>.
     * Every two items are separated with a '|' character.
     * <p>
     * For example, this player has an Antidote (description: "foofoo"), and a Key
     * (description: "barbar").
     *
     * <p>
     * The string representation of his inventory will be <i>"A@foofoo|B@barbar"</i>
     * <p>
     * Character abbreviation table:<br>
     *
     * <li>A: Antidote<br>
     * <li>K: Key<br>
     * <li>T: Torch<br>
     * <br>
     *
     * @param uid
     *            --- the id number of the player
     * @return --- a string representation of all items in this player's inventory. This
     *         is used for network transmission.
     */
    public String getPlayerInventoryString(int uid) {
        Player player = players.get(uid);
        List<Item> inv = player.getInventory();

        StringBuilder sb = new StringBuilder();

        for (Item i : inv) {
            if (i instanceof Antidote) {
                sb.append("A@");
            } else if (i instanceof Key) {
                sb.append("K@");
            } else if (i instanceof Torch) {
                sb.append("T@");
            }
            sb.append(i.toString());
            sb.append("|");
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((areas == null) ? 0 : areas.hashCode());
        result = prime * result + ((clock == null) ? 0 : clock.hashCode());
        result = prime * result + ((containers == null) ? 0 : containers.hashCode());
        result = prime * result + gameID;
        result = prime * result + ((player == null) ? 0 : player.hashCode());
        result = prime * result + ((players == null) ? 0 : players.hashCode());
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
        if (containers == null) {
            if (other.containers != null)
                return false;
        } else if (!containers.equals(other.containers))
            return false;
        if (gameID != other.gameID)
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
        if (world == null) {
            if (other.world != null)
                return false;
        } else if (!world.equals(other.world))
            return false;
        return true;
    }

}
