package client.rendering;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import server.game.player.Avatar;
import server.game.player.Direction;

public class Images {

    public static final Image GAMEICON_IMAGE = loadImage("/game-icon.png");

    public static final Image INVENTORY_IMAGE = loadImage("/item-tray.png");

    public static final Image SLASH_SCREEN_IMAGE = loadImage(
            "/spash-screen-background.png");

    public static final Image BACKGROUND_IMAGE = loadImage("/night.png");

    public static final Image GRASS_IMAGE = loadImage("/grass.png");

    public static final Image TREE_IMAGE = loadImage("/tree.png");

    public static final Image CHEST_IMAGE = loadImage("/chest.png");

    /**
     * This is designed as a table for renderer to retrieve the avatar images.
     */
    public static final Map<Avatar, Map<Side, Image>> AVATAR_IMAGES;

    /**
     * This is designed as a table for renderer to index char board to render objects.
     */
    public static final Map<Character, Image> MAP_OBJECT_IMAGES;

    /**
     * This is designed as a table for renderer to index item images.
     */
    public static final Map<Character, Image> ITEM_IMAGES;

    /*
     * Initialise the table for Renderer. Each table contains a map which maps a char to
     * the corresponding object, so the Renderer knows what to render by knowing what char
     * was sent by server.
     */
    static {
        MAP_OBJECT_IMAGES = new HashMap<>();
        ITEM_IMAGES = new HashMap<>();
        AVATAR_IMAGES = new HashMap<>();
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
        // MAP_OBJECT_IMAGES.put('P', loadImage("/ScrapPile.png"));
        // this is the TransitionSpace, which is actually a normal ground for renderer.
        MAP_OBJECT_IMAGES.put('D', loadImage("/grass.png"));

        // ============= inventory objects ====================

        ITEM_IMAGES.put('A', loadImage("/antidote.png"));
        ITEM_IMAGES.put('K', loadImage("/key.png"));
        ITEM_IMAGES.put('T', loadImage("/torch.png"));

        // ============= Avatar images ====================

        Map<Side, Image> avatarImg_1 = new HashMap<>();
        // avatarImg_1.put(Side.Front, loadImage("/north_image_for_avatar_1"));
        // avatarImg_1.put(Side.Back, loadImage("/east_image_for_avatar_1"));
        // avatarImg_1.put(Side.Left, loadImage("/south_image_for_avatar_1"));
        // avatarImg_1.put(Side.Right, loadImage("/west_image_for_avatar_1"));
        AVATAR_IMAGES.put(Avatar.Avatar_1, avatarImg_1);

        Map<Side, Image> avatarImg_2 = new HashMap<>();
        // avatarImg_2.put(Side.Front, loadImage("/north_image_for_avatar_2"));
        // avatarImg_2.put(Side.Back, loadImage("/east_image_for_avatar_2"));
        // avatarImg_2.put(Side.Left, loadImage("/south_image_for_avatar_2"));
        // avatarImg_2.put(Side.Right, loadImage("/west_image_for_avatar_2"));
        AVATAR_IMAGES.put(Avatar.Avatar_2, avatarImg_2);

        Map<Side, Image> avatarImg_3 = new HashMap<>();
        // avatarImg_3.put(Side.Front, loadImage("/north_image_for_avatar_3"));
        // avatarImg_3.put(Side.Back, loadImage("/east_image_for_avatar_3"));
        // avatarImg_3.put(Side.Left, loadImage("/south_image_for_avatar_3"));
        // avatarImg_3.put(Side.Right, loadImage("/west_image_for_avatar_3"));
        AVATAR_IMAGES.put(Avatar.Avatar_3, avatarImg_3);

        Map<Side, Image> avatarImg_4 = new HashMap<>();
        // avatarImg_4.put(Side.Front, loadImage("/north_image_for_avatar_4"));
        // avatarImg_4.put(Side.Back, loadImage("/east_image_for_avatar_4"));
        // avatarImg_4.put(Side.Left, loadImage("/south_image_for_avatar_4"));
        // avatarImg_4.put(Side.Right, loadImage("/west_image_for_avatar_4"));
        AVATAR_IMAGES.put(Avatar.Avatar_4, avatarImg_4);

        // public static final Image[] AVATAR_IMAGES = { loadImage("/front_stand_1.png"),
        // loadImage("/front_stand_2.png"), loadImage("/front_stand_3.png"),
        // loadImage("/front_stand_4.png"), loadImage("/front_stand_5.png") };

    }

    /**
     * This utility method is used to retrieve avatar image according to your own
     * direction and the other player's direction.
     * 
     * @param avatar
     *            --- the avatar
     * @param ownDir
     *            --- your own direction
     * @param hisDir
     *            --- the other player's direction.
     * @return --- the proper image to render the other player.
     */
    public static Image getAvatarImageByDirection(Avatar avatar, Direction ownDir,
            Direction hisDir) {
        return AVATAR_IMAGES.get(avatar)
                .get(Side.getSideByRelativeDirection(ownDir, hisDir));
    }

    /**
     * This utility method is used to retrieve avatar image from a given side.
     * 
     * @param avatar
     *            --- the avatar
     * @param side
     *            --- the side
     * @return --- the proper image to render the other player.
     */
    public static Image getAvatarImageBySide(Avatar avatar, Side side) {
        return AVATAR_IMAGES.get(avatar).get(side);
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
