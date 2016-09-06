package game.items;

import javax.swing.ImageIcon;

/**
 * This class represents a pick-up object. A pick-up object is whatever a player can pick
 * up and put into inventory.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public abstract class PickUps {

    private String description;
    private ImageIcon sprite;

    /**
     * Constructor
     * 
     * @param description
     * @param sprite
     */
    public PickUps(String description, ImageIcon sprite) {
        this.description = description;
        this.sprite = sprite;
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
