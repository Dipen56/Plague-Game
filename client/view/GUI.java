package client.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import server.game.player.Avatar;
import server.game.player.Direction;
import server.game.player.Player;
import server.game.player.Position;
import server.game.player.Virus;
import client.rendering.Images;
import client.rendering.Rendering;
import client.rendering.Side;

/**
 * This class represents the main GUI class this class bring together all the
 * different components of the GUI.
 *
 * @author Dipen Patel 3000304965
 *
 */
public class GUI extends Application {

	// Application style CSS
	private static final String STYLE_CSS = "/main.css";
	// music file
	private static final String BACKGROUND_MUSIC = "/background.wav";
	/**
	 * Constant width of the window
	 */
	public static final int WIDTH_VALUE = 1000;
	/**
	 * Constant height of the window
	 */
	public static final int HEIGHT_VALUE = 700;
	/**
	 * Constants Width Dimensions for the rendering panel
	 */
	private static final int RIGHTPANE_WIDTH_VALUE = WIDTH_VALUE - 600;
	/**
	 * Constants Width Dimensions for the right panel
	 */
	public static final int GAMEPANE_WIDTH_VALUE = WIDTH_VALUE - 400;
	// miniMap Canvus size
	private static final int MINIMAP_CANVAS_SIZE = 200;

	// Main window for the game
	private static Stage window;
	// controls
	private Label objectNotificationLabel;
	private Canvas miniMapCanvas;
	private Label textAreaLable;
	private TextField chatMessage;

	// Controls for the login Screen
	private TextField userNameInput;
	private TextField ipInput;
	private TextField portInput;
	private Label zoomedItem;
	private Label itemDetail;
	private ProgressBar bar;
	private Text healthBarText;
	private Label virus;
	private Label avatarLable;
	// waiting room Controls
	private Label waitingRoomMessage;

	private Button readyGame;
	private Label objectDescription;
	private Label worldTimeLable;
	// control for menu item;
	private CheckMenuItem descriptionToggle;
	// Layouts
	private Pane pane = new Pane();

	private TitledPane titlePane;
	private VBox rightPanel;
	private GridPane itemGrid;
	private GridPane iteminfo;
	private BorderPane borderPane;
	private StringBuffer chatText;
	// layouts for the login Screen
	private GridPane detail;
	private FlowPane healthPane;

	// for button event
	private EventHandler<ActionEvent> actionEvent;
	// for keys inputs
	private EventHandler<KeyEvent> keyEvent;
	// for mouse events
	private EventHandler<MouseEvent> mouseEvent;

	// this if rotating the avtar image in the login screen
	private static int avatarIndex = 0;
	// this map is used to store the the point of item in the grid which is
	// mapped to there descriptions.
	private Map<Point, String> itemsDescription;
	// this is used to store the list of Avatars which with the avatarIndex is
	// used to change the images in the login screen
	private List<Avatar> avatarList = new ArrayList<Avatar>();

	// this is used to store the controller.
	private static ClientUI controller;
	// this is used to store the rendering object
	private static Rendering rendering;

	/**
	 * Gui Constructor
	 * 
	 * @param viewControler
	 * @param rendering
	 */
	@SuppressWarnings("static-access")
	public GUI(ClientUI viewControler, Rendering rendering) {
		this.controller = viewControler;
		this.rendering = rendering;
		chatText = new StringBuffer();
	}

	public GUI() {
		// this Constructor is used as a place holder to prevent the complier
		// using the parent class constructor by default.
	}

	@Override
	public void start(Stage mainWindow) throws Exception {
		window = mainWindow;
		window.setTitle("Plague Game");
		window.getIcons().add(Images.GAMEICON_IMAGE);
		controller.startListeners();
		activateListners();
		window.setResizable(false);
		slashScreen();
		window.show();
		window.setOnCloseRequest(e -> {
			System.exit(0);
			Platform.exit();
			controller.closeSocket();
		});
	}

