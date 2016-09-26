package server.view;

import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.application.*;
import javafx.stage.Stage;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;

import java.util.concurrent.CountDownLatch;

import client.view.AlertBox;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import server.ServerMain;

/**
 * this class represents the Server GUI which is just basically used to display
 * information about the server, like port number, name, ip address, which users are
 * currently on the server ect... this is info then can be used by the client to connect
 * to the game.NOTE: this GUI only outputs information it does not actually takes any in.
 *
 * @author Dipen
 *
 */

public class ServerGui extends Application {
    private static final String GAMEICON_IMAGE = "/game-icon.png";
    private Stage window;
    private BorderPane borderPane;
    private Label textLabel;
    private FadeTransition ft;
    public static String ip;
    public static int port;

    public ServerGui() {

    }

    @Override
    public void start(Stage mainWindow) throws Exception {
        window = mainWindow;
        window.setTitle("Plague Game Server");
        window.getIcons().add(loadImage(GAMEICON_IMAGE));
        borderPane = new BorderPane();
        textLabel = new Label();
        textLabel.getStyleClass().add("root-server");
        // textLabel.setText("Welcome Server Is Starting Up...\n Operated By:
        // HARDD inc");
        setText();
        ft = new FadeTransition(Duration.millis(4000), textLabel);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        textLabel.setWrapText(true);
        borderPane.setCenter(textLabel);
        // setText("192.1.1.0", "8080");
        Scene mainScene = new Scene(borderPane, 300, 300);
        window.setScene(mainScene);
        mainScene.getStylesheets()
                .add(this.getClass().getResource("/main.css").toExternalForm());

        window.show();
        // setText("10.1.1","5000");

    }

    /**
     * this method is used to set the information about the server to the GUI
     */
    public void setText() {
        textLabel.setText("Server Address: " + ip + " \nPort Number: " + port);
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
     * This main method is only here for testing the server Gui
     */
    public static void main(String[] args) {
        launch(args);

    }
}
