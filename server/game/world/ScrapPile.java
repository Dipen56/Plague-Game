package server.game.world;

import java.util.List;

import server.game.GameError;
import server.game.items.Item;

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

}
