package client.view;

import javafx.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.application.*;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.event.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.WindowEvent;
import javafx.beans.value.*;

import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import client.rendering.Rendering;

/**
 * This class represents the main GUI class this class bring together all the
 * different components of the GUI.
 *
 * @author Dipen
 *
 */
public class GUI extends Application {
	// Point on center of the board
	private String defaultDirection = "up";
	// GUI Style CSS
	private static final String STYLE_CSS = "/main.css";
	// Constants Images
	private static final String GAMEICON_IMAGE = "/game-icon.png";
	private static final String INVENTORY_IMAGE = "/item-tray.png";
	private static final String SLASH_SCREEN_IMAGE = "/night.jpg"; // this will
																	// be
																	// changed
	private static final String AVATAR_ONE_IMAGE = "/standingstillrear.png";

	// Constants Dimensions
	public static final int WIDTH_VALUE = 1000;
	public static final int HEIGHT_VALUE = 700;
	private static final int RIGHTPANE_WIDTH_VALUE = WIDTH_VALUE - 600;
	public static final int GAMEPANE_WIDTH_VALUE = WIDTH_VALUE - 400;

	// main window
	private Stage window;
	// controls
	private MenuBar menuBar;
	private Label timeLable;
	private Label miniMapLable;
	private Label textAreaLable;
	private TextField msg;
	private Button send;
	private Group group = new Group();
	// panes
	// right pane with vertical alligment
	private VBox rightPanel;
	private GridPane itemGrid;
	private GridPane iteminfo;
	// standard layout
	private BorderPane borderPane;
	private String chatText = "HARDD: Welcome Players";
	private StackPane gamePane;
	// Event Handlers
	// for clicks
	private EventHandler<ActionEvent> actionEvent;
	// for keys inputs
	private EventHandler<KeyEvent> keyEvent;
	// for mouse events
	private EventHandler<MouseEvent> mouseEvent;
	// for window resizing not really need else where
	private EventHandler<WindowEvent> windowEvent;

	private Rendering render = new Rendering();

	public static ViewControler viewControler;

	// Button Controls For Slash Screen
	private Button play;
	private Button quit;
	private Button help;
	// Controls for the login Screen
	private Label info;
	private Button login;
	private Button quitLogin;
	private TextField userNameInput;
	private TextField ipInput;
	private TextField portInput;
	private Group avatarGroup;
	private String selectedAvatar;
	// waiting room Controls
	FlowPane playersWaiting;
	private Button beginGame;
	private Button quitWaitingRoom;

	public GUI(ViewControler viewControler) {
		this.viewControler = viewControler;
		// GUI.launch(GUI.class);

	}

	public GUI() {
		// leave this constructor in here need to run the gui.
	}

	/**
	 * this method will get passed in a stage which is the main window and will
	 * start it
	 */
	@Override
	public void start(Stage mainWindow) throws Exception {
		this.window = mainWindow;
		window.setTitle("Plague Game");
		window.getIcons().add(loadImage(GAMEICON_IMAGE));
		// this will disable and enable resizing so when we have a working
		// version we can just set this to false;
		window.setResizable(false);
		// this starts the action listener
		actionEventHandler();
		// this will start the key listener
		keyEventHander();
		// this will start the mouse listener
		mouseEventHander();
		slashScreen();
		window.show();

	}

	public void slashScreen() {
		Group slashGroup = new Group();
		Image slashBackground = loadImage(SLASH_SCREEN_IMAGE);
		ImageView slashBackgroundImage = new ImageView(slashBackground);
		slashBackgroundImage.setFitHeight(HEIGHT_VALUE + 20);
		slashBackgroundImage.setFitWidth(WIDTH_VALUE + 20);
		HBox buttonBox = new HBox(10);
		buttonBox.setLayoutX((WIDTH_VALUE / 2) - (100));
		buttonBox.setLayoutY(HEIGHT_VALUE / 2);
		play = new Button("Play");
		play.setOnAction(actionEvent);
		quit = new Button("Run Away");
		quit.setOnAction(actionEvent);
		help = new Button("Help");
		help.setOnAction(actionEvent);
		buttonBox.getChildren().addAll(play, quit, help);
		slashGroup.getChildren().add(slashBackgroundImage);
		slashGroup.getChildren().add(buttonBox);
		BorderPane slashBorderPane = new BorderPane();
		slashBorderPane.getChildren().add(slashGroup);
		Scene slashScene = new Scene(slashBorderPane, WIDTH_VALUE, HEIGHT_VALUE);
		window.setScene(slashScene);

	}

