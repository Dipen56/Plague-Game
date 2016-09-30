
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.Map;
import java.util.Scanner;

import client.view.ClientUI;
import client.view.GUI;
import server.Packet;
import server.ServerMain;

/**
 * This class represents a single thread that handles communication with a connected
 * server. It receives events from the server connection via a socket as well as send
 * actions to the server about the player's command.
 * 
 * @author Rafaela & Hector
 *
 */
public class Client extends Thread {

    /**
     * The period between every broadcast
     */
    public static final int DEFAULT_UPDATE_CLK_PERIOD = 20;

    /**
     * The pointer to the controller so we can let controller update renderer and GUI.
     */
    private ClientUI controller;

    /**
     * The socket connection with server
     */
    private final Socket socket;

    /**
     * The upload link to the server
     */
    private DataOutputStream output;

    /**
     * The download link from the server
     */
    private DataInputStream input;

    /**
     * This flag is used to indicate whether the user is ready to enter the game.
     */
    private boolean isUserReady;

    /**
     * This flag is used to indicate whether the game is running
     */
    private boolean isGameRunning;

    /**
     * Constructor. It also initialise the socket input and output.
     * 
     * @param socket
     *            --- the socket connecting the server
     * @param controller
     *            --- the controller
     */
    public Client(Socket socket, ClientUI controller) {
        this.socket = socket;
        this.controller = controller;
        isUserReady = false;
        isGameRunning = false;

        // initialise the socket input and output
        try {
            output = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            GUI.showWarningPane("I/O exceptions, " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * This method will send a packet to server.
     * 
     * @param packet
     *            --- the packet need to send
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
     *            --- the packet need to send
     * @param i
     *            --- the index
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

    /**
     * Set ready to enter the game.
     * 
     * @param isUserReady
     */
    public void setUserReady(boolean isUserReady) {
        this.isUserReady = isUserReady;
    }

    @Override
    public void run() {
        try {
            // 1. receive from server about the maps
            String incoming = input.readUTF();
            while (!incoming.equals("Fin")) {
                controller.parseMap(incoming);
                System.out.println("received map string:\n" + incoming);
                incoming = input.readUTF();
            }

            // 2. get the uId from server.
            int uid = input.readInt();
            controller.parseUID(uid);

            // 3. tell the server the avatar index and user name
            output.writeByte(controller.getAvatar().ordinal());
            output.flush();
            output.writeUTF(controller.getUserName());
            output.flush();

            // 4. get the virus type from server.
            int virusIndex = input.readInt();
            controller.parseVirus(virusIndex);

            // 5. get everybody's avatar
            String avatars = input.readUTF();
            controller.parseAvatars(avatars);

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

            // now start rendering the game interface
            controller.startGame();

            // last, let the client constantly get updated from server.
            while (isGameRunning) {
                if (input.available() > 0) {
                    incoming = input.readUTF();
                    stringToGame(incoming);

                    // ======= [TESTING] ==========
                    // printTestString(incoming);
                }

                try {
                    Thread.sleep(DEFAULT_UPDATE_CLK_PERIOD);
                } catch (InterruptedException e) {
                    // Should never happen
                }
            }
        } catch (IOException e) {
            GUI.showWarningPane("I/O Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                // TODO cleaning up process.

                // send server a disconnect packet

                // close input and output.

                socket.close();
            } catch (IOException e) {
                System.err.println("I/O error. But who cares, disconnected anyway.");
            }
        }
    }

    /**
     * This method generates a String representation of the game status. The format of it
     * is:
     * 
     * <p>
     * <li>Time
     * <li>Health
     * <li>Visibility
     * <li>Positions of all players
     * <li>The inventory of the player in this client
     * <li>The status of player holding torch or not.
     * 
     * <p>
     * Each one of them is separated by a new line character '\n'. The format of each part
     * should refer to {@link client.ParserUtilities #parseTime(String) parseTime},
     * {@link client.ParserUtilities #parsePosition(java.util.Map, String) parsePosition},
     * {@link client.ParserUtilities #parseInventory(String) parseInventory}, and
     * {@link client.ParserUtilities #parseTorchStatus(Map, String) parseTorchStatus}..
     * 
     * @return --- a String representation of the game status
     */
    private void stringToGame(String gameStr) {
        Scanner s = new Scanner(gameStr);
        String line;

        // 1. time
        if (s.hasNextLine()) {
            line = s.nextLine();
            controller.parseTime(line);
        } else {
            System.out.println("Data incomplete, no time received.");
            // Data is incomplete, ignore.
            s.close();
            return;
        }

        // 2. health
        if (s.hasNextLine()) {
            line = s.nextLine();
            try {
                controller.parseHealth(Integer.valueOf(line));
            } catch (NumberFormatException e) {
                // Data is incorrect, ignore.
                System.out.println("Data incorrect, health is not integer.");
                s.close();
                return;
            }
        } else {
            System.out.println("Data incomplete, no health received.");
            // Data is incomplete, ignore.
            s.close();
            return;
        }

        // 3. visibility
        if (s.hasNextLine()) {
            line = s.nextLine();
            try {
                controller.parseVisibility(Integer.valueOf(line));
            } catch (NumberFormatException e) {
                // Data is incorrect, ignore.
                System.out.println("Data incorrect, visibility is not integer.");
                s.close();
                return;
            }
        } else {
            System.out.println("Data incomplete, no visibility received.");
            // Data is incomplete, ignore.
            s.close();
            return;
        }

        // 4. all players' positions
        if (s.hasNextLine()) {
            line = s.nextLine();
            controller.parsePosition(line);
        } else {
            System.out.println("Data incomplete, no player's positions received.");
            // Data is incomplete, ignore.
            s.close();
            return;
        }

        // 5. inventory String
        if (s.hasNextLine()) {
            line = s.nextLine();
            controller.parseInventory(line);
        } else {
            System.out.println("Data incomplete, no inventory received.");
            // Data is incomplete, ignore.
            s.close();
            return;
        }

        // 6. holding torch or not for all players.
        if (s.hasNextLine()) {
            line = s.nextLine();
            controller.parseTorchStatus(line);
        } else {
            System.out.println("Data incomplete, no torch status received.");
            // Data is incomplete, ignore.
            s.close();
            return;
        }

        // close the scanner
        s.close();
    }

    /**
     * A helper method for testing
     * 
     * @param incoming
     */
    private void printTestString(String incoming) {
        Scanner sc = new Scanner(incoming);

        System.out.println("Time: " + sc.nextLine());
        System.out.println("Health: " + sc.nextLine());
        System.out.println("Visibility: " + sc.nextLine());
        System.out.println("Players: " + sc.nextLine());

        if (sc.hasNextLine()) {
            System.out.println("Inventory: " + sc.nextLine());
        }

        System.out.println("=====================================");

        sc.close();
    }

}
