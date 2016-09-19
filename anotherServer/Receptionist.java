package anotherServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

import server.game.Game;

/**
 * This class represents a single thread that handles communication with a connected
 * client. It receives events from a client connection via a socket as well as transmit
 * information to the client about the current board state.
 * 
 * @author Hector
 *
 */
public class Receptionist extends Thread {

    private final Game game;
    private final int broadcastClock;
    private final int uid;
    private final Socket socket;

    public Receptionist(Socket client, int uid, int broadcastClock, Game game) {
        this.game = game;
        this.broadcastClock = broadcastClock;
        this.socket = client;
        this.uid = uid;
    }

    @Override
    public void run() {
        /*
         * TODO this is where the receptionist communicate with client socket. What it
         * should do is same as Master class in Pacman
         */
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            
            // First, tell the client about the game world
            
            
            // second, join in the player, tell the client its id.
            
            
            
            // last, a while true loop to let the receptionist communicate with clients.
            
            
            
            socket.close();
        } catch (IOException e) {
            System.err.println("Player " + uid + " disconnected.");
            game.disconnectPlayer(uid);
        }
    }

}
