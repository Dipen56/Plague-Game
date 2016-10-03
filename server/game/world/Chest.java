package server.game.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import server.game.GameError;
import server.game.items.Item;
import server.game.player.Player;

/**
 * This class represents a chest. A chest can be locked or unlocked. If it is locked, a
 * key is required to unlock it. Each chest contains at least one loot.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Chest extends Obstacle implements Container, Lockable {

    /**
     * The keyID specifies which key can open this chest. Only the key with the same keyID
     * can open it.
     */
    private int keyID;

    /**
     * True if the chest is locked.
     */
    private boolean isLocked;

    /**
     * Items in the chest.
     */
    private List<Item> loot;

    /**
     * Constructor
     *
     * @param description
     *            --- description
     * @param keyID
     *            --- the key Id used to identify the matching key
     * @param isLocked
     *            --- whether it is locked
     * @param loot
     *            --- the loot inside.
     */
    public Chest(String description, int keyID, boolean isLocked, List<Item> loot) {
        super(description);
        this.keyID = keyID;
        this.isLocked = isLocked;

        if (loot.size() > Container.CHEST_SIZE) {
            throw new GameError(
                    "Chest can only contain " + Container.CHEST_SIZE + " items.");
        }
        if(loot == null)
        	this.loot = new ArrayList<>();
        else
        	this.loot = loot;
    }

    @Override
    public List<Item> getLoot() {
        return loot;
    }

    @Override
    public boolean putItemIn(Item item) {
        if (isLocked) {
            return false;
        }
        if (loot.size() >= Container.CHEST_SIZE) {
            return false;
        }

        return loot.add(item);
    }

    @Override
    public boolean lootTakenOutByPlayer(Player player) {
        if (isLocked) {
            return false;
        }
        boolean tookAtLeastOne = false;
        Iterator<Item> itr = loot.iterator();
        while (itr.hasNext()) {
            if (player.getInventory().size() < Player.INVENTORY_SIZE) {
                Item item = itr.next();
                player.pickUpItem(item);
                itr.remove();
                tookAtLeastOne = true;
            } else {
                break;
            }
        }
        return tookAtLeastOne;
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
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (isLocked ? 1231 : 1237);
        result = prime * result + keyID;
        result = prime * result + ((loot == null) ? 0 : loot.hashCode());
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
        Chest other = (Chest) obj;
        if (isLocked != other.isLocked)
            return false;
        if (keyID != other.keyID)
            return false;
        if (loot == null) {
            if (other.loot != null)
                return false;
        } else if (!loot.equals(other.loot))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "C";
    }

}
