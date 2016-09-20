package anotherServer;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import server.game.player.Avatar;
import server.game.player.Position;

/**
 * This is the client side. This class also has a really simple GUI for testing.
 * 
 * @author Rafaela (Just put your Id here)
 *
 */
@SuppressWarnings("serial")
public class ClientMain extends JFrame implements Runnable, KeyListener {

    private final Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private int uid;

    /**
     * This map keeps track of all player's avatars.
     */
    private Map<Integer, Avatar> avatars;

    /**
     * This map keeps track of all player's Positions.
     */
    private Map<Integer, Position> positions;

    /**
     * This is a mirror of the field, Map<Integer, Area> areas, in Game class, except the
     * area is represented as a char[][]
     */
    private Map<Integer, char[][]> areas;

    /**
     * Only for test, we use a text area to display the game here, just like in console.
     */
    private JTextArea window;

    public ClientMain(Socket socket) {
        this.socket = socket;
        areas = new HashMap<>();
        avatars = new HashMap<>();
        positions = new HashMap<>();

        // =========== SWING =============

        this.setLayout(new BorderLayout(3, 3));
        window = new JTextArea(30, 50);
        window.setEditable(false);
        this.add(window, BorderLayout.CENTER);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.validate();
        this.setResizable(true);
        this.setVisible(true);
        this.run();
    }

    @Override
    public void run() {
        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());

            // First, receive from server about the game world
            String incoming = input.readUTF();

            System.out.println("received map string:\n" + incoming);

            while (!incoming.equals("Fin")) {
                ParserUtilities.parseMap(areas, incoming);
                incoming = input.readUTF();
                System.out.println("received map string:\n" + incoming);
            }

            // choose an avatar and type in a name
            // TODO this should be GUI instead of console.
            System.out.println("Please choose your Avatar(1-4):");
            byte avatarIndex = (byte) ParserUtilities.parseInt(1, 4);
            output.writeByte(avatarIndex - 1);

            System.out.println("Please type in your name:");
            String name = ParserUtilities.parseString();
            output.writeUTF(name);
            output.flush();

            /*
             * FIXME so readInt is not blocking and waiting for another int to come in, it
             * throws EOF exception if it meets the end.
             */
            uid = input.readInt();

            System.out.println("Your uId is: " + uid);

            // =========== [DEBUG] ========================
            System.out.println("initialisation done. ready to start game.");
            for (char[][] area : areas.values()) {
                StringBuilder sb = new StringBuilder();
                for (char[] line : area) {
                    sb.append(line);
                    sb.append("\n");
                }

                System.out.println("received map string:\n");
                System.out.println(sb.toString());
            }

            // last, a while true loop to let the client communicate with server.
            boolean connected = true;
            while (connected) {

                // read in broadcast

                // update gui

                // when the client sent an disconnect flag, set connected = false

            }

        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("I/O error. But who cares, disconnected anyway. ");
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent event) {

        // @formatter:off
        @SuppressWarnings("unused")
        String s = "[Help]\n"
                + "Move forward: w\n"
                + "Move backward: s\n"
                + "Move left: a\n"
                + "Move right: d\n"
                + "Turn left: q\n"
                + "Trun right: e\n"
                + "Unlock Lockable: f\n"
                + "Take items in Container: g\n"
                + "Enter/Exit room: r\n"
                + "Open inventory: i\n"
                + "Clock & Time left: c\n"
                + "Use the 1st item in inventory: 1\n"
                + "Use the 2nd item in inventory: 2\n"
                + "Destroy the 1st item in inventory: 0\n";
        // @formatter:on

        int keyCode = event.getKeyCode();
        try {
            switch (keyCode) {
            case KeyEvent.VK_W:
                output.writeByte(Packet.Forward.toByte());
                break;
            case KeyEvent.VK_S:
                output.writeByte(Packet.Backward.toByte());
                break;
            case KeyEvent.VK_A:
                output.writeByte(Packet.Left.toByte());
                break;
            case KeyEvent.VK_D:
                output.writeByte(Packet.Right.toByte());
                break;
            case KeyEvent.VK_Q:
                output.writeByte(Packet.TurnLeft.toByte());
                break;
            case KeyEvent.VK_E:
                output.writeByte(Packet.TurnRight.toByte());
                break;
            case KeyEvent.VK_F:
                output.writeByte(Packet.Unlock.toByte());
                break;
            case KeyEvent.VK_G:
                output.writeByte(Packet.TakeOutItem.toByte());
                break;
            case KeyEvent.VK_R:
                output.writeByte(Packet.Transit.toByte());
                break;
            case KeyEvent.VK_I:
                // TODO this should be implemented. Need more discussion
                break;
            case KeyEvent.VK_C:
                // TODO this should be implemented. Need more discussion
                break;
            case KeyEvent.VK_1:
                // XXX if something is wrong we can try synchronized (this) { }

                output.writeByte(Packet.UseItem.toByte());
                output.writeInt(0);
                byte b_1 = input.readByte();
                Packet acknowledgement_1 = Packet.fromByte(b_1);
                if (acknowledgement_1 == Packet.Success) {
                    /*
                     * TODO rendering part should then stop rendering the first item, i.e.
                     * delete it, and then move other items up.
                     */
                }

                break;
            case KeyEvent.VK_2:
                output.writeByte(Packet.UseItem.toByte());
                output.writeInt(1);
                byte b_2 = input.readByte();
                Packet acknowledgement_2 = Packet.fromByte(b_2);
                if (acknowledgement_2 == Packet.Success) {
                    /*
                     * TODO rendering part should then stop rendering the second item,
                     * i.e. delete it, and then move other items up.
                     */
                }
                break;
            case KeyEvent.VK_0:
                output.writeByte(Packet.DestroyItem.toByte());
                output.writeInt(0);
                byte b_3 = input.readByte();
                Packet acknowledgement_3 = Packet.fromByte(b_3);
                if (acknowledgement_3 == Packet.Success) {
                    /*
                     * TODO rendering part should then stop rendering the first item, i.e.
                     * delete it, and then move other items up.
                     */
                }
                break;

            // TODO Of cause the client should handle more events. Need more.

            default:
            }
        } catch (IOException e) {
            System.err.println("I/O exceptions, " + e.toString());
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {

        System.out.println("Server address?");
        String ip = ParserUtilities.parseString();
        System.out.println("Port number?");
        int port = ParserUtilities.parseInt(0, 99999);
        Socket s = null;
        try {
            s = new Socket(ip, port);
        } catch (IOException e) {
            System.err.println("I/O exceptions, " + e.toString());
            System.exit(1);
        }

        new ClientMain(s);
    }
}
