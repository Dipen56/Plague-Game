package client.view;

import client.ClientMain;
import client.rendering.Rendering;

/**
 * this is class represents the controller witch will be used to communicate
 * between client and GUI.
 * 
 * @author Dipen
 *
 */
public class ViewControler {
	private GUI gui;
	private Rendering render;
	private ClientMain client;

	public ViewControler(String[] args) {
		gui = new GUI(this,render);
		//GUI.launch(GUI.class);

	}

	public void loginPlayer(String ip, int port, String userName, int avatarID) {
		client = new ClientMain(ip, port, userName, avatarID);
	}

	public String getChatMsg(String msg) {
		System.out.println(msg);
		return msg;
	}

	public static void main(String[] args) {
		new ViewControler(args);
		GUI.launch(GUI.class,args);
	}
}
