package client.rendering;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import server.game.player.Avatar;
import server.game.player.Direction;

/**
 * This is the image warehouse. All images used in this project are statically
 * stored in this class. There are also some utility methods for quickly
 * retrieving images.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Images {

	/**
	 * This is the game icon image
	 */
	public static final Image GAMEICON_IMAGE = loadImage("/game-icon.png");

	/**
	 * This is an empty image for empty item slot in inventory
	 */
	public static final Image INVENTORY_IMAGE = loadImage("/item-tray.png");

	/**
	 * This is the splash screen image
	 */
	public static final Image SLASH_SCREEN_IMAGE = loadImage("/spash-screen-background.png");

	/**
	 * This is the background image for login screen
	 */
	public static final Image LOGIN_SCREEN_IMAGE = loadImage("/login-background.png");

	/**
	 * The day time background image
	 */
	public static final Image DAYTIME_IMAGE = loadImage("/Daytime.jpg");

	/**
	 * The night time background image
	 */
	public static final Image NIGHTIME_IMAGE = loadImage("/background.gif");

	/**
	 * The grass image
	 */
	public static final Image GRASS_IMAGE = loadImage("/grass.png");

	/**
	 * The grass image in the night
	 */
	public static final Image GRASSNIGHT_IMAGE = loadImage("/grass_dark.png");

	/**
	 * The ground tile image in the room
	 */
	public static final Image ROOMTILE_IMAGE = loadImage("/roomTile.png");

	/**
	 * Help screen image
	 */
	public static final Image HOWTOPLAY_IMAGE = loadImage("/HowToPlay.png");

	/**
	 * Keyboard short-cut help screen image
	 */
	public static final Image KEYBOARDSHORT_IMAGE = loadImage("/Keyboard_Help.jpg");

	/**
	 * waiting room image
	 */
	public static final Image WAITING_ROOM_IMAGE = loadImage("/wating-room.gif");

	/**
	 * Four green arrow images used for rendering mini-map
	 */
	public static final Map<Direction, Image> GREEN_ARROW;

	/**
	 * Four red arrow images used for rendering mini-map
	 */
	public static final Map<Direction, Image> RED_ARROW;

	/**
	 * This is designed as a table for renderer to retrieve the avatar images.
	 */
	public static final Map<Avatar, Map<Side, Image>> AVATAR_IMAGES_WITHOUT_TORCH;

	/**
	 * This is designed as a table for renderer to retrieve the avatar images.
	 */
	public static final Map<Avatar, Map<Side, Image>> AVATAR_IMAGES_WITH_TORCH;

	/**
	 * This is designed as a table for renderer to index char board to render
	 * objects.
	 */
	public static final Map<Character, Image> MAP_OBJECT_IMAGES;

	/**
	 * This is designed as a table for renderer to index item images.
	 */
	public static final Map<Character, Image> ITEM_IMAGES;

	/**
	 * This is the profile pictures.
	 */
	public static final Map<Avatar, Image> PROFILE_IMAGES;

	/**
	 * This is designed as a table for renderer to retrieve the dead player
	 * images.
	 */
	public static final Map<Avatar, Map<Side, Image>> DEAD_IMAGES;

	/**
	 * Mini-map colour table
	 */
	public static final Map<Character, Color> MINIMAP_COLOR_TABLE;

	/**
	 * Map object description table
	 */
	public static final Map<Character, String> MAP_OBJECT_DESCRIPTION;

	/*
	 * Initialise the constant tables for renderer.
	 */
	static {
		MAP_OBJECT_IMAGES = new HashMap<>();
		ITEM_IMAGES = new HashMap<>();
		AVATAR_IMAGES_WITHOUT_TORCH = new HashMap<>();
		AVATAR_IMAGES_WITH_TORCH = new HashMap<>();
		GREEN_ARROW = new HashMap<>();
		RED_ARROW = new HashMap<>();
		PROFILE_IMAGES = new HashMap<>();
		DEAD_IMAGES = new HashMap<>();
		MINIMAP_COLOR_TABLE = new HashMap<>();
		MAP_OBJECT_DESCRIPTION = new HashMap<>();

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
		 * D: a door. This should be rendered as ground, but it indicates which
		 * direction the room should be facing.
		 *
		 */

		MAP_OBJECT_IMAGES.put('T', loadImage("/treeresized.png"));
		MAP_OBJECT_IMAGES.put('R', loadImage("/boulder.png"));
		MAP_OBJECT_IMAGES.put('C', loadImage("/chest.png"));
		MAP_OBJECT_IMAGES.put('B', loadImage("/barrel.png"));
		MAP_OBJECT_IMAGES.put('A', loadImage("/table.png"));
		MAP_OBJECT_IMAGES.put('U', loadImage("/Cupboard.png"));
		MAP_OBJECT_IMAGES.put('P', loadImage("/ScrapPile1.png"));
		MAP_OBJECT_IMAGES.put('D', loadImage("/door1.png"));
		MAP_OBJECT_IMAGES.put('H', loadImage("/chair.png"));

		// ============= inventory objects ====================

		ITEM_IMAGES.put('A', loadImage("/antidote.png"));
		ITEM_IMAGES.put('K', loadImage("/key.png"));
		ITEM_IMAGES.put('T', loadImage("/torch.png"));
		ITEM_IMAGES.put('B', loadImage("/bag.png"));

		// ============= Avatar images without torch ==========

		Map<Side, Image> avatarImg_1 = new HashMap<>();
		avatarImg_1.put(Side.Front, loadImage("/Char_1_Front.gif"));
		avatarImg_1.put(Side.Back, loadImage("/Char_1_Rear.gif"));
		avatarImg_1.put(Side.Left, loadImage("/Char_1_Left.gif"));
		avatarImg_1.put(Side.Right, loadImage("/Char_1_Right.gif"));
		AVATAR_IMAGES_WITHOUT_TORCH.put(Avatar.Avatar_1, avatarImg_1);

		Map<Side, Image> avatarImg_2 = new HashMap<>();
		avatarImg_2.put(Side.Front, loadImage("/Char_3_Front.gif"));
		avatarImg_2.put(Side.Back, loadImage("/Char_3_Back.gif"));
		avatarImg_2.put(Side.Left, loadImage("/Char_3_Left.gif"));
		avatarImg_2.put(Side.Right, loadImage("/Char_3_Right.gif"));
		AVATAR_IMAGES_WITHOUT_TORCH.put(Avatar.Avatar_2, avatarImg_2);

		Map<Side, Image> avatarImg_3 = new HashMap<>();
		avatarImg_3.put(Side.Front, loadImage("/Char_2_Front.gif"));
		avatarImg_3.put(Side.Back, loadImage("/Char_2_Rear.gif"));
		avatarImg_3.put(Side.Left, loadImage("/Char_2_left.gif"));
		avatarImg_3.put(Side.Right, loadImage("/Char_2_right.gif"));
		AVATAR_IMAGES_WITHOUT_TORCH.put(Avatar.Avatar_3, avatarImg_3);

		Map<Side, Image> avatarImg_4 = new HashMap<>();
		avatarImg_4.put(Side.Front, loadImage("/Char_4_Front.gif"));
		avatarImg_4.put(Side.Back, loadImage("/Char_4_Back.gif"));
		avatarImg_4.put(Side.Left, loadImage("/Char_4_left.gif"));
		avatarImg_4.put(Side.Right, loadImage("/Char_4_right.gif"));
		AVATAR_IMAGES_WITHOUT_TORCH.put(Avatar.Avatar_4, avatarImg_4);

		// ============= Avatar images with torch ==========

		Map<Side, Image> avatarImgTorch_1 = new HashMap<>();
		avatarImgTorch_1.put(Side.Front, loadImage("/Char_1_Front_Torch.gif"));
		avatarImgTorch_1.put(Side.Back, loadImage("/Char_1_Rear_Torch.gif"));
		avatarImgTorch_1.put(Side.Left, loadImage("/Char_1_Left_Torch.gif"));
		avatarImgTorch_1.put(Side.Right, loadImage("/Char_1_Right_Torch.gif"));
		AVATAR_IMAGES_WITH_TORCH.put(Avatar.Avatar_1, avatarImgTorch_1);

		Map<Side, Image> avatarImgTorch_2 = new HashMap<>();
		avatarImgTorch_2.put(Side.Front, loadImage("/Char_3_Front_Torch.gif"));
		avatarImgTorch_2.put(Side.Back, loadImage("/Char_3_Back_Torch.gif"));
		avatarImgTorch_2.put(Side.Left, loadImage("/Char_3_Left_Torch.gif"));
		avatarImgTorch_2.put(Side.Right, loadImage("/Char_3_Right_Torch.gif"));
		AVATAR_IMAGES_WITH_TORCH.put(Avatar.Avatar_2, avatarImgTorch_2);

		Map<Side, Image> avatarImgTorch_3 = new HashMap<>();
		avatarImgTorch_3.put(Side.Front, loadImage("/Char_2_Front_Torch.gif"));
		avatarImgTorch_3.put(Side.Back, loadImage("/Char_2_Rear_Torch.gif"));
		avatarImgTorch_3.put(Side.Left, loadImage("/Char_2_left_Torch.gif"));
		avatarImgTorch_3.put(Side.Right, loadImage("/Char_2_right_Torch.gif"));
		AVATAR_IMAGES_WITH_TORCH.put(Avatar.Avatar_3, avatarImgTorch_3);

		Map<Side, Image> avatarImgTorch_4 = new HashMap<>();
		avatarImgTorch_4.put(Side.Front, loadImage("/Char_4_Front_Torch.gif"));
		avatarImgTorch_4.put(Side.Back, loadImage("/Char_4_Back_Torch.gif"));
		avatarImgTorch_4.put(Side.Left, loadImage("/Char_4_left_Torch.gif"));
		avatarImgTorch_4.put(Side.Right, loadImage("/Char_4_right_Torch.gif"));
		AVATAR_IMAGES_WITH_TORCH.put(Avatar.Avatar_4, avatarImgTorch_4);

		// =================== arrows ==========================

		GREEN_ARROW.put(Direction.North, loadImage("/Green_North.png"));
		GREEN_ARROW.put(Direction.East, loadImage("/Green_East.png"));
		GREEN_ARROW.put(Direction.South, loadImage("/Green_South.png"));
		GREEN_ARROW.put(Direction.West, loadImage("/Green_West.png"));
		RED_ARROW.put(Direction.North, loadImage("/Red_North.png"));
		RED_ARROW.put(Direction.East, loadImage("/Red_East.png"));
		RED_ARROW.put(Direction.South, loadImage("/Red_South.png"));
		RED_ARROW.put(Direction.West, loadImage("/Red_West.png"));

		// ============= profile pictures =====================

		PROFILE_IMAGES.put(Avatar.Avatar_1, loadImage("/Char_1_face.png"));
		PROFILE_IMAGES.put(Avatar.Avatar_2, loadImage("/Char_3_Face.png"));
		PROFILE_IMAGES.put(Avatar.Avatar_3, loadImage("/Char_2_Face.png"));
		PROFILE_IMAGES.put(Avatar.Avatar_4, loadImage("/Char_4_Face.png"));

		// ============= dead avatar pictures =====================

		Map<Side, Image> deadImg_1 = new HashMap<>();
		deadImg_1.put(Side.Front, loadImage("/Char_1_front_stand_Dead.png"));
		deadImg_1.put(Side.Back, loadImage("/Char_1_rear_stand_Dead.png"));
		deadImg_1.put(Side.Left, loadImage("/Char_1_left_stand_Dead.png"));
		deadImg_1.put(Side.Right, loadImage("/Char_1_right_stand_Dead.png"));
		DEAD_IMAGES.put(Avatar.Avatar_1, deadImg_1);

		Map<Side, Image> deadImg_2 = new HashMap<>();
		deadImg_2.put(Side.Front, loadImage("/Char_3_Front_Dead.png"));
		deadImg_2.put(Side.Back, loadImage("/Char_3_Back_Dead.png"));
		deadImg_2.put(Side.Left, loadImage("/Char_3_Left_Dead.png"));
		deadImg_2.put(Side.Right, loadImage("/Char_3_Right_Dead.png"));
		DEAD_IMAGES.put(Avatar.Avatar_2, deadImg_2);

		Map<Side, Image> deadImg_3 = new HashMap<>();
		deadImg_3.put(Side.Front, loadImage("/Char_2_Front_Dead.png"));
		deadImg_3.put(Side.Back, loadImage("/Char_2_Rear_Dead.png"));
		deadImg_3.put(Side.Left, loadImage("/Char_2_left_Dead.png"));
		deadImg_3.put(Side.Right, loadImage("/Char_2_right_Dead.png"));
		DEAD_IMAGES.put(Avatar.Avatar_3, deadImg_3);

		Map<Side, Image> deadImg_4 = new HashMap<>();
		deadImg_4.put(Side.Front, loadImage("/Char_4_Front_Dead.png"));
		deadImg_4.put(Side.Back, loadImage("/Char_4_Back_Dead.png"));
		deadImg_4.put(Side.Left, loadImage("/Char_4_left_Dead.png"));
		deadImg_4.put(Side.Right, loadImage("/Char_4_right_Dead.png"));
		DEAD_IMAGES.put(Avatar.Avatar_4, deadImg_4);

		// ===== Mini-map colour & Map object description table ======

		// Rock
		MINIMAP_COLOR_TABLE.put('R', Color.rgb(83, 86, 102, 1.0));
		MAP_OBJECT_DESCRIPTION.put('R', "A rock. That won't heal me.");
		// Barrel
		MINIMAP_COLOR_TABLE.put('B', Color.rgb(83, 86, 102, 1.0));
		MAP_OBJECT_DESCRIPTION.put('B', "People put things in there. I can't though.");
		// Table
		MINIMAP_COLOR_TABLE.put('A', Color.rgb(83, 86, 102, 1.0));
		MAP_OBJECT_DESCRIPTION.put('A', "A table. That can't help me.");
		// Chair
		MINIMAP_COLOR_TABLE.put('H', Color.rgb(83, 86, 102, 1.0));
		MAP_OBJECT_DESCRIPTION.put('H', "It's a chair. I'd rather sit on it to rest.");

		// ===== Containers: golden, chest, cupboard, scrap pile =====

		// Chest
		MINIMAP_COLOR_TABLE.put('C', Color.rgb(255, 170, 37, 1.0));
		MAP_OBJECT_DESCRIPTION.put('C', "A chest. Probably contains loot.");
		// Cupboard
		MINIMAP_COLOR_TABLE.put('U', Color.rgb(255, 170, 37, 1.0));
		MAP_OBJECT_DESCRIPTION.put('U', "A cupboard. It might contain some medicine.");
		// Scrap pile
		MINIMAP_COLOR_TABLE.put('P', Color.rgb(255, 170, 37, 1.0));
		MAP_OBJECT_DESCRIPTION.put('P', "A pile of useless scrap. Or is it?");

		// ============== Tree or ground: green ======================

		// Tree, dark green
		MINIMAP_COLOR_TABLE.put('T', Color.rgb(68, 170, 58, 1.0));
		MAP_OBJECT_DESCRIPTION.put('T', "A tree. Why they all look the same?");
		// Ground, light green
		MINIMAP_COLOR_TABLE.put('G', Color.rgb(200, 236, 204, 1.0));
		MAP_OBJECT_DESCRIPTION.put('G', "");
		// Door space, this is just ground
		MINIMAP_COLOR_TABLE.put('D', Color.rgb(200, 236, 204, 1.0));
		MAP_OBJECT_DESCRIPTION.put('D', "");

		// =========== Room obstacles: blue ====================

		// Room obstacles
		MINIMAP_COLOR_TABLE.put('E', Color.rgb(19, 137, 245, 1.0));
		MAP_OBJECT_DESCRIPTION.put('E', "I found a hidden cabin! I need to get inside.");
	}

	/**
	 * This utility method is used to retrieve avatar image according to your
	 * own direction and the other player's direction.
	 *
	 * @param avatar
	 *            --- the avatar
	 * @param ownDir
	 *            --- your own direction
	 * @param hisDir
	 *            --- the other player's direction.
	 * @param isHoldingTorch
	 *            --- is the other player holding a torch?
	 * @return --- the proper image to render the other player.
	 */
	public static Image getAvatarImageByDirection(Avatar avatar, Direction ownDir, Direction hisDir,
			boolean isHoldingTorch) {
		if (isHoldingTorch) {
			return AVATAR_IMAGES_WITH_TORCH.get(avatar).get(Side.getSideByRelativeDirection(ownDir, hisDir));
		} else {
			return AVATAR_IMAGES_WITHOUT_TORCH.get(avatar).get(Side.getSideByRelativeDirection(ownDir, hisDir));
		}
	}

	/**
	 * This utility method is used to retrieve avatar image from a given side.
	 *
	 * @param avatar
	 *            --- the avatar
	 * @param side
	 *            --- the side
	 * @param isHoldingTorch
	 *            --- is the other player holding a torch?
	 * @return --- the proper image to render the other player.
	 */
	public static Image getAvatarImageBySide(Avatar avatar, Side side, boolean isHoldingTorch) {
		if (isHoldingTorch) {
			return AVATAR_IMAGES_WITH_TORCH.get(avatar).get(side);
		} else {
			return AVATAR_IMAGES_WITHOUT_TORCH.get(avatar).get(side);
		}
	}

	/**
	 * This utility method is used to retrieve dead avatar image according to
	 * your own direction and the other player's direction.
	 * 
	 * @param avatar
	 *            --- the avatar
	 * @param ownDir
	 *            --- your own direction
	 * @param hisDir
	 *            --- the other player's direction.
	 * @return --- the proper image to render the other player when he is dead.
	 */
	public static Image getDeadImageByDirection(Avatar avatar, Direction ownDir, Direction hisDir) {
		return DEAD_IMAGES.get(avatar).get(Side.getSideByRelativeDirection(ownDir, hisDir));
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
