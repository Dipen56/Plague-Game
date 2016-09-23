package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import server.ParserUtilities;

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
}
