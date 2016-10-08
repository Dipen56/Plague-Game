package client.rendering;

import server.game.player.Direction;

/**
 * This enum class represents four sides of a object, including front, back,
 * left, right.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public enum Side {

	Front, Back, Left, Right;

	/**
	 * Get the correct side according to your own facing direction and his
	 * facing direction.
	 * 
	 * @param ownDir
	 *            --- your own facing direction
	 * @param hisDir
	 *            --- his facing direction
	 * @return --- the correct side
	 */
	public static Side getSideByRelativeDirection(Direction ownDir, Direction hisDir) {

		switch (ownDir) {
		case East:
			switch (hisDir) {
			case East:
				return Back;
			case North:
				return Left;
			case South:
				return Right;
			case West:
				return Front;
			default:
				return null; // dead code
			}
		case North:
			switch (hisDir) {
			case East:
				return Right;
			case North:
				return Back;
			case South:
				return Front;
			case West:
				return Left;
			default:
				return null; // dead code
			}
		case South:
			switch (hisDir) {
			case East:
				return Left;
			case North:
				return Front;
			case South:
				return Back;
			case West:
				return Right;
			default:
				return null; // dead code
			}
		case West:
			switch (hisDir) {
			case East:
				return Front;
			case North:
				return Right;
			case South:
				return Left;
			case West:
				return Back;
			default:
				return null; // dead code
			}
		default:
			return null; // dead code
		}
	}

}
