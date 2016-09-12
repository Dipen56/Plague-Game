package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ThreadClient extends Thread {

	private DatagramSocket clientSocket;
	private InetAddress ipAddress;

	public ThreadClient(String addressName) {
		try {
			clientSocket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(addressName);
		} catch (SocketException e) {
			e.getMessage();
		} catch (UnknownHostException e) {
			e.getMessage();
		}

	}

	public void run() {
		while (true) {
			
			byte[] message = new byte[5000];
			DatagramPacket packet = new DatagramPacket(message, message.length);
			 
			try{
				clientSocket.receive(packet);	
			}
			catch(IOException e){e.getMessage();}
			parseMessage(packet);
			
		}
	}

	public void parseMessage(DatagramPacket packet) {
		
		
		
		
	}

	public void sendDataToServer(byte[] message) {

	}

}
