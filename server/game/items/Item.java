package server.game.items;

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
    
    public String getDescription(){
    	return description;
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
