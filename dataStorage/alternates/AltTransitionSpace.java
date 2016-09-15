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
	 * The area which this space resides.
	 */
	@XmlElement
	private AltArea currentArea;

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

	public AltTransitionSpace(TransitionSpace ts) {
		if(ts == null)
			throw new IllegalArgumentException("Argument is null");
		// Checks if the area that the object lies in has been copied.
		if(ts.isAreaCopiedForSave()){
			currentArea = ts.getAreaCopy();
		}
		else{
			currentArea = new AltArea(ts.currentArea);
		}
		// Checks if destination area from the object has been copied.
		if(ts.isDestAreaCopiedForSave()){
			destArea = ts.getDestAreaCopy();
		}
		else{
			destArea = new AltArea(ts.destArea);
		}

		position = new AltPosition(ts.position);

		destX = ts.destX;
		destY = ts.destY;
		direction = ts.direction;
	}

	/**
	 * Only to be used by XML marshaller.
	 */
	AltTransitionSpace(){

	}

	public TransitionSpace getOriginal() {
		Area areaD = destArea.getOriginal();
		Area areaC = currentArea.getOriginal();
		Position pos = position.getOriginal();
		return new TransitionSpace(destX, destY, areaC, pos, areaD, direction);
	}

}