	/**
	 * This method will load the slash screen for the game
	 */
	public void slashScreen() {
		// this will create the layout for the slash screen
		Group slashGroup = new Group();
		// load the slash background image
		Image slashBackground = Images.SLASH_SCREEN_IMAGE;
		ImageView slashBackgroundImage = new ImageView(slashBackground);
		slashBackgroundImage.setFitHeight(HEIGHT_VALUE + 18);
		slashBackgroundImage.setFitWidth(WIDTH_VALUE + 18);
		// creates a vertical box layout with 10 gap
		VBox buttonBox = new VBox(10);
		buttonBox.setLayoutX(50);
		buttonBox.setLayoutY(HEIGHT_VALUE / 2 + (50));
		// creates the plays button
		Button play = new Button("Play");
		play.getStyleClass().add("button-slashscreen");
		play.setPrefWidth(200);
		play.setOnAction(actionEvent);
		// creates the quit button
		Button quit = new Button("Run Away");
		quit.getStyleClass().add("button-slashscreen");
		quit.setPrefWidth(200);
		quit.setOnAction(actionEvent);
		// creates the help button
		Button help = new Button("Help");
		help.getStyleClass().add("button-slashscreen");
		help.setPrefWidth(200);
		help.setOnAction(actionEvent);
		// add the the buttons to the layout
		buttonBox.getChildren().addAll(play, quit, help);
		slashGroup.getChildren().add(slashBackgroundImage);
		slashGroup.getChildren().add(buttonBox);
		BorderPane slashBorderPane = new BorderPane();
		slashBorderPane.getChildren().add(slashGroup);
		// lastly add the parent layout to the scene
		Scene slashScene = new Scene(slashBorderPane, WIDTH_VALUE, HEIGHT_VALUE);
		// Attach the CSS file to the scene
		slashScene.getStylesheets().add(this.getClass().getResource(STYLE_CSS).toExternalForm());
		// add the scene to the window
		window.setScene(slashScene);
		window.setOnCloseRequest(e -> {
			System.exit(0);
			Platform.exit();
			controller.closeSocket();
		});
	}

	/**
	 * This method will load the login screen for the game
	 */
	public void loginScreen() {
		// load the avatar images into a list
		avatarList.add(Avatar.Avatar_1);
		avatarList.add(Avatar.Avatar_2);
		avatarList.add(Avatar.Avatar_3);
		avatarList.add(Avatar.Avatar_4);
		// start the action listeners
		activateListners();
		// creates the parent layout
		Pane loginPane = new Pane();
		// load the background image
		Image loginBackground = Images.LOGIN_SCREEN_IMAGE;
		ImageView loginBackgroundImage = new ImageView(loginBackground);
		loginBackgroundImage.setFitHeight(HEIGHT_VALUE + 18);
		loginBackgroundImage.setFitWidth(WIDTH_VALUE + 18);
		loginPane.getChildren().add(loginBackgroundImage);
		// creates the a label for information
		Label info = new Label();
		info.setText("Welcome Players");
		info.getStyleClass().add("login-info");
		loginPane.getChildren().add(info);
		info.setLayoutX(390);

		FlowPane avatar = new FlowPane();
		avatar.setPrefWidth(WIDTH_VALUE);
		// this will store the left button
		BorderPane left = new BorderPane();
		Button prev = new Button("Prev");
		prev.setOnAction(actionEvent);
		prev.setId("PrevAvatar");
		left.setCenter(prev);
		prev.getStyleClass().add("button-login");
		avatarLable = new Label();
		avatarLable.setLayoutX(200);
		Image avatarImg = Images.getAvatarImageBySide(avatarList.get(0), Side.Front, false);
		avatarIndex = 0;
		ImageView avatarImage = new ImageView(avatarImg);
		avatarImage.setFitHeight(300);
		avatarImage.setFitWidth(300);
		avatarLable.setGraphic(avatarImage);
		// this will store the right button
		BorderPane right = new BorderPane();
		Button next = new Button("Next");
		next.setOnAction(actionEvent);
		next.setId("NextAvatar");
		next.getStyleClass().add("button-login");
		right.setCenter(next);
		avatar.getChildren().add(left);
		avatar.getChildren().add(avatarLable);
		avatar.getChildren().add(right);
		loginPane.getChildren().add(avatar);
		avatar.setLayoutX(280);
		avatar.setLayoutY(100);

		VBox inputStore = new VBox(5);
		HBox userNameBox = new HBox(3);
		// this used to retrieve the users name of the player
		Label user = new Label("Enter UserName");
		user.getStyleClass().add("input-login");
		userNameInput = new TextField();
		userNameInput = new TextField();
		userNameBox.getChildren().addAll(user, userNameInput);
		// this is used to retrieve the ip address
		HBox ipBox = new HBox(3);
		Label ip = new Label("Enter IP Address");
		ip.getStyleClass().add("input-login");
		ipInput = new TextField();
		ipBox.getChildren().addAll(ip, ipInput);
		// this is used to set the port number
		HBox portBox = new HBox(3);
		portBox.alignmentProperty().set(Pos.CENTER);
		Label port = new Label("Enter Port");
		port.getStyleClass().add("input-login");
		portInput = new TextField();
		portBox.getChildren().addAll(port, portInput);

		inputStore.getChildren().addAll(userNameBox, ipBox, portBox);
		loginPane.getChildren().add(inputStore);
		inputStore.setLayoutX(350);
		inputStore.setLayoutY(450);

		FlowPane buttons = new FlowPane();
		buttons.alignmentProperty().set(Pos.CENTER);
		buttons.setHgap(10);
		// this will create the login button
		Button login = new Button("Login");
		login.getStyleClass().add("button-login");
		login.setOnAction(actionEvent);
		// this will create the leave button which is used to quit the game.
		Button quitLogin = new Button("Leave");
		quitLogin.getStyleClass().add("button-login");
		quitLogin.setOnAction(actionEvent);
		buttons.getChildren().addAll(login, quitLogin);
		loginPane.getChildren().add(buttons);
		buttons.setLayoutX(300);
		buttons.setLayoutY(580);
		// this will create the scene which the parent layout is added to
		Scene loginScene = new Scene(loginPane, WIDTH_VALUE, HEIGHT_VALUE);
		// this will attach the CSS file the scene
		loginScene.getStylesheets().add(this.getClass().getResource(STYLE_CSS).toExternalForm());
		window.setScene(loginScene);

		window.setOnCloseRequest(e -> {
			System.exit(0);
			Platform.exit();
			controller.closeSocket();
		});
	}

