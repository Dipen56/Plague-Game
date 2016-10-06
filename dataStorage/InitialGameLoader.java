package dataStorage;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import server.game.Game;
import server.game.items.Antidote;
import server.game.items.Item;
import server.game.items.Key;
import server.game.player.Direction;
import server.game.player.Position;
import server.game.player.Virus;
import server.game.world.Area;
import server.game.world.Chest;
import server.game.world.Cupboard;
import server.game.world.GroundSpace;
import server.game.world.MapElement;
import server.game.world.Obstacle;
import server.game.world.Room;
import server.game.world.ScrapPile;
import server.game.world.TransitionSpace;

public class InitialGameLoader {

    /**
     * For a plain piece of ground.
     */
    private static GroundSpace g = new GroundSpace();

    // Obstacles which are used more than once.

    /**
     * An empty "room square".
     */
    private static Obstacle e = new Obstacle("E");

    /**
     * A tree.
     */
    private static Obstacle t = new Obstacle("T");

    /**
     * A rock.
     */
    private static Obstacle r = new Obstacle("R");

    /**
     * A chair.
     */
    private static Obstacle c = new Obstacle("H");

    /**
     * A barrel.
     */
    private static Obstacle b = new Obstacle("B");

    /**
     * A table.
     */
    private static Obstacle tb = new Obstacle("A");

    /**
     * A list of keys to containers and rooms.
     */
    private static List<Key> keys;

    /**
     * A list of loot lists for chests.
     */
    private static List<List<Item>> chestLoot;

    /**

     * A list of loot lists for cupboards.
     */
    private static List<List<Item>> cupboardLoot;

    /**
     * A list of loot lists for scrap piles.
     */
    private static List<List<Item>> scrapLoot;

    /**
     * An array of cupboard objects to put in rooms.
     */
    private static Cupboard[] cp;

    /**
     * A list of area ids for rooms and the main map.
     */
    private static List<Integer> areaIDs;

    /**
     * An array of scrap pile objects to put in the main map.
     */
    private static ScrapPile[] s;

    /**
     * An array of chest objects to be put in the main map and in rooms.
     */
    private static Chest[] ch;

    /**
     * Constants dictating the number containers and rooms.
     */
    public static final int NUMBER_OF_CHESTS = 23, NUMBER_OF_CUPBOARDS = 6,
            NUMBER_OF_S_PILES = 18, NUMBER_OF_ROOMS = 4;

    /**
     * Multipliers for the antidote placement in containers.
     */
    public static final double ANTIDOTE_CHEST_MULT = 0.6, ANTIDOTE_S_PILE_MULT = 0.1,
            ANTIDOTE_CUPBOARD_MULT = 0.8;

