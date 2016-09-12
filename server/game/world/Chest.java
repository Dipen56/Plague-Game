package server.game.world;

import java.awt.image.BufferedImage;
import java.util.List;

import server.game.items.Item;

/**
 * This class represents a chest. A chest can be locked or unlocked. If it is locked, a
 * key is required to unlock it. Each chest contains at least one loot.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Chest extends Obstacle {
    /**
     * The keyID specifies which key can open the door to this room. Only the key with the
     * same keyID can open the door to this room.
     */
    private int keyID;
    private boolean isLocked;

    private List<Item> loot;

    public Chest(int x, int y, String description, BufferedImage image, int keyID,
            boolean isLocked, List<Item> loot) {
        super(x, y, description, image);
        this.keyID = keyID;
        this.isLocked = isLocked;
        this.loot = loot;
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

    public List<Item> getLoot() {
        return loot;
    }

    @Override
    public String toString() {
        return "c";
    }

}
