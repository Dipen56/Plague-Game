package client;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import server.Packet;
import server.ParserUtilities;
import server.game.player.Avatar;
import server.game.player.Position;

/**
 * This is the client side. This class also has a really simple GUI for testing.
 * 
 * @author Rafaela & Hector
 *
 */
@SuppressWarnings("serial")
public class ClientMain extends JFrame implements Runnable, KeyListener {

	/**
	 * This is designed as a table for renderer to index char board to render
	 * objects.
	 */
	private static final Map<Character, String> MAP_OBJECTS_TABLE;

	// ITEM_TABLE is not a good idea. leave it aside for now.
	// private static final Map<Character, Item> ITEM_TABLE;

	/*
	 * Initialise the table for Renderer. Each table contains a map which maps a
	 * char to the corresponding object, so the Renderer knows what to render by
	 * knowing what char was sent by server.
	 */
	static {

		MAP_OBJECTS_TABLE = new HashMap<>();

		/**
		 * TODO initialise these maps, use whatever way you like to link the
		 * char to resource picture, so the renderer knows what to render if he
		 * knows the char.
		 * 
		 * possible solution: Map<Character, String> where the value is the
		 * resource path
		 * 
		 * for MAP_OBJECTS_TABLE: <br>
		 * <br>
		 * E: Empty space <br>
		 * T: Tree<br>
		 * R: Rock<br>
		 * B: Cupboard<br>
		 * S: ScrapPile<br>
		 * 1-9: Room<br>
		 * a-z: if our world is so big that 1-9 can't represent them all.<br>
		 * D: a door. This should be rendered as ground, but it indicates which
		 * direction the room should be facing.<br>
		 * 
		 * 
		 * 
		 * 
		 */

	}

	/**
	 * The communicating socket.
	 */
	private final Socket socket;
	/**
	 * The data stream used to send packet to server.
	 */
	private DataOutputStream output;
	/**
	 * The data stream used to receive packet from server.
	 */
	private DataInputStream input;
	/**
	 * User id of this connection.
	 */
	private int uid;
	/**
	 * The world time. This is updated by server broadcast.
	 */
	private LocalTime time;
	/**
	 * The health left. This is updated by server broadcast.
	 */
	private int health;
	/**
	 * The visibility. This is updated by server broadcast.
	 */
	private int visibility;
	/**
	 * This map keeps track of all player's avatars. Renderer can look for which
	 * avatar to render from here.
	 */
	private Map<Integer, Avatar> avatars;
	/**
	 * This map keeps track of all player's Positions. Renderer can look for
	 * where to render different players from here.
	 */
	private Map<Integer, Position> positions;
	/**
	 * This map keeps track of this player's inventory. Renderer can look for
	 * what item to render from here.
	 * 
	 * TODO this should change.
	 */
	private List<String> inventory;
	/**
	 * This is a mirror of the field, Map<Integer, Area> areas, in Game class,
	 * except the area is represented as a char[][]. Renderer can look for what
	 * map object to render from here.
	 */
	private Map<Integer, char[][]> areas;
	/**
	 * [Only for test] A text area to display the game status, just like in
	 * console.
	 */
	private JTextArea window;

	/**
	 * Constructor
	 * 
	 * @param socket
	 */
	public ClientMain(Socket socket) {
		this.socket = socket;
		areas = new HashMap<>();
		avatars = new HashMap<>();
		positions = new HashMap<>();

		// =========== SWING =============

		this.setLayout(new BorderLayout(3, 3));
		window = new JTextArea(30, 50);
		window.setEditable(false);
		window.addKeyListener(this);
		this.add(window, BorderLayout.CENTER);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Plague alpha test version 0.1");
		this.pack();
		this.validate();
		this.setResizable(true);
		this.setVisible(true);

		// run the thread
		this.run();
	}

	public ClientMain(String ip, int port, String userName, int avatarID) {
		// TODO: still need to add avatar in and also make use of the username
		// System.out.println("Server address?");
		String tempIp = ParserUtilities.parseString();
		// System.out.println("Port number?");
		int tempPort = ParserUtilities.parseInt(0, 99999);
		Socket s = null;
		try {
			s = new Socket(ip, port);
		} catch (IOException e) {
			System.err.println("Failed to connect to server, I/O exceptions, " + e.toString());
			System.exit(1);
		}
		this.socket = s;
	}

