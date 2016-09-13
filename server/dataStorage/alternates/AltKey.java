package server.dataStorage.alternates;

import javax.xml.bind.annotation.XmlElement;

import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;

/**
 * A copy of a Key object, for use in parsing the object into XML.
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878
 */
public class AltKey extends AltItem{

	 /**
     * The keyID specifies which door it can open. Only the door with the same keyID can
     * be opened by this key.
     */
	@XmlElement
    private int keyID;

    /**
     * The description of the key.
     */
	@XmlElement
    private String description;

	public AltKey(Key item) {
		if(item == null)
			throw new IllegalArgumentException("Argument is null");
		keyID = item.getKeyID();
	}

	/**
	 * Only to be used by XML parser.
	 */
	public AltKey(){

	}

	/**
	 * Returns a copy of the object which this is based on.
	 * @return A Key object
	 */
	public Key getOriginal(){
		return new Key(description, keyID);
	}

}
