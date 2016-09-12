package server.dataStorage.alternates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import server.game.items.Antidote;
import server.game.items.Destroyable;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
import server.game.items.Tradable;
import server.game.player.Direction;
import server.game.player.Player;
import server.game.player.Virus;
import server.game.world.Chest;
import server.game.world.GroundSquare;
import server.game.world.Room;
import server.game.world.RoomEntrance;
/**
 * This class represents the an alternate version of the Player class, specifically for XML parsing.
 * 
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878
 *
 */
public class AltPlayer {
	    private final int uID;
	    private final String name;
	    private final Virus virus;
	    private int health;
	    private boolean isAlive;
	    private List<Item> inventory;

	    // Geographical infos
	    private AltArea area;
	    private AltPosition position;
	    private Direction direction;

	    
	    /**
	     * This constructor should only be called by an XML marshaller.
	     */
	    public AltPlayer(){
	    	
	    }
	    
	    /**
	     * @param The original Player object
	     */
	    public AltPlayer(Player player) {
	        this.uID = player.getId();
	        this.name = player.getName();
	        this.virus = player.getVirus();
	        health = player.getHealthLeft();
	        isAlive = player.isAlive();
	        inventory = new ArrayList<>();
	        //Creates a new AltItem from each item in the inventory.
	        for(Item i : player.getInventory()){
	        	inventory.add(new AltItem(i));
	        }
	        area = new AltArea(player.getArea());
	        position = new AltPosition(player.getPosition());
	    }

	/**
	 * Returns a copy of the original class which this was based on.
	 * @return A Player object.
	 */
	public Player getOriginal(){
		return new Player(this);	//Passing only this object prevents sending 6 arguments.
	}

}

