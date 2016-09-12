package server.game.control;

public abstract class Packet {

	
	private int type = 0;
	
	public Packet(int number){
		this.type = number;
	}
	
	/**
	 * Goes through the list of PacketTypes enum and checks which one will be used
	 * @param packetName
	 * @return PacketTypes
	 */
	public static DataType getPacketType(String packetName){
		int num;
		try{
			num = Integer.parseInt(packetName);
		}
		catch(NumberFormatException e){
			return DataType.ERROR;
		}
		
		for(DataType packet: DataType.values()){
			if(packet.id == num){
				return packet;
			}
		}
		return DataType.ERROR; //did not find the packet type
	}
	
	/**
	 * Each packet is different so it will have a different encoded message.
	 * The subclasses will implement their own encoded message depending on their type
	 * @return the compressed message
	 */
	public abstract byte[] getMessage();
	
	/**
	 * Sends the encoded message to the server from the client 
	 * @param client
	 */
	public void sendMessage(ThreadClient client){
		client.sendDataToServer(getMessage());
	}
	/**
	 * Sends the encoded message to all the clients
	 * @param server
	 */
	public void sendMessage(ThreadServer server){
		server.broadcastToAll(getMessage());
	}
	
	
	public static enum DataType{
		ERROR(0),LOGIN(1),TRADE(2), UPDATEVIEW(3),PICKUP(4);
		
		private int id;
		private DataType(int type){
			id = type;
		}
		
	}
}
