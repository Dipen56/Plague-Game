package server.game.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import server.game.Game;
import server.game.player.Direction;
import server.game.player.Player;

/**
 * This abstract class represents an area, which could be either the open world, or a
 * closed space like rooms.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public abstract class Area {

    private int width;
    private int height;

    protected Position[][] board;

    /**
     * Empty position, which can be used to spawn players.
     */
    private Set<GroundSquare> playerPortals;

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
        // }
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
    public Area(Position[][] board) {
        this.board = board;
        this.width = board[0].length;
        this.height = board.length;
        playerPortals = new HashSet<>();
    }

    /**
     * let this area remember where empty positions are, so that player can be spawned
     * from one of them.
     */
    public void registerPortals() {
        for (Position[] row : board) {
            for (Position col : row) {
                if (col instanceof GroundSquare) {
                    playerPortals.add((GroundSquare) col);
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
    public GroundSquare getPlayerSpawnPos(Game game) {

        List<GroundSquare> portalList = new ArrayList<>(playerPortals);

        Collections.shuffle(portalList);

        for (GroundSquare gs : portalList) {
            if (game.isEmptyPosition(gs)) {
                return gs;
            }
        }

        return null;
    }

    /**
     * Get the ground type at coordinate (x, y)
     * 
     * @param x
     * @param y
     * @return --- the ground type at coordinate (x, y); or null if the given (x, y) is
     *         out of current map.
     */
    public Position getPosAt(int x, int y) {
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
     * @return --- the ground type in front of the player; or null if that position is out
     *         of current map.
     */
    public Position getFrontPos(Player player) {

        GroundSquare currentPos = player.getPosition();
        Direction direction = player.getDirection();
        Position forwardPos;

        switch (direction) {
        case East:
            forwardPos = getPosAt(currentPos.x + 1, currentPos.y);
            break;
        case North:
            forwardPos = getPosAt(currentPos.x, currentPos.y - 1);
            break;
        case South:
            forwardPos = getPosAt(currentPos.x, currentPos.y + 1);
            break;
        case West:
            forwardPos = getPosAt(currentPos.x - 1, currentPos.y);
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

        GroundSquare currentPos = player.getPosition();
        Direction direction = player.getDirection();
        Position backPos;

        switch (direction) {
        case East:
            backPos = getPosAt(currentPos.x - 1, currentPos.y);
            break;
        case North:
            backPos = getPosAt(currentPos.x, currentPos.y + 1);
            break;
        case South:
            backPos = getPosAt(currentPos.x, currentPos.y - 1);
            break;
        case West:
            backPos = getPosAt(currentPos.x + 1, currentPos.y);
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

        GroundSquare currentPos = player.getPosition();
        Direction direction = player.getDirection();
        Position leftPos;

        switch (direction) {
        case East:
            leftPos = getPosAt(currentPos.x, currentPos.y - 1);
            break;
        case North:
            leftPos = getPosAt(currentPos.x - 1, currentPos.y);
            break;
        case South:
            leftPos = getPosAt(currentPos.x + 1, currentPos.y);
            break;
        case West:
            leftPos = getPosAt(currentPos.x, currentPos.y + 1);
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

        GroundSquare currentPos = player.getPosition();
        Direction direction = player.getDirection();
        Position rightPos;

        switch (direction) {
        case East:
            rightPos = getPosAt(currentPos.x, currentPos.y + 1);
            break;
        case North:
            rightPos = getPosAt(currentPos.x + 1, currentPos.y);
            break;
        case South:
            rightPos = getPosAt(currentPos.x - 1, currentPos.y);
            break;
        case West:
            rightPos = getPosAt(currentPos.x, currentPos.y - 1);
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

        for (Position[] row : board) {
            for (Position col : row) {
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

}
