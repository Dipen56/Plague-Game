package client.view;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;

import client.Client;
import client.ParserUtilities;
import client.rendering.Rendering;
import server.Packet;
import server.game.player.Avatar;
import server.game.player.Direction;
import server.game.player.Position;
import server.game.player.Virus;

/**
 * This class is the client side UI, which is where the user start the game
 * from. It also servers as the controller for communicating between client and
 * GUI/Renderer. The controller tells the server about the user's action by
 * interpreting mouse and keyboard events from the user, and updates the
 * renderer/GUI according to the received information from server.
 *
 * @author Dipen & Hector
 *
 */
public class ClientUI {

	/**
	 * The period between every update
	 */
	// This is the default clock thread
	// public static final int DEFAULT_CLK_PERIOD = 100;
	// Testing clock thread
	public static final int DEFAULT_CLK_PERIOD = 50;

	// ============ info fields =================
	/**
	 * User id of this connection.
	 */
	private int uid;

	/**
	 * User name of this connection.
	 */
	private String userName;

	/**
	 * Avatar type of this connection.
	 */
	private Avatar avatar;

	/**
	 * e Virus type of the player at this connection
	 */
	private Virus virus;

	/**
	 * The health left. This is updated by server broadcast.
	 */
	private int health;

	/**
	 * The visibility. This is updated by server broadcast.
	 */
	private int visibility;

	/**
	 * True if player health reached zero.
	 */
	private boolean playerDead = false;

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
	 * This map keeps track of the status for all players that whether he is
	 * holding a torch.
	 */
	private Map<Integer, Boolean> torchStatus;

	/**
	 * This list keeps track of this player's inventory. Each item is
	 * represented as a String whose format is: Character@Description. <br>
	 * <br>
	 * e.g. "A@An antidote. It has a label: Type G"<br>
	 * <br>
	 * The character at beginning is used to look for resource image. The
	 * description is used to pop up a hover tootip for this item.
	 */
	private List<String> inventory;

	/**
	 * This is a mirror of the field, Map<Integer, Area> areas, in Game class,
	 * except the area is represented as a char[][]. Renderer can look for what
	 * map object to render from here.
	 */
	private Map<Integer, char[][]> areas;

	/**
	 * This is a table for retrieving descriptions for rooms. Renderer can look
	 * for what description for specific rooms to render.
	 */
	private Map<Integer, String> descriptions;

	// ============ Model and Views =============
	/**
	 * The Gui
	 */
	private GUI gui;

	/**
	 * The renderer
	 */
	private Rendering render;

	/**
	 * The client side socket connection maintainer
	 */
	private Client client;

	/**
	 * A clock thread for generating constant pulse to update rendering and GUI.
	 */
	private ClockThread clockThread;

	// ============ Event Handlers ==============
	/**
	 * Event Handler for buttons
	 */
	private EventHandler<ActionEvent> actionEvent;

	/**
	 * Event Handler for key events
	 */
	private EventHandler<KeyEvent> keyEvent;

	/**
	 * Event Handler for mouse events
	 */
	private EventHandler<MouseEvent> mouseEvent;

	/**
	 * Event Handler for window events
	 */
	private EventHandler<WindowEvent> windowEvent;
	/**
	 * This is the index of the Avatar
	 */
	private int avatarIndex = 0;

	/**
	 * This is index for the item array
	 */
	private int itemIndex;

	private boolean descriptionToggle = true;

	/**
	 * Constructor
	 */
	public ClientUI() {
		areas = new HashMap<>();
		descriptions = new HashMap<>();
		avatars = new HashMap<>();
		positions = new HashMap<>();
		torchStatus = new HashMap<>();
		// TODO: need to uses the other constructor
		// render = new Rendering();
		render = new Rendering();
		// TODO: get the actual player direction
		gui = new GUI(this, render);
		GUI.launch(GUI.class);
	}

