package server;

/**
 * This enumeration class represents the data packets exchanged between server
 * and client.
 *
 * For efficiency each Packet should be converted to byte in transition. This
 * class also provides methods to perform byte <--> Packet conversion.
 *
 * @author Rafaela (300350087)
 * @author Hector (Fang Zhao 300364061)
 *
 */
public enum Packet {

	/**
	 * Move forward.
	 */
	Forward,

	/**
	 * Move backward.
	 */
	Backward,

	/**
	 * Move left.
	 */
	Left,

	/**
	 * Move right.
	 */
	Right,

	/**
	 * Turn Light.
	 */
	TurnLeft,

	/**
	 * Turn Right.
	 */
	TurnRight,

	/**
	 * Transit between areas.
	 */
	Transit,

	/**
	 * Use an Item. This should be followed by an index indicating which item in
	 * inventory.
	 */
	UseItem,

	/**
	 * Destroy an Item. This should be followed by index indicating which item
	 * in inventory.
	 */
	DestroyItem,

	/**
	 * Put an item inside a container. This should be followed by index
	 * indicating which item in inventory.
	 */
	PutItemIntoContainer,

	/**
	 * Take out items from a container.
	 */
	TakeOutItem,

	/**
	 * Unlock a lockable object in front.
	 */
	Unlock,

	/**
	 * Disconnect with server/client. This is used for both side.
	 */
	Disconnect,

	/**
	 * Save game status
	 */
	Save,

	/**
	 * Load game status
	 */
	Load,

	/**
	 * Chat massage
	 */
	Chat,

	/**
	 * A flag indicating ready.
	 */
	Ready;

	/**
	 * Convert the Packet into byte.
	 * 
	 * @return --- a byte value which equals the ordinal number.
	 */
	public byte toByte() {
		// no way to have more than 127 values in this enum. Safe to cast.
		return (byte) this.ordinal();
	}

	/**
	 * Convert a byte back to a Packet.
	 *
	 * @param b
	 *            --- a byte balue
	 * @return --- the Packet whose ordinal number equals the byte.
	 */
	public static Packet fromByte(byte b) {
		if (b < 0 || b >= Packet.values().length) {
			throw new IndexOutOfBoundsException();
		}
		return Packet.values()[b];
	}
}
