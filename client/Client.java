package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.rendering.rendering;
import client.view.GUI;
import server.PacketTypes;

/**
 * 
 * @author Rafaela Tabay 300350087
 *
 */

public class Client {

	public static void main(String[] args) {
		PacketTypes.Message message;
		GUI gui;
		rendering renderer;

		/*
		 * This is just for testing purposes. It reads the input from the
		 * console and constructs a message or log in packet and broadcasts it
		 * to each client
		 */
		gui = new GUI();
		ThreadClient c = new ThreadClient(gui); // the name of the address
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		c.start(); // start thread
		PacketTypes p = new PacketTypes();
		PacketTypes.LogIn login = null;
		
		try {
			login = p.new LogIn(bf.readLine().getBytes()); // try to make a new log in packet and send it to server
			login.sendMessage(c);
		} catch (IOException e1) {
			e1.getMessage();
		}
		
		try {
			c.sleep(10); // sleep the thread and wait for the server to accept the request
		} catch (InterruptedException e) {
			e.getMessage();
		}

		while (true) {
			try {
				message = p.new Message(("[" + login.getUserName() + "]" + bf.readLine()).getBytes()); // send  message to everyone																			// to																					
				message.sendMessage(c);
			} catch (IOException e) {
				e.getMessage();
			}
		}

	}
}
