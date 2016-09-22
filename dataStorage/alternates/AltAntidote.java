package dataStorage.alternates;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

import server.game.items.Antidote;
import server.game.items.Item;
import server.game.player.Virus;

/**
 * A copy of a Antidote object, for use in parsing the object into XML.
 * @author Daniel Anastasi (anastadani 300145878)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AltAntidote extends AltItem{

	/**
	 * The virus which this antidote provides relief from.
	 */
	@XmlElement
	private Virus virus;

	/**
	 * A description of this virus.
	 */
	@XmlElement
	private String description;
	
	public AltAntidote(Antidote a) {
		if(a == null)
			throw new IllegalArgumentException("Argument is null");
		virus = a.getVirus();
		description = a.getDescription();
	}

	/**
	 * Only to be called by XML parser.
	 */
	AltAntidote(){

	}

	/**
	 * Returns a copy of the object which this object was based on.
	 * @return An Antidote object.
	 */
	public Antidote getOriginal(){
		return new Antidote(description, virus);
	}

	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){
		StringBuffer b = new StringBuffer("");
		b.append("ANTIDOTE: ");
		b.append(this.virus + " ");
		b.append(this.description + " ");
		return b.toString();
	}
}
