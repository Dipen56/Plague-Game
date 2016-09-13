package server.game.items;

import java.awt.image.BufferedImage;

import server.game.player.Virus;

/**
 * This class represents the antidote. An antidote is put in player's antidote inventory.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Antidote extends Item implements Destroyable, Tradable {

    /**
     * How long this antidote is going to prolong player's life if it's correct type. This
     * number is set to 2 hours (equivalent 2 minutes in game).
     */
    public static final int EFFECT = 2 * 60;
    public static final float CURE_CHANCE = 0.2f;

    private Virus virus;

    public Antidote(String description, BufferedImage sprite, Virus virus) {
        super(description, sprite);
        this.virus = virus;
    }

    public Virus getVirus() {
        return virus;
    }

    @Override
    public String toString() {
        return super.toString() + " It has a label: " + virus.toString() + ".";
    }

}
