package server.game.player;

/**
 * This is the avatar of player. Each player has an avatar of his choice.
 * 
 * @author Hector
 *
 */
public enum Avatar {
    Avatar_1, Avatar_2, Avatar_3, Avatar_4;

    /**
     * This method get the avatar by its ordinal number.
     * 
     * @param index
     * @return
     */
    public static Avatar get(int index) {
        if (index < 0 || index >= Avatar.values().length) {
            throw new IndexOutOfBoundsException();
        }
        return Avatar.values()[index];
    }

}
