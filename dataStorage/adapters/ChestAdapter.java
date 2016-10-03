package dataStorage.adapters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import server.game.items.Antidote;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
import server.game.world.Chest;

/**
 * A copy of a Chest object, for use in parsing the object into XML.
 * @author Daniel Anastasi (anastadani 300145878)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ChestAdapter extends ObstacleAdapter{
	/**
	 * The id for the key that unlocks this chest.
	 */
	@XmlElement
	private int keyID;

	/**
	 * True if the chest is locked.
	 */
	@XmlElement
	private boolean isLocked;

	/**
	 * A list of all items in this chest.
	 */
	@XmlElement
	private ItemAdapter[] loot;

	public ChestAdapter(Chest chest){
		if(chest == null)
			throw new IllegalArgumentException("Argument is null");
		keyID = chest.getKeyID();
		isLocked = chest.isLocked();
		this.setDescrption(chest.getDescription());

		//Array is used, as List cannot have the same JAXB annotation.
		List<Item> chestLoot = chest.getLoot();
		loot = new ItemAdapter[chestLoot.size()];

		Item item = null;
		for(int i = 0; i < chestLoot.size(); i++){
			item = chestLoot.get(i);
			if(item instanceof Antidote){
				loot[i] = (new AntidoteAdapter((Antidote)item));
			}
			else if(item instanceof Key){
				loot[i] = (new KeyAdapter((Key)item));
			}
			else{
				continue;
			}
		}
	}

	/**
	 * Only to be called by XML parser.
	 */
	ChestAdapter(){

	}

	/**
	 * Returns a copy of the object which this object was based on.
	 * @return A Chest object.
	 */
	public Chest getOriginal(){
		List<Item> newLoot = new ArrayList<>();
		ItemAdapter item = null;
		for(int i = 0; i < loot.length; i++){
			item = loot[i];
			if(item instanceof AntidoteAdapter){
				newLoot.add(((AntidoteAdapter)item).getOriginal());
			}
			else if(item instanceof KeyAdapter){
				newLoot.add(((KeyAdapter)item).getOriginal());
			}
			else{
				throw new RuntimeException("Item is not of a recognised type.");
			}
		}

		return new Chest(description, keyID, isLocked, newLoot);
	}

	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){
		StringBuffer b = new StringBuffer();
		b.append("CHEST: ");
		b.append(keyID + " ");
		b.append(isLocked + " ");

		for(int i = 0; i < loot.length; i ++){
			b.append(loot[i] + " ");
		}
		b.append(description + " ");
		return b.toString();
	}
}
