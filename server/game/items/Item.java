package server.game.items;
<<<<<<< HEAD:server/game/items/Item.java
=======

import java.awt.image.BufferedImage;
>>>>>>> master:server/game/items/Item.java

/**
 * This class represents a pick-up object. A pick-up object is whatever a player can pick
 * up and put into inventory.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public abstract class Item {

    private String description;

    /**
     * Constructor
     *
     * @param description
     */
    public Item(String description) {
        this.description = description;
    }

<<<<<<< HEAD:server/game/items/Item.java
    public String getDescription() {
        return this.description;
=======
    public String getDescription(){
    	return this.description;
>>>>>>> master:server/game/items/Item.java
    }

    /**
     * Change the description of this object. Whenever the status of an object is changed,
     * the description should change accordingly. E.g. a torch has less time to light up.
     *
     * @param description
     */
    public void changeDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
    
}
