package client.rendering;

import java.awt.Point;
import java.util.Map;

import javax.swing.ImageIcon;

import client.view.ClientUI;
import client.view.GUI;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import server.game.player.Direction;
import server.game.player.Player;
import server.game.player.Position;
import server.game.world.Area;
import javafx.scene.paint.ImagePattern;

/**
 * This class represents the main rendering class, this class will control the
 * rendering of the game board, character, and objects.
 *
 * @author Angelo & Dipen
 *
 */

public class Rendering {
	private static final String PLAYER_IMAGE = "/standingstillrear.png";
	// private static final String BACKGROUND_IMAGE = "/background.gif";
	private static final String BACKGROUND_IMAGE = "/night.jpg";
	private static final String GRASS_IMAGE = "/grass.png";
	private static final String TREE_IMAGE = "/tree.png";
	private static final String CHEST_IMAGE = "/chest.png";

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
	private Point playerLoc = new Point(5, 1);
	private int squaresInFront = 0;
	private int squaresToLeft = 0;
	private int squaresToRight = 0;
	private Player player;
	private Map<Integer, Player> playersOnMap;
	private Map<Integer, Area> map;
	private int avatarID;
	private String direction;

	Group renderGroup;
	int boardSize = 10;

	// for the renderer to get informations from client side controller
	private ClientUI controller;

	public Rendering(ClientUI controller) {
		this.controller = controller;
	}

	public Rendering() {
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
	public Rendering(Player player, Map<Integer, Player> playersOnMap, Map<Integer, Area> map, int avatarID) {
		this.player = player;
		this.playersOnMap = playersOnMap;
		this.map = map;
		this.avatarID = avatarID;
	}

	/**
	 * Redraw the rendering panel.
	 *
	 * @param positions
	 *            --- the position of all player.
	 * @param areaMap
	 *            --- the area map represented as a char[][]
	 * @param visibility
	 *            --- current visibility.
	 */
	private void redraw(Map<Integer, Position> positions, char[][] areaMap, int visibility) {
		// player's coordinate on board, and direction.
		Position selfPosition = positions.get(controller.getUid());
		int x = selfPosition.x;
		int y = selfPosition.y;
		Direction direction = selfPosition.getDirection();
		// TODO redraw the rendering panel
	}

	/**
	 * this method is used to render the game
	 *
	 * @param renderGroup
	 */
	public void render(Map<Integer, Position> positions, char[][] areaMap, int visibility) {
		// player's coordinate on board, and direction.
		Position selfPosition = positions.get(controller.getUid());
		int x = selfPosition.x;
		int y = selfPosition.y;
		Direction direction = selfPosition.getDirection();
		// TODO redraw the rendering panel
		// TODO: get ride of the renderGroup parameters call the set group
		// method below first before calling this method
		Image image = loadImage(BACKGROUND_IMAGE);
		Image grass = loadImage(GRASS_IMAGE);
		ImageView imageViewNight = new ImageView();
		addImage(image, imageViewNight, renderGroup, gamePaneWidth + 3, gamePaneHeight + 35, 0, 0);
		setNumSquares(direction);
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
			renderGroup.getChildren().add(p);
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
				renderGroup.getChildren().add(p2);
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
				p3.setStroke(javafx.scene.paint.Color.AQUA);
				p3.setStrokeWidth(1);
				renderGroup.getChildren().add(p3);
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
			// Renders the player onto the board
		}
		renderObjects(renderGroup, direction, x, y);
		charRender();
	}

	private void setNumSquares(Direction direction) {
		switch (direction) {
		// needs to be switched over to integers , 0 = north, 1 = east, 2 =
		// south, 3 = west
		case North:
			squaresInFront = boardSize - playerLoc.y;
			squaresToLeft = (boardSize - playerLoc.x) - 1;
			squaresToRight = (boardSize - squaresToLeft);
			break;
		case East:
			squaresToLeft = squaresInFront;
			squaresInFront = squaresToRight;
			squaresToRight = (boardSize - squaresToLeft);
			break;
		case South:
			// To do
			break;
		case West:
			squaresToRight = squaresInFront;
			squaresInFront = squaresToLeft;
			squaresToLeft = boardSize - squaresToRight;
			break;
		}
	}

	public void charRender() {

	}

	// 1) need to fix how the array iterates
	// 2) need to get the x points of each image (hard due to scalling)

	/**
	 * this method is used to render the object in the game.
	 * 
	 * @param renderGroup
	 * 
	 * @param renderGroup
	 * @param y
	 * @param x
	 */
	public void renderObjects(Group renderGroup, Direction direction, int playerX, int playerY) {
		String[][] worldMap = mapParser.getMap();
		switch (direction) {
		case North:
			gatherObjectsNorth(worldMap, renderGroup, 0, 0, worldMap.length, playerY, playerX, playerY);
			break;
		case East:
			gatherObjectsEast(worldMap, renderGroup, worldMap.length, 0, playerX, worldMap.length, playerX, playerY);
			break;
		case South:
			gatherObjectsSouth(worldMap, renderGroup, worldMap.length, worldMap.length, 0, playerY, playerX, playerY);
			break;
		case West:
			gatherObjectsWest(worldMap, renderGroup, 0, worldMap.length, playerX, 0, playerX, playerY);
			break;
		}
	}

