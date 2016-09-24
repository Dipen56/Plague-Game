package client.view;

import javafx.scene.Scene;

import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.geometry.Pos;

/**
 * This class represents a dialog box and is used to display massages.
 * 
 * @author Dipen
 *
 */

public class AlertBox {
	/**
	 * this method will open a dialog box given the title and the massage.
	 * 
	 * @param title
	 * @param massage
	 */
	public static void displayMsg(String title, String massage) {
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
		lable.setText(massage);
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

}