	@Override
	public void run() {
		try {
			output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

			// First, receive from server about the maps
			String incoming = input.readUTF();

			System.out.println("received map string:\n" + incoming);

			/*
			 * TODO "Fin" should be coded in Packet as a properly defined packet
			 * type.
			 */

			while (!incoming.equals("Fin")) {
				ParserUtilities.parseMap(areas, incoming);
				incoming = input.readUTF();
				System.out.println("received map string:\n" + incoming);
			}

			// choose an avatar and input a name
			// TODO this should be GUI instead of console.
			System.out.println("Please choose your Avatar(1-4):");
			byte avatarIndex = (byte) ParserUtilities.parseInt(1, 4);
			output.writeByte(avatarIndex - 1);
			output.flush();
			System.out.println("Please type in your name:");
			String name = ParserUtilities.parseString();
			output.writeUTF(name);
			output.flush();

			// get the uid from server.
			uid = input.readInt();
			System.out.println("Your uId is: " + uid);

			// =========== [DEBUG] ========================
			System.out.println("initialisation done. ready to start game.");
			for (char[][] area : areas.values()) {
				StringBuilder sb = new StringBuilder();
				for (char[] line : area) {
					sb.append(line);
					sb.append("\n");
				}

				System.out.println("received map string:\n");
				System.out.println(sb.toString());
			}

			System.out.println("Now entering while(connected) loop");

			/*
			 * TODO This boolean is not much useful now. It may be useful when
			 * we add in like force-quit features. And it should be a field.
			 */
			boolean connected = true;

			// last, a while true loop to let the client communicate with
			// server.
			while (connected) {

				// read in broadcast

				// 1. all players' position.
				incoming = input.readUTF();
				while (!incoming.equals("Fin")) {
					ParserUtilities.parsePosition(positions, incoming);
					incoming = input.readUTF();
				}

				// 2. the world time
				incoming = input.readUTF();
				time = ParserUtilities.parseTime(incoming);

				// 3. the player's health
				health = input.readInt();

				// 4. the player's visibility
				visibility = input.readInt();

				// 5. the player's inventory
				// XXX This should not be a list of Item.
				incoming = input.readUTF();
				inventory = ParserUtilities.parseInventory(incoming);

				/*
				 * ====================================================
				 * 
				 * This is where we update the Renderer. Or, alternatively, like
				 * Pacman, we could update Renderer in ClockThread
				 * 
				 * ====================================================
				 */

				// update GUI
				window.setText(toTextUI());
			}

		} catch (IOException e) {
			/*
			 * TODO should handle all exceptions on GUI, i.e. catch clause
			 * should popup a dialog on GUI.
			 */
			System.err.println("I/O Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				// TODO when the client sent an disconnect flag, set connected =
				// false
				socket.close();
			} catch (IOException e) {
				System.out.println("I/O error. But who cares, disconnected anyway. ");
			}
		}
	}