	/**
	 * This method is passed in a value between 0-4 which is then used to change
	 * the avatar image in the login screen.
	 * 
	 * @param change
	 *            -- this is the number from 0-4 which is the avatar number
	 */
	public void changeAvatarImage(int change) {
		avatarIndex = change;
		Image avatarImg = Images.getAvatarImageBySide(avatarList.get(change), Side.Front, false);
		ImageView avatarImage = new ImageView(avatarImg);
		avatarImage.setFitHeight(300);
		avatarImage.setFitWidth(300);
		avatarLable.setGraphic(avatarImage);
	}

	/**
	 * this method will load the waiting room for the game.
	 */
	public void waitingRoom() {
		// this will create the parent layout
		Pane waitingPane = new Pane();
		Image waitingImage = Images.WAITING_ROOM_IMAGE;
		ImageView img = new ImageView(waitingImage);
		img.setFitHeight(HEIGHT_VALUE);
		img.setFitWidth(WIDTH_VALUE);
		waitingPane.getChildren().add(img);
		// Creates the label which is used display a message to the user
		waitingRoomMessage = new Label();
		waitingRoomMessage.setLayoutX(20);
		waitingRoomMessage.setPrefWidth(WIDTH_VALUE);
		waitingRoomMessage.getStyleClass().add("input-login");
		waitingRoomMessage.setText(
				"Welcome! When you are ready to start, click Ready. When the minimum number of players have connected, the game will automatically start.");
		waitingRoomMessage.setWrapText(true);
		waitingPane.getChildren().add(waitingRoomMessage);
		// stores the buttons
		FlowPane buttons = new FlowPane();
		buttons.setLayoutX((WIDTH_VALUE / 2) - 180);
		buttons.setLayoutY(HEIGHT_VALUE - 80);
		buttons.setHgap(10);
		// this will create the ready button
		readyGame = new Button("Ready");
		readyGame.getStyleClass().add("button-login");
		readyGame.setOnAction(actionEvent);
		// this will create the leave game button
		Button quitWaitingRoom = new Button("Leave Game");
		quitWaitingRoom.getStyleClass().add("button-login");
		quitWaitingRoom.setOnAction(actionEvent);
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(readyGame, quitWaitingRoom);
		waitingPane.getChildren().add(buttons);
		// this will create the scene which will store the parent layout
		Scene slashScene = new Scene(waitingPane, WIDTH_VALUE, HEIGHT_VALUE);
		// this will attach CSS file to the scene
		slashScene.getStylesheets().add(this.getClass().getResource(STYLE_CSS).toExternalForm());
		window.setScene(slashScene);
		window.setOnCloseRequest(e -> {
			System.exit(0);
			Platform.exit();
			controller.closeSocket();
		});

	}

	/**
	 * this method is used to disable the ready button once the player has clock
	 * ready in the waiting room.
	 */
	public void disableReadyButton() {
		readyGame.setDisable(true);
		waitingRoomMessage.setText("Waiting for other players...");
	}