	/**
	 * This method is used to connect the players to the client which then
	 * connects them to the server
	 *
	 * @param ip
	 *            --- the server ip address.
	 * @param port
	 *            --- the port number
	 * @param userName
	 *            --- the user name
	 * @param avatarIndex
	 *            --- the index of Avatar that the player chose.
	 * @return
	 */
	public boolean loginPlayer(String ip, int port, String userName, int avatarIndex) {
		// ip address format check
		if (!ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
			GUI.showMsgPane("Error", "It's not a correct ip address.");
			return false;
		}
		// create a socket
		Socket s = null;
		try {
			s = new Socket(ip, port);
		} catch (IOException e) {
			GUI.showMsgPane("Error", "Failed to connect to server");
			return false;
		}
		client = new Client(s, this);
		this.userName = userName;
		this.avatar = Avatar.get(avatarIndex);
		client.start();
		return true;
	}

	/**
	 * This method is used to start the listeners
	 */
	public void startListeners() {
		// start the gui components listener, key listener, and mouse listener
		setActionEventHandler();
		setKeyEventHander();
		setMouseEventHander();
	}

	/*
	 * ===============================
	 *
	 * Methods related to the client
	 *
	 * ===============================
	 */
	/**
	 * When the client receives the user ID from the server, this method will
	 * update the local user ID.
	 *
	 * @param uid
	 *            --- user id
	 */
	public void parseUID(int uid) {
		this.uid = uid;
	}

	/**
	 * When the client receives the user's virus type from the server, this
	 * method will update the local record
	 *
	 * @param virusIndex
	 *            --- index of virus, which is equal to its ordinal number.
	 */
	public void parseVirus(int virusIndex) {
		this.virus = Virus.get(virusIndex);
	}

	/**
	 * When the client receives a map string from the server, this method will
	 * update the local table which records every area's map (in a plain char
	 * matrix).
	 *
	 * @param mapStr
	 *            --- a string representation of all maps in game.
	 */
	public void parseMap(String mapStr) {
		ParserUtilities.parseMap(areas, descriptions, mapStr);
	}

	/**
	 * When the client receives the string recording all players positions from
	 * the server, this method will update the local table which records every
	 * player's position.
	 *
	 * @param posStr
	 *            --- a string representation of all positions of players.
	 */
	public void parsePosition(String posStr) {
		ParserUtilities.parsePosition(positions, posStr);
	}

	/**
	 * When the client receives the string recording all players avatars from
	 * the server, this method will update the local table which records every
	 * player's avatar.
	 *
	 * @param avatarsStr
	 *            --- a string representation of all avatars of all players.
	 */
	public void parseAvatars(String avatarsStr) {
		ParserUtilities.parseAvatar(avatars, avatarsStr);
	}

	/**
	 * When the client receives a time string from the server, this method will
	 * update the local time.
	 *
	 * @param timeStr
	 *            --- a string representation of world time.
	 */
	public void parseTime(String timeStr) {
		gui.setTime(timeStr);
	}

	/**
	 * When the client receives a health update from the server, this method
	 * will update the local health.
	 *
	 * @param health
	 *            --- the health left
	 */
	public void parseHealth(int health) {
		this.health = health;
	}

	/**
	 * When the client receives a health update from the server, this method
	 * will update the local health.
	 *
	 * @param visibility
	 *            --- the visibility
	 */
	public void parseVisibility(int visibility) {
		this.visibility = visibility;
	}

	/**
	 * When the client receives a inventory update from the server, this method
	 * will update the local inventory.
	 *
	 * @param invenStr
	 *            --- a string representation of inventory items.
	 */
	public void parseInventory(String invenStr) {
		inventory = ParserUtilities.parseInventory(invenStr);
	}

	/**
	 * When the client receives the string recording the status of player
	 * holding torch from the server, this method will update the local table
	 * which records every player's status of holding torch.
	 *
	 * @param torchStatusStr
	 *            --- a string representation of the status of player holding
	 *            torch
	 */
	public void parseTorchStatus(String torchStatusStr) {
		ParserUtilities.parseTorchStatus(torchStatus, torchStatusStr);
	}

