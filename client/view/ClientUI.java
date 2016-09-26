package client.view;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;

import client.Client;
import client.ParserUtilities;
import client.rendering.Rendering;
import server.Packet;
import server.game.player.Avatar;
import server.game.player.Position;
import server.game.player.Virus;

/**
 * This class is the client side UI, which is where the user start the game from. It also
 * servers as the controller for communicating between client and GUI/Renderer. The
 * controller tells the server about the user's action by interpreting mouse and keyboard
 * events from the user, and updates the renderer/GUI according to the received
 * information from server.
 * 
 * @author Dipen
 *
 */
public class ClientUI {

    // ============ info fields =================

    /**
     * The period between every update
     */
    public static final int DEFAULT_CLK_PERIOD = 100;

    /**
     * This is designed as a table for renderer to index char board to render objects.
     */
    private static final Map<Character, String> MAP_OBJECTS_TABLE;

    // ITEM_TABLE is not a good idea. leave it aside for now.
    private static final Map<Character, String> ITEM_TABLE;

    /*
     * Initialise the table for Renderer. Each table contains a map which maps a char to
     * the corresponding object, so the Renderer knows what to render by knowing what char
     * was sent by server.
     */
    static {
        MAP_OBJECTS_TABLE = new HashMap<>();
        ITEM_TABLE = new HashMap<>();

        /*
         * TODO This is probably not appropriate, some map objects may need more than one
         * png path, e.g. a room has four sides of views, each of them should be
         * different.
         * 
         * But the idea is, we initialise this map for renderer so that renderer knows
         * what map object to render by looking into this map.
         */

        // ============= map objects ====================

        MAP_OBJECTS_TABLE.put('E', "/Resourse/Ground.png");
        MAP_OBJECTS_TABLE.put('C', "/Resourse/Chest.png");
        MAP_OBJECTS_TABLE.put('T', "/Resourse/Tree.png");
        MAP_OBJECTS_TABLE.put('R', "/Resourse/Rock.png");
        MAP_OBJECTS_TABLE.put('B', "/Resourse/Cupboard.png");
        MAP_OBJECTS_TABLE.put('S', "/Resourse/ScrapPile.png");
        MAP_OBJECTS_TABLE.put('1', "/Resourse/Room_1.png");
        MAP_OBJECTS_TABLE.put('2', "/Resourse/Room_2.png");
        MAP_OBJECTS_TABLE.put('3', "/Resourse/Room_3.png");
        MAP_OBJECTS_TABLE.put('4', "/Resourse/Room_4.png");
        MAP_OBJECTS_TABLE.put('5', "/Resourse/Room_5.png");
        MAP_OBJECTS_TABLE.put('6', "/Resourse/Room_6.png");
        MAP_OBJECTS_TABLE.put('7', "/Resourse/Room_7.png");
        MAP_OBJECTS_TABLE.put('8', "/Resourse/Room_8.png");
        MAP_OBJECTS_TABLE.put('9', "/Resourse/Room_9.png");
        MAP_OBJECTS_TABLE.put('a', "/Resourse/Room_10.png");
        MAP_OBJECTS_TABLE.put('b', "/Resourse/Room_11.png");

        // this is the TransitionSpace, which is actually a normal ground for renderer.
        MAP_OBJECTS_TABLE.put('D', "/Resourse/Ground.png");

        // ============= inventory objects ====================

        ITEM_TABLE.put('A', "/Resourse/Antidote.png");
        ITEM_TABLE.put('K', "/Resourse/Key.png");
        ITEM_TABLE.put('T', "/Resourse/Torch.png");

    }

