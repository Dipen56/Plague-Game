package anotherServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import server.game.Game;
import server.game.player.Avatar;
import server.game.player.Player;

/**
 * This class represents a single thread that handles communication with a connected
 * client. It receives events from a client connection via a socket as well as transmit
 * information to the client about the current board state.
 * 
 * @author Rafaela (Just put your Id here)
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
        try {
            DataInputStream input = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            DataOutputStream output = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));

            // First, tell the client about the game world
            // TODO should write a method to convert the map directly into string
            String areaString1 = "0,8,7\nEECRT111\nETRRE111\nEEEEE111\nERRETCDE\nERRETTTE\nEREEEEEE\nCRETTEET";
            String areaString2 = "1,3,3\nEEC\nESB\nEDE";

            output.writeUTF(areaString1);
            output.writeUTF(areaString2);
            output.writeUTF("Fin");
            output.flush();

            // second, join in the player, tell the client its id.
            byte avatarIndex = input.readByte();
            String name = input.readUTF();
            System.out.println("player uid: " + uid + ". avatar " + avatarIndex);
            game.joinPlayer(new Player(uid, Avatar.get(avatarIndex), name));
            System.out.println("initialisation done. joined player: " + uid
                    + " with avatar " + avatarIndex);

            // tell the client his uid
            output.writeInt(uid);
            output.flush();

            // TODO tell the client his virus type (Packet type need to add)

            // ============= [DEBUG] =======================
            System.out.println("Ready to start the game.");
            System.out.println("Server now has players:");
            for (Player p : game.getPlayers().values()) {
                System.out.println("Player: [" + p.getGeographicString() + "]");
            }

            System.out.println("Now entering while(connected) loop");

            // last, a while true loop to let the receptionist communicate with clients.
            boolean connected = true;
            while (connected) {

                if (input.available() != 0) {
                    // read actions from client
                    byte b = input.readByte();
                    Packet packet = Packet.fromByte(b);

                    switch (packet) {
                    case Forward:
                        System.out.println("received forward from: " + uid);
                        if (!game.playerMoveForward(uid)) {
                            System.out.println("Failed to move forward: " + uid);
                        }
                        break;
                    case Backward:
                        System.out.println("received backward from: " + uid);
                        if (!game.playerMoveBackward(uid)) {
                            System.out.println("Failed to move backward: " + uid);
                        }
                        break;
                    case Left:
                        System.out.println("received left from: " + uid);
                        if (!game.playerMoveLeft(uid)) {
                            System.out.println("Failed to move left: " + uid);
                        }
                        break;
                    case Right:
                        System.out.println("received right from: " + uid);
                        if (!game.playerMoveRight(uid)) {
                            System.out.println("Failed to move right: " + uid);
                        }
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
                        connected = false;
                        input.close();
                        output.close();
                        break;
                    default:
                        break;
                    }
                }

                // then broadcast the game status
                if (connected) {
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
