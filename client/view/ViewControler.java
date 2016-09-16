package client.view;

/**
 * this is class represents the controller witch will be used to communicate
 * between client and GUI.
 * 
 * @author Dipen
 *
 */
public class ViewControler {
	GUI gui;

	public ViewControler(String[] args) {
		gui = new GUI(this);
		GUI.launch(GUI.class, args);

	}

	public String getChatMsg(String msg) {
		return msg;
	}

}
