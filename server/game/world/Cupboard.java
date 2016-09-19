package server.game.world;

import java.util.List;

import server.game.GameError;
import server.game.items.Item;

/**
 * This class represents a cupboard (normally in rooms). A cupboard can contain loot, and
 * it's lockable.
 * 
 * @author Hector
 *
 */
public class Cupboard extends Obstacle implements Container, Lockable {

    /**
     * The keyID specifies which key can open the door to this room. Only the key with the
     * same keyID can open the door to this room.
     */
    private int keyID;
    private boolean isLocked;

    private List<Item> loot;

    public Cupboard(String description, int keyID, boolean isLocked, List<Item> loot) {
        super(description);

        this.keyID = keyID;
        this.isLocked = isLocked;

        if (loot.size() > Container.OTHER_SIZE) {
            throw new GameError(
                    "Chest can only contain " + Container.OTHER_SIZE + " items.");
        }

        this.loot = loot;
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
    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    @Override
    public List<Item> getLoot() {
        return loot;
    }

    @Override
    public boolean putItemIn(Item item) {
        if (loot.size() >= Container.OTHER_SIZE) {
            return false;
        }

        return loot.add(item);
    }

}