	/**
	 * this method will load the game controls and start the render.
	 */
	public void startGame() {
		// Create a VBox which is just layout manger and adds gap of 10
		rightPanel = new VBox(10);
		rightPanel.setPrefSize(RIGHTPANE_WIDTH_VALUE, HEIGHT_VALUE);
		rightPanel.getStyleClass().add("cotrolvbox");
		borderPane = new BorderPane();
		// this will set up the menu bar
		setMenuBar();
		// this will set up the mini map
		setminiMap();
		// this will set up the chat room
		setchat();
		// this will set up the object notification
		setObjectNotification();
		// this will set up the item inventory
		setItems();
		pane.prefWidth(GAMEPANE_WIDTH_VALUE);
		pane.prefHeight(HEIGHT_VALUE);
		// Calls the rendering
		rendering.setGroup(pane);
		pane.setLayoutX(3);
		pane.setLayoutY(35);

		HBox hbox = new HBox(5);
		hbox.getChildren().addAll(pane, rightPanel);
		borderPane.setCenter(hbox);

		Scene scene = new Scene(borderPane, WIDTH_VALUE, HEIGHT_VALUE);
		scene.getStylesheets().add(this.getClass().getResource(STYLE_CSS).toExternalForm());
		startMusic();
		scene.setOnKeyPressed(keyEvent);
		window.setScene(scene);
		window.setOnCloseRequest(e -> {
			System.exit(0);
			Platform.exit();
			controller.closeSocket();
		});
	}

	/**
	 * this method creates the health bar and adds it to the pane.
	 * 
	 * @param health
	 *            -- this is a double from INVENTORY_SIZE
	 * @param virusName
	 *            -- this is the userName of the player
	 */
	public void setHealthBar(double health, Virus virusName, String userName, Avatar avatar) {
		// this will create a flow pane which will store the player avatar and
		// Their health and other information
		healthPane = new FlowPane();
		healthPane.setHgap(2);
		healthPane.setPrefHeight(50);
		healthPane.setPrefWidth(200);
		healthPane.setLayoutX(10);
		healthPane.setLayoutY(10);

		Image avatarImg = Images.PROFILE_IMAGES.get(avatar);
		ImageView avatarImage = new ImageView(avatarImg);
		avatarImage.setFitHeight(60);
		avatarImage.setFitWidth(50);
		healthPane.getChildren().add(avatarImage);
		VBox healthBox = new VBox(2);

		StackPane barPlusNum = new StackPane();

		bar = new ProgressBar(health);
		bar.setPrefWidth(148);

		healthBarText = new Text();
		healthBarText.setText(String.valueOf(Player.MAX_HEALTH));

		barPlusNum.getChildren().setAll(bar, healthBarText);

		virus = new Label();
		virus.setText(" UserName: " + userName + "\n Virus Type: " + virusName.toString());
		virus.setWrapText(true);
		virus.setPrefWidth(148);
		virus.getStyleClass().add("virus-label");
		healthBox.getChildren().add(barPlusNum);
		healthBox.getChildren().add(virus);
		healthPane.getChildren().add(healthBox);
		// this will add the parent layout to the same pane used my rendering
		pane.getChildren().add(healthPane);
	}

	/**
	 * This method is passed int which is the players current health which is
	 * then used to display the amount of health they have remaining in the
	 * progress bar.
	 * 
	 * @param health
	 *            -- this is the current health of the player thats less then
	 *            INVENTORY_SIZE
	 */
	public void updateHealth(int health) {
		bar.progressProperty().set(((double) health / Player.MAX_HEALTH));
		healthBarText.setText(String.valueOf(health));
		pane.getChildren().add(healthPane);
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
		itmLoad.setId("LoadMenu");
		itmLoad.setOnAction(actionEvent);
		MenuItem itmSave = new MenuItem("Save");
		itmSave.setId("SaveMenu");
		itmSave.setOnAction(actionEvent);

		// description toggle menu item
		descriptionToggle = new CheckMenuItem("Description");
		descriptionToggle.setId("Description");
		descriptionToggle.setOnAction(actionEvent);
		descriptionToggle.selectedProperty().setValue(true);

		MenuItem itmClose = new MenuItem("Close");
		itmClose.setId("CloseMenu");
		itmClose.setOnAction(actionEvent);
		// add the items to menu
		file.getItems().addAll(itmLoad, itmSave, descriptionToggle, itmClose);
		// creates the menu
		Menu help = new Menu("Help");
		// creates the menu items
		MenuItem itmShortcut = new MenuItem("Keyboard shortcuts");
		itmShortcut.setId("InfoMenu");
		itmShortcut.setOnAction(actionEvent);
		MenuItem itmHowToPlay = new MenuItem("How to play");
		itmHowToPlay.setId("AboutMenu");
		itmHowToPlay.setOnAction(actionEvent);
		// add the items to menu
		help.getItems().addAll(itmShortcut, itmHowToPlay);
		// adds menus to menu Bar
		menuBar.getMenus().addAll(file, help);
		// add the layout to the borderPane Layout
		borderPane.setTop(menuBar);
	}

