package server.game.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import server.game.items.Antidote;
import server.game.items.Destroyable;
import server.game.items.Item;
import server.game.items.Key;
import server.game.items.Torch;
import server.game.items.Tradable;
import server.game.world.Container;
import server.game.world.Lockable;

/**
 * This class represents a player.
 *
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878
 *
 */
public class Player {
    /**
     * The inventory size
     */
    private static final int INVENTORY_SIZE = 8;
    /**
     * The max health (time left) of player. This number is set to 10 minutes. After 10
     * minutes, the player will die.
     */
    private static final int MAX_HEALTH = 10 * 60;
    /**
     * User Id to identify this player.
     */
    private final int uID;
    /**
     * player's name
     */
    private final String name;
    /**
     * What virus this player has
     */
    private final Virus virus;
    /**
     * The player's health
     */
    private int health;
    /**
     * Only used for testing validity of game load.
     */
    private int healthSavingConstant = 0;
    /**
     * Indicates whether the player is alive or not.
     */
    private boolean isAlive;
    /**
     * If the player is holding a lighted torch or not.
     */
    private boolean isHoldingTorch;
    /**
     * All player's items as a list
     */
    private List<Item> inventory;
    /**
     * The player's coordinate
     */
    private Position position;
    /**
     * The player's facing direction
     */
    private Direction direction;

    /**
     * Constructor
     *
     * @param uID
     * @param name
     * @param virus
     */
    public Player(int uID, String name) {
        this.uID = uID;
        this.name = name;
        health = MAX_HEALTH;
        isAlive = true;
        isHoldingTorch = false;
        inventory = new ArrayList<>();
        this.virus = Virus.randomVirus();
        this.direction = Direction.randomDirection();

        // player's position should be set by server by joinPlayer().
        this.position = null;
    }

    /**
     * A construction used for testing data storage.
     * 
     * The builder pattern was considered as an alternative to this constructor. As this
     * will only be constructed once per game load, that pattern was considered
     * unnecessary.
     * 
     * @param ID
     * @param name
     * @param virus
     * @param area
     * @param health
     * @param isAlive
     * @param newInventory
     * @param newPosition
     * @param direction
     */
    public Player(int ID, String name, Virus virus, int health, boolean isAlive,
            List<Item> newInventory, Position newPosition, Direction direction) {

        /*
         * kept for testing game load. Health field is decremented with time, so can't use
         * for test.
         */
        this.healthSavingConstant = health;
        this.uID = ID;
        this.name = name;
        this.virus = virus;
        this.health = health;
        this.isAlive = isAlive;
        this.inventory = newInventory;
        this.position = newPosition;
        this.direction = direction;
    }

    /**
     * This method increases (or decrease if the argument is a negative integer) player's
     * health (time left).
     *
     * @param effect
     *            --- how much time could the antidote give to player. if this argument is
     *            negative, it decreases player's life.
     */
    public void increaseHealth(int effect) {
        if (isAlive) {
            health += effect;
            if (health <= 0) {
                isAlive = false;
            }
        }
    }

    /**
     * This method let the player to drink a potion of antidote. If the antidote's type
     * matches the player's virus, then it prolongs the player's life. Otherwise it has a
     * certain chance to either lessen the player's life for the same amount, or prolong
     * twice the amount of a right antidote.
     *
     * @param antidote
     */
    public void drinkAntidote(Antidote antidote) {
        // dead man should do nothing
        if (!isAlive) {
            return;
        }

        // this is not my antidote!
        if (!inventory.contains(antidote)) {
            return;
        }

        if (antidote.getVirus().equals(virus)) {
            increaseHealth(Antidote.EFFECT);
        } else {
            int effect = Math.random() < Antidote.CURE_CHANCE ? Antidote.EFFECT * 2
                    : -Antidote.EFFECT;
            increaseHealth(effect);
        }

        destroyItem(antidote);
    }

    /**
     * This method let the player to light up a torch.
     *
     * @param torch
     */
    public void lightUpTorch(Torch torch) {
        // dead man should do nothing
        if (!isAlive) {
            return;
        }

        // this is not my torch!
        if (!inventory.contains(torch)) {
            return;
        }

        if (!torch.isFlaming()) {
            torch.setIsFlaming(true);
        }

        isHoldingTorch = true;
    }

    /**
     * This method let the player to extinguish a torch.
     * 
     * @param torch
     */
    public void extinguishTorch(Torch torch) {
        // dead man should do nothing
        if (!isAlive) {
            return;
        }

        // this is not my torch!
        if (!inventory.contains(torch)) {
            return;
        }

        if (torch.isFlaming()) {
            torch.setIsFlaming(false);
        }

        isHoldingTorch = false;
    }

    /**
     * Is the player holding a lighted torch?
     * 
     * @return
     */
    public boolean isHoldingTorch() {
        return isHoldingTorch;
    }