	/**
	 * When the client receives the string of chat message from the server, this
	 * method will update the chat text area.
	 *
	 * @param chat
	 *            --- a string representation of the status of player holding
	 *            torch
	 */
	public void parseChatMessage(String chat) {
		gui.setChatText(chat);
	}

	/**
	 * This method reset uid for a hard reset
	 *
	 * @param uId
	 */
	public void resetUid(int uId) {
		this.uid = uId;
	}

	/**
	 * Get the user name
	 *
	 * @return --- the player's choice of user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Get the avatar.
	 *
	 * @return --- the player's choice of avatar
	 */
	public Avatar getAvatar() {
		return avatar;
	}

	/*
	 * ===============================
	 *
	 * Methods related to the render
	 *
	 * ===============================
	 */
	/**
	 * This method is called by ClockThread periodically to update the renderer
	 * and GUI.
	 */
	public void updateRenderAndGui() {
		// 0. necessary variables
		Position playerLoc = positions.get(uid);
		int areaId = playerLoc.areaId;
		char[][] worldMap = areas.get(areaId);

		// 1. update minimap
		gui.updateMinimap(playerLoc, uid, worldMap, visibility, positions);

		// 2. update the renderer
		render.render(playerLoc, worldMap, visibility, uid, avatars, positions, torchStatus, false);

		// 3. update the health bar
		gui.updateHealth(health);

		// 4. update the inventory
		gui.setInventory(inventory);

		// 5. update area/room description
		render.updateAreaDescription(descriptions.get(areaId));
		// 6. update the map object description
		if (descriptionToggle) {
			gui.displayObjectDescription(getFrontElementString());
		} else {
			gui.displayObjectDescription("");
		}

		// 7. if player is dead, prompt it.
		if (!playerDead) {
			// Displays dialog when player health is 0
			if (health <= 0) {
				AlertBox.displayMsg("YOU ARE DEAD", "GAMEOVER");
				playerDead = true;
			}
		}
	}

