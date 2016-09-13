package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.rendering.rendering;
import client.view.GUI;
import server.PacketTypes;
import sun.rmi.runtime.Log;

/**
 * 
 * @author Rafaela Tabay 300350087
 *
 */

public class Client {

	public static void main(String[] args) throws IOException, InterruptedException {
		PacketTypes p = new PacketTypes();
		PacketTypes.Message message;
		PacketTypes.LogIn login = null;
		GUI gui = new GUI();
		rendering renderer;
		String consoleMessage;
		String serverMessage;

		/*
		 * This is just for testing purposes. It reads the input from the
		 * console and constructs a message or log in packet and broadcasts it
		 * to each client
		 */
		ThreadClient clientThread = new ThreadClient(gui); 
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
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
		clientThread.closeSocket();

	}
}
