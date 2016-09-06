package game.items;

import javax.swing.ImageIcon;

import game.ClockThread;
import game.player.Player;
import game.player.Virus;

/**
 * This class represents the antidote. An antidote is put in player's antidote inventory.
 * 
 * @author Hector
 *
 */
public class Antidote extends PickUps implements Breakable, Tradable {

    /**
     * How long this antidote is going to prolong player's life if it's correct type. This
     * number is set to 2 hours (equivalent 2 minutes in game).
     */
    private static final int EFFECT = 2 * 60;

    private static final float CURE_CHANCE = 0.2f;
    private Virus virus;

    public Antidote(String description, ImageIcon sprite, Virus virus) {
        super(description, sprite);
        this.virus = virus;
    }

    public Virus getVirus() {
        return virus;
    }

    /**
     * This method let the player to drink a potion of antidote. If the antidote's type
     * matches the player's virus, then it prolongs the player's life. Otherwise it has
     * 80% chance to lessen the player's life for the same amount, and 20% chance to
     * prolong twice the amount of a right antidote.
     * 
     * @param antidote
     */
    public void drink(Player player) {
        if (player.getVirus().equals(virus)) {
            player.increaseHealth(EFFECT);
        } else {
            /*
             * There is 20% chance that this antidote has double effect, and 80% chance
             * that will damage the player.
             */
            int effect = Math.random() < CURE_CHANCE ? EFFECT * 2 : -EFFECT;
            player.increaseHealth(effect);
        }
    }

}
