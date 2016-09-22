package dataStorage.alternates;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import server.game.items.Antidote;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
import server.game.world.ScrapPile;

public class AltScrapPile extends AltObstacle{

	/**
	 * The loot contained in this scrap pile
	 */
	@XmlElement
	private AltItem[] loot;
	
	/**
	 * Describes this object in the game client.
	 */
	@XmlElement
	private String description;
	
	
	public AltScrapPile(ScrapPile sp){
		 List<Item> spLoot = sp.getLoot();
		this.loot = new AltItem[spLoot.size()];
		Item item = null;
		//Copies each item in the loot as an adapter version of the original.
		for(int i = 0; i <spLoot.size(); i++){
			item = spLoot.get(i);
			if(item instanceof Antidote){
				this.loot[i] = new AltAntidote((Antidote)item);
			}
			else if(item instanceof Key){
				this.loot[i] = new AltKey((Key)item);
			}
			else if(item instanceof Torch){
				this.loot[i] = new AltTorch((Torch)item);
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
	AltScrapPile(){
		
	}
	
	/**
	 * Returns a copy of the original version of the object which this was a copy of.
	 * @return A ScrapPile object.
	 */
	public ScrapPile getOriginal(){
		List<Item> newLoot = new ArrayList<>();
		AltItem item = null;
		for(int i = 0; i < this.loot.length; i++){
				item = loot[i];
				if(item instanceof AltAntidote){
					newLoot.add(((AltAntidote)item).getOriginal());
				}
				else if(item instanceof AltKey){
					newLoot.add(((AltKey)item).getOriginal());
				}
				else if(item instanceof AltTorch){
					newLoot.add(((AltTorch)item).getOriginal());
				}
				else{
					throw new RuntimeException("Item is not of a recognised type.");
				}
		}
		return new ScrapPile(this.description, newLoot);
	}
	
}
