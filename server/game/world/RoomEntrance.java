package server.game.world;

import server.game.player.Direction;

/**
 * This class represents a special Square only standing on which the player could interact
 * with the door and try to enter the room.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class RoomEntrance extends GroundSquare {

    private Room room;

    /**
     * standing on this tile, with which direction could the player enter that room
     */
    private Direction direction;

    public RoomEntrance(int x, int y, Area area, Room room, Direction direction) {
        super(x, y, area);
        this.room = room;
        this.direction = direction;
    }

    public Room getRoom() {
        return room;
    }

    public Direction getFacingDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "E";
    }
}
