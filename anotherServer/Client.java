package anotherServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {

    private final Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private int uid;

    public Client(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());

            // First, receive from server about the game world

            // receive from the server about the client id, stuff, initialise the player.

            
            // last, a while true loop to let the client communicate with server.

            
            
            socket.close();
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