	// Need to fix how the for-loop iterates
	private void gatherObjectsNorth(String[][] worldMap, Group renderGroup, int startX, int startY, int endX, int endY,
			int playerX, int playerY) {
		for (int rows = startX; startX <= endX; startX++) {
			int scaleY = getScaleY(playerY, rows);
			for (int cols = startY; startY <= endX; startY++) {
				int scaleX = getScaleX(playerX, cols);
				Image image = getImageFromChar(worldMap[rows][cols], rows, cols);
				Point p = getImagePoint(scaleY, image, playerX, playerY, rows, cols);
				ImageView views = new ImageView();
				// need to fix width...
				addImage(image, views, renderGroup, (image.getWidth() / scaleX), (image.getHeight() / scaleY), p.x,
						p.y);
			}
		}
	}

	private void gatherObjectsEast(String[][] worldMap, Group renderGroup, int startX, int startY, int endX, int endY,
			int playerX, int playerY) {
		for (int rows = startX; startX <= endX; startX++) {
			int scaleY = getScaleY(playerY, rows);
			for (int cols = startY; startY <= endX; startY++) {
				int scaleX = getScaleX(playerX, cols);
				Image image = getImageFromChar(worldMap[rows][cols], rows, cols);
				Point p = getImagePoint(scaleY, image, playerX, playerY, rows, cols);
				ImageView views = new ImageView();
				// need to fix width...
				addImage(image, views, renderGroup, (image.getWidth() / scaleX), (image.getHeight() / scaleY), p.x,
						p.y);
			}
		}
	}

	private void gatherObjectsSouth(String[][] worldMap, Group renderGroup, int startX, int startY, int endX, int endY,
			int playerX, int playerY) {
		for (int rows = startX; startX <= endX; startX++) {
			int scaleY = getScaleY(playerY, rows);
			for (int cols = startY; startY <= endX; startY++) {
				int scaleX = getScaleX(playerX, cols);
				Image image = getImageFromChar(worldMap[rows][cols], rows, cols);
				Point p = getImagePoint(scaleY, image, playerX, playerY, rows, cols);
				ImageView views = new ImageView();
				// need to fix width...
				addImage(image, views, renderGroup, (image.getWidth() / scaleX), (image.getHeight() / scaleY), p.x,
						p.y);
			}
		}
	}

	private void gatherObjectsWest(String[][] worldMap, Group renderGroup, int startX, int startY, int endX, int endY,
			int playerX, int playerY) {
		for (int rows = startX; startX <= endX; startX++) {
			int scaleY = getScaleY(playerY, rows);
			for (int cols = startY; startY <= endX; startY++) {
				int scaleX = getScaleX(playerX, cols);
				Image image = getImageFromChar(worldMap[rows][cols], rows, cols);
				Point p = getImagePoint(scaleY, image, playerX, playerY, rows, cols);
				ImageView views = new ImageView();
				// need to fix width...
				addImage(image, views, renderGroup, (image.getWidth() / scaleX), (image.getHeight() / scaleY), p.x,
						p.y);
			}
		}
	}

	// Needs more thought
	private int getScaleY(int playerY, int rows) {
		return playerY - rows;
	}

	private int getScaleX(int playerX, int cols) {
		return playerX - cols;
	}

	private Image getImageFromChar(String input, int rows, int cols) {
		Image image = null;
		switch (input) {
		// Unsure if its trees or T
		case "tree":
			image = loadImage(TREE_IMAGE);
			break;
		case "chest":
			image = loadImage(CHEST_IMAGE);
			break;
		}
		return image;
	}

	// Get points of each image on the board, based on the rows and cols of the
	// image location, and being relative to the players position
	private Point getImagePoint(int scale, Image image, int playerX, int playerY, int rows, int cols) {
		int height = 60;
		int yCoordinate = playerY;
		for (int i = scale; i > 0; i--) {
			yCoordinate += height / i;
		}
		// need to fix the xCoordinate
		Point point = new Point(10, yCoordinate);
		return point;
	}

	private void addImage(Image image, ImageView imageView, Group renderGroup, double width, double height, double setX,
			double setY) {
		imageView.setImage(image);
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		imageView.setX(setX);
		imageView.setY(setY);
		renderGroup.getChildren().add(imageView);
	}

	private Image loadImage(String name) {
		Image image = new Image(this.getClass().getResourceAsStream(name));
		return image;
	}

	public void setDirection(String dir) {
		this.direction = dir;
	}

	/**
	 * this method will render the aviator to the screen
	 */
	public void renderAvatar() {
		// TODO: render the avartar to the center of the screen
	}

	/**
	 * this method is used to move the player in the screen given player, it
	 * then calls the render method
	 *
	 * @param player
	 */
	public void movePlayer(Player player) {
		this.player = player;
		// TODO: call render;
		// render();
	}

	@Override
	public String toString() {
		return "renderclass";
	}

}
