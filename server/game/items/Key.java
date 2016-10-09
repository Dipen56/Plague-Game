package server.game.items;

/**
 * This class represents a key.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Key extends Item implements Tradable {

	/**
	 * The keyID specifies which lockable it can open. Only the lockable with
	 * the same keyID can be opened by this key.
	 */
	private int keyID;

	/**
	 * Constructor
	 *
	 * @param description
	 *            --- the description
	 * @param keyID
	 *            --- the keyID that specifies which lockable it can open.
	 */
	public Key(String description, int keyID) {
		super(description);
		this.keyID = keyID;
	}

	/**
	 * Get the key id.
	 *
	 * @return --- key id
	 */
	public int getKeyID() {
		return keyID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + keyID;
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
		Key other = (Key) obj;
		if (keyID != other.keyID)
			return false;
		return true;
	}

}
