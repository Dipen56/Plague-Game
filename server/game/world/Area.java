package server.game.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import server.game.Game;
import server.game.player.Direction;
import server.game.player.Player;
import server.game.player.Position;

/**
 * This class represents the world map.
 *
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878
 *
 */
public class Area {
    /**
     * Width of the map.
     */
    private int width;
    
    /**
     * Height of the map.
     */
    private int height;
    
    /**
     * The map itself.
     */
    protected MapElement[][] map;
    
    /**
     * Each area has a unique ID number, which can be used for locating player.
     */
    private int areaId;
    
    /**
     * Empty position, which can be used to spawn players.
     */
    private List<int[]> playerPortals = new ArrayList<>();

    /**
     * Constructor
     *
     * This the proper constructor we use, although the argument could be different. The
     * basic idea is we read in a file, parse it, and construct the world with what's in
     * the file. The file could be XML, or simple text file. It's still to be decided.
     *
     * @param filename
     */
    public Area(String filename) {

        // FIXME: Not useful yet

    }

    /**
     * Constructor used only for text client. Can be removed after testing.
     * @param board
     * @param Area id.
     * @param A list of player spawn locations.
     */
    public Area(MapElement[][] board, int areaID) {
        this.map = board;
        this.width = board[0].length;
        this.height = board.length;
        this.areaId = areaID;
    }
    
    /**
     * Constructor used in data storage.
     *
     * @param board
     * @param Area id.
     * @param A list of player spawn locations.
     */
    public Area(MapElement[][] board, int areaID, List<int[]> playerPortals) {
        this.map = board;
        this.width = board[0].length;
        this.height = board.length;
        this.areaId = areaID;
        this.playerPortals = playerPortals;
    }

    public List<int[]> getPlayerPortals(){
    	return this.playerPortals;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return this.height;
    }

    public MapElement[][] getMap() {
        return this.map;
    }

    public int getAreaID(){
    	return this.areaId;
    }
    
    /**
     * let this area remember where empty positions are, so that player can be spawned
     * from one of them.
     */
    public void registerPortals() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col] instanceof GroundSpace) {
                    playerPortals.add(new int[] { col, row });
                }
            }
        }
    }

    /**
     * Get a random empty position to spawn a player.
     *
     * @param game
     * @return --- an empty position to spawn player. If this area is so occupied that
     *         there is no empty space, null will be returned.
     */
    public synchronized Position getPlayerSpawnPos(Game game) {
        Collections.shuffle(playerPortals);
        for (int[] e : playerPortals) {
            Position p = new Position(e[0], e[1], areaId, Direction.randomDirection());
            if (!game.isOccupiedByOtherPlayer(p)) {
                return p;
            }
        }
        return null;
    }

    /**
     * This method checks whether the given position is in this area.
     * 
     * @param position
     * @return --- true if the given position is in area; or false if it's out of area
     *         boundary or it's in other area.
     */
    public boolean isInBoard(Position position) {
        if (position == null) {
            return false;
        }

        if (position.areaId != areaId) {
            return false;
        }

        if (position.x < 0 || position.x >= width || position.y < 0
                || position.y >= height) {
            return false;
        }

        return true;
    }

    /**
     * Get MapElement at coordinate (x, y)
     *
     * @param x
     * @param y
     * @return --- the ground type at coordinate (x, y); or null if the given (x, y) is
     *         out of current map.
     */
    public MapElement getMapElementAt(int x, int y) {
        if (x < 0 || x >= width) {
            return null;
        }

        if (y < 0 || y >= height) {
            return null;
        }

        return map[y][x];
    }

    /**
     * Get the MapElement type in front of the player.
     * 
     * @param player
     * @return --- the MapElement type in front of the player; or null if that place is
     *         out of current map.
     */
    public MapElement getFrontMapElement(Player player) {
        Position currentPos = player.getPosition();
        int x = currentPos.x;
        int y = currentPos.y;
        Direction direction = player.getDirection();
        MapElement frontMapElement;

        switch (direction) {
        case East:
            frontMapElement = getMapElementAt(x + 1, y);
            break;
        case North:
            frontMapElement = getMapElementAt(x, y - 1);
            break;
        case South:
            frontMapElement = getMapElementAt(x, y + 1);
            break;
        case West:
            frontMapElement = getMapElementAt(x - 1, y);
            break;
        default:
            // this should not happen
            frontMapElement = null;
        }

        return frontMapElement;
    }

    /**
     * Get the MapElement type behind the player.
     *
     * @param player
     * @return --- the MapElement type behind the player; or null if that space is out of
     *         current map.
     */
    public MapElement getBackMapElement(Player player) {
        Position currentPos = player.getPosition();
        int x = currentPos.x;
        int y = currentPos.y;
        Direction direction = player.getDirection();
        MapElement backPos;

        switch (direction) {
        case East:
            backPos = getMapElementAt(x - 1, y);
            break;
        case North:
            backPos = getMapElementAt(x, y + 1);
            break;
        case South:
            backPos = getMapElementAt(x, y - 1);
            break;
        case West:
            backPos = getMapElementAt(x + 1, y);
            break;
        default:
            // this should not happen
            backPos = null;
        }

        return backPos;
    }

    /**
     * Get the MapElement type on the left of the player.
     *
     * @param player
     * @return --- the MapElement type on the left of the player; or null if that space is
     *         out of current map.
     */
    public MapElement getLeftMapElement(Player player) {
        Position currentPos = player.getPosition();
        int x = currentPos.x;
        int y = currentPos.y;
        Direction direction = player.getDirection();
        MapElement leftPos;

        switch (direction) {
        case East:
            leftPos = getMapElementAt(x, y - 1);
            break;
        case North:
            leftPos = getMapElementAt(x - 1, y);
            break;
        case South:
            leftPos = getMapElementAt(x + 1, y);
            break;
        case West:
            leftPos = getMapElementAt(x, y + 1);
            break;
        default:
            // this should not happen
            leftPos = null;
        }

        return leftPos;
    }

    /**
     * Get the MapElement type on the right of the player.
     *
     * @param player
     * @return --- the MapElement type on the right of the player; or null if that space
     *         is out of current map.
     */
    public MapElement getRightMapElement(Player player) {
        Position currentPos = player.getPosition();
        int x = currentPos.x;
        int y = currentPos.y;
        Direction direction = player.getDirection();
        MapElement rightPos;

        switch (direction) {
        case East:
            rightPos = getMapElementAt(x, y + 1);
            break;
        case North:
            rightPos = getMapElementAt(x + 1, y);
            break;
        case South:
            rightPos = getMapElementAt(x - 1, y);
            break;
        case West:
            rightPos = getMapElementAt(x, y - 1);
            break;
        default:
            // this should not happen
            rightPos = null;
        }

        return rightPos;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + areaId;
        result = prime * result + height;
        result = prime * result + Arrays.deepHashCode(map);
        result = prime * result
                + ((playerPortals == null) ? 0 : playerPortals.hashCode());
        result = prime * result + width;
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
        Area other = (Area) obj;
        if (areaId != other.areaId)
            return false;
        if (height != other.height)
            return false;
        if (!Arrays.deepEquals(map, other.map))
            return false;
        if (playerPortals == null) {
            if (other.playerPortals != null)
                return false;
        } else if (!playerPortals.equals(other.playerPortals))
            return false;
        if (width != other.width)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (MapElement[] row : map) {
            for (MapElement col : row) {
            	if(col != null)
            		sb.append(col.toString());
            	else
            		sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}
