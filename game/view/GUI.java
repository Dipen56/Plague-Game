package game.view;

import javafx.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.application.*;
import javafx.stage.Stage;
import javafx.scene.Group;

import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;

/**
 * This class represents the main GUI class this class bring together all the
 * different components of the GUI.
 * 
 * @author Dipen
 *
 */
public class GUI extends Application {
	private static final String GAMEICON_IMAGE = "/game-icon.png";
	private static int WIDTH_VALUE = 1500;
	private static int HEIGHT_VALUE = 1000;
	// main window
	private Stage window;
	// controls
	private MenuBar menuBar;
	private Label timeLable;
	private Label miniMapLable;
	private Label textAreaLable;
	private TextField msg;
	// panes
	// right pane with vertical alligment
	private VBox vbox;
	// standard layout
	private BorderPane borderPane;

	public GUI() {
	}

	/**
	 * this method will get passed in a stage which is the main window and will
	 * start it
	 */
	@Override
	public void start(Stage mainWindow) throws Exception {
		this.window = mainWindow;
		window.setTitle("Plague Game Developed By Team: HARDD");
		window.getIcons().add(loadImage(GAMEICON_IMAGE));
		window.setResizable(false);

		// Create a VBox which is just layout manger and adds gap of 10
		vbox = new VBox(10);
		vbox.setPrefSize(400, 1000);
		// vbox.setStyle("-fx-background-color: #242731;");
		// vbox.setPadding(new Insets(10, 50, 50, 50));
		vbox.getStyleClass().add("cotrolvbox");
		borderPane = new BorderPane();
		borderPane.setRight(vbox);
		// creates a scene
		setMenuBar();
		setWorldTime();
		setminiMap();
		setchat();
		setItems();
		Scene scene = new Scene(borderPane, WIDTH_VALUE, HEIGHT_VALUE);
		scene.getStylesheets().add(this.getClass().getResource("/main.css").toExternalForm());
		window.setScene(scene);
		window.show();

	}

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

	public void setchat() {
		TitledPane titlePane = new TitledPane();
		VBox chatControls = new VBox(5);
		titlePane.setText("Chat Room");
		chatControls.setPrefWidth(400);
		chatControls.setPrefHeight(200);
		chatControls.getStyleClass().add("chatarea-background");
		textAreaLable = new Label();
		textAreaLable.setPrefWidth(400);
		textAreaLable.setPrefHeight(150);
		textAreaLable.getStyleClass().add("chat-display");
		HBox hbox = new HBox(5);
		Button send = new Button("Send");
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

	public void setItems() {
		TitledPane titlePane = new TitledPane();
		titlePane.setText("Item Inventory");

		// FlowPane flow = new FlowPane();
		// flow.getStyleClass().add("itempane-background");
		// flow.setPrefHeight(230);
		// titlePane.setContent(flow);
		// flow.setVgap(2);
		// flow.setHgap(2);
		TableView table = new TableView();
		table.setPrefHeight(230);
		table.setPrefWidth(400);
		TableColumn thrid = new TableColumn("First Name");
        TableColumn second = new TableColumn("Last Name");
        
        //table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
       // titlePane.setContent(table);

		vbox.getChildren().add(titlePane);

	}

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
