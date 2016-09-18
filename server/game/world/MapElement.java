package server.game.world;

/**
 * An element to be placed within a map.
<<<<<<< HEAD
 * 
 * @author Daniel Anastasi 300145878
 *
 */
public interface MapElement {

    // @Override
    // public boolean equals(Object obj) {
    // if (this instanceof Chest)
    // return ((Chest) this).equals(obj);
    // if (this instanceof Obstacle)
    // return ((Obstacle) this).equals(obj);
    // if (this instanceof TransitionSpace)
    // return ((TransitionSpace) this).equals(obj);
    // return false;
    //
    // }

=======
 * @author Daniel Anastasi 300145878
 *
 */
public class MapElement {
	
	@Override
	public boolean equals(Object obj){
		if(this instanceof Chest)
			return ((Chest)this).equals(obj);
		if(this instanceof Obstacle)
			return ((Obstacle)this).equals(obj);
		if(this instanceof TransitionSpace)
			return ((TransitionSpace)this).equals(obj);
		return false;
		
	}
>>>>>>> master
}
