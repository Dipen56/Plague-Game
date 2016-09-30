package client.rendering;
import static client.rendering.Images.loadImage;

import java.awt.Panel;
import java.awt.Point;
import java.util.Map;

import javax.swing.text.AbstractDocument.LeafElement;

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

/**
 * This class represents the main rendering class, this class will control the
 * rendering of the game board, character, and objects.
 * 
 * @author Dipen
 *
 */
public class Rendering {
	
	private static final String PLAYER_IMAGE = "/standingstillrear.png";
	private static final String BACKGROUND_IMAGE = "/night.jpg";
	private static final String GRASS_IMAGE = "/grass.png";
	private static final String TREE_IMAGE = "/tree.png";
	private static final String CHEST_IMAGE = "/chest.png";
	private Group group;
	public double scaleY = 0.85; // lower number less scaling
	private int gamePaneHeight = GUI.HEIGHT_VALUE - 35; // 35 y alignment of
														// group
	private int gamePaneWidth = GUI.GAMEPANE_WIDTH_VALUE - 3; // 3 x alignment
																// of group
	private int tileWidth = 200;
	private int tileHeight = 60;
	public int centerWidth = gamePaneWidth / 2;
	public int centerHeight = gamePaneHeight;
	private MapParser mapParser;
	private Point playerLoc = new Point(5, 0);
	private int squaresInFront = 0;
	private int squaresToLeft = 0;
	private int squaresToRight = 0;
	private Player player;
	private Map<Integer, Player> playersOnMap;
	private Map<Integer, Area> map;
	private int avatarID;
	private int direction;
	private int boardSize = 10;
	private double topLine_Tile;
	private double x2_Tile;
	private double y2_Tile;
	private double x3_Tile;
	private double y3_Tile;
	private String[][] worldMap;
	private double x2_Object;
	private double y3_Object;
	private double topLine_Object;
	private double x3_Object;
	
	public Rendering() {
		mapParser = new MapParser(10, 10);
		worldMap = mapParser.getMap();

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
	 * Draws the squares tiles when the player is facing NORTH
	 */
	public void renderNorth() {
		setBackground();
		initiliazeTilePoints();
		y2_Tile = centerHeight;
		for (int i = 0; i < squaresInFront; i++) {
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, i + 1);
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			makeFrontSquares(i, tileWidth, topLine_Tile, nowStartX, nowWidthOfSquare, x2_Tile, x3_Tile, y2_Tile,
					y2_Tile);
			for (int j = 0; j <= squaresToLeft; j++) {
				makeLeftSquares(j, i, y2_Tile, y2_Tile, x3_Tile, x2_Tile, nowStartX, nowWidthOfSquare, topLine_Tile,
						tileHeight, scaleY);
			}
			for (int j = 0; j < squaresToRight; j++) {
				makeRightSquare(j, i, x2_Tile, x3_Tile, y2_Tile, y2_Tile, nowWidthOfSquare, topLine_Tile, nowStartX,
						scaleY);
			}
			calculateTilePoints(nowStartX, i, nowWidthOfSquare);
		}
	}

