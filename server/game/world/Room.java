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

    private TransitionSpace exit;

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
        rememberExit();
    }

    /**
     * Constructor used in test. Probably will be discarded.
     *
     * @param The room board.
     * @param The room key ID.
     * @param True if the room is locked.
     */
    public Room(MapElement[][] board, int keyID, boolean isLocked) {
        super(board);
        this.keyID = keyID;
        this.isLocked = isLocked;

        // remember the exit
        rememberExit();
    }

    /** Used after XML unmarshalling.
     * @param The room board.
     * @param The room key ID
     * @param True if the room is locked.
     * @param The room exit.
     */
    public Room(MapElement[][] board, int keyID, boolean isLocked, TransitionSpace exit) {
        super(board);
        this.keyID = keyID;
        this.isLocked = isLocked;
        this.exit = exit;

    }

    /**
     * let the room remember where the exit is.
     */
    public void rememberExit() {
        for (MapElement[] row : board) {
            for (MapElement col : row) {
                if (col instanceof TransitionSpace) {
                    this.exit = (TransitionSpace) col;
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

    public TransitionSpace getExit() {
        return exit;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exit == null) ? 0 : exit.hashCode());
		result = prime * result + (isLocked ? 1231 : 1237);
		result = prime * result + keyID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		if (exit == null) {
			if (other.exit != null)
				return false;
		} else if (!exit.equals(other.exit))
			return false;
		if (isLocked != other.isLocked)
			return false;
		if (keyID != other.keyID)
			return false;
		if (!super.equals(obj))
			return false;
		return true;
	}
    
    

}
