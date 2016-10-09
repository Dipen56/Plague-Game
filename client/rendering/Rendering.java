package client.rendering;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import client.view.ClientUI;
import client.view.GUI;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import server.game.player.Avatar;
import server.game.player.Direction;
import server.game.player.Player;
import server.game.player.Position;
import server.game.world.Area;
import javafx.scene.paint.ImagePattern;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * This class represents the main rendering class, this class will control the
 * rendering of the game board, character, and objects.
 *
 * @author Angelo
 *
 */

public class Rendering {

	private int gamePaneHeight = GUI.HEIGHT_VALUE - 35;
	private int gamePanelWidth = GUI.GAMEPANE_WIDTH_VALUE - 1;
	private int tileWidth = 130;
	private int tileHeight = 50;
	private double imageOffset = 15;
	private double scale = 0.8;
	public double centerWidth = gamePanelWidth / 2;
	private int squaresInFront = 0;
	private int squaresToLeft = 0;
	private int squaresToRight = 0;
	private Pane renderGroup;
	private int imageBound = 10;
	private Label mapDescription;

	public Rendering() {

	}

	/**
	 * Renders the entire game, method gets called from the CLient UI class
	 * 
	 * @param playerLoc
	 * @param worldMap
	 * @param visibility
	 * @param uid
	 * @param avatars
	 * @param positions
	 */
	public void render(Position playerLoc, char[][] worldMap, int visibility, int uid, Map<Integer, Avatar> avatars,
			Map<Integer, Position> positions) {
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
					addAvatar(xLeftTop, yBottom, xRightTop, row, playerLoc.x, "middle", worldMap, renderGroup,
							direction, yTop, avatars, positions, uid);
				} else {
					addObject(xLeftTop, yBottom, xRightTop, row, playerLoc.y, "middle", worldMap, renderGroup,
							direction, yTop);
					addAvatar(xLeftTop, yBottom, xRightTop, row, playerLoc.y, "middle", worldMap, renderGroup,
							direction, yTop, avatars, positions, uid);
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
						addAvatar(tileXLeftTop, yBottom, tileXRightBottom, row, col, "left", worldMap, renderGroup,

								direction, yTop, avatars, positions, uid);

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
						addAvatar(tileXLeftBottom, yBottom, tileXRightTop, row, col, "right", worldMap, renderGroup,

								direction, yTop, avatars, positions, uid);

					}
				}
			}
			xLeftTop = xLeftBottom;
			xRightTop = xLeftBottom + currentTileWidth;
			yTop = yBottom;
			previousTileWidth = currentTileWidth;
		}
	}

	/**
	 * Calculates the offset
	 * 
	 * @return
	 */
	private double getTopOffset() {
		double count = 0;
		for (int i = 0; i < squaresInFront; i++) {
			count += tileHeight * Math.pow(scale, squaresInFront - i - 1);
		}
		return gamePaneHeight - count;
	}

	/**
	 * Creates the tiles / grass on the board via the provided parameters.
	 * 
	 * @param p
	 * @param xLeftTop
	 * @param xRightTop
	 * @param xRightBottom
	 * @param xLeftBottom
	 * @param yBottom
	 * @param yTop
	 * @param renderGroup
	 */
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
		// This forms the grid of squares
		p.setStroke(javafx.scene.paint.Color.AQUA);
		p.setStrokeWidth(1);
		//////////////////////////////////////////////
		renderGroup.getChildren().add(p);
	}

	/**
	 * Calculates the number of squares to the front, left and right of the
	 * character
	 * 
	 * @param height
	 * @param width
	 * @param direction
	 * @param playerLoc
	 * @param map
	 */
	private void setNumSquares(int height, int width, Direction direction, Position playerLoc, char[][] map) {
		switch (direction) {
		case North:
			// System.out.println("North");
			squaresInFront = playerLoc.y + 1;
			squaresToLeft = playerLoc.x;
			squaresToRight = width - playerLoc.x - 1;
			break;
		case East:
			// System.out.println("East");
			squaresInFront = width - playerLoc.x;
			squaresToLeft = playerLoc.y;
			squaresToRight = height - playerLoc.y - 1;
			break;
		case South:
			// System.out.println("South");
			squaresInFront = height - playerLoc.y;
			squaresToLeft = width - playerLoc.x - 1;
			squaresToRight = playerLoc.x;
			break;
		case West:
			// System.out.println("West");
			squaresInFront = playerLoc.x + 1;
			squaresToLeft = height - playerLoc.y - 1;
			squaresToRight = (height - squaresToLeft) - 1;
			break;
		}
	}

	/**
	 * Renders the current character, and all other characters onto the board
	 * 
	 * @param xLeftTop
	 * @param yBottom
	 * @param xRightTop
	 * @param row
	 * @param x
	 * @param string
	 * @param worldMap
	 * @param renderGroup2
	 * @param direction2
	 * @param yTop
	 * @param avatars
	 * @param positions
	 * @param uid
	 */
	public void addAvatar(double xLeftTop, double yBottom, double xRightTop, int row, int x, String string,
			char[][] worldMap, Pane renderGroup2, Direction direction2, double yTop, Map<Integer, Avatar> avatars,
			Map<Integer, Position> positions, int uid) {
		// Image playerImg = Images.getAvatarImageBySide(avatars.get(uid),
		// Side.Back);
		// Image another = Images.getAvatarImageByDirection(Avatar.get(22),
		// Direction.East, Direction.West);
	}

	/**
	 * Given the provided arguments, Find a point in which to access the array,
	 * if it contains a image char, then pass that char into getImageFromChar
	 * which will then return an image object which can then be drawn
	 * 
	 * @param tileXLeftBottom
	 * @param yBottom
	 * @param tileXRightBottom
	 * @param row
	 * @param col
	 * @param side
	 * @param worldMap
	 * @param renderGroup
	 * @param direction
	 * @param yTop
	 */
	private void addObject(double tileXLeftBottom, double yBottom, double tileXRightBottom, int row, int col,
			String side, char[][] worldMap, Pane renderGroup, Direction direction, double yTop) {
		// System.out.println(direction.toString());
		Point imageCoordinate = getImagePoint(direction, row, col, side, worldMap.length, worldMap[0].length);
		// System.out.println(direction.toString());
		char object = worldMap[imageCoordinate.y][imageCoordinate.x];
		Image image = getImageFromChar(object);
		if (image != null) {
			double height = image.getHeight() * Math.pow(scale, squaresInFront - row - 1);
			double width = image.getWidth() * Math.pow(scale, squaresInFront - row - 1);
			double xPoint = getImageX(width, tileXLeftBottom, tileXRightBottom);
			double yPoint = getImageY(height, yBottom, yTop);
			addImage(renderGroup, image, width, height, xPoint, yPoint + imageOffset);
		}
	}

	/**
	 * Calculates the front, left, and right coordinates for finding char
	 * objects, creates a point and then return the newly created point
	 * 
	 * @param direction
	 * @param row
	 * @param col
	 * @param side
	 * @param boardHeight
	 * @param boardWidth
	 * @return
	 */
	private Point getImagePoint(Direction direction, int row, int col, String side, int boardHeight, int boardWidth) {
		switch (direction) {
		case North:
			if (side.equals("left")) {
				Point temp = new Point(squaresToLeft - col - 1, row);
				// System.out.println(temp.x + " rightttttttttttt" + temp.y);
				return temp;
			} else if (side.equals("right")) {
				Point temp = new Point(squaresToLeft + col + 1, row);
				// System.out.println(temp.x + " rightttttttttttt" + temp.y);
				return temp;
			} else {
				Point temp = new Point(col, row);
				// System.out.println(temp.x + " rightttttttttttt" + temp.y);
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
				// System.out.println(temp.x + "lefttttttttttttt " + temp.y);
				return temp;
			} else if (side.equals("right")) {
				Point temp = new Point(boardWidth - 1 - row, squaresToLeft + col + 1);
				// System.out.println(temp.x + " rightttttttttttt" + temp.y);
				return temp;
			} else {
				Point temp = new Point(boardWidth - 1 - row, col);
				// System.out.println(temp.x + "centerttttttttttttttttttt " +
				// temp.y);
				// System.out.println(boardHeight + " " + boardWidth);
				return temp;
			}
		case West:
			// System.out.println(col + " " + row);
			if (side.equals("left")) {
				// this loop is broken
				Point temp = new Point(row, boardHeight - (squaresToLeft - col));
				// System.out.println(temp.x + "lefttttttttttttt " + temp.y);
				return temp;
			} else if (side.equals("right")) {
				Point temp = new Point(row, squaresToRight - col - 1);
				// System.out.println(temp.x + "lefttttttttttttt " + temp.y);
				return temp;
			} else {
				Point temp = new Point(row, col);
				// System.out.println(temp.x + "lefttttttttttttt " + temp.y);
				return temp;
			}
		}
		return null;
	}

	/**
	 * Provided the given character, match an image to it, and return the image
	 * object
	 * 
	 * @param input
	 * @return
	 */
	private Image getImageFromChar(char input) {
		return Images.MAP_OBJECT_IMAGES.get(input);
	}

	/**
	 * Get the X point of each image on the board, based on the rows and cols of
	 * the image location, and being relative to the players position
	 * 
	 * @param imageWidth
	 * @param tileXLeft
	 * @param tileXRight
	 * @return
	 */
	private double getImageX(double imageWidth, double tileXLeft, double tileXRight) {
		double tileWidth = tileXRight - tileXLeft;
		double widthOffset = Math.abs(tileWidth - imageWidth) / 2;
		if (tileWidth > imageWidth) {
			return tileXLeft + widthOffset;
		} else {
			return tileXLeft - widthOffset;
		}
	}

	/*
	 * Get the Y point of each image on the board, based on the rows and cols of
	 * the image location, and being relative to the players position
	 * 
	 * @param imageHeight
	 * 
	 * @param tileBottom
	 * 
	 * @param tileTop
	 * 
	 * @return
	 */
	private double getImageY(double imageHeight, double tileBottom, double tileTop) {
		double tileHeight = tileBottom - tileTop;
		double heightOffset = tileHeight / 2;
		return tileBottom - heightOffset - imageHeight;
	}

	/**
	 * Given the provided arguments, add an image into the renderGroup for it to
	 * be redrawn
	 * 
	 * @param renderGroup
	 * @param image
	 * @param width
	 * @param height
	 * @param setX
	 * @param setY
	 */
	private void addImage(Pane renderGroup, Image image, double width, double height, double setX, double setY) {
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		imageView.setX(setX);
		imageView.setY(setY);
		renderGroup.getChildren().add(imageView);
	}

	public void setAreaDescription() {
		mapDescription = new Label();
		mapDescription.setWrapText(true);
		mapDescription.setLayoutX(gamePanelWidth - 150);
		mapDescription.setLayoutY(30);
		mapDescription.getStyleClass().add("area-description");

	}

	public void updateAreaDescription(String areaDescription) {
		mapDescription.setText(areaDescription);
		renderGroup.getChildren().add(mapDescription);
	}

	public void applyWeather() {

	}

	/**
	 * Sets the pane to be renderGroup
	 * 
	 * @param renderGroup
	 */
	public void setGroup(Pane renderGroup) {
		this.renderGroup = renderGroup;
	}

	/**
	 * Generic toString method.
	 */
	@Override
	public String toString() {
		return "renderclass";
	}

}
