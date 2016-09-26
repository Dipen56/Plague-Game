package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import client.ParserUtilities;
import server.game.Game;
import server.game.GameError;
import server.game.TestConst;
import server.view.ServerGui;

/**
 * This class is where the server is started.
 * 
 * @author Rafaela & Hector
 *
 */
public class ServerMain {
    /**
     * The period between every broadcast
     */
    public static final int DEFAULT_BROADCAST_CLK_PERIOD = 50;
    /**
     * A series of port number, in case the port is used.
     */
    public static final int[] PORT_NUM = { 6000, 6001, 6002, 6003, 6004, 6005 };
    /**
     * The server socket waiting for connection.
     */
    private ServerSocket serverSocket;
    /**
     * The game
     */
    private Game game;
    /**
     * This map keeps track of every Receptionist for every connected client. The key is
     * the unique id of each client
     */
    private HashMap<Integer, Receptionist> receptionists;

    /**
     * this is the server GUI which is used to display the ip an port of the server
     */

    /**
     * Constructor
     */
    public ServerMain() {
        receptionists = new HashMap<>();

        // how many players?
        System.out.println("How many players (between 2 and 4):");
        int numPlayers = ParserUtilities.parseInt(2, 4);

        // Parsing a file to construct the world
        // game = new Game(file);

        // create the game world
        game = new Game(TestConst.world, TestConst.areas);

        runServer(numPlayers);
    }

    /**
     * This method listens to client connections, and when all clients are ready, a
     * multi-player game is started.
     * 
     * @param numPlayers
     *            --- the number of players
     */
    private void runServer(int numPlayers) {

        serverSocket = createServerSocket();
        // display the server address and port.
        System.out.println("Plague server is listening on IP address: "
                + serverSocket.getInetAddress().toString() + ", port: "
                + serverSocket.getLocalPort());

        /*
         * TODO integrate the server into GUI
         */
        new Thread() {
            public void run() {
                ServerGui.port = serverSocket.getLocalPort();
                ServerGui.ip = serverSocket.getInetAddress().toString();
                ServerGui.launch(ServerGui.class);
            }
        }.start();

        int count = 0;
        try {
            // Wait for a connection
            while (count != numPlayers) {
                Socket clientSocket = serverSocket.accept();
                int uId = clientSocket.getPort();

                System.out.println("Accepted connection from: "
                        + clientSocket.getInetAddress().toString() + ". uId is: " + uId
                        + ".");

                Receptionist receptionist = new Receptionist(clientSocket, uId, game);
                // send the map to client
                receptionist.sendMapID();
                receptionists.put(uId, receptionist);
                count++;
            }

            System.out.println(
                    "All clients accepted, now entering lobby, wait till all players are ready");

            // start the initialisation process (enter the lobby)
            runGame();

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } finally {
            /*
             * XXX maybe need more clean up actions, close server, etc.
             */
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("I/O error: " + e.getMessage());
            }
        }
    }

    /**
     * Start the multi-player game.
     */
    private void runGame() {

        // join the player in, get the player's user name, avatar and id.
        for (Receptionist r : receptionists.values()) {
            r.receiveNameAvatar();
        }

        // broadcast each client's virus type
        for (Receptionist r : receptionists.values()) {
            r.sendVirusType();
        }

        // broadcast every player's avatar
        for (Receptionist r : receptionists.values()) {
            r.sendAvatars();
        }

        // Now get those workers busy
        for (Receptionist r : receptionists.values()) {
            r.start();
        }

        // ======= Enter the lobby, waiting for everyone ready ========

        // do not start timing until everybody is ready.
        outer: while (true) {
            for (Receptionist r : receptionists.values()) {
                if (!r.isReady()) {
                    try {
                        Thread.sleep(DEFAULT_BROADCAST_CLK_PERIOD);
                    } catch (InterruptedException e) {
                        // Should never happen
                    }
                    continue outer;
                }
            }
            break;
        }

        // ====== everybody is ready, now enter the game ========
        for (Receptionist r : receptionists.values()) {
            r.setGameRunning();
        }

        System.out.println("Finished waiting for everybody ready. Game start");

        game.startTiming();
    }

    /**
     * This method creates a server socket. If it is not successfully created, a GameError
     * is thrown.
     * 
     * @return --- the server socket.
     */
    private ServerSocket createServerSocket() {

        ServerSocket s = null;
        // try to create a server with port number from pre-defined array.
        for (int i = 0; i < PORT_NUM.length; i++) {
            try {
                s = new ServerSocket(PORT_NUM[i], 50, InetAddress.getLocalHost());
                break;
            } catch (IOException e) {
                continue;
            }
        }

        // check if it is created successfully
        if (s != null) {
            return s;
        } else {
            throw new GameError(
                    "Cannot create server socket, all predefined ports are used");
        }
    }

    /**
     * Main function
     * 
     * @param args
     */
    public static void main(String args[]) {
        new ServerMain();
    }

}
