package server.game.items;

/**
 * This class represents a pick-up object. A pick-up object is whatever a player
 * can pick up and put into inventory.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public abstract class Item {

	/**
	 * The description of this item.
	 */
	private String description;

	/**
	 * Constructor
	 *
	 * @param description
	 *            --- The description of this item.
	 */
	public Item(String description) {
		this.description = description;
	}

	/**
	 * Get the description of this item.
	 * 
	 * @return --- the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Change the description of this object. Whenever the status of an object
	 * is changed, the description should change accordingly. E.g. a torch has
	 * less time to light up.
	 *
	 * @param description
	 */
	public void changeDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		Item other = (Item) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return description;
	}

}
