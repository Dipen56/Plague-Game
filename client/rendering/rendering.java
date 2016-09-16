package client.rendering;

import client.view.GUI;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents the main rendering class, this class will control the
 * rendering of the game board, character, and objects.
 * 
 * @author Angelo & Dipen
 *
 */

public class Rendering {
	private double dimension = 9.1;
	private int renderWidth = 630;
	private int charWidth = 40;
	private int charHeight = 70;
	private Image player = loadImage("/standingstillrear.png");
	private ImageView imageViewCharacter = new ImageView();
	private Thread thread = new Thread();
	private Group group;

	public Rendering() {
	}

	// public void render(Group group) {
	// // going to do this with poly gones and then add images on top of them
	// // done.
	// this.group = group;
	//
	//
	// }
	public void render(Group group) {
		this.group = group;
		// Night image background
		Image image = loadImage("/background.gif");
		ImageView imageViewNight = new ImageView();
		imageViewNight.setImage(image);
		imageViewNight.setFitWidth(renderWidth);
		imageViewNight.setFitHeight(GUI.HEIGHT_VALUE / 2);
		group.getChildren().add(imageViewNight);

		// Game grass field
		Image grass = loadImage("/grass border.jpg");
		// x position on which the grass (board) starts to render
		// needs revising in accordance to the renderWidth
		double xPoint = ((renderWidth / 2) - (grass.getWidth() * dimension / 2));
		// y position on which the grass (board) starts to render
		double yPoint = GUI.HEIGHT_VALUE / 5 * 2.5;
		// height of the grass image
		int grassHeight;
		for (int row = 0; row < dimension; row++) {
			grassHeight = (int) (((grass.getHeight() / dimension) * row) + grass.getHeight() / dimension);
			for (int col = 0; col < dimension; col++) {
				// switch between the border, and no border grass images
				grass = loadImage("/grass.png");
				// grass = loadImage("/grass border.jpg");
				ImageView imageViewGrass = new ImageView();
				imageViewGrass.setImage(grass);
				imageViewGrass.setX((xPoint) + (col * grass.getWidth()));
				imageViewGrass.setY(yPoint);
				imageViewGrass.setFitHeight(grassHeight);
				group.getChildren().add(imageViewGrass);
			}
			yPoint = yPoint + grassHeight;
		}

		// Tree on the board
		Image tree = loadImage("/tree.png");
		ImageView imageViewTree = new ImageView();
		imageViewTree.setImage(tree);
		// This will need to be scaled later....
		imageViewTree.setFitHeight(200);
		imageViewTree.setFitWidth(170);
		////////////////////////////////////////////////
		imageViewTree.setX(100);
		imageViewTree.setY(GUI.HEIGHT_VALUE - 500);
		group.getChildren().add(imageViewTree);

		// Player on the board
		imageViewCharacter.setImage(player);
		imageViewCharacter.setFitHeight(charHeight);
		imageViewCharacter.setFitWidth(charWidth);
		imageViewCharacter.setX(renderWidth / 2);
		imageViewCharacter.setY(GUI.HEIGHT_VALUE - 150);
		group.getChildren().add(imageViewCharacter);
	}

	private Image loadImage(String name) {
		Image image = new Image(this.getClass().getResourceAsStream(name));
		return image;
	}

}
