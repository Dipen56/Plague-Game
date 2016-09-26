package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import server.game.Game;
import server.game.player.Avatar;
import server.game.player.Player;

/**
 * This class represents a single thread that handles communication with a connected
 * client. It receives events from a client connection via a socket as well as transmit
 * information to the client about the current board state.
 * 
 * @author Rafaela & Hector
 *
 */
public class Receptionist extends Thread {
    /**
     * The game instance running on server side.
     */
    private final Game game;
    /**
     * The duration between every two broadcast.
     */
    private final int broadcastClock;
    /**
     * User (client) id of this connection.
     */
    private final int uid;
    /**
     * The communicating socket.
     */
    private final Socket socket;

    private DataInputStream input;
    private DataOutputStream output;

    /**
     * A flag indicating whether this client is ready to enter game. If it's false, the
     * server will keep this client in lobby waiting for all clients ready.
     */
    private boolean isClientReady = false;
    /**
     * A flag indicating whether the game is running or not.
     */
    private boolean isGameRunning = false;

    /**
     * Constructor
     * 
     * @param socket
     * @param uid
     * @param broadcastClock
     * @param game
     */
    public Receptionist(Socket socket, int uid, int broadcastClock, Game game) {
        this.game = game;
        this.broadcastClock = broadcastClock;
        this.socket = socket;
        this.uid = uid;

        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Lost connection with client." + e.toString());
            e.printStackTrace();
        }
    }

    public void sendMapAndID() {
        /*
         * TODO It's sending the mock game world currently. should write a method to
         * convert the map directly into string
         */
        String areaString1 = "0,8,7\nEECRT111\nETRRE111\nEEEEE111\nERRETCDE\nERRETTTE\nEREEEEEE\nCRETTEET";
        String areaString2 = "1,3,3\nEEC\nESB\nEDE";

        try {
            // maps
            output.writeUTF(areaString1);
            output.writeUTF(areaString2);
            output.writeUTF("Fin");
            output.flush();

            // tell the client his uid
            output.writeInt(uid);
            output.flush();
        } catch (IOException e) {
            System.err.println("Lost connection with client." + e.toString());
            e.printStackTrace();
        }
    }

    public void receiveNameAvatar() {
        try {
            // join the player in, tell the client his uid.
            byte avatarIndex = input.readByte();
            String name = input.readUTF();
            System.out.println("player uid: " + uid + ". avatar " + avatarIndex);
            game.joinPlayer(new Player(uid, Avatar.get(avatarIndex), name));
            System.out.println("initialisation done. joined player: " + uid
                    + " with avatar " + avatarIndex);

            // TODO tell the client his virus type (Packet type need to add)

            // ============= [DEBUG] =======================
            System.out.println("Ready to start the game.");
            System.out.println("Server now has players:");
            for (Player p : game.getPlayers().values()) {
                System.out.println("Player: [" + p.getGeographicString() + "]");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Is this client ready to enter game?
     * 
     * @return
     */
    public boolean isReady() {
        return isClientReady;
    }

    /**
     * This method usually is called by server to tell the client ready to enter game.
     */
    public void setGameRunning() {
        this.isGameRunning = true;
    }

    @Override
    public void run() {
        try {

            // wait until the user is ready
            while (true) {
                if (input.available() > 0) {
                    Packet packet = Packet.fromByte(input.readByte());
                    if (packet == Packet.Ready) {
                        isClientReady = true;
                        break;
                    }
                }

                try {
                    Thread.sleep(ServerMain.DEFAULT_BROADCAST_CLK_PERIOD);
                } catch (InterruptedException e) {
                    // Should never happen
                }
            }

            // don't start the game until server tell us to start.
            // (when all clients are ready).
            while (true) {
                if (isGameRunning) {
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

            // last, a while true loop to let the receptionist communicate with clients.
            System.out.println("Now entering while(isGameRunning) loop");
            while (isGameRunning) {

                if (input.available() != 0) {
                    // read input from client
                    byte b = input.readByte();
                    Packet packet = Packet.fromByte(b);

                    // what did the client want?
                    switch (packet) {
                    case Forward:
                        game.playerMoveForward(uid);
                        break;
                    case Backward:
                        game.playerMoveBackward(uid);
                        break;
                    case Left:
                        game.playerMoveLeft(uid);
                        break;
                    case Right:
                        game.playerMoveRight(uid);
                        break;
                    case TurnLeft:
                        game.playerTurnLeft(uid);
                        break;
                    case TurnRight:
                        game.playerTurnRight(uid);
                        break;
                    case Transit:
                        game.playerTransit(uid);
                        break;
                    case UseItem:
                        int index_1 = input.readInt();
                        game.playerUseItem(uid, index_1);
                        break;
                    case DestroyItem:
                        int index_2 = input.readInt();
                        game.playerDestroyItem(uid, index_2);
                        break;
                    case TakeOutItem:
                        game.playerTakeItemsFromContainer(uid);
                        break;
                    case Unlock:
                        game.playerUnlockLockable(uid);
                        break;
                    case Success:
                        // shouldn't come in here.
                        break;
                    case Failure:
                        // shouldn't come in here.
                        break;
                    case Disconnect:
                        // handle disconnection. or we can handle it in catch clause.
                        isGameRunning = false;
                        input.close();
                        output.close();
                        break;
                    default:
                        break;
                    }
                }

                // then broadcast the game status
                if (isGameRunning) {
                    // 1, tell the client all players' position.
                    for (Player p : game.getPlayers().values()) {
                        String s = p.getGeographicString();
                        output.writeUTF(s);
                    }
                    output.writeUTF("Fin");
                    output.flush();

                    // 2, tell the client the world time
                    String time = game.getClockString();
                    output.writeUTF(time);
                    output.flush();

                    // 3, tell the client the player's health
                    output.writeInt(game.getPlayerHealth(uid));
                    output.flush();

                    // 4, tell the client the player's visibility
                    output.writeInt(game.getPlayerVisibility(uid));
                    output.flush();

                    // 5, tell the client the player's inventory
                    output.writeUTF(game.getPlayerInventoryString(uid));
                    output.flush();

                    // 6, have a good nap.
                    Thread.sleep(broadcastClock);
                }
            }
        } catch (IOException e) {
            System.err.println("Player " + uid + " disconnected.");
        } catch (InterruptedException e) {
            System.err.println("Thread sleep interrupted. No big deal.");
        } finally {
            game.disconnectPlayer(uid);
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(
                        "I/O error. But who cares, Clients disconnected anyway. ");
            }
        }
    }
}