package anotherServer;

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
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

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
            output.writeInt(uid);
            output.flush();

            // ============= [DEBUG] =======================
            System.out.println("initialisation done. joined player: " + uid
                    + " with avatar " + avatarIndex);
            System.out.println("Ready to start the game.");
            System.out.println("Server now has players:");
            for (Player p : game.getPlayers().values()) {
                System.out.println("Player: [" + p.getPositionString() + "]");
            }

            // last, a while true loop to let the receptionist communicate with clients.
            boolean connected = true;
            while (connected) {

                if (input.available() != 0) {
                    // read actions from client
                    byte b = input.readByte();
                    Packet packet = Packet.fromByte(b);

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
                        boolean result_1 = game.playerUseItem(uid, index_1);
                        byte by_1 = result_1 ? Packet.Success.toByte()
                                : Packet.Failure.toByte();
                        output.writeByte(by_1);
                        output.flush();
                        break;
                    case DestroyItem:
                        int index_2 = input.readInt();
                        boolean result_2 = game.playerDestroyItem(uid, index_2);
                        byte by_2 = result_2 ? Packet.Success.toByte()
                                : Packet.Failure.toByte();
                        output.writeByte(by_2);
                        output.flush();
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
                    // first, tell the client all players' position.
                    for (Player p : game.getPlayers().values()) {
                        String s = p.getPositionString();
                        output.writeUTF(s);
                    }

                    output.writeUTF("Fin");
                    output.flush();

                    // second, tell the client the world time

                    // third, tell the client the player's health

                    // last, tell the client all

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