	/**
	 * Alert the Renderer and GUI to start the game.
	 */
	public void startGame() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gui.startGame();
				clockThread = new ClockThread(DEFAULT_CLK_PERIOD, ClientUI.this);
				clockThread.start();
			}
		});

		gui.setHealthBar(health, virus,userName,avatar);

		gui.objectLabel();
		render.setAreaDescription();

	}

	/**
	 * This method is used to set action event handlers. The actions for certain
	 * button or FX component events are defined here.
	 */
	private void setActionEventHandler() {
		actionEvent = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (event.toString().contains("Send")) {
					// send chat message.
					client.sendWithString(Packet.Chat, gui.getChatMsg());
				} else if (event.toString().contains("Play")) {
					// this is used to simply change the scene
					gui.loginScreen();
				} else if (event.toString().contains("Run Away")) {
					// this is for the main screen of the game
					gui.getWindow().close();
				} else if (event.toString().contains("Help")) {
					// TODO: need to make a help thing which tells the user how
					// to play the game
				} else if (event.toString().contains("Login")) {
					// parse the port number to int
					int port = -1;
					try {
						port = Integer.valueOf(gui.getPortString());
					} catch (NumberFormatException e) {
						GUI.showMsgPane("Error", "Port number should be an integer");
						return;
					}

					if (loginPlayer(gui.getIpAddress(), port, gui.getUserName(), gui.getAvatarIndex())) {
						// in the waiting room
						gui.waitingRoom();

					}
				} else if (event.toString().contains("Leave")) {
					// this is for the login screen
					gui.getWindow().close();
				} else if (event.toString().contains("Ready")) {

					client.setUserReady(true);
					gui.disableReadyButton();

				} else if (event.toString().contains("Leave Game")) {
					// this is for leaving the waiting room
					gui.getWindow().close();
				} else if (event.toString().contains("LoadMenu")) {
					client.send(Packet.Load);
				} else if (event.toString().contains("SaveMenu")) {
					client.send(Packet.Save);
				} else if (event.toString().contains("CloseMenu")) {
					gui.getWindow().close();
				} else if (event.toString().contains("InfoMenu")) {
					// TODO: make a info page
					System.out.println("INFO");
				} else if (event.toString().contains("AboutMenu")) {
					System.out.println("ABOUT");
				} else if (event.toString().contains("PrevAvatar")) {
					avatarIndex--;
					if (avatarIndex < 0) {
						avatarIndex = 3;
					}
					gui.changeAvatarImage(avatarIndex);
				} else if (event.toString().contains("NextAvatar")) {
					avatarIndex++;
					if (avatarIndex > 3) {
						avatarIndex = 0;
					}
					gui.changeAvatarImage(avatarIndex);
				} else if (event.toString().contains("right-insert")) {

					// System.out.println("insert");
					// System.out.println(itemIndex);

					// the player want to insert item into container
					client.sendWithIndex(Packet.PutItemIntoContainer, itemIndex);

				} else if (event.toString().contains("right-use")) {
					// System.out.println("right-use");
					client.sendWithIndex(Packet.UseItem, itemIndex);

				} else if (event.toString().contains("drop-use")) {
					// System.out.println("drop-use");
					client.sendWithIndex(Packet.DestroyItem, itemIndex);

				} else if (event.toString().contains("Description")) {
					descriptionToggle = !descriptionToggle;
					gui.setDescriptionOn(descriptionToggle);
				}
			}
		};
	}

	/**
	 * This method is used to set Key event handlers. The actions for Key events
	 * are defined here.
	 */
	private void setKeyEventHander() {
		keyEvent = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode keyCode = event.getCode();
				// getSorce will give the control which caused the event
				if (keyCode == KeyCode.LEFT || keyCode == KeyCode.A) {
					client.send(Packet.Left);
				} else if (keyCode == KeyCode.RIGHT || keyCode == KeyCode.D) {
					client.send(Packet.Right);
				} else if (keyCode == KeyCode.UP || keyCode == KeyCode.W) {
					client.send(Packet.Forward);
				} else if (keyCode == KeyCode.DOWN || keyCode == KeyCode.S) {
					client.send(Packet.Backward);
				} else if (keyCode == KeyCode.Q) {
					client.send(Packet.TurnLeft);
				} else if (keyCode == KeyCode.E) {
					client.send(Packet.TurnRight);
				} else if (keyCode == KeyCode.F) {
					client.send(Packet.Unlock);

				} else if (keyCode == KeyCode.G) {
					client.send(Packet.TakeOutItem);

				} else if (keyCode == KeyCode.R) {
					client.send(Packet.Transit);
				} else if (keyCode == KeyCode.DIGIT1) {
					client.sendWithIndex(Packet.UseItem, 0);
				} else if (keyCode == KeyCode.DIGIT2) {
					client.sendWithIndex(Packet.UseItem, 1);
				} else if (keyCode == KeyCode.DIGIT3) {
					client.sendWithIndex(Packet.UseItem, 2);
				} else if (keyCode == KeyCode.DIGIT4) {
					client.sendWithIndex(Packet.UseItem, 3);
				} else if (keyCode == KeyCode.DIGIT5) {
					client.sendWithIndex(Packet.UseItem, 4);
				} else if (keyCode == KeyCode.DIGIT6) {
					client.sendWithIndex(Packet.UseItem, 5);
				} else if (keyCode == KeyCode.DIGIT7) {
					client.sendWithIndex(Packet.UseItem, 6);
				} else if (keyCode == KeyCode.DIGIT8) {
					client.sendWithIndex(Packet.UseItem, 7);
				}
				/*
				 * TODO need more keys
				 *
				 * How to implement shift + 1 keys???
				 *
				 *
				 */
			}
		};
	}

	/**
	 * This method is used to set mouse event handlers. The actions for mouse
	 * events are defined here.
	 */
	private void setMouseEventHander() {
		mouseEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Currently this listen to clicks on the items
				// TODO: some how make it work with items
				// System.out.println("here" + event.toString());

				if (event.toString().contains("Grid") && event.isSecondaryButtonDown() == false) {
					// System.out.println(event.getX());
					if (inventory.size() != 0) {
						int itemX = (int) (event.getX() / 60);
						int itemY = (int) (event.getY() / 60);
						gui.setItemDescription(itemX, itemY);
						// System.out.println(itemX + " " + itemY);
					}
				} else if (event.getButton() == MouseButton.SECONDARY) {
					if (inventory.size() != 0) {
						int itemX = (int) (event.getX() / 60);
						int itemY = (int) (event.getY() / 60);
						String item = gui.getItemDescription(itemX, itemY);

						// System.out.println("item " + item);
						if (item != null) {

							if (item.startsWith("A")) {
								gui.antidoteRightClickOption();
							} else if (item.startsWith("K")) {
								gui.keyRightClickOption();
							} else if (item.startsWith("T")) {
								gui.antidoteRightClickOption();
							} else if (item.startsWith("B")) {
								gui.keyRightClickOption();
							} else if (item.startsWith("N")) {
								gui.rightClickClear();
							}

							if (itemY == 0) {
								itemIndex = itemX;
							} else if (itemY == 1) {
								if (itemX == 0) {
									itemIndex = 4;
								} else if (itemX == 1) {
									itemIndex = 5;
								} else if (itemX == 2) {
									itemIndex = 6;
								} else if (itemX == 3) {
									itemIndex = 7;
								}

							}

						} else {
							gui.rightClickClear();
						}
						// System.out.println(itemX + " " + itemY);
					}
					// System.out.println("here" + event.toString());
				}
			}
		};
	}

	/**
	 *
	 * @return
	 */
	public String getFrontElementString() {

		Position selfPos = positions.get(uid);
		Direction selfDir = selfPos.getDirection();
		int currentAreaId = selfPos.areaId;
		char[][] currentMap = areas.get(currentAreaId);
		int width = currentMap[0].length;
		int height = currentMap.length;

		// what's in front
		int frontX;
		int frontY;
		switch (selfDir) {
		case East:
			if (selfPos.x + 1 >= width) {
				return "";
			}
			frontX = selfPos.x + 1;
			frontY = selfPos.y;
			break;
		case North:
			if (selfPos.y - 1 < 0) {
				return "";
			}
			frontX = selfPos.x;
			frontY = selfPos.y - 1;
			break;
		case South:
			if (selfPos.y + 1 >= height) {
				return "";
			}
			frontX = selfPos.x;
			frontY = selfPos.y + 1;
			break;
		case West:
			if (selfPos.x - 1 < 0) {
				return "";
			}
			frontX = selfPos.x - 1;
			frontY = selfPos.y;
			break;
		default:
			return "";
		}

		String decription = GUI.MAP_OBJECT_DESCRIPTION.get(currentMap[frontY][frontX]);
		if (decription == null) {
			return "";
		} else {
			return decription;
		}
	}

	/**
	 * This method will return the action listeners
	 *
	 * @return --- the action listeners
	 */
	public EventHandler<ActionEvent> getActionEventHandler() {
		return actionEvent;
	}

	/**
	 * This method will return the key listeners
	 *
	 * @return --- the key listeners
	 */
	public EventHandler<KeyEvent> getKeyEventHander() {
		return keyEvent;
	}

	/**
	 * This method will return the mouse listener
	 *
	 * @return --- the mouse listener
	 */
	public EventHandler<MouseEvent> getMouseEventHander() {
		return mouseEvent;
	}

	/**
	 * This method will return the window listener
	 *
	 * @return --- the window listener
	 */
	public EventHandler<WindowEvent> getWindowEventHander() {
		return windowEvent;
	}

	/**
	 * Main function.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new ClientUI();
	}

}