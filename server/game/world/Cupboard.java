package server.game.world;

import java.util.Iterator;
import java.util.List;

import server.game.items.Item;
import server.game.player.Player;

/**
 * This class represents a cupboard (normally in rooms). A cupboard can contain
 * loot, and it's lockable.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Cupboard extends Obstacle implements Container, Lockable {

	/**
	 * The keyID specifies which key can open the door to this room. Only the
	 * key with the same keyID can open the door to this room.
	 */
	private int keyID;

	/**
	 * Whether it is locked or not.
	 */
	private boolean isLocked;

	/**
	 * The loot inside.
	 */
	private List<Item> loot;

	/**
	 * Constructor
	 * 
	 * @param description
	 *            --- description
	 * @param keyID
	 *            --- the key Id used to identify the matching key
	 * @param isLocked
	 *            --- whether it is locked
	 * @param loot
	 *            --- the loot inside.
	 */
	public Cupboard(String description, int keyID, boolean isLocked, List<Item> loot) {
		super(description);
		this.keyID = keyID;
		this.isLocked = isLocked;
		this.loot = loot;
	}

	@Override
	public int getKeyID() {
		return keyID;
	}

	@Override
	public boolean isLocked() {
		return isLocked;
	}

	@Override
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	@Override
	public List<Item> getLoot() {
		return loot;
	}

	@Override
	public boolean putItemIn(Item item) {
		if (isLocked) {
			return false;
		}
		if (loot.size() >= Container.OTHER_SIZE) {
			return false;
		}

		return loot.add(item);
	}

	@Override
	public boolean lootTakenOutByPlayer(Player player) {
		if (isLocked) {
			return false;
		}
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
		result = prime * result + (isLocked ? 1231 : 1237);
		result = prime * result + keyID;
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
		Cupboard other = (Cupboard) obj;
		if (isLocked != other.isLocked)
			return false;
		if (keyID != other.keyID)
			return false;
		if (loot == null) {
			if (other.loot != null)
				return false;
		} else if (!loot.equals(other.loot))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public char getMapChar() {
		return 'U';
	}

}
