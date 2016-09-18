package server.game.player;

/**
 * This enumeration class represents four directions.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public enum Direction {

    North, East, South, West;

    /**
     * Get the the left of current direction.
     * 
     * @return
     */
    public Direction left() {
        switch (this) {
        case East:
            return North;
        case North:
            return West;
        case South:
            return East;
        case West:
            return South;
        default:
            return null; // dead code
        }
    }

    /**
     * Get the the right of current direction.
     * 
     * @return
     */
    public Direction right() {
        switch (this) {
        case East:
            return South;
        case North:
            return East;
        case South:
            return West;
        case West:
            return North;
        default:
            return null; // dead code
        }
    }

    public static Direction randomDirection() {
        int i = (int) (Math.random() * 4);
        switch (i) {
        case 0:
            return South;
        case 1:
            return East;
        case 2:
            return West;
        case 3:
            return North;
        default:
            return null; // dead code
        }
    }

    public char getChar() {
        switch (this) {
        case East:
            return '>';
        case North:
            return '^';
        case South:
            return 'v';
        case West:
            return '<';
        default:
            return ' '; // dead code
        }
    }

}
