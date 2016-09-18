package server.game.items;

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
    /**
     * If this antidote is of a wrong type, the player can be cured by this chance (a
     * relatively low chance).
     */
    public static final float CURE_CHANCE = 0.2f;

    private Virus virus;

    public Antidote(String description, Virus virus) {
        super(description);
        this.virus = virus;
    }

    public Virus getVirus() {
        return virus;
    }

    @Override
    public String toString() {
        return super.toString() + " It has a label: " + virus.toString() + ".";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        Antidote other = (Antidote) obj;
        if (virus != other.virus)
            return false;
        return true;
    }

}
