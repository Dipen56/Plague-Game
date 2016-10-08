package dataStorage.adapters;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import server.game.items.Key;
import server.game.items.Torch;

/**
 * A copy of a Torch object, for use in parsing the object into XML.
 * @author Daniel Anastasi (anastadani 300145878)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TorchAdapter extends ItemAdapter{

	/**
     * The "health" of this torch. The number indicates how many seconds left to burn.
     */
    private int timeLimit;

    /**
     * Is this torch lighted?
     */
    private boolean isFlaming;

    /**
     * The description of this item.
     */
    private String description;

	public TorchAdapter(Torch item) {
		if(item == null)
			throw new IllegalArgumentException("Argument is null");
		this.timeLimit = item.getTimeLeft();
		this.isFlaming = item.isFlaming();
		this.description = item.getDescription();
	}

	/**
	 * Only to be used by XML parser.
	 */
	TorchAdapter(){

	}

	/**
	 * Returns a copy of the object which this is based on.
	 * @return A Torch object
	 */
	public Torch getOriginal(){
		return new Torch(description, timeLimit, isFlaming);
	}

	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){

		return "t";
	}
}