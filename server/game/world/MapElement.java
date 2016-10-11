package server.game.world;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * An element to be placed within a map.
 * 
 * @author Hector (Fang Zhao 300364061)
 * @author Daniel Anastasi 300145878
 *
 */
@XmlRootElement
public interface MapElement {

	/**
	 * Get the char that representing this map element for broadcasting the map.
	 * 
	 * @return --- a single char to represent the map element.
	 */
	public char getMapChar();

}
