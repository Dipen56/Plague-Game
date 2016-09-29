package antherrendering;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import client.*;
import client.rendering.MapParser;
import client.view.GUI;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import server.game.player.Player;
import server.game.world.Area;
import javafx.scene.paint.ImagePattern;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.application.*;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.event.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.WindowEvent;
import javafx.scene.layout.*;

/**
 * This class represents the main rendering class, this class will control the
 * rendering of the game board, character, and objects.
 * 
 * @author Dipen
 *
 */

public class AntherRendering {
	private static final String PLAYER_IMAGE = "/standingstillrear.png";
	// private static final String BACKGROUND_IMAGE = "/background.gif";
	private static final String BACKGROUND_IMAGE = "/night.jpg";
	private static final String GRASS_IMAGE = "/grass.png";
	private static final String TREE_IMAGE = "/tree.png";
	private static final String CHEST_IMAGE = "/chest.png";
	private Pane group;

	public double scaleY = 0.85; // lower number less scaling
	private int gamePaneHeight = GUI.HEIGHT_VALUE - 35; // 35 y alignment of
														// group
	private int gamePaneWidth = GUI.GAMEPANE_WIDTH_VALUE - 3; // 3 x alignment
																// of group
	private int tileWidth = 200;
	private int tileHeight = 60;
	public int centerWidth = gamePaneWidth / 2;
	public int centerHeght = gamePaneHeight;
	private MapParser mapParser;
	private Point playerLoc = new Point(2, 0);
	private int squaresInFront = 0;
	private int squaresToLeft = 0;
	private int squaresToRight = 0;
	private Player player;
	private Map<Integer, Player> playersOnMap;
	private Map<Integer, Area> map;
	private int avatarID;
	private int direction;

	private int boardSize = 10;

	public AntherRendering() {
		// will need to get board size passed in
		mapParser = new MapParser(10, 10);
	}

	/**
	 * this constructor is to be used for integration and will be passed in a
	 * Player and a map, and also all the other player on the map and also the
	 * avatar id atm.
	 * 
	 * @param player
	 */
	public AntherRendering(Player player, Map<Integer, Player> playersOnMap, Map<Integer, Area> map, int avatarID) {
		this.player = player;
		this.playersOnMap = playersOnMap;
		this.map = map;
		this.avatarID = avatarID;
	}

