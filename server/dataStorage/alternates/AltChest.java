package server.dataStorage.alternates;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import server.game.items.Antidote;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
import server.game.world.Chest;

/**
 * A copy of a Chest object, for use in parsing the object into XML.
 * @author Daniel Anastasi 300145878
 */
public class AltChest {
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
	private List<AltItem> loot;

	/**
	 * A description of this chest.
	 */
	@XmlElement
	private String description;

	/**
	 * The x and y coordinates of this chest in the area which it resides.
	 */
	@XmlElement
	int x,y;

	public AltChest(Chest chest){
		if(chest == null)
			throw new IllegalArgumentException("Argument is null");
		keyID = chest.getKeyID();
		isLocked = chest.isLocked();
		description = chest.getDescription();

		loot = new ArrayList<>();
		for(Item i : chest.getLoot()){
			if(i instanceof Antidote){
				loot.add(new AltAntidote((Antidote)i));
			}
			else if(i instanceof Key){
				loot.add(new AltKey((Key)i));
			}
			else if(i instanceof Torch){
				loot.add(new AltTorch((Torch)i));
			}
			else{
				continue;
			}
		}
	}

	/**
	 * Only to be called by XML parser.
	 */
	public AltChest(){

	}

	/**
	 * Returns a copy of the object which this object was based on.
	 * @return A Chest object.
	 */
	public Chest getOriginal(){
		List<Item> newLoot = new ArrayList<>();
		for(AltItem i : loot){
			if(i instanceof AltAntidote){
				newLoot.add(((AltAntidote)i).getOriginal());
			}
			else if(i instanceof AltKey){
				newLoot.add(((AltKey)i).getOriginal());
			}
			else if(i instanceof AltTorch){
				newLoot.add(((AltTorch)i).getOriginal());
			}
			else{
				continue;
			}
		}

		return new Chest(x,y,description, keyID, isLocked, newLoot);
	}

}
