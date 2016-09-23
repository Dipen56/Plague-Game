package dataStorage.adapters;

/**
 * This class represents the an alternate version of the MapElement class, specifically for XML parsing.
 * @author Daniel Anastasi (anastadani 300145878)
 *
 */
public class MapElementAdapter {

	MapElementAdapter(){

	}

	public String toString(){
		if(this instanceof ChestAdapter)
			return ((ChestAdapter)this).toString();
		if(this instanceof ObstacleAdapter)
			return ((ObstacleAdapter)this).toString();
		if(this instanceof TransitionSpaceAdapter)
			return ((TransitionSpaceAdapter)this).toString();
		return null;
	}
}
