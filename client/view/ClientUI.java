package client.view;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
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
import server.game.player.Direction;
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
    /**
     * The period between every update
     */
    // This is the default clock thread
    // public static final int DEFAULT_CLK_PERIOD = 100;

    // Testing clock thread
    public static final int DEFAULT_CLK_PERIOD = 50;
    // ============ info fields =================
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
     * This map keeps track of the status for all players that whether he is holding a
     * torch.
     */
    private Map<Integer, Boolean> torchStatus;
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
    /**
     * The Gui
     */
    private GUI gui;
    /**
     * The renderer
     */
    private Rendering render;
    /**
     * The client side socket connection maintainer
     */
    private Client client;
    /**
     * A clock thread for generating constant pulse to update rendering and GUI.
     */
    private ClockThread clockThread;
    // ============ Event Handlers ==============
    /**
     * Event Handler for buttons
     */
    private EventHandler<ActionEvent> actionEvent;
    /**
     * Event Handler for key events
     */
    private EventHandler<KeyEvent> keyEvent;
    /**
     * Event Handler for mouse events
     */
    private EventHandler<MouseEvent> mouseEvent;
    /**
     * Event Handler for window events
     */
    private EventHandler<WindowEvent> windowEvent;

    /**
     * Constructor
     */
    public ClientUI() {
        areas = new HashMap<>();
        avatars = new HashMap<>();
        positions = new HashMap<>();
        torchStatus = new HashMap<>();
        // TODO: need to uses the other constructor
        // render = new Rendering();
        render = new Rendering();
        // TODO: get the actual player direction
        gui = new GUI(this, render);
        GUI.launch(GUI.class);
    }

    /**
     * This method is used to connect the players to the client which then connects them
     * to the server
     * 
     * @param ip
     *            --- the server ip address.
     * @param port
     *            --- the port number
     * @param userName
     *            --- the user name
     * @param avatarIndex
     *            --- the index of Avatar that the player chose.
     * @return
     */
    public boolean loginPlayer(String ip, int port, String userName, int avatarIndex) {
        // ip address format check
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
     * This method is used to start the listeners
     */
    public void startListeners() {
        // start the gui components listener, key listener, and mouse listener
        setActionEventHandler();
        setKeyEventHander();
        setMouseEventHander();
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
     *            --- user id
     */
    public void parseUID(int uid) {
        this.uid = uid;
    }

    /**
     * When the client receives the user's virus type from the server, this method will
     * update the local record
     * 
     * @param virusIndex
     *            --- index of virus, which is equal to its ordinal number.
     */
    public void parseVirus(int virusIndex) {
        this.virus = Virus.get(virusIndex);
    }

    /**
     * When the client receives a map string from the server, this method will update the
     * local table which records every area's map (in a plain char matrix).
     * 
     * @param mapStr
     *            --- a string representation of all maps in game.
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
     *            --- a string representation of all positions of players.
     */
    public void parsePosition(String posStr) {
        ParserUtilities.parsePosition(positions, posStr);
    }

    /**
     * When the client receives the string recording all players avatars from the server,
     * this method will update the local table which records every player's avatar.
     * 
     * @param avatarsStr
     *            --- a string representation of all avatars of all players.
     */
    public void parseAvatars(String avatarsStr) {
        ParserUtilities.parseAvatar(avatars, avatarsStr);
    }

    /**
     * When the client receives a time string from the server, this method will update the
     * local time.
     * 
     * @param timeStr
     *            --- a string representation of world time.
     */
    public void parseTime(String timeStr) {
        gui.setTime(timeStr);
    }

    /**
     * When the client receives a health update from the server, this method will update
     * the local health.
     * 
     * @param health
     *            --- the health left
     */
    public void parseHealth(int health) {
        this.health = health;
    }

    /**
     * When the client receives a health update from the server, this method will update
     * the local health.
     * 
     * @param visibility
     *            --- the visibility
     */
    public void parseVisibility(int visibility) {
        this.visibility = visibility;
    }

    /**
     * When the client receives a inventory update from the server, this method will
     * update the local inventory.
     * 
     * @param invenStr
     *            --- a string representation of inventory items.
     */
    public void parseInventory(String invenStr) {
        inventory = ParserUtilities.parseInventory(invenStr);
    }

    /**
     * When the client receives the string recording the status of player holding torch
     * from the server, this method will update the local table which records every
     * player's status of holding torch.
     * 
     * @param torchStatusStr
     *            --- a string representation of the status of player holding torch
     */
    public void parseTorchStatus(String torchStatusStr) {
        ParserUtilities.parseTorchStatus(torchStatus, torchStatusStr);
    }

    /**
     * When the client receives the string of chat message from the server, this method
     * will update the chat text area.
     * 
     * @param chat
     *            --- a string representation of the status of player holding torch
     */
    public void parseChatMessage(String chat) {
        gui.setChatText(chat);
    }

    /**
     * Get the user name
     * 
     * @return --- the player's choice of user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get the avatar.
     * 
     * @return --- the player's choice of avatar
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
        // 1. update GUI
        // a. update minimap
        // b. update the inventory
        gui.setInventory(inventory);
        // c. update the health bar if it is in right panel in GUI.
        // 2. update Renderer
        // a. call update renderer method.
        // ====================
        Position playerLoc = positions.get(uid);
        // System.out.println("X:" +playerLoc.x+" Y: "+playerLoc.y);
        int areaId = playerLoc.areaId;
        char[][] worldMap = areas.get(areaId);
        render.render(playerLoc, worldMap, visibility, uid);

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
        // private void redraw(Map<Integer, Position> positions, char[][]
        // areaMap,
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

    /**
     * Alert the Renderer and GUI to start the game.
     */
    public void startGame() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gui.startGame();
                // Position pos = new Position(4, 0, 1, Direction.South);
                // char[][] world = {
                // // NORTH
                // { 'C', 'T', 'T', 'G', 'G', 'T', 'C', 'T' },
                // // 2
                // { 'G', 'G', 'G', 'G', 'G', 'G', 'C', 'C' },
                // // 3
                // { 'G', 'G', 'G', 'G', 'G', 'G', 'C', 'T' },
                // // 4
                // { 'T', 'C', 'G', 'G', 'G', 'C', 'T', 'C' },
                // // 5
                // { 'G', 'G', 'G', 'G', 'G', 'G', 'C', 'T' },
                // // 6
                // { 'G', 'G', 'G', 'G', 'C', 'G', 'C', 'C' },
                // // 7
                // { 'G', 'G', 'C', 'G', 'T', 'G', 'C', 'G' },
                // // 8
                // { 'C', 'T', 'G', 'G', 'G', 'T', 'C', 'T' } };
                // // SOUTH
                // render.render(pos, world, 1, uid);
                clockThread = new ClockThread(DEFAULT_CLK_PERIOD, ClientUI.this);
                clockThread.start();
            }
        });
    }

    /**
     * This method is used to set action event handlers. The actions for certain button or
     * FX component events are defined here.
     */
    private void setActionEventHandler() {
        actionEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.toString().contains("Send")) {
                    // send chat message.
                    client.sendWithString(Packet.Chat, gui.getChatMsg());
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
                } else if (event.toString().contains("LoadMenu")) {
                    System.out.println("LOAD");
                } else if (event.toString().contains("SaveMenu")) {
                    System.out.println("SAVE");
                } else if (event.toString().contains("CloseMenu")) {
                    gui.getWindow().close();
                } else if (event.toString().contains("InfoMenu")) {
                    // TODO: make a info page
                    System.out.println("INFO");
                } else if (event.toString().contains("AboutMenu")) {
                    System.out.println("ABOUT");

                }
            }
        };
    }

    /**
     * This method is used to set Key event handlers. The actions for Key events are
     * defined here.
     */
    private void setKeyEventHander() {
        keyEvent = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                // getSorce will give the control which caused the event
                if (keyCode == KeyCode.LEFT || keyCode == KeyCode.A) {
                    client.send(Packet.Left);
                } else if (keyCode == KeyCode.RIGHT || keyCode == KeyCode.D) {
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
     * This method is used to set mouse event handlers. The actions for mouse events are
     * defined here.
     */
    private void setMouseEventHander() {
        mouseEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Currently this listen to clicks on the items
                // TODO: some how make it work with items
                System.out.println("here" + event.toString());
                if (event.toString().contains("Group")) {
                    gui.changeAvatar();
                } else if (event.toString().contains("Grid")) {
                    // System.out.println(event.getX());
                    int itemX = (int) (event.getX() / 60);
                    int itemY = (int) (event.getY() / 60);
                    gui.setItemDescription(itemX, itemY);
                    // System.out.println(itemX + " " + itemY);
                }
            }
        };
    }

    /**
     * This method will return the action listeners
     * 
     * @return --- the action listeners
     */
    public EventHandler<ActionEvent> getActionEventHandler() {
        return actionEvent;
    }

    /**
     * This method will return the key listeners
     * 
     * @return --- the key listeners
     */
    public EventHandler<KeyEvent> getKeyEventHander() {
        return keyEvent;
    }

    /**
     * This method will return the mouse listener
     * 
     * @return --- the mouse listener
     */
    public EventHandler<MouseEvent> getMouseEventHander() {
        return mouseEvent;
    }

    /**
     * This method will return the window listener
     * 
     * @return --- the window listener
     */
    public EventHandler<WindowEvent> getWindowEventHander() {
        return windowEvent;
    }

    /**
     * Main function.
     * 
     * @param args
     */
    public static void main(String[] args) {
        new ClientUI();
    }
}