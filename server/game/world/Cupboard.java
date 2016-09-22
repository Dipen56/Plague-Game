package server.game.world;

import java.util.Iterator;
import java.util.List;

import server.game.GameError;
import server.game.items.Item;
import server.game.player.Player;

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
        if (isLocked) {
            return false;
        }
        if (loot.size() >= Container.OTHER_SIZE) {
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
    public String toString() {
        return "C";
    }

}
