package server;

import client.view.ClientUI;

/**
 * This is the main class of "Plague". Run server with "-server" argument, or
 * run client with "-client" argument.
 * 
 * @author Hector (Fang Zhao 300364061)
 * 
 */
public class Main {

	/**
	 * Main function
	 * 
	 * @param args
	 *            --- the argument. Run server with "-server" argument, or run
	 *            client with "-client" argument.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("use \"-server\" to start server, and use \"-client\" to start client");
			return;
		}

		if ("-server".equals(args[0])) {
			new ServerMain();
		} else if ("-client".equals(args[0])) {
			new ClientUI();
		} else {
			System.out.println("use \"-server\" to start server, and use \"-client\" to start client");
			return;
		}
	}
}
