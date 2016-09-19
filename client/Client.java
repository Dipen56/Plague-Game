package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.ThreadClient;
import client.rendering.rendering;
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
		ViewControler viewControl = new ViewControler(null);
		rendering renderer;
		String consoleMessage;
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

		/*
		 * This is just for testing purposes. It reads the input from the
		 * console and constructs a message or log in packet and broadcasts it
		 * to each client
		 */
		String servername = bf.readLine().trim();
		ThreadClient clientThread = new ThreadClient(servername,viewControl);
		clientThread.start(); // start thread
		consoleMessage = "Welcome to the SERVER!\n" + "IP address is : " + clientThread.getClientAddress()
				+ " Port number is " + clientThread.getClientPort() + "\n";
		consoleMessage += "Pls enter a username";
		System.out.println(consoleMessage);

		login = p.new LogIn( ("1"+bf.readLine()).getBytes());
		login.sendMessage(clientThread);
		while (true) {
			try {
				message = p.new Message(("[" + login.getUserName() + "]: " + bf.readLine()).getBytes());
				message.sendMessage(clientThread);
			} catch (IOException e) {
				e.getMessage();
				break;
			}
		}

	}
}
