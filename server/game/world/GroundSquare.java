package server.game.world;

/**
 * This class represents a position which player can walk into and stand on.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class GroundSquare extends Position {

    private Area area;

    public GroundSquare(int x, int y, Area area) {
        super(x, y);
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

    @Override
    public String toString() {
        return " ";
    }

}
