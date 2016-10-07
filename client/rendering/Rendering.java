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
	// 35 y alignment of group
	private int gamePaneHeight = GUI.HEIGHT_VALUE - 35;
	// 3 x alignment of group
	private int gamePanelWidth = GUI.GAMEPANE_WIDTH_VALUE-1;
	private int tileWidth = 130;
	private int tileHeight = 50;
	private double imageOffset = 15;
	private double scale = 0.8;
	public double centerWidth = gamePanelWidth / 2;
	private int squaresInFront = 0;
	private int squaresToLeft = 0;
	private int squaresToRight = 0;
	private Player player;
	private Map<Integer, Player> playersOnMap;
	private Map<Integer, Area> map;
	private int avatarID;
	private String direction;
	// private int boardSize = 10;
	private Pane renderGroup;
	private int imageBound = 10;

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
	public void render(Position playerLoc, char[][] worldMap, int visibility, int uid) {
		// player's coordinate on board, and direction.
		// need to get position from param (uid)
		// Position selfPosition = positions.get(uid);
		// Position selfPosition = new Position(5, 10, 1, Direction.North);
		// Attempting to make the vision boundary... Eg: player can only see 20
		// squares to the front, and across.
		renderGroup.getChildren().clear();
		int x = playerLoc.x;
		int y = playerLoc.y;
		Direction direction = playerLoc.getDirection();
		Image background = Images.BACKGROUND_IMAGE;
		Image grass = Images.GRASS_IMAGE;
		addImage(renderGroup, background, gamePanelWidth, gamePaneHeight, 0, 0);
		setNumSquares(worldMap.length, worldMap[0].length, direction, playerLoc, worldMap);
		double yTop = getTopOffset();
		double previousTileWidth = tileWidth * Math.pow(scale, squaresInFront);
		double xRightTop = centerWidth + previousTileWidth / 2;
		double xLeftTop = centerWidth - previousTileWidth / 2;
		// ===================================================================================================
		// Below this is point is the code for all the rendering for first
		// person
		// ====================================================================================================
		for (int row = 0; row < squaresInFront; row++) {
			Polygon squareFront = new Polygon();
			squareFront.setFill(new ImagePattern(grass));
			squareFront.setLayoutY(10);
			double currentTileWidth = tileWidth * Math.pow(scale, squaresInFront - row - 1);
			double currentTileHeight = tileHeight * Math.pow(scale, squaresInFront - row - 1);
			double xLeftBottom = centerWidth - currentTileWidth / 2;
			double yBottom = yTop + currentTileHeight;
			if (squaresInFront - row <= imageBound) {
				addTile(squareFront, xLeftTop, xRightTop, xLeftBottom + currentTileWidth, xLeftBottom, yBottom, yTop,
						renderGroup);
				if (direction.equals(Direction.North) || direction.equals(Direction.South)) {
					addObject(xLeftTop, yBottom, xRightTop, row, playerLoc.x, "middle", worldMap, renderGroup,
							direction, yTop);
				} else {
					addObject(xLeftTop, yBottom, xRightTop, row, playerLoc.y, "middle", worldMap, renderGroup,
							direction, yTop);
				}
				for (int col = squaresToLeft - 1; col >= 0; col--) {
					Polygon squareLeft = new Polygon();
					squareLeft.setLayoutY(10);
					squareLeft.setFill(new ImagePattern(grass));
					double tileXLeftTop = xLeftTop - previousTileWidth - (col * previousTileWidth);
					double tileXRightTop = xLeftTop - (col * previousTileWidth);
					double tileXRightBottom = xLeftBottom - col * currentTileWidth;
					double tileXLeftBottom = xLeftBottom - currentTileWidth - (col * currentTileWidth);
					if (tileXRightTop >= 0) {
						addTile(squareLeft, tileXLeftTop, tileXRightTop, tileXRightBottom, tileXLeftBottom, yBottom,
								yTop, renderGroup);
						addObject(tileXLeftTop, yBottom, tileXRightBottom, row, col, "left", worldMap, renderGroup,
								direction, yTop);
					}
				}
				for (int col = squaresToRight - 1; col >= 0; col--) {
					Polygon squareRight = new Polygon();
					squareRight.setFill(new ImagePattern(grass));
					squareRight.setLayoutY(10);
					double tileXLeftTop = xLeftTop + previousTileWidth + (col * previousTileWidth);
					double tileXRightTop = xLeftTop + (previousTileWidth * 2) + (col * previousTileWidth);
					double tileXRightBottom = xLeftBottom + (currentTileWidth * 2) + (col * currentTileWidth);
					double tileXLeftBottom = xLeftBottom + currentTileWidth + (col * currentTileWidth);
					if (tileXLeftTop >= 0) {
						addTile(squareRight, tileXLeftTop, tileXRightTop, tileXRightBottom, tileXLeftBottom, yBottom,
								yTop, renderGroup);
						addObject(tileXLeftBottom, yBottom, tileXRightTop, row, col, "right", worldMap, renderGroup,
								direction, yTop);
					}
				}
			}
			xLeftTop = xLeftBottom;
			xRightTop = xLeftBottom + currentTileWidth;
			yTop = yBottom;
			previousTileWidth = currentTileWidth;
		}
	}

	private double getTopOffset() {
		double count = 0;
		for (int i = 0; i < squaresInFront; i++) {
			count += tileHeight * Math.pow(scale, squaresInFront - i - 1);
		}
		return gamePaneHeight - count;
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

	private void setNumSquares(int height, int width, Direction direction, Position playerLoc, char[][] map) {
		switch (direction) {
		case North:
			//System.out.println("North");
			squaresInFront = playerLoc.y + 1;
			squaresToLeft = playerLoc.x;
			squaresToRight = width - playerLoc.x - 1;
			break;
		case East:
			//System.out.println("East");
			squaresInFront = width - playerLoc.x;
			squaresToLeft = playerLoc.y;
			squaresToRight = height - playerLoc.y - 1;
			break;
		case South:
			//System.out.println("South");
			squaresInFront = height - playerLoc.y;
			squaresToLeft = width - playerLoc.x - 1;
			squaresToRight = playerLoc.x;
			break;
		case West:
			//System.out.println("West");
			squaresInFront = playerLoc.x + 1;
			squaresToLeft = height - playerLoc.y - 1;
			squaresToRight = (height - squaresToLeft) - 1;
			break;
		}
	
	}

	public void charRender() {

	}

	private void addObject(double tileXLeftBottom, double yBottom, double tileXRightBottom, int row, int col,
			String side, char[][] worldMap, Pane renderGroup, Direction direction, double yTop) {
		// System.out.println(direction.toString());
		Point imageCoordinate = getImagePoint(direction, row, col, side, worldMap.length, worldMap[0].length);
		// System.out.println(direction.toString());
		char object = worldMap[imageCoordinate.y][imageCoordinate.x];
		Image image = getImageFromChar(object);
		// if (image != null) {
		// double height = image.getHeight() * Math.pow(0.8, squaresInFront -
		// row - 1);
		// double width = image.getWidth() * Math.pow(0.8, squaresInFront - row
		// - 1);
		// double xPoint = getImageX(width, tileXLeftBottom, tileXRightBottom);
		// addImage(renderGroup, image, width, height, xPoint, yBottom -
		// height);
		// }
		if (image != null) {
			double height = image.getHeight() * Math.pow(scale, squaresInFront - row - 1);
			double width = image.getWidth() * Math.pow(scale, squaresInFront - row - 1);
			double xPoint = getImageX(width, tileXLeftBottom, tileXRightBottom);
			double yPoint = getImageY(height, yBottom, yTop);
			addImage(renderGroup, image, width, height, xPoint, yPoint + imageOffset);
		}
	}

	private Point getImagePoint(Direction direction, int row, int col, String side, int boardHeight, int boardWidth) {
		// if (row < 0 || row >= boardHeight)
		// throw new IllegalArgumentException("row is out of bounds.");
		// if (col < 0 || col >= boardWidth)
		// throw new IllegalArgumentException(" is out of bounds.");

		switch (direction) {
		case North:
			if (side.equals("left")) {
				Point temp = new Point(squaresToLeft - col - 1, row);
				//System.out.println(temp.x + " rightttttttttttt" + temp.y);
				return temp;
			} else if (side.equals("right")) {
				Point temp = new Point(squaresToLeft + col + 1, row);
				//System.out.println(temp.x + " rightttttttttttt" + temp.y);
				return temp;
			} else {
				Point temp = new Point(col, row);
				//System.out.println(temp.x + " rightttttttttttt" + temp.y);
				return temp;
			}
		case South:
			if (side.equals("left")) {

				Point temp = new Point(boardWidth - (squaresToLeft - col), boardHeight - row - 1);
				// System.out.println(temp.x+"lefttttttttttttt "+temp.y);
				return temp;
			} else if (side.equals("right")) {
				Point temp = new Point((squaresToRight - col) - 1, boardHeight - row - 1);
				// System.out.println(temp.x+" rightttttttttttt"+temp.y);
				return temp;
			} else {
				Point temp = new Point(col, boardHeight - row - 1);
				// System.out.println(temp.x+"centerttttttttttttttttttt
				// "+temp.y);
				return temp;
			}
		case East:
			if (side.equals("left")) {
				Point temp = new Point(boardWidth - 1 - row, squaresToLeft - col - 1);
				//System.out.println(temp.x + "lefttttttttttttt " + temp.y);
				return temp;
			} else if (side.equals("right")) {
				Point temp = new Point(boardWidth - 1 - row, squaresToLeft + col + 1);
				//System.out.println(temp.x + " rightttttttttttt" + temp.y);
				return temp;
			} else {
				Point temp = new Point(boardWidth - 1 - row, col);
				//System.out.println(temp.x + "centerttttttttttttttttttt " + temp.y);
				// System.out.println(boardHeight + " " + boardWidth);
				return temp;
			}
		case West:
			// System.out.println(col+" "+row);
			if (side.equals("left")) {
				// this loop is broken
				Point temp = new Point(row, boardHeight-(squaresToLeft-col));
				//System.out.println(temp.x + "lefttttttttttttt " + temp.y);
				return temp;
			} else if (side.equals("right")) {
				Point temp = new Point(row, squaresToRight - col - 1);
				//System.out.println(temp.x + "lefttttttttttttt " + temp.y);
				return temp;
			} else {
				Point temp = new Point(row, col);
				//System.out.println(temp.x + "lefttttttttttttt " + temp.y);
				return temp;
			}
		}
		return null;
	}

	private Image getImageFromChar(char input) {
		return Images.MAP_OBJECT_IMAGES.get(input);
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

	private double getImageY(double imageHeight, double tileBottom, double tileTop) {
		double tileHeight = tileBottom - tileTop;
		double heightOffset = tileHeight / 2;
		return tileBottom - heightOffset - imageHeight;
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

	public void setDirection(String dir) {
		this.direction = dir;
	}

	public void setGroup(Pane renderGroup) {
		this.renderGroup = renderGroup;
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
