package client.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * @author Dipen
 *
 */
public class GUI extends Application {

	// GUI Style CSS
	private static final String STYLE_CSS = "/main.css";
	// Constants Dimensions
	public static final int WIDTH_VALUE = 1000;
	public static final int HEIGHT_VALUE = 700;
	private static final int RIGHTPANE_WIDTH_VALUE = WIDTH_VALUE - 600;
	public static final int GAMEPANE_WIDTH_VALUE = WIDTH_VALUE - 400;
	/**
	 * Minimap height and width
	 */
	private static final int MINIMAP_CANVAS_SIZE = 200;

	/**
	 * mini map color table
	 */
	private static final Map<Character, Color> MINIMAP_COLOR_TABLE;
	static {
		MINIMAP_COLOR_TABLE = new HashMap<>();
		// ========== obstacles: Grey, Rock, Barrel, Table ===========
		// Rock
		MINIMAP_COLOR_TABLE.put('R', Color.rgb(83, 86, 102, 1.0));
		// Barrel
		MINIMAP_COLOR_TABLE.put('B', Color.rgb(83, 86, 102, 1.0));
		// Table
		MINIMAP_COLOR_TABLE.put('A', Color.rgb(83, 86, 102, 1.0));
		// Chair
		MINIMAP_COLOR_TABLE.put('H', Color.rgb(83, 86, 102, 1.0));
		// ===== Containers: golden, chest, cupboard, scrap pile =====
		// Chest
		MINIMAP_COLOR_TABLE.put('C', Color.rgb(255, 170, 37, 1.0));
		// Cupboard
		MINIMAP_COLOR_TABLE.put('U', Color.rgb(255, 170, 37, 1.0));
		// Scrap pile
		MINIMAP_COLOR_TABLE.put('P', Color.rgb(255, 170, 37, 1.0));
		// ============== Tree or ground: green ======================
		// Tree, dark grenn
		MINIMAP_COLOR_TABLE.put('T', Color.rgb(68, 170, 58, 1.0));
		// Ground, light green
		MINIMAP_COLOR_TABLE.put('G', Color.rgb(200, 236, 204, 1.0));
		// Door space, this is just ground
		MINIMAP_COLOR_TABLE.put('D', Color.rgb(200, 236, 204, 1.0));
		// =========== Room obstacles: blue ====================
		// Room obstacles
		MINIMAP_COLOR_TABLE.put('E', Color.rgb(19, 137, 245, 1.0));
	}
	// main window
	private static Stage window;
	// controls
	private Label timeLable;
	// private Label miniMapLable;
	private Canvas miniMapCanvas;
	private Label textAreaLable;
	private TextField msg;
	private Button send;
	// private Group group = new Group();
	private Pane group = new Pane();
	// panes
	// right pane with vertical alligment
	private VBox rightPanel;
	private GridPane itemGrid;
	private GridPane iteminfo;
	// standard layout
	private BorderPane borderPane;
	private StringBuffer chatText;
	// private Rendering render = new Rendering();
	private static ClientUI viewControler;
	// private static Rendering render;
	private static Rendering render;
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
	private Label zoomedItem;
	private Label itemDetail;
	private ProgressBar bar;
	private Text healthBarText;
	private Label virus;
	private FlowPane healthPane;
	private Label avatarLable;
	private GridPane detail;
	// waiting room Controls
	private Label waitingMsg;
	private FlowPane playersWaiting;
	private Button readyGame;
	private Button quitWaitingRoom;
	private Label objectMessage;
	private FadeTransition ft;
	// this is for event
	// for action events
	private EventHandler<ActionEvent> actionEvent;
	// for keys inputs
	private EventHandler<KeyEvent> keyEvent;
	// for mouse events
	private EventHandler<MouseEvent> mouseEvent;
	// for window resizing not really need else where
	private EventHandler<WindowEvent> windowEvent;
	private static int avatarIndex = 0;
	private Map<Point, String> itemsDescription;

	private List<Avatar> avatarList = new ArrayList<Avatar>();

