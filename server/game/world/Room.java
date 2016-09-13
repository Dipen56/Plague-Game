package server.game.world;

/**
 * This class represents a room.
 * 
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Room extends Area {

    /**
     * The keyID specifies which key can open the door to this room. Only the key with the
     * same keyID can open the door to this room.
     */
    private int keyID;

    private boolean isLocked;

    private RoomExit exit;

    /**
     * Constructor
     * 
     * @param filename
     * @param roomID
     * @param keyID
     * @param isLocked
     */
    public Room(String filename, int keyID, boolean isLocked) {
        super(filename);
        this.keyID = keyID;
        this.isLocked = isLocked;

        // remember the exit
        rememberEixt();
    }

    /**
     * Constructor used in test. Probably will be discarded.
     * 
     * @param width
     * @param height
     * @param board
     */
    public Room(Position[][] board, int keyID, boolean isLocked) {
        super(board);
        this.keyID = keyID;
        this.isLocked = isLocked;

        // remember the exit
        rememberEixt();
    }

    /**
     * let the room remember where the exit is.
     */
    public void rememberEixt() {
        for (Position[] row : board) {
            for (Position col : row) {
                if (col instanceof RoomExit) {
                    this.exit = (RoomExit) col;
                    return;
                }
            }
        }
    }

    public int getKeyID() {
        return keyID;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean boo) {
        isLocked = boo;
    }

    public RoomExit getExit() {
        return exit;
    }

}
