package game.world;

import game.items.Key;

/**
 * This class represents a room in the game world.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Room {

    /**
     * The keyID specifies which key can open the door to this room. Only the key with the
     * same keyID can open the door to this room.
     */
    private int keyID;

    private boolean isLocked;

    public Room(int keyID, boolean isLocked) {
        this.keyID = keyID;
        this.isLocked = isLocked;
    }

    /**
     * This method let any player to try to unlock this room with a key. If the key
     * matches, this room is unlocked.
     * 
     * @param key
     * @return
     */
    public boolean tryUnlockWithKey(Key key) {
        if (key.getKeyID() == keyID) {
            isLocked = true;
            return true;
        }

        return false;
    }

}
