package dataStorage.adapters;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import server.game.items.Torch;

/**
 * A copy of a Torch object, for use in parsing the object into XML.
 * @author Daniel Anastasi (anastadani 300145878)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TorchAdapter extends ItemAdapter{

	/**
	 * The time limit on this torch.
	 */
	@XmlElement
	private int timeLimit;

	/**
	 * True if the torch is being used.
	 */
	@XmlElement
    private boolean isFlaming;

    /**
     * A description of the torch.
     */
	@XmlElement
    private String description;

	public TorchAdapter(Torch item) {
		if(item == null)
			throw new IllegalArgumentException("Argument is null");
		this.timeLimit = item.getTimeLeft();
		this.isFlaming = item.isFlaming();
		this.description = item.getDescription();
	}

	/**
	 * Only to be called by XML parser.
	 */
	TorchAdapter() {

	}

	/**
	 * Creates a copy of the original Torch which this object was based on.
	 * @return The Torch copy.
	 */
	public Torch getOriginal(){
		return new Torch(description, timeLimit, isFlaming);
	}

	
	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){
		StringBuffer b = new StringBuffer("");
		b.append("TORCH: ");
		b.append(this.timeLimit + " ");
		b.append(this.isFlaming + " ");
		return b.toString();
	}
}
