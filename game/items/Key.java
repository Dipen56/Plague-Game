package game.items;

import javax.swing.ImageIcon;

public class Key extends Item implements Tradable {

    /**
     * The keyID specifies which door it can open. Only the door with the same keyID can
     * be opened by this key.
     */
    private int keyID;

    public Key(String description, ImageIcon sprite, int keyID) {
        super(description, sprite);
        this.keyID = keyID;
    }

    public int getKeyID() {
        return keyID;
    }

}
