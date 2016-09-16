package server.game.world;

import dataStorage.alternates.AltArea;
import server.game.player.Direction;

/**
 * A space in the map, on which the player must be standing in order to enter a destination Area.
 * @author Daniel Anastasi 300145878, Hector (Fang Zhao 300364061)
 *
 */
public class TransitionSpace extends MapElement{

	/**
	 * The position of this object in the area.
	 */
	public final Position position;

	/**
	 * The position in the destination area from this space.
	 */
	public final Position destPosition;

    /**
     * The direction which the player must travel from this space to enter the destination area.
     */
    public final Direction direction;


    /**
     * @param The position of this object on the map.
     * @param The destination position in the destination area.
     * @param Direction to face in order to enter destination area.
     */
    public TransitionSpace(Position pos, Position destPos, Direction direction) {
    	this.position = pos;
        this.destPosition = destPos;
        this.direction = direction;
    }


    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destPosition == null) ? 0 : destPosition.hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
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
		TransitionSpace other = (TransitionSpace) obj;
		if (destPosition == null) {
			if (other.destPosition != null)
				return false;
		} else if (!destPosition.equals(other.destPosition))
			return false;
		if (direction != other.direction)
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}




	@Override
    public String toString() {
        return "E";
    }
}
