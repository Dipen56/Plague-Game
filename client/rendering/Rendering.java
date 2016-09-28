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
	private int gamePanelWidth = GUI.GAMEPANE_WIDTH_VALUE - 3; // 3 x alignment
																// of group
	private int tileWidth = 200;
	private int tileHeight = 60;
	public double centerWidth = gamePanelWidth / 2;
	public double centerHeight = gamePaneHeight;
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

	int boardSize = 10;

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
	public void render(Group renderGroup, Map<Integer, Position> positions, char[][] worldMap, int visibility,
			int uid) {
		// player's coordinate on board, and direction.
		// need to get position from param (uid)
		// Position selfPosition = positions.get(uid);
		Position selfPosition = new Position(5, 10, 1, Direction.North);
		int x = selfPosition.x;
		int y = selfPosition.y;
		Direction direction = selfPosition.getDirection();

		Image background = loadImage(BACKGROUND_IMAGE);
		Image grass = loadImage(GRASS_IMAGE);
		addImage(renderGroup, background, gamePanelWidth + 3, gamePaneHeight + 35, 0, 0);
		setNumSquares(direction);
		double xRight = centerWidth + tileWidth / 2;
		double yBottom = centerHeight;
		double xLeft = centerWidth - tileWidth / 2;
		// ===================================================================================================
		// Below this is point is the code for all the rendering for first
		// person
		// ====================================================================================================
		for (int i = 0; i < squaresInFront; i++) {
			Polygon squareFront = new Polygon();
			squareFront.setFill(new ImagePattern(grass));
			squareFront.setLayoutY(10);
			double currentTileWidth = tileWidth * Math.pow(scaleY, i + 1);
			double previousTileWidth = Math.abs(xRight - xLeft);
			double xLeftTop = centerWidth - currentTileWidth / 2;
			double yTop = yBottom - tileHeight * Math.pow(scaleY, i + 1);
			addTile(squareFront, xLeft, yBottom, xRight, yTop, xLeftTop + currentTileWidth, xLeftTop, renderGroup);
			for (int j = 0; j < squaresToLeft; j++) {
				Polygon squareLeft = new Polygon();
				squareLeft.setLayoutY(10);
				squareLeft.setFill(new ImagePattern(grass));
				addTile(squareLeft, xLeft - (j * previousTileWidth), yBottom, xRight - (j * previousTileWidth), yTop,
						xLeftTop + currentTileWidth - j * currentTileWidth, xLeftTop - j * currentTileWidth,
						renderGroup);
			}
			for (int j = 0; j < squaresToRight; j++) {
				Polygon squareRight = new Polygon();
				squareRight.setFill(new ImagePattern(grass));
				squareRight.setLayoutY(10);
				double tileXLeftBottom = xLeft + (j * previousTileWidth);
				if (tileXLeftBottom > gamePanelWidth) {
					tileXLeftBottom = gamePanelWidth;
				}
				double tileXRightBottom = xRight + (j * previousTileWidth);
				if (tileXRightBottom > gamePanelWidth) {
					tileXRightBottom = gamePanelWidth;
				}
				double tileXRightTop = xLeftTop + currentTileWidth + j * currentTileWidth;
				if (tileXRightTop > gamePanelWidth) {
					tileXRightTop = gamePanelWidth;
				}
				double tileXLeftTop = xLeftTop + j * currentTileWidth;
				if (tileXLeftTop > gamePanelWidth) {
					tileXLeftTop = gamePanelWidth;
				}

				addTile(squareRight, tileXLeftBottom, yBottom, yTop, tileXRightBottom, tileXRightTop, tileXLeftTop,
						renderGroup);
			}
			xLeft = xLeftTop;
			xRight = xLeftTop + currentTileWidth;
			yBottom = yTop;
		}
		renderObjects(renderGroup, worldMap, direction, x, y);
		charRender();
	}

	private void addTile(Polygon p, double xLeftBottom, double yBottom, double yTop, double xRightBottom,
			double xRightTop, double xLeftTop, Group renderGroup) {
		p.getPoints().add(xLeftBottom);
		p.getPoints().add(yBottom);
		p.getPoints().add(xRightBottom);
		p.getPoints().add(yBottom);
		// this is the top part to the tile and will be scaled from larger
		// to smaller
		// /__\
		p.getPoints().add(xRightTop);
		p.getPoints().add(yTop);
		p.getPoints().add(xLeftTop);
		p.getPoints().add(yTop);
		p.setStroke(javafx.scene.paint.Color.AQUA);
		p.setStrokeWidth(1);
		renderGroup.getChildren().add(p);
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
	public void renderObjects(Group renderGroup, char[][] worldMap, Direction direction, int playerX, int playerY) {
		switch (direction) {
		case North:
			gatherObjectsNorth(renderGroup, worldMap, 0, 0, worldMap.length - 1, playerY, playerX, playerY);
			break;
		case East:
			gatherObjectsEast(renderGroup, worldMap, worldMap.length - 1, 0, playerX, worldMap.length - 1, playerX,
					playerY);
			break;
		case South:
			gatherObjectsSouth(renderGroup, worldMap, worldMap.length - 1, worldMap.length - 1, 0, playerY, playerX,
					playerY);
			break;
		case West:
			gatherObjectsWest(renderGroup, worldMap, 0, worldMap.length - 1, playerX, 0, playerX, playerY);
			break;
		}
	}

	// North facing
	private void gatherObjectsNorth(Group renderGroup, char[][] worldMap, int startX, int startY, int endX, int endY,
			int playerX, int playerY) {
		for (int rows = startX; rows <= endX; rows++) {
			int scale = getScale(playerY, rows);
			for (int cols = startY; cols <= endX; cols++) {
				Image image = getImageFromChar(worldMap[rows][cols], rows, cols);
				if (image != null) {
					Point point = getImagePoint(scale, image, playerX, playerY, rows, cols);
					addImage(renderGroup, image, (image.getWidth() / scale), (image.getHeight() / scale), point.x,
							point.y);
				}
			}
		}
	}

	// East Facing
	private void gatherObjectsEast(Group renderGroup, char[][] worldMap, int startX, int startY, int endX, int endY,
			int playerX, int playerY) {
		for (int cols = startX; cols <= endX; cols--) {
			int scale = getScale(playerY, cols);
			for (int rows = startY; rows <= endX; rows++) {
				Image image = getImageFromChar(worldMap[rows][cols], rows, cols);
				Point point = getImagePoint(scale, image, playerX, playerY, rows, cols);
				addImage(renderGroup, image, (image.getWidth() / scale), (image.getHeight() / scale), point.x, point.y);
			}
		}
	}

	// South Facing
	private void gatherObjectsSouth(Group renderGroup, char[][] worldMap, int startX, int startY, int endX, int endY,
			int playerX, int playerY) {
		for (int rows = startX; rows <= endX; rows--) {
			int scale = getScale(playerY, rows);
			for (int cols = startY; cols <= endX; cols--) {
				Image image = getImageFromChar(worldMap[rows][cols], rows, cols);
				Point point = getImagePoint(scale, image, playerX, playerY, rows, cols);
				addImage(renderGroup, image, (image.getWidth() / scale), (image.getHeight() / scale), point.x, point.y);
			}
		}
	}

	// West facing
	private void gatherObjectsWest(Group renderGroup, char[][] worldMap, int startX, int startY, int endX, int endY,
			int playerX, int playerY) {
		for (int cols = startX; cols <= endX; cols++) {
			int scale = getScale(playerY, cols);
			for (int rows = startY; rows <= endX; rows--) {
				Image image = getImageFromChar(worldMap[rows][cols], rows, cols);
				Point point = getImagePoint(scale, image, playerX, playerY, rows, cols);
				addImage(renderGroup, image, (image.getWidth() / scale), (image.getHeight() / scale), point.x, point.y);
			}
		}
	}

	// Needs more thought
	private int getScale(int playerY, int rows) {
		return playerY - rows;
	}

	private Image getImageFromChar(char input, int rows, int cols) {
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

	private void addImage(Group renderGroup, Image image, double width, double height, double setX, double setY) {
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
