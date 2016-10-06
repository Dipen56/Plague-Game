package client.rendering;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import client.view.GUI;
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
	private static final String BACKGROUND_IMAGE = "/roomBackGround.png";

	private static final String GRASS_IMAGE = "/grass.png";
	private static final String BRICK_IMAGE = "/brickWall.png";
	private static final String FLOORTILE_IMAGE = "/roomTile.png";
	private static final String TREE_IMAGE = "/tree.png";
	private static final String CHEST_IMAGE = "/chest.png";
	public double scaleY = 1.2; // lower number less scaling
	public double scaleX = 1.2; // lower number less scaling

	// 35 y alignment of group
	private int gamePaneHeight = GUI.HEIGHT_VALUE - 130;
	// 3 x alignment of group

	private int gamePanelWidth = GUI.GAMEPANE_WIDTH_VALUE;
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

	private HashMap<Integer, Point[]> leftSidePoints = new HashMap<>();
	private  ArrayList<Point> frontLeftPoints = new ArrayList<>();
	private  ArrayList<Point> frontRightPoints = new ArrayList<>();
	private HashMap<Integer, Point[]> rightSidePoints = new HashMap<>();
	private Polygon wallPolygon = new Polygon();
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
		int x = playerLoc.x;
		int y = playerLoc.y;
		Direction direction = playerLoc.getDirection();
		Image background = Images.BACKGROUND_IMAGE;
		Image grass = Images.GRASS_IMAGE;
		addImage(renderGroup, background, gamePanelWidth, gamePaneHeight, 0, 0);
		setNumSquares(worldMap.length, worldMap[0].length, direction, playerLoc);
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
						addPointsToList(row, col, (int) tileXLeftTop, (int) tileXLeftBottom, (int) tileXRightTop, (int) yBottom,
								(int) yTop,"left");
						addFrontPoints(row,(int) tileXLeftTop, (int)tileXRightTop,(int) yTop,"left");
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
						addPointsToList(row, col, (int) tileXLeftTop, (int) tileXRightBottom, (int) tileXRightTop, (int) yBottom,
								(int) yTop,"right");
							addFrontPoints(row,(int) tileXLeftTop, (int)tileXRightTop,(int) yTop,"right");
					}
				}
			}
			xLeftTop = xLeftBottom;
			xRightTop = xLeftBottom + currentTileWidth;
			yTop = yBottom;
			previousTileWidth = currentTileWidth;
			currentTileWidth = currentTileWidth * scaleX;
			currentTileHeight = currentTileHeight * scaleY;
		}

		//renderRoom(renderGroup);
		
		

	}

	private double getTopOffset() {
		double count = 0;
		for (int i = 0; i < squaresInFront; i++) {

			count += tileHeight * Math.pow(scale, squaresInFront - i - 1);
		}
		return gamePaneHeight - count;
	}
	private void addPointsToList(int row, int col, int xLeftTop_Tile, int tileXLeftBottom, int xRightTop_Tile,
			int yBottom, int yTop, String side) {
		if (col == squaresToLeft - 1 && side.equals("left")) {
			Point[] points = new Point[3];
			points[0] = new Point(xRightTop_Tile, yTop); // this is to find the												// width
			points[1] = new Point(tileXLeftBottom, yBottom);
			points[2] = new Point(xLeftTop_Tile, yTop);
			leftSidePoints.put(row, points);
		}
		
		else if (col == squaresToRight -1 && side.equals("right")){
			Point[] points = new Point[3];
			points[0] = new Point(xRightTop_Tile, yTop); // this is to find the												// width
			points[1] = new Point(tileXLeftBottom, yBottom);
			points[2] = new Point(xLeftTop_Tile, yTop);
			rightSidePoints.put(row, points);
		}
		
	}

	private void addFrontPoints(int row, int xLeftTop, int xTopRight, int yTop,String side){
		if(row == 0 && side.equals("left")) {
			Point leftPoint = new  Point(xLeftTop, yTop);
			frontLeftPoints.add(leftPoint);
			Point rightPoint = new Point(xTopRight, yTop); 
			frontLeftPoints.add(rightPoint);// width
			return;
		}
		else if(row == 0 && side.equals("right")){
		Point leftPoint = new  Point(xLeftTop, yTop);
		frontRightPoints.add(leftPoint);
		Point rightPoint = new Point(xTopRight, yTop); 
		frontRightPoints.add(rightPoint);
		}
	}
	private void renderRoom(Pane renderGroup) {
		renderLeftWall(renderGroup);
		renderFrontWall(renderGroup);
		renderRightWall(renderGroup);
	}

	private void renderLeftWall(Pane renderGroup) {
		double currentTileHeight = tileHeight * scaleY;
		Point xTopRight = (leftSidePoints.get(0))[0];
		Point xBottomLeft = (leftSidePoints.get(0))[1];
		double xTopLeft = (leftSidePoints.get(0))[2].getX();
		double xTopLeft_Wall =  (xTopLeft - (0 * Math.abs(xTopRight.getX() - xTopLeft) )); //xLeftTop-previousWidth
		double xBottomRight_Wall = xBottomLeft.getX() - 0 * currentTileHeight; 
		double	yTopLeft_Wall = (xTopRight.getX() -((tileHeight - (50) ) * Math.pow(0.25, 0)));
		double  yTopRight_Wall = (xTopRight.getX() -((tileHeight -(10)) * Math.pow(0.1, 0)));
		double tempTop = yTopLeft_Wall-20; //so that the previous bottom Y will be the top Y of the next wall
		System.out.println("temp top " + tempTop);
		addWall(wallPolygon, xTopLeft_Wall, xTopLeft_Wall, xBottomRight_Wall, xBottomRight_Wall, yTopRight_Wall, tempTop, xBottomLeft.getY(), xTopRight.getY(), renderGroup);
		for (int row =1; row != squaresInFront ; row++) {
			wallPolygon = new Polygon();
			tempTop = yTopRight_Wall;
			xTopRight = (leftSidePoints.get(row))[0];
			 xBottomLeft = (leftSidePoints.get(row))[1];
			 xTopLeft = (leftSidePoints.get(row))[2].getX();
			 xTopLeft_Wall =  (xTopLeft - (0 * Math.abs(xTopRight.getX() - xTopLeft) ));
			 xBottomRight_Wall = xBottomLeft.getX() - 0 * currentTileHeight;
			 yTopLeft_Wall = (xTopRight.getX() -((tileHeight - (50) ) * Math.pow(0.25, 0)));
			 yTopRight_Wall = (xTopRight.getX() -((tileHeight -(10)) * Math.pow(0.1, 0)));
			currentTileHeight = currentTileHeight * scaleY;
			addWall(wallPolygon, xTopLeft_Wall, xTopLeft_Wall, xBottomRight_Wall, xBottomRight_Wall, yTopRight_Wall, tempTop, xBottomLeft.getY(), xTopRight.getY(), renderGroup);
		}
	}
	
	
	public void renderRightWall(Pane renderGroup){
		wallPolygon = new Polygon();
		Point previousPoint = (leftSidePoints.get(0))[0];
		double	leftTopY_Wall = (previousPoint.getX() -((tileHeight - (50) ) * Math.pow(0.25, 0))) -20;
		Point xbottomRight = (rightSidePoints.get(0))[1];
		Point xTopLeft= (rightSidePoints.get(0))[2];
		double currentTileHeight = tileHeight * scaleY;
		double xTopRight = (rightSidePoints.get(0))[0].getX();
		double  yTopRight_Wall = leftTopY_Wall -((tileHeight  ) * Math.pow(1.25, 0));
		double tempTop = leftTopY_Wall; //so that the previous bottom Y will be the top Y of the next wall;

		addWall(wallPolygon,xTopRight,xTopRight, xbottomRight.getX() , xbottomRight.getX(), yTopRight_Wall,tempTop , xbottomRight.getY(), xTopLeft.getY(), renderGroup);	

		for (int row =1; row != squaresInFront ; row++) {
			tempTop = yTopRight_Wall;
			wallPolygon = new Polygon();
			xTopLeft = (rightSidePoints.get(row))[2];
			 xbottomRight = (rightSidePoints.get(row))[1];
			 xTopRight = (rightSidePoints.get(row))[0].getX();
			 double xBottomRight_Wall = xbottomRight.getX() + 0 * currentTileHeight;
			 yTopRight_Wall =yTopRight_Wall  -((tileHeight  ) * Math.pow(1.25, row)) ;
			 //TODO: ONCE ANGELO FIXED HIS RENDERING ISSUES. HAVE A BOUNDS CHECK HERE
			 //SO THE WALL HEIGHT IS NOT GREATER THAN THE GUI PANE HEIGHT
			 if(Math.abs(yTopRight_Wall) > (gamePaneHeight * 0.65)){return;}
			currentTileHeight = currentTileHeight * scaleY;
		addWall(wallPolygon, xTopRight, xTopRight, xBottomRight_Wall, xBottomRight_Wall, yTopRight_Wall, tempTop, xbottomRight.getY(), xTopLeft.getY(), renderGroup);

		}	
		
	}
	public void renderFrontWall(Pane renderGroup){
		wallPolygon = new Polygon();
		Point xTopRight = (leftSidePoints.get(0))[0];
		double	leftTopY_Wall = (xTopRight.getX() -((tileHeight - (50) ) * Math.pow(0.25, 0))) -20;
		double xRightTop = centerWidth + tileWidth / 2;
		double yTop = getTopOffset();
		System.out.println(yTop + "\n\n\n");

		double xLeftTop = centerWidth - tileWidth / 2;	
		addWall(wallPolygon, xLeftTop, xLeftTop, xRightTop, xRightTop, leftTopY_Wall, leftTopY_Wall, yTop, yTop, renderGroup);
		for(int squaresInLeft = 0 ; squaresInLeft<frontLeftPoints.size()-1; squaresInLeft++){
			wallPolygon = new Polygon();	
			Point topLeft = (frontLeftPoints.get(squaresInLeft));
			Point topRight = (frontLeftPoints.get(squaresInLeft+1));
			addWall(wallPolygon, topLeft.getX(), topLeft.getX(), topRight.getX(), topRight.getX(), yTop, yTop, leftTopY_Wall, leftTopY_Wall, renderGroup);			
		}	
		for(int squaresInRight = frontRightPoints.size()-1 ; squaresInRight>0; squaresInRight--){
			wallPolygon = new Polygon();	
			Point topLeft = (frontRightPoints.get(squaresInRight));
			Point topRight = (frontRightPoints.get(squaresInRight-1));
			System.out.println("top right x" + topRight.getX());
			addWall(wallPolygon, topLeft.getX(), topLeft.getX(), topRight.getX(), topRight.getX(), yTop, yTop, leftTopY_Wall, leftTopY_Wall, renderGroup);			
		}
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

	private void addWall(Polygon p, double xLeftTop, double xRightTop, double xRightBottom, double xLeftBottom,
			double yLeftBottom, double yLeftTop, double yRightBottom, double yRightTop, Pane renderGroup) {
		Image grass = loadImage(BRICK_IMAGE);
		p.setLayoutY(10);
		p.setFill(new ImagePattern(grass));
		p.getPoints().add(xLeftTop);
		p.getPoints().add(yLeftTop);
		p.getPoints().add(xRightTop);
		p.getPoints().add(yRightTop);
		p.getPoints().add(xRightBottom);
		p.getPoints().add(yRightBottom);
		p.getPoints().add(xLeftBottom);
		 p.getPoints().add(yLeftBottom);
		 p.setStroke(javafx.scene.paint.Color.ANTIQUEWHITE);
			p.setStrokeWidth(1);
		renderGroup.getChildren().add(p);
	}

	private void setNumSquares(int height, int width, Direction direction, Position playerLoc) {
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
			squaresToLeft = playerLoc.y;
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
			squaresToRight = height - playerLoc.y - 2;
			break;
		}
	}
	public void charRender() {

	}

	private void addObject(double tileXLeftBottom, double yBottom, double tileXRightBottom, int row, int col,
			String side, char[][] worldMap, Pane renderGroup, Direction direction, double yTop) {
		Point imageCoordinate = getImagePoint(direction, row, col, side, worldMap.length, worldMap[0].length);
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
				return new Point(squaresToLeft - col - 1, row);
			} else if (side.equals("right")) {
				return new Point(squaresToLeft + col + 1, row);
			} else {
				return new Point(col, row);
			}
		case South:
			if (side.equals("left")) {

				return new Point(col + 1, boardHeight - row - 1);

			} else if (side.equals("right")) {
				return new Point(squaresToLeft - col, boardHeight - row - 1);
			} else {
				return new Point(col, boardHeight - row - 1);
			}
		case East:
			if (side.equals("left")) {
				return new Point(boardWidth - row - 1, squaresToLeft - col - 1);
			} else if (side.equals("right")) {
				return new Point(boardWidth - row - 1, squaresToLeft + col + 1);
			} else {
				return new Point(boardWidth - row - 1, col);
			}
		case West:
			if (side.equals("left")) {
				return new Point(row, col + squaresToLeft);
			} else if (side.equals("right")) {
				return new Point(row, squaresToRight - col - 1);
			} else {
				return new Point(row, col);
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
	private Image loadImage(String name) {
		Image image = new Image(this.getClass().getResourceAsStream(name));
		return image;
	}

}
