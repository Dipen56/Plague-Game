package dataStorage.alternates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import server.game.player.Direction;
import server.game.player.Position;


/**
 * This class represents the an alternate version of the Position class, specifically for XML parsing.
 * @author Daniel Anastasi (anastadani 300145878)
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AltPosition {

	/**
	 * X and Y coordinates of this position.
	 */
	@XmlElement
	private int x, y;
	
	 /**
     * The area which position is in.
     */
	@XmlElement
	private int areaId;
	
	/**
	 * The direction which the object at this position is facing.
	 */
	@XmlElement
	private Direction direction;

	public AltPosition(Position pos){
		if(pos == null)
			throw new IllegalArgumentException("Argument is null");
		x = pos.x;
		y = pos.y;
		areaId = pos.areaId;
		direction = pos.getDirection();
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
		return new Position(x,y,areaId,direction);
	}

	public String toString(){
		StringBuffer b = new StringBuffer();
		b.append("POSITION:");
		b.append(x + " ");
		b.append(y + " ");
		b.append(areaId + " ");
		b.append(direction + " ");
		return b.toString();
	}
}
