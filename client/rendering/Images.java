package client.rendering;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class Images {

    public static final Image GAMEICON_IMAGE = loadImage("/game-icon.png");

    public static final Image INVENTORY_IMAGE = loadImage("/item-tray.png");

    public static final Image SLASH_SCREEN_IMAGE = loadImage(
            "/spash-screen-background.png");

    //public static final Image BACKGROUND_IMAGE = loadImage("/night.png");
    public static final Image BACKGROUND_IMAGE = loadImage("/background3.gif");
    public static final Image GRASS_IMAGE = loadImage("/grass.png");

    public static final Image TREE_IMAGE = loadImage("/tree.png");

    public static final Image CHEST_IMAGE = loadImage("/chest.png");

    public static final Image[] AVATAR_IMAGES = { loadImage("/front_stand_1.png"),
            loadImage("/front_stand_2.png"), loadImage("/front_stand_3.png"),
            loadImage("/front_stand_4.png"), loadImage("/front_stand_5.png") };

    /**
     * This is designed as a table for renderer to index char board to render objects.
     */
    public static final Map<Character, Image> MAP_OBJECT_IMAGES;

    public static final Map<Character, Image> ITEM_IMAGES;

    /*
     * Initialise the table for Renderer. Each table contains a map which maps a char to
     * the corresponding object, so the Renderer knows what to render by knowing what char
     * was sent by server.
     */
    static {
        MAP_OBJECT_IMAGES = new HashMap<>();
        ITEM_IMAGES = new HashMap<>();

        /*
         * TODO This is probably not appropriate, some map objects may need more than one
         * png path, e.g. a room has four sides of views, each of them should be
         * different.
         * 
         * But the idea is, we initialise this map for renderer so that renderer knows
         * what map object to render by looking into this map.
         */

        // ============= map objects ====================

        /*
         * E: Room Obstacle
         * 
         * G: Ground Space
         * 
         * T: Tree
         * 
         * R: Rock
         * 
         * B: Barrel
         * 
         * A: Table
         * 
         * C: Chest
         * 
         * U: Cupboard
         * 
         * P: Scrap Pile
         * 
         * H: chair
         * 
         * D: a door. This should be rendered as ground, but it indicates which direction
         * the room should be facing.
         * 
         */
         MAP_OBJECT_IMAGES.put('T', loadImage("/tree.png"));
         MAP_OBJECT_IMAGES.put('R', loadImage("/boulder.png"));
         MAP_OBJECT_IMAGES.put('C', loadImage("/chest.png"));
         MAP_OBJECT_IMAGES.put('B', loadImage("/barrel.png"));
         MAP_OBJECT_IMAGES.put('A', loadImage("/table.png"));
         MAP_OBJECT_IMAGES.put('U', loadImage("/Cupboard.png"));
         //MAP_OBJECT_IMAGES.put('P', loadImage("/ScrapPile.png"));
//         this is the TransitionSpace, which is actually a normal ground for renderer.
         MAP_OBJECT_IMAGES.put('D', loadImage("/grass.png"));

        // ============= inventory objects ====================

        ITEM_IMAGES.put('A', loadImage("/antidote.png"));
        ITEM_IMAGES.put('K', loadImage("/key.png"));
        ITEM_IMAGES.put('T', loadImage("/torch.png"));

    }

    /**
     * A helper method used to load images
     *
     * @param name
     * @return
     */
    public static Image loadImage(String name) {
        return new Image(Images.class.getResourceAsStream(name));
    }
}
