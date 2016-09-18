package server.game.world;

import java.util.List;

import server.game.items.Item;

public interface Container {

    /**
     * This constant is used for chest indicating that chest can only contain this number
     * of items.
     */
    public static final int CHEST_SIZE = 5;

    /**
     * This constant is used for other containers indicating that other containers can
     * only contain this number of items.
     */
    public static final int OTHER_SIZE = 2;

    /**
     * Get all Items inside this container
     * 
     * @return
     */
    public List<Item> getLoot();

    /**
     * Put item in this container.
     * 
     * @param item
     * @return --- true if this item is put in; false if this action failed.
     */
    public boolean putItemIn(Item item);

}