    /**
     * User id of this connection.
     */
    private int uid;
    /**
     * User name of this connection.
     */
    private String userName;
    /**
     * Avatar type of this connection.
     */
    private Avatar avatar;
    /**
     * Virus type of the player at this connection
     */
    private Virus virus;
    /**
     * The health left. This is updated by server broadcast.
     */
    private int health;
    /**
     * The visibility. This is updated by server broadcast.
     */
    private int visibility;
    /**
     * This map keeps track of all player's avatars. Renderer can look for which avatar to
     * render from here.
     */
    private Map<Integer, Avatar> avatars;
    /**
     * This map keeps track of all player's Positions. Renderer can look for where to
     * render different players from here.
     */
    private Map<Integer, Position> positions;
    /**
     * This list keeps track of this player's inventory. Each item is represented as a
     * String whose format is: Character|Description. <br>
     * <br>
     * e.g. "A|An antidote. It has a label: Type G"<br>
     * <br>
     * The character at beginning is used to look for resource image. The description is
     * used to pop up a hover tootip for this item.
     */
    private List<String> inventory;
    /**
     * This is a mirror of the field, Map<Integer, Area> areas, in Game class, except the
     * area is represented as a char[][]. Renderer can look for what map object to render
     * from here.
     */
    private Map<Integer, char[][]> areas;

    // ============ Model and Views =============
    private GUI gui;
    private Rendering render;
    private Client client;
    private ClockThread clockThread;

    // ============ Event Handlers ==============

    // for clicks
    private EventHandler<ActionEvent> actionEvent;
    // for keys inputs
    private EventHandler<KeyEvent> keyEvent;
    // for mouse events
    private EventHandler<MouseEvent> mouseEvent;
    // for window resizing not really need else where
    private EventHandler<WindowEvent> windowEvent;

    public ClientUI() {
        // initialise the info fields.
        areas = new HashMap<>();
        avatars = new HashMap<>();
        positions = new HashMap<>();

        // TODO: need to uses the other constructor
        render = new Rendering();
        // TODO: get the actual player direction
        render.setDirection("up");
        gui = new GUI(this, render);

        GUI.launch(GUI.class);
    }

    /**
     * this method is used to connect the players to the client which then connects them
     * to the server
     * 
     * @param ip
     * @param port
     * @param userName
     * @param avatarIndex
     * @return
     */
    public boolean loginPlayer(String ip, int port, String userName, int avatarIndex) {

        if (!ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            GUI.showWarningPane("It's not a proper ip address.");
            return false;
        }

        // create a socket
        Socket s = null;
        try {
            s = new Socket(ip, port);
        } catch (IOException e) {
            GUI.showWarningPane(
                    "Failed to connect to server, I/O exceptions, " + e.toString());
            return false;
        }

        client = new Client(s, this);
        this.userName = userName;
        this.avatar = Avatar.get(avatarIndex);
        client.start();
        return true;
    }

    /**
     * this method is used to start the listeners
     */
    public void startListeners() {
        // start the gui components listener, key listener, and mouse listener
        setActionEventHandler();
        setKeyEventHander();
        setMouseEventHander();
    }

    /**
     * this method is called client receives a msg from anther client and need to update
     * this client. will need to pass a user or give the player to the constructor.
     * 
     * @param msg
     * @param user
     */
    public void setChatMasg(String msg, String user) {
        gui.setChatText(msg, user);
    }

    /*
     * ===============================
     * 
     * Methods related to the client
     * 
     * ===============================
     */

    /**
     * When the client receives the user ID from the server, this method will update the
     * local user ID.
     * 
     * @param uid
     */
    public void parseUID(int uid) {
        this.uid = uid;
    }

    /**
     * When the client receives the user's virus type from the server, this method will
     * update the local record
     * 
     * @param uid
     */
    public void parseVirus(int virusIndex) {
        this.virus = Virus.get(virusIndex);
    }

    /**
     * When the client receives a map string from the server, this method will update the
     * local table which records every area's map (in a plain char matrix).
     * 
     * @param mapStr
     */
    public void parseMap(String mapStr) {
        ParserUtilities.parseMap(areas, mapStr);
    }

