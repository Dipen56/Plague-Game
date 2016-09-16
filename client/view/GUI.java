package client.view;

import javafx.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.application.*;
import javafx.stage.Stage;
import javafx.scene.Group;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
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
	// GUI Style CSS
	private static final String STYLE_CSS = "/main.css";
	// Constants Images
	private static final String GAMEICON_IMAGE = "/game-icon.png";
	private static final String INVENTORY_IMAGE = "/item-tray.png";

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

	public GUI() {
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

		// Create a VBox which is just layout manger and adds gap of 10
		rightPanel = new VBox(10);
		rightPanel.setPrefSize(RIGHTPANE_WIDTH_VALUE, HEIGHT_VALUE);
		rightPanel.getStyleClass().add("cotrolvbox");
		borderPane = new BorderPane();
		borderPane.setRight(rightPanel);

		// this starts the action listener
		actionEventHandler();
		// this will start the key listener
		keyEventHander();
		// this will start the mouse listener
		mouseEventHander();
		// creates a scene
		setMenuBar();
		setWorldTime();
		setminiMap();
		setchat();
		setItems();
		gamePane = new StackPane();
		gamePane.setPrefHeight(HEIGHT_VALUE);
		gamePane.setPrefWidth(GAMEPANE_WIDTH_VALUE);

		// Calls the rendering
		render.render(group);
		gamePane.getChildren().add(group);
		borderPane.setLeft(gamePane);
		Scene scene = new Scene(borderPane, WIDTH_VALUE, HEIGHT_VALUE);
		scene.getStylesheets().add(this.getClass().getResource(STYLE_CSS).toExternalForm());
		scene.setOnKeyPressed(keyEvent);
		window.setScene(scene);
		window.show();

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
	public void setChatText(String text, String user) {
		chatText = chatText + "\n";
		chatText = chatText + user + ": " + text;
		textAreaLable.setText(chatText);
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
					// send the typed massage and clear text area with ""
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
					System.out.println("left");
				} else if (event.getCode() == KeyCode.RIGHT) {
					// this is for moving right
					System.out.println("right");
				} else if (event.getCode() == KeyCode.UP) {
					// this is for moving up
					System.out.println("up");
				} else if (event.getCode() == KeyCode.DOWN) {
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

	/**
	 * this method is just here for testing the gui
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// this lunch's the window which will end up call the start method above
		launch(args);

	}
}
