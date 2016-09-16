package dataStorage.alternates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import server.game.world.Position;

/**
 * This class represents the an alternate version of the Position class, specifically for XML parsing.
 * @author anastadani
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AltPosition {

	@XmlElement
	private int x, y;

	public AltPosition(Position pos){
		if(pos == null)
			throw new IllegalArgumentException("Argument is null");
		x = pos.x;
		y = pos.y;
	}

	/**
	 * Only to be used by XML parser
	 */
	AltPosition(){

	}

	/**
	 * Returns a copy of the original Position object which this was based on.
	 * @return A Position object.
	 */
	public Position getOriginal() {
		return new Position(x,y);
	}

}
