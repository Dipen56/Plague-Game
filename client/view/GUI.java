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

/**
 * This class represents the main GUI class this class bring together all the
 * different components of the GUI.
 *
 * @author Dipen
 *
 */
public class GUI extends Application {
	private static final String GAMEICON_IMAGE = "/game-icon.png";
	// private static int WIDTH_VALUE = 1500;
	// private static int HEIGHT_VALUE = 1000;
	private int WIDTH_VALUE = 1000;
	private int HEIGHT_VALUE = 700;
	// main window
	private Stage window;
	// controls
	private MenuBar menuBar;
	private Label timeLable;
	private Label miniMapLable;
	private Label textAreaLable;
	private TextField msg;
	private Button send;
	// panes
	// right pane with vertical alligment
	private VBox vbox;
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

	public GUI() {
		Button b = new Button();
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
		vbox = new VBox(10);
		vbox.setPrefSize(400, 1000);
		vbox.getStyleClass().add("cotrolvbox");
		borderPane = new BorderPane();
		borderPane.setRight(vbox);

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
		Group group = new Group();
		// Calls the rendering

		//Constant is set to 5 atm
		render(group,5);
		borderPane.setLeft(group);

		Scene scene = new Scene(borderPane, WIDTH_VALUE, HEIGHT_VALUE);
		scene.getStylesheets().add(this.getClass().getResource("/main.css").toExternalForm());
		scene.setOnKeyPressed(keyEvent);
		window.setScene(scene);
		window.show();

	}

	private void render(Group group, int dimension) {
		int renderWidth = WIDTH_VALUE - 400;

		// Night image background
		Image image = loadImage("/background.gif");
		ImageView iv1 = new ImageView();
		iv1.setImage(image);
		iv1.setFitWidth(renderWidth);
		iv1.setFitHeight(HEIGHT_VALUE);
		group.getChildren().add(iv1);


		//Game field
//		Rectangle rect = new Rectangle(0, HEIGHT_VALUE / 2 + 50, renderWidth, HEIGHT_VALUE / 2 - 50);
//		rect.setArcHeight(15);
//		rect.setArcWidth(15);
//		rect.setFill(Color.FORESTGREEN);
//		rect.setStroke(Color.BLACK);
//		group.getChildren().add(rect);


		for (int i = 0; i < dimension;i++){
			for (int j = 0; j < dimension;j++){
				Image grass = loadImage("/grass.png");
				ImageView iv3 = new ImageView();
				iv3.setImage(grass);
				iv3.setX(i*grass.getWidth());
				iv3.setY((HEIGHT_VALUE / 2 + 50)+(j*grass.getHeight()));
				group.getChildren().add(iv3);
			}
		}

		//Player on the board
		Image player = loadImage("/standingstillrear.png");
		ImageView iv2 = new ImageView();
		iv2.setImage(player);
		iv2.setX(renderWidth/2);
		iv2.setY(HEIGHT_VALUE-200);
		group.getChildren().add(iv2);
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
		vbox.getChildren().add(titlePane);
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
		vbox.getChildren().add(titlePane);
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

		vbox.getChildren().add(titlePane);

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
				Image img = loadImage("/item-tray.png");
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
		Image img = loadImage("/item-tray.png");
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

		vbox.getChildren().add(titlePane);

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
