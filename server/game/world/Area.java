package server.game.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
 *
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878
 *
 */
public class Area {

	private int width;
	private int height;

	protected MapElement[][] board;

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
		this.board = board;
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

	public MapElement[][] getBoard(){
		return this.board;
	}

	/**
	 * let this area remember where empty positions are, so that player can be spawned
	 * from one of them.
	 */
	public void registerPortals() {
		for (int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[0].length; col++) {
				if(!(board[row][col] instanceof Obstacle)) {
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

		return board[y][x];
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


	/*
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();			//turned off to test map save contents. Can be turned on afterwards

		for (MapElement[] row : board) {
			for (MapElement col : row) {
				if(col != null)
					sb.append(col.toString());
				else
					sb.append("f");	//f for free space
			}
			sb.append("\n");
		}

		return sb.toString();
	}*/

	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){
		StringBuffer b = new StringBuffer("");
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[0].length; j++){
				b.append(board[i][j]);
			}
		}
		return b.toString();
	}
	
	public int getWidth() {
		return width;
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
		//not needed at this stage of testing
		/*if (exits == null) {
			if (other.exits != null)
				return false;
		} else if (!exits.equals(other.exits))
			return false;	
		 */	
		if (height != other.height)
			return false;

		if (playerPortals == null) {
			if (other.playerPortals != null)
				return false;
		} else if (!playerPortals.equals(other.playerPortals))
			return false;
		if (width != other.width)
			return false;
		
		
		List<Item> a =((Chest)board[0][2]).getLoot();
		List<Item> n =((Chest)other.getBoard()[0][2]).getLoot();
		for(int i = 0; i < a.size(); i ++){
			if(!a.get(i).equals(n.get(i)))
				return false;
		}
		return true;
	}

}
