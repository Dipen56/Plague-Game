package server.game.world;

/**
 * This class represents an unwalkable position on map, like a rock, or a tree.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Obstacle implements MapElement {

    /**
     * The char representation of this obstacle.
     * 
     * <p>
     * <li>'T': tree
     * <li>'R': chest
     */
    protected String charRep;

    /**
     * Constructor
     * 
     * @param charRep
     *            --- The char representation of this obstacle.
     */
    public Obstacle(String charRep) {
        this.charRep = charRep;
    }

    /**
     * Get the char representation of this obstacle.
     * 
     * @return
     */
    public String getDescription() {
        return charRep;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((charRep == null) ? 0 : charRep.hashCode());
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
        Obstacle other = (Obstacle) obj;
        if (charRep == null) {
            if (other.charRep != null)
                return false;
        } else if (!charRep.equals(other.charRep))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return charRep;
    }
}
