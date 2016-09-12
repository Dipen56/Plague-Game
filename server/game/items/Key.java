package server.game.items;

import java.awt.image.BufferedImage;

/**
 * This class represents a key.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Key extends Item implements Tradable {

    /**
     * The keyID specifies which door it can open. Only the door with the same keyID can
     * be opened by this key.
     */
    private int keyID;

    public Key(String description, BufferedImage sprite, int keyID) {
        super(description, sprite);
        this.keyID = keyID;
    }

    public int getKeyID() {
        return keyID;
    }

    @Override
    public String toString() {
        return super.toString() + " A number is engraved on it: " + keyID + ".";
    }
}
