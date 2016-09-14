package dataStorage.alternates;

import javax.xml.bind.annotation.XmlElement;

import server.game.items.Antidote;
import server.game.items.Item;
import server.game.items.Torch;

/**
 * A copy of a Torch object, for use in parsing the object into XML.
 * @author Daniel Anastasi 300145878
 */
public class AltTorch extends AltItem{

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

	public AltTorch(Torch item) {
		if(item == null)
			throw new IllegalArgumentException("Argument is null");
		this.timeLimit = item.getTimeLeft();
		this.isFlaming = item.isFlaming();
		this.description = item.getDescription();
	}

	/**
	 * Only to be called by XML parser.
	 */
	AltTorch() {

	}

	/**
	 * Creates a copy of the original Torch which this object was based on.
	 * @return The Torch copy.
	 */
	public Torch getOriginal(){
		return new Torch(description, timeLimit, isFlaming);
	}

}
