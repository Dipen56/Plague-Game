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
import server.game.world.Chest;
import server.game.world.GroundSquare;
import server.game.world.Room;
import server.game.world.RoomEntrance;

/**
 * This class represents a player.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Player {

    private static final int INVENTORY_SIZE = 8;
    /**
     * The max health (time left) of player. This number is set to 10 minutes. After 10
     * minutes, the player will die.
     */
    private static final int MAX_HEALTH = 10 * 60;

    private final int uID;
    private final String name;
    private final Virus virus;
    private int health;
    private boolean isAlive;
    private List<Item> inventory;

    // Geographical infos
    private GroundSquare position;
    private Direction direction;

    /**
     * Constructor
     * 
     * @param uID
     * @param name
     * @param virus
     */
    public Player(int uID, String name, Virus virus) {
        this.uID = uID;
        this.name = name;
        this.virus = virus;
        health = MAX_HEALTH;
        isAlive = true;
        inventory = new ArrayList<>();
    }

    public int getId() {
        return uID;
    }

    public GroundSquare getPosition() {
        return position;
    }

    public void setPosition(GroundSquare pos) {
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

    public boolean isAlive() {
        return isAlive;
    }

    public int getHealthLeft() {
        return health;
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
     * matches the player's virus, then it prolongs the player's life. Otherwise it has
     * 80% chance to lessen the player's life for the same amount, and 20% chance to
     * prolong twice the amount of a right antidote.
     * 
     * Note this method doesn't check if the player has this antidote in inventory. This
     * should be done before this method is called.
     * 
     * @param antidote
     */
    public void drinkAntidote(Antidote antidote) {
        // dead man should do nothing
        if (!isAlive) {
            return;
        }

        if (!inventory.contains(antidote)) {
            return;
        }

        if (antidote.getVirus().equals(virus)) {
            increaseHealth(Antidote.EFFECT);
        } else {
            /*
             * There is 20% chance that this antidote has double effect, and 80% chance
             * that will damage the player.
             */
            int effect = Math.random() < Antidote.CURE_CHANCE ? Antidote.EFFECT * 2
                    : -Antidote.EFFECT;
            increaseHealth(effect);
        }

        destroyItem(antidote);
    }

    /**
     * This method let the player to light up a torch.
     * 
     * Note this method doesn't check if the player has this torch in inventory. This
     * should be done before this method is called.
     * 
     * @param torch
     */
    public void lightUpTorch(Torch torch) {
        // dead man should do nothing
        if (!isAlive) {
            return;
        }

        if (!torch.isFlaming()) {
            torch.setIsFlaming(true);
        }

        // TODO do something about visibility
    }

    /**
     * This method breaks the specified item in player's inventory.
     * 
     * @param destroyable
     * @return
     */
    public boolean destroyItem(Destroyable destroyable) {
        // dead man should do nothing
        if (!isAlive) {
            return false;
        }

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

        if (isInventoryFull()) {
            return false;
        }

        return inventory.add(item);

    }

    /**
     * This method throw away an item.
     *
     * @param item
     * @return
     */
    public boolean throwAwayItem(Item item) {
        // dead man should do nothing
        if (!isAlive) {
            return false;
        }

        // the player has to have this object first
        if (!hasItem(item)) {
            return false;
        }

        return inventory.remove(item);

    }

    /**
     * Let the player give an item to another player
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

        // 0. if it's not tradable, you cannot give it to others.
        if (!(item instanceof Tradable)) {
            return false;
        }

        // TODO 1. should check if two players are within a certain distance

        // 2. check if this item belongs to current player.
        if (!hasItem(item)) {
            return false;
        }

        // 3. check if the other player has space for the item.
        if (other.isInventoryFull()) {
            return false;
        }

        // OK, now we can give it to the other player.
        throwAwayItem(item);
        other.pickUpItem(item);
        return true;

    }

    private boolean hasItem(Item pickUp) {
        // dead man should do nothing
        if (!isAlive) {
            return false;
        }

        return inventory.contains(pickUp);
    }

    private boolean isInventoryFull() {
        return inventory.size() >= INVENTORY_SIZE;
    }

    /**
     * This method let the player try to unlock a room. If the player has the key to open
     * this room, then it is unlocked. Notice door unlocked != room entered.
     * 
     * @param room
     * @return
     */
    public boolean tryUnlockDoor(Room room) {
        // dead man should do nothing
        if (!isAlive) {
            return false; // gosh this should never happen!
        }

        for (Item item : inventory) {
            if (item instanceof Key) {
                Key key = (Key) item;
                if (key.getKeyID() == room.getKeyID()) {
                    room.setLocked(false);
                    inventory.remove(key);
                    return true;
                }
            }
        }

        // TODO should check if the player is standing in front of the room

        return false;
    }

    /**
     * This method let the player try to enter the room.
     * 
     * @param room
     * @return
     */
    public boolean tryEnterRoom(Room room) {
        // dead man should do nothing
        if (!isAlive) {
            return false; // gosh this should never happen!
        }

        // if the room is locked, fail.
        if (room.isLocked()) {
            return false;
        }

        // OK, let move player into the room
        position = room.getExit();
        return true;
    }

    public boolean tryExitRoom(Room room, RoomEntrance entrance) {
        // dead man should do nothing
        if (!isAlive) {
            return false; // gosh this should never happen!
        }

        /*
         * currently, as long as the player is inside a room, the room cannot be locked
         * for sure. The reason is that the key to unlock the room is never going to be
         * located inside the room. The following check is redundant because it will
         * always be false.
         * 
         * If we decide to spawn player inside rooms (the key has to be inside the room),
         * the following check is not redundant any more.
         */
        // if the room is locked, fail.
        if (room.isLocked()) {
            return false;
        }

        position = entrance;
        return true;
    }

    /**
     * This method let the player try to unlock a chest. If the player has the key to open
     * this chest, then it is unlocked. Notice chest unlocked != items inside picked up.
     * 
     * @param chest
     * @return
     */
    public boolean tryUnlockChest(Chest chest) {
        // dead man should do nothing
        if (!isAlive) {
            return false; // gosh this should never happen!
        }

        for (Item item : inventory) {
            if (item instanceof Key) {
                Key key = (Key) item;
                if (key.getKeyID() == chest.getKeyID()) {
                    chest.setLocked(false);
                    inventory.remove(key);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This method let the player try to take items from the chest in front. If the
     * player's inventory can contain all items, he will take them all; otherwise he will
     * take as many as he can until his inventory is full.
     * 
     * @param chest
     * @return --- true if he has taken at least one item from the chest, or false if he
     *         has taken none from the chest.
     */
    public synchronized boolean tryTakeItemsInChest(Chest chest) {
        // dead man should do nothing
        if (!isAlive) {
            return false; // gosh this should never happen!
        }

        // if the chest is locked, the player cannot take item from it
        if (chest.isLocked()) {
            return false;
        }

        boolean tookAtLeastOne = false;

        List<Item> loot = chest.getLoot();
        Iterator<Item> itr = loot.iterator();
        while (itr.hasNext()) {
            if (!isInventoryFull()) {
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

}
