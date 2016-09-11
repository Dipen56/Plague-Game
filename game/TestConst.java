package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.items.Antidote;
import game.items.Item;
import game.items.Key;
import game.items.Torch;
import game.player.Direction;
import game.player.Virus;
import game.world.Chest;
import game.world.GroundSquare;
import game.world.Obstacle;
import game.world.Position;
import game.world.Room;
import game.world.RoomEntrance;
import game.world.RoomExit;
import game.world.World;

/**
 * OK, this class will NOT be used in our final game. It is created to provide a tiny game
 * world for testing.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class TestConst {

    public static World world;

    public static Map<Room, RoomEntrance> entrances;

    static {

        entrances = new HashMap<>();

        // first make the world map
        Position[][] worldBoard = new Position[7][8];
        world = new World(worldBoard);

        // ground squares (positions we can enter)
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 8; x++) {
                worldBoard[y][x] = new GroundSquare(x, y, world);
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
            worldBoard[y][x] = new Obstacle(x, y, "obstacle", null);
        }

        // chest 1
        List<Item> loot1 = new ArrayList<>();
        loot1.add(new Antidote("A potion of antidote.", null, Virus.T_Virus));
        loot1.add(new Key("A key.", null, 456));
        worldBoard[0][2] = new Chest(2, 0, "chest 1", null, 123, false, loot1);

        // chest2
        List<Item> loot2 = new ArrayList<>();
        loot2.add(new Antidote("A potion of antidote.", null, Virus.T_Virus));
        loot2.add(new Key("A key.", null, 11111));
        loot2.add(new Key("A key.", null, 789));
        worldBoard[3][5] = new Chest(5, 3, "chest 2", null, 456, true, loot2);

        // chest3
        List<Item> loot3 = new ArrayList<>();
        loot3.add(new Antidote("A potion of antidote.", null, Virus.G_Virus));
        worldBoard[6][0] = new Chest(0, 6, "chest 3", null, 789, true, loot3);

        // ===============================

        // room
        Position[][] roomBoard = new Position[3][3];
        Room room = new Room(roomBoard, 11111, true);

        // ground squares (positions we can enter)
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                roomBoard[y][x] = new GroundSquare(x, y, room);
            }
        }

        // obstacles
        roomBoard[1][1] = new Obstacle(1, 1, "obstacle", null);
        roomBoard[1][2] = new Obstacle(2, 1, "Table", null);

        // chest in room
        List<Item> lootInRoom = new ArrayList<>();
        lootInRoom.add(new Antidote("A potion of antidote.", null, Virus.G_Virus));
        lootInRoom.add(new Torch("A Torch.", null));
        roomBoard[0][2] = new Chest(2, 0, "chest in room", null, 123, false, lootInRoom);

        // room entrance
        // this has to be remembered by the room
        RoomEntrance entrance = new RoomEntrance(6, 3, world, room, Direction.North);
        worldBoard[3][6] = entrance;

        // room eixt
        roomBoard[2][1] = new RoomExit(1, 2, room, entrance, Direction.South);

        // let the room remember exit
        room.rememberEixt();

        // resister portals
        world.registerPortals();
        room.registerPortals();

        // remember entrances.
        entrances.put(room, entrance);

    }

}