	public void loginScreen() {
		VBox loginBox = new VBox(5);
		info = new Label();
		info.setText("Enter The IP,Port and UserName");
		loginBox.getChildren().add(info);
		BorderPane loginBorderPane = new BorderPane();

		VBox inputStore = new VBox(5);

		HBox userNameBox = new HBox(3);
		Label user = new Label("Enter UserName");
		userNameInput = new TextField();
		userNameBox.getChildren().addAll(user, userNameInput);
		// loginBox.getChildren().add(userNameBox);

		HBox ipBox = new HBox(3);
		Label ip = new Label("Enter IP Address");
		ipInput = new TextField();
		ipBox.getChildren().addAll(ip, ipInput);
		// loginBox.getChildren().add(ipBox);

		HBox portBox = new HBox(3);
		Label port = new Label("Enter UserName");
		portInput = new TextField();
		portBox.getChildren().addAll(port, portInput);
		// loginBox.getChildren().add(portBox);
		inputStore.getChildren().addAll(userNameBox, ipBox, portBox);
		loginBorderPane.setLeft(inputStore);
		loginBox.getChildren().add(loginBorderPane);
		avatarGroup = new Group();
		Image avatarImg = loadImage(AVATAR_ONE_IMAGE);
		ImageView avatarImage = new ImageView(avatarImg);
		avatarGroup.getChildren().add(avatarImage);
		loginBorderPane.setRight(avatarGroup);
		// TODO: need to add some from of action listener here to change images
		FlowPane buttons = new FlowPane();
		buttons.setHgap(10);
		login = new Button("Login");
		login.setOnAction(actionEvent);
		quitLogin = new Button("Leave");
		quitLogin.setOnAction(actionEvent);
		buttons.getChildren().addAll(login, quitLogin);
		loginBox.getChildren().add(buttons);
		Scene slashScene = new Scene(loginBox, WIDTH_VALUE / 2, HEIGHT_VALUE / 2);
		window.setScene(slashScene);

	}

	public void waitingRoom() {
		VBox waitingRoomBox = new VBox(5);
		Label waitingMsg = new Label();
		waitingMsg.setText(
				"Welome Players! This is the Waiting, You are seeing this room as this game reqires min of 2 players to play. Game Will Start as soon as there are 2 players or more players");
		waitingMsg.setWrapText(true);
		waitingRoomBox.getChildren().add(waitingMsg);
		playersWaiting = new FlowPane();
		playersWaiting.setHgap(10);
		waitingRoomBox.getChildren().add(playersWaiting);
		FlowPane buttons = new FlowPane();
		buttons.setHgap(10);
		beginGame = new Button("Begain");
		beginGame.setOnAction(actionEvent);
		quitWaitingRoom = new Button("Leave Game");
		quitWaitingRoom.setOnAction(actionEvent);
		buttons.getChildren().addAll(beginGame, quitWaitingRoom);
		waitingRoomBox.getChildren().add(buttons);
		Scene slashScene = new Scene(waitingRoomBox, WIDTH_VALUE, HEIGHT_VALUE);
		window.setScene(slashScene);

	}