    /**
     * Produces a Game object from a number of component objects.
     *
     * Keys to be distributed are ordered in the order: chest keys, cupboard keys, room
     * keys. So in order to get the key id for a certain cupboard you will need the index
     * of the cupboard relative to the the other cupboards, and also the number of chest
     * keys.
     *
     * KeyIDs are allocated based on their index in the keys list. KeysIDs 0-15 are for
     * outdoor chests. KeysIDs 16-22 are for indoor chests. KeysIDs 23-28 are for
     * cupboards.
     *
     * The following locations are required for mapping objects to the game maps. Chest
     * locations: Chests 0-15 are outdoors. Chest 16 is in room 2. Chests 17-21 are in
     * room 3. Chest 22 is in room 4.
     *
     * ScrapPiles: Objects 0-9 are outside. Objects 10-11 are in room1. Objects 12-13 are
     * in room3. Objects 14-17 are in room4
     *
     * Cupboards: Cupboard 0 is in room1. Cupboards 1-2 are in room2. Cupboards 3-4 are in
     * room3. Cupboard 5 is in room4.
     *
     * @return The game.
     */
    public static Game makeGame() {
        keys = new ArrayList<>();
        chestLoot = new ArrayList<>();
        cupboardLoot = new ArrayList<>();
        scrapLoot = new ArrayList<>();
        cp = new Cupboard[NUMBER_OF_CUPBOARDS]; // Used to fill all cupboards.
        areaIDs = new ArrayList<>();
        s = new ScrapPile[NUMBER_OF_S_PILES];
        ch = new Chest[NUMBER_OF_CHESTS]; // Used to fill all chests.

        // Initialises chest loot lists.
        initialiseListLootLists(chestLoot, NUMBER_OF_CHESTS);
        // Initialises cupboard loot lists.
        initialiseListLootLists(cupboardLoot, NUMBER_OF_CUPBOARDS);
        // Initialises chest loot lists.
        initialiseListLootLists(scrapLoot, NUMBER_OF_S_PILES);

        // Area ID numbers are generated
        /*
         * This allows TransitionSpace numbers to match indices. Also index 0 is the main
         * map.
         */
        areaIDs.add(0);
        Integer id = 0;
        for (int i = 1; i <= NUMBER_OF_ROOMS; i++) {
          /*  do {
               id = (int) (Math.random() * 29) + 1;
            
            } while (areaIDs.contains(id));
            */
        	
            areaIDs.add(i);
        }

        // Sets up all transition spaces.
        // Outside transition spaces
        TransitionSpace ts1 = new TransitionSpace(
                new Position(2, 5, areaIDs.get(0), Direction.North),
                new Position(0, 5, areaIDs.get(1), Direction.North));
        TransitionSpace ts2 = new TransitionSpace(
                new Position(13, 15, areaIDs.get(0), Direction.North),
                new Position(0, 4, areaIDs.get(2), Direction.North));
        TransitionSpace ts3 = new TransitionSpace(
                new Position(24, 16, areaIDs.get(0), Direction.East),
                new Position(0, 4, areaIDs.get(3), Direction.East));
        TransitionSpace ts4 = new TransitionSpace(
                new Position(8, 24, areaIDs.get(0), Direction.South),
                new Position(5, 0, areaIDs.get(4), Direction.South));
        // Inside transition spaces
        TransitionSpace ts5 = new TransitionSpace(
                new Position(0, 5, areaIDs.get(1), Direction.South),
                new Position(2, 5, areaIDs.get(0), Direction.South)); // In room1
        TransitionSpace ts6 = new TransitionSpace(
                new Position(0, 4, areaIDs.get(2), Direction.South),
                new Position(13, 15, areaIDs.get(0), Direction.South)); // In room2
        TransitionSpace ts7 = new TransitionSpace(
                new Position(0, 4, areaIDs.get(3), Direction.West),
                new Position(24, 16, areaIDs.get(0), Direction.West)); // In room3
        TransitionSpace ts8 = new TransitionSpace(
                new Position(5, 0, areaIDs.get(4), Direction.North),
                new Position(8, 24, areaIDs.get(0), Direction.North)); // In room4

        // Generates Keys
        Key key = null;
        int keyID = -1;
        // Creates a key based on a random number. If the list contains that same key,
        // make a new one.
        for (int i = 0; i < NUMBER_OF_CHESTS + NUMBER_OF_CUPBOARDS
                + NUMBER_OF_ROOMS; i++) {
            do {
                keyID = (int) (Math.random() * 1000);
                key = new Key("A key with number " + keyID, keyID);

            } while (keys.contains(key));
            // Adds the generated key to the lists.
            keys.add(key);
        }

        // Creates Chests and adds antidotes to them.
        for (int i = 0; i < NUMBER_OF_CHESTS; i++) {
        	List<Item> loot = chestLoot.get(i);
        	if(loot == null)
        		loot = new ArrayList<>();	//Uses empty list for empty chest.
            ch[i] = new Chest("A chest. Probably contains loot.", keys.get(i).getKeyID(),
                    true, loot);
            spawnAntidotes(ch[i].getLoot(), ANTIDOTE_CHEST_MULT);// Random chance of
                                                                 // adding antidotes to
                                                                 // Chest loot list
        }

        // Creates Cupboard and adds antidotes to them.
        for (int i = 0; i < NUMBER_OF_CUPBOARDS; i++) {
        	List<Item> loot = cupboardLoot.get(i);
        	if(loot == null)
        		loot = new ArrayList<>();	//Uses empty list for empty chest.
            cp[i] = new Cupboard("A cupboard. It might contain some medicine.",
                    keys.get(NUMBER_OF_CHESTS + i).getKeyID(), false, loot);

        }

        // Creates ScrapPiles
        for (int i = 0; i < NUMBER_OF_S_PILES; i++) {
        	List<Item> loot = scrapLoot.get(i);
        	if(loot == null)
        		loot = new ArrayList<>();	//Uses empty list for empty chest.
            s[i] = new ScrapPile("A pile of useless scrap. Or is it?", loot);
        }

        // Adds Room keys to random container
        allocateRoomKeys();

        // Adds container keys to containers
        addContainerKeysToContainers();

        /**
         * Key map key: g = groundspace t = tree r = rock b = barrel tb = table ch = chest
         * cp = cupboard s = scrap pile ts = transition space
         */

        // Room 1 map
        MapElement[][] room1Map = { { b, b, b, cp[0], b, tb }, { b, g, g, g, g, c },
                { b, g, g, g, g, g }, { g, g, g, g, g, b }, { g, g, g, g, g, g },
                { ts5, g, g, s[10], s[11], c } };

        // Room 2 map
        MapElement[][] room2Map = { { b, b, tb, tb, cp[1] }, { g, g, g, g, g },
                { g, b, c, tb, ch[16] }, { g, g, g, g, g }, { ts6, c, b, b, cp[2] } };

        // Room 3 map
        MapElement[][] room3Map = { { ch[17], b, cp[3], cp[4], ch[18] },
                { g, g, g, g, g }, { ch[19], g, c, tb, ch[20] }, { g, g, g, g, g },
                { ts7, g, s[12], s[13], ch[21] } };

        // Room 4 map
        MapElement[][] room4Map = { { s[14], g, tb, g, g, ts8 }, { g, g, g, g, g, g },
                { b, g, g, tb, g, b }, { c, g, tb, g, g, s[15] }, { g, g, g, g, g, g },
                { s[16], s[17], cp[5], b, b, ch[22] } };

        // Puts the rooms together.
        //TODO: change back to true 
        Room room1 = new Room(room1Map, areaIDs.get(1),
                keys.get(keys.size() - 4).getKeyID(), false);
        Room room2 = new Room(room2Map, areaIDs.get(2),
                keys.get(keys.size() - 3).getKeyID(), false);
        Room room3 = new Room(room3Map, areaIDs.get(3),
                keys.get(keys.size() - 2).getKeyID(), false);
        Room room4 = new Room(room4Map, areaIDs.get(4),
                keys.get(keys.size() - 1).getKeyID(), false);

        MapElement[][] worldMap = {
                { t, t, t, t, t, t, t, t, t, g, t, t, t, t, t, t, t, t, t, t, t, t, t, t,
                        t, t, t, t, t, t },
                { t, t, e, e, e, e, t, g, g, g, g, g, g, t, g, t, ch[0], r, g, g, r, t, t,
                        g, g, r, g, g, g, s[0] },
                { t, t, e, e, e, e, t, ch[1], g, g, g, g, g, t, g, t, g, r, g, g, g, g, g,
                        g, g, r, g, ch[2], g, t },
                { s[1], t, e, e, e, e, t, g, g, g, g, g, g, t, g, g, g, r, g, g, g, t, g,
                        g, g, g, g, g, g, t },
                { g, t, e, e, e, e, t, g, g, t, g, g, ch[3], t, g, g, g, g, g, g, g, t, g,
                        r, g, g, g, g, g, t },
                { g, g, ts1, g, g, g, g, g, g, t, r, r, r, g, g, s[2], g, g, r, r, r, r,
                        g, g, g, g, r, r, g, g },
                { t, g, g, g, g, g, g, g, g, t, g, g, g, g, g, g, g, g, r, g, g, t, g, t,
                        t, t, t, t, g, g },
                { t, g, g, g, g, g, s[3], g, g, t, g, g, g, g, g, g, g, g, r, g, g, t, g,
                        g, g, g, g, g, r, g },
                { t, g, g, g, g, g, g, g, g, t, g, g, g, g, r, r, g, g, g, g, g, t, g, r,
                        s[4], r, t, g, ch[4], g },
                { ch[5], g, g, g, g, g, r, g, g, t, g, g, g, g, r, ch[6], g, g, g, g, g,
                        t, g, r, r, r, t, g, g, g },
                { t, t, t, r, g, g, g, g, g, g, g, g, t, t, t, t, t, t, t, t, t, t, g, g,
                        g, g, t, t, t, g },
                { t, ch[7], g, r, r, g, g, g, g, g, g, g, t, e, e, e, e, t, g, g, ch[8],
                        t, g, g, g, g, g, g, g, g },
                { t, g, g, r, g, g, g, g, g, g, g, g, t, e, e, e, e, t, g, r, r, t, g, g,
                        t, t, t, t, t, t },
                { t, g, g, g, g, g, r, r, r, r, g, g, t, e, e, e, e, t, g, g, g, t, g, g,
                        g, e, e, e, e, t },
                { g, g, g, g, g, g, r, ch[9], g, g, g, r, t, e, e, e, e, t, g, g, g, t, g,
                        g, g, e, e, e, e, t },
                { t, t, t, g, r, r, g, g, g, g, g, r, g, ts2, g, g, g, g, g, g, g, t, g,
                        g, g, e, e, e, e, t },
                { s[5], g, g, g, g, g, g, g, g, g, r, g, g, g, g, g, g, g, g, g, g, t, g,
                        g, ts3, e, e, e, e, t },
                { t, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, ch[10], t,
                        g, g, t, t, t, t, t, t },
                { g, g, g, g, r, r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, t, g, g,
                        t, s[6], g, g, g, t },
                { g, g, g, g, s[7], r, g, g, g, t, t, t, t, g, g, s[8], g, g, g, g, g, t,
                        g, g, t, t, g, g, g, g },
                { t, g, g, g, r, g, g, g, g, t, g, g, g, g, g, g, g, g, g, g, g, t, g, g,
                        g, g, g, g, ch[11], g },
                { g, g, g, g, r, g, g, g, g, t, g, g, g, g, g, g, g, g, g, g, g, t, g, g,
                        g, g, g, g, r, r },
                { t, g, g, g, r, g, g, g, g, t, g, t, t, g, t, g, r, r, g, g, g, g, g, g,
                        g, g, g, g, t, t },
                { t, g, g, g, t, g, g, g, g, t, t, g, g, g, t, r, g, g, g, r, r, r, g, r,
                        r, r, r, g, g, g },
                { t, g, r, g, t, g, g, g, ts4, t, g, g, g, g, g, g, g, g, g, g, s[9], t,
                        g, r, g, g, ch[12], r, g, g },
                { g, r, ch[13], g, t, e, e, e, e, t, g, g, g, g, g, g, g, g, g, g, g, t,
                        g, r, g, g, g, r, g, g },
                { g, r, g, g, t, e, e, e, e, t, g, g, g, g, g, g, g, g, g, r, g, t, g, r,
                        g, g, g, r, r, g },
                { g, g, g, g, t, e, e, e, e, t, g, g, r, r, t, g, g, g, g, g, t, t, g, g,
                        g, g, g, g, g, g },
                { g, g, g, g, t, e, e, e, e, t, g, g, ch[14], r, t, g, g, g, g, g, g, g,
                        g, g, g, g, g, g, g, r },
                { g, t, t, t, t, t, t, t, t, t, g, g, g, g, t, t, t, t, t, t, t, ch[15],
                        g, t, t, t, t, t, t, t } };

        // Puts game world Area together.
        Area world = new Area(worldMap, areaIDs.get(0));

        // A map of all area ids and their game areas
        Map<Integer, Area> areas = new HashMap<>();

        // Adds each area to the map.
        areas.put(areaIDs.get(0), world);
        areas.put(areaIDs.get(1), room1);
        areas.put(areaIDs.get(2), room2);
        areas.put(areaIDs.get(3), room3);
        areas.put(areaIDs.get(4), room4);

        // NOTICE: VERY IMPORTANT, resister portals for each area in the game
        for (Area a : areas.values()) {
            a.registerPortals();
        }

        Game game = new Game(world, areas);
        return game;
    }

