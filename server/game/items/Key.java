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

    public Key(String description, int keyID) {
        super(description);
        this.keyID = keyID;
    }

    public int getKeyID() {
        return keyID;
    }

    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + keyID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		if (keyID != other.keyID)
			return false;
		return true;
	}

	@Override
    public String toString() {
        return super.toString() + " A number is engraved on it: " + keyID + ".";
    }
}
