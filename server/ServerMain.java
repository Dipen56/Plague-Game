package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.game.Game;
import server.game.GameError;
import server.game.TestConst;
import server.view.ServerGui;

/**
 * This is a server main class I (Hector) copied from Rafaela's code. Some
 * modifications.
 * 
 * @author Rafaela & Hector
 *
 */
public class ServerMain {
	/**
	 * The period between every update
	 */
	public static final int DEFAULT_CLK_PERIOD = 100;
	/**
	 * The period between every broadcast
	 */
	static final int DEFAULT_BROADCAST_CLK_PERIOD = 50;
	/**
	 * A series of port number, in case the port is used.
	 */
	public static final int[] PORT_NUM = { 6000, 6001, 6002, 6003, 6004, 6005 };
	/**
	 * The thread used for maintaining the game world
	 */
	private ClockThread clockThread;
	/**
	 * The server socket waiting for connection.
	 */
	private ServerSocket serverSocket;
	/**
	 * A thread pool handling all clients
	 */
	private ExecutorService clientsPool;
	/**
	 * The game
	 */
	private Game game;
	/**
	 * This map keeps track of every Receptionist for every connected client
	 */
	private HashMap<Integer, Receptionist> receptionists;

	/**
	 * this is the server GUI which is used to display the ip an port of the
	 * server
	 */

	/**
	 * Constructor
	 */
	public ServerMain() {
		receptionists = new HashMap<>();

		// how many players?
		System.out.println("How many players (between 2 and 4):");
		int numPlayers = ParserUtilities.parseInt(2, 4);

		// Parsing a file to construct the world
		// game = new Game(file);

		// create the game world
		game = new Game(TestConst.world, TestConst.areas);
		clockThread = new ClockThread(DEFAULT_CLK_PERIOD, game);
		clientsPool = Executors.newFixedThreadPool(numPlayers);

		runServer(numPlayers);
	}

	/**
	 * This method listens to client connections, and when all clients are
	 * ready, a multi-player game is started.
	 * 
	 * @param game
	 * @param numPlayers
	 * @param clock
	 */
	private void runServer(int numPlayers) {

		serverSocket = createServerSocket();
		// display the server address and port.
		System.out.println("Plague server is listening on IP address: " + serverSocket.getInetAddress().toString()
				+ ", port: " + serverSocket.getLocalPort());

		new Thread() {
			public void run() {
				ServerGui.port = serverSocket.getLocalPort();
				;
				ServerGui.ip = serverSocket.getInetAddress().toString();
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

				Receptionist receptionist = new Receptionist(clientSocket, uId, DEFAULT_BROADCAST_CLK_PERIOD, game);
				receptionists.put(uId, receptionist);
				count++;
			}

			System.out.println("All clients accepted, GAME ON!");

			// start the initialisation process (enter the lobby)
			runGame();

		} catch (IOException e) {
			System.err.println("I/O error: " + e.getMessage());
		} finally {
			/*
			 * XXX clean up actions, shut down clients pool, close server, etc.
			 * Perhaps this should be put in finally clause.
			 */

			// shut down the thread pool and server socket.
			clientsPool.shutdown();
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("I/O error: " + e.getMessage());
			}

		}

	}

	private void runGame() {
		// Now get those workers busy
		for (Receptionist r : receptionists.values()) {
			clientsPool.submit(r);
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

		System.out.println("finished waiting for everybody ready.");

		// ====== everybody is ready, now enter the game ========
		for (Receptionist r : receptionists.values()) {
			r.setReady();
		}

		System.out.println("finished telling everybody to start.");

		game.startTiming();
		clockThread.start();
	}

	/**
	 * This method creates a server socket. If it is not successfully created, a
	 * GameError is thrown.
	 * 
	 * @return
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
	 * Main function
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		new ServerMain();
	}

}
