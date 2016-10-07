package client.rendering;

import java.util.Map;

import server.game.player.Player;
import server.game.player.Position;
import server.game.world.Area;

import javafx.scene.layout.GridPane;

/**
 * This class is used to render the mini map to the gui.
 * 
 * @author Dipen
 *
 */
public class RenderMiniMap {
    private static Player player;
    private static Map<Integer, Player> playerOnMap;
    private static Map<Integer, Area> map;
    private static int avatarID;
    private static GridPane grid;

    public static void setValue(Player players, Map<Integer, Player> playersOnMaps,
            Map<Integer, Area> maps, int avatarIDs) {
        player = players;
        playerOnMap = playersOnMaps;
        map = maps;
        avatarID = avatarIDs;
    }

    public static void setGridPane(GridPane gridPane) {
        grid = gridPane;
    }

    public static void renderMiniMap(Position playerLoc, char[][] worldMap,
            int visibility, int uid) {
        
        
        
        

    }

    @Override
    public String toString() {
        return "RenderMiniMap";
    }

}
