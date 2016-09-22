package server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.game.items.Antidote;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
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

/**
 * OK, this class will NOT be used in our final game. It is created to provide a tiny game
 * world for testing.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class TestConst {

    /*
     * world area id: 0, the room area id: 1
     */

    public static Area world;

    public static Map<Integer, Area> areas;

    static {

        areas = new HashMap<>();

        // ============ World ===============

        // first make the world map
        MapElement[][] worldBoard = new MapElement[7][8];
        world = new Area(worldBoard, 0);

        // ground squares (positions we can enter)
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 8; x++) {
                worldBoard[y][x] = new GroundSpace();
            }
        }

        // obstacles
        int[][] obstacleCoords = { { 3, 0 }, { 4, 0 }, { 5, 0 }, { 6, 0 }, { 7, 0 },
                { 1, 1 }, { 2, 1 }, { 3, 1 }, { 5, 1 }, { 6, 1 }, { 7, 1 }, { 5, 2 },
                { 6, 2 }, { 7, 2 }, { 1, 3 }, { 2, 3 }, { 4, 3 }, { 1, 4 }, { 2, 4 },
                { 4, 4 }, { 5, 4 }, { 6, 4 }, { 1, 5 }, { 1, 6 }, { 3, 6 }, { 4, 6 },
                { 7, 6 } };

        for (int[] coord : obstacleCoords) {
            int x = coord[0];
            int y = coord[1];
            worldBoard[y][x] = new Obstacle("obstacle");
        }

        // chest 1
        List<Item> loot1 = new ArrayList<>();
        loot1.add(new Antidote("A potion of antidote.", Virus.T_Virus));
        loot1.add(new Key("A key.", 456));
        worldBoard[0][2] = new Chest("chest 1", 123, false, loot1);

        // chest2
        List<Item> loot2 = new ArrayList<>();
        loot2.add(new Antidote("A potion of antidote.", Virus.T_Virus));
        loot2.add(new Key("A key.", 11111));
        loot2.add(new Key("A key.", 789));
        worldBoard[3][5] = new Chest("chest 2", 456, true, loot2);

        // chest3
        List<Item> loot3 = new ArrayList<>();
        loot3.add(new Antidote("A potion of antidote.", Virus.G_Virus));
        worldBoard[6][0] = new Chest("chest 3", 789, true, loot3);

        // transition space
        Position p1 = new Position(6, 3, 0, Direction.North);
        Position p2 = new Position(1, 2, 1, Direction.North);
        TransitionSpace tsWorld = new TransitionSpace(p1, p2);
        worldBoard[3][6] = tsWorld;

        // ============= ROOM ==================

        // room
        MapElement[][] roomBoard = new MapElement[3][3];

        // locked room
        // Room room = new Room(roomBoard, 1, 11111, true);

        // unlocked room
        Room room = new Room(roomBoard, 1, 11111, false);

        // ground squares (positions we can enter)
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                roomBoard[y][x] = new GroundSpace();
            }
        }

        // A pile of scrap
        List<Item> loot4 = new ArrayList<>();

        loot4.add(new Key("A key.", 999));
        roomBoard[1][1] = new ScrapPile("A pile of scrap", loot4);

        // cupboard
        List<Item> loot5 = new ArrayList<>();
        loot5.add(new Antidote("A potion of antidote.", Virus.G_Virus));
        loot5.add(new Antidote("A potion of antidote.", Virus.G_Virus));
        roomBoard[1][2] = new Cupboard("A cupboard", 999, true, loot5);

        // chest in room
        List<Item> lootInRoom = new ArrayList<>();
        lootInRoom.add(new Antidote("A potion of antidote.", Virus.G_Virus));
        lootInRoom.add(new Torch("A Torch."));
        roomBoard[0][2] = new Chest("chest in room", 123, false, lootInRoom);

        // transition space
        Position p3 = new Position(1, 2, 1, Direction.South);
        Position p4 = new Position(6, 3, 0, Direction.South);

        TransitionSpace tsRoom = new TransitionSpace(p3, p4);
        roomBoard[2][1] = tsRoom;

        // the Hashmap
        areas.put(0, world);
        areas.put(1, room);

        // NOTICE: VERY IMPORTANT, resister portals for each area in the game
        for (Area a : areas.values()) {
            a.registerPortals();
        }
    }

}
