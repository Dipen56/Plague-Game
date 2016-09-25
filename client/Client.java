package client;

<<<<<<< HEAD
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import server.ParserUtilities;
=======
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import client.view.ClientUI;
import client.view.GUI;
import server.Packet;
import server.ParserUtilities;
import server.ServerMain;
>>>>>>> Hector

/**
 * This is the client side. This class should be integrated with GUI
 * 
 * NOTE: This class is not completed. I moved everything in client into ClientMain. This
 * class is probably to be deleted.
 * 
 * @author Rafaela & Hector
 *
 */
public class Client extends Thread {
<<<<<<< HEAD

    private final Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private int uid;

    /*
     * TODO: In this class or in Rendering class, we should have a Map<Avatar, Position>
     * to keep track of all other player's location. This field is updated in every
     * broadcast.
     */

    /*
     * This is a mirror of the field, areas, in Game class, except the area is replaced
     * with a char[][]
     */
    private Map<Integer, char[][]> areas;

    /*
     * TODO; It may be a good idea to integrate the Client class with the Rendering class.
     * If not, the Client class should have access to Rendering class, so he can tell
     * Rendering what to render.
     */

    public Client(Socket socket) {
        this.socket = socket;
        areas = new HashMap<>();
    }
    public Client(String ip, int port, String userName, int avatarID) {
		// TODO: still need to add avatar in and also make use of the username
		// System.out.println("Server address?");
    	areas = new HashMap<>();
		String tempIp = ParserUtilities.parseString();
		// System.out.println("Port number?");
		int tempPort = ParserUtilities.parseInt(0, 99999);
		Socket s = null;
		try {
			s = new Socket(ip, port);
		} catch (IOException e) {
			System.err.println("Failed to connect to server, I/O exceptions, " + e.toString());
			System.exit(1);
		}
		this.socket = s;
		run();
	}

    @Override
    public void run() {
        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());

            // First, receive from server about the game world

            String incoming = input.readUTF();

            while (!incoming.equals("Map done")) {
                ParserUtilities.parseMap(areas, incoming);
                incoming = input.readUTF();
            }

            // second, receive from the server about the client id, stuff, initialise the
            // player.

            // TODO choose an avatar and type in a name, this should be GUI instead of
            // console.
            System.out.println("Please choose your Avatar(1-4):");
            byte avatarIndex = (byte) ParserUtilities.parseInt(1, 4);
            output.writeByte(avatarIndex - 1);

            System.out.println("Please type in your name:");
            String name = ParserUtilities.parseString();
            output.writeUTF(name);
            output.flush();

            uid = input.readInt();
            System.out.println("Your uId is: " + uid);

            // last, a while true loop to let the client communicate with server.
            boolean connected = true;
            while (connected) {

                // read in broadcast

                // update gui

                // when the client sent an disconnect flag, set connected = false

            }

        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("I/O error. But who cares, disconnected anyway. ");
            }
        }
    }