	@SuppressWarnings("static-access")
	public GUI(ClientUI viewControler, Rendering rendering) {
		this.viewControler = viewControler;
		this.render = rendering;
		chatText = new StringBuffer();
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
		window = mainWindow;
		window.setTitle("Plague Game");
		window.getIcons().add(Images.GAMEICON_IMAGE);
		// this will disable and enable resizing so when we have a working
		// version we can just set this to false;
		// this starts the action listener
		viewControler.startListeners();
		actionEvent = viewControler.getActionEventHandler();
		keyEvent = viewControler.getKeyEventHander();
		mouseEvent = viewControler.getMouseEventHander();
		windowEvent = viewControler.getWindowEventHander();
		window.setResizable(false);
		slashScreen();
		// loginScreen() ;
		window.show();
		window.setOnCloseRequest(e -> Platform.exit());
		window.setOnCloseRequest(e -> System.exit(0));
	}

	public void slashScreen() {
		Group slashGroup = new Group();
		Image slashBackground = Images.SLASH_SCREEN_IMAGE;
		ImageView slashBackgroundImage = new ImageView(slashBackground);
		slashBackgroundImage.setFitHeight(HEIGHT_VALUE + 18);
		slashBackgroundImage.setFitWidth(WIDTH_VALUE + 18);
		VBox buttonBox = new VBox(10);
		buttonBox.setLayoutX(50);
		buttonBox.setLayoutY(HEIGHT_VALUE / 2 + (50));
		play = new Button("Play");
		play.getStyleClass().add("button-slashscreen");
		play.setPrefWidth(200);
		play.setOnAction(actionEvent);
		quit = new Button("Run Away");
		quit.getStyleClass().add("button-slashscreen");
		quit.setPrefWidth(200);
		quit.setOnAction(actionEvent);
		help = new Button("Help");
		help.getStyleClass().add("button-slashscreen");
		help.setPrefWidth(200);
		help.setOnAction(actionEvent);
		buttonBox.getChildren().addAll(play, quit, help);
		slashGroup.getChildren().add(slashBackgroundImage);
		slashGroup.getChildren().add(buttonBox);
		BorderPane slashBorderPane = new BorderPane();
		slashBorderPane.getChildren().add(slashGroup);
		Scene slashScene = new Scene(slashBorderPane, WIDTH_VALUE, HEIGHT_VALUE);
		slashScene.getStylesheets().add(this.getClass().getResource(STYLE_CSS).toExternalForm());
		window.setScene(slashScene);
		window.setOnCloseRequest(e -> Platform.exit());
		window.setOnCloseRequest(e -> System.exit(0));
	}

	public void loginScreen() {
		avatarList.add(Avatar.Avatar_1);
		avatarList.add(Avatar.Avatar_2);
		avatarList.add(Avatar.Avatar_3);
		avatarList.add(Avatar.Avatar_4);
		actionEvent = viewControler.getActionEventHandler();
		keyEvent = viewControler.getKeyEventHander();
		mouseEvent = viewControler.getMouseEventHander();
		windowEvent = viewControler.getWindowEventHander();
		Pane loginPane = new Pane();
		Image loginBackground = Images.LOGIN_SCREEN_IMAGE;
		ImageView loginBackgroundImage = new ImageView(loginBackground);
		loginBackgroundImage.setFitHeight(HEIGHT_VALUE + 18);
		loginBackgroundImage.setFitWidth(WIDTH_VALUE + 18);
		loginPane.getChildren().add(loginBackgroundImage);
		info = new Label();
		info.setText("Welcome Players");
		info.getStyleClass().add("login-info");
		loginPane.getChildren().add(info);
		info.setLayoutX(390);
		FlowPane avatar = new FlowPane();
		avatar.setPrefWidth(WIDTH_VALUE);
		BorderPane left = new BorderPane();
		Button prev = new Button("Prev");
		prev.setOnAction(actionEvent);
		prev.setId("PrevAvatar");
		left.setCenter(prev);
		prev.getStyleClass().add("button-login");
		avatarLable = new Label();
		avatarLable.setLayoutX(200);
		Image avatarImg = Images.getAvatarImageBySide(avatarList.get(0), Side.Front);
		avatarIndex = 0;
		ImageView avatarImage = new ImageView(avatarImg);
		avatarImage.setFitHeight(300);
		avatarImage.setFitWidth(300);
		avatarLable.setGraphic(avatarImage);
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
		Label user = new Label("Enter UserName");
		user.getStyleClass().add("input-login");
		userNameInput = new TextField();
		userNameInput = new TextField();
		userNameBox.getChildren().addAll(user, userNameInput);
		HBox ipBox = new HBox(3);
		Label ip = new Label("Enter IP Address");
		ip.getStyleClass().add("input-login");
		ipInput = new TextField();
		ipBox.getChildren().addAll(ip, ipInput);
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
		login = new Button("Login");
		login.getStyleClass().add("button-login");
		login.setOnAction(actionEvent);
		quitLogin = new Button("Leave");
		quitLogin.getStyleClass().add("button-login");
		quitLogin.setOnAction(actionEvent);
		buttons.getChildren().addAll(login, quitLogin);
		loginPane.getChildren().add(buttons);
		buttons.setLayoutX(300);
		buttons.setLayoutY(580);

		Scene loginScene = new Scene(loginPane, WIDTH_VALUE, HEIGHT_VALUE);
		loginScene.getStylesheets().add(this.getClass().getResource(STYLE_CSS).toExternalForm());
		window.setScene(loginScene);
		window.setOnCloseRequest(e -> Platform.exit());
		window.setOnCloseRequest(e -> System.exit(0));
	}

	public void changeAvatarImage(int change) {
		avatarIndex = change;
		Image avatarImg = Images.getAvatarImageBySide(avatarList.get(change), Side.Front);
		ImageView avatarImage = new ImageView(avatarImg);
		avatarImage.setFitHeight(300);
		avatarImage.setFitWidth(300);
		avatarLable.setGraphic(avatarImage);
	}

	public void waitingRoom() {
		VBox waitingRoomBox = new VBox(5);
		waitingMsg = new Label();
		waitingMsg.setText(
				"Welcome! When you are ready to start, click Ready. When the minimum number of players have connected, the game will automatically start.");
		waitingMsg.setWrapText(true);
		waitingRoomBox.getChildren().add(waitingMsg);
		playersWaiting = new FlowPane();
		playersWaiting.setHgap(10);
		waitingRoomBox.getChildren().add(playersWaiting);
		FlowPane buttons = new FlowPane();
		buttons.setHgap(10);
		readyGame = new Button("Ready");
		readyGame.setOnAction(actionEvent);
		quitWaitingRoom = new Button("Leave Game");
		quitWaitingRoom.setOnAction(actionEvent);
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(readyGame, quitWaitingRoom);
		waitingRoomBox.getChildren().add(buttons);
		Scene slashScene = new Scene(waitingRoomBox, 400, 170);
		window.setScene(slashScene);
		window.setOnCloseRequest(e -> Platform.exit());
		window.setOnCloseRequest(e -> System.exit(0));

	}

	public void disableReadyButton() {
		readyGame.setDisable(true);
		waitingMsg.setText("Waiting for other players...");
	}

	public void startGame() {
		// Create a VBox which is just layout manger and adds gap of 10
		rightPanel = new VBox(10);
		rightPanel.setPrefSize(RIGHTPANE_WIDTH_VALUE, HEIGHT_VALUE);
		rightPanel.getStyleClass().add("cotrolvbox");
		borderPane = new BorderPane();
		setMenuBar();
		setWorldTime();
		setminiMap();
		setchat();
		setItems();
		group.prefWidth(GAMEPANE_WIDTH_VALUE);
		group.prefHeight(HEIGHT_VALUE);
		// Calls the rendering
		render.setGroup(group);
		// render.renderNorth();
		group.setLayoutX(3);
		group.setLayoutY(35);
		HBox hbox = new HBox(5);
		hbox.getChildren().addAll(group, rightPanel);
		borderPane.setCenter(hbox);
		Scene scene = new Scene(borderPane, WIDTH_VALUE, HEIGHT_VALUE);
		scene.getStylesheets().add(this.getClass().getResource(STYLE_CSS).toExternalForm());
		scene.setOnKeyPressed(keyEvent);
		window.setOnCloseRequest(windowEvent);
		window.setScene(scene);
		window.setOnCloseRequest(e -> Platform.exit());
		window.setOnCloseRequest(e -> System.exit(0));
	}