	public void startGame() {
		// Create a VBox which is just layout manger and adds gap of 10
		rightPanel = new VBox(10);
		rightPanel.setPrefSize(RIGHTPANE_WIDTH_VALUE, HEIGHT_VALUE);
		rightPanel.getStyleClass().add("cotrolvbox");
		borderPane = new BorderPane();
		borderPane.setRight(rightPanel);

		// creates a scene
		setMenuBar();
		setWorldTime();
		setminiMap();
		setchat();
		setItems();

		group.prefWidth(GAMEPANE_WIDTH_VALUE);
		group.prefHeight(HEIGHT_VALUE);

		// Calls the rendering
		render.render(group, defaultDirection);
		group.setLayoutX(3);
		group.setLayoutY(35);
		// only anchor sort of works
		// AnchorPane temp = new AnchorPane();
		// temp.setPrefWidth(GAMEPANE_WIDTH_VALUE);
		// temp.getChildren().add(group);
		borderPane.getChildren().add(group);
		//borderPane.getChildren().add(temp);

		Scene scene = new Scene(borderPane, WIDTH_VALUE, HEIGHT_VALUE);
		scene.getStylesheets().add(this.getClass().getResource(STYLE_CSS).toExternalForm());
		scene.setOnKeyPressed(keyEvent);
		window.setScene(scene);

	}

	/**
	 * this methods will set up the menu bar with all it items
	 */
	private void setMenuBar() {
		// creates the menuBar
		MenuBar menuBar = new MenuBar();
		// creates the menu
		Menu file = new Menu("File");
		// creates the menu items
		MenuItem itmLoad = new MenuItem("Load");
		MenuItem itmSave = new MenuItem("Save");
		MenuItem itmClose = new MenuItem("Close");
		// add the items to menu
		file.getItems().addAll(itmLoad, itmSave, itmClose);
		// creates the menu
		Menu help = new Menu("Help");
		// creates the menu items
		MenuItem itmInfo = new MenuItem("Plague Info");
		MenuItem itmAbout = new MenuItem("About Game");
		// add the items to menu
		help.getItems().addAll(itmInfo, itmAbout);
		// adds menus to menu Bar
		menuBar.getMenus().addAll(file, help);
		// add the layout to the borderPane Layout
		borderPane.setTop(menuBar);

	}

	/**
	 * this method sets up the world clock controls
	 */
	private void setWorldTime() {
		TitledPane titlePane = new TitledPane();
		titlePane.setText("World Time");
		timeLable = new Label();
		titlePane.setContent(timeLable);
		timeLable.setPrefWidth(400);
		timeLable.setPrefHeight(50);
		timeLable.getStyleClass().add("world-time-lable");
		timeLable.setText("00:00");
		rightPanel.getChildren().add(titlePane);
	}

	/**
	 * this method will set up the controls for the mini map of the game
	 */
	public void setminiMap() {
		TitledPane titlePane = new TitledPane();
		titlePane.setText("Mini Map");
		miniMapLable = new Label();
		titlePane.setContent(miniMapLable);
		miniMapLable.setPrefWidth(400);
		miniMapLable.setPrefHeight(370);
		miniMapLable.getStyleClass().add("minimap-lable");
		rightPanel.getChildren().add(titlePane);
	}

	/**
	 * this method will setup the chat controls
	 */
	public void setchat() {
		TitledPane titlePane = new TitledPane();
		VBox chatControls = new VBox(5);
		titlePane.setText("Chat Room");
		chatControls.setPrefWidth(400);
		chatControls.setPrefHeight(200);
		chatControls.getStyleClass().add("chatarea-background");
		textAreaLable = new Label();
		textAreaLable.setAlignment(Pos.TOP_LEFT);
		textAreaLable.setText(chatText);
		textAreaLable.setPrefWidth(400);
		textAreaLable.setPrefHeight(150);
		textAreaLable.getStyleClass().add("chat-display");
		textAreaLable.setWrapText(true);
		HBox hbox = new HBox(5);
		send = new Button("Send");
		send.setOnAction(actionEvent);
		send.setPrefWidth(100);
		send.getStyleClass().add("button-send");
		msg = new TextField();
		msg.setPrefWidth(280);
		msg.setPrefHeight(40);
		hbox.getChildren().add(msg);
		hbox.getChildren().add(send);
		chatControls.getChildren().add(textAreaLable);
		chatControls.getChildren().add(hbox);
		titlePane.setContent(chatControls);

		rightPanel.getChildren().add(titlePane);
	}

