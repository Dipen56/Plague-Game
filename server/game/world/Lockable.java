package server.game.world;

/**
 * This interface represents the property of being able to be locked. It can be
 * used in many Items or MapElements to create diversity of Item usage and map
 * interaction.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public interface Lockable {

	/**
	 * The keyID specifies which key can open this locked thing. Only the key
	 * with the same keyID can open it.
	 * 
	 * @return
	 */
	public int getKeyID();

	/**
	 * Is this thing locked?
	 * 
	 * @return
	 */
	public boolean isLocked();

	/**
	 * Lock it or unlock it.
	 * 
	 * @param isLocked
	 */
	public void setLocked(boolean isLocked);
}