	/**
	 * this method sets up the world clock controls
	 */
	private void setObjectNotification() {
		TitledPane titlePane = new TitledPane();
		titlePane.setText("Object Notification");
		objectNotificationLabel = new Label();
		titlePane.setContent(objectNotificationLabel);
		objectNotificationLabel.setPrefWidth(400);
		objectNotificationLabel.setPrefHeight(50);
		objectNotificationLabel.getStyleClass().add("world-time-lable");
		rightPanel.getChildren().add(titlePane);
	}

	/**
	 * this method will set up the controls for the mini map of the game
	 */
	public void setminiMap() {
		TitledPane titlePane = new TitledPane();
		titlePane.setText("Mini Map");

		miniMapCanvas = new Canvas(MINIMAP_CANVAS_SIZE, MINIMAP_CANVAS_SIZE);
		miniMapCanvas.layoutXProperty().set(5);

		BorderPane minimapLable = new BorderPane();
		minimapLable.setCenter(miniMapCanvas);
		minimapLable.getStyleClass().add("minimap-lable");
		titlePane.setContent(minimapLable);

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
		ScrollPane scrollPane = new ScrollPane();

		scrollPane.setPrefHeight(150);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		textAreaLable = new Label();
		textAreaLable.setAlignment(Pos.TOP_LEFT);
		textAreaLable.setText(chatText.toString());
		textAreaLable.setPrefWidth(400);
		textAreaLable.setPrefHeight(150);
		textAreaLable.getStyleClass().add("chat-display");
		textAreaLable.setWrapText(true);
		scrollPane.setContent(textAreaLable);

		HBox hbox = new HBox(5);
		Button send = new Button("Send");
		send.setOnAction(actionEvent);
		send.setPrefWidth(100);
		send.getStyleClass().add("button-send");
		chatMessage = new TextField();
		chatMessage.setPrefWidth(280);
		chatMessage.setPrefHeight(40);
		hbox.getChildren().add(chatMessage);
		hbox.getChildren().add(send);
		chatControls.getChildren().add(scrollPane);
		chatControls.getChildren().add(hbox);
		titlePane.setContent(chatControls);
		// this will add the parent layout to the right panel layout
		rightPanel.getChildren().add(titlePane);
	}

	/**
	 * this method will setup the items control
	 */
	public void setItems() {
		titlePane = new TitledPane();

		titlePane.setText("Item Inventory");

		VBox itemContainer = new VBox(5);
		HBox hbox = new HBox(5);
		itemGrid = new GridPane();
		itemGrid.setOnMouseMoved(mouseEvent);
		itemGrid.setOnMousePressed(mouseEvent);
		itemContainer.getStyleClass().add("itempane-background");
		itemGrid.setGridLinesVisible(true);

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				Label item = new Label();
				item.getStyleClass().add("item-grid");
				Image img = Images.INVENTORY_IMAGE;
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
		zoomedItem = new Label();
		Image img = Images.INVENTORY_IMAGE;
		ImageView image = new ImageView();
		image.setFitWidth(120);
		image.setFitHeight(120);
		image.setImage(img);
		zoomedItem.setGraphic(image);
		GridPane.setRowIndex(zoomedItem, 0);
		GridPane.setColumnIndex(zoomedItem, 0);
		iteminfo.getChildren().add(zoomedItem);
		hbox.getChildren().add(iteminfo);

		// makes the extra box for the info of the item
		// info stuff
		detail = new GridPane();
		detail.setGridLinesVisible(true);
		itemDetail = new Label("No Item Currently Selected");
		itemDetail.getStyleClass().add("item-lable");
		GridPane.setRowIndex(itemDetail, 0);
		GridPane.setColumnIndex(itemDetail, 0);
		itemDetail.setWrapText(true);
		itemDetail.setPrefHeight(100);
		itemDetail.setPrefWidth(375);
		detail.getChildren().add(itemDetail);
		// hbox.getChildren().add(detail);
		itemContainer.getChildren().add(hbox);
		itemContainer.getChildren().add(detail);

		titlePane.setContent(itemContainer);
		rightPanel.getChildren().add(titlePane);
	}

