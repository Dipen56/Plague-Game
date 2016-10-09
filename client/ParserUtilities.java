package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import server.game.player.Avatar;
import server.game.player.Direction;
import server.game.player.Position;

/**
 * This class is a utility class containing static methods to parse
 * communications between server and client.
 * 
 * @author Dipen, Rafaela & Hector (Fang Zhao 300364061)
 *
 */
public class ParserUtilities {


	/**
	 * A universally used console input scanner. It's used in test based UI
	 */
	private static final Scanner SCANNER = new Scanner(System.in);

	/**
	 * Private constructor. No instantiation is allowed.
	 */
	private ParserUtilities() {
	}

	/**
	 * This method reads in a String and parse it into a char[][], which is used
	 * on client side to render the world. The char[][] is recorded in
	 * <i><b>areas</b></i> given as parameter. The input String is expected to
	 * have the following format:
	 * 
	 * <p>
	 * Say we have a 3 by 3 room (room's areaId is 1, description:
	 * "blabla"):<br>
	 * <p>
	 * GGG<br>
	 * GGC<br>
	 * GDG<br>
	 * 
	 * <p>
	 * G stands for empty space, C stands for chest, D stands for door.
	 * 
	 * <p>
	 * The string should be <i>"1,3,3,blabla\nGGG\nGGC\nGTG"</i>, [areaId,
	 * width, height, description\nMapChars]
	 * 
	 * @param areas
	 *            --- a map recording all area boards in game, where the key is
	 *            area Id, and the value is a char[][] representation of board.
	 * @param string
	 *            --- a string recording all area boards in game, received by
	 *            client from server.
	 */
	public static void parseMap(Map<Integer, char[][]> areas, Map<Integer, String> descriptions, String string) {

		Scanner scanner = new Scanner(string);
		String line = scanner.nextLine();

		// first get the width and height
		String[] firstLine = line.split(",");
		int areaId = -1;
		int width = -1;
		int height = -1;
		String description = null;
		try {
			areaId = Integer.valueOf(firstLine[0]);
			width = Integer.valueOf(firstLine[1]);
			height = Integer.valueOf(firstLine[2]);
			description = firstLine[3];

			if (areaId < 0 || width <= 0 || height <= 0) {
				System.out
						.println("Error occurred when parsing map. Negative areaId, Width or height. String input is: "
								+ string);
				scanner.close();
				return; // do not crash the game.
			}
		} catch (NumberFormatException e1) {
			System.out.println(
					"Error occurred when parsing map. AreaId, width or height is not integer. String input is: "
							+ string);
			scanner.close();
			return; // do not crash the game.
		}

		// then set the map char by char
		char[][] map = new char[height][width];

		int row = 0;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();

			if (line.length() != width) {
				System.out.println("Error occurred when parsing map. Width mismatch at row: " + row
						+ ". String input is: " + string);
				scanner.close();
				return; // do not crash the game.
			}

			for (int i = 0; i < line.length(); i++) {
				try {
					map[row][i] = line.charAt(i);
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Error occurred when parsing map. Index out of boundary at row: " + row
							+ ", column: " + i + ". String input is: " + string);
					scanner.close();
					return; // do not crash the game.
				}
			}
			row++;
		}

		if (row != height) {
			System.out.println(
					"Error occurred when parsing map. Height mismatch: " + row + ". String input is: " + string);
			scanner.close();
			return; // do not crash the game.
		}

		// job done, let's put it in map.
		areas.put(areaId, map);

		// put descriptions into map
		descriptions.put(areaId, description);

		scanner.close();
	}

	/**
	 * This method reads in a String and parse it into different avatar indices
	 * for each player in game, which is then used on client side to render all
	 * connected players with their avatars. Result is recorded in
	 * <i><b>avatars</b></i> given as parameter. The input String is expected to
	 * have the following format:
	 * <i>"uId_1,avatar_index_1|uId_2,avatar_index_2"</i>
	 * 
	 * <p>
	 * Say 2 players currently in game:
	 * <li>player 1, id 111, avatar index 0
	 * <li>player 2, id 222, avatar index 1<br>
	 * <br>
	 * <p>
	 * The string representation will be <i>"111,0|222,1"</i>
	 * 
	 * @param avatars
	 *            --- a map recording all player's avatars, where the key is
	 *            player Id, and the value is the avatar.
	 * @param avatarsStr
	 *            --- a string recording all player's avatars, received by
	 *            client from server.
	 */
	public static void parseAvatar(Map<Integer, Avatar> avatars, String avatarsStr) {
		Scanner scanner = new Scanner(avatarsStr);
		String line = scanner.nextLine();

		String[] posStrs = line.split("\\|"); // not '|'

		int uId = -1;
		int avatarIndex = -1;

		try {
			for (String posStr : posStrs) {

				if (posStr.length() < 1) {
					continue;
				}

				String[] nums = posStr.split(",");
				uId = Integer.valueOf(nums[0]);
				avatarIndex = Integer.valueOf(nums[1]);

				if (uId < 0 || avatarIndex < 0) {
					System.out.println(
							"Error occurred when parsing postion. Negative uId or avatarIndex. String input is: "
									+ avatarsStr);
					scanner.close();
					return; // do not crash the game.
				}

				avatars.put(uId, Avatar.get(avatarIndex));
			}

		} catch (NumberFormatException e1) {
			System.out
					.println("Error occurred when parsing postion. uId or avatarIndex is not integer. String input is: "
							+ avatarsStr);
			scanner.close();
			return; // do not crash the game.
		}
		scanner.close();

	}

	/**
	 * This method reads in a String and parse it into torch status for each
	 * player in game, which is then used on client side to render player
	 * holding torch and player not holding torch with different image. Result
	 * is recorded in <i><b>torchStatus</b></i> given as parameter. The input
	 * String is expected to have the following format:
	 * <i>"uId_1,true/false|uId_2,true/false"</i>
	 * 
	 * <p>
	 * Say 2 players currently in game:
	 * <li>player 1, id 111, is holding torch
	 * <li>player 2, id 222, is not holding torch<br>
	 * <br>
	 * <p>
	 * The string representation will be <i>"111,1|222,0"</i>
	 * 
	 * @param torchStatus
	 *            --- a map recording all player's status of holding torch or
	 *            not, where the key is player Id, and the value is a boolean
	 *            value for holding or not holding torch.
	 * @param torchStatusStr
	 *            --- a string recording all player's status of holding torch or
	 *            not, received by client from server.
	 */
	public static void parseTorchStatus(Map<Integer, Boolean> torchStatus, String torchStatusStr) {
		Scanner scanner = new Scanner(torchStatusStr);
		String line = scanner.nextLine();

		String[] posStrs = line.split("\\|"); // not '|'

		int uId = -1;
		int isHoldingTorch = -1;

		try {
			for (String posStr : posStrs) {

				if (posStr.length() < 1) {
					continue;
				}

				String[] nums = posStr.split(",");
				uId = Integer.valueOf(nums[0]);
				isHoldingTorch = Integer.valueOf(nums[1]);

				if (uId < 0 || isHoldingTorch < 0 || isHoldingTorch > 1) {
					System.out.println(
							"Error occurred when parsing postion. Negative uId or torch status. String input is: "
									+ torchStatusStr);
					scanner.close();
					return; // do not crash the game.
				}

				torchStatus.put(uId, isHoldingTorch == 0 ? false : true);
			}

		} catch (NumberFormatException e1) {
			System.out.println(
					"Error occurred when parsing postion. uId or torch status is not integer. String input is: "
							+ torchStatusStr);
			scanner.close();
			return; // do not crash the game.
		}
		scanner.close();

	}

	/**
	 * This method reads in a String and parse it into positions, which is used
	 * on client side to locate all connected players and render them. Result is
	 * recorded in <i><b>positions</b></i> given as parameter. The input String
	 * is expected to have the following format:
	 * 
	 * <p>
	 * Say Player(uId: 123) is in area(areaId: 456), his coordinates is (78,
	 * 90), and his facing direction is north (clockwisely we have North: 0;
	 * East: 1; South: 2; West: 3):
	 * 
	 * <p>
	 * The string should be <i>"123,456,78,90,0"</i>
	 * 
	 * @param positions
	 *            --- a map recording all player's positions, where the key is
	 *            player Id, and the value is the position information
	 *            containing areaId, coordinates(x,y), and direction.
	 * @param positionsStr
	 *            --- a string recording all player's positions, received by
	 *            client from server.
	 */
	public static void parsePosition(Map<Integer, Position> positions, String positionsStr) {

		Scanner scanner = new Scanner(positionsStr);
		String line = scanner.nextLine();

		String[] posStrs = line.split("\\|"); // not '|'

		int uId = -1;
		int areaId = -1;
		int x = -1;
		int y = -1;
		int dir = -1;

		try {
			for (String posStr : posStrs) {

				if (posStr.length() < 1) {
					continue;
				}

				String[] nums = posStr.split(",");
				uId = Integer.valueOf(nums[0]);
				areaId = Integer.valueOf(nums[1]);
				x = Integer.valueOf(nums[2]);
				y = Integer.valueOf(nums[3]);
				dir = Integer.valueOf(nums[4]);

				if (uId < 0 || areaId < 0 || x < 0 || y < 0 || dir < 0) {
					System.out.println(
							"Error occurred when parsing postion. Negative uId, areaId, x, y or direction. String input is: "
									+ positionsStr);
					scanner.close();
					return; // do not crash the game.
				}

				positions.put(uId, new Position(x, y, areaId, Direction.fromOrdinal(dir)));
			}

		} catch (NumberFormatException e1) {
			System.out.println(
					"Error occurred when parsing postion. uId, areaId, x, y or direction is not integer. String input is: "
							+ positionsStr);
			scanner.close();
			return; // do not crash the game.
		}
		scanner.close();
	}

	/**
	 * This method reads in a String and parse it into a List of Items, which is
	 * player's inventory, and then used on client side to render the inventory.
	 * The input String is expected to have the following format:
	 * 
	 * <p>
	 * Say an item (type A, description B), its string representation will be
	 * <i>"A@B"</i>, where A is a single character, B is the return of
	 * <i>toString()</i>. Every two items are separated with a '|' character.
	 * 
	 * <p>
	 * For example, this player has an Antidote (description: "foofoo"), and a
	 * Key (description: "barbar").
	 *
	 * <p>
	 * The string representation of his inventory will be
	 * <i>"A@foofoo|B@barbar"</i>
	 * <p>
	 * Character abbreviation table:<br>
	 *
	 * <li>A: Antidote<br>
	 * <li>K: Key<br>
	 * <li>T: Torch<br>
	 * <li>B: Bag<br>
	 * <br>
	 * 
	 * @param invenStr
	 *            --- a string recording all items in this player's inventory,
	 *            received by client from server.
	 * 
	 * @return --- a list of items (still as string.)
	 */
	public static List<String> parseInventory(String invenStr) {

		List<String> list = new ArrayList<>();

		if (invenStr.length() < 1) {
			// empty inventory
			return list;
		}

		Scanner scanner = new Scanner(invenStr);
		String line = scanner.nextLine();
		String[] items = line.split("\\|"); // not '|'

		for (String item : items) {
			list.add(item);
		}

		scanner.close();
		return list;
	}

	/**
	 * This helper method parse user's input as integer, and limits the maximum
	 * and minimum boundary of it.
	 * 
	 * @param min
	 *            --- the minimum boundary of input as an integer
	 * @param max
	 *            --- the maximum boundary of input as an integer
	 * @return --- the parsed integer
	 */
	public static int parseInt(int min, int max) {
		while (true) {
			String line = SCANNER.nextLine();

			try {
				// parse the input
				int i = Integer.valueOf(line);
				if (i >= min && i <= max) {
					// a good input
					return i;
				} else {
					// a out of boundary input, let the user retry.
					System.out.println("Please choose between " + min + " and " + max + ":");
					continue;
				}
			} catch (NumberFormatException e) {
				// the input is not an integer
				System.out.println("Please enter an integer:");
				continue;
			}
		}
	}

	/**
	 * This method read in a user input.
	 * 
	 * @return --- a line of string input from standard input.
	 */
	public static String parseString() {
		return SCANNER.nextLine();
	}

	/**
	 * This method read in a char. If more than one character is input, the user
	 * will be asked to re-type a char.
	 * 
	 * @return --- a char from standard input.
	 */
	public static char parseChar() {

		String line = SCANNER.nextLine();

		while (line.length() > 1) {
			System.out.println("Please only type in one char");
			line = SCANNER.nextLine();
		}

		return line.charAt(0);
	}


}
