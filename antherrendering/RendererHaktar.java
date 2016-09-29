package antherrendering;

import java.util.Map;

import server.game.player.Avatar;
import server.game.player.Direction;
import server.game.player.Position;

/**
 * This is only a memo for how I think the renderer should work.
 * 
 * @author Hector
 *
 */
public class RendererHaktar {

    /**
     * User id of this connection.
     */
    private int uid;

    public RendererHaktar(int uid) {
        this.uid = uid;
    }

    /**
     * Redraw the renderer.
     * 
     * @param avatars
     *            --- This map keeps track of all player's avatars, where the key is
     *            userId, and value is his avatar.
     * @param positions
     *            --- This map keeps track of all player's Positions, where the key is
     *            userId, and value is his position. With a Position instance, you can
     *            easily access the player's area, coordinates(x, y), and direction.
     * @param torchStatus
     *            --- This map keeps track of the status for all players that whether he
     *            is holding a torch. The key is userId, and the value is either true or
     *            false.
     * @param areas
     *            --- This map contains all area boards represented as char[][]. The key
     *            is the areaId, and the value is the char map of the corresponding area.
     * @param visibility
     *            --- the visibility.
     */
    public void redraw(Map<Integer, Avatar> avatars, Map<Integer, Position> positions,
            Map<Integer, Boolean> torchStatus, Map<Integer, char[][]> areas,
            int visibility) {

        // current player's position
        Position selfPosition = positions.get(uid);

        // the areaId of current position.
        int areaId = selfPosition.areaId;

        // the board map as a char[][]
        char[][] map = areas.get(areaId);

        // current player's coordinate, x
        int x = selfPosition.x;

        // current player's coordinates, y
        int y = selfPosition.y;

        // current player's direction.
        Direction direction = selfPosition.getDirection();

        // =========== TODO Render ====================

        /*
         * 1. draw the front map objects, farthest map object is current position goes
         * forward "visibility" steps. say visibility is 8, we are standing at (10, 10)
         * facing north, the farthest position to draw is (10, 2)
         */

        /*
         * 2. draw the left map objects, left-most map object is current position goes
         * left "visibility" steps. say visibility is 8, we are standing at (10, 10)
         * facing north, the left-most position to draw is (2, 10)
         */

        /*
         * 3. draw the right map objects, right-most map object is current position goes
         * right "visibility" steps. say visibility is 8, we are standing at (10, 10)
         * facing north, the right-most position to draw is (10, 18)
         */

        /*
         * in step 2 and step 3, perhaps we do not even have to draw that far to left and
         * right. in the above example, at row 10, we may only need to draw (9, 10) and
         * (11, 10).
         */

        /*
         * 4. Iterate through Map<Integer, Position> positions, see if there is any other
         * players within visible distance. If there is/are, draw them at there. We
         * retrieve that player's avatar from Map<Integer, Avatar> avatars, and the avatar
         * should be consistent with his torch status. This could be done by looking into
         * Map<Integer, Boolean> torchStatus.
         */

    }

}