	/**
	 * this method creats the health bar and adds it to the pane.
	 * 
	 * @param health
	 * @param virusName
	 */
	public void setHealthBar(double health, Virus virusName) {
		healthPane = new FlowPane();
		healthPane.setHgap(2);
		healthPane.setPrefHeight(50);
		healthPane.setPrefWidth(150);
		healthPane.setLayoutX(10);
		healthPane.setLayoutY(10);
		// TODO link it to the avatar image using avatar index upto
		Image avatar = Images.SLASH_SCREEN_IMAGE;
		ImageView avatarImage = new ImageView(avatar);
		avatarImage.setFitHeight(60);
		avatarImage.setFitWidth(50);
		healthPane.getChildren().add(avatarImage);
		VBox healthBox = new VBox(2);

		StackPane barPlusNum = new StackPane();

		bar = new ProgressBar(health);
		bar.setPrefWidth(98);

		healthBarText = new Text();
		healthBarText.setText(String.valueOf(Player.MAX_HEALTH));

		barPlusNum.getChildren().setAll(bar, healthBarText);

		virus = new Label();
		virus.setText("Virus Type: " + virusName.toString());
		virus.setWrapText(true);
		virus.setPrefWidth(98);
		virus.getStyleClass().add("virus-label");
		healthBox.getChildren().add(barPlusNum);
		healthBox.getChildren().add(virus);
		healthPane.getChildren().add(healthBox);
		group.getChildren().add(healthPane);
	}

	public void updateHealth(int health) {
		bar.progressProperty().set((double) ((double) health / Player.MAX_HEALTH));
		healthBarText.setText(String.valueOf(health));
		group.getChildren().add(healthPane);
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
		MenuItem itmClose = new MenuItem("Close");
		itmClose.setId("CloseMenu");
		itmClose.setOnAction(actionEvent);
		// add the items to menu
		file.getItems().addAll(itmLoad, itmSave, itmClose);
		// creates the menu
		Menu help = new Menu("Help");
		// creates the menu items
		MenuItem itmInfo = new MenuItem("Plague Info");
		itmInfo.setId("InfoMenu");
		itmInfo.setOnAction(actionEvent);
		MenuItem itmAbout = new MenuItem("About Game");
		itmAbout.setId("AboutMenu");
		itmAbout.setOnAction(actionEvent);
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
		// timeLable.setText(clockTime);
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
		textAreaLable = new Label();
		textAreaLable.setAlignment(Pos.TOP_LEFT);
		textAreaLable.setText(chatText.toString());
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
		VBox itemContainer = new VBox(5);
		HBox hbox = new HBox(5);
		itemGrid = new GridPane();
		itemGrid.setOnMouseMoved(mouseEvent);
		// hbox.setOnMousePressed(mouseEvent);
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
		String msgToSend = msg.getText();
		msg.clear();
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
	 * this method will set the world time
	 * 
	 * @param worldTime
	 */
	public void setTime(String time) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				timeLable.setText(time);
			}
		});
	}

	public void setWaitingRoomAvatar() {
		Image avatarImg = Images.getAvatarImageBySide(Avatar.values()[avatarIndex], Side.Front);
		ImageView avatarImage = new ImageView(avatarImg);
		avatarImage.setFitHeight(80);
		avatarImage.setFitWidth(80);
		playersWaiting.getChildren().add(avatarImage);
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
				color = MINIMAP_COLOR_TABLE.get(areaMap[row][col]);
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

	public void setItemDescription(int x, int y) {
		// System.out.println(x+" "+y);
		String item = null;
		for (Point p : itemsDescription.keySet()) {
			// System.out.println(p.x+" "+p.y);
			if (p.x == x && p.y == y) {
				// System.out.println(p.x + " " + p.y);
				item = itemsDescription.get(p);
				break;
			}
		}
		// no items yet
		if (item == null) {
			return;
		}
		// System.out.println("string: " + item + ", length: " + item.length());
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

	public void objectLabel() {
		objectMessage = new Label();
		objectMessage.setLayoutX((GAMEPANE_WIDTH_VALUE / 2) - 20);
		objectMessage.setLayoutY(HEIGHT_VALUE - 160);
		objectMessage.getStyleClass().add("object-description");

	}

	/**
	 * This method is used to display to the user the objects description.
	 * 
	 * @param description
	 */
	public void displayObjectDescription(String description) {
		objectMessage.setText(description);
		//ft = new FadeTransition(Duration.millis(49), objectMessage);
		//ft.setFromValue(0.0);
		//ft.setToValue(1.0);
		//ft.play();
		group.getChildren().add(objectMessage);
	}

	/**
	 * This static helper method will pop up a message dialog to user.
	 *
	 * @param msg
	 */
	public static void showMsgPane(String title, String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AlertBox.displayMsg(title, msg);
			}
		});
		System.err.println(msg);
	}

	/**
	 * This static helper method will pop up a warning dialog to user.
	 *
	 * @param msg
	 */
	public static void showWarningPane(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AlertBox.displayMsg("Warning", msg);
			}
		});
		System.err.println(msg);
	}

}