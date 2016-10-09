package server.game.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import server.game.GameError;
import server.game.player.Player;
import server.game.world.Container;

/**
 * This class represents a bag. A bag can contain at least one loot.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Bag extends Item implements Container {

	/**
	 * Items in the bag.
	 */
	private List<Item> loot;

	/**
	 * Constructor
	 * 
	 * @param description
	 *            --- description
	 * @param loot
	 *            --- the loot inside.
	 */
	public Bag(String description, List<Item> loot) {
		super(description);

		if (loot == null) {
			this.loot = new ArrayList<>();
		} else {
			if (loot.size() > Container.OTHER_SIZE) {
				throw new GameError("Chest can only contain " + Container.OTHER_SIZE + " items.");
			}
			this.loot = loot;
		}
	}

	@Override
	public List<Item> getLoot() {
		return loot;
	}

	@Override
	public boolean putItemIn(Item item) {
		// bag is one-use item.
		return false;
	}

	@Override
	public boolean lootTakenOutByPlayer(Player player) {
		boolean tookAtLeastOne = false;
		Iterator<Item> itr = loot.iterator();
		while (itr.hasNext()) {
			if (player.getInventory().size() < Player.INVENTORY_SIZE) {
				Item item = itr.next();
				player.pickUpItem(item);
				itr.remove();
				tookAtLeastOne = true;
			} else {
				break;
			}
		}
		return tookAtLeastOne;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((loot == null) ? 0 : loot.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bag other = (Bag) obj;
		if (loot == null) {
			if (other.loot != null)
				return false;
		} else if (!loot.equals(other.loot))
			return false;
		return true;
	}

}
