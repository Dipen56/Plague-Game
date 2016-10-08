package tests.gameLogicTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import org.junit.Test;

import server.game.Game;
import server.game.TestConst;
import server.game.items.Antidote;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
import server.game.player.Avatar;
import server.game.player.Direction;
import server.game.player.Player;
import server.game.player.Position;
import server.game.player.Virus;

/**
 * These tests are related to players actions.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class PlayerActionTest {

    /**
     * a mock user id
     */
    private static final int MOCK_UID = 123;

    /**
     * This method tests whether the player can move forward, backward, left, and right
     * when they should be able to do so.
     */
    @Test
    public void validMove() {
        // mock a game world
        Game game = new Game(TestConst.world, TestConst.areas);
        // mock player
        Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
        game.joinPlayer(player);

        // ======== test move forward ========
        Position[] validMoveForwardPos = { new Position(0, 1, 0, Direction.North),
                new Position(0, 5, 0, Direction.North),
                new Position(4, 2, 0, Direction.North),
                new Position(3, 3, 0, Direction.North),
                new Position(2, 6, 0, Direction.North),
                new Position(5, 6, 0, Direction.North),
                new Position(7, 5, 0, Direction.North),
                new Position(0, 0, 0, Direction.East),
                new Position(0, 2, 0, Direction.East),
                new Position(3, 2, 0, Direction.East),
                new Position(2, 5, 0, Direction.East),
                new Position(4, 5, 0, Direction.East),
                new Position(5, 6, 0, Direction.East),
                new Position(6, 5, 0, Direction.East),
                new Position(6, 3, 0, Direction.East),
                new Position(0, 0, 0, Direction.South),
                new Position(0, 3, 0, Direction.South),
                new Position(4, 1, 0, Direction.South),
                new Position(3, 2, 0, Direction.South),
                new Position(3, 4, 0, Direction.South),
                new Position(2, 5, 0, Direction.South),
                new Position(5, 5, 0, Direction.South),
                new Position(6, 5, 0, Direction.South),
                new Position(7, 3, 0, Direction.South),
                new Position(1, 0, 0, Direction.West),
                new Position(1, 2, 0, Direction.West),
                new Position(4, 2, 0, Direction.West),
                new Position(3, 5, 0, Direction.West),
                new Position(5, 5, 0, Direction.West),
                new Position(6, 6, 0, Direction.West),
                new Position(7, 3, 0, Direction.West) };

        for (Position p : validMoveForwardPos) {
            player.setPosition(p);
            if (!game.playerMoveForward(MOCK_UID)) {
                fail("player should be able to move forward from (" + p.x + ", " + p.y
                        + ") when facing " + p.getDirection());
            }
        }

        // ======== test move backward ========
        Position[] validMoveBackwardPos = { new Position(0, 0, 0, Direction.North),
                new Position(0, 2, 0, Direction.North),
                new Position(0, 4, 0, Direction.North),
                new Position(4, 1, 0, Direction.North),
                new Position(3, 2, 0, Direction.North),
                new Position(2, 5, 0, Direction.North),
                new Position(6, 5, 0, Direction.North),
                new Position(7, 3, 0, Direction.North),
                new Position(1, 0, 0, Direction.East),
                new Position(1, 2, 0, Direction.East),
                new Position(4, 2, 0, Direction.East),
                new Position(3, 5, 0, Direction.East),
                new Position(6, 6, 0, Direction.East),
                new Position(7, 3, 0, Direction.East),
                new Position(0, 4, 0, Direction.South),
                new Position(4, 2, 0, Direction.South),
                new Position(2, 6, 0, Direction.South),
                new Position(6, 6, 0, Direction.South),
                new Position(7, 5, 0, Direction.South),
                new Position(0, 0, 0, Direction.West),
                new Position(1, 2, 0, Direction.West),
                new Position(3, 5, 0, Direction.West),
                new Position(5, 6, 0, Direction.West),
                new Position(6, 5, 0, Direction.West),
                new Position(6, 3, 0, Direction.West) };

        for (Position p : validMoveBackwardPos) {
            player.setPosition(p);
            if (!game.playerMoveBackward(MOCK_UID)) {
                fail("player should be able to move backward from (" + p.x + ", " + p.y
                        + ") when facing " + p.getDirection());
            }
        }

        // ======== test move left ========
        Position[] validMoveLeftPos = { new Position(1, 0, 0, Direction.North),
                new Position(1, 2, 0, Direction.North),
                new Position(3, 2, 0, Direction.North),
                new Position(4, 5, 0, Direction.North),
                new Position(6, 5, 0, Direction.North),
                new Position(6, 6, 0, Direction.North),
                new Position(7, 3, 0, Direction.North),
                new Position(0, 1, 0, Direction.East),
                new Position(3, 3, 0, Direction.East),
                new Position(0, 4, 0, Direction.East),
                new Position(4, 2, 0, Direction.East),
                new Position(2, 6, 0, Direction.East),
                new Position(5, 6, 0, Direction.East),
                new Position(7, 4, 0, Direction.East),
                new Position(7, 5, 0, Direction.East),
                new Position(0, 0, 0, Direction.South),
                new Position(0, 2, 0, Direction.South),
                new Position(2, 2, 0, Direction.South),
                new Position(3, 5, 0, Direction.South),
                new Position(5, 6, 0, Direction.South),
                new Position(6, 5, 0, Direction.South),
                new Position(6, 3, 0, Direction.South),
                new Position(0, 0, 0, Direction.West),
                new Position(3, 2, 0, Direction.West),
                new Position(3, 4, 0, Direction.West),
                new Position(2, 5, 0, Direction.West),
                new Position(6, 5, 0, Direction.West),
                new Position(7, 3, 0, Direction.West) };

        for (Position p : validMoveLeftPos) {
            player.setPosition(p);
            if (!game.playerMoveLeft(MOCK_UID)) {
                fail("player should be able to move left from (" + p.x + ", " + p.y
                        + ") when facing " + p.getDirection());
            }
        }

        // ======== test move right ========
        Position[] validMoveRightPos = { new Position(0, 0, 0, Direction.North),
                new Position(0, 2, 0, Direction.North),
                new Position(3, 2, 0, Direction.North),
                new Position(3, 5, 0, Direction.North),
                new Position(5, 5, 0, Direction.North),
                new Position(6, 3, 0, Direction.North),
                new Position(5, 6, 0, Direction.North),
                new Position(0, 0, 0, Direction.East),
                new Position(0, 4, 0, Direction.East),
                new Position(4, 1, 0, Direction.East),
                new Position(3, 4, 0, Direction.East),
                new Position(2, 5, 0, Direction.East),
                new Position(6, 5, 0, Direction.East),
                new Position(7, 4, 0, Direction.East),
                new Position(1, 0, 0, Direction.South),
                new Position(2, 2, 0, Direction.South),
                new Position(4, 2, 0, Direction.South),
                new Position(6, 6, 0, Direction.South),
                new Position(5, 5, 0, Direction.South),
                new Position(7, 3, 0, Direction.South),
                new Position(0, 2, 0, Direction.West),
                new Position(0, 5, 0, Direction.West),
                new Position(4, 2, 0, Direction.West),
                new Position(3, 5, 0, Direction.West),
                new Position(6, 6, 0, Direction.West),
                new Position(7, 4, 0, Direction.West) };

        for (Position p : validMoveRightPos) {
            player.setPosition(p);
            if (!game.playerMoveRight(MOCK_UID)) {
                fail("player should be able to move right from (" + p.x + ", " + p.y
                        + ") when facing " + p.getDirection());
            }
        }

    }

    /**
     * This method tests whether the player can move forward, backward, left, and right
     * when they should not be able to do so.
     */
    @Test
    public void invalidMove() {
        // mock a game world
        Game game = new Game(TestConst.world, TestConst.areas);
        // mock player
        Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
        game.joinPlayer(player);

        // ======== test move forward ========
        Position[] invalidMoveForwardPos = { new Position(1, 0, 0, Direction.North),
                new Position(1, 2, 0, Direction.North),
                new Position(3, 2, 0, Direction.North),
                new Position(4, 1, 0, Direction.North),
                new Position(2, 5, 0, Direction.North),
                new Position(5, 5, 0, Direction.North),
                new Position(7, 3, 0, Direction.North),
                new Position(6, 3, 0, Direction.North),
                new Position(1, 0, 0, Direction.East),
                new Position(0, 1, 0, Direction.East),
                new Position(0, 5, 0, Direction.East),
                new Position(4, 1, 0, Direction.East),
                new Position(4, 2, 0, Direction.East),
                new Position(3, 4, 0, Direction.East),
                new Position(2, 6, 0, Direction.East),
                new Position(6, 6, 0, Direction.East),
                new Position(7, 3, 0, Direction.East),
                new Position(7, 5, 0, Direction.East),
                new Position(1, 0, 0, Direction.South),
                new Position(0, 5, 0, Direction.South),
                new Position(2, 2, 0, Direction.South),
                new Position(4, 2, 0, Direction.South),
                new Position(2, 6, 0, Direction.South),
                new Position(4, 5, 0, Direction.South),
                new Position(6, 3, 0, Direction.South),
                new Position(7, 5, 0, Direction.South),
                new Position(0, 0, 0, Direction.West),
                new Position(0, 3, 0, Direction.West),
                new Position(4, 1, 0, Direction.West),
                new Position(3, 4, 0, Direction.West),
                new Position(2, 5, 0, Direction.West),
                new Position(5, 6, 0, Direction.West),
                new Position(5, 6, 0, Direction.West),
                new Position(6, 3, 0, Direction.West) };

        for (Position p : invalidMoveForwardPos) {
            player.setPosition(p);
            if (game.playerMoveForward(MOCK_UID)) {
                fail("player should not be able to move forward from (" + p.x + ", " + p.y
                        + ") when facing " + p.getDirection());
            }
        }

        // ======== test move backward ========
        Position[] invalidMoveBackwardPos = { new Position(1, 0, 0, Direction.North),
                new Position(0, 5, 0, Direction.North),
                new Position(2, 2, 0, Direction.North),
                new Position(4, 2, 0, Direction.North),
                new Position(2, 6, 0, Direction.North),
                new Position(7, 5, 0, Direction.North),
                new Position(6, 3, 0, Direction.North),
                new Position(4, 5, 0, Direction.North),
                new Position(0, 1, 0, Direction.East),
                new Position(0, 2, 0, Direction.East),
                new Position(4, 1, 0, Direction.East),
                new Position(3, 3, 0, Direction.East),
                new Position(2, 5, 0, Direction.East),
                new Position(7, 4, 0, Direction.East),
                new Position(6, 3, 0, Direction.East),
                new Position(5, 6, 0, Direction.East),
                new Position(0, 0, 0, Direction.South),
                new Position(4, 1, 0, Direction.South),
                new Position(2, 2, 0, Direction.South),
                new Position(2, 5, 0, Direction.South),
                new Position(5, 5, 0, Direction.South),
                new Position(6, 3, 0, Direction.South),
                new Position(7, 3, 0, Direction.South),
                new Position(0, 1, 0, Direction.West),
                new Position(0, 4, 0, Direction.West),
                new Position(4, 2, 0, Direction.West),
                new Position(3, 4, 0, Direction.West),
                new Position(2, 6, 0, Direction.West),
                new Position(7, 4, 0, Direction.West) };

        for (Position p : invalidMoveBackwardPos) {
            player.setPosition(p);
            if (game.playerMoveBackward(MOCK_UID)) {
                fail("player should not be able to move backward from (" + p.x + ", "
                        + p.y + ") when facing " + p.getDirection());
            }
        }

        // ======== test move left ========
        Position[] invalidMoveLeftPos = { new Position(0, 0, 0, Direction.North),
                new Position(0, 4, 0, Direction.North),
                new Position(4, 1, 0, Direction.North),
                new Position(3, 3, 0, Direction.North),
                new Position(2, 5, 0, Direction.North),
                new Position(2, 6, 0, Direction.North),
                new Position(5, 6, 0, Direction.North),
                new Position(7, 4, 0, Direction.North),
                new Position(6, 3, 0, Direction.North),
                new Position(1, 0, 0, Direction.East),
                new Position(1, 2, 0, Direction.East),
                new Position(4, 1, 0, Direction.East),
                new Position(2, 5, 0, Direction.East),
                new Position(5, 5, 0, Direction.East),
                new Position(6, 3, 0, Direction.East),
                new Position(7, 3, 0, Direction.East),
                new Position(1, 0, 0, Direction.South),
                new Position(0, 1, 0, Direction.South),
                new Position(0, 3, 0, Direction.South),
                new Position(4, 2, 0, Direction.South),
                new Position(3, 4, 0, Direction.South),
                new Position(2, 6, 0, Direction.South),
                new Position(6, 6, 0, Direction.South),
                new Position(7, 3, 0, Direction.South),
                new Position(1, 0, 0, Direction.West),
                new Position(0, 5, 0, Direction.West),
                new Position(4, 2, 0, Direction.West),
                new Position(4, 5, 0, Direction.West),
                new Position(5, 6, 0, Direction.West),
                new Position(6, 3, 0, Direction.West) };

        for (Position p : invalidMoveLeftPos) {
            player.setPosition(p);
            if (game.playerMoveLeft(MOCK_UID)) {
                fail("player should not be able to move left from (" + p.x + ", " + p.y
                        + ") when facing " + p.getDirection());
            }
        }

        // ======== test move right ========
        Position[] invalidMoveRightPos = { new Position(1, 0, 0, Direction.North),
                new Position(0, 1, 0, Direction.North),
                new Position(0, 5, 0, Direction.North),
                new Position(4, 2, 0, Direction.North),
                new Position(3, 4, 0, Direction.North),
                new Position(2, 6, 0, Direction.North),
                new Position(7, 5, 0, Direction.North),
                new Position(6, 6, 0, Direction.North),
                new Position(1, 0, 0, Direction.East),
                new Position(2, 2, 0, Direction.East),
                new Position(4, 2, 0, Direction.East),
                new Position(2, 6, 0, Direction.East),
                new Position(4, 5, 0, Direction.East),
                new Position(6, 3, 0, Direction.East),
                new Position(7, 5, 0, Direction.East),
                new Position(0, 0, 0, Direction.South),
                new Position(4, 1, 0, Direction.South),
                new Position(0, 2, 0, Direction.South),
                new Position(3, 4, 0, Direction.South),
                new Position(2, 5, 0, Direction.South),
                new Position(5, 6, 0, Direction.South),
                new Position(7, 4, 0, Direction.South),
                new Position(6, 3, 0, Direction.South),
                new Position(0, 0, 0, Direction.West),
                new Position(4, 1, 0, Direction.West),
                new Position(2, 5, 0, Direction.West),
                new Position(5, 5, 0, Direction.West),
                new Position(6, 3, 0, Direction.West) };

        for (Position p : invalidMoveRightPos) {
            player.setPosition(p);
            if (game.playerMoveRight(MOCK_UID)) {
                fail("player should not be able to move right from (" + p.x + ", " + p.y
                        + ") when facing " + p.getDirection());
            }
        }

    }

    /**
     * This method tests whether the player can turn left or right when they should be
     * able to do so.
     */
    @Test
    public void validTurn() {
        /*
         * when the player join in game, his direction is random, so we do multiple rounds
         * of test to increase confidence level.
         */
        for (int i = 0; i < 100; i++) {

            // mock a game world
            Game game = new Game(TestConst.world, TestConst.areas);
            // mock player
            Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
            game.joinPlayer(player);

            // ======== test turn left ========
            Position currentPos = player.getPosition();
            Direction currentDir = currentPos.getDirection();
            game.playerTurnLeft(MOCK_UID);
            Direction newDir = player.getPosition().getDirection();
            if (newDir != currentDir.left()) {
                fail("player should be able to turn left.");
            }

            // ======== test turn right ========
            currentPos = player.getPosition();
            currentDir = currentPos.getDirection();
            game.playerTurnRight(MOCK_UID);
            newDir = player.getPosition().getDirection();
            if (newDir != currentDir.right()) {
                fail("player should be able to turn right.");
            }
        }
    }

    /**
     * This method tests whether the player can be blocked when he try to move into a
     * position that is occupied by another player.
     */
    @Test
    public void blockBetweenPlayers() {
        // mock a game world
        Game game = new Game(TestConst.world, TestConst.areas);
        // mock player
        Player player_1 = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
        Player player_2 = new Player(MOCK_UID + 1, Avatar.Avatar_2, "AnotherPlayer");
        game.joinPlayer(player_1);
        game.joinPlayer(player_2);
        player_2.setPosition(new Position(3, 2, 0, Direction.North));

        // ======== block in front ========
        player_1.setPosition(new Position(3, 3, 0, Direction.North));
        if (game.playerMoveForward(MOCK_UID)) {
            fail("player should not be able to move forward, it's blocked by another player.");
        }

        // ======== block on left ========
        player_1.setPosition(new Position(4, 2, 0, Direction.North));
        if (game.playerMoveLeft(MOCK_UID)) {
            fail("player should not be able to move left, it's blocked by another player.");
        }

        // ======== block on right ========
        player_1.setPosition(new Position(2, 2, 0, Direction.North));
        if (game.playerMoveRight(MOCK_UID)) {
            fail("player should not be able to move right, it's blocked by another player.");
        }

        // ======== block on behind ========
        player_2.setPosition(new Position(3, 5, 0, Direction.North));
        player_1.setPosition(new Position(3, 4, 0, Direction.North));
        if (game.playerMoveBackward(MOCK_UID)) {
            fail("player should not be able to move backward, it's blocked by another player.");
        }

        // ======== block at entrance of rooms ========
        player_2.setPosition(new Position(1, 2, 1, Direction.North));
        player_1.setPosition(new Position(6, 3, 0, Direction.North));
        if (game.playerTransit(MOCK_UID)) {
            fail("player should not be able to enter the room, the entrance is blocked by another player.");
        }
    }

    /**
     * This method tests whether the player can move between areas when they should be
     * able to do so.
     */
    @Test
    public void validTransit() {
        // mock a game world
        Game game = new Game(TestConst.world, TestConst.areas);
        // mock player
        Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
        game.joinPlayer(player);

        // ======== test transit from world to room ========
        player.setPosition(new Position(6, 3, 0, Direction.North));
        if (!game.playerTransit(MOCK_UID)) {
            fail("player should be able to enter the room.");
        }

        // ======== test transit from room to world ========
        player.setPosition(new Position(1, 2, 1, Direction.South));
        if (!game.playerTransit(MOCK_UID)) {
            fail("player should be able to exit the room.");
        }
    }

    /**
     * This method tests whether the player can move between areas when they should be
     * able to do so.
     */
    @Test
    public void invalidTransit() {
        // mock a game world
        Game game = new Game(TestConst.world, TestConst.areas);
        // mock player
        Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
        game.joinPlayer(player);

        // ======== test invalid transit from world to room ========
        Position[] invalidPos_1 = { new Position(6, 3, 0, Direction.South),
                new Position(6, 3, 0, Direction.East),
                new Position(6, 3, 0, Direction.West),
                new Position(3, 3, 0, Direction.North),
                new Position(6, 6, 0, Direction.North) };

        for (Position p : invalidPos_1) {
            player.setPosition(p);
            if (game.playerTransit(MOCK_UID)) {
                fail("player should not be able to enter the room from: " + p.toString());
            }
        }

        // ======== test invalid transit from room to world ========
        Position[] invalidPos_2 = { new Position(1, 2, 1, Direction.North),
                new Position(1, 2, 1, Direction.East),
                new Position(1, 2, 1, Direction.West),
                new Position(0, 0, 1, Direction.South),
                new Position(2, 2, 1, Direction.South) };

        for (Position p : invalidPos_2) {
            player.setPosition(p);
            if (game.playerTransit(MOCK_UID)) {
                fail("player should not be able to enter the room from: " + p.toString());
            }
        }
    }

    /**
     * This method tests whether the player can unlock a lockable object when they should
     * be able to do so.
     */
    @Test
    public void validUnlock() {
        // mock a game world
        Game game = new Game(TestConst.world, TestConst.areas);
        // mock player
        Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
        game.joinPlayer(player);

        // ====== chest 2 ======
        player.setPosition(new Position(6, 3, 0, Direction.West));
        player.pickUpItem(new Key("A key.", 456));
        if (!game.playerUnlockLockable(MOCK_UID)) {
            fail("player should be able to unlock the chest");
        }

        // ====== chest 3 ======
        player.setPosition(new Position(0, 5, 0, Direction.South));
        player.pickUpItem(new Key("A key.", 789));
        if (!game.playerUnlockLockable(MOCK_UID)) {
            fail("player should be able to unlock the chest");
        }

    }

    /**
     * This method tests whether the player can unlock a lockable object when they should
     * not be able to do so.
     */
    @Test
    public void invalidUnlock() {
        // mock a game world
        Game game = new Game(TestConst.world, TestConst.areas);
        // mock player
        Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
        game.joinPlayer(player);

        // ====== invalid unlock on chest 2 ======
        player.setPosition(new Position(5, 3, 0, Direction.West));
        player.pickUpItem(new Key("A key.", 457));
        if (game.playerUnlockLockable(MOCK_UID)) {
            fail("player should not be able to unlock the chest because the key is incorrect");
        }

        player.setPosition(new Position(5, 3, 0, Direction.East));
        player.pickUpItem(new Key("A key.", 456));
        if (game.playerUnlockLockable(MOCK_UID)) {
            fail("player should not be able to unlock the chest because he is facing wrong direction");
        }

        // ====== invalid unlock on chest 3 ======
        player.setPosition(new Position(0, 5, 0, Direction.South));
        player.pickUpItem(new Key("A key.", 777));
        if (game.playerUnlockLockable(MOCK_UID)) {
            fail("player should not be able to unlock the chest because the key is incorrect");
        }

        player.setPosition(new Position(0, 5, 0, Direction.North));
        player.pickUpItem(new Key("A key.", 789));
        if (game.playerUnlockLockable(MOCK_UID)) {
            fail("player should not be able to unlock the chest because he is facing wrong direction");
        }
    }

    /**
     * This method tests whether the player can take items from a container when they
     * should be able to do so.
     */
    @Test
    public void validTakeItems() {
        // mock a game world
        Game game = new Game(TestConst.world, TestConst.areas);
        // mock player
        Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
        game.joinPlayer(player);

        // ====== take items from chest 1 ======
        player.setPosition(new Position(1, 0, 0, Direction.East));
        if (!game.playerTakeItemsFromContainer(MOCK_UID)) {
            fail("player should be able to take items from the chest 1");
        }

        List<Item> inventory = player.getInventory();
        if (!inventory.contains(new Antidote("A potion of antidote.", Virus.Black_Death))
                || !inventory.contains(new Key("A key.", 456))) {
            fail("Should have taken out an antidote and a key");
        }

        // ====== take items from chest 2 ======
        player.setPosition(new Position(6, 3, 0, Direction.West));
        if (!game.playerUnlockLockable(MOCK_UID)) {
            fail("player should be able to unlock the chest");
        }
        if (!game.playerTakeItemsFromContainer(MOCK_UID)) {
            fail("player should be able to take items from the chest 2");
        }

        inventory = player.getInventory();
        if (!inventory.contains(new Key("A key.", 11111))
                || !inventory.contains(new Key("A key.", 789))) {
            fail("Should have taken out another two keys");
        }

        // ====== take items from chest 3 ======
        player.setPosition(new Position(0, 5, 0, Direction.South));
        if (!game.playerUnlockLockable(MOCK_UID)) {
            fail("player should be able to unlock the chest");
        }
        if (!game.playerTakeItemsFromContainer(MOCK_UID)) {
            fail("player should be able to take items from the chest 3");
        }

        inventory = player.getInventory();
        if (!inventory
                .contains(new Antidote("A potion of antidote.", Virus.Spanish_Flu))) {
            fail("Should have taken out another antidote");
        }

    }

    /**
     * This method tests whether the player can take items from a container when they
     * should not be able to do so.
     */
    @Test
    public void invalidTakeItems() {
        // mock a game world
        Game game = new Game(TestConst.world, TestConst.areas);
        // mock player
        Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
        game.joinPlayer(player);

        // ====== invalid take at chest 1 ======
        player.setPosition(new Position(1, 0, 0, Direction.West));
        if (game.playerTakeItemsFromContainer(MOCK_UID)) {
            fail("player should not be able to take items from the chest 1, direction is wrong");
        }

        player.setPosition(new Position(0, 0, 0, Direction.East));
        if (game.playerTakeItemsFromContainer(MOCK_UID)) {
            fail("player should not be able to take items from the chest 1, the chest is not in front");
        }

        // take items out for testing chest 2
        player.setPosition(new Position(1, 0, 0, Direction.East));
        game.playerTakeItemsFromContainer(MOCK_UID);

        // ====== invalid take at chest 2 ======
        player.setPosition(new Position(6, 3, 0, Direction.West));
        if (game.playerTakeItemsFromContainer(MOCK_UID)) {
            fail("player should not be able to take items from chest 2, chest locked.");
        }

        // take items out for testing inventory full, now we have 4 items.
        game.playerUnlockLockable(MOCK_UID);
        game.playerTakeItemsFromContainer(MOCK_UID);
        List<Item> inventory = player.getInventory();

        while (inventory.size() < Player.INVENTORY_SIZE) {
            player.pickUpItem(new Torch("a torch"));
        }

        // ==== now the player's inventory is full, let's test chest 3 ====
        player.setPosition(new Position(0, 5, 0, Direction.South));
        game.playerUnlockLockable(MOCK_UID);
        // unlocking chest consumes a key, so keep the inventory full.
        player.pickUpItem(new Torch("a torch"));
        if (game.playerTakeItemsFromContainer(MOCK_UID)) {
            fail("player should not be able to take items from the chest 3, inventory full");
        }

    }

    /**
     * This method tests whether the player can use antidote, and the effect of antidote.
     */
    @Test
    public void validUseAntidote() {
        // mock a game world
        Game game = new Game(TestConst.world, TestConst.areas);
        // mock player
        Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
        game.joinPlayer(player);

        // use antidote
        Virus playerVirus = player.getVirus();
        player.pickUpItem(new Antidote("antidote", playerVirus));
        if (!game.playerUseItem(MOCK_UID, 0)) {
            fail("player should be able to use antidote at index 0");
        }
        assertEquals("player's health should be increased", player.getHealthLeft(),
                Player.MAX_HEALTH + Antidote.EFFECT);

        // try antidote with other virus serveral times more
        for (int i = 0; i < 10; i++) {
            // mock a game world
            game = new Game(TestConst.world, TestConst.areas);
            // mock player
            player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
            game.joinPlayer(player);

            playerVirus = player.getVirus();
            Virus otherVirus = Virus.randomVirus();
            while (otherVirus == playerVirus) {
                otherVirus = Virus.randomVirus();
            }
            player.pickUpItem(new Antidote("antidote", otherVirus));
            if (!game.playerUseItem(MOCK_UID, 0)) {
                fail("player should be able to use antidote at index 0");
            }
            if (player.getHealthLeft() != Player.MAX_HEALTH + Antidote.EFFECT * 2
                    && player.getHealthLeft() != Player.MAX_HEALTH - Antidote.EFFECT) {
                fail("player's health should either be increased double amount of effect, or decresed single amount of effect");
            }
        }
    }

    /**
     * This method tests player's visibility.
     */
    @Test
    public void playerVisibility() {
        for (int i = 0; i < 50; i++) {
            // mock a game world
            Game game = new Game(TestConst.world, TestConst.areas);
            // mock player
            Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
            game.joinPlayer(player);

            int hour = game.getClock().getHour();

            if (hour >= 6 && hour < 18) {
                // it's day time
                assertEquals("visibility is wrong", game.getPlayerVisibility(MOCK_UID),
                        Game.DAY_VISIBLIITY);
            } else {
                // it's night time
                assertEquals("visibility is wrong", game.getPlayerVisibility(MOCK_UID),
                        Game.NIGHT_VISIBILITY);

                // light up a torch
                player.pickUpItem(new Torch("a torch"));
                game.playerUseItem(MOCK_UID, 0);
                assertEquals("visibility is wrong", game.getPlayerVisibility(MOCK_UID),
                        Game.TORCH_VISIBILITY);
            }
        }
    }
}