package server.dataStorage.alternates;

import javax.xml.bind.annotation.XmlElement;

import server.game.world.Position;

/**
 * This class represents the an alternate version of the Position class, specifically for XML parsing.
 * @author anastadani
 *
 */
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
	 * Returns a copy of the original Position object which this was based on.
	 * @return A Position object.
	 */
	public Position getOriginal() {
		return new Position(x,y);
	}

}
