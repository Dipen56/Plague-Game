package game.control;

import game.player.Player;

/**
 * PacketTypes contains inner classes that specifies which kind of packet are they going to encode.
 * @author Rafaela Tabay
 *
 */
public class PacketTypes {
	
	
	
	public class LogIn extends Packet{

		private String username;
		public LogIn(byte[] playerName) {
			super(1);
			username =  new String(playerName).trim();

			
		}

		/**
		 * Returns the code for the type and the username of player
		 */
		@Override
		public byte[] getMessage() {
			return  ("1"+username).getBytes();
		}
		
		public String getUserName(){
			return username;
		}
	}

	public class MouseEvent extends Packet
	{
		
		public MouseEvent(int number,ClientInformation client1, ClientInformation client2){
			super(2);
			
		}
		@Override
		public byte[] getMessage() {
			return null;
		}

		
		//TODO: implement ParseTrade
		public String ParseTradePacket(String key){
			return "";
			
		}
		
	}
	
	
	public class KeyEvent extends Packet{
		
		//TODO: UPDATE PARAMETER BY PASSING THE VIEW/RENDERER TO UPDATE THE SCREEN OF EACH PLAYER

		public KeyEvent(int number) {
			super(3);
		}

		@Override
		public byte[] getMessage() {
			return null;
		}
		
	}
	
	
	
	public class Message extends Packet{

		
		
		private String message ;
		public Message(byte[] m) {
			super(5);	
			this.message = new String(m).trim();
		}

		
		@Override
		public byte[] getMessage() {
			return ("5"+message).getBytes();
		}
		
		public String toString(){
			return this.message.substring(1).trim();
		}
		
	}
	
	

}
