package server.view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.application.*;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

/**
 * this class represents the Server GUI which is just basically used to display
 * information about the server, like port number, name, ip address, which users are
 * currently on the server etc... this is info then can be used by the client to connect
 * to the game.NOTE: this GUI only outputs information it does not actually takes any in.
 * 
 * @author Dipen
 *
 */

public class ServerGui extends Application {
    private static final String GAMEICON_IMAGE = "/game-icon.png";
    Stage window;
    BorderPane borderPane;
    Label textLabel;
    FadeTransition ft;

    public ServerGui() {
        // will be used later to start the gui
    }

    @Override
    public void start(Stage mainWindow) throws Exception {
        window = mainWindow;
        window.setTitle("Plague Game Server");
        window.getIcons().add(loadImage(GAMEICON_IMAGE));
        borderPane = new BorderPane();
        textLabel = new Label();
        textLabel.getStyleClass().add("root-server");
        textLabel.setText("Welcome Server Is Starting Up...\n Operated By: HARDD inc");
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

    }

    /**
     * this method is used to set the information about the server to the GUI
     */
    public void setText(String address, String port) {
        textLabel.setText("Server Address: " + address + " \nPort Number: " + port);
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
