package tests.gameLogicTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import server.game.Game;
import server.game.TestConst;
import server.game.items.Antidote;
import server.game.items.Bag;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
import server.game.player.Avatar;
import server.game.player.Direction;
import server.game.player.Player;
import server.game.player.Position;
import server.game.player.Virus;
import server.game.world.Area;
import server.game.world.Chest;
import server.game.world.MapElement;

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
	 * This method tests whether the player can move forward, backward, left,
	 * and right when they should be able to do so.
	 */
	@Test
	public void validMove() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

		// mock player
		Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
		game.joinPlayer(player);

		// ======== test move forward ========
		Position[] validMoveForwardPos = { new Position(0, 1, 0, Direction.North),
				new Position(0, 5, 0, Direction.North), new Position(4, 2, 0, Direction.North),
				new Position(3, 3, 0, Direction.North), new Position(2, 6, 0, Direction.North),
				new Position(5, 6, 0, Direction.North), new Position(7, 5, 0, Direction.North),
				new Position(0, 0, 0, Direction.East), new Position(0, 2, 0, Direction.East),
				new Position(3, 2, 0, Direction.East), new Position(2, 5, 0, Direction.East),
				new Position(4, 5, 0, Direction.East), new Position(5, 6, 0, Direction.East),
				new Position(6, 5, 0, Direction.East), new Position(6, 3, 0, Direction.East),
				new Position(0, 0, 0, Direction.South), new Position(0, 3, 0, Direction.South),
				new Position(4, 1, 0, Direction.South), new Position(3, 2, 0, Direction.South),
				new Position(3, 4, 0, Direction.South), new Position(2, 5, 0, Direction.South),
				new Position(5, 5, 0, Direction.South), new Position(6, 5, 0, Direction.South),
				new Position(7, 3, 0, Direction.South), new Position(1, 0, 0, Direction.West),
				new Position(1, 2, 0, Direction.West), new Position(4, 2, 0, Direction.West),
				new Position(3, 5, 0, Direction.West), new Position(5, 5, 0, Direction.West),
				new Position(6, 6, 0, Direction.West), new Position(7, 3, 0, Direction.West) };

		for (Position p : validMoveForwardPos) {
			player.setPosition(p);
			if (!game.playerMoveForward(MOCK_UID)) {
				fail("player should be able to move forward from (" + p.x + ", " + p.y + ") when facing "
						+ p.getDirection());
			}
		}

		// ======== test move backward ========
		Position[] validMoveBackwardPos = { new Position(0, 0, 0, Direction.North),
				new Position(0, 2, 0, Direction.North), new Position(0, 4, 0, Direction.North),
				new Position(4, 1, 0, Direction.North), new Position(3, 2, 0, Direction.North),
				new Position(2, 5, 0, Direction.North), new Position(6, 5, 0, Direction.North),
				new Position(7, 3, 0, Direction.North), new Position(1, 0, 0, Direction.East),
				new Position(1, 2, 0, Direction.East), new Position(4, 2, 0, Direction.East),
				new Position(3, 5, 0, Direction.East), new Position(6, 6, 0, Direction.East),
				new Position(7, 3, 0, Direction.East), new Position(0, 4, 0, Direction.South),
				new Position(4, 2, 0, Direction.South), new Position(2, 6, 0, Direction.South),
				new Position(6, 6, 0, Direction.South), new Position(7, 5, 0, Direction.South),
				new Position(0, 0, 0, Direction.West), new Position(1, 2, 0, Direction.West),
				new Position(3, 5, 0, Direction.West), new Position(5, 6, 0, Direction.West),
				new Position(6, 5, 0, Direction.West), new Position(6, 3, 0, Direction.West) };

		for (Position p : validMoveBackwardPos) {
			player.setPosition(p);
			if (!game.playerMoveBackward(MOCK_UID)) {
				fail("player should be able to move backward from (" + p.x + ", " + p.y + ") when facing "
						+ p.getDirection());
			}
		}

		// ======== test move left ========
		Position[] validMoveLeftPos = { new Position(1, 0, 0, Direction.North), new Position(1, 2, 0, Direction.North),
				new Position(3, 2, 0, Direction.North), new Position(4, 5, 0, Direction.North),
				new Position(6, 5, 0, Direction.North), new Position(6, 6, 0, Direction.North),
				new Position(7, 3, 0, Direction.North), new Position(0, 1, 0, Direction.East),
				new Position(3, 3, 0, Direction.East), new Position(0, 4, 0, Direction.East),
				new Position(4, 2, 0, Direction.East), new Position(2, 6, 0, Direction.East),
				new Position(5, 6, 0, Direction.East), new Position(7, 4, 0, Direction.East),
				new Position(7, 5, 0, Direction.East), new Position(0, 0, 0, Direction.South),
				new Position(0, 2, 0, Direction.South), new Position(2, 2, 0, Direction.South),
				new Position(3, 5, 0, Direction.South), new Position(5, 6, 0, Direction.South),
				new Position(6, 5, 0, Direction.South), new Position(6, 3, 0, Direction.South),
				new Position(0, 0, 0, Direction.West), new Position(3, 2, 0, Direction.West),
				new Position(3, 4, 0, Direction.West), new Position(2, 5, 0, Direction.West),
				new Position(6, 5, 0, Direction.West), new Position(7, 3, 0, Direction.West) };

		for (Position p : validMoveLeftPos) {
			player.setPosition(p);
			if (!game.playerMoveLeft(MOCK_UID)) {
				fail("player should be able to move left from (" + p.x + ", " + p.y + ") when facing "
						+ p.getDirection());
			}
		}

		// ======== test move right ========
		Position[] validMoveRightPos = { new Position(0, 0, 0, Direction.North), new Position(0, 2, 0, Direction.North),
				new Position(3, 2, 0, Direction.North), new Position(3, 5, 0, Direction.North),
				new Position(5, 5, 0, Direction.North), new Position(6, 3, 0, Direction.North),
				new Position(5, 6, 0, Direction.North), new Position(0, 0, 0, Direction.East),
				new Position(0, 4, 0, Direction.East), new Position(4, 1, 0, Direction.East),
				new Position(3, 4, 0, Direction.East), new Position(2, 5, 0, Direction.East),
				new Position(6, 5, 0, Direction.East), new Position(7, 4, 0, Direction.East),
				new Position(1, 0, 0, Direction.South), new Position(2, 2, 0, Direction.South),
				new Position(4, 2, 0, Direction.South), new Position(6, 6, 0, Direction.South),
				new Position(5, 5, 0, Direction.South), new Position(7, 3, 0, Direction.South),
				new Position(0, 2, 0, Direction.West), new Position(0, 5, 0, Direction.West),
				new Position(4, 2, 0, Direction.West), new Position(3, 5, 0, Direction.West),
				new Position(6, 6, 0, Direction.West), new Position(7, 4, 0, Direction.West) };

		for (Position p : validMoveRightPos) {
			player.setPosition(p);
			if (!game.playerMoveRight(MOCK_UID)) {
				fail("player should be able to move right from (" + p.x + ", " + p.y + ") when facing "
						+ p.getDirection());
			}
		}

	}

	/**
	 * This method tests whether the player can move forward, backward, left,
	 * and right when they should not be able to do so.
	 */
	@Test
	public void invalidMove() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

		// mock player
		Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
		game.joinPlayer(player);

		// ======== test move forward ========
		Position[] invalidMoveForwardPos = { new Position(1, 0, 0, Direction.North),
				new Position(1, 2, 0, Direction.North), new Position(3, 2, 0, Direction.North),
				new Position(4, 1, 0, Direction.North), new Position(2, 5, 0, Direction.North),
				new Position(5, 5, 0, Direction.North), new Position(7, 3, 0, Direction.North),
				new Position(6, 3, 0, Direction.North), new Position(1, 0, 0, Direction.East),
				new Position(0, 1, 0, Direction.East), new Position(0, 5, 0, Direction.East),
				new Position(4, 1, 0, Direction.East), new Position(4, 2, 0, Direction.East),
				new Position(3, 4, 0, Direction.East), new Position(2, 6, 0, Direction.East),
				new Position(6, 6, 0, Direction.East), new Position(7, 3, 0, Direction.East),
				new Position(7, 5, 0, Direction.East), new Position(1, 0, 0, Direction.South),
				new Position(0, 5, 0, Direction.South), new Position(2, 2, 0, Direction.South),
				new Position(4, 2, 0, Direction.South), new Position(2, 6, 0, Direction.South),
				new Position(4, 5, 0, Direction.South), new Position(6, 3, 0, Direction.South),
				new Position(7, 5, 0, Direction.South), new Position(0, 0, 0, Direction.West),
				new Position(0, 3, 0, Direction.West), new Position(4, 1, 0, Direction.West),
				new Position(3, 4, 0, Direction.West), new Position(2, 5, 0, Direction.West),
				new Position(5, 6, 0, Direction.West), new Position(5, 6, 0, Direction.West),
				new Position(6, 3, 0, Direction.West) };

		for (Position p : invalidMoveForwardPos) {
			player.setPosition(p);
			if (game.playerMoveForward(MOCK_UID)) {
				fail("player should not be able to move forward from (" + p.x + ", " + p.y + ") when facing "
						+ p.getDirection());
			}
		}

		// ======== test move backward ========
		Position[] invalidMoveBackwardPos = { new Position(1, 0, 0, Direction.North),
				new Position(0, 5, 0, Direction.North), new Position(2, 2, 0, Direction.North),
				new Position(4, 2, 0, Direction.North), new Position(2, 6, 0, Direction.North),
				new Position(7, 5, 0, Direction.North), new Position(6, 3, 0, Direction.North),
				new Position(4, 5, 0, Direction.North), new Position(0, 1, 0, Direction.East),
				new Position(0, 2, 0, Direction.East), new Position(4, 1, 0, Direction.East),
				new Position(3, 3, 0, Direction.East), new Position(2, 5, 0, Direction.East),
				new Position(7, 4, 0, Direction.East), new Position(6, 3, 0, Direction.East),
				new Position(5, 6, 0, Direction.East), new Position(0, 0, 0, Direction.South),
				new Position(4, 1, 0, Direction.South), new Position(2, 2, 0, Direction.South),
				new Position(2, 5, 0, Direction.South), new Position(5, 5, 0, Direction.South),
				new Position(6, 3, 0, Direction.South), new Position(7, 3, 0, Direction.South),
				new Position(0, 1, 0, Direction.West), new Position(0, 4, 0, Direction.West),
				new Position(4, 2, 0, Direction.West), new Position(3, 4, 0, Direction.West),
				new Position(2, 6, 0, Direction.West), new Position(7, 4, 0, Direction.West) };

		for (Position p : invalidMoveBackwardPos) {
			player.setPosition(p);
			if (game.playerMoveBackward(MOCK_UID)) {
				fail("player should not be able to move backward from (" + p.x + ", " + p.y + ") when facing "
						+ p.getDirection());
			}
		}

		// ======== test move left ========
		Position[] invalidMoveLeftPos = { new Position(0, 0, 0, Direction.North),
				new Position(0, 4, 0, Direction.North), new Position(4, 1, 0, Direction.North),
				new Position(3, 3, 0, Direction.North), new Position(2, 5, 0, Direction.North),
				new Position(2, 6, 0, Direction.North), new Position(5, 6, 0, Direction.North),
				new Position(7, 4, 0, Direction.North), new Position(6, 3, 0, Direction.North),
				new Position(1, 0, 0, Direction.East), new Position(1, 2, 0, Direction.East),
				new Position(4, 1, 0, Direction.East), new Position(2, 5, 0, Direction.East),
				new Position(5, 5, 0, Direction.East), new Position(6, 3, 0, Direction.East),
				new Position(7, 3, 0, Direction.East), new Position(1, 0, 0, Direction.South),
				new Position(0, 1, 0, Direction.South), new Position(0, 3, 0, Direction.South),
				new Position(4, 2, 0, Direction.South), new Position(3, 4, 0, Direction.South),
				new Position(2, 6, 0, Direction.South), new Position(6, 6, 0, Direction.South),
				new Position(7, 3, 0, Direction.South), new Position(1, 0, 0, Direction.West),
				new Position(0, 5, 0, Direction.West), new Position(4, 2, 0, Direction.West),
				new Position(4, 5, 0, Direction.West), new Position(5, 6, 0, Direction.West),
				new Position(6, 3, 0, Direction.West) };

		for (Position p : invalidMoveLeftPos) {
			player.setPosition(p);
			if (game.playerMoveLeft(MOCK_UID)) {
				fail("player should not be able to move left from (" + p.x + ", " + p.y + ") when facing "
						+ p.getDirection());
			}
		}

		// ======== test move right ========
		Position[] invalidMoveRightPos = { new Position(1, 0, 0, Direction.North),
				new Position(0, 1, 0, Direction.North), new Position(0, 5, 0, Direction.North),
				new Position(4, 2, 0, Direction.North), new Position(3, 4, 0, Direction.North),
				new Position(2, 6, 0, Direction.North), new Position(7, 5, 0, Direction.North),
				new Position(6, 6, 0, Direction.North), new Position(1, 0, 0, Direction.East),
				new Position(2, 2, 0, Direction.East), new Position(4, 2, 0, Direction.East),
				new Position(2, 6, 0, Direction.East), new Position(4, 5, 0, Direction.East),
				new Position(6, 3, 0, Direction.East), new Position(7, 5, 0, Direction.East),
				new Position(0, 0, 0, Direction.South), new Position(4, 1, 0, Direction.South),
				new Position(0, 2, 0, Direction.South), new Position(3, 4, 0, Direction.South),
				new Position(2, 5, 0, Direction.South), new Position(5, 6, 0, Direction.South),
				new Position(7, 4, 0, Direction.South), new Position(6, 3, 0, Direction.South),
				new Position(0, 0, 0, Direction.West), new Position(4, 1, 0, Direction.West),
				new Position(2, 5, 0, Direction.West), new Position(5, 5, 0, Direction.West),
				new Position(6, 3, 0, Direction.West) };

		for (Position p : invalidMoveRightPos) {
			player.setPosition(p);
			if (game.playerMoveRight(MOCK_UID)) {
				fail("player should not be able to move right from (" + p.x + ", " + p.y + ") when facing "
						+ p.getDirection());
			}
		}

	}

	/**
	 * This method tests whether the player can turn left or right when they
	 * should be able to do so.
	 */
	@Test
	public void validTurn() {
		/*
		 * when the player join in game, his direction is random, so we do
		 * multiple rounds of test to increase confidence level.
		 */
		for (int i = 0; i < 100; i++) {

			// mock a game world
			Map<Integer, Area> areas = TestConst.createAreas();
			Area world = areas.get(0);
			Game game = new Game(world, areas);

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
	 * This method tests whether the player can be blocked when he try to move
	 * into a position that is occupied by another player.
	 */
	@Test
	public void blockBetweenPlayers() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

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
	 * This method tests whether the player can move between areas when they
	 * should be able to do so.
	 */
	@Test
	public void validTransit() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

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
	 * This method tests whether the player can move between areas when they
	 * should be able to do so.
	 */
	@Test
	public void invalidTransit() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

		// mock player
		Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
		game.joinPlayer(player);

		// ======== test invalid transit from world to room ========
		Position[] invalidPos_1 = { new Position(6, 3, 0, Direction.South), new Position(6, 3, 0, Direction.East),
				new Position(6, 3, 0, Direction.West), new Position(3, 3, 0, Direction.North),
				new Position(6, 6, 0, Direction.North) };

		for (Position p : invalidPos_1) {
			player.setPosition(p);
			if (game.playerTransit(MOCK_UID)) {
				fail("player should not be able to enter the room from: " + p.toString());
			}
		}

		// ======== test invalid transit from room to world ========
		Position[] invalidPos_2 = { new Position(1, 2, 1, Direction.North), new Position(1, 2, 1, Direction.East),
				new Position(1, 2, 1, Direction.West), new Position(0, 0, 1, Direction.South),
				new Position(2, 2, 1, Direction.South) };

		for (Position p : invalidPos_2) {
			player.setPosition(p);
			if (game.playerTransit(MOCK_UID)) {
				fail("player should not be able to enter the room from: " + p.toString());
			}
		}
	}

	/**
	 * This method tests whether the player can unlock a lockable object when
	 * they should be able to do so.
	 */
	@Test
	public void validUnlock() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

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
	 * This method tests whether the player can unlock a lockable object when
	 * they should not be able to do so.
	 */
	@Test
	public void invalidUnlock() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

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
	 * This method tests whether the player can take items from a container when
	 * they should be able to do so.
	 */
	@Test
	public void validTakeItems() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

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
		if (!inventory.contains(new Key("A key.", 11111)) || !inventory.contains(new Key("A key.", 789))) {
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
		if (!inventory.contains(new Antidote("A potion of antidote.", Virus.Spanish_Flu))) {
			fail("Should have taken out another antidote");
		}

	}

	/**
	 * This method tests whether the player can take items from a container when
	 * they should not be able to do so.
	 */
	@Test
	public void invalidTakeItems() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

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
	 * This method tests whether the player can put items into a container when
	 * they should be able to do so.
	 */
	@Test
	public void validPutItemsIntoContainer() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

		// mock player
		Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
		game.joinPlayer(player);

		// ====== put items into chest 1 ======
		player.setPosition(new Position(1, 0, 0, Direction.East));
		Antidote antidote = new Antidote("An antidote", Virus.randomVirus());
		player.pickUpItem(antidote);

		// put items into container
		game.playerPutItemIntoContainer(MOCK_UID, 0);

		// the player should no longer have the item
		if (player.getInventory().contains(antidote)) {
			fail("After put item into the container, player shouldn't still have it.");
		}

		MapElement me = areas.get(0).getMapElementAt(2, 0);
		Chest chest = null;
		try {
			chest = (Chest) me;
		} catch (ClassCastException e) {
			// shouldn't happen
		}

		// the container should have item.
		if (!chest.getLoot().contains(antidote)) {
			fail("After put item into the container, the container should have item.");
		}

		// then try to get it out again
		if (!game.playerTakeItemsFromContainer(MOCK_UID)) {
			fail("player should be able to take items from the chest 1");
		}

		List<Item> inventory = player.getInventory();
		if (!inventory.contains(new Antidote("A potion of antidote.", Virus.Black_Death))
				|| !inventory.contains(new Key("A key.", 456)) || !inventory.contains(antidote)) {
			fail("Should have taken out an antidote and a key");
		}

	}

	/**
	 * This method tests whether the player can put items into a container when
	 * they shouldn't be able to do so.
	 */
	@Test
	public void invalidPutItemsIntoContainer() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

		// mock player
		Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
		game.joinPlayer(player);

		// ====== invalid put items into chest 1 ======
		player.setPosition(new Position(1, 0, 0, Direction.West));
		Antidote antidote = new Antidote("An antidote", Virus.randomVirus());
		player.pickUpItem(antidote);

		// invalid put items into container, wrong direction
		if (game.playerPutItemIntoContainer(MOCK_UID, 0)) {
			fail("Player shouldn't be able to put items inside, the direction is incorrect");
		}
		// the player should still have the item
		if (!player.getInventory().contains(antidote)) {
			fail("player should still have it.");
		}

		// invalid put items into container
		player.setPosition(new Position(0, 0, 0, Direction.East));
		if (game.playerPutItemIntoContainer(MOCK_UID, 0)) {
			fail("Player shouldn't be able to put items inside, the chest is not infront.");
		}

		// the player should still have the item
		if (!player.getInventory().contains(antidote)) {
			fail("player should still have it.");
		}

		// ====== invalid put items into chest 2 ======
		player.setPosition(new Position(6, 3, 0, Direction.West));
		// invalid put items into container, wrong direction
		if (game.playerPutItemIntoContainer(MOCK_UID, 0)) {
			fail("Player shouldn't be able to put items inside, the chest is locked");
		}
		// the player should still have the item
		if (!player.getInventory().contains(antidote)) {
			fail("player should still have it.");
		}

		// ====== now let the player put items into chest 2 ======
		player.setPosition(new Position(6, 3, 0, Direction.West));
		player.pickUpItem(new Key("A key.", 456));
		if (!game.playerUnlockLockable(MOCK_UID)) {
			fail("should be able to unlock the chest");
		}
		// valid put items into container, wrong direction
		if (!game.playerPutItemIntoContainer(MOCK_UID, 0)) {
			fail("Player should be able to put items inside");
		}

		// the player should no longer have the item
		if (player.getInventory().contains(antidote)) {
			fail("After put item into the container, player shouldn't still have it.");
		}

		MapElement me = areas.get(0).getMapElementAt(5, 3);
		Chest chest = null;
		try {
			chest = (Chest) me;
		} catch (ClassCastException e) {
			// shouldn't happen
		}

		// the container should have item.
		if (!chest.getLoot().contains(antidote)) {
			fail("After put item into the container, the container should have item.");
		}

		// then try to get it out again
		if (!game.playerTakeItemsFromContainer(MOCK_UID)) {
			fail("player should be able to take items from the chest 1");
		}

		List<Item> inventory = player.getInventory();
		if (!inventory.contains(antidote)) {
			fail("Should have taken out the antidote");
		}

	}

	/**
	 * This method tests whether the player can use antidote, and the effect of
	 * antidote.
	 */
	@Test
	public void validUseAntidote() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

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
			areas = TestConst.createAreas();
			world = areas.get(0);
			game = new Game(world, areas);

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
			if (player.getHealthLeft() != Player.MAX_HEALTH + Antidote.EFFECT * Antidote.MULTIPLIER
					&& player.getHealthLeft() != Player.MAX_HEALTH - Antidote.EFFECT) {
				fail("player's health should either be increased double amount of effect, or decresed single amount of effect");
			}
		}
	}

	/**
	 * This method tests whether the player can use torch, and the effect of
	 * torch.
	 */
	@Test
	public void validUseTorch() {

		for (int i = 0; i < 50; i++) {

			// mock a game world
			Map<Integer, Area> areas = TestConst.createAreas();
			Area world = areas.get(0);
			Game game = new Game(world, areas);

			// mock player
			Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
			game.joinPlayer(player);

			// use torch
			Torch torch = new Torch("A good torch");
			player.pickUpItem(torch);
			if (!game.playerUseItem(MOCK_UID, 0)) {
				fail("player should be able to use antidote at index 0");
			}

			assertTrue("player should be holding a torch", player.isHoldingTorch());

			// get the world time
			LocalTime time = game.getClock();

			// check the torch has already taken effect
			if (time.getHour() >= 6 && time.getHour() < 18) {
				assertEquals("the visibility is wrong", game.getPlayerVisibility(MOCK_UID), Game.DAY_VISIBLIITY);
			} else {
				assertEquals("the visibility is wrong", game.getPlayerVisibility(MOCK_UID), Game.TORCH_VISIBILITY);
			}
		}
	}

	/**
	 * This method tests whether the player can use bag, and the effect of bag.
	 */
	@Test
	public void validUseBag() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);
		// mock player
		Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
		game.joinPlayer(player);

		// make a bag that contains another bag
		List<Item> anotherLoot = new ArrayList<>();
		Antidote antidote = new Antidote("an antidote", Virus.randomVirus());
		Key key = new Key("a key", 000);
		Torch torch = new Torch("a torch");
		anotherLoot.add(antidote);
		anotherLoot.add(key);
		anotherLoot.add(torch);
		Bag anotherBag = new Bag("Another bag", anotherLoot);

		List<Item> loot = new ArrayList<>();
		loot.add(anotherBag);
		Bag bag = new Bag("a bag", loot);

		player.pickUpItem(bag);

		// let the player use the bag
		if (!game.playerUseItem(MOCK_UID, 0)) {
			fail("player should be able to use the bag at index 0");
		}

		// see if the player get everything
		for (Item item : bag.getLoot()) {
			if (!player.getInventory().contains(item)) {
				fail("player should have item: " + item.toString());
			}
		}

		// the player shouldn't still have the bag
		if (player.getInventory().contains(bag)) {
			fail("player shouldn't still have the bag");
		}

		// let the player use another bag
		if (!game.playerUseItem(MOCK_UID, 0)) {
			fail("player should be able to use another bag at index 0");
		}

		// see if the player get everything
		for (Item item : anotherBag.getLoot()) {
			if (!player.getInventory().contains(item)) {
				fail("player should have item: " + item.toString());
			}
		}

		// the player shouldn't still have the bag
		if (player.getInventory().contains(anotherBag)) {
			fail("player shouldn't still have the bag");
		}

		assertEquals("the player should have 3 items", 3, player.getInventory().size());

	}

	/**
	 * This method tests whether the player can destroy items when they should
	 * be able to do so.
	 */
	@Test
	public void validDestroy() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);
		// mock player
		Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
		game.joinPlayer(player);

		// mock some items
		Antidote antidote = new Antidote("an antidote", Virus.randomVirus());
		Torch torch = new Torch("a torch");

		// pick up items
		player.pickUpItem(antidote);
		player.pickUpItem(torch);

		// test antidote
		if (!game.playerDestroyItem(MOCK_UID, 0)) {
			fail("player should be able to destroy antidote");
		}
		if (player.getInventory().contains(antidote)) {
			fail("player shouldn't have antidote");
		}

		// test torch
		if (!game.playerDestroyItem(MOCK_UID, 0)) {
			fail("player should be able to destroy antidote");
		}
		if (player.getInventory().contains(antidote)) {
			fail("player shouldn't have antidote");
		}

	}

	/**
	 * This method tests whether the player can destroy items when they
	 * shouldn't be able to do so.
	 */
	@Test
	public void invalidDestroy() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);
		// mock player
		Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
		game.joinPlayer(player);

		// mock some items
		Key key = new Key("a key", 000);

		List<Item> loot = new ArrayList<>();
		Antidote anotherAntidote = new Antidote("another antidote", Virus.randomVirus());
		loot.add(anotherAntidote);
		Bag bag = new Bag("Another bag", loot);

		// pick up items
		player.pickUpItem(key);
		player.pickUpItem(bag);

		// test key
		if (game.playerDestroyItem(MOCK_UID, 0)) {
			fail("player shouldn't be able to destroy key");
		}
		if (!player.getInventory().contains(key)) {
			fail("player should have the key");
		}

		// test bag
		if (game.playerDestroyItem(MOCK_UID, 1)) {
			fail("player shouldn't be able to destroy bag");
		}
		if (!player.getInventory().contains(bag)) {
			fail("player should have the bag");
		}

		// test item that the player doesn't have
		if (game.playerDestroyItem(MOCK_UID, 3)) {
			fail("player shouldn't be able to destroy things he doesn't have");
		}

	}

	/**
	 * This method tests player's visibility.
	 */
	@Test
	public void playerVisibility() {
		for (int i = 0; i < 50; i++) {
			// mock a game world
			Map<Integer, Area> areas = TestConst.createAreas();
			Area world = areas.get(0);
			Game game = new Game(world, areas);

			// mock player
			Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
			game.joinPlayer(player);

			int hour = game.getClock().getHour();

			if (hour >= 6 && hour < 18) {
				// it's day time
				assertEquals("visibility is wrong", Game.DAY_VISIBLIITY, game.getPlayerVisibility(MOCK_UID));
			} else {
				// it's night time
				assertEquals("visibility is wrong", Game.NIGHT_VISIBILITY, game.getPlayerVisibility(MOCK_UID));

				// light up a torch
				player.pickUpItem(new Torch("a torch"));
				game.playerUseItem(MOCK_UID, 0);
				assertEquals("visibility is wrong", Game.TORCH_VISIBILITY, game.getPlayerVisibility(MOCK_UID));
			}
		}
	}
}