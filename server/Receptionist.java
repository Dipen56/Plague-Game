package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import server.game.Game;
import server.game.player.Avatar;
import server.game.player.Player;
import server.game.world.Area;

/**
 * This class represents a single thread that handles communication with a
 * connected client. It receives events from a client connection via a socket as
 * well as send information to the client about the current board state.
 *
 * @author Rafaela & Hector
 *
 */
public class Receptionist extends Thread {

	/**
	 * The server
	 */
	private ServerMain server;

	/**
	 * The game instance running on server side.
	 */
	private Game game;

	/**
	 * User (client) id of this connection.
	 */
	private int uid;

	/**
	 * User (client) specified name of this connection.
	 */
	private String userName;

	/**
	 * The communicating socket.
	 */
	private final Socket socket;

	/**
	 * The download link from the client.
	 */
	private DataInputStream input;

	/**
	 * The upload link to the client
	 */
	private DataOutputStream output;

	/**
	 * A flag indicating whether this client is ready to enter game. If it's
	 * false, the server will keep this client in lobby waiting for all clients
	 * ready.
	 */
	private boolean isClientReady = false;

	/**
	 * A flag indicating whether the game is running or not.
	 */
	private boolean isGameRunning = false;

	/**
	 * Constructor. It also initialises the socket input and output.
	 *
	 * @param server
	 *            --- the server
	 * @param socket
	 *            --- the socket used to connect to client
	 * @param uid
	 *            --- the unique id of this client
	 * @param game
	 *            --- the game instance
	 */
	public Receptionist(ServerMain server, Socket socket, int uid, Game game) {
		this.server = server;
		this.game = game;
		this.socket = socket;
		this.uid = uid;

		// create the output and input stream.
		try {
			output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (IOException e) {
			System.err.println("Lost connection with client." + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Send the game world maps as strings to client, and tell the client his
	 * id.
	 */
	public void sendMapID() {

		try {
			// tell the client all maps
			Map<Integer, Area> areas = game.getAreas();
			for (Area map : areas.values()) {
				output.writeUTF(map.toString());
			}
			output.writeUTF("Fin");
			output.flush();

			// tell the client his uid
			output.writeInt(uid);
			output.flush();

		} catch (IOException e) {
			System.err.println("Lost connection with client." + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Receive from client about the player's name and avatar.
	 */
	public void receiveNameAvatar() {
		try {
			byte avatarIndex = input.readByte();
			String name = input.readUTF();
			this.userName = name;
			game.joinPlayer(new Player(uid, Avatar.get(avatarIndex), name));

			System.out.println("player uid: " + uid + ". avatar " + avatarIndex);
			System.out.println("initialisation done. joined player: " + uid + " with avatar " + avatarIndex);

		} catch (IOException e) {
			System.err.println("IO exception: " + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Send the player's virus type to the client
	 */
	public void sendVirusType() {
		// tell the client his virus type
		int virusIndex = game.getPlayerVirus(uid).ordinal();
		try {
			output.writeInt(virusIndex);
			output.flush();
		} catch (IOException e) {
			System.err.println("IO exception: " + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Send each player's avatar to the client.
	 */
	public void sendAvatars() {
		try {
			output.writeUTF(game.getAvatarsString());
			output.flush();
		} catch (IOException e) {
			System.err.println("IO exception: " + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Is this client ready to enter game?
	 *
	 * @return --- true/false for yes/no
	 */
	public boolean isReady() {
		return isClientReady;
	}

	/**
	 * This method usually is called by server to tell the client ready to enter
	 * game.
	 */
	public void setGameRunning() {
		this.isGameRunning = true;
	}

	/**
	 * Updates the uid field.
	 *
	 * @param A
	 *            new uid integer.
	 */
	public void setUID(int uid) {
		this.uid = uid;
	}

	/**
	 * Gets the id of the player.
	 *
	 * @return The player UID.
	 */
	public int getUID() {
		return this.uid;
	}

	@Override
	public void run() {
		try {

			// wait until all users are ready
			while (true) {
				if (input.available() > 0) {
					Packet packet = Packet.fromByte(input.readByte());
					if (packet == Packet.Ready) {
						isClientReady = true;
						break;
					}
				}

				try {
					Thread.sleep(ServerMain.DEFAULT_BROADCAST_CLK_PERIOD);
				} catch (InterruptedException e) {
					// Should never happen
				}
			}

			// don't start the game until server tell us to start.
			// (when all clients are ready).
			while (true) {
				if (isGameRunning) {
					output.writeByte(Packet.Ready.toByte());
					output.flush();
					break;
				}

				try {
					Thread.sleep(ServerMain.DEFAULT_BROADCAST_CLK_PERIOD);
				} catch (InterruptedException e) {
					// Should never happen
				}
			}

			System.out.println("[Debug] Now entering while(isGameRunning) loop");

			// last, let the receptionist constantly communicate with clients.
			while (isGameRunning) {
				// broadcast the game status.
				String str = gameToString();
				output.writeUTF(str);
				output.flush();

				// check if clients has requested anything
				if (input.available() > 0) {
					// read input from client
					byte b = input.readByte();
					Packet packet = Packet.fromByte(b);

					// what did the client want?
					switch (packet) {
					case Forward:
						game.playerMoveForward(uid);
						break;
					case Backward:
						game.playerMoveBackward(uid);
						break;
					case Left:
						game.playerMoveLeft(uid);
						break;
					case Right:
						game.playerMoveRight(uid);
						break;
					case TurnLeft:
						game.playerTurnLeft(uid);
						break;
					case TurnRight:
						game.playerTurnRight(uid);
						break;
					case Transit:
						game.playerTransit(uid);
						break;
					case UseItem:
						int index_1 = input.readInt();
						game.playerUseItem(uid, index_1);
						break;
					case DestroyItem:
						int index_2 = input.readInt();
						game.playerDestroyItem(uid, index_2);
						break;
					case TakeOutItem:
						game.playerTakeItemsFromContainer(uid);
						break;
					case Unlock:
						game.playerUnlockLockable(uid);
						break;
					case Save:
						server.save(uid);
						break;
					case Load:
						if(!server.load(uid))
							throw new RuntimeException("Game could not load.");
						break;
					case Chat:
						String message = "[" + userName + "] " + input.readUTF();
						server.addMessage(message);
						break;
					case Disconnect:
						// TODO handle disconnection. or we can handle it in
						// catch clause.
						input.close();
						output.close();

						// TODO close this socket

						// TODO stop this thread

						break;
					default:
						break;
					}
				}

				// a little nap
				Thread.sleep(ServerMain.DEFAULT_BROADCAST_CLK_PERIOD);
			}
		} catch (IOException e) {
			System.err.println("Player " + uid + " disconnected.");
		} catch (InterruptedException e) {
			System.err.println("Thread sleep interrupted. No big deal.");
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			game.disconnectPlayer(uid);
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("I/O error. But who cares, Clients disconnected anyway. ");
			}
		}
	}

	/**
	 * This method generates a String representation of the game status. The
	 * format of it is:
	 *
	 * <p>
	 * <li>Time
	 * <li>Health
	 * <li>Visibility
	 * <li>Positions of all players
	 * <li>The inventory of the player in this client
	 * <li>The status of player holding torch or not.
	 * <li>Chat message if there is any.
	 *
	 * <p>
	 * Each one of them is separated by a new line character '\n'. The format of
	 * each part should refer to
	 * {@link client.ParserUtilities #parseTime(String) parseTime},
	 * {@link client.ParserUtilities #parsePosition(java.util.Map, String)
	 * parsePosition}, {@link client.ParserUtilities #parseInventory(String)
	 * parseInventory}, and
	 * {@link client.ParserUtilities #parseTorchStatus(Map, String)
	 * parseTorchStatus}.
	 *
	 * @return --- a String representation of the game status
	 */
	private String gameToString() {
		StringBuilder gameString = new StringBuilder();

		// 1. time
		String time = game.getClockString();
		gameString.append(time);
		gameString.append('\n');

		// 2. health
		int health = game.getPlayerHealth(uid);
		gameString.append(health);
		gameString.append('\n');

		// 3. visibility
		int visibility = game.getPlayerVisibility(uid);
		gameString.append(visibility);
		gameString.append('\n');

		// 4. positions of all players
		for (Player p : game.getPlayers().values()) {
			String s = p.getGeographicString();
			gameString.append(s);
			gameString.append('|');
		}
		gameString.append('\n');

		// 5. inventory
		String inventory = game.getPlayerInventoryString(uid);
		gameString.append(inventory);
		gameString.append('\n');

		// 6. holding torch or not for all players.
		String torchStatus = game.getTorchStatusString();
		gameString.append(torchStatus);
		gameString.append('\n');

		// 7. chat message
		String message = server.retrieveMessage();
		if (message != null) {
			gameString.append(message);
			gameString.append('\n');
		}

		/*
		 * TODO 8. user-specified content
		 *
		 *
		 *
		 * chat message
		 */

		return gameString.toString();
	}

	/**
	 * This method is used when the server loads game back, the game instance
	 * gets re-referenced.
	 *
	 * @param game
	 *            --- the new game instance
	 */
	public void setGame(Game game) {
		this.game = game;
	}

}