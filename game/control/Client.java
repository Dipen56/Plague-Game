package game.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.ThreadClient;

import server.PacketTypes;

/**
 * 
 * @author Rafaela Tabay 300350087
 *
 */

public class Client {
	
//	private GUI gui;
//	private rendering renderer;
//	

	public static void main(String[] args){
		/*
		 * This is just for testing purposes. It reads the input from the console
		 * and constructs a message or log in packet and broadcasts it to each client
		 */
		 ThreadClient c = new ThreadClient("localhost"); //the name of the address
		 BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		 c.start(); //start thread
		 
		 PacketTypes p  = new PacketTypes();
		 PacketTypes.LogIn login = null; 
		try {
			login = p.new LogIn(bf.readLine().getBytes()); //try to make a new log in packet and send it to server
			 login.sendMessage(c);
		} catch (IOException e1) {
			e1.getMessage();
		}
		 PacketTypes.Message message ; 
		 try{
			 c.sleep(10); //sleep the thread and wait for the server to accept the request
		 }
		catch (InterruptedException e) {
			e.getMessage();
		}
		 
		 while(true){
			try {
				message = p.new Message(("[" + login.getUserName()+ "]"+bf.readLine()).getBytes()); //send message to everyone
				message.sendMessage(c);
			} catch (IOException e) {
				e.getMessage();
			}
		 }
		 
	}
}
