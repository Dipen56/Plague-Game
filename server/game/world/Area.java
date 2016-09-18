package server.game.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
<<<<<<< HEAD
import java.util.List;

import server.game.Game;
import server.game.player.Direction;
import server.game.player.Player;
import server.game.player.Position;

/**
 * This class represents the world map.
=======
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import server.game.Game;
import server.game.items.Item;
import server.game.player.Direction;
import server.game.player.Player;

/**
 * This class represents an area, which could be either the open world, or a
 * closed space like rooms.
>>>>>>> master
 *
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878
 *
 */
public class Area {
<<<<<<< HEAD
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
    private List<Position> playerPortals = new ArrayList<>();

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
     * Constructor used in text-based UI. Probably will be discarded.
     *
     * @param width
     * @param height
     * @param board
     */
    public Area(MapElement[][] board, int areaID) {
        this.map = board;
        this.width = board[0].length;
        this.height = board.length;
        this.areaId = areaID;
    }

    public MapElement[][] getMap() {
        return this.map;
    }

    /**
     * let this area remember where empty positions are, so that player can be spawned
     * from one of them.
     */
    public void registerPortals() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col] instanceof GroundSpace) {
                    playerPortals.add(new Position(col, row, areaId));
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
    public Position getPlayerSpawnPos(Game game) {
        List<Position> portalList = new ArrayList<>(playerPortals);
        Collections.shuffle(portalList);

        for (Position p : portalList) {
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
                sb.append(col.toString());
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    // =======The following methods will be deleted =================
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return this.height;
    }
=======
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
	 * Empty position, which can be used to spawn players.
	 */
	private List<Position> playerPortals = new ArrayList<>();

	/**
	 * All exits from this area.
	 */
	protected Set<TransitionSpace> exits;

	/**
	 * Constructor
	 *
	 * FOR Team: This the proper constructor we use, although the argument could be
	 * different. The basic idea is we read in a file, parse it, and construct the world
	 * with what's in the file. The file could be XML, or simple text file. It's still to
	 * be decided.
	 *
	 * @param filename
	 */
	public Area(String filename) {

		// FIXME: Not useful yet

		// List<String> lines = null;
		// try {
		// // List<String> lines = Files.readAllLines(Paths.get(filename),
		// // StandardCharsets.UTF_8);
		// lines = Files.readAllLines(Paths.get(PATH), StandardCharsets.UTF_8);
		// } catch (IOException e) {
		// System.out.println("[IO Exception] " + e.getMessage());
		// e.printStackTrace();
		// System.exit(1);
		// }
		//
		// height = lines.size();
		// width = -1;
		// board = new Position[height][width];
		//
		// for (int y = 0; y < lines.size(); y++) {
		// String line = lines.get(y);
		//
		// // sanity check
		// if (width == -1) {
		// width = line.length();
		// } else if (width != line.length()) {
		// throw new IllegalArgumentException("Input file \"" + filename
		// + "\" is malformed; line " + lines.size() + " incorrect width.");
		// }
		//
		// for (int x = 0; x < line.length(); x++) {
		// switch (line.charAt(x)) {
		// case ' ':
		// board[y][x] = new GroundSquare(x, y);
		// break;
		//
		// /*
		// * lower case letter: obstacles.
		// *
		// * 'c': Chest. 'r': Rock. 't': Tree. 'a': Table.
		// */
		// case 'c':
		// // TODO
		// // board[y][x] = new Chest(x, y, "Chest", null, , isLocked, loot);
		// break;
		//
		// // case ' ':
		// // board[y][x] = new GroundSquare(x, y);
		// // break;
		// // case ' ':
		// // board[y][x] = new GroundSquare(x, y);
		// // break;
		// // case ' ':
		// // board[y][x] = new GroundSquare(x, y);
		// // break;
		// // case ' ':
		// // board[y][x] = new GroundSquare(x, y);
		// // break;
		// // case ' ':
		// // board[y][x] = new GroundSquare(x, y);
		// // break;
		// // case ' ':
		// // board[y][x] = new GroundSquare(x, y);
		// // break;
		// // case ' ':
		// // board[y][x] = new GroundSquare(x, y);
		// // break;
		//
		// }Game
		//
		// }
		// }

		// playerPortals = new ArrayList<>();
		//

	}

	/**
	 * Constructor used in test. Probably will be discarded.
	 *
	 * @param width
	 * @param height
	 * @param board
	 */
	public Area(MapElement[][] board) {
		this.map = board;
		this.width = board[0].length;
		this.height = board.length;
		this.exits = new HashSet<>();
		//fills the exits set
		for(MapElement[] row : board){
			for(MapElement m : row){
				if(m instanceof TransitionSpace){
					this.exits.add((TransitionSpace)m);
				}
			}
		}
	}

	public Set<TransitionSpace> getExits(){
		return this.exits;
	}

	public int getHeight(){
		return this.height;
	}

	public MapElement[][] getMap(){
		return this.map;
	}

	/**
	 * let this area remember where empty positions are, so that player can be spawned
	 * from one of them.
	 */
	public void registerPortals() {
		for (int row = 0; row < map.length; row++) {
			for(int col = 0; col < map[0].length; col++) {
				if(!(map[row][col] instanceof Obstacle)) {
					playerPortals.add(new Position(col,row));
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
	public Position getPlayerSpawnPos(Game game) {

		List<Position> portalList = new ArrayList<>(playerPortals);

		Collections.shuffle(portalList);

		for (Position p : portalList) {
			if (game.isEmptyPosition(p)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Get MapElement at coordinate (x, y)
	 *
	 * @param x
	 * @param y
	 * @return --- the ground type at coordinate (x, y); or null if the given (x, y) is
	 *         out of current map.
	 */
	public MapElement getMapElementAtIndex(int x, int y) {
		if (x < 0 || x >= width) {
			return null;
		}

		if (y < 0 || y >= height) {
			return null;
		}

		return map[y][x];
	}

	/**
	 * Get the ground type in front of the player.
	 *
	 * @param player
	 * @return A mapElement
	 */
	public Position getFrontPos(Player player) {

		Position currentPos = player.getPosition();
		Direction direction = player.getDirection();
		Position forwardPos;

		switch (direction) {
		case East:
			forwardPos = new Position(currentPos.x + 1, currentPos.y);
			break;
		case North:
			forwardPos = new Position(currentPos.x, currentPos.y - 1);
			break;
		case South:
			forwardPos = new Position(currentPos.x, currentPos.y + 1);
			break;
		case West:
			forwardPos = new Position(currentPos.x - 1, currentPos.y);
			break;
		default:
			// this should not happen
			forwardPos = null;
			break;
		}

		return forwardPos;
	}

	/**
	 * Get the ground type behind the player.
	 *
	 * @param player
	 * @return --- the ground type behind the player; or null if that position is out of
	 *         current map.
	 */
	public Position getBackPos(Player player) {

		Position currentPos = player.getPosition();
		Direction direction = player.getDirection();
		Position backPos;

		switch (direction) {
		case East:
			backPos = new Position(currentPos.x - 1, currentPos.y);
			break;
		case North:
			backPos = new Position(currentPos.x, currentPos.y + 1);
			break;
		case South:
			backPos = new Position(currentPos.x, currentPos.y - 1);
			break;
		case West:
			backPos = new Position(currentPos.x + 1, currentPos.y);
			break;
		default:
			// this should not happen
			backPos = null;
			break;
		}

		return backPos;
	}

	/**
	 * Get the ground type on the left of the player.
	 *
	 * @param player
	 * @return --- the ground type on the left of the player; or null if that position is
	 *         out of current map.
	 */
	public Position getLeftPos(Player player) {

		Position currentPos = player.getPosition();
		Direction direction = player.getDirection();
		Position leftPos;

		switch (direction) {
		case East:
			leftPos = new Position(currentPos.x, currentPos.y - 1);
			break;
		case North:
			leftPos = new Position(currentPos.x - 1, currentPos.y);
			break;
		case South:
			leftPos = new Position(currentPos.x + 1, currentPos.y);
			break;
		case West:
			leftPos = new Position(currentPos.x, currentPos.y + 1);
			break;
		default:
			// this should not happen
			leftPos = null;
			break;
		}

		return leftPos;
	}

	/**
	 * Get the ground type on the right of the player.
	 *
	 * @param player
	 * @return --- the ground type on the right of the player; or null if that position is
	 *         out of current map.
	 */
	public Position getRightPos(Player player) {

		Position currentPos = player.getPosition();
		Direction direction = player.getDirection();
		Position rightPos;

		switch (direction) {
		case East:
			rightPos = new Position(currentPos.x, currentPos.y + 1);
			break;
		case North:
			rightPos = new Position(currentPos.x + 1, currentPos.y);
			break;
		case South:
			rightPos = new Position(currentPos.x - 1, currentPos.y);
			break;
		case West:
			rightPos = new Position(currentPos.x, currentPos.y - 1);
			break;
		default:
			// this should not happen
			rightPos = null;
			break;
		}

		return rightPos;
	}

	public void playerMoveTo(Player player, GroundSquare position) {

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();	

		for (MapElement[] row : map) {
			for (MapElement col : row) {
				if(col != null)
					sb.append(col.toString());
				else
					sb.append("f");	//f for free space
			}
			sb.append("\n");
		}

		return sb.toString();
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		Class objclass = obj.getClass();
		boolean b = obj instanceof World;

		if(getClass() != obj.getClass()){
			return false;
		}
		
		Area other = (Area) obj;
		if (height != other.height)
			return false;

		if (playerPortals == null) {
			if (other.playerPortals != null)
				return false;
		} else if (!playerPortals.equals(other.playerPortals))
			return false;
		if (width != other.width)
			return false;
		
		
		List<Item> a =((Chest)map[0][2]).getLoot();
		List<Item> n =((Chest)other.getMap()[0][2]).getLoot();
		for(int i = 0; i < a.size(); i ++){
			if(!a.get(i).equals(n.get(i)))
				return false;
		}
		return true;
	}
>>>>>>> master

}
