package game.player;

import java.util.ArrayList;
import java.util.List;

import game.ClockThread;
import game.GameError;
import game.items.Antidote;
import game.items.Breakable;
import game.items.Item;
import game.items.PickUps;
import game.items.Tradable;

/**
 * This class represents a player.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Player {

    private static final int MAX_ANTIDOTES = 6;
    private static final int MAX_ITEMS = 6;
    /**
     * The max health (time left) of player. This number is set to one day, 24 hours
     * (equivalent to 24 minutes in game). After 24 hours, the player will die.
     */
    private static final int MAX_HEALTH = 24 * 60;

    private final int uID;
    private final String name;
    private final Virus virus;
    private int health;
    private boolean isAlive;
    private List<Item> inventory;
    private List<Antidote> antidotes;

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
        antidotes = new ArrayList<>();
    }

    public Virus getVirus() {
        return virus;
    }

    public boolean isAlive() {
        return isAlive;
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
        health += effect;
        if (health <= 0) {
            isAlive = false;
        }
    }

    /**
     * This method breaks the specified item in player's inventory.
     * 
     * @param breakable
     * @return
     */
    public boolean breakItem(Breakable breakable) {
        if (!inventory.contains(breakable)) {
            throw new GameError(
                    "Player " + name + " (uID: " + uID + ") doesn't own this item.");
        }

        return inventory.remove(breakable);
    }

    /**
     * This method let the player to pick up an item or antidote.
     * 
     * @param item
     * @return
     */
    public boolean pickUpObject(PickUps pickUp) {
        if (pickUp instanceof Antidote) {
            if (antidotes.size() < MAX_ANTIDOTES) {
                return antidotes.add((Antidote) pickUp);
            } else {
                return false;
            }
        } else if (pickUp instanceof Item) {
            if (inventory.size() < MAX_ITEMS) {
                return inventory.add((Item) pickUp);
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * This method throw away an item or antidote.
     *
     * @param item
     * @return
     */
    public boolean throwAwayObject(PickUps pickUp) {
        // the player has to have this object first
        if (!hasItem(pickUp)) {
            return false;
        }

        if (pickUp instanceof Antidote) {
            return antidotes.remove((Antidote) pickUp);
        } else if (pickUp instanceof Item) {
            return inventory.remove((Item) pickUp);
        }

        return false;
    }

    public boolean giveItemTo(Player other, PickUps pickUp) {
        // 0. if it's not tradable, you cannot give it to others.
        if (!(pickUp instanceof Tradable)) {
            return false;
        }

        // TODO 1. should check if two players are within a certain distance

        // 2. check if this item belongs to current player.
        if (!this.hasItem(pickUp)) {
            return false;
        }

        // 3. check if the other player has space for the item.
        if (!(other.hasRoomForItem(pickUp))) {
            return false;
        }

        // ok, now we can give it to the other player.

        return false;
    }

    private boolean hasItem(PickUps pickUp) {
        if (pickUp instanceof Antidote) {
            return antidotes.contains(pickUp);
        } else if (pickUp instanceof Item) {
            inventory.contains(pickUp);
        }

        return false;
    }

    private boolean hasRoomForItem(PickUps pickUp) {
        if (pickUp instanceof Antidote) {
            return antidotes.size() < MAX_ANTIDOTES;
        } else if (pickUp instanceof Item) {
            return inventory.size() < MAX_ITEMS;
        }
        throw new GameError("This item is not supported in game.");
    }
}