	public void renderSouth() {
		setBackground();
		initiliazeTilePoints();
		y2_Tile = centerHeight;
		for (int i = 0; i < squaresInFront; i++) {
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, i + 1);
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			makeFrontSquares(i, tileWidth, topLine_Tile, nowStartX, nowWidthOfSquare, x2_Tile, x3_Tile, y2_Tile,
					y2_Tile);
			for (int j = 0; j < squaresToLeft; j++) {
				makeLeftSquares(j, i, y2_Tile, y2_Tile, x3_Tile, x2_Tile, nowStartX, nowWidthOfSquare, topLine_Tile,
						tileHeight, scaleY);
			}
			for (int j = 0; j <= squaresToRight; j++) {
				makeRightSquare(j, i, x2_Tile, x3_Tile, y2_Tile, y2_Tile, nowWidthOfSquare, topLine_Tile, nowStartX,
						scaleY);
			}
			calculateTilePoints(nowStartX, i, nowWidthOfSquare);
		}
	}

	/**
	 * this method is used to render the game
	 * 
	 * @param group
	 */
	public void renderEast() {
		setBackground();
		initiliazeTilePoints();
		y2_Tile = centerHeight;
		for (int i = 0; i <= squaresInFront; i++) {
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, i + 1);
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			makeFrontSquares(i, tileWidth, topLine_Tile, nowStartX, nowWidthOfSquare, x2_Tile, x3_Tile, y2_Tile,
					y3_Tile);
			for (int j = 0; j <= squaresToLeft; j++) {
				makeLeftSquares(j, i, y3_Tile, y2_Tile, x3_Tile, x2_Tile, nowStartX, nowWidthOfSquare, topLine_Tile,
						tileHeight, scaleY);
			}
			for (int j = 0; j <= squaresToRight; j++) {
				makeRightSquare(j, i, x2_Tile, x3_Tile, y3_Tile, y2_Tile, nowWidthOfSquare, topLine_Tile, nowStartX,
						scaleY);
			}
			calculateTilePoints(nowStartX, i, nowWidthOfSquare);
		}
	}

	/**
	 * this method is used to render the game
	 * 
	 * @param group
	 */
	public void renderWest() {
		setBackground();
		initiliazeTilePoints();
		y2_Tile = centerHeight;
		for (int i = 0; i < squaresInFront; i++) {
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, i + 1);
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			makeFrontSquares(i, tileWidth, topLine_Tile, nowStartX, nowWidthOfSquare, x2_Tile, x3_Tile, y2_Tile,
					y3_Tile);
			for (int j = 0; j <= squaresToLeft; j++) {
				makeLeftSquares(j, i, y3_Tile, y2_Tile, x3_Tile, x2_Tile, nowStartX, nowWidthOfSquare, topLine_Tile,
						tileHeight, scaleY);
			}
			for (int j = 0; j <= squaresToRight; j++) {
				makeRightSquare(j, i, x2_Tile, x3_Tile, y3_Tile, y2_Tile, nowWidthOfSquare, topLine_Tile, nowStartX,
						scaleY);
			}
			calculateTilePoints(nowStartX, i, nowWidthOfSquare);
		}
	}

	/**
	 * Renders the objects that are placed in the north direction.
	 */
	public void renderObjectsNorth() {
		initializeObjectPoints();
		int col = playerLoc.x;
		for (int frontSq = playerLoc.y; frontSq < 10; frontSq++) {
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, (frontSq - playerLoc.y) + 1);
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			if (isInMap(frontSq, col)) {
				double x0 = nowStartX;
				double y0 = topLine_Object - tileHeight * Math.pow(scaleY, (frontSq - playerLoc.y) + 1);
				getSurroundings(worldMap[frontSq][col], (x2_Object - x3_Object), (y3_Object - y0), x0, y0);
			}
			for (int leftSq = 0; leftSq <= squaresToLeft; leftSq++) {
				if (isInMap((frontSq), (col + leftSq))) {
					double previouWidthOfSquare = (x2_Object - x3_Object);
					double tempLeftx3 = x3_Object - (leftSq * previouWidthOfSquare);
					double tempLeftx2 = x2_Object - (leftSq * previouWidthOfSquare);
					double tempLeftx0 = nowStartX - leftSq * nowWidthOfSquare;
					double tempLefty0 = topLine_Object - tileHeight * Math.pow(scaleY, (frontSq - playerLoc.y) + 1);
					getSurroundings(worldMap[frontSq][col + leftSq], tempLeftx2 - tempLeftx3, y3_Object - tempLefty0,
							tempLeftx0, tempLefty0);
				}
			}
			for (int rightSq = 0; rightSq < squaresToRight; rightSq++) {
				if (isInMap((frontSq), (col - rightSq))) {
					double previouWidthOfSquare = (x2_Object - x3_Object);
					double tempRightx3 = x3_Object + (rightSq * previouWidthOfSquare);
					double tempRightx2 = x2_Object + (rightSq * previouWidthOfSquare);
					double tempRightx0 = nowStartX + rightSq * nowWidthOfSquare;
					double tempRighty0 = topLine_Object - tileHeight * Math.pow(scaleY, (frontSq - playerLoc.y) + 1);
					getSurroundings(worldMap[frontSq][col - rightSq], (tempRightx2 - tempRightx3),
							(y3_Object - tempRighty0), tempRightx0, tempRighty0);
				}
			}
			x3_Object = nowStartX;
			y3_Object = topLine_Object - tileHeight * Math.pow(scaleY, (frontSq - playerLoc.y) + 1);
			x2_Object = nowStartX + nowWidthOfSquare;
			topLine_Object = topLine_Object - tileHeight * Math.pow(scaleY, (frontSq - playerLoc.y) + 1);

		}
	}

	/**
	 * Renders the objects that are placed in the south direction
	 */
	public void renderObjectsSouth() {
		initializeObjectPoints();

		for (int frontSq = 0; frontSq < squaresInFront; frontSq++) {
			int col = playerLoc.x;
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, frontSq + 1);
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			if (isInMap(((playerLoc.y - 1) - frontSq), col)) {
				double x0 = nowStartX;
				double y0 = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
				getSurroundings(worldMap[(playerLoc.y - 1) - frontSq][col], (x2_Object - x3_Object), (y3_Object - y0),
						x0, y0);

			}
			for (int leftSq = 0; leftSq < squaresToLeft; leftSq++) {
				if (isInMap(((playerLoc.y - 1) - frontSq), (col - leftSq))) {
					double previouWidthOfSquare = (x2_Object - x3_Object);
					double leftx3 = x3_Object - (leftSq * previouWidthOfSquare);
					double leftx2 = x2_Object - (leftSq * previouWidthOfSquare);
					double leftx0 = nowStartX - leftSq * nowWidthOfSquare;
					double lefty0 = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
					getSurroundings(worldMap[(playerLoc.y - 1) - frontSq][col - leftSq], (leftx2 - leftx3),
							(y3_Object - lefty0), leftx0, lefty0);
				}
			}
			for (int rightSq = 0; rightSq <= squaresToRight; rightSq++) {
				if (isInMap(((playerLoc.y - 1) - frontSq), (col + rightSq))) {
					double previouWidthOfSquare = (x2_Object - x3_Object);
					double rightx3 = x3_Object + (rightSq * previouWidthOfSquare);
					double rightx2 = x2_Object + (rightSq * previouWidthOfSquare);
					double rightx0 = nowStartX + rightSq * nowWidthOfSquare;
					double righty0 = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
					getSurroundings(worldMap[(playerLoc.y - 1) - frontSq][col + rightSq], (rightx2 - rightx3),
							(y3_Object - righty0), rightx0, righty0);
				}
			}
			x3_Object = nowStartX;
			y3_Object = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
			x2_Object = nowStartX + nowWidthOfSquare;
			topLine_Object = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
		}
	}

	/**
	 * this method is used to render the object in the game that are to the
	 * north of the player..
	 */
	public void renderObjectsEast() {
		initializeObjectPoints();
		int col = playerLoc.x;
		for (int frontSq = 0; frontSq <= squaresInFront; frontSq++) {
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, frontSq + 1);
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			System.out.println(playerLoc.x - frontSq);// **********************
			if (isInMap((playerLoc.y), (playerLoc.x - frontSq))) {
				double x0 = nowStartX;
				double y0 = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
				getSurroundings(worldMap[playerLoc.y][playerLoc.x - frontSq], (x2_Object - x3_Object), (y3_Object - y0),
						x0, y0);
			}
			for (int leftSq = 0; leftSq <= squaresToLeft; leftSq++) {
				if (isInMap((playerLoc.y + leftSq), (col - frontSq))) {
					double previouWidthOfSquare = (x2_Object - x3_Object);
					double leftx3 = x3_Object - (leftSq * previouWidthOfSquare);
					double lefty3 = y3_Object;
					double leftx2 = x2_Object - (leftSq * previouWidthOfSquare);
					double leftx0 = nowStartX - leftSq * nowWidthOfSquare;
					double lefty0 = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
					getSurroundings(worldMap[playerLoc.y + leftSq][col - frontSq], (leftx2 - leftx3), (lefty3 - lefty0),
							leftx0, lefty0);
				}
			}
			for (int rightSq = 0; rightSq <= squaresToRight; rightSq++) {
				if (isInMap((playerLoc.y - rightSq), (col - frontSq))) {
					double previouWidthOfSquare = (x2_Object - x3_Object);
					double rightx3 = x3_Object + (rightSq * previouWidthOfSquare);
					double righty3 = y3_Object;
					double rightx2 = x2_Object + (rightSq * previouWidthOfSquare);
					double rightx0 = nowStartX + rightSq * nowWidthOfSquare;
					double righty0 = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
					getSurroundings(worldMap[playerLoc.y - rightSq][col - frontSq], (rightx2 - rightx3),
							(righty3 - righty0), rightx0, righty0);
				}
			}
			x3_Object = nowStartX;
			y3_Object = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
			x2_Object = nowStartX + nowWidthOfSquare;
			topLine_Object = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
		}
	}

	/**
	 * Renders the objects that are placed in the WEST direction
	 */
	public void renderObjectsWest() {
		initializeObjectPoints();
		int col = playerLoc.x;
		for (int frontSq = 0; frontSq < squaresInFront; frontSq++) {
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, frontSq + 1);
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			if (isInMap((playerLoc.y), (playerLoc.x + frontSq))) {
				double x0 = nowStartX;
				double y0 = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
				getSurroundings(worldMap[playerLoc.y][playerLoc.x + frontSq], (x2_Object - x3_Object), (y3_Object - y0),
						x0, y0);
			}
			for (int leftSq = 0; leftSq <= squaresToLeft; leftSq++) {
				if (isInMap((playerLoc.y - leftSq), (col + frontSq))) {
					double previouWidthOfSquare = (x2_Object - x3_Object);
					double leftx3 = x3_Object - (leftSq * previouWidthOfSquare);
					double leftx2 = x2_Object - (leftSq * previouWidthOfSquare);
					double leftx0 = nowStartX - leftSq * nowWidthOfSquare;
					double lefty0 = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
					getSurroundings(worldMap[playerLoc.y - leftSq][col + frontSq], (leftx2 - leftx3),
							(y3_Object - lefty0), leftx0, lefty0);
				}
			}
			for (int rightSq = 0; rightSq <= squaresToRight; rightSq++) {
				if (isInMap((playerLoc.y + rightSq), (col + frontSq))) {
					double previouWidthOfSquare = (x2_Object - x3_Object);
					double rightx3 = x3_Object + (rightSq * previouWidthOfSquare);
					double rightx2 = x2_Object + (rightSq * previouWidthOfSquare);
					double rightx0 = nowStartX + rightSq * nowWidthOfSquare;
					double righty0 = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
					getSurroundings(worldMap[playerLoc.y + rightSq][col + frontSq], (rightx2 - rightx3),
							(y3_Object - righty0), rightx0, righty0);
				}
			}
			x3_Object = nowStartX;
			y3_Object = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
			x2_Object = nowStartX + nowWidthOfSquare;
			topLine_Object = topLine_Object - tileHeight * Math.pow(scaleY, frontSq + 1);
		}
	}

	/**
	 * Uploads the image of the object that the player can see
	 * 
	 * @param objectName
	 *            name of object to be drawn
	 * @param width
	 * @param height
	 * @param x
	 *            starting X
	 * @param y
	 *            starting Y
	 */

	public void getSurroundings(String objectName, double width, double height, double x, double y) {
		ImageView imageView = new ImageView();

		switch (objectName) {
		case "tree":
			loadImage(TREE_IMAGE);
			imageView.setImage(loadImage(TREE_IMAGE));
			break;
		case "chest":
			imageView.setImage(loadImage(CHEST_IMAGE));
			break;
		}
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		imageView.setX(x);
		imageView.setY(y);
		group.getChildren().add(imageView);

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
	 * Checks if the object in front or beside the player is a valid object in
	 * the map
	 * 
	 * @param row
	 * @param col
	 * @return true if valid, false if not
	 */
	public boolean isInMap(int row, int col) {
		return worldMap[row][col].equals("tree") || worldMap[row][col].equals("chest");
	}

	/**
	 * Constructs left squares
	 * 
	 * @param row
	 * @param col
	 * @param y3
	 * @param y2
	 * @param x3
	 * @param x2
	 * @param nowStartX
	 * @param nowWidthOfSquare
	 * @param topLine
	 * @param tileHeight
	 * @param scaleY
	 */
	public void makeLeftSquares(int row, int col, double y3, double y2, double x3, double x2, double nowStartX,
			double nowWidthOfSquare, double topLine, double tileHeight, double scaleY) {
		double previouWidthOfSquare = (x2 - x3);
		double leftx3 = x3 - (row * previouWidthOfSquare);
		double leftx2 = x2 - (row * previouWidthOfSquare);
		double leftx1 = nowStartX + nowWidthOfSquare - row * nowWidthOfSquare;
		double lefty1 = (topLine - tileHeight * Math.pow(scaleY, col + 1));
		double leftx0 = nowStartX - row * nowWidthOfSquare;
		makePolygonTile(leftx3, y3, leftx2, y2, leftx1, lefty1, leftx0, lefty1);

	}

	public void makeRightSquare(int j, int i, double x2, double x3, double y3, double y2, double nowWidthOfSquare,
			double topLine, double nowStartX, double scaleY) {
		double previouWidthOfSquare = (x2 - x3);
		double rightx2 = x2 + (j * previouWidthOfSquare);
		double rightx1 = nowStartX + nowWidthOfSquare + j * nowWidthOfSquare;
		double rightx3 = x3 + (j * previouWidthOfSquare);
		double rightx4 = nowStartX + j * nowWidthOfSquare;
		double righty1 = topLine - tileHeight * Math.pow(scaleY, i + 1);
		makePolygonTile(rightx3, y3, rightx2, y2, rightx1, righty1, rightx4, righty1);
	}

	public void makeFrontSquares(int i, double tileWidth, double topLine, double nowStartX, double nowWidthOfSquare,
			double x2, double x3, double y2, double y3) {
		double x1 = (nowStartX + nowWidthOfSquare);
		double y1 = topLine - tileHeight * Math.pow(scaleY, i + 1);
		makePolygonTile(x3, y3, x2, y2, x1, y1, nowStartX, y1);
	}

	/**
	 * Makes a new polygon object with a given set of coordinates and adds it to the group
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 */
	public void makePolygonTile(double x1, double y1, double x2, double y2, double x3, double y3, double x4,
			double y4) {
		Image grass = loadImage(GRASS_IMAGE);
		Polygon polygon = new Polygon();
		polygon.setFill(new ImagePattern(grass));
		polygon.setLayoutY(10);
		polygon.getPoints().add(x1);
		polygon.getPoints().add(y1);
		polygon.getPoints().add(x2);
		polygon.getPoints().add(y2);
		polygon.getPoints().add(x3);
		polygon.getPoints().add(y3);
		polygon.getPoints().add(x4);
		polygon.getPoints().add(y4);
		polygon.setStroke(javafx.scene.paint.Color.AQUA);
		polygon.setStrokeWidth(1);
		group.getChildren().add(polygon);
	}

	/**
	 * Sets the background of the graphics pane
	 */
	public void setBackground() {
		Image image = loadImage(BACKGROUND_IMAGE);
		ImageView imageViewNight = new ImageView();
		imageViewNight.setImage(image);
		imageViewNight.setFitWidth(gamePaneWidth + 3);
		imageViewNight.setFitHeight(gamePaneHeight + 35);
		group.getChildren().add(imageViewNight);
	}

	/**
	 * Initializes the points for drawing the tiles in the graphics pane
	 */
	public void initiliazeTilePoints() {
		topLine_Tile = centerHeight;
		x2_Tile = centerWidth + tileWidth / 2;
		x3_Tile = centerWidth - tileWidth / 2;
		y3_Tile = centerHeight;
	}

	/**
	 * Initializes the points that are used when drawing the objects in the
	 * graphics pane
	 */
	public void initializeObjectPoints() {
		topLine_Object = centerHeight;
		x2_Object = centerWidth + tileWidth / 2;
		x3_Object = centerWidth - tileWidth / 2;
		y3_Object = centerHeight;
	}

	/**
	 * 
	 * @param nowStartX
	 *            the new starting point for the top right point of the tile
	 * @param Nsquare
	 *            the nth square in the loop
	 * @param nowWidthOfSquare
	 *            the new width of the tile
	 */
	public void calculateTilePoints(double nowStartX, int Nsquare, double nowWidthOfSquare) {
		x3_Tile = nowStartX;
		y3_Tile = topLine_Tile - tileHeight * Math.pow(scaleY, Nsquare + 1);
		x2_Tile = nowStartX + nowWidthOfSquare;
		y2_Tile = topLine_Tile - tileHeight * Math.pow(scaleY, Nsquare + 1);
		topLine_Tile = topLine_Tile - tileHeight * Math.pow(scaleY, Nsquare + 1);

	}

	/**
	 * this method is used to move the player in the screen given player, it
	 * then calls the render method. so when a player makes a move there stats
	 * will be updated and based on this a string will be passed containg this
	 * update information which is used to render the player in tand the
	 * objectss in the right place.
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
			moveNorth();
			renderNorth();
			renderObjectsNorth();
		} else if (direction == 1) {
			System.out.println("East");
			moveEast();
			renderEast();
			renderObjectsEast();

		} else if (direction == 2) {
			System.out.println("South");
			moveSouth();
			renderSouth();
			renderObjectsSouth();
		} else if (direction == 3) {
			System.out.println("West");
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
		int tempSquares = boardSize - playerLoc.x;
		squaresInFront = boardSize - tempSquares;
		squaresToLeft = (boardSize - playerLoc.y);
		squaresToRight = (boardSize - squaresToLeft);
		// could be off by one
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
	public void setGroup(Panel renderGroup) {
		//this.group = renderGroup;
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
