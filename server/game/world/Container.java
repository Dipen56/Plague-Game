package server.game.world;

import java.util.List;
import server.game.items.Item;
import server.game.player.Player;

/**
 * This interface represents the property of being able to contain items inside.
 * It can be used in many Items or MapElements to create diversity of Item usage
 * and map interaction.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public interface Container {

	/**
	 * This constant is used for chest indicating that chest can only contain
	 * this number of items.
	 */
	public static final int CHEST_SIZE = 5;

	/**
	 * This constant is used for other containers indicating that other
	 * containers can only contain this number of items.
	 */
	public static final int OTHER_SIZE = 2;

	/**
	 * Get all Items inside this container
	 * 
	 * @return
	 */
	public List<Item> getLoot();

	/**
	 * Put item in this container.
	 * 
	 * @param item
	 * @return --- true if this item is put in; false if this action failed.
	 */
	public boolean putItemIn(Item item);

	/**
	 * let a player try to take items out from this container.
	 * 
	 * @param player
	 * @return --- true if the player has taken at least one item from this
	 *         container, or false if he has taken none from the container.
	 */
	public boolean lootTakenOutByPlayer(Player player);

}
