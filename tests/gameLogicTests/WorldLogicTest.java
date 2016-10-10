package tests.gameLogicTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalTime;
import java.util.Map;

import org.junit.Test;

import server.game.Game;
import server.game.TestConst;
import server.game.items.Torch;
import server.game.player.Avatar;
import server.game.player.Player;
import server.game.world.Area;

public class WorldLogicTest {

	/**
	 * a mock user id
	 */
	private static final int MOCK_UID = 123;

	/**
	 * This method tests joining and disconnecting players.
	 */
	@Test
	public void joinDisconnectPlayer() {
		// mock a game world and player
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

		Player player_1 = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
		Player player_2 = new Player(MOCK_UID, Avatar.Avatar_2, "AnotherHector");
		Player player_3 = new Player(MOCK_UID + 1, Avatar.Avatar_3, "SameHector");

		// test joining players.
		assertEquals("There should be no players yet", 0, game.getPlayers().size());
		game.joinPlayer(player_1);
		assertEquals("Should have 1 player in game", 1, game.getPlayers().size());
		game.joinPlayer(player_2);
		assertEquals("Cannot have more than one players with same id", 1, game.getPlayers().size());
		game.joinPlayer(player_3);
		assertEquals("Should have 2 players in game", 2, game.getPlayers().size());
		if (player_1.getPosition().equals(player_3.getPosition())) {
			fail("Players should be spawn in different positions.");
		}

		// test disconnecting players.
		game.disconnectPlayer(MOCK_UID);
		assertEquals("Should have 1 players in game", 1, game.getPlayers().size());
	}

	/**
	 * This method tests the time elapse and its effect on player's health and
	 * torch time. Warning: this method will put current Thread into sleep for
	 * more than 2 seconds.
	 */
	@Test
	public void timeElapse() {
		// mock a game world
		Map<Integer, Area> areas = TestConst.createAreas();
		Area world = areas.get(0);
		Game game = new Game(world, areas);

		// mock players
		int size = 10;
		Player[] players = new Player[size];
		int[] healthBefore = new int[size];
		Torch[] torches = new Torch[size];
		int[] torchTimeBefore = new int[size];

		for (int i = 0; i < size; i++) {
			players[i] = new Player(i, Avatar.Avatar_1, "hector_" + i);
			game.joinPlayer(players[i]);
			healthBefore[i] = players[i].getHealthLeft();

			// pick up a torch
			torches[i] = new Torch("a torch");
			players[i].pickUpItem(torches[i]);
			torchTimeBefore[i] = torches[i].getTimeLeft();

			// light up the torch
			game.playerUseItem(i, 0);
		}

		LocalTime timeBefore = game.getClock();
		game.startTiming();

		// advance time for 1.2 second
		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// get the time and health after
		LocalTime timeAfter = game.getClock();
		int[] healthAfter = new int[size];
		int[] torchTimeAfter = new int[size];
		for (int i = 0; i < size; i++) {
			healthAfter[i] = players[i].getHealthLeft();
			torchTimeAfter[i] = torches[i].getTimeLeft();
		}

		// check time
		if (!timeBefore.plusSeconds(Game.TIME_ADVANCING_SPEED).equals(timeAfter)) {
			fail("time should be advancing");
		}

		// check health and torch time reducing
		for (int i = 0; i < size; i++) {
			assertEquals("Time elapsing doesn't take effect on player's life", healthBefore[i] - 1, healthAfter[i]);
			assertEquals("Time elapsing doesn't take effect on torch's life", torchTimeBefore[i] - 1,
					torchTimeAfter[i]);
		}

		// let the player extinguish torch fire
		for (int i = 0; i < size; i++) {
			game.playerUseItem(i, 0);
			torchTimeBefore[i] = torchTimeAfter[i];
		}

		// advance time for another 1.2 second
		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < size; i++) {
			torchTimeAfter[i] = torches[i].getTimeLeft();
		}

		// check torch time reducing
		for (int i = 0; i < size; i++) {
			assertEquals("Time elapsing shouldn't take effect on torch's life because it's not lit", torchTimeBefore[i],
					torchTimeAfter[i]);
		}

	}

}
