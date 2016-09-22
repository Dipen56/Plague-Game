package server.game.world;

public class GroundSpace implements MapElement {

    @Override
    public boolean equals(Object obj) {
        // We want every GroundSpace equals to each other.
        return true;
    }

    @Override
    public int hashCode() {
        // We want every GroundSpace has same hash code.
        return 31;
    }

    @Override
    public String toString() {
        return " ";
    }
}
