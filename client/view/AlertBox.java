package client.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import client.rendering.Images;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;

/**
 * This class represents a dialog box and is used to display messages.
 *
 * @author Dipen Patel (300304965)
 *
 */
public class AlertBox {
	/**
	 * this method will open a dialog box given the title and the massage.
	 *
	 * @param title
	 * @param message
	 */
	public static void displayMsg(String title, String message) {
		Stage window = new Stage();
		// this makes it so you can't click on the window other then this one
		window.initModality(Modality.APPLICATION_MODAL);
		// sets the titel
		window.setTitle(title);
		// sets the weight and height
		window.setMinHeight(200);
		window.setMinWidth(300);

		Label lable = new Label();
		// set the massage
		lable.setText(message);
		// creates the button
		Button ok = new Button("Ok");
		ok.setOnAction(e -> window.close());

		VBox layout = new VBox(10);
		layout.getChildren().addAll(lable, ok);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.show();

	}

	public static void aboutPopUp() {
		Stage window = new Stage();
		// this makes it so you can't click on the window other then this one
		window.initModality(Modality.APPLICATION_MODAL);
		// sets the titel
		window.setTitle("About plague");
		// sets the weight and height
		window.setMinHeight(GUI.HEIGHT_VALUE);
		window.setMinWidth(GUI.WIDTH_VALUE);

		Pane pane = new Pane();
		Image image = Images.HOWTOPLAY_IMAGE;
		ImageView howImage = new ImageView(image);
		howImage.setFitHeight(GUI.HEIGHT_VALUE);
		howImage.setFitWidth(GUI.WIDTH_VALUE);
		pane.getChildren().add(howImage);

		// VBox layout = new VBox(10);
		// layout.getChildren().addAll(lable, ok);
		// layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(pane);
		window.setScene(scene);
		window.show();

	}

	public static void keyPopUp() {
		Stage window = new Stage();
		// this makes it so you can't click on the window other then this one
		window.initModality(Modality.APPLICATION_MODAL);
		// sets the titel
		window.setTitle("About plague");
		// sets the weight and height
		window.setMinHeight(GUI.HEIGHT_VALUE);
		window.setMinWidth(GUI.WIDTH_VALUE);

		Pane pane = new Pane();
		Image image = Images.KEYBOARDSHORT_IMAGE;
		ImageView howImage = new ImageView(image);
		howImage.setFitHeight(GUI.HEIGHT_VALUE);
		howImage.setFitWidth(GUI.WIDTH_VALUE);
		pane.getChildren().add(howImage);

		Scene scene = new Scene(pane);
		window.setScene(scene);
		window.show();

	}

}
