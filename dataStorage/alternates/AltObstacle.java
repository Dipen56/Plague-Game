package dataStorage.alternates;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

import server.game.world.MapElement;
import server.game.world.Obstacle;

/**
 * This class represents the an alternate version of the Obstacle class, specifically for XML parsing.
 * @author Daniel Anastasi (anastadani 300145878)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AltObstacle extends AltMapElement{

	/**
	 * Describes the obstacle.
	 */
	@XmlElement
	protected String description;

	public AltObstacle(Obstacle obstacle) {
		if(obstacle == null)
			throw new IllegalArgumentException("Argument is null");
		this.description = obstacle.getDescription();
	}


	/*
	 * Only to be used by XML parser.
	 * @param obstacle
	 */
	AltObstacle(){

	}

	
	public void setDescrption(String description){
		this.description = description;
	}

	/**
	 * Return a copy of the object which this object was based on.
	 * @return The Obstacle copy.
	 */
	public Obstacle getOriginal() {
		return new Obstacle(description);
	}
	
	
	
	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){
		return "OBSTACLE: "+this.description + " ";
	}
}
