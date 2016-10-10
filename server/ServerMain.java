package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import client.ParserUtilities;
import dataStorage.InitialGameLoader;
import dataStorage.XmlFunctions;
import server.game.Game;
import server.game.GameError;
import server.game.player.Player;
import server.view.ServerGui;

/**
 * This class is where the server is started.
 *
 * @author Rafaela & Hector
 *
 */
public class ServerMain {

	/**
	 * The period between every broadcast
	 */
	public static final int DEFAULT_BROADCAST_CLK_PERIOD = 50;

	/**
	 * A series of port number, in case the port is used.
	 */
	public static final int[] PORT_NUM = { 6000, 6001, 6002, 6003, 6004, 6005 };

	/**
	 * The server socket waiting for connection.
	 */
	private ServerSocket serverSocket;

	/**
	 * The game
	 */
	private Game game;

	/**
	 * The number of players.
	 */
	private int numPlayers;

	/**
	 * A buffer for messages that client send for chatting.
	 */
	private Queue<String> messages;
	
	/**
	 * A map storing all notification messages, where the key is uid, 
	 */
	private Map<Integer, String> notificationMsg;

	/**
	 * This map keeps track of every Receptionist for every connected client.
	 * The key is the unique id of each client
	 */
	private HashMap<Integer, Receptionist> receptionists;

	/**
	 * Constructor
	 */
	public ServerMain() {
		receptionists = new HashMap<>();
		messages = new ConcurrentLinkedQueue<>();

		// how many players?
		System.out.println("How many players (at least 2):");
		numPlayers = ParserUtilities.parseInt(2, 10);

		// create the game world with test version tiny world.
		// game = new Game(TestConst.world, TestConst.areas);

		// load from game maker
		game = InitialGameLoader.makeGame();

		// just for integration day demo
		XmlFunctions.saveInitialFile(game);

		// run the server
		runServer(numPlayers);
	}

	/**
	 * This method listens to client connections, and when all clients are
	 * ready, a multi-player game is started.
	 *
	 * @param numPlayers
	 *            --- the number of players
	 */
	private void runServer(int numPlayers) {

		serverSocket = createServerSocket();
		// display the server address and port.
		System.out.println("Plague server is listening on IP address: " + serverSocket.getInetAddress().toString()
				+ ", port: " + serverSocket.getLocalPort());

		// A simple GUI to display server running.
		new Thread() {
			public void run() {
				ServerGui.port = serverSocket.getLocalPort();
				String[] ipAdress = serverSocket.getInetAddress().toString().split("/");
				ServerGui.ip = ipAdress[1];
				ServerGui.launch(ServerGui.class);
			}
		}.start();

		int count = 0;
		try {
			// Wait for a connection
			while (count != numPlayers) {
				Socket clientSocket = serverSocket.accept();
				int uId = clientSocket.getPort();

				System.out.println("Accepted connection from: " + clientSocket.getInetAddress().toString()
						+ ". uId is: " + uId + ".");

				Receptionist receptionist = new Receptionist(this, clientSocket, uId, game);
				// send the map to client
				receptionist.sendMapID();
				receptionists.put(uId, receptionist);
				count++;
			}

			System.out.println("All clients accepted, now entering lobby, wait till all players are ready");

			// start the initialisation process (enter the lobby)
			runGame();

		} catch (IOException e) {
			System.err.println("I/O error: " + e.getMessage());
		} finally {
			/*
			 * XXX maybe need more clean up actions, close server, etc.
			 */
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("I/O error: " + e.getMessage());
			}
		}
	}

	/**
	 * Start the multi-player game.
	 */
	private void runGame() {

		// join the player in, get the player's user name, avatar and id.
		for (Receptionist r : receptionists.values()) {
			r.receiveNameAvatar();
		}

		// broadcast each client's virus type
		for (Receptionist r : receptionists.values()) {
			r.sendVirusType();
		}

		// broadcast every player's avatar
		for (Receptionist r : receptionists.values()) {
			r.sendAvatars();
		}

		// Now get those workers busy
		for (Receptionist r : receptionists.values()) {
			r.start();
		}

		// ======= Enter the lobby, waiting for everyone ready ========

		// do not start timing until everybody is ready.
		outer: while (true) {
			for (Receptionist r : receptionists.values()) {
				if (!r.isReady()) {
					try {
						Thread.sleep(DEFAULT_BROADCAST_CLK_PERIOD);
					} catch (InterruptedException e) {
						// Should never happen
					}
					continue outer;
				}
			}
			break;
		}

		// ====== everybody is ready, now enter the game ========
		for (Receptionist r : receptionists.values()) {
			r.setGameRunning();
		}

		System.out.println("Finished waiting for everybody ready. Game start");

		game.startTiming();
	}

	/**
	 * This method creates a server socket. If it is not successfully created, a
	 * GameError is thrown.
	 *
	 * @return --- the server socket.
	 */
	private ServerSocket createServerSocket() {

		ServerSocket s = null;
		// try to create a server with port number from pre-defined array.
		for (int i = 0; i < PORT_NUM.length; i++) {
			try {
				s = new ServerSocket(PORT_NUM[i], 50, InetAddress.getLocalHost());
				break;
			} catch (IOException e) {
				continue;
			}
		}

		// check if it is created successfully
		if (s != null) {
			return s;
		} else {
			throw new GameError("Cannot create server socket, all predefined ports are used");
		}
	}

	/**
	 * Add a message into the queue, ready for the server to broadcast.
	 *
	 * @param message
	 *            --- the message
	 */
	public void addMessage(String message) {
		for (int i = 0; i < numPlayers; i++) {
			messages.offer(message);
		}
	}

	/**
	 * retrieve a message from the queue, and let the socket send it out.
	 *
	 * @return --- the next message at the head of queue, or null if the queue
	 *         is empty.
	 */
	public String retrieveMessage() {
		return messages.poll();
	}

	/**
	 * Load game
	 *
	 * @param uid
	 *            --- the uid of the client who requested to load game.
	 * @return --- true/false for success/failure
	 */
	public boolean load(int uid) {
		// check save file existence
		String userName = game.getPlayerById(uid).getName();
		boolean isExisting = XmlFunctions.saveExists(userName);

		if (!isExisting) {
			System.err.println("Cannot find the save file for player " + userName);
			return false;
		}

		Player player = null;
		int newID = 0;
		String name = null;

		// load game
		Game newGame = XmlFunctions.loadFile(userName);
		if (newGame == null) {
			System.err.println("Game object not constucted, Load failed.");
			return false;
		}

		Map<Integer, Player> map = new HashMap<>();

		// Reset player ID in Player object and in map.
		for (Player p : newGame.getPlayers().values()) {
			// gets new ID from other version of player.
			name = p.getName();
			player = game.getPlayerByName(name);
			// Skips player if they are not in the current game before load.
			if (player == null) {
				continue;
			}
			newID = player.getId();
			// Resets UID of player in newGame
			p.resetId(newID);
			map.put(newID, p);
		}
		// Reset players map
		newGame.resetPlayers(map);

		// make sure every client get access to the new game instance,
		for (Receptionist r : receptionists.values()) {
			// Updates the game field.
			r.setGame(newGame);
		}

		return true;
	}

	/**
	 * Save game
	 *
	 * @param uid
	 *            --- the uid of the client who requested to save game.
	 */
	public void save(int uid) {
		XmlFunctions.saveFile(game, game.getPlayerById(uid).getName());
	}

	/**
	 * Main function, start the server.
	 *
	 * @param args
	 */
	public static void main(String args[]) {
		new ServerMain();
	}

}
