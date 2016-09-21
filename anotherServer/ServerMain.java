package anotherServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.game.Game;
import server.game.GameError;
import server.game.TestConst;

/**
 * This is a server main class I (Hector) copied from Rafaela's code. Some modifications.
 * 
 * @author Rafaela (Just put your Id here)
 *
 */
public class ServerMain {
    /**
     * The period between every update
     */
    public static final int DEFAULT_CLK_PERIOD = 100;
    /**
     * The period between every broadcast
     */
    private static final int DEFAULT_BROADCAST_CLK_PERIOD = 50;
    /**
     * A series of port number, in case the port is used.
     */
    public static final int[] PORT_NUM = { 6000, 6001, 6002, 6003, 6004, 6005 };
    /**
     * The thread used for maintaining the game world
     */
    private ClockThread clockThread;
    /**
     * The server socket waiting for connection.
     */
    private ServerSocket serverSocket;
    /**
     * A thread pool handling all clients
     */
    private ExecutorService clientsPool;
    /**
     * The game
     */
    private Game game;
    /**
     * This map keeps track of every Receptionist for every connected client
     */
    private HashMap<Integer, Receptionist> receptionists;

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
        clockThread = new ClockThread(DEFAULT_CLK_PERIOD, game);
        clientsPool = Executors.newFixedThreadPool(numPlayers);

        runServer(numPlayers);
    }

    /**
     * This method listens to client connections, and when all clients are ready, a
     * multi-player game is started.
     * 
     * @param game
     * @param numPlayers
     * @param clock
     */
    private void runServer(int numPlayers) {

        serverSocket = createServerSocket();
        // display the server address and port.
        System.out.println("Plague server is listening on IP address: "
                + serverSocket.getInetAddress().toString() + ", port: "
                + serverSocket.getLocalPort());

        int count = 0;

        try {
            while (count != numPlayers) {
                // Wait for a connection
                Socket clientSocket = serverSocket.accept();
                int uId = clientSocket.getPort();

                System.out.println("Accepted connection from: "
                        + clientSocket.getInetAddress().toString() + ". uId is: " + uId
                        + ".");

                Receptionist receptionist = new Receptionist(clientSocket, uId,
                        DEFAULT_BROADCAST_CLK_PERIOD, game);
                receptionists.put(uId, receptionist);
                count++;
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }

        System.out.println("All clients accepted, GAME ON!");
        runGame();

        /**
         * XXX runGame() ends so quickly, and "All clients disconnected." was printed
         */
        System.out.println("All clients disconnected.");

        // shut down the thread pool and server socket.
        clientsPool.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    private void runGame() {
        // Now get those clients busy
        for (Receptionist r : receptionists.values()) {
            clientsPool.submit(r);
        }

        // FIXME Start timing should be somewhere else
        game.startTiming();
        clockThread.start();

        // loop forever until game ends.
        while (atleastOneConnection()) {
            Thread.yield();
            System.out.println("Running");
        }
    }

    /**
     * This method creates a server socket. If it is not successfully created, a GameError
     * is thrown.
     * 
     * @return
     */
    private ServerSocket createServerSocket() {

        ServerSocket s = null;
        // try to create a server with port number from pre-defined array.
        for (int i = 0; i < PORT_NUM.length; i++) {
            try {
                // s = new ServerSocket(PORT_NUM[i]);

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
            throw new GameError("Cannot create server socket");
        }
    }

    /**
     * Check whether or not there is at least one connection alive.
     * 
     * @return
     */
    private boolean atleastOneConnection() {
        for (Receptionist r : receptionists.values()) {
            if (r.isAlive()) {
                return true;
            }
        }
        return false;
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
