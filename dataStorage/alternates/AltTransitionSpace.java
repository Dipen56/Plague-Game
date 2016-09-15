package dataStorage.alternates;

import javax.xml.bind.annotation.XmlElement;

import server.game.player.Direction;
import server.game.world.Area;
import server.game.world.MapElement;
import server.game.world.Position;
import server.game.world.RoomEntrance;
import server.game.world.TransitionSpace;

/**
 * An alternative version of the TransistionSpace class, only to be used for XML parsing.
 * @author Daniel Anastasi 300145878
 *
 */
public class AltTransitionSpace extends AltMapElement{

	/**
	 * The position of this object in the area.
	 */
	@XmlElement
	private AltPosition position;

	/**
	 * The Position in the area that this space leads to.
	 */
	@XmlElement
	private AltPosition destPos;

    /**
     * The direction which the player must travel from this space to enter the destination area.
     */
	@XmlElement
	private Direction direction;

	public AltTransitionSpace(TransitionSpace ts) {
		if(ts == null)
			throw new IllegalArgumentException("Argument is null");

		position = new AltPosition(ts.position);
		destPos = new AltPosition(ts.destPosition);
		direction = ts.direction;
	}

	/**
	 * Only to be used by XML marshaller.
	 */
	AltTransitionSpace(){

	}

	public TransitionSpace getOriginal() {
		Position pos = position.getOriginal();
		Position destPos = this.destPos.getOriginal();
		return new TransitionSpace(pos, destPos, direction);
	}

}
