package dataStorage.adapters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import server.game.items.Antidote;
import server.game.items.Bag;
import server.game.items.Item;


/**
 * A copy of a Bag object, for use in parsing the object into XML.
 * @author Daniel Anastasi (anastadani 300145878)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BagAdapter extends ItemAdapter{

	/**
	 * The antidote contained in this bag
	 */
	@XmlElement
	private AntidoteAdapter loot;

	/**
	 * Describes this object in the game client.
	 */
	@XmlElement
	private String description;


	/**
	 * @param sp	The object on which to base this object
	 **/
	public BagAdapter(Bag sp){
		List<Item> spLoot = sp.getLoot();
		Item item = null;
		//Copies the item in the loot as an adapter version of the original.
		item = spLoot.get(0);
		if(!(item instanceof Antidote))
			throw new RuntimeException("Bag contains item that is not an antidote " + item.getDescription());
		this.loot = new AntidoteAdapter((Antidote)item);
		this.description = sp.getDescription();
	}

	/**
	 * This constructor is only to be called by an XML parser.
	 */
	BagAdapter(){

	}

	/**
	 * Returns a copy of the original version of the object which this was a copy of.
	 * @return A ScrapPile object.
	 */
	public Bag getOriginal(){
		List<Item> newLoot = new ArrayList<>();

		if(loot == null)
			throw new RuntimeException("Item should never be null");
		newLoot.add(((AntidoteAdapter)loot).getOriginal());

		return new Bag(this.description, newLoot);
	}

	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){
		return "Bag: "+this.description + " ";
	}

}
