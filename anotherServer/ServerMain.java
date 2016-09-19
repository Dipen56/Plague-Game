package anotherServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.game.Game;
import server.game.GameError;
import server.game.TestConst;

/**
 * This is a server main class I (Hector) copied from Rafaela's code. Some modifications.
 * 
 * @author Hector
 *
 */
public class ServerMain {
    /**
     * The period between every update
     */
    public static final int DEFAULT_CLK_PERIOD = 20;
    /**
     * The period between every broadcast
     */
    private static final int DEFAULT_BROADCAST_CLK_PERIOD = 5;
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
    private ExecutorService pool;
    /**
     * The game
     */
    private Game game;
    /**
     * This map keeps track of every Receptionist for every connected client
     */
    private HashMap<Integer, Receptionist> receptionists;

    /**
     * System.in wrapped in scanner to get user input.
     */
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Constructor
     */
    public ServerMain() {
        // how many players?
        System.out.println("How many players (between 2 and 4):");
        int numPlayers = parseInt(2, 4);

        // Parsing a file to construct the world
        // game = new Game(file);

        // create the game world
        game = new Game(TestConst.world, TestConst.areas);
        clockThread = new ClockThread(DEFAULT_CLK_PERIOD, game);
        pool = Executors.newFixedThreadPool(numPlayers);

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
        System.out.println("All clients disconnected.");
    }

    private void runGame() {
        // Now get those clients busy
        for (Receptionist r : receptionists.values()) {
            pool.submit(r);
        }

        game.startTiming();
        clockThread.start();

        // loop forever until game ends.
        while (atleastOneConnection()) {
            Thread.yield();
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
                s = new ServerSocket(PORT_NUM[i]);
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
     * This helper method parse user's input as integer, and limits the maximum and
     * minimum boundary of it.
     * 
     * @param min
     *            --- the minimum boundary of input as an integer
     * @param max
     *            --- the maximum boundary of input as an integer
     * @return --- the parsed integer
     */
    private int parseInt(int min, int max) {
        while (true) {
            String line = SCANNER.nextLine();

            try {
                // parse the input
                int i = Integer.valueOf(line);
                if (i >= min && i <= max) {
                    // a good input
                    return i;
                } else {
                    // a out of boundary input, let the user retry.
                    System.out.println(
                            "Please choose between " + min + " and " + max + ":");
                    continue;
                }
            } catch (NumberFormatException e) {
                // the input is not an integer
                System.out.println("Please enter an integer:");
                continue;
            }
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
