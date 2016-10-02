package client.rendering;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import client.view.ClientUI;
import client.view.GUI;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
	public double scaleY = 1.2; // lower number less scaling
	public double scaleX = 1.2; // lower number less scaling
	// 35 y alignment of group
	private int gamePaneHeight = GUI.HEIGHT_VALUE - 200;
	// 3 x alignment of group
	private int gamePanelWidth = GUI.GAMEPANE_WIDTH_VALUE - 3;
	private int tileWidth = 45;
	private int tileHeight = 15;
	public double centerWidth = gamePanelWidth / 2;
	public double centerHeight = gamePaneHeight;
	private MapParser mapParser;
	private Point playerLoc = new Point(4, 0);
	private int squaresInFront = 0;
	private int squaresToLeft = 0;
	private int squaresToRight = 0;
	private Player player;
	private Map<Integer, Player> playersOnMap;
	private Map<Integer, Area> map;
	private int avatarID;
	private String direction;
	// private int boardSize = 10;

	public Rendering() {
		// will need to get board size passed in
		// mapParser = new MapParser(10, 10);
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

	/**
	 * this method is used to render the game
	 *
	 * @param renderGroup
	 */
	// public void render(Pane renderGroup, Map<Integer, Position> positions,
	// char[][] worldMap, int visibility, int uid) {
	public void render(Pane renderGroup, Position pos, char[][] worldMap, int visibility, int uid) {
		// player's coordinate on board, and direction.
		// need to get position from param (uid)
		// Position selfPosition = positions.get(uid);
		// Position selfPosition = new Position(5, 10, 1, Direction.North);
		int x = pos.x;
		int y = pos.y;
		Direction direction = pos.getDirection();
		Image background = loadImage(BACKGROUND_IMAGE);
		Image grass = loadImage(GRASS_IMAGE);
		addImage(renderGroup, background, gamePanelWidth + 3, gamePaneHeight, 0, 0);
		setNumSquares(worldMap.length, worldMap[0].length, direction);
		double xRightTop = centerWidth + tileWidth / 2;
		double yTop = getTopOffset();
		double xLeftTop = centerWidth - tileWidth / 2;
		double currentTileWidth = tileWidth * scaleX;
		double previousTileWidth = Math.abs(xRightTop - xLeftTop);
		double currentTileHeight = tileHeight;
		// ===================================================================================================
		// Below this is point is the code for all the rendering for first
		// person
		// ====================================================================================================
		for (int row = 0; row < squaresInFront; row++) {
			Polygon squareFront = new Polygon();
			squareFront.setFill(new ImagePattern(grass));
			squareFront.setLayoutY(10);
			double xLeftBottom = centerWidth - currentTileWidth / 2;
			double yBottom = yTop + currentTileHeight * scaleY;
			addTile(squareFront, xLeftTop, xRightTop, xLeftBottom + currentTileWidth, xLeftBottom, yBottom, yTop,
					renderGroup);
			addObject(xLeftTop, yBottom, xRightTop, row, playerLoc.x, "middle", worldMap,
					renderGroup);
			for (int col = 0; col < squaresToLeft; col++) {
				Polygon squareLeft = new Polygon();
				squareLeft.setLayoutY(10);
				squareLeft.setFill(new ImagePattern(grass));
				double tileXLeftTop = xLeftTop - previousTileWidth - (col * previousTileWidth);
				double tileXRightTop = xLeftTop - (col * previousTileWidth);
				double tileXRightBottom = xLeftBottom - col * currentTileWidth;
				double tileXLeftBottom = xLeftBottom - currentTileWidth - (col * currentTileWidth);
				addTile(squareLeft, tileXLeftTop, tileXRightTop, tileXRightBottom, tileXLeftBottom, yBottom, yTop,
						renderGroup);
				addObject(tileXLeftBottom, yBottom, tileXRightTop, row, col, "left", worldMap, renderGroup);
			}
			for (int col = 0; col < squaresToRight; col++) {
				Polygon squareRight = new Polygon();
				squareRight.setFill(new ImagePattern(grass));
				squareRight.setLayoutY(10);
				double tileXLeftTop = xLeftTop + previousTileWidth + (col * previousTileWidth);
				double tileXRightTop = xLeftTop + (previousTileWidth * 2) + (col * previousTileWidth);
				double tileXRightBottom = xLeftBottom + (currentTileWidth * 2) + (col * currentTileWidth);
				double tileXLeftBottom = xLeftBottom + currentTileWidth + (col * currentTileWidth);
				addTile(squareRight, tileXLeftTop, tileXRightTop, tileXRightBottom, tileXLeftBottom, yBottom, yTop,
						renderGroup);
				addObject(tileXLeftTop, yBottom, tileXRightBottom, row, col, "right", worldMap, renderGroup);
			}
			xLeftTop = xLeftBottom;
			xRightTop = xLeftBottom + currentTileWidth;
			yTop = yBottom;
			previousTileWidth = currentTileWidth;
			currentTileWidth = currentTileWidth * scaleX;
			currentTileHeight = currentTileHeight * scaleY;
		}
	}

	private double getTopOffset() {
		double count = 0;
		for (int i = 0; i < squaresInFront; i++) {
			count += tileHeight * Math.pow(scaleY, i);
		}
		return centerHeight - count;
	}

	private void addTile(Polygon p, double xLeftTop, double xRightTop, double xRightBottom, double xLeftBottom,
			double yBottom, double yTop, Pane renderGroup) {
		p.getPoints().add(xLeftTop);
		p.getPoints().add(yTop);
		p.getPoints().add(xRightTop);
		p.getPoints().add(yTop);
		p.getPoints().add(xRightBottom);
		p.getPoints().add(yBottom);
		p.getPoints().add(xLeftBottom);
		p.getPoints().add(yBottom);
		p.setStroke(javafx.scene.paint.Color.AQUA);
		p.setStrokeWidth(1);
		renderGroup.getChildren().add(p);
	}

	private void setNumSquares(int height, int width, Direction direction) {
		switch (direction) {
		// needs to be switched over to integers , 0 = north, 1 = east, 2 =
		// south, 3 = west
		case North:
			squaresInFront = height - playerLoc.y;
			squaresToLeft = playerLoc.x;
			squaresToRight = width - playerLoc.x - 1;
			break;
		case East:
			squaresInFront = width - playerLoc.x;
			squaresToLeft = height - playerLoc.y - 1;
			squaresToRight = height - playerLoc.y - 1;
			break;
		case South:
			squaresInFront = height - playerLoc.y;
			squaresToLeft = width - playerLoc.x - 1;
			squaresToRight = playerLoc.x;
			break;
		case West:
			squaresInFront = playerLoc.x + 1;
			squaresToLeft = height - playerLoc.y - 1;
			squaresToRight = height - playerLoc.y - 1;
			break;
		}
	}

	public void charRender() {

	}

	private void addObject(double tileXLeftBottom, double yBottom, double tileXRightBottom, int row, int col,
			String side, char[][] worldMap, Pane renderGroup) {
		switch (side) {
		case "left":
			col = squaresToLeft - col - 1;
			break;
		case "right":
			col = squaresToLeft + col + 1;
			break;
		}
		char object = worldMap[row][col];
		Image image = getImageFromChar(object);
		if (image != null) {
			double height = image.getHeight() * Math.pow(0.8, squaresInFront - row - 1);
			double width = image.getWidth() * Math.pow(0.8, squaresInFront - row - 1);
			double xPoint = getImageX(width,tileXLeftBottom,tileXRightBottom);
			switch (side){
			case "left":
				addImage(renderGroup, image, width, height, xPoint, yBottom - height);
				break;
			case "right":
				addImage(renderGroup, image, width, height, xPoint, yBottom - height);				
				break;
			}
		}
	}

	private Image getImageFromChar(char input) {
		Image image = null;
		switch (input) {
		// Unsure if its trees or T
		case 'T':
			image = loadImage(TREE_IMAGE);
			break;
		case 'C':
			image = loadImage(CHEST_IMAGE);
			break;
		}
		return image;
	}

	// Get points of each image on the board, based on the rows and cols of the
	// image location, and being relative to the players position
	private double getImageX(double imageWidth, double tileXLeft, double tileXRight) {
		double tileWidth = tileXRight - tileXLeft;
		double widthOffset = Math.abs(tileWidth - imageWidth) / 2;
		if (tileWidth > imageWidth) {
			return tileXLeft + widthOffset;
		} else {
			return tileXLeft - widthOffset;
		}
	}

	private void addImage(Pane renderGroup, Image image, double width, double height, double setX, double setY) {
		ImageView imageView = new ImageView();
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
