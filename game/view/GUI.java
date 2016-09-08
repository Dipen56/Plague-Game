package game.view;

import javafx.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javafx.application.*;
import javafx.stage.Stage;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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
	private Stage window;

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
		//window.getIcons().add(loadImage(GAMEICON_IMAGE));
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
		// Create a VBox which is just layout manger
		VBox vbox = new VBox();
		vbox.setPrefSize(400, 1000);
		vbox.setStyle("-fx-background-color: #336699;");
		BorderPane borderPane = new BorderPane();
		// add the layout to the borderPane Layout
		borderPane.setTop(menuBar);
		borderPane.setRight(vbox);
		// creates a scene
		Scene scene = new Scene(borderPane, WIDTH_VALUE, HEIGHT_VALUE);
		window.setScene(scene);
		window.show();

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
