package dataStorage.adapters;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import server.game.items.Key;

/**
 * A copy of a Key object, for use in parsing the object into XML.
 * @author Daniel Anastasi (anastadani 300145878)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class KeyAdapter extends ItemAdapter{

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

	public KeyAdapter(Key item) {
		if(item == null)
			throw new IllegalArgumentException("Argument is null");
		keyID = item.getKeyID();
		description = item.getDescription();
	}

	/**
	 * Only to be used by XML parser.
	 */
	KeyAdapter(){

	}

	/**
	 * Returns a copy of the object which this is based on.
	 * @return A Key object
	 */
	public Key getOriginal(){
		return new Key(description, keyID);
	}
	
	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){
		StringBuffer b = new StringBuffer("");
		b.append("KEY: ");
		b.append(this.keyID + " ");
		b.append(this.description + " ");
		return b.toString();
	}
}
