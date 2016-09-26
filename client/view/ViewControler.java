package client.view;

import client.Client;
import client.ClientMain;
import client.rendering.Rendering;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;

/**
 * this is class represents the controller witch will be used to communicate
 * between client and GUI. The controller interprets the mouse and keyboard
 * inputs from the user, informing the model and/or the view to change as
 * appropriate.
 * 
 * @author Dipen &  Hector
 * 
 *
 */
public class ViewControler {
	private GUI gui;
	private Rendering render;
	private Client client;

	// Event Handlers
	// for clicks
	private EventHandler<ActionEvent> actionEvent;
	// for keys inputs
	private EventHandler<KeyEvent> keyEvent;
	// for mouse events
	private EventHandler<MouseEvent> mouseEvent;
	// for window resizing not really need else where
	private EventHandler<WindowEvent> windowEvent;

	public ViewControler(String[] args) {
		// TODO: need to uses the other constructor
		render = new Rendering();
		// TODO: get the actual player direction
		render.setDirection("up");
		gui = new GUI(this, render);
		GUI.launch(GUI.class);

	}

	/**
	 * this method is used to connect the players to the client which then
	 * connects them to the server
	 * 
	 * @param ip
	 * @param port
	 * @param userName
	 * @param avatarID
	 */
	public void loginPlayer(String ip, int port, String userName, int avatarID) {
		client = new Client(ip, port, userName, avatarID);
	}

	/**
	 * this method is used to start the listeners
	 */
	public void startListeners() {
		actionEventHandler();
		// this will start the key listener
		keyEventHander();
		// this will start the mouse listener
		mouseEventHander();
	}

	/**
	 * this method will return the action listeners
	 * 
	 * @return
	 */
	public EventHandler<ActionEvent> getActionEventHandler() {
		return actionEvent;
	}

	/**
	 * this method will return the key listeners
	 * 
	 * @return
	 */
	public EventHandler<KeyEvent> getKeyEventHander() {
		return keyEvent;
	}

	/**
	 * this method will return the mouse listener
	 * 
	 * @return
	 */
	public EventHandler<MouseEvent> getMouseEventHander() {
		return mouseEvent;
	}

	/**
	 * this method will return the window listener
	 * 
	 * @return
	 */
	public EventHandler<WindowEvent> getWindowEventHander() {
		return windowEvent;
	}

	/**
	 * this method is called client recives a msg from anther client and need to
	 * update this client. will need to pass a user or give the player to the
	 * contructor.
	 * 
	 * @param msg
	 * @param user
	 */
	public void setChatMasg(String msg, String user) {
		gui.setChatText(msg, user);
	}

	/**
	 * this method is used to check for action and give a implementation of
	 * handle method
	 */
	private void actionEventHandler() {
		actionEvent = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (event.toString().contains("Send")) {
					// TODO: connect up this with the client and server so that
					// others can recive the msg. to get the msg typed in the
					// text filed use gui.getChatMsg()
					// System.out.println(gui.getChatMsg());

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
					// TODO: set secected avatar
					// TODO: check login was correct
					// TODO set the Avatars whicha re all the palyes currently
					// in
					// the waiting room
					gui.waitingRoom();

				} else if (event.toString().contains("Leave")) {
					// this is for the login screen
					gui.getWindow().close();
				} else if (event.toString().contains("Begin")) {
					// this for when there is a player in the waiting room
					// TODO: check that there are 2 > players only start the
					// game if there are else promt a masg and also let the
					// other players know the game is starting
					// gui.setTime("1:00");
					// TODO: set the world clock here using gui.setTime(String);
					gui.startGame();
				} else if (event.toString().contains("Leave Game")) {
					// this is for leaving the waiting room
					gui.getWindow().close();
				}

			}
		};
	}

	/**
	 * this methods is used to listen for keys being pressed and will respond
	 * Accordingly
	 */
	private void keyEventHander() {
		keyEvent = new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// getSorce will give the control which caused the event
				if (event.getCode() == KeyCode.LEFT) {
					// this is for moving left
					// TODO: check if the move is vaild if it's vaild then then
					// call the render.movePlayer(Player) and move the player on
					// screen and in the game
					System.out.println("left");
				} else if (event.getCode() == KeyCode.RIGHT) {
					// this is for moving right
					// TODO: check if the move is vaild if it's vaild then then
					// call the render.movePlayer(Player) and move the player on
					// screen and in the game
					System.out.println("right");
				} else if (event.getCode() == KeyCode.UP) {
					// TODO: check if the move is vaild if it's vaild then then
					// call the render.movePlayer(Player) and move the player on
					// screen and in the game
					// this is for moving up
					System.out.println("up");
				} else if (event.getCode() == KeyCode.DOWN) {
					// TODO: check if the move is vaild if it's vaild then then
					// call the render.movePlayer(Player) and move the player on
					// screen and in the game
					// this is for moving down
					System.out.println("down");
				}

			}
		};
	}

	/**
	 * this this is used to listen for mouse clicks on different controls
	 */
	private void mouseEventHander() {
		mouseEvent = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// Currently this listen to clicks on the items
				// TODO: some how make it work with items
				System.out.println("here");
			}
		};
	}

	// ==========================================================================================================
	//
	// Below this point are all the method related to the render
	//
	// =========================================================================================================

	public static void main(String[] args) {
		new ViewControler(args);
	}
}
