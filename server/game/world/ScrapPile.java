package server.game.world;

import java.util.Iterator;
import java.util.List;

import server.game.GameError;
import server.game.items.Item;
import server.game.player.Player;

/**
 * This class represents a pile of scrap. A pile of scrap can contain loot.
 * 
 * @author Hector
 *
 */
public class ScrapPile extends Obstacle implements Container {

    private List<Item> loot;

    public ScrapPile(String description, List<Item> loot) {
        super(description);

        if (loot.size() > Container.OTHER_SIZE) {
            throw new GameError(
                    "Chest can only contain " + Container.OTHER_SIZE + " items.");
        }

        this.loot = loot;
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

    @Override
    public boolean lootTakenOutByPlayer(Player player) {
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
        return "S";
    }

}