    /**
     * Initialises a number of lists of type Item within a list of items.
     *
     * @param The
     *            list of lists of items.
     * @param The
     *            number of lists to initialise inside the list of lists.
     */
    public static void initialiseListLootLists(List<List<Item>> list, int number) {
        for (int i = 0; i < number; i++) {
            list.add(new ArrayList<>());
        }
    }

    /**
     * Puts all room keys into loot lists of either randomly decided Chests and Cupboards.
     *
     */
    public static void allocateRoomKeys() {
        int containerIndex = 0, keyIndex = 0;
        for (int i = 0; i < NUMBER_OF_ROOMS; i++) {
            keyIndex = keys.size() - 4 + i;
            if (Math.random() * 10 > 5) {
                // Adds key to random outdoor chest
                // generates index of chest.
                containerIndex = (int) (Math.random() * 15 + 1);
                ch[containerIndex].getLoot().add(keys.get(keyIndex));
            } else {
                // Adds key to random scrap pile. Generates index of scrap pile. Value 9
                // is number of scrap piles outside.
                containerIndex = (int) (Math.random() * 9 + 1);
                s[containerIndex].getLoot().add(keys.get(keyIndex));
            }
        }
    }

    /**
     * Adds Antidote items to the loot list. The chance of adding 1 antidote = multiplier.
     * The chance of adding 2 antidote = multiplier/2. The chance of adding 3 antidote =
     * multiplier/3.
     *
     * @param loot
     *            The loot list
     * @param multiplier
     *            A double used as a multiplier
     *
     */
    private static void spawnAntidotes(List<Item> loot, double multiplier) {
        int result = 0;
        for (int i = 0; i < 3; i++) {
            result = (int) (Math.random() * 10);
            // As the multiplier decreases, the less chance of adding an
            // antidote.
            if (result > 10 - multiplier * 10) {
                Virus v = Virus.randomVirus(); // Adds an antidote to the list.
                String description = "A temporary antidote to " + v.name();
                loot.add(new Antidote(description, v));
                multiplier *= 0.5; // reduces multiplier for next attempt.
            } else {
                return;
            }
        }
    }

