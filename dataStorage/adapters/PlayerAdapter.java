package dataStorage.adapters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import server.game.items.Antidote;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
import server.game.player.Avatar;
import server.game.player.Direction;
import server.game.player.Player;
import server.game.player.Position;
import server.game.player.Virus;

/**
 * This class is an adapter for a Player to be loaded to and from XML.
 * This behaves as a builder object in the builder pattern, in order to prevent the need for a constructor with 7+ parameters.
 *
 * @author Daniel Anastasi (anastadani 300145878)
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerAdapter {
	/**
	 * The player's user ID
	 */
	@XmlElement
	private int uID;

	/**
	 * Player name
	 */
	@XmlElement
	private String name;

	/**
	 * The virus which the player has.
	 */
	@XmlElement
	private  Virus virus;

	/**
	 * The player's health remaining.
	 */
	@XmlElement
	private int health;
	
	/**
	 * True if the player is still alive.
	 */
	@XmlElement
	private boolean isAlive;
	
	/**
     * If the player is holding a lighted torch or not.
     */
    private boolean isHoldingTorch;

	/**
	 * The player's inventory.
	 */
	@XmlElement
	private ItemAdapter[] inventory;

	// Geographical information.
	/**
	 * The position of the player in the current area.
	 */
	@XmlElement
	private PositionAdapter position;

	/**
	 * The direction which the player is facing.
	 */
	@XmlElement
	private Direction direction;

	/**
	 * The player's avatar.
	 */
	@XmlElement
	private Avatar avatar;

	/**
	 * This constructor should only be called by an XML marshaller.
	 */
	PlayerAdapter(){

	}

	/**
	 * @param The original Player object
	 */
	public PlayerAdapter(Player player) {
		if(player == null)
			throw new IllegalArgumentException("Argument is null");
		player.saveRecordOfHealth();	//records player health as unchanging int for testing game save validity.
		this.uID = player.getId();
		this.name = player.getName();
		this.virus = player.getVirus();
		health = player.getHealthLeft();
		isAlive = player.isAlive();
		isHoldingTorch = player.isHoldingTorch();
		//Inventory is converted to array as List cannot have JXB annotations.
		List<Item> playerInventory = player.getInventory();
		inventory = new ItemAdapter[playerInventory.size()];

		Item item = null;
		//Creates a new AltItem from each item in the inventory.
		for(int i = 0; i < playerInventory.size(); i++){
			item = playerInventory.get(i);
			if(item instanceof Antidote){
				inventory[i] = new AntidoteAdapter((Antidote)item);
			}
			else if(item instanceof Key){
				inventory[i] = new KeyAdapter((Key)item);
			}
			else if(item instanceof Torch){
				inventory[i] = new TorchAdapter((Torch)item);
			}
			else{
				continue;
			}

		}
		avatar = player.getAvatar();
		position = new PositionAdapter(player.getPosition());
		direction = player.getDirection();
		
	}

	/**
	 * Returns a copy of the original class which this was based on.
	 * @return A Player object.
	 */
	public Player getOriginal(){
		List<Item> newInventory = new ArrayList<>();
		ItemAdapter item = null;
		// If player inventory was empty before save, then the current inventory will be null.
		if(inventory != null){
			for(int index = 0; index < inventory.length; index++){
				item = inventory[index];
				if(item instanceof AntidoteAdapter){
					newInventory.add(((AntidoteAdapter)item).getOriginal());
				}
				else if(item instanceof KeyAdapter){
					newInventory.add(((KeyAdapter)item).getOriginal());
				}
				else if(item instanceof TorchAdapter){
					newInventory.add(((TorchAdapter)item).getOriginal());
				}
				else{
					continue;
				}
			}
		}
		
		Position newPosition = position.getOriginal();
		Direction direction = this.direction;
		
		
		//Restores player from this adapter object.
		Player p = new Player(avatar, health);
		p.setPosition(newPosition);	//set position
		p.setDirection(direction);//set direction
		p.setVirus(virus);
		p.setIsAlive(isAlive);
		p.setId(uID);
		p.setName(name);
		p.setInventory(newInventory);
		
		return p;
	}
	


}