	/**
	 * This is a helper method to generate a JTextArea display for testing. Will
	 * be deleted.
	 * 
	 * @return
	 */
	private String toTextUI() {
		StringBuilder sb = new StringBuilder();

		// get current area
		Position currentPos = positions.get(uid);
		int areaId = currentPos.areaId;
		char[][] currentArea = areas.get(areaId);

		// need a clone of this area map.
		char[][] clone = deepClone2DArray(currentArea);

		// draw self
		replaceCharAtPosition(clone, currentPos);

		// look for other players, and put them in char[][] (like draw them)
		for (Position p : positions.values()) {
			// skip self
			if (p.equals(currentPos)) {
				continue;
			}

			/*
			 * TODO In GUI rendering, we should add visibility into
			 * consideration as well. e.g. if he is out of my visibility, no
			 * need to render him.
			 */

			// no need to draw the player in other areas.
			int hisAreaId = p.areaId;
			if (hisAreaId != areaId) {
				continue;
			}

			// replace a character to represent current player
			replaceCharAtPosition(clone, p);
		}

		// display map
		sb.append("========= Map =========\n");
		for (char[] chaSeq : clone) {
			sb.append(chaSeq);
			sb.append("\n");
		}

		// display player's status
		sb.append("======== Status ========\n");
		sb.append("current time: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + "\n");
		sb.append("your health: " + health + "\n");
		sb.append("your visibility: " + visibility + "\n");

		// TODO inventory. need to iterate the inventory and print it out.
		sb.append("your inventory: \n");
		for (String s : inventory) {
			sb.append("- " + s + "\n");
		}

		sb.append("===== Board Legend =====\n");
		sb.append("'E' stands for empty space that you can walk.\n");
		sb.append("'T' stands for tree. 'R' stands for rock.\n");
		sb.append("'B' stands for cupboard. 'S' stands for scrap pile.\n");
		sb.append("'D' stands for door. '1-9' stands for rooms.\n");
		sb.append("'1-9' stands for rooms (roomId in fact).\n");

		sb.append("========= Keys =========\n");

		// @formatter:off
		String s = "[w] Move forward\n" + "[s] Move backward\n" + "[a] Move left\n" + "[d] Move right: \n"
				+ "[q] Turn left: \n" + "[e] Trun right: \n" + "[f] Unlock Lockable: \n"
				+ "[g] Take items from Container: \n" + "[ ] Put item into container: data packet not implemented yet"
				+ "[r] Enter/Exit room: \n" + "[1] Use the 1st item in inventory: \n"
				+ "[2] Use the 2nd item in inventory: \n" + "[0] Destroy the 1st item in inventory: \n";
		// @formatter:on

		sb.append(s);

		return sb.toString();
	}

	private char[][] deepClone2DArray(char[][] grid) {
		char[][] clone = new char[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) {
			System.arraycopy(grid[i], 0, clone[i], 0, grid[i].length);
		}
		return clone;
	}

	private void replaceCharAtPosition(char[][] currentArea, Position p) {
		switch (p.getDirection()) {
		case East:
			currentArea[p.y][p.x] = '>';
			break;
		case North:
			currentArea[p.y][p.x] = '^';
			break;
		case South:
			currentArea[p.y][p.x] = 'v';
			break;
		case West:
			currentArea[p.y][p.x] = '<';
			break;
		default:
			break; // dead code
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {

		int keyCode = event.getKeyCode();

		System.out.println("Key pressed, keycode: " + keyCode);

		try {
			switch (keyCode) {
			case KeyEvent.VK_W:
				output.writeByte(Packet.Forward.toByte());
				output.flush();
				break;
			case KeyEvent.VK_S:
				output.writeByte(Packet.Backward.toByte());
				output.flush();
				break;
			case KeyEvent.VK_A:
				output.writeByte(Packet.Left.toByte());
				output.flush();
				break;
			case KeyEvent.VK_D:
				output.writeByte(Packet.Right.toByte());
				output.flush();
				break;
			case KeyEvent.VK_Q:
				output.writeByte(Packet.TurnLeft.toByte());
				output.flush();
				break;
			case KeyEvent.VK_E:
				output.writeByte(Packet.TurnRight.toByte());
				output.flush();
				break;
			case KeyEvent.VK_F:
				output.writeByte(Packet.Unlock.toByte());
				output.flush();
				break;
			case KeyEvent.VK_G:
				output.writeByte(Packet.TakeOutItem.toByte());
				output.flush();
				break;
			case KeyEvent.VK_R:
				output.writeByte(Packet.Transit.toByte());
				output.flush();
				break;
			case KeyEvent.VK_1:
				// XXX if something is wrong we can try synchronized (this) { }
				output.writeByte(Packet.UseItem.toByte());
				output.writeInt(0);
				output.flush();
				break;
			case KeyEvent.VK_2:
				output.writeByte(Packet.UseItem.toByte());
				output.writeInt(1);
				output.flush();
				break;
			case KeyEvent.VK_0:
				output.writeByte(Packet.DestroyItem.toByte());
				output.writeInt(0);
				output.flush();
				break;

			// TODO Of cause the client should handle more events. We can always
			// add here.

			default:
			}
		} catch (IOException e) {
			System.err.println("I/O exceptions, " + e.toString());
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public static void main(String[] args) {

		System.out.println("Server address?");
		String ip = ParserUtilities.parseString();
		System.out.println("Port number?");
		int port = ParserUtilities.parseInt(0, 99999);
		Socket s = null;
		try {
			s = new Socket(ip, port);
		} catch (IOException e) {
			System.err.println("Failed to connect to server, I/O exceptions, " + e.toString());
			System.exit(1);
		}

		new ClientMain(s);
	}
}
