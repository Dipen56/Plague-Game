package server.game.world;

import java.util.List;

/**
 * This class represents a room.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Room extends Area implements Lockable {

    /**
     * The keyID specifies which key can open the door to this room. Only the key with the
     * same keyID can open the door to this room.
     */
    private int keyID;

    /**
     * Is this room locked? If so the player needs a right key to open this room.
     */
    private boolean isLocked;

    /**
     * A string describing this area.
     */
    private String description;

    /**
     * Constructor used to parse in a file. NOT used currently.
     *
     * @param filename
     *            --- the XML file recording details of this room
     * @param keyID
     *            --- the key id
     * @param isLocked
     *            -- whether it is locked.
     */
    public Room(String filename, int keyID, boolean isLocked) {
        super(filename);
        this.keyID = keyID;
        this.isLocked = isLocked;
    }

    /**
     * Constructor used during game creation.
     *
     * @param board
     *            --- the board represented as 2d-array of MapElement
     * @param areaID
     *            --- area id
     * @param keyID
     *            --- the key id
     * @param isLocked
     *            -- whether it is locked.
     * @param description
     * 			  --- A description of this area
     */
    public Room(MapElement[][] board, int areaID, int keyID, boolean isLocked, String description) {
        super(board, areaID, description);
        this.keyID = keyID;
        this.isLocked = isLocked;
        this.description = description;
    }

    /**
     * Constructor used in game load.
     *
     * @param board
     *            --- the board represented as 2d-array of MapElement
     * @param areaID
     *            --- area id
     * @param keyID
     *            --- the key id
     * @param isLocked
     *            -- whether it is locked.
     * @param playerPortals
     *            --- A list of possible player spawn locations.
     * @param description
     * 			  --- A description of this area
     */
    public Room(MapElement[][] board, int areaID, int keyID, boolean isLocked,
            List<int[]> playerPortals, String description) {
        super(board, areaID, playerPortals, description);
        this.keyID = keyID;
        this.isLocked = isLocked;
    }

    @Override
    public int getKeyID() {
        return keyID;
    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public void setLocked(boolean boo) {
        isLocked = boo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (isLocked ? 1231 : 1237);
        result = prime * result + keyID;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Room other = (Room) obj;
        if (isLocked != other.isLocked)
            return false;
        if (keyID != other.keyID)
            return false;
        return true;
    }

}
