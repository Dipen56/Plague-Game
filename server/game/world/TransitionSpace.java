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
    public String toString() {
        return "E";
    }
}
