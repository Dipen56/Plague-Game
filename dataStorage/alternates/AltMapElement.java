package dataStorage.alternates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import server.game.world.MapElement;

/**
 * This class represents the an alternate version of the MapElement class, specifically for XML parsing.
 * @author Daniel Anastasi 300145878
 *
 */
public class AltMapElement {

	AltMapElement(){

	}

	public String toString(){
		if(this instanceof AltChest)
			return ((AltChest)this).toString();
		if(this instanceof AltObstacle)
			return ((AltObstacle)this).toString();
		if(this instanceof AltTransitionSpace)
			return ((AltTransitionSpace)this).toString();
		return null;
	}
}
