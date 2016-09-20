package server.game.player;

/**
 * This class represents a set of geographical information for a player. It contains a set
 * of (x, y) coordinates, and the id number of area.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Position {
    /**
     * X coordinate.
     */
    public final int x;
    /**
     * Y coordinate.
     */
    public final int y;
    /**
     * Which area is this position.
     */
    public final int areaId;

    /**
     * Constructor
     * 
     * @param x
     * @param y
     * @param areaId
     */
    public Position(int x, int y, int areaId) {
        this.x = x;
        this.y = y;
        this.areaId = areaId;
    }

    public Position frontPosition(Direction direction) {
        switch (direction) {
        case East:
            return new Position(x + 1, y, areaId);
        case North:
            return new Position(x, y - 1, areaId);
        case South:
            return new Position(x, y + 1, areaId);
        case West:
            return new Position(x - 1, y, areaId);
        default:
            return null; // dead code
        }
    }

    public Position backPosition(Direction direction) {
        switch (direction) {
        case East:
            return new Position(x - 1, y, areaId);
        case North:
            return new Position(x, y + 1, areaId);
        case South:
            return new Position(x, y - 1, areaId);
        case West:
            return new Position(x + 1, y, areaId);
        default:
            return null; // dead code
        }
    }

    public Position leftPosition(Direction direction) {
        switch (direction) {
        case East:
            return new Position(x, y - 1, areaId);
        case North:
            return new Position(x - 1, y, areaId);
        case South:
            return new Position(x + 1, y, areaId);
        case West:
            return new Position(x, y + 1, areaId);
        default:
            return null; // dead code
        }
    }

    public Position rightPosition(Direction direction) {
        switch (direction) {
        case East:
            return new Position(x, y + 1, areaId);
        case North:
            return new Position(x + 1, y, areaId);
        case South:
            return new Position(x - 1, y, areaId);
        case West:
            return new Position(x, y - 1, areaId);
        default:
            return null; // dead code
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + areaId;
        result = prime * result + x;
        result = prime * result + y;
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
        Position other = (Position) obj;
        if (areaId != other.areaId)
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
