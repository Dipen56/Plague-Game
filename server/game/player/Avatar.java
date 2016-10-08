package server.game.player;

/**
 * This is the avatar of player. Each player has an avatar of his choice.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public enum Avatar {

	Avatar_1, Avatar_2, Avatar_3, Avatar_4;

	/**
	 * This method get the avatar by its ordinal number. If the index is
	 * illegal, an <i>IndexOutOfBoundsException</i> is thrown.
	 * 
	 * @param index
	 *            --- the ordinal number
	 * @return --- the Avatar at the given index.
	 */
	public static Avatar get(int index) {
		if (index < 0 || index >= Avatar.values().length) {
			throw new IndexOutOfBoundsException();
		}
		return Avatar.values()[index];
	}

}
