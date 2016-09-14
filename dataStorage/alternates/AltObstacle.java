package dataStorage.alternates;

import javax.xml.bind.annotation.XmlElement;

import server.game.world.MapElement;
import server.game.world.Obstacle;

/**
 * This class represents the an alternate version of the Obstacle class, specifically for XML parsing.
 * @author Daniel Anastasi 300145878
 *
 */
public class AltObstacle extends AltMapElement{

	/**
	 * Describes the obstacle.
	 */
	@XmlElement
	private String description;

	/**
	 * X an Y coordinates for this obstacle on the map.
	 */
	@XmlElement
	private int x,y;


	public AltObstacle(Obstacle obstacle) {
		if(obstacle == null)
			throw new IllegalArgumentException("Argument is null");
		this.description = obstacle.getDescription();
		this.x = obstacle.getX();
		this.y = obstacle.getY();
	}


	/**
	 * Only to be used by XML parser.
	 * @param obstacle
	 */
	AltObstacle() {

	}


	/**
	 * Return a copy of the object which this object was based on.
	 * @return The Obstacle copy.
	 */
	public Obstacle getOriginal() {
		return new Obstacle(x,y,description);
	}


}
