package server.game.world;

import java.awt.image.BufferedImage;

/**
 * This class represents an unwalkable position on map, like a rock, or a tree.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Obstacle extends MapElement{

    /*
     * @forTeam@
     *
     * I don't think there is any point to make separate classes like Rock, Tree, or
     * others. I mean they essentially have the same function: 1. displaying itself, 2.
     * blocking players from walking into itself. So a tree object can be constructed as
     * new Obstacle(x, y, "Weird looking tree", an-image-of-tree), then rock can be
     * constructed as new Obstacle(x, y, "Reaaaaally hard rock", an-image-of-rock).
     *
     * If, in later stages, we have need to do separated classes for rocks or other stuff,
     * I'll make one then.
     *
     */

    private String description;
    private BufferedImage image;
    /**
     * X and Y coordinates of this object in the area map.
     */
    private int x, y;

    public Obstacle(int x, int y, String description, BufferedImage image) {
    	this.x = x;
       	this.y = y;
        this.description = description;
        this.image = image;
    }

    @Override
    public String toString() {
        return "o";
    }

}
