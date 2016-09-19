package client.rendering;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JLabel;

import client.view.GUI;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.ImagePattern;

/**
 * This class represents the main rendering class, this class will control the
 * rendering of the game board, character, and objects.
 * 
 * @author Dipen
 *
 */

public class Rendering {
	private static final String PLAYER_IMAGE = "/standingstillrear.png";
	private static final String BACKGROUND_IMAGE = "/background.gif";
	private static final String GRASS_IMAGE = "/grass.png";
	private Group group;

	public double scaleY = 0.85; // lower number less scaling
	private int gamePaneHeight = GUI.HEIGHT_VALUE - 35; // 35 y alignment of
														// group
	private int gamePaneWidth = GUI.GAMEPANE_WIDTH_VALUE - 3; // 3 x alignment
																// of group
	private int tileWidth = 200;
	private int tileHeight = 60;
	public int centerWidth = gamePaneWidth / 2;
	public int centerHeght = gamePaneHeight;

	public Rendering() {
		MapParser mapParser = new MapParser(10, 10);
	}

	/**
	 * this method is used to render the game
	 * 
	 * @param group
	 */
	public void render(Group renderGroup) {
		this.group = renderGroup;

		Image image = loadImage(BACKGROUND_IMAGE);
		Image grass = loadImage(GRASS_IMAGE);
		ImageView imageViewNight = new ImageView();
		imageViewNight.setImage(image);
		imageViewNight.setFitWidth(gamePaneWidth + 3);
		imageViewNight.setFitHeight(gamePaneHeight + 35);
		group.getChildren().add(imageViewNight);
		int squaresInFront = 0;// the number of squares on the player's face
		// side
		int squaresToLeft = 0;// the number of squares on the player's left
		// side
		int squaresToRight = 0;// //the number of squares on the player's
		// right side
		Point playerLoc = new Point(5, 0); // center
		int boardSize = 10;
		squaresInFront = boardSize - playerLoc.y;
		squaresToLeft = (boardSize - playerLoc.x) - 1;
		squaresToRight = (boardSize - squaresToLeft);

		// this is used to for the top line points x0 and x1 which will be
		// scaled from larger to smaller
		double topLine = centerHeght;
		// double prevTopLine = centerHeght;
		// this point is the bot right and will also got from larger to smaller
		// int previouX1
		// it's twice the size of the set tile width
		double x2 = centerWidth + tileWidth / 2;
		// this point is is the bot right and is set it the height of the game
		// pane so at the bottom of window.
		double y2 = centerHeght;
		// this point is the bot lift and will also got from larger to smaller
		// int previouX0
		// it's twice the size of the set tile width
		double x3 = centerWidth - tileWidth / 2;
		// this point is is the bot lift and is set it the height of the game
		// pane so at the bottom of window.
		double y3 = centerHeght;
		// ===================================================================================================
		// Below this is point is the code for all the rendering for first
		// person
		// ====================================================================================================
		for (int i = 0; i < squaresInFront; i++) {
			// tiles in front of the player is rendered first
			Polygon p = new Polygon();

			p.setFill(new ImagePattern(grass));
			// origins
			p.setLayoutY(10);
			// current scaled width of the top part of the tile
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, i + 1);
			// this is the top part of the tile starting x pos
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			// this is the bot part of the tile which is just the previous tiles
			// top part
			p.getPoints().add(x3);
			p.getPoints().add(y3);
			p.getPoints().add(x2);
			p.getPoints().add(y2);
			// this is the top part to the tile and will be scaled from larger
			// to smaller
			// /__\
			p.getPoints().add(nowStartX + nowWidthOfSquare);
			p.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
			p.getPoints().add(nowStartX);
			p.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
			group.getChildren().add(p);
			// tiles on the lift of the players are render second for ever 1
			// tile that in front of them
			for (int j = 0; j < squaresToLeft; j++) {
				Polygon p2 = new Polygon();
				// this just push it down by 10
				p2.setLayoutY(10);
				// adds the grass image
				p2.setFill(new ImagePattern(grass));
				// this will get the width of the square that is the on facing
				// the player
				double previouWidthOfSquare = (x2 - x3);
				// this is the bot part of the tile which is just placed to the
				// left of the facing tile.
				p2.getPoints().add(x3 - (j * previouWidthOfSquare));
				p2.getPoints().add(y3);
				p2.getPoints().add(x2 - (j * previouWidthOfSquare));
				p2.getPoints().add(y2);
				// this is the top part to the tile and will be scaled on the
				// left from larger
				// to smaller
				p2.getPoints().add(nowStartX + nowWidthOfSquare - j * nowWidthOfSquare);
				p2.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
				p2.getPoints().add(nowStartX - j * nowWidthOfSquare);
				p2.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
				group.getChildren().add(p2);
			}
			// tiles on the right of the players are render second for ever 1
			// tile that in front of them
			for (int j = 0; j < squaresToRight; j++) {

				Polygon p3 = new Polygon();
				// adds the grass image
				p3.setFill(new ImagePattern(grass));
				// this just push it down by 10
				p3.setLayoutY(10);
				// this will get the width of the square that is the on facing
				// the player, the width will be the bot part of the facing
				// player square.
				double previouWidthOfSquare = (x2 - x3);
				// this is the bot part of the tile which is just placed to the
				// left of the facing tile.
				double tempx3 = x3 + (j * previouWidthOfSquare);
				// this is to force the polygons to draw in range of the screen
				if (tempx3 > gamePaneWidth) {
					tempx3 = gamePaneWidth;
				}
				p3.getPoints().add(tempx3);
				p3.getPoints().add(y3);
				double tempx2 = x2 + (j * previouWidthOfSquare);
				if (tempx2 > gamePaneWidth) {
					tempx2 = gamePaneWidth;
				}

				p3.getPoints().add(tempx2);
				p3.getPoints().add(y2);
				double tempx1 = nowStartX + nowWidthOfSquare + j * nowWidthOfSquare;
				if (tempx1 > gamePaneWidth) {
					tempx1 = gamePaneWidth;
				}
				// this is the top part to the tile and will be scaled on the
				// left from larger
				// to smaller
				p3.getPoints().add(tempx1);
				p3.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
				double tempx0 = nowStartX + j * nowWidthOfSquare;
				if (tempx0 > gamePaneWidth) {
					tempx0 = gamePaneWidth;
				}
				p3.getPoints().add(tempx0);
				p3.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
				group.getChildren().add(p3);
			}

			// update the bot right point to previous tiles top part which will
			// also be the top left part in the prevous tile
			x3 = nowStartX;
			y3 = topLine - tileHeight * Math.pow(scaleY, i + 1);
			// update the bot left point to the previous tiles top part which
			// will also be the top right part in the previous tile
			x2 = nowStartX + nowWidthOfSquare;
			y2 = topLine - tileHeight * Math.pow(scaleY, i + 1);
			// prevTopLine = topLine;
			// this updates the width of the next topline which will be used to
			// calc x0 , x1 so the top part
			topLine = topLine - tileHeight * Math.pow(scaleY, i + 1);

		}

	}

	private Image loadImage(String name) {
		Image image = new Image(this.getClass().getResourceAsStream(name));
		return image;
	}

	@Override
	public String toString() {

		return "renderclass";
	}

}