    /**
     * When the client receives the string recording all players positions from the
     * server, this method will update the local table which records every player's
     * position.
     * 
     * @param posStr
     */
    public void parsePosition(String posStr) {
        ParserUtilities.parsePosition(positions, posStr);
    }

    /**
     * When the client receives the string recording all players avatars from the server,
     * this method will update the local table which records every player's avatar.
     * 
     * @param avatarsStr
     */
    public void parseAvatars(String avatarsStr) {
        ParserUtilities.parseAvatar(avatars, avatarsStr);
    }

    /**
     * When the client receives a time string from the server, this method will update the
     * local time.
     * 
     * @param timeStr
     */
    public void parseTime(String timeStr) {
        gui.setTime(timeStr);
    }

    /**
     * When the client receives a health update from the server, this method will update
     * the local health.
     * 
     * @param health
     */
    public void parseHealth(int health) {
        this.health = health;
    }

    /**
     * When the client receives a health update from the server, this method will update
     * the local health.
     * 
     * @param visibility
     */
    public void parseVisibility(int visibility) {
        this.visibility = visibility;

    }

    /**
     * When the client receives a inventory update from the server, this method will
     * update the local inventory.
     * 
     * @param invenStr
     */
    public void parseInventory(String invenStr) {
        inventory = ParserUtilities.parseInventory(invenStr);
    }

    /**
     * Get the user name
     * 
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get the avatar.
     * 
     * @return
     */
    public Avatar getAvatar() {
        return avatar;
    }

    /*
     * ===============================
     * 
     * Methods related to the render
     * 
     * ===============================
     */

    /**
     * This method is called by ClockThread periodically to update the renderer and GUI.
     */
    public void updateRenderAndGui() {
        // ====================
        // These method should be somewhere for rendering

        // /**
        // * Redraw the rendering panel
        // */
        // public void redraw() {
        // Map<Integer, Position> positions = controller.getPositions();
        // Position selfPosition = positions.get(controller.getUid());
        // int areaId = selfPosition.areaId;
        // char[][] map = controller.getCharMapByAreaId(areaId);
        // int visibility = controller.getVisibility();
        //
        // redraw(positions, map, visibility);
        // }
        //
        // /**
        // * Redraw the rendering panel.
        // *
        // * @param positions
        // * --- the position of all player.
        // * @param areaMap
        // * --- the area map represented as a char[][]
        // * @param visibility
        // * --- current visibility.
        // */
        // private void redraw(Map<Integer, Position> positions, char[][] areaMap,
        // int visibility) {
        //
        // // player's coordinate on board, and direction.
        // Position selfPosition = positions.get(controller.getUid());
        //
        // int x = selfPosition.x;
        // int y = selfPosition.y;
        // Direction direction = selfPosition.getDirection();
        //
        // // TODO redraw the rendering panel
        //
        // }

    }

    public void startGame() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gui.startGame();

                clockThread = new ClockThread(DEFAULT_CLK_PERIOD, ClientUI.this);

