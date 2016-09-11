package game.world;

import game.player.Direction;

/**
 * This class represents a special Square only standing on which the player could interact
 * with the door and try to exit the room.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class RoomExit extends GroundSquare {

    private RoomEntrance entrance;
    private Direction direction;

    public RoomExit(int x, int y, Area area, RoomEntrance entrance, Direction direction) {
        super(x, y, area);
        this.entrance = entrance;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "E";
    }

    public Direction getFacingDirection() {
        return direction;
    }

}
