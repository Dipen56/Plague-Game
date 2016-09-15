package dataStorage.alternates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;

import server.game.items.Antidote;
import server.game.items.Destroyable;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
import server.game.items.Tradable;
import server.game.player.Direction;
import server.game.player.Player;
import server.game.player.Virus;
import server.game.world.Area;
import server.game.world.Chest;
import server.game.world.GroundSquare;
import server.game.world.Position;
import server.game.world.Room;
import server.game.world.RoomEntrance;
import server.game.world.World;
/**
 * This class represents the an alternate version of the Player class, specifically for XML parsing.
 *
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878
 *
 */
public class AltPlayer {
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
	 * The player's inventory.
	 */
	@XmlElement
	private AltItem[] inventory;

	// Geographical information.
	/**
	 * The area in which the player currently resides.
	 */
	@XmlElement
	private AltArea altArea;

	/**
	 * The position of the player in the current area.
	 */
	@XmlElement
	private AltPosition position;

	/**
	 * The direction which the player is facing.
	 */
	//@XmlAttribute
	@XmlElement
	private Direction direction;


	/**
	 * This constructor should only be called by an XML marshaller.
	 */
	AltPlayer(){

	}

	/**
	 * @param The original Player object
	 */
	public AltPlayer(Player player) {
		if(player == null)
			throw new IllegalArgumentException("Argument is null");
		this.uID = player.getId();
		this.name = player.getName();
		this.virus = player.getVirus();
		health = player.getHealthLeft();
		isAlive = player.isAlive();
		//Inventory is converted to array as List cannot have JXB annotations.
		List<Item> playerInventory = player.getInventory();
		inventory = new AltItem[playerInventory.size()];

		Item item = null;
		//Creates a new AltItem from each item in the inventory.
		for(int i = 0; i < playerInventory.size(); i++){
			item = playerInventory.get(i);
			if(item instanceof Antidote){
				inventory[i] = new AltAntidote((Antidote)item);
			}
			else if(item instanceof Key){
				inventory[i] = new AltKey((Key)item);
			}
			else if(item instanceof Torch){
				inventory[i] = new AltTorch((Torch)item);
			}
			else{
				continue;
			}

		}
		Area area = player.getArea();

		if(area instanceof World){
			altArea = new AltWorld(area);
		}
		else if(area instanceof Room){
			altArea = new AltRoom(area);
		}
		else{
			//This should not happen.
		}

		altArea = new AltArea(player.getArea());
		position = new AltPosition(player.getPosition());
		direction = player.getDirection();
	}

	/**
	 * Returns a copy of the original class which this was based on.
	 * @return A Player object.
	 */
	public Player getOriginal(){
		List<Item> newInventory = new ArrayList<>();
		AltItem item = null;
		// If player inventory was empty before save, then the current inventory will be null.
		if(inventory != null){
			for(int index = 0; index < inventory.length; index++){
				item = inventory[index];
				if(item instanceof AltAntidote){
					newInventory.add(((AltAntidote)item).getOriginal());
				}
				else if(item instanceof AltKey){
					newInventory.add(((AltKey)item).getOriginal());
				}
				else if(item instanceof AltTorch){
					newInventory.add(((AltTorch)item).getOriginal());
				}
				else{
					continue;
				}
			}
		}
		
		Area newArea = altArea.getOriginal();
		Position newPosition = position.getOriginal();
		Direction direction = this.direction;

		return new Player(uID, name, virus, newArea, health, isAlive, newInventory, newPosition, direction);	//Passing only this object prevents sending 6 arguments.
	}

}

