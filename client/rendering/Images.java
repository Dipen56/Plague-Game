package client.rendering;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
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

	public static final Image GAMEICON_IMAGE = loadImage("/game-icon.png");

	public static final Image INVENTORY_IMAGE = loadImage("/item-tray.png");

	public static final Image SLASH_SCREEN_IMAGE = loadImage("/spash-screen-background.png");

	public static final Image LOGIN_SCREEN_IMAGE = loadImage("/login-background.png");

	public static final Image DAYTIME_IMAGE = loadImage("/Daytime.jpg");

	public static final Image NIGHTIME_IMAGE = loadImage("/background.gif");

	public static final Image GRASS_IMAGE = loadImage("/grass.png");
	
	public static final Image GRASSNIGHT_IMAGE = loadImage("/grass_dark.png");

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

	public static final Map<Avatar, Map<Side, Image>> DEAD_IMAGES;

	/*
	 * Initialise the table for Renderer. Each table contains a map which maps a
	 * char to the corresponding object, so the Renderer knows what to render by
	 * knowing what char was sent by server.
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

		/*
		 * TODO This is probably not appropriate, some map objects may need more
		 * than one png path, e.g. a room has four sides of views, each of them
		 * should be different.
		 *
		 * But the idea is, we initialise this map for renderer so that renderer
		 * knows what map object to render by looking into this map.
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
		// deadImg_1.put(Side.Front, loadImage("/Char_1_Front.gif"));
		// deadImg_1.put(Side.Back, loadImage("/Char_1_Rear.gif"));
		// deadImg_1.put(Side.Left, loadImage("/Char_1_Left.gif"));
		// deadImg_1.put(Side.Right, loadImage("/Char_1_Right.gif"));
		DEAD_IMAGES.put(Avatar.Avatar_1, deadImg_1);

		Map<Side, Image> deadImg_2 = new HashMap<>();
		// deadImg_2.put(Side.Front, loadImage("/Char_1_Front.gif"));
		// deadImg_2.put(Side.Back, loadImage("/Char_1_Rear.gif"));
		// deadImg_2.put(Side.Left, loadImage("/Char_1_Left.gif"));
		// deadImg_2.put(Side.Right, loadImage("/Char_1_Right.gif"));
		DEAD_IMAGES.put(Avatar.Avatar_2, deadImg_2);

		Map<Side, Image> deadImg_3 = new HashMap<>();
		// deadImg_3.put(Side.Front, loadImage("/Char_1_Front.gif"));
		// deadImg_3.put(Side.Back, loadImage("/Char_1_Rear.gif"));
		// deadImg_3.put(Side.Left, loadImage("/Char_1_Left.gif"));
		// deadImg_3.put(Side.Right, loadImage("/Char_1_Right.gif"));
		DEAD_IMAGES.put(Avatar.Avatar_3, deadImg_3);

		Map<Side, Image> deadImg_4 = new HashMap<>();
		// deadImg_4.put(Side.Front, loadImage("/Char_1_Front.gif"));
		// deadImg_4.put(Side.Back, loadImage("/Char_1_Rear.gif"));
		// deadImg_4.put(Side.Left, loadImage("/Char_1_Left.gif"));
		// deadImg_4.put(Side.Right, loadImage("/Char_1_Right.gif"));
		DEAD_IMAGES.put(Avatar.Avatar_4, deadImg_4);

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
