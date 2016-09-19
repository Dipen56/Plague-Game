package client.view;

import server.PacketTypes;

/**
 * this is class represents the controller witch will be used to communicate
 * between client and GUI.
 * 
 * @author Dipen
 *
 */
public class ViewController {
	private GUI gui;
	private PacketTypes packetType = new PacketTypes();


	public ViewController(GUI gui) {
		this.gui = gui;
	}
	
	
	/**
	 * Extracts the message by converting the bytes of data 
	 * into a Message Packet and converts it to a string of words.
	 * @param data bytes of message to be extracted
	 */
	public void updateChatBox(byte[] data){
		String message = packetType.new Message(data).toString();
		gui.setChatText(message);
	}

}