	/**
	 * this method is used to render the game
	 * 
	 * @param group
	 */
	public void renderNorth() {

		Image image = loadImage(BACKGROUND_IMAGE);
		Image grass = loadImage(GRASS_IMAGE);
		ImageView imageViewNight = new ImageView();
		imageViewNight.setImage(image);
		imageViewNight.setFitWidth(gamePaneWidth + 3);
		imageViewNight.setFitHeight(gamePaneHeight + 35);
		// System.out.println(group);
		group.getChildren().add(imageViewNight);

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
			p.setStroke(javafx.scene.paint.Color.AQUA);
			p.setStrokeWidth(1);
			group.getChildren().add(p);
			// tiles on the lift of the players are render second for ever 1
			// tile that in front of them
			for (int j = 0; j <= squaresToLeft; j++) {
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
				p2.setStroke(javafx.scene.paint.Color.AQUA);
				p2.setStrokeWidth(1);
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

				p3.getPoints().add(tempx3);
				p3.getPoints().add(y3);
				double tempx2 = x2 + (j * previouWidthOfSquare);

				p3.getPoints().add(tempx2);
				p3.getPoints().add(y2);
				double tempx1 = nowStartX + nowWidthOfSquare + j * nowWidthOfSquare;

				// this is the top part to the tile and will be scaled on the
				// left from larger
				// to smaller
				p3.getPoints().add(tempx1);
				p3.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
				double tempx0 = nowStartX + j * nowWidthOfSquare;

				p3.getPoints().add(tempx0);
				p3.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
				p3.setStroke(javafx.scene.paint.Color.AQUA);
				p3.setStrokeWidth(1);
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

	/**
	 * this method is used to render the game
	 * 
	 * @param group
	 */
	public void renderSouth() {
		Image image = loadImage(BACKGROUND_IMAGE);
		Image grass = loadImage(GRASS_IMAGE);
		ImageView imageViewNight = new ImageView();
		imageViewNight.setImage(image);
		imageViewNight.setFitWidth(gamePaneWidth + 3);
		imageViewNight.setFitHeight(gamePaneHeight + 35);
		group.getChildren().add(imageViewNight);

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
			p.setStroke(javafx.scene.paint.Color.AQUA);
			p.setStrokeWidth(1);
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
				p2.setStroke(javafx.scene.paint.Color.AQUA);
				p2.setStrokeWidth(1);
				group.getChildren().add(p2);
			}
			// tiles on the right of the players are render second for ever 1
			// tile that in front of them
			for (int j = 0; j <= squaresToRight; j++) {

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

				p3.getPoints().add(tempx3);
				p3.getPoints().add(y3);
				double tempx2 = x2 + (j * previouWidthOfSquare);

				p3.getPoints().add(tempx2);
				p3.getPoints().add(y2);
				double tempx1 = nowStartX + nowWidthOfSquare + j * nowWidthOfSquare;

				// this is the top part to the tile and will be scaled on the
				// left from larger
				// to smaller
				p3.getPoints().add(tempx1);
				p3.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
				double tempx0 = nowStartX + j * nowWidthOfSquare;

				p3.getPoints().add(tempx0);
				p3.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
				p3.setStroke(javafx.scene.paint.Color.AQUA);
				p3.setStrokeWidth(1);
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

	/**
	 * this method is used to render the game
	 * 
	 * @param group
	 */
	public void renderEast() {

		Image image = loadImage(BACKGROUND_IMAGE);
		Image grass = loadImage(GRASS_IMAGE);
		ImageView imageViewNight = new ImageView();
		imageViewNight.setImage(image);
		imageViewNight.setFitWidth(gamePaneWidth + 3);
		imageViewNight.setFitHeight(gamePaneHeight + 35);
		// System.out.println(group);
		group.getChildren().add(imageViewNight);

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
		for (int i = 0; i <= squaresInFront; i++) {
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
			p.setStroke(javafx.scene.paint.Color.AQUA);
			p.setStrokeWidth(1);
			group.getChildren().add(p);
			// tiles on the lift of the players are render second for ever 1
			// tile that in front of them
			for (int j = 0; j <= squaresToLeft; j++) {
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
				p2.setStroke(javafx.scene.paint.Color.AQUA);
				p2.setStrokeWidth(1);
				group.getChildren().add(p2);
			}
			// tiles on the right of the players are render second for ever 1
			// tile that in front of them
			for (int j = 0; j <= squaresToRight; j++) {

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

				p3.getPoints().add(tempx3);
				p3.getPoints().add(y3);
				double tempx2 = x2 + (j * previouWidthOfSquare);

				p3.getPoints().add(tempx2);
				p3.getPoints().add(y2);
				double tempx1 = nowStartX + nowWidthOfSquare + j * nowWidthOfSquare;

				// this is the top part to the tile and will be scaled on the
				// left from larger
				// to smaller
				p3.getPoints().add(tempx1);
				p3.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
				double tempx0 = nowStartX + j * nowWidthOfSquare;

				p3.getPoints().add(tempx0);
				p3.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
				p3.setStroke(javafx.scene.paint.Color.AQUA);
				p3.setStrokeWidth(1);
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

	/**
	 * this method is used to render the game
	 * 
	 * @param group
	 */
	public void renderWest() {

		Image image = loadImage(BACKGROUND_IMAGE);
		Image grass = loadImage(GRASS_IMAGE);
		ImageView imageViewNight = new ImageView();
		imageViewNight.setImage(image);
		imageViewNight.setFitWidth(gamePaneWidth + 3);
		imageViewNight.setFitHeight(gamePaneHeight + 35);
		// System.out.println(group);
		group.getChildren().add(imageViewNight);

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
			p.setStroke(javafx.scene.paint.Color.AQUA);
			p.setStrokeWidth(1);
			group.getChildren().add(p);
			// tiles on the lift of the players are render second for ever 1
			// tile that in front of them
			for (int j = 0; j <= squaresToLeft; j++) {
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
				p2.setStroke(javafx.scene.paint.Color.AQUA);
				p2.setStrokeWidth(1);
				group.getChildren().add(p2);
			}
			// tiles on the right of the players are render second for ever 1
			// tile that in front of them
			for (int j = 0; j <= squaresToRight; j++) {

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

				p3.getPoints().add(tempx3);
				p3.getPoints().add(y3);
				double tempx2 = x2 + (j * previouWidthOfSquare);

				p3.getPoints().add(tempx2);
				p3.getPoints().add(y2);
				double tempx1 = nowStartX + nowWidthOfSquare + j * nowWidthOfSquare;

				// this is the top part to the tile and will be scaled on the
				// left from larger
				// to smaller
				p3.getPoints().add(tempx1);
				p3.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
				double tempx0 = nowStartX + j * nowWidthOfSquare;

				p3.getPoints().add(tempx0);
				p3.getPoints().add(topLine - tileHeight * Math.pow(scaleY, i + 1));
				p3.setStroke(javafx.scene.paint.Color.AQUA);
				p3.setStrokeWidth(1);
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

	/**
	 * this method is used to render the object in the game that are to the
	 * north of the player..
	 */
	public void renderObjectsNorth() {
		String[][] worldMap = mapParser.getMap();
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
		// it scaling by thinking it throught away make 0 some how and it will
		// work ******** it moving down quicker now
		// the 10 will be the sqare in front of the players
		// TODO: thid 10 would need to change
		for (int row = playerLoc.y; row < 10; row++) {
			int col = playerLoc.x;
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, (row - playerLoc.y) + 1);
			// this is the top part of the tile starting x pos
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			// front view
			if (worldMap[row][col].equals("tree") || worldMap[row][col].equals("chest")) {
				// this will draw the object that are in front
				double x1 = nowStartX + nowWidthOfSquare;
				double y1 = topLine - tileHeight * Math.pow(scaleY, (row - playerLoc.y) + 1);
				double x0 = nowStartX;
				double y0 = topLine - tileHeight * Math.pow(scaleY, (row - playerLoc.y) + 1);
				double height = (y3 - y0);
				double width = (x2 - x3);
				// System.out.println(worldMap[row][col]);
				if (worldMap[row][col].equals("tree")) {
					Image tree = loadImage(TREE_IMAGE);
					ImageView imageViewTree = new ImageView();
					imageViewTree.setImage(tree);

					imageViewTree.setFitHeight(height);
					imageViewTree.setFitWidth(width);
					// here is where we need to do the logic
					imageViewTree.setX(x0);
					imageViewTree.setY(y0);
					group.getChildren().add(imageViewTree);
				} else if (worldMap[row][col].equals("chest")) {
					Image tree = loadImage(CHEST_IMAGE);
					ImageView imageViewTree = new ImageView();
					imageViewTree.setImage(tree);

					imageViewTree.setFitHeight(height);
					imageViewTree.setFitWidth(width);
					// here is where we need to do the logic
					imageViewTree.setX(x0);
					imageViewTree.setY(y0);
					group.getChildren().add(imageViewTree);
				}
			}
			// this will be the sqares to the left but is realy get the
			// right side on the 2d map
			col = playerLoc.x;
			for (int j = 0; j <= squaresToLeft; j++) {
				if (worldMap[row][col + j].equals("tree") || worldMap[row][col + j].equals("chest")) {

					// left side logic here
					double previouWidthOfSquare = (x2 - x3);
					// this is the bot part of the tile which is just placed
					// to the
					// left of the facing tile.
					double tempLeftx3 = x3 - (j * previouWidthOfSquare);
					double tempLefty3 = y3;
					double tempLeftx2 = x2 - (j * previouWidthOfSquare);
					double tempLefty2 = y2;
					// this is the top part to the tile and will be scaled
					// on the
					// left from larger
					// to smaller
					double tempLeftx1 = nowStartX + nowWidthOfSquare - (j * nowWidthOfSquare);
					double tempLefty1 = topLine - tileHeight * Math.pow(scaleY, (row - playerLoc.y) + 1);
					double tempLeftx0 = nowStartX - j * nowWidthOfSquare;
					double tempLefty0 = topLine - tileHeight * Math.pow(scaleY, (row - playerLoc.y) + 1);

					double heightleft = (tempLefty3 - tempLefty0);
					double widthleft = (tempLeftx2 - tempLeftx3);
					if (worldMap[row][col + j].equals("tree")) {
						Image tree = loadImage(TREE_IMAGE);
						ImageView imageViewTree = new ImageView();
						imageViewTree.setImage(tree);

						imageViewTree.setFitHeight(heightleft);
						imageViewTree.setFitWidth(widthleft);
						// here is where we need to do the logic
						imageViewTree.setX(tempLeftx0);
						imageViewTree.setY(tempLefty0);
						group.getChildren().add(imageViewTree);
					} else if (worldMap[row][col + j].equals("chest")) {
						Image tree = loadImage(CHEST_IMAGE);
						ImageView imageViewTree = new ImageView();
						imageViewTree.setImage(tree);

						imageViewTree.setFitHeight(heightleft);
						imageViewTree.setFitWidth(widthleft);
						// here is where we need to do the logic
						imageViewTree.setX(tempLeftx0);
						imageViewTree.setY(tempLefty0);
						group.getChildren().add(imageViewTree);
					}
				}

			}

			col = playerLoc.x;
			// this is the right side on the screen but the left side on the
			// board
			for (int j = 0; j < squaresToRight; j++) {
				if (worldMap[row][col - j].equals("tree") || worldMap[row][col - j].equals("chest")) {

					// this will get the width of the square that is the on
					// facing
					// the player, the width will be the bot part of the facing
					// player square.
					double previouWidthOfSquare = (x2 - x3);
					// this is the bot part of the tile which is just placed to
					// the
					// left of the facing tile.
					double tempRightx3 = x3 + (j * previouWidthOfSquare);
					// this is to force the polygons to draw in range of the
					// screen

					double tempRighty3 = y3;
					double tempRightx2 = x2 + (j * previouWidthOfSquare);

					double tempRighty2 = y2;
					double tempRightx1 = nowStartX + nowWidthOfSquare + j * nowWidthOfSquare;

					// this is the top part to the tile and will be scaled on
					// the
					// left from larger
					// to smaller

					double tempRightY1 = topLine - tileHeight * Math.pow(scaleY, (row - playerLoc.y) + 1);
					double tempRightx0 = nowStartX + j * nowWidthOfSquare;

					double tempRighty0 = topLine - tileHeight * Math.pow(scaleY, (row - playerLoc.y) + 1);
					double heightRight = (tempRighty3 - tempRighty0);
					double widthRight = (tempRightx2 - tempRightx3);
					// System.out.println(gamePaneWidth);
					// System.out.println(tempRightx0 + " " + tempRightx1 + " "
					// + tempRightx2 + " " + tempRightx3);
					// System.out.println(worldMap[row][col - j]);
					if (worldMap[row][col - j].equals("tree")) {
						Image tree = loadImage(TREE_IMAGE);
						ImageView imageViewTree = new ImageView();
						// imageViewTree.toBack();
						imageViewTree.setImage(tree);
						// no idea why the first image on the right is not
						// draw
						imageViewTree.setFitHeight(heightRight);
						imageViewTree.setFitWidth(widthRight);
						// here is where we need to do the logic
						imageViewTree.setX(tempRightx0);
						imageViewTree.setY(tempRighty0);
						group.getChildren().add(imageViewTree);
					} else if (worldMap[row][col - j].equals("chest")) {
						Image tree = loadImage(CHEST_IMAGE);
						ImageView imageViewTree = new ImageView();
						imageViewTree.setImage(tree);

						imageViewTree.setFitHeight(heightRight);
						imageViewTree.setFitWidth(widthRight);
						// here is where we need to do the logic
						imageViewTree.setX(tempRightx0);
						imageViewTree.setY(tempRighty0);
						group.getChildren().add(imageViewTree);
					}

				}
			}

			// update the bot right point to previous tiles top part which will
			// also be the top left part in the prevous tile
			x3 = nowStartX;
			y3 = topLine - tileHeight * Math.pow(scaleY, (row - playerLoc.y) + 1);

			// update the bot left point to the previous tiles top part which
			// will also be the top right part in the previous tile
			x2 = nowStartX + nowWidthOfSquare;
			y2 = topLine - tileHeight * Math.pow(scaleY, (row - playerLoc.y) + 1);
			// prevTopLine = topLine;
			// this updates the width of the next topline which will be used to
			// calc x0 , x1 so the top part
			topLine = topLine - tileHeight * Math.pow(scaleY, (row - playerLoc.y) + 1);

		}
	}

	/**
	 * this method is used to render the object in the game that are to the
	 * north of the player..
	 */
	public void renderObjectsSouth() {
		String[][] worldMap = mapParser.getMap();
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
		// it scaling by thinking it throught away make 0 some how and it will
		// work ******** it moving down quicker now
		// the 10 will be the sqare in front of the players
		for (int row = 0; row < squaresInFront; row++) {
			int col = playerLoc.x;
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, row + 1);
			// this is the top part of the tile starting x pos
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			// front view
			if (worldMap[(playerLoc.y - 1) - row][col].equals("tree")
					|| worldMap[(playerLoc.y - 1) - row][col].equals("chest")) {

				// this will draw the object that are in front
				double x1 = nowStartX + nowWidthOfSquare;
				double y1 = topLine - tileHeight * Math.pow(scaleY, row + 1);
				double x0 = nowStartX;
				double y0 = topLine - tileHeight * Math.pow(scaleY, row + 1);
				double height = (y3 - y0);
				double width = (x2 - x3);
				if (worldMap[(playerLoc.y - 1) - row][col].equals("tree")) {
					Image tree = loadImage(TREE_IMAGE);
					ImageView imageViewTree = new ImageView();
					imageViewTree.setImage(tree);

					imageViewTree.setFitHeight(height);
					imageViewTree.setFitWidth(width);
					// here is where we need to do the logic
					imageViewTree.setX(x0);
					imageViewTree.setY(y0);
					group.getChildren().add(imageViewTree);
				} else if (worldMap[(playerLoc.y - 1) - row][col].equals("chest")) {
					Image tree = loadImage(CHEST_IMAGE);
					ImageView imageViewTree = new ImageView();
					imageViewTree.setImage(tree);

					imageViewTree.setFitHeight(height);
					imageViewTree.setFitWidth(width);
					// here is where we need to do the logic
					imageViewTree.setX(x0);
					imageViewTree.setY(y0);
					group.getChildren().add(imageViewTree);
				}
			}
			// this will be the sqares to the left but is realy get the
			// right side on the 2d map
			for (int j = 0; j < squaresToLeft; j++) {
				if (worldMap[(playerLoc.y - 1) - row][col - j].equals("tree")
						|| worldMap[(playerLoc.y - 1) - row][col - j].equals("chest")) {

					// left side logic here
					double previouWidthOfSquare = (x2 - x3);
					// this is the bot part of the tile which is just placed
					// to the
					// left of the facing tile.
					double tempLeftx3 = x3 - (j * previouWidthOfSquare);
					double tempLefty3 = y3;
					double tempLeftx2 = x2 - (j * previouWidthOfSquare);
					double tempLefty2 = y2;
					// this is the top part to the tile and will be scaled
					// on the
					// left from larger
					// to smaller
					double tempLeftx1 = nowStartX + nowWidthOfSquare - (j * nowWidthOfSquare);
					double tempLefty1 = topLine - tileHeight * Math.pow(scaleY, row + 1);
					double tempLeftx0 = nowStartX - j * nowWidthOfSquare;
					double tempLefty0 = topLine - tileHeight * Math.pow(scaleY, row + 1);

					double heightleft = (tempLefty3 - tempLefty0);
					double widthleft = (tempLeftx2 - tempLeftx3);
					if (worldMap[(playerLoc.y - 1) - row][col - j].equals("tree")) {
						Image tree = loadImage(TREE_IMAGE);
						ImageView imageViewTree = new ImageView();
						imageViewTree.setImage(tree);

						imageViewTree.setFitHeight(heightleft);
						imageViewTree.setFitWidth(widthleft);

						imageViewTree.setX(tempLeftx0);
						imageViewTree.setY(tempLefty0);
						group.getChildren().add(imageViewTree);
					} else if (worldMap[(playerLoc.y - 1) - row][col - j].equals("chest")) {
						Image tree = loadImage(CHEST_IMAGE);
						ImageView imageViewTree = new ImageView();
						imageViewTree.setImage(tree);

						imageViewTree.setFitHeight(heightleft);
						imageViewTree.setFitWidth(widthleft);
						// here is where we need to do the logic
						imageViewTree.setX(tempLeftx0);
						imageViewTree.setY(tempLefty0);
						group.getChildren().add(imageViewTree);
					}
				}

			}

			col = playerLoc.x;
			// this is the right side on the screen but the left side on the
			// board
			for (int j = 0; j <= squaresToRight; j++) {
				if (worldMap[(playerLoc.y - 1) - row][col + j].equals("tree")
						|| worldMap[(playerLoc.y - 1) - row][col + j].equals("chest")) {

					// this will get the width of the square that is the on
					// facing
					// the player, the width will be the bot part of the facing
					// player square.
					double previouWidthOfSquare = (x2 - x3);
					// this is the bot part of the tile which is just placed to
					// the
					// left of the facing tile.
					double tempRightx3 = x3 + (j * previouWidthOfSquare);
					// this is to force the polygons to draw in range of the
					// screen

					double tempRighty3 = y3;
					double tempRightx2 = x2 + (j * previouWidthOfSquare);

					double tempRighty2 = y2;
					double tempRightx1 = nowStartX + nowWidthOfSquare + j * nowWidthOfSquare;

					// this is the top part to the tile and will be scaled on
					// the
					// left from larger
					// to smaller

					double tempRightY1 = topLine - tileHeight * Math.pow(scaleY, row + 1);
					double tempRightx0 = nowStartX + j * nowWidthOfSquare;

					double tempRighty0 = topLine - tileHeight * Math.pow(scaleY, row + 1);
					double heightRight = (tempRighty3 - tempRighty0);
					double widthRight = (tempRightx2 - tempRightx3);

					if (worldMap[(playerLoc.y - 1) - row][col + j].equals("tree")) {
						Image tree = loadImage(TREE_IMAGE);
						ImageView imageViewTree = new ImageView();
						// imageViewTree.toBack();
						imageViewTree.setImage(tree);
						// no idea why the first image on the right is not
						// draw
						imageViewTree.setFitHeight(heightRight);
						imageViewTree.setFitWidth(widthRight);
						// here is where we need to do the logic
						imageViewTree.setX(tempRightx0);
						imageViewTree.setY(tempRighty0);
						group.getChildren().add(imageViewTree);
					} else if (worldMap[(playerLoc.y - 1) - row][col + j].equals("chest")) {
						Image tree = loadImage(CHEST_IMAGE);
						ImageView imageViewTree = new ImageView();
						imageViewTree.setImage(tree);

						imageViewTree.setFitHeight(heightRight);
						imageViewTree.setFitWidth(widthRight);
						// here is where we need to do the logic
						imageViewTree.setX(tempRightx0);
						imageViewTree.setY(tempRighty0);
						group.getChildren().add(imageViewTree);
					}

				}
			}

			// update the bot right point to previous tiles top part which will
			// also be the top left part in the prevous tile
			x3 = nowStartX;
			y3 = topLine - tileHeight * Math.pow(scaleY, row + 1);

			// update the bot left point to the previous tiles top part which
			// will also be the top right part in the previous tile
			x2 = nowStartX + nowWidthOfSquare;
			y2 = topLine - tileHeight * Math.pow(scaleY, row + 1);
			// prevTopLine = topLine;
			// this updates the width of the next topline which will be used to
			// calc x0 , x1 so the top part
			topLine = topLine - tileHeight * Math.pow(scaleY, row + 1);

		}
	}

	/**
	 * this method is used to render the object in the game that are to the
	 * north of the player..
	 */
	public void renderObjectsEast() {
		String[][] worldMap = mapParser.getMap();
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
		// it scaling by thinking it throught away make 0 some how and it will
		// work ******** it moving down quicker now
		// the 10 will be the sqare in front of the players
		// row == cols in this case
		for (int row = 0; row <= squaresInFront; row++) {
			int col = playerLoc.x;
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, row + 1);
			// this is the top part of the tile starting x pos
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			// front view
			System.out.println(playerLoc.x - row);//**********************
			if (worldMap[playerLoc.y][playerLoc.x - row].equals("tree")
					|| worldMap[playerLoc.y][playerLoc.x - row].equals("chest")) {
				// this will draw the object that are in front
				double x1 = nowStartX + nowWidthOfSquare;
				double y1 = topLine - tileHeight * Math.pow(scaleY, row + 1);
				double x0 = nowStartX;
				double y0 = topLine - tileHeight * Math.pow(scaleY, row + 1);
				double height = (y3 - y0);
				double width = (x2 - x3);
				if (worldMap[playerLoc.y][playerLoc.x - row].equals("tree")) {
					Image tree = loadImage(TREE_IMAGE);
					ImageView imageViewTree = new ImageView();
					imageViewTree.setImage(tree);

					imageViewTree.setFitHeight(height);
					imageViewTree.setFitWidth(width);
					// here is where we need to do the logic
					imageViewTree.setX(x0);
					imageViewTree.setY(y0);
					group.getChildren().add(imageViewTree);
				} else if (worldMap[playerLoc.y][playerLoc.x - row].equals("chest")) {
					Image tree = loadImage(CHEST_IMAGE);
					ImageView imageViewTree = new ImageView();
					imageViewTree.setImage(tree);

					imageViewTree.setFitHeight(height);
					imageViewTree.setFitWidth(width);
					// here is where we need to do the logic
					imageViewTree.setX(x0);
					imageViewTree.setY(y0);
					group.getChildren().add(imageViewTree);
				}
			}
			// this will be the sqares to the left but is realy get the
			// right side on the 2d map
			col = playerLoc.x;
			for (int j = 0; j <= squaresToLeft; j++) {
				if (worldMap[playerLoc.y + j][col - row].equals("tree")
						|| worldMap[playerLoc.y + j][col - row].equals("chest")) {
					// System.out.println(col + j + " " + worldMap[row][col +
					// j]);
					// left side logic here
					double previouWidthOfSquare = (x2 - x3);
					// this is the bot part of the tile which is just placed
					// to the
					// left of the facing tile.
					double tempLeftx3 = x3 - (j * previouWidthOfSquare);
					double tempLefty3 = y3;
					double tempLeftx2 = x2 - (j * previouWidthOfSquare);
					double tempLefty2 = y2;
					// this is the top part to the tile and will be scaled
					// on the
					// left from larger
					// to smaller
					double tempLeftx1 = nowStartX + nowWidthOfSquare - (j * nowWidthOfSquare);
					double tempLefty1 = topLine - tileHeight * Math.pow(scaleY, row + 1);
					double tempLeftx0 = nowStartX - j * nowWidthOfSquare;
					double tempLefty0 = topLine - tileHeight * Math.pow(scaleY, row + 1);

					double heightleft = (tempLefty3 - tempLefty0);
					double widthleft = (tempLeftx2 - tempLeftx3);
					if (worldMap[playerLoc.y + j][col - row].equals("tree")) {
						Image tree = loadImage(TREE_IMAGE);
						ImageView imageViewTree = new ImageView();
						imageViewTree.setImage(tree);

						imageViewTree.setFitHeight(heightleft);
						imageViewTree.setFitWidth(widthleft);

						imageViewTree.setX(tempLeftx0);
						imageViewTree.setY(tempLefty0);
						group.getChildren().add(imageViewTree);
					} else if (worldMap[playerLoc.y + j][col - row].equals("chest")) {
						// TODO: could
						Image tree = loadImage(CHEST_IMAGE);
						ImageView imageViewTree = new ImageView();
						imageViewTree.setImage(tree);

						imageViewTree.setFitHeight(heightleft);
						imageViewTree.setFitWidth(widthleft);
						// here is where we need to do the logic
						imageViewTree.setX(tempLeftx0);
						imageViewTree.setY(tempLefty0);
						group.getChildren().add(imageViewTree);
					}
				}

			}

			col = playerLoc.x;
			// this is the right side on the screen but the left side on the
			// board
			for (int j = 0; j <= squaresToRight; j++) {
				if (worldMap[playerLoc.y - j][col - row].equals("tree")
						|| worldMap[playerLoc.y - j][col - row].equals("chest")) {

					// this will get the width of the square that is the on
					// facing
					// the player, the width will be the bot part of the facing
					// player square.
					double previouWidthOfSquare = (x2 - x3);
					// this is the bot part of the tile which is just placed to
					// the
					// left of the facing tile.
					double tempRightx3 = x3 + (j * previouWidthOfSquare);
					// this is to force the polygons to draw in range of the
					// screen

					double tempRighty3 = y3;
					double tempRightx2 = x2 + (j * previouWidthOfSquare);

					double tempRighty2 = y2;
					double tempRightx1 = nowStartX + nowWidthOfSquare + j * nowWidthOfSquare;

					// this is the top part to the tile and will be scaled on
					// the
					// left from larger
					// to smaller

					double tempRightY1 = topLine - tileHeight * Math.pow(scaleY, row + 1);
					double tempRightx0 = nowStartX + j * nowWidthOfSquare;

					double tempRighty0 = topLine - tileHeight * Math.pow(scaleY, row + 1);
					double heightRight = (tempRighty3 - tempRighty0);
					double widthRight = (tempRightx2 - tempRightx3);

					if (worldMap[playerLoc.y - j][col - row].equals("tree")) {
						Image tree = loadImage(TREE_IMAGE);
						ImageView imageViewTree = new ImageView();
						// imageViewTree.toBack();
						imageViewTree.setImage(tree);
						// no idea why the first image on the right is not
						// draw
						imageViewTree.setFitHeight(heightRight);
						imageViewTree.setFitWidth(widthRight);
						// here is where we need to do the logic
						imageViewTree.setX(tempRightx0);
						imageViewTree.setY(tempRighty0);
						group.getChildren().add(imageViewTree);
					} else if (worldMap[playerLoc.y - j][col - row].equals("chest")) {
						Image tree = loadImage(CHEST_IMAGE);
						ImageView imageViewTree = new ImageView();
						imageViewTree.setImage(tree);

						imageViewTree.setFitHeight(heightRight);
						imageViewTree.setFitWidth(widthRight);
						// here is where we need to do the logic
						imageViewTree.setX(tempRightx0);
						imageViewTree.setY(tempRighty0);
						group.getChildren().add(imageViewTree);
					}

				}
			}

			// update the bot right point to previous tiles top part which will
			// also be the top left part in the prevous tile
			x3 = nowStartX;
			y3 = topLine - tileHeight * Math.pow(scaleY, row + 1);

			// update the bot left point to the previous tiles top part which
			// will also be the top right part in the previous tile
			x2 = nowStartX + nowWidthOfSquare;
			y2 = topLine - tileHeight * Math.pow(scaleY, row + 1);
			// prevTopLine = topLine;
			// this updates the width of the next topline which will be used to
			// calc x0 , x1 so the top part
			topLine = topLine - tileHeight * Math.pow(scaleY, row + 1);

		}
	}

	/**
	 * this method is used to render the object in the game that are to the
	 * north of the player..
	 */
	public void renderObjectsWest() {
		String[][] worldMap = mapParser.getMap();
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
		// it scaling by thinking it throught away make 0 some how and it will
		// work ******** it moving down quicker now
		// the 10 will be the sqare in front of the players
		// row == cols in this case
		for (int row = 0; row < squaresInFront; row++) {
			int col = playerLoc.x;
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, row + 1);
			// this is the top part of the tile starting x pos
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			// front view
			if (worldMap[playerLoc.y][playerLoc.x + row].equals("tree")
					|| worldMap[playerLoc.y][playerLoc.x + row].equals("chest")) {
				// this will draw the object that are in front
				double x1 = nowStartX + nowWidthOfSquare;
				double y1 = topLine - tileHeight * Math.pow(scaleY, row + 1);
				double x0 = nowStartX;
				double y0 = topLine - tileHeight * Math.pow(scaleY, row + 1);
				double height = (y3 - y0);
				double width = (x2 - x3);

				if (worldMap[playerLoc.y][playerLoc.x + row].equals("tree")) {
					Image tree = loadImage(TREE_IMAGE);
					ImageView imageViewTree = new ImageView();
					imageViewTree.setImage(tree);

					imageViewTree.setFitHeight(height);
					imageViewTree.setFitWidth(width);
					// here is where we need to do the logic
					imageViewTree.setX(x0);
					imageViewTree.setY(y0);
					group.getChildren().add(imageViewTree);
				} else if (worldMap[playerLoc.y][playerLoc.x + row].equals("chest")) {
					Image tree = loadImage(CHEST_IMAGE);
					ImageView imageViewTree = new ImageView();
					imageViewTree.setImage(tree);

					imageViewTree.setFitHeight(height);
					imageViewTree.setFitWidth(width);
					// here is where we need to do the logic
					imageViewTree.setX(x0);
					imageViewTree.setY(y0);
					group.getChildren().add(imageViewTree);
				}
			}
			// this will be the sqares to the left but is realy get the
			// right side on the 2d map
			col = playerLoc.x;
			for (int j = 0; j <= squaresToLeft; j++) {
				// System.out.println(row + " " + (col + j) + " " +
				// worldMap[row][col + j]);
				if (worldMap[playerLoc.y - j][col + row].equals("tree")
						|| worldMap[playerLoc.y - j][col + row].equals("chest")) {

					// left side logic here
					double previouWidthOfSquare = (x2 - x3);
					// this is the bot part of the tile which is just placed
					// to the
					// left of the facing tile.
					double tempLeftx3 = x3 - (j * previouWidthOfSquare);
					double tempLefty3 = y3;
					double tempLeftx2 = x2 - (j * previouWidthOfSquare);
					double tempLefty2 = y2;
					// this is the top part to the tile and will be scaled
					// on the
					// left from larger
					// to smaller
					double tempLeftx1 = nowStartX + nowWidthOfSquare - (j * nowWidthOfSquare);
					double tempLefty1 = topLine - tileHeight * Math.pow(scaleY, row + 1);
					double tempLeftx0 = nowStartX - j * nowWidthOfSquare;
					double tempLefty0 = topLine - tileHeight * Math.pow(scaleY, row + 1);

					double heightleft = (tempLefty3 - tempLefty0);
					double widthleft = (tempLeftx2 - tempLeftx3);
					if (worldMap[playerLoc.y - j][col + row].equals("tree")) {
						Image tree = loadImage(TREE_IMAGE);
						ImageView imageViewTree = new ImageView();
						imageViewTree.setImage(tree);

						imageViewTree.setFitHeight(heightleft);
						imageViewTree.setFitWidth(widthleft);

						imageViewTree.setX(tempLeftx0);
						imageViewTree.setY(tempLefty0);
						group.getChildren().add(imageViewTree);
					} else if (worldMap[playerLoc.y - j][col + row].equals("chest")) {
						// TODO: could
						Image tree = loadImage(CHEST_IMAGE);
						ImageView imageViewTree = new ImageView();
						imageViewTree.setImage(tree);

						imageViewTree.setFitHeight(heightleft);
						imageViewTree.setFitWidth(widthleft);
						// here is where we need to do the logic
						imageViewTree.setX(tempLeftx0);
						imageViewTree.setY(tempLefty0);
						group.getChildren().add(imageViewTree);
					}
				}

			}

			col = playerLoc.x;
			// this is the right side on the screen but the left side on the
			// board
			for (int j = 0; j <= squaresToRight; j++) {
				if (worldMap[playerLoc.y + j][col + row].equals("tree")
						|| worldMap[playerLoc.y + j][col + row].equals("chest")) {

					// this will get the width of the square that is the on
					// facing
					// the player, the width will be the bot part of the facing
					// player square.
					double previouWidthOfSquare = (x2 - x3);
					// this is the bot part of the tile which is just placed to
					// the
					// left of the facing tile.
					double tempRightx3 = x3 + (j * previouWidthOfSquare);
					// this is to force the polygons to draw in range of the
					// screen

					double tempRighty3 = y3;
					double tempRightx2 = x2 + (j * previouWidthOfSquare);

					double tempRighty2 = y2;
					double tempRightx1 = nowStartX + nowWidthOfSquare + j * nowWidthOfSquare;

					// this is the top part to the tile and will be scaled on
					// the
					// left from larger
					// to smaller

					double tempRightY1 = topLine - tileHeight * Math.pow(scaleY, row + 1);
					double tempRightx0 = nowStartX + j * nowWidthOfSquare;

					double tempRighty0 = topLine - tileHeight * Math.pow(scaleY, row + 1);
					double heightRight = (tempRighty3 - tempRighty0);
					double widthRight = (tempRightx2 - tempRightx3);

					if (worldMap[playerLoc.y + j][col + row].equals("tree")) {
						Image tree = loadImage(TREE_IMAGE);
						ImageView imageViewTree = new ImageView();
						// imageViewTree.toBack();
						imageViewTree.setImage(tree);
						// no idea why the first image on the right is not
						// draw
						imageViewTree.setFitHeight(heightRight);
						imageViewTree.setFitWidth(widthRight);
						// here is where we need to do the logic
						imageViewTree.setX(tempRightx0);
						imageViewTree.setY(tempRighty0);
						group.getChildren().add(imageViewTree);
					} else if (worldMap[playerLoc.y + j][col + row].equals("chest")) {
						Image tree = loadImage(CHEST_IMAGE);
						ImageView imageViewTree = new ImageView();
						imageViewTree.setImage(tree);

						imageViewTree.setFitHeight(heightRight);
						imageViewTree.setFitWidth(widthRight);
						// here is where we need to do the logic
						imageViewTree.setX(tempRightx0);
						imageViewTree.setY(tempRighty0);
						group.getChildren().add(imageViewTree);
					}

				}
			}

			// update the bot right point to previous tiles top part which will
			// also be the top left part in the prevous tile
			x3 = nowStartX;
			y3 = topLine - tileHeight * Math.pow(scaleY, row + 1);

			// update the bot left point to the previous tiles top part which
			// will also be the top right part in the previous tile
			x2 = nowStartX + nowWidthOfSquare;
			y2 = topLine - tileHeight * Math.pow(scaleY, row + 1);
			// prevTopLine = topLine;
			// this updates the width of the next topline which will be used to
			// calc x0 , x1 so the top part
			topLine = topLine - tileHeight * Math.pow(scaleY, row + 1);

		}
	}

	public void renderMiniMap() {

	}

	/**
	 * this method will render the aviator to the screen
	 */
	public void renderAvatar() {
		// TODO: render the avartar to the center of the screen
	}

	/**
	 * this method is used to move the player in the screen given player, it
	 * then calls the render method. so when a player makes a move there stats
	 * will be updated and based on this a string will be passed containg this
	 * update information which is used to render the player in tand the
	 *map<Interger user id, Avatar enum> map<Integer user id, Position>
	 * objectss in the right place. //int x int y , Direction .. , int area ID
	 * 
	 * @param player
	 */
	public void updatePlayer(String info) {
		// TODO: this method wil need to be changed based on the input string
		// given by the client but for no testing this will do.
		String[] temp = info.split(",");
		playerLoc = new Point(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
		direction = Integer.parseInt(temp[2]);
		System.out.println(playerLoc.x + " " + playerLoc.y);
		if (direction == 0) {
			System.out.println("North");
			// north
			moveNorth();
			renderNorth();
			renderObjectsNorth();
		} else if (direction == 1) {
			System.out.println("East");
			// east
			moveEast();
			renderEast();
			renderObjectsEast();

		} else if (direction == 2) {
			System.out.println("South");
			// south
			moveSouth();
			renderSouth();
			renderObjectsSouth();
		} else if (direction == 3) {
			System.out.println("West");
			// west
			moveWest();
			renderWest();
			renderObjectsWest();
		}
	}

	private void moveNorth() {
		squaresInFront = boardSize - playerLoc.y;
		squaresToLeft = (boardSize - playerLoc.x) - 1;
		squaresToRight = (boardSize - squaresToLeft);
	}

	private void moveWest() {
		squaresInFront = boardSize - playerLoc.x;
		squaresToRight = (boardSize - playerLoc.y);
		squaresToLeft = (boardSize - squaresToRight);
		// could be off by 1
		squaresToRight = squaresToRight - 1;
	}

	private void moveEast() {
		int tempSquares=boardSize - playerLoc.x;
		squaresInFront = boardSize - tempSquares;
		squaresToLeft = (boardSize - playerLoc.y);
		squaresToRight = (boardSize - squaresToLeft);
		// could be off by one
//System.out.println(squaresInFront);
		squaresToLeft = squaresToLeft - 1;
	}

	private void moveSouth() {
		int tempSquares = boardSize - playerLoc.y;
		squaresInFront = boardSize - tempSquares;
		squaresToRight = (boardSize - playerLoc.x) - 1;
		squaresToLeft = (boardSize - squaresToRight);
		// could be off by one
	}

	/**
	 * this method is used to set the group which is used to render the images
	 * on the screen and will be used by the gui class
	 * 
	 * @param renderGroup
	 */
	// public void setGroup(Group renderGroup) {
	// this.group = renderGroup;
	// }
	public void setGroup(Pane renderGroup) {
		this.group = renderGroup;
	}

	/**
	 * this is just a helper method which is used to load images
	 * 
	 * @param name
	 * @return
	 */
	private Image loadImage(String name) {
		Image image = new Image(this.getClass().getResourceAsStream(name));
		return image;
	}

	@Override
	public String toString() {
		return "renderclass";
	}

}
