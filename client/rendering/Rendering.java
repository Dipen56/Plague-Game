package client.rendering;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

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
import javafx.scene.paint.*;

import static client.view.GUI.loadImage;

/**
 * This class represents the main rendering class, this class will control the rendering
 * of the game board, character, and objects.
 * 
 * @author Dipen
 *
 */

public class Rendering {
    private static final String PLAYER_IMAGE = "/standingstillrear.png";
    // private static final String BACKGROUND_IMAGE = "/background.gif";
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
    private Point playerLoc = new Point(5, 1);
    private int squaresInFront = 0;
    private int squaresToLeft = 0;
    private int squaresToRight = 0;
    private Player player;
    private Map<Integer, Player> playersOnMap;
    private Map<Integer, Area> map;
    private int avatarID;
    private String direction;

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
     * this constructor is to be used for integration and will be passed in a Player and a
     * map, and also all the other player on the map and also the avatar id atm.
     * 
     * @param player
     */
    public Rendering(Player player, Map<Integer, Player> playersOnMap,
            Map<Integer, Area> map, int avatarID) {
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
    public void render(Group renderGroup) {
        // TODO: get ride of the renderGroup parameters call the set group
        // method below first before calling this method
        this.group = renderGroup;
        Image image = loadImage(BACKGROUND_IMAGE);
        Image character = loadImage(PLAYER_IMAGE);
        Image grass = loadImage(GRASS_IMAGE);
        ImageView imageViewNight = new ImageView();
        imageViewNight.setImage(image);
        imageViewNight.setFitWidth(gamePaneWidth + 3);
        imageViewNight.setFitHeight(gamePaneHeight + 35);
        group.getChildren().add(imageViewNight);
        // int squaresInFront = 0;// the number of squares on the player's face
        // side
        // int squaresToLeft = 0;// the number of squares on the player's left
        // side
        // int squaresToRight = 0;// //the number of squares on the player's
        // right side
        // this will be changed to the real players pos apon integration
        // playerLoc = new Point(5, 0);
        int boardSize = 10;
        // squaresInFront = boardSize - playerLoc.y;
        // squaresToLeft = (boardSize - playerLoc.x) - 1;
        // squaresToRight = (boardSize - squaresToLeft);
        if (direction.equals("up")) {
            squaresInFront = boardSize - playerLoc.y;
            squaresToLeft = (boardSize - playerLoc.x) - 1;
            squaresToRight = (boardSize - squaresToLeft);
        } else if (direction.equals("down")) {
        } else if (direction.equals("left")) {
            squaresToRight = squaresInFront;
            squaresInFront = squaresToLeft;
            squaresToLeft = boardSize - squaresToRight;
        } else if (direction.equals("right")) {
            squaresToLeft = squaresInFront;
            squaresInFront = squaresToRight;
            squaresToRight = (boardSize - squaresToLeft);
        }

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

        }
        // TODO: call the render avatar method here
        // renderObjects();

        renderObjects();
    }

    /**
     * this method is used to render the object in the game.
     */
    public void renderObjects() {
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

        // the 10 will be the sqare in front of the players
        for (int row = 0; row < 10; row++) {
            int col = playerLoc.x;
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
                // this will be the sqares to the left

                for (int j = 0; j < 3; j++) {
                    if (worldMap[row][col - j].equals("tree")
                            || worldMap[row][col - j].equals("chest")) {
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
                        double tempLeftx1 = nowStartX + nowWidthOfSquare
                                - j * nowWidthOfSquare;
                        double tempLefty1 = topLine
                                - tileHeight * Math.pow(scaleY, row + 1);
                        double tempLeftx0 = nowStartX - j * nowWidthOfSquare;
                        double tempLefty0 = topLine
                                - tileHeight * Math.pow(scaleY, row + 1);

                        double heightleft = (tempLefty3 - tempLefty0);
                        double widthleft = (tempLeftx2 - tempLeftx3);
                        if (worldMap[row][col - j].equals("tree")) {
                            Image tree = loadImage(TREE_IMAGE);
                            ImageView imageViewTree = new ImageView();
                            imageViewTree.setImage(tree);

                            imageViewTree.setFitHeight(heightleft);
                            imageViewTree.setFitWidth(widthleft);
                            // here is where we need to do the logic
                            imageViewTree.setX(tempLeftx0);
                            imageViewTree.setY(tempLefty0);
                            group.getChildren().add(imageViewTree);
                        } else if (worldMap[row][col - j].equals("chest")) {
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

            }
            col = playerLoc.x;
            for (int j = 0; j < 3; j++) {
                if (worldMap[row][col - j].equals("tree")
                        || worldMap[row][col - j].equals("chest")) {

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
                    if (tempRightx3 > gamePaneWidth) {
                        tempRightx3 = gamePaneWidth;
                    }

                    double tempRighty3 = y3;
                    double tempRightx2 = x2 + (j * previouWidthOfSquare);
                    if (tempRightx2 > gamePaneWidth) {
                        tempRightx2 = gamePaneWidth;
                    }

                    double tempRighty2 = y2;
                    double tempRightx1 = nowStartX + nowWidthOfSquare
                            + j * nowWidthOfSquare;
                    if (tempRightx1 > gamePaneWidth) {
                        tempRightx1 = gamePaneWidth;
                    }
                    // this is the top part to the tile and will be scaled on
                    // the
                    // left from larger
                    // to smaller

                    double tempRightY1 = topLine - tileHeight * Math.pow(scaleY, row + 1);
                    double tempRightx0 = nowStartX + j * nowWidthOfSquare;
                    if (tempRightx0 > gamePaneWidth) {
                        tempRightx0 = gamePaneWidth;
                    }

                    double tempRighty0 = topLine - tileHeight * Math.pow(scaleY, row + 1);
                    double heightRight = (tempRighty3 - tempRighty0);
                    double widthRight = (tempRightx2 - tempRightx3);
                    // System.out.println(gamePaneWidth);
                    // System.out.println(tempRightx0 + " " + tempRightx1 + " "
                    // + tempRightx2 + " " + tempRightx3);
                    if (tempRightx2 != gamePaneWidth && tempRightx1 != gamePaneWidth) {
                        // System.out.println(worldMap[row][col - j]);
                        if (worldMap[row][col - j].equals("tree")) {
                            Image tree = loadImage(TREE_IMAGE);
                            ImageView imageViewTree = new ImageView();
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
     * this method is used to move the player in the screen given player, it then calls
     * the render method
     * 
     * @param player
     */
    public void movePlayer(Player player) {
        this.player = player;
        // TODO: call render;
        // render();
    }

    /**
     * this method is used to set the group which is used to render the images on the
     * screen and will be used by the gui class
     * 
     * @param renderGroup
     */
    public void setGroup(Group renderGroup) {
        this.group = renderGroup;
    }

    /**
     * Redraw the rendering panel
     */
    public void redraw() {
        Map<Integer, Position> positions = controller.getPositions();
        Position selfPosition = positions.get(controller.getUid());
        int areaId = selfPosition.areaId;
        char[][] map = controller.getCharMapByAreaId(areaId);
        int visibility = controller.getVisibility();

        redraw(positions, map, visibility);
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
    private void redraw(Map<Integer, Position> positions, char[][] areaMap,
            int visibility) {

        // player's coordinate on board, and direction.
        Position selfPosition = positions.get(controller.getUid());

        int x = selfPosition.x;
        int y = selfPosition.y;
        Direction direction = selfPosition.getDirection();

        // TODO redraw the rendering panel

    }

    @Override
    public String toString() {

        return "renderclass";
    }

}