	/**
	 * this method will setup the items control
	 */
	public void setItems() {
		TitledPane titlePane = new TitledPane();
		titlePane.setText("Item Inventory");
		HBox hbox = new HBox(5);
		itemGrid = new GridPane();
		hbox.setOnMousePressed(mouseEvent);
		hbox.getStyleClass().add("itempane-background");
		itemGrid.setGridLinesVisible(true);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				Label item = new Label();
				item.getStyleClass().add("item-grid");
				Image img = loadImage(INVENTORY_IMAGE);
				ImageView image = new ImageView();
				image.setFitWidth(60);
				image.setFitHeight(60);
				image.setImage(img);
				item.setGraphic(image);
				GridPane.setRowIndex(item, i);
				GridPane.setColumnIndex(item, j);
				itemGrid.getChildren().add(item);

			}
		}
		// makes the extra box for enlarged item
		hbox.getChildren().add(itemGrid);
		iteminfo = new GridPane();
		iteminfo.setGridLinesVisible(true);
		Label zoomedItem = new Label();
		Image img = loadImage(INVENTORY_IMAGE);
		ImageView image = new ImageView();
		image.setFitWidth(100);
		image.setFitHeight(100);
		image.setImage(img);
		zoomedItem.setGraphic(image);
		GridPane.setRowIndex(zoomedItem, 0);
		GridPane.setColumnIndex(zoomedItem, 0);
		iteminfo.getChildren().add(zoomedItem);
		// makes the extra box for the info of the item
		Label itemDetail = new Label("No Item Currently Selected");
		itemDetail.getStyleClass().add("item-lable");
		GridPane.setRowIndex(itemDetail, 1);
		GridPane.setColumnIndex(itemDetail, 0);
		itemDetail.setWrapText(true);
		itemDetail.setPrefHeight(80);
		iteminfo.getChildren().add(itemDetail);

		hbox.getChildren().add(iteminfo);
		titlePane.setContent(hbox);

		rightPanel.getChildren().add(titlePane);

	}

	/**
	 * this method is used to set the chat message the text area in the gui
	 *
	 * @param text
	 * @param user
	 */
	public void setChatText(String text) {
		chatText = chatText + "\n";
		chatText = chatText +text;
		textAreaLable.setText(chatText);
	}

	/**
	 * this method will return the masg type in the chat box apon clicking the
	 * send button.
	 *
	 * @return
	 */
	public String getChatMsg() {
		// System.out.println(render);
		String msgToSend = msg.getText();
		return msgToSend;
	}

	/**
	 * this method is used to check for action and give a implementation of
	 * handle method
	 */
	private void actionEventHandler() {
		actionEvent = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (event.getSource() == send) {
					viewControler.getChatMsg(getChatMsg());
				} else if (event.getSource() == play) {
					loginScreen();
				} else if (event.getSource() == quit) {
					window.close();
				} else if (event.getSource() == help) {
					// TODO: need to make a help thing which tells the user how
					// to play the game
					System.out.println("help me");
				} else if (event.getSource() == login) {
					// TODO: set secected avatar
					// TODO: check login was correct
					// TODO set the Avatars whicha re all the palyes currently in
					// the waiting room
					waitingRoom();
				} else if (event.getSource() == quitLogin) {
					window.close();
				} else if (event.getSource() == beginGame) {
					// TODO: check that there are 2 > players only start the
					// game if there are else promt a masg and also let the
					// other players know the game is starting
					startGame();
				} else if (event.getSource() == quitWaitingRoom) {
					window.close();
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
				if (event.getCode() == KeyCode.UP) {
					// this is for moving left
					render.render(group, "up");
				} else if (event.getCode() == KeyCode.DOWN) {
					// this is for moving right
					render.render(group, "down");
				} else if (event.getCode() == KeyCode.LEFT) {
					// this is for moving up
					render.render(group, "left");
				} else if (event.getCode() == KeyCode.RIGHT) {
					// this is for moving down
					render.render(group, "right");
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
				System.out.println("here");
			}
		};
	}

	/**
	 * this is a helper method used to load images
	 *
	 * @param name
	 * @return
	 */
	private Image loadImage(String name) {
		Image image = new Image(this.getClass().getResourceAsStream(name));
		return image;
	}

}
