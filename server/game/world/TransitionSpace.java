package server.game.world;

import server.game.player.Direction;

/**
 * A space in the map, on which the player must be standing in order to enter a destination Area.
 * @author Daniel Anastasi 300145878, Hector (Fang Zhao 300364061)
 *
 */
public class TransitionSpace extends MapElement{

	/**
	 * The area which the space resides;
	 */
	public final Area currentArea;

	/**
	 * The destination Area to be reached from this space.
	 */
	public final Area destArea;
	/**
	 * The x and y indeces to transition the player to in the destination area.
	 */
	public final int destX, destY;

    /**
     * The direction which the player must travel from this space to enter the destination area.
     */
    public final Direction direction;

    /**
     * @param Destination X coordinate
     * @param Destination Y coordinate
     * @param Destination Area
     * @param Direction to face in order to enter destination area.
     */
    public TransitionSpace(int x, int y, Area currentArea, Area destArea, Direction direction) {
    	this.currentArea = currentArea;
        this.destArea = destArea;
        this.destX = x;
        this.destY = y;
        this.direction = direction;
    }


    @Override
    public String toString() {
        return "E";
    }
}