	/**
	 * this method is used to set the chat message the text area in the gui
	 *
	 * @param text
	 *            -- this is the chat message to display to the player
	 */
	public void setChatText(String text) {
		// don't keep the message too long.
		int length = chatText.length();
		if (length > 8000) {
			chatText.delete(0, length - 3000);
		}
		// append the new chat
		chatText.append(text);
		chatText.append('\n');
		// set the new text
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textAreaLable.setText(chatText.toString());
			}
		});
	}

	/**
	 * this method will return the massage typed in the chat box upon clicking
	 * the send button.
	 *
	 * @return
	 */
	public String getChatMsg() {
		String msgToSend = chatMessage.getText();
		chatMessage.clear();
		return msgToSend;
	}

	/**
	 * This method will return the user name.
	 *
	 * @return
	 */
	public String getUserName() {
		String name = userNameInput.getText();
		return name;
	}

	/**
	 * This method will return the IP address as a string
	 *
	 * @return
	 */
	public String getIpAddress() {
		String ipAddress = ipInput.getText();
		return ipAddress;
	}

	/**
	 * This method will return the port number as a String
	 *
	 * @return
	 */
	public String getPortString() {
		String portStr = portInput.getText();
		return portStr;
	}

	/**
	 * This method will return the avatar index. Note that it's 0-indexed.
	 *
	 * @return
	 */
	public int getAvatarIndex() {
		return avatarIndex;
	}

	/**
	 * Is the description toggle on
	 * 
	 * @return
	 */
	public boolean isDescriptionOn() {
		return descriptionToggle.selectedProperty().getValue();
	}

	/**
	 * Set the description toggle as the given boolean value
	 * 
	 * @param isToggleOn
	 *            --boolean of rather it on or not
	 */
	public void setDescriptionOn(boolean isToggleOn) {
		this.descriptionToggle.selectedProperty().setValue(isToggleOn);
	}

	/**
	 * this method will set the world time
	 * 
	 * @param worldTime
	 *            -- object description
	 */
	public void setObjectNotification(String objectDiscription) {
		objectNotificationLabel.setText(objectDiscription);
	}

	/**
	 * this method will return the window
	 *
	 * @return
	 */
	public Stage getWindow() {
		return window;
	}

	/**
	 * This method draws a minimap on the minimap panel.
	 * 
	 * @param playerLoc
	 *            --- player's location
	 * @param uId
	 *            --- user id.
	 * @param areaMap
	 *            --- the current area map as a char[][]
	 * @param visibility
	 *            --- the visibility
	 * @param positions
	 *            --- a collection of every player's location.
	 * 
	 * @author Hector (Fang Zhao 300364061)
	 */
	public void updateMinimap(Position playerLoc, int uId, char[][] areaMap, int visibility,
			Map<Integer, Position> positions) {
		// player's coordinate on board, and direction.
		Position selfPosition = positions.get(uId);
		int selfAreaId = selfPosition.areaId;
		int selfX = selfPosition.x;
		int selfY = selfPosition.y;
		Direction selDir = selfPosition.getDirection();
		// the width of height of current map
		int width = areaMap[0].length;
		int height = areaMap.length;

		// set up the canvas
		GraphicsContext gc = miniMapCanvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(1);

		// clear the old drawing
		gc.setFill(Color.rgb(50, 54, 57));
		gc.fillRect(0, 0, MINIMAP_CANVAS_SIZE, MINIMAP_CANVAS_SIZE);

		// calculate the four boundaries
		int bound_top = selfY - visibility < 0 ? 0 : selfY - visibility;
		int bound_bottom = selfY + visibility + 1 > height ? height : selfY + visibility + 1;
		int bound_left = selfX - visibility < 0 ? 0 : selfX - visibility;
		int bound_right = selfX + visibility + 1 > width ? width : selfX + visibility + 1;

		int divider = (bound_bottom - bound_top) > (bound_right - bound_left) ? bound_bottom - bound_top
				: bound_right - bound_left;
		double size = MINIMAP_CANVAS_SIZE / (divider);

		// draw the minimap
		Color color = null;
		for (int row = bound_top; row < bound_bottom; row++) {
			for (int col = bound_left; col < bound_right; col++) {
				color = Images.MINIMAP_COLOR_TABLE.get(areaMap[row][col]);
				if (color == null) {
					// This is an unknown character/MapElement, shouldn't happen
					color = Color.BLACK;
				}
				gc.setFill(color);
				// draw the map elements
				gc.fillRect((col - bound_left) * size, (row - bound_top) * size, size, size);
				gc.strokeRect((col - bound_left) * size, (row - bound_top) * size, size, size);
			}
		}
		// draw player arrow on map
		for (Position p : positions.values()) {
			// if this player is not in the same map,skip.
			if (p.areaId != selfAreaId) {
				continue;
			}
			// this player's x, y, and direction
			int x = p.x;
			int y = p.y;
			Direction dir = p.getDirection();
			// if this player isn't within visible distance, skip
			if (Math.abs(x - selfX) > visibility || Math.abs(y - selfY) > visibility) {
				continue;
			}
			Image img;
			if (x == selfX && y == selfY) {
				// it's yourself
				img = Images.GREEN_ARROW.get(selDir);
			} else {
				// it's your enemy
				img = Images.RED_ARROW.get(dir);
			}

			// draw arrows for players
			gc.drawImage(img, (x - bound_left) * size, (y - bound_top) * size, size, size);
		}

	}

	/**
	 * this method is to display the inventory items the that the player is
	 * currently holding given a List if string which is the item letter and it
	 * description.
	 */
	public void setInventory(List<String> inventory) {
		itemsDescription = new HashMap<Point, String>();
		int row = 0;
		int col = 0;
		itemGrid.getChildren().clear();

		for (String s : inventory) {
			Image image = null;
			if (s.startsWith("A")) {
				image = Images.ITEM_IMAGES.get('A');
			} else if (s.startsWith("K")) {
				image = Images.ITEM_IMAGES.get('K');
			} else if (s.startsWith("T")) {
				image = Images.ITEM_IMAGES.get('T');
			} else if (s.startsWith("B")) {
				image = Images.ITEM_IMAGES.get('B');
			}
			ImageView imageView = new ImageView(image);
			Label item = new Label();
			item.getStyleClass().add("item-grid");
			imageView.setFitWidth(60);
			imageView.setFitHeight(60);
			imageView.setImage(image);
			item.setGraphic(imageView);

			GridPane.setRowIndex(item, row);
			GridPane.setColumnIndex(item, col);
			itemGrid.getChildren().add(item);
			itemsDescription.put(new Point(col, row), s);
			col++;
			if (col == 4) {
				col = 0;
				row++;
			}
		}
		// this will fill in the empty space where there are not items
		for (int i = row; i < 2; i++) {
			for (int j = col; j < 4; j++) {
				Label item = new Label();
				item.getStyleClass().add("item-grid");
				Image img = Images.INVENTORY_IMAGE;
				ImageView image = new ImageView();
				image.setFitWidth(60);
				image.setFitHeight(60);
				image.setImage(img);
				item.setGraphic(image);
				GridPane.setRowIndex(item, i);
				GridPane.setColumnIndex(item, j);
				itemsDescription.put(new Point(j, i), "N|none");
				itemGrid.getChildren().add(item);
			}
			col = 0;
		}
		itemGrid.setGridLinesVisible(true);
	}

	/**
	 * This method is used to give a more detail about the item the player is
	 * hovering over currently.
	 * 
	 * @param x
	 *            -- x grid value
	 * @param y
	 *            -- y grid value
	 */
	public void setItemDescription(int x, int y) {
		String item = null;
		for (Point p : itemsDescription.keySet()) {
			if (p.x == x && p.y == y) {
				item = itemsDescription.get(p);
				break;
			}
		}
		// no items yet
		if (item == null) {
			return;
		}
		String description = item.substring(2, item.length());
		Image img = null;
		if (item.startsWith("A")) {
			img = Images.ITEM_IMAGES.get('A');
			itemDetail.setText(description);
		} else if (item.startsWith("K")) {
			img = Images.ITEM_IMAGES.get('K');
			itemDetail.setText(description);
		} else if (item.startsWith("T")) {
			img = Images.ITEM_IMAGES.get('T');
			itemDetail.setText(description);
		} else if (item.startsWith("B")) {
			img = Images.ITEM_IMAGES.get('B');
			itemDetail.setText(description);
		} else {
			img = Images.INVENTORY_IMAGE;
			itemDetail.setText("No Item Currently Selected");
		}

		ImageView image = new ImageView();
		image.setFitWidth(120);
		image.setFitHeight(120);
		image.setImage(img);
		zoomedItem.setGraphic(image);
	}

	/**
	 * this method is used to get the item description given the two int x and y
	 * of the which is the grid the player click on.
	 * 
	 * @param x
	 *            -- x grid value
	 * @param y
	 *            -- y grid value
	 * @return
	 */
	public String getItemDescription(int x, int y) {
		String item = null;
		for (Point p : itemsDescription.keySet()) {
			if (p.x == x && p.y == y) {
				item = itemsDescription.get(p);
				break;
			}
		}
		return item;
	}

	/**
	 * This method is used to create the label which is used to display object
	 * description to the player.
	 */
	public void objectLabel() {
		objectDescription = new Label();
		objectDescription.setWrapText(true);
		objectDescription.setPrefWidth(150);
		objectDescription.setLayoutX((GAMEPANE_WIDTH_VALUE / 2) - 20);
		objectDescription.setLayoutY(HEIGHT_VALUE - 170);
		objectDescription.getStyleClass().add("object-description");
	}

	/**
	 * This method is used to create the right click pop up menu.
	 */
	public void keyRightClickOption() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem item1 = new MenuItem("Insert");
		item1.setId("right-insert");
		item1.setOnAction(actionEvent);
		MenuItem item2 = new MenuItem("Use");
		item2.setId("right-use");
		item2.setOnAction(actionEvent);
		contextMenu.getItems().addAll(item1, item2);
		titlePane.setContextMenu(contextMenu);
	}

	/**
	 * this method is used to create right click menu which is got a more item
	 * "Drop"
	 */
	public void antidoteRightClickOption() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem item1 = new MenuItem("Insert");
		item1.setId("right-insert");
		item1.setOnAction(actionEvent);
		MenuItem item2 = new MenuItem("Use");
		item2.setId("right-use");
		item2.setOnAction(actionEvent);
		MenuItem item3 = new MenuItem("Drop");
		item3.setId("drop-use");
		item3.setOnAction(actionEvent);
		contextMenu.getItems().addAll(item1, item2, item3);
		titlePane.setContextMenu(contextMenu);
	}

	/**
	 * This method is used to clear the right click pop up
	 */
	public void rightClickClear() {
		titlePane.setContextMenu(null);
	}

	/**
	 * This method is used to display to the user the objects description.
	 * 
	 * @param description
	 *            -- string with object detail
	 */
	public void displayObjectDescription(String description) {
		objectDescription.setText(description);
		pane.getChildren().add(objectDescription);
	}

	/**
	 * this method is used to set the world time
	 */
	public void setWorldTime() {
		worldTimeLable = new Label();
		worldTimeLable.setWrapText(true);
		worldTimeLable.setPrefWidth(150);
		worldTimeLable.setLayoutX((GAMEPANE_WIDTH_VALUE / 2) - 20);
		worldTimeLable.setLayoutY(30);
		worldTimeLable.getStyleClass().add("object-description");
	}

	/**
	 * this method is used to display the world time give a string which is the
	 * time
	 * 
	 * @param time
	 *            -- string of the world time
	 */
	public void displayTime(String time) {
		worldTimeLable.setText(time);
		pane.getChildren().add(worldTimeLable);
	}

	/**
	 * This static helper method will pop up a message dialog to user.
	 *
	 * @param msg
	 *            -- message for the popup box
	 * @param title
	 *            -- title for the popup box
	 */
	public static void showMsgPane(String title, String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AlertBox.displayMsg(title, msg);
			}
		});
	}

	/**
	 * this method is used to start the background music used thought out the
	 * game.
	 */
	public void startMusic() {
		new Thread() {
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream ais = AudioSystem
							.getAudioInputStream(this.getClass().getResource(BACKGROUND_MUSIC));
					clip.open(ais);
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * this method is used to play different sound effect during the game
	 * 
	 * @param file
	 *            --sound file name
	 */
	public void soundEffect(String file) {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(this.getClass().getResource(file));
			clip.open(ais);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is used to get the event listeners from the controller.
	 */
	private void activateListners() {
		actionEvent = controller.getActionEventHandler();
		keyEvent = controller.getKeyEventHander();
		mouseEvent = controller.getMouseEventHander();
	}

}
