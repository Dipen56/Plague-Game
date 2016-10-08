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

    /**
     * What type of virus does this antidote used for?
     */
    private Virus virus;

    /**
     * Constructor, give the antidote a description and virus type.
     *
     * @param description
     *            --- description
     * @param virus
     *            --- What type of virus does this antidote used for
     */
    public Antidote(String description, Virus virus) {
        super(description);
        this.virus = virus;
    }

    /**
     * Get the virus type that the antidote can be used for.
     *
     * @return --- the virus type
     */
    public Virus getVirus() {
        return virus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((virus == null) ? 0 : virus.hashCode());
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
        Antidote other = (Antidote) obj;
        if (virus != other.virus)
            return false;
        return true;
    }

}
