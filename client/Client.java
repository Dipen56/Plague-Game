package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.ThreadClient;
import client.rendering.Rendering;
import client.view.GUI;
import client.view.ViewControler;
import server.PacketTypes;

/**
 *
 * @author Rafaela Tabay 3003500871
 *
 */

public class Client {

	public static void main(String[] args) throws IOException, InterruptedException {
		PacketTypes p = new PacketTypes();
		PacketTypes.Message message;
		PacketTypes.LogIn login = null;
		// GUI gui = new GUI(); i think this is not needed instead

		ViewControler viewControler = new ViewControler(args);
		// GUI gui = new GUI(); // i think this is not needed instead
		// GUI.launch(GUI.class, args);
		Rendering renderer;
		String consoleMessage;
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

		/*
		 * This is just for testing purposes. It reads the input from the
		 * console and constructs a message or log in packet and broadcasts it
		 * to each client
		 */
		String servername = bf.readLine().trim();
		ThreadClient clientThread = new ThreadClient(servername, viewControler);
		clientThread.start(); // start thread
		consoleMessage = "Welcome to the SERVER!\n" + "IP address is : " + clientThread.getClientAddress()
				+ " Port number is " + clientThread.getClientPort() + "\n";
		consoleMessage += "Pls enter a username";
		System.out.println(consoleMessage);

		login = p.new LogIn(("1" + bf.readLine()).getBytes());
		login.sendMessage(clientThread);
		String m;
		while (true) {
			try {
				m = bf.readLine();
				System.out.println("debugger: " + m);
				message = p.new Message(("[" + login.getUserName() + "]: " + m).getBytes());
				message.sendMessage(clientThread);
			} catch (IOException e) {
				e.getMessage();
				break;
			}
		}

	}
}
