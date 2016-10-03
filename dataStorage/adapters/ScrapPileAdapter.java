package dataStorage.adapters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import server.game.items.Antidote;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
import server.game.world.ScrapPile;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ScrapPileAdapter extends ObstacleAdapter{

	/**
	 * The loot contained in this scrap pile
	 */
	@XmlElement
	private ItemAdapter[] loot;

	/**
	 * Describes this object in the game client.
	 */
	@XmlElement
	private String description;


	public ScrapPileAdapter(ScrapPile sp){
		 List<Item> spLoot = sp.getLoot();
		this.loot = new ItemAdapter[spLoot.size()];
		Item item = null;
		//Copies each item in the loot as an adapter version of the original.
		for(int i = 0; i <spLoot.size(); i++){
			item = spLoot.get(i);
			if(item instanceof Antidote){
				this.loot[i] = new AntidoteAdapter((Antidote)item);
			}
			else if(item instanceof Key){
				this.loot[i] = new KeyAdapter((Key)item);
			}
			else{
				throw new RuntimeException("Loot item is not a recognised item type.");
			}
		}
		this.description = sp.getDescription();
	}

	/**
	 * This constructor is only to be called by an XML parser.
	 */
	ScrapPileAdapter(){

	}

	/**
	 * Returns a copy of the original version of the object which this was a copy of.
	 * @return A ScrapPile object.
	 */
	public ScrapPile getOriginal(){
		List<Item> newLoot = new ArrayList<>();
		ItemAdapter item = null;
		for(int i = 0; i < this.loot.length; i++){
				item = loot[i];
				if(item == null)
					throw new RuntimeException("Item should never be null");
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
		return new ScrapPile(this.description, newLoot);
	}

	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){
		return "SCRAPPILE: "+this.description + " ";
	}
}
