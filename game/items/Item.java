package game.items;

import javax.swing.ImageIcon;

/**
 * This class represents an non-antidote item. An item is put in player's item inventory.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public abstract class Item extends PickUps {

    /**
     * Constructor
     * 
     * @param description
     * @param sprite
     */
    public Item(String description, ImageIcon sprite) {
        super(description, sprite);
    }

}
