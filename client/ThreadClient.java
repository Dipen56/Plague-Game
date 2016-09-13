package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import client.view.GUI;
import game.control.Server;
import server.Packet.DataType;
import server.PacketTypes;
import server.Packet;;


//import game.control.Packet.DataType;

public class ThreadClient extends Thread {

     DatagramSocket clientSocket;
	private InetAddress ipAddress;
	private GUI gui;

	public ThreadClient(GUI g) {
		this.gui = g;
		try {
			clientSocket = new DatagramSocket();
			ipAddress = InetAddress.getByName("localhost");
			clientSocket.connect(ipAddress, Server.PORTN_NUM);
		} catch (SocketException e) {
			e.getMessage();
		} catch (UnknownHostException e) {
			e.getMessage();
		}

	}

	public void run() {
		while (true) {
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.getMessage();
			}
			
			byte[] message = new byte[5000];
			DatagramPacket packet = new DatagramPacket(message, message.length);
			
			try {
				clientSocket.receive(packet);
			} catch (IOException e) {
				e.getMessage();
			}
			
			readMessage(packet);
		}
	}
	
	public void readMessage(DatagramPacket dPacket) {
		
		DataType type = Packet.getPacketType(new String(dPacket.getData()).trim().substring(0, 1));
		
		if(type.equals(DataType.MESSAGE)){
			PacketTypes p = new PacketTypes();
			PacketTypes.Message m = p.new Message(dPacket.getData());
			System.out.println("From client side\n " + m.toString());
		}
		

	}
	/**
	 * 	Makes a new Datagram object and sends it to the server
	 * @param data bytes of information to be sent
	 */
	synchronized public void sendDataToServer(byte[] message) {
		  DatagramPacket packet = new DatagramPacket(message, message.length, ipAddress, Server.PORTN_NUM);
	        try {
	            clientSocket.send(packet);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	}

}