=======

    /**
     * 
     */
    private ClientUI controller;

    private final Socket socket;
    private DataOutputStream output;
    private DataInputStream input;

    private boolean isUserReady;

    private boolean isGameRunning;

    /*
     * TODO: In this class or in Rendering class, we should have a Map<Avatar, Position>
     * to keep track of all other player's location. This field is updated in every
     * broadcast.
     */

    /*
     * TODO; It may be a good idea to integrate the Client class with the Rendering class.
     * If not, the Client class should have access to Rendering class, so he can tell
     * Rendering what to render.
     */

    public Client(Socket socket, ClientUI controller) {
        this.socket = socket;
        this.controller = controller;
        isUserReady = false;
        isGameRunning = false;
    }

    public void setUserReady(boolean isUserReady) {
        this.isUserReady = isUserReady;
    }

    @Override
    public void run() {
        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            // First, receive from server about the maps
            String incoming = input.readUTF();

            System.out.println("received map string:\n" + incoming);

            /*
             * TODO "Fin" should be coded in Packet as a properly defined packet type.
             */

            while (!incoming.equals("Fin")) {
                controller.parseMap(incoming);
                incoming = input.readUTF();
                System.out.println("received map string:\n" + incoming);
            }

            // get the uid from server.
            int uid = input.readInt();
            controller.parseUID(uid);
            System.out.println("Your uId is: " + uid);

            // tell the server the avatar index and user name
            output.writeByte(controller.getAvatarIndex());
            output.flush();
            output.writeUTF(controller.getUserName());
            output.flush();

            // =========== [DEBUG] ========================

            // System.out.println("initialisation done. ready to start game.");
            // for (char[][] area : areas.values()) {
            // StringBuilder sb = new StringBuilder();
            // for (char[] line : area) {
            // sb.append(line);
            // sb.append("\n");
            // }
            //
            // System.out.println("received map string:\n");
            // System.out.println(sb.toString());
            // }

            // don't start the game until server tell us to start.
            // (when all clients are ready).
            while (true) {
                if (isUserReady) {
                    output.writeByte(Packet.Ready.toByte());
                    output.flush();
                    break;
                }

                try {
                    Thread.sleep(ServerMain.DEFAULT_BROADCAST_CLK_PERIOD);
                } catch (InterruptedException e) {
                    // Should never happen
                }
            }

            System.out.println("now wait for the server to tell us ready");

            // wait until the server tells us ready to begin
            while (true) {
                if (input.available() != 0) {

                    Packet packet = Packet.fromByte(input.readByte());
                    if (packet == Packet.Ready) {
                        isGameRunning = true;
                    }
                }

                if (isGameRunning && isUserReady) {
                    System.out.println("two flags are all ready");
                    break;
                }

                try {
                    Thread.sleep(ServerMain.DEFAULT_BROADCAST_CLK_PERIOD);
                } catch (InterruptedException e) {
                    // Should never happen
                }
            }

            controller.startGame();

            System.out.println("Now entering while(connected) loop");

            // last, a while true loop to let the client communicate with
            // server.
            while (isGameRunning) {

                // read in broadcast

                // 1. all players' position.
                incoming = input.readUTF();
                while (!incoming.equals("Fin")) {
                    controller.parsePosition(incoming);
                    incoming = input.readUTF();
                }

                // 2. the world time
                incoming = input.readUTF();
                controller.parseTime(incoming);

                // 3. the player's health
                controller.parseHealth(input.readInt());

                // 4. the player's visibility
                controller.parseVisibility(input.readInt());

                // 5. the player's inventory
                // XXX This should not be a list of Item.
                incoming = input.readUTF();
                controller.parseInventory(incoming);

                /*
                 * ====================================================
                 * 
                 * This is where we update the Renderer. Or, alternatively, like Pacman,
                 * we could update Renderer in ClockThread
                 * 
                 * ====================================================
                 */

                // TODO: update GUI, or, not, we can update it as the ClockThread

            }

        } catch (IOException e) {
            /*
             * TODO should handle all exceptions on GUI, i.e. catch clause should popup a
             * dialog on GUI.
             */
            GUI.showWarningPane("I/O Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                // TODO when the client sent an disconnect flag, set connected =
                // false
                socket.close();
            } catch (IOException e) {
                GUI.showWarningPane("I/O error. But who cares, disconnected anyway.");
            }
        }
    }

    /**
     * This method will send a packet to server.
     * 
     * @param packet
     */
    public void send(Packet packet) {
        try {
            output.writeByte(packet.toByte());
            output.flush();
        } catch (IOException e) {
            GUI.showWarningPane("I/O exceptions, " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * This method will send a packet to server, with an integer followed. This integer is
     * usually used as a index for special commands.
     * 
     * @param packet
     * @param i
     */
    public void sendWithIndex(Packet packet, int i) {
        try {
            output.writeByte(packet.toByte());
            output.writeInt(i);
            output.flush();
        } catch (IOException e) {
            GUI.showWarningPane("I/O exceptions, " + e.toString());
            e.printStackTrace();
        }
    }

>>>>>>> Hector
}