    /**
     * This method destroys the specified item in player's inventory.
     *
     * @param destroyable
     * @return
     */
    public boolean destroyItem(Destroyable destroyable) {
        // dead man should do nothing
        if (!isAlive) {
            return false;
        }

        // this is not my item!
        if (!inventory.contains(destroyable)) {
            return false;
        }

        return inventory.remove(destroyable);
    }

    /**
     * This method let the player to pick up an item.
     *
     * @param item
     * @return
     */
    public boolean pickUpItem(Item item) {
        // dead man should do nothing
        if (!isAlive) {
            return false;
        }

        if (inventory.size() >= INVENTORY_SIZE) {
            return false;
        }

        return inventory.add(item);
    }

    /**
     * Let the player give an item to another player. This is used in trading system.
     *
     * @param other
     * @param item
     * @return
     */
    public boolean giveItemTo(Player other, Item item) {
        // dead man should do nothing
        if (!isAlive) {
            return false;
        }

        // if it's not tradable, you cannot give it to others.
        if (!(item instanceof Tradable)) {
            return false;
        }

        // TODO should check if two players are within a certain distance
        // or let the game check.

        // 2. check if this item belongs to current player.
        if (!inventory.contains(item)) {
            return false;
        }

        // 3. check if the other player has space for the item.
        if (other.inventory.size() >= INVENTORY_SIZE) {
            return false;
        }

        // OK, now we can give it to the other player.
        inventory.remove(item);
        other.pickUpItem(item);
        return true;
    }

    /**
     * This method let the player try to unlock a locked object (room, chest or Cupboard).
     * If the player has the key to open it, then it is unlocked. Notice: unlocked != room
     * entered / items taken.
     *
     * @param lockable
     * @return
     */
    public boolean tryUnlock(Lockable lockable) {
        // dead man should do nothing
        if (!isAlive) {
            return false; // gosh this should never happen!
        }

        for (Item item : inventory) {
            if (item instanceof Key) {
                Key key = (Key) item;
                if (key.getKeyID() == lockable.getKeyID()) {
                    lockable.setLocked(false);
                    inventory.remove(key);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This method let the player try to take items from a container (chest or cupboard,
     * etc.) in front. If the player's inventory can take in all items, he will take them
     * all; otherwise he will take as many as he can until his inventory is full.
     *
     * @param container
     * @return --- true if he has taken at least one item from the container, or false if
     *         he has taken none from the container.
     */
    public synchronized boolean tryTakeItemsFromContainer(Container container) {
        // dead man should do nothing
        if (!isAlive) {
            return false; // gosh this should never happen!
        }

        // if the container is locked, the player cannot take item from it
        if (container instanceof Lockable && ((Lockable) container).isLocked()) {
            return false;
        }

        boolean tookAtLeastOne = false;

        List<Item> loot = container.getLoot();
        Iterator<Item> itr = loot.iterator();
        while (itr.hasNext()) {
            if (inventory.size() < INVENTORY_SIZE) {
                Item item = itr.next();
                pickUpItem(item);
                itr.remove();
                tookAtLeastOne = true;
            } else {
                break;
            }
        }

        return tookAtLeastOne;
    }

    /**
     * Saves a record of the player's current health. This method is for use in comparing
     * game states from before and after a game load.
     */
    public void saveRecordOfHealth() {
        this.healthSavingConstant = health;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return uID;
    }

    public int getAreaID() {
        if (position != null) {
            return position.areaId;
        }

        // error code
        return -1;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position pos) {
        this.position = pos;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void turnLeft() {
        direction = direction.left();
    }

    public void turnRight() {
        direction = direction.right();
    }

    public Virus getVirus() {
        return virus;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * This method is used to track all torches owned by this player.
     * 
     * @return
     */
    public List<Torch> getAllTorches() {
        List<Torch> torches = new ArrayList<>();

        for (Item itm : inventory) {
            if (itm instanceof Torch) {
                torches.add((Torch) itm);
            }
        }

        return torches;
    }

    /**
     * This method is used to track all keys owned by this player.
     * 
     * @return
     */
    public List<Key> getAllKeys() {
        List<Key> keys = new ArrayList<>();

        for (Item itm : inventory) {
            if (itm instanceof Key) {
                keys.add((Key) itm);
            }
        }

        return keys;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getHealthLeft() {
        return health;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        result = prime * result + health;
        result = prime * result + healthSavingConstant;
        result = prime * result + ((inventory == null) ? 0 : inventory.hashCode());
        result = prime * result + (isAlive ? 1231 : 1237);
        result = prime * result + (isHoldingTorch ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + uID;
        result = prime * result + ((virus == null) ? 0 : virus.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Player other = (Player) obj;
        if (direction != other.direction)
            return false;
        if (health != other.health)
            return false;
        if (healthSavingConstant != other.healthSavingConstant)
            return false;
        if (inventory == null) {
            if (other.inventory != null)
                return false;
        } else if (!inventory.equals(other.inventory))
            return false;
        if (isAlive != other.isAlive)
            return false;
        if (isHoldingTorch != other.isHoldingTorch)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (uID != other.uID)
            return false;
        if (virus != other.virus)
            return false;
        return true;
    }

}
