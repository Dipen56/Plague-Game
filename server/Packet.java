package server;

/**
 * This enumeration class represents the data packets exchanged between server and client.
 *
 * For efficiency each Packet should be converted to byte in transition. This class also
 * provides methods to perform byte <--> Packet conversion.
 *
 * XXX if we need more packet, we can just simply add it here, and then add more clause in
 * client and receptionist.
 *
 * @author Rafaela & Hector
 *
 */
public enum Packet {
    /**
     * Move forward. This is used at client side, and it doesn't need any acknowledgement.
     */
    Forward,
    /**
     * Move backward. This is used at client side, and it doesn't need any
     * acknowledgement.
     */
    Backward,
    /**
     * Move left. This is used at client side, and it doesn't need any acknowledgement.
     */
    Left,
    /**
     * Move right. This is used at client side, and it doesn't need any acknowledgement.
     */
    Right,
    /**
     * Turn Light. This is used at client side, and it doesn't need any acknowledgement.
     */
    TurnLeft,
    /**
     * Turn Right. This is used at client side, and it doesn't need any acknowledgement.
     */
    TurnRight,
    /**
     * Transit between areas. This is used at client side, and it doesn't need any
     * acknowledgement.
     */
    Transit,
    /**
     * Use an Item. This is used at client side, and should be followed by an int
     * indicating which item in inventory. This packet also needs an acknowledgement from
     * server, so that the client can properly render what's changed.
     */
    UseItem,
    /**
     * Destroy an Item. This is used at client side, and should be followed by int
     * indicating which item in inventory. This packet also needs an acknowledgement from
     * server, so that the client can properly render what's changed.
     */
    DestroyItem,
    /**
     * Take out items from a container. This is used at client side, and it doesn't need
     * any acknowledgement.
     */
    TakeOutItem,
    /**
     * Unlock a lockable object in front. This is used at client side, and it doesn't need
     * any acknowledgement.
     */
    Unlock,
    /**
     * Disconnect with server/client. This is used for both side.
     */
    Disconnect,
    /**
     * A flag indicating ready.
     */
    Ready;

    /*
     * NOTE!!!!!
     *
     * If new packet type is added, the long nasty switch statement in Receptionist and
     * Client should both be added.
     *
     * TODO:
     *
     * need to add: Put_Items_in_container
     */

    /**
     * Convert the Packet into byte.
     * @return
     */
    public byte toByte() {
        // We are never going to have more than 127 values in this enum. Safe to cast.
        return (byte) this.ordinal();
    }

    /**
     * Convert a byte back to a Packet.
     *
     * @param b
     * @return
     */
    public static Packet fromByte(byte b) {
        if (b < 0 || b >= Packet.values().length) {
            throw new IndexOutOfBoundsException();
        }
        return Packet.values()[b];
    }
}
