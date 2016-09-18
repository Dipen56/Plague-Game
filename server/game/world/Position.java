package server.game.world;

/**
 * This abstract class represents a position on map.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Position {
	/**
	 * X coordinate of the object located with this position.
	 */
    public final int x;
    /**
	 * Y coordinate of the object located with this position.
	 */
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
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
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}


	public String toString(){
		StringBuffer b = new StringBuffer("");
		b.append("Position: ");
		b.append(x +" ");
		b.append(y + " ");
		return b.toString();
	}
}
