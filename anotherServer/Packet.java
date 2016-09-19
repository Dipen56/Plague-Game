package anotherServer;

public class Packet {

    public static enum GeneralType {
        Login, Disconnect, Message, Move;

        public byte toByte() {
            return (byte) this.ordinal();
        }
    }
    
    public static enum MoveType {
        Forward, Backward, Left, Right, TurnLeft, TurnRight;

        public byte toByte() {
            return (byte) this.ordinal();
        }
    }
    
    
}
