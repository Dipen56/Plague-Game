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
import javafx.scene.paint.*;

/**
 * This class represents the main rendering class, this class will control the
 * rendering of the game board, character, and objects.
 *
 * @author Angelo & Dipen
 *
 */

public class Rendering {
	private static final String PLAYER_IMAGE = "/standingstillrear.png";
	//private static final String BACKGROUND_IMAGE = "/background.gif";
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
	public int centerHeght = gamePaneHeight;
	private MapParser mapParser;
	private Point playerLoc = new Point (5,0);
	private int squaresInFront =0;
	private int squaresToLeft =0;
	private int squaresToRight= 0;


	public Rendering() {
		// will need to get board size passed in
		mapParser = new MapParser(10, 10);
	}


	/**
	 * this method is used to render the game
	 * @param direction 
	 * @param group
	 */
	public void render(Group renderGroup, String direction) {
		this.group = renderGroup;
		Image character =  loadImage(PLAYER_IMAGE);
		Image image = loadImage(BACKGROUND_IMAGE);
		Image grass = loadImage(GRASS_IMAGE);
		ImageView imageViewNight = new ImageView();
		imageViewNight.setImage(image);
		imageViewNight.setFitWidth(gamePaneWidth + 3);
		imageViewNight.setFitHeight(gamePaneHeight + 35);
		group.getChildren().add(imageViewNight);
		 //squaresInFront = 0;// the number of squares on the player's face
		// side
		 //squaresToLeft = 0;// the number of squares on the player's left
		// side
		 //squaresToRight = ;// //the number of squares on the player's
		// right side
		// this will be changed to the real players pos apon integration
		//this.playerLoc = new Point(5, 0);
		
		int boardSize = 10;
		int north = boardSize - playerLoc.y;
		int west = (boardSize - playerLoc.x) - 1;
		int east = (boardSize - squaresToLeft);
		////////////////////////////////////
		if (direction.equals("up")){
			squaresInFront =north;
			squaresToLeft = west;
			squaresToRight = east;
			String values = "squares in front " + squaresInFront +"\n";
			values+= "squares to the left" + squaresToLeft + "\n";
			values+= "squares to the right " + squaresToRight + "\n";
			//playerLoc.y+=1; 
			System.out.println("up/n " + values);
			System.out.println("Loc Y " + playerLoc.y);
			System.out.println("Loc X " + playerLoc.x);

		}
		else if (direction.equals("down")){
//			//Assumes that when player wants to move south
//			//player will always turn right to face south
//			int l1 =  (boardSize - playerLoc.y);
//			int f1 = (boardSize -  playerLoc.x) +1;
//			int r1 =(boardSize -  squaresToLeft) ;
//			
//			squaresInFront = r1;
//			squaresToLeft = f1;
//			squaresToRight = (boardSize -f1);
			playerLoc.y-=1; 

			
		}
		else if(direction.equals("left")){
			
			squaresToRight =  north ;
			squaresInFront = west ;
			squaresToLeft = boardSize - east ;
			String values = "squares in front " + squaresInFront +"\n";
			values+= "squares to the left" + squaresToLeft + "\n";
			values+= "squares to the right " + squaresToRight + "\n";
			System.out.println("left \n" + values);

		}
		else if (direction.equals("right")){
			squaresToLeft = north;
			squaresInFront = east;
			squaresToRight =(boardSize -  west) ;

			String values = "squares in front " + squaresInFront +"\n";
			values+= "squares to the left" + squaresToLeft + "\n";
			values+= "squares to the right " + squaresToRight + "\n";
			System.out.println("right \n" + values);


		}
		//////////////////////////////////////////////////
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
			//Renders the player onto the board
			charRender();
		}
	//	renderObjects();
	}

	public void charRender(){


	}

	/**
	 * this method is used to render the object in the game.
	 */
	public void renderObjects() {
		String[][] worldMap = mapParser.getMap();
		System.out.println(worldMap);
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
		int col = playerLoc.x;
		// the 10 will be the sqare in front of the players
		for (int row = 0; row < 10; row++) {
			double nowWidthOfSquare = tileWidth * Math.pow(scaleY, row + 1);
			// this is the top part of the tile starting x pos
			double nowStartX = centerWidth - nowWidthOfSquare / 2;
			// front view
			if (worldMap[row][col].equals("tree") || worldMap[row][col].equals("chest")) {
				// this will draw the object that are in front
				double x1 = nowStartX + nowWidthOfSquare;
				double y1 = topLine - tileHeight * Math.pow(scaleY, row + 1);
				double x0 = nowStartX;
				double y0 = topLine - tileHeight * Math.pow(scaleY, row + 1);
				double height = (y3 - y0);
				double width = (x2 - x3);
				System.out.println(worldMap[row][col]);
				if (worldMap[row][col].equals("tree")) {
					Image tree = loadImage(TREE_IMAGE);
					ImageView imageViewTree = new ImageView();
					imageViewTree.setImage(tree);
					System.out.println("tree height " + height);

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
				//this will be the sqares to the left
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


	private Image loadImage(String name) {
		Image image = new Image(this.getClass().getResourceAsStream(name));
		return image;
	}

	@Override
	public String toString() {
		return "renderclass";
	}




}
