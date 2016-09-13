package server.game.world;

import server.dataStorage.alternates.AltArea;
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
	 * The position of this object in the area.
	 */
	public final Position position;

	/**
	 * The x and y indeces to transition the player to in the destination area.
	 */
	public final int destX, destY;

    /**
     * The direction which the player must travel from this space to enter the destination area.
     */
    public final Direction direction;

    /**
     * A copy of the area and destination area created when during the game save.
     */
    private AltArea areaCopiedForSave = null, destAreaCopiedForSave = null;



    /**
     * @param Destination X coordinate
     * @param Destination Y coordinate
     * @param The current area of this TransitionSpace
     * @param The position of this object on the map.
     * @param Destination Area
     * @param Direction to face in order to enter destination area.
     */ 
    public TransitionSpace(int x, int y, Area currentArea, Position pos, Area destArea, Direction direction) {
    	this.currentArea = currentArea;
    	this.position = pos;
        this.destArea = destArea;
        this.destX = x;
        this.destY = y;
        this.direction = direction;
    }

    public AltArea getAreaCopy(){
    	return this.areaCopiedForSave;
    }
    
    public AltArea getDestAreaCopy(){
    	return this.destAreaCopiedForSave;
    }
    
    /**
     * Argument can be null.
     */
    public void setAreaCopiedForSave(AltArea a){
    	this.areaCopiedForSave = a;
    }

    /**
     * Argument can be null.
     */
    public void setDestAreaCopiedForSave(AltArea a){
    	this.destAreaCopiedForSave = a;
    }
    
    
    /**
     * Returns true if the current area has been copied.
     * @return A boolean.
     */
    public boolean isAreaCopiedForSave(){
    	return this.areaCopiedForSave != null;
    }
    
    /**
     * Returns true if the destination area has been copied.
     * @return A boolean.
     */
    public boolean isDestAreaCopiedForSave(){
    	return this.destAreaCopiedForSave != null;
    }

    
    
    @Override
    public String toString() {
        return "E";
    }
}
