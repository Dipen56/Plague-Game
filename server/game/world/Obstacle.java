package server.game.world;

/**
 * This class represents an unwalkable position on map, like a rock, or a tree.
 *
 * FOR TEAM
 *
 * I don't think there is any point to make separate classes like Rock, Tree, or others. I
 * mean they essentially have the same function: 1. displaying itself, 2. blocking players
 * from walking into itself. So a tree object can be constructed as new Obstacle(x, y,
 * "Weird looking tree", an-image-of-tree), then rock can be constructed as new
 * Obstacle(x, y, "Reaaaaally hard rock", an-image-of-rock).
 *
 * If, in later stages, we have need to do separated classes for rocks or other stuff,
 * I'll make one then.
 *
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Obstacle implements MapElement {

    protected String description;

    public Obstacle(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
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
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "o";
    }
}
