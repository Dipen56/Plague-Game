package server.dataStorage.alternates;

import server.game.player.Direction;
import server.game.world.Area;
import server.game.world.MapElement;
import server.game.world.RoomEntrance;
import server.game.world.TransitionSpace;

/**
 * An alternative version of the TransistionSpace class, only to be used for XML parsing.
 * @author Daniel Anastasi 300145878
 *
 */
public class AltTransitionSpace extends AltMapElement{
	/**
	 * The destination Area to be reached from this space.
	 */
	@XmlElement
	private AltArea destArea;
	/**
	 * The x and y indeces to transition the player to in the destination area.
	 */
	@XmlElement
	private int destX, destY;

    /**
     * The direction which the player must travel from this space to enter the destination area.
     */
	@XmlElement
	private Direction direction;

	public AltTransitionSpace(TransitionSpace value) {
		destArea = new AltArea(value.destArea);
		destX = value.destX;
		destY = value.destY;
		direction = value.direction;
	}

	/**
	 * Only to be used by XML marshaller.
	 */
	public AltTransitionSpace(){

	}

	public TransitionSpace getOriginal() {
		Area area = destArea.getOriginal();
		return new TransitionSpace(destX, destY, area, direction);
	}

}
