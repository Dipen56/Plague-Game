package dataStorage.adapters;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import server.game.player.Direction;
import server.game.player.Position;
import server.game.world.Area;
import server.game.world.MapElement;
import server.game.world.TransitionSpace;

/**
 * An alternative version of the TransistionSpace class, only to be used for XML parsing.
 * @author Daniel Anastasi (anastadani 300145878)
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TransitionSpaceAdapter extends MapElementAdapter{

	/**
	 * The position of this object in the area.
	 */
	@XmlElement
	private PositionAdapter position;

	/**
	 * The Position in the area that this space leads to.
	 */
	@XmlElement
	private PositionAdapter destPos;

    /**
     * The direction which the player must travel from this space to enter the destination area.
     */
	@XmlElement
	private Direction direction;

	/**
	 * @param ts	The object on which to base this object
	 **/
	public TransitionSpaceAdapter(TransitionSpace ts) {
		if(ts == null)
			throw new IllegalArgumentException("Argument is null");

		position = new PositionAdapter(ts.getCurrentPosition());
		destPos = new PositionAdapter(ts.getDestination());
	}

	/**
	 * Only to be used by XML marshaller.
	 */
	TransitionSpaceAdapter(){

	}

	/**
	 * Return a copy of the object which this object was based on.
	 * @return The TransitionSpace copy.
	 */
	public TransitionSpace getOriginal() {
		Position pos = position.getOriginal();
		Position destPos = this.destPos.getOriginal();
		return new TransitionSpace(pos, destPos);
	}

	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){
		StringBuffer b = new StringBuffer("");
		b.append(position + " ");
		b.append(destPos + " ");
		b.append(direction + " ");
		return b.toString();
	}
}