                clockThread.start();
            }
        });

    }

    /**
     * this method is used to check for action and give a implementation of handle method
     */
    private void setActionEventHandler() {
        actionEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.toString().contains("Send")) {
                    /*
                     * TODO: connect up this with the client and server so that others can
                     * recive the msg. to get the msg typed in the text filed use
                     * gui.getChatMsg()
                     */
                    // System.out.println(gui.getChatMsg());

                } else if (event.toString().contains("Play")) {
                    // this is used to simply change the scene
                    gui.loginScreen();
                } else if (event.toString().contains("Run Away")) {
                    // this is for the main screen of the game
                    gui.getWindow().close();
                } else if (event.toString().contains("Help")) {
                    // TODO: need to make a help thing which tells the user how
                    // to play the game
                } else if (event.toString().contains("Login")) {
                    // parse the port number to int
                    int port = -1;
                    try {
                        port = Integer.valueOf(gui.getPortString());
                    } catch (NumberFormatException e) {
                        GUI.showWarningPane("Port number should be an integer");
                        return;
                    }

                    loginPlayer(gui.getIpAddress(), port, gui.getUserName(),
                            gui.getAvatarIndex());

                    // in the waiting room
                    gui.waitingRoom();

                } else if (event.toString().contains("Leave")) {
                    // this is for the login screen
                    gui.getWindow().close();
                } else if (event.toString().contains("Ready")) {

                    /*
                     * TODO set the ready button unavailable. tell the player it's waiting
                     * for others.
                     */

                    client.setUserReady(true);
                } else if (event.toString().contains("Leave Game")) {
                    // this is for leaving the waiting room
                    gui.getWindow().close();
                }

            }
        };
    }

    /**
     * this methods is used to listen for keys being pressed and will respond Accordingly
     */
    private void setKeyEventHander() {
        keyEvent = new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                // getSorce will give the control which caused the event
                if (keyCode == KeyCode.LEFT || keyCode == KeyCode.A) {
                    client.send(Packet.Left);
                } else if (keyCode == KeyCode.RIGHT || keyCode == KeyCode.R) {
                    client.send(Packet.Right);
                } else if (keyCode == KeyCode.UP || keyCode == KeyCode.W) {
                    client.send(Packet.Forward);
                } else if (keyCode == KeyCode.DOWN || keyCode == KeyCode.S) {
                    client.send(Packet.Backward);
                } else if (keyCode == KeyCode.Q) {
                    client.send(Packet.TurnLeft);
                } else if (keyCode == KeyCode.E) {
                    client.send(Packet.TurnRight);
                } else if (keyCode == KeyCode.F) {
                    client.send(Packet.Unlock);
                } else if (keyCode == KeyCode.G) {
                    client.send(Packet.TakeOutItem);
                } else if (keyCode == KeyCode.R) {
                    client.send(Packet.Transit);
                } else if (keyCode == KeyCode.DIGIT1) {
                    client.sendWithIndex(Packet.UseItem, 0);
                } else if (keyCode == KeyCode.DIGIT2) {
                    client.sendWithIndex(Packet.UseItem, 1);
                } else if (keyCode == KeyCode.DIGIT3) {
                    client.sendWithIndex(Packet.UseItem, 2);
                } else if (keyCode == KeyCode.DIGIT4) {
                    client.sendWithIndex(Packet.UseItem, 3);
                } else if (keyCode == KeyCode.DIGIT5) {
                    client.sendWithIndex(Packet.UseItem, 4);
                } else if (keyCode == KeyCode.DIGIT6) {
                    client.sendWithIndex(Packet.UseItem, 5);
                } else if (keyCode == KeyCode.DIGIT7) {
                    client.sendWithIndex(Packet.UseItem, 6);
                } else if (keyCode == KeyCode.DIGIT8) {
                    client.sendWithIndex(Packet.UseItem, 7);
                }

                /*
                 * TODO need more keys
                 * 
                 * How to implement shift + 1 keys???
                 * 
                 * 
                 */

            }
        };
    }

    /**
     * this this is used to listen for mouse clicks on different controls
     */
    private void setMouseEventHander() {
        mouseEvent = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                // Currently this listen to clicks on the items
                // TODO: some how make it work with items
                System.out.println("here");
            }
        };
    }

    /**
     * this method will return the action listeners
     * 
     * @return
     */
    public EventHandler<ActionEvent> getActionEventHandler() {
        return actionEvent;
    }

    /**
     * this method will return the key listeners
     * 
     * @return
     */
    public EventHandler<KeyEvent> getKeyEventHander() {
        return keyEvent;
    }

    /**
     * this method will return the mouse listener
     * 
     * @return
     */
    public EventHandler<MouseEvent> getMouseEventHander() {
        return mouseEvent;
    }

    /**
     * this method will return the window listener
     * 
     * @return
     */
    public EventHandler<WindowEvent> getWindowEventHander() {
        return windowEvent;
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        new ClientUI();
    }

}
