package server.game.world;

/**
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
