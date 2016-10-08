package server.game.world;

/**
 * This class represents an unwalkable position on map, like a rock, or a tree.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Obstacle implements MapElement {

	/**
	 * The description
	 */
	protected String description;

	/**
	 * Constructor
	 * 
	 * @param charRep
	 *            --- The char representation of this obstacle.
	 */
	public Obstacle(String charRep) {
		this.description = charRep;
	}

	/**
	 * Get the char representation of this obstacle.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
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
		Obstacle other = (Obstacle) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return description.substring(2);
	}

	@Override
	public char getMapChar() {
		return description.charAt(0);
	}

}
