package game.control;

import game.player.Player;

/**
 * PacketTypes contains inner classes that specifies which kind of packet are they going to encode.
 * @author Rafaela Tabay
 *
 */
public class PacketTypes {
	
	
	
	public class LogIn extends Packet{

		private String username = null;
		public LogIn(String playerName) {
			super(1);
			username = playerName;
		}

		/**
		 * Returns the code for the type and the username of player
		 */
		@Override
		public byte[] getMessage() {
			return  ("1"+username).getBytes();
		}
	}

	public class Trade extends Packet
	{
		
		public Trade(int number,ClientInformation client1, ClientInformation client2){
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
	
	
	public class UpdateView extends Packet{
		
		//TODO: UPDATE PARAMETER BY PASSING THE VIEW/RENDERER TO UPDATE THE SCREEN OF EACH PLAYER

		public UpdateView(int number) {
			super(3);
		}

		@Override
		public byte[] getMessage() {
			return null;
		}
		
	}
	
	
	
	public class PickUp extends Packet{

		public PickUp(int number) {
			super(4);
			// TODO Auto-generated constructor stub
		}

		@Override
		public byte[] getMessage() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