    /**
     * Adds keys belonging to containers to different containers' loot lists.
     */
    private static void addContainerKeysToContainers() {
        int containerIndex = 0;
        // Records which chests have had their key given out before i == index.
        List<Integer> addedInAdvance = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CHESTS + NUMBER_OF_CUPBOARDS; i++) {
            /*
             * Randomly decides which sort of container will hold the key, out of Chest or
             * ScrapPile
             */
            int rand = (int) (Math.random() * 10);
            if (rand > 5) {
                // Chest
                do {
                    // The index of the chest that gets the key.
                    containerIndex = (int) (Math.random() * NUMBER_OF_CHESTS);
                    // Prevents chest from holding its own key.
                } while (containerIndex == i || addedInAdvance.contains(containerIndex));
                // Adds the key to the chest.
                ch[containerIndex].getLoot().add(keys.get(i));
                /*
                 * Makes sure key to this chest is put inside a ScrapPile, if this chest's
                 * key has not yet been assigned. The intention is to make chest access
                 * less reliant on keys contained in chests.
                 */
                if (i < containerIndex) {
                    s[(int) (Math.random() * NUMBER_OF_S_PILES)].getLoot()
                            .add(keys.get(i));
                    addedInAdvance.add(containerIndex);
                }
            } else {
                // ScrapPile
                s[(int) (Math.random() * NUMBER_OF_S_PILES)].getLoot().add(keys.get(i));
                addedInAdvance.add(containerIndex);
            }
        }
    }

    public static void main(String[] args) {
        Game game = makeGame();
        XmlFunctions.saveInitialFile(game);
    }
}
