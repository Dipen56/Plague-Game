package server.game.world;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

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
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
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
        ScrapPile other = (ScrapPile) obj;
        if (loot == null) {
            if (other.loot != null)
                return false;
        } else if (!loot.equals(other.loot))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "S";
    }

}
