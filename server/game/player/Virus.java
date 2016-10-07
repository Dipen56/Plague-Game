package server.game.player;

/**
 * This enumeration class represents different types of virus.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public enum Virus {

    // Form BioHazzard. Or we could use other names. Ideas welcomed.
    Spanish_Flu, Black_Death, Ebola;

    /**
     * Get the virus at given index. If the index is illegal, an
     * <i>IndexOutOfBoundsException</i> is thrown.
     * 
     * @param index
     *            --- the ordinal number
     * @return --- the virus type at the given index.
     */
    public static Virus get(int index) {
        if (index < 0 || index >= Virus.values().length) {
            throw new IndexOutOfBoundsException();
        }
        return Virus.values()[index];
    }

    /**
     * Get a random virus type.
     * 
     * @return --- a random virus type.
     */
    public static Virus randomVirus() {
        int i = (int) (Math.random() * 3);
        switch (i) {
        case 0:
            return Spanish_Flu;
        case 1:
            return Black_Death;
        case 2:
            return Ebola;
        default:
            return null; // dead code
        }
    }

    @Override
    public String toString() {
        return super.toString().replace('_', ' ');
    }

}
