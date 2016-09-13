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
 * OK, this class will NOT be used in our final game. It is created to provide a tiny game
 * world for testing.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class TestConst {

    public static World world;

    public static Map<Room, TransitionSpace> entrances;

    static {

        entrances = new HashMap<>();

        // first make the world map
        MapElement[][] worldBoard = new MapElement[7][8];
        world = new World(worldBoard);

        // ground squares (positions we can enter)
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 8; x++) {
                worldBoard[y][x] = new MapElement();
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
            worldBoard[y][x] = new Obstacle(x, y, "obstacle");
        }

        // chest 1
        List<Item> loot1 = new ArrayList<>();
        loot1.add(new Antidote("A potion of antidote.", Virus.T_Virus));
        loot1.add(new Key("A key.", 456));
        worldBoard[0][2] = new Chest(2, 0, "chest 1", 123, false, loot1);

        // chest2
        List<Item> loot2 = new ArrayList<>();
        loot2.add(new Antidote("A potion of antidote.", Virus.T_Virus));
        loot2.add(new Key("A key.", 11111));
        loot2.add(new Key("A key.", 789));
        worldBoard[3][5] = new Chest(5, 3, "chest 2", 456, true, loot2);

        // chest3
        List<Item> loot3 = new ArrayList<>();
        loot3.add(new Antidote("A potion of antidote.", Virus.G_Virus));
        worldBoard[6][0] = new Chest(0, 6, "chest 3", 789, true, loot3);

        // ===============================

        // room
        MapElement[][] roomBoard = new MapElement[3][3];
        Room room = new Room(roomBoard, 11111, true);

        // ground squares (positions we can enter)
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                roomBoard[y][x] = new MapElement();
            }
        }

        // obstacles
        roomBoard[1][1] = new Obstacle(1, 1, "obstacle");
        roomBoard[1][2] = new Obstacle(2, 1, "Table");

        // chest in room
        List<Item> lootInRoom = new ArrayList<>();
        lootInRoom.add(new Antidote("A potion of antidote.", Virus.G_Virus));
        lootInRoom.add(new Torch("A Torch."));
        roomBoard[0][2] = new Chest(2, 0, "chest in room", 123, false, lootInRoom);

        // room entrance
        // this has to be remembered by the room
        TransitionSpace entrance = new TransitionSpace(6, 3, world, new Position(6,3), room, Direction.North);
        worldBoard[3][6] = entrance;

        // room exit
        roomBoard[2][1] = new TransitionSpace(1, 2, room, new Position(1,2), world, Direction.South);


        // let the room remember exit
        room.rememberExit();

        // resister portals
        world.registerPortals();
        room.registerPortals();

        // remember entrances.
        entrances.put(room, entrance);

    }

}
