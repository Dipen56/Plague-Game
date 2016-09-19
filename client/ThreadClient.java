package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.net.UnknownHostException;

import client.view.GUI;
import client.view.ViewControler;
import server.Packet.DataType;
import server.PacketTypes;
import server.Server;
import server.Packet;;


//import game.control.Packet.DataType;

public class ThreadClient extends Thread {

    private DatagramSocket clientSocket;
	private InetAddress ipAddress;
	private ViewControler GUIController;

	public ThreadClient(String add,ViewControler view) {
		this.GUIController = view;
		try {
			clientSocket = new DatagramSocket();
			ipAddress = InetAddress.getByName(add);
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
				sleep(10); //sleep for 10 ms and wait for reply from server

			} catch (InterruptedException e) {
				e.getMessage();
			}

			byte[] message = new byte[5000];
			DatagramPacket packet = new DatagramPacket(message, message.length);

			try {
				clientSocket.receive(packet);
			} catch (PortUnreachableException e) {
				e.getCause();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//clientSocket.close();
			readMessage(packet);
		}
	}

	/**
	 * Extracts the type of packet the client had received and calls the appropriate method
	 * to handle the data
	 * @param dPacket the packet to be read and extracted
	 */
	 public void readMessage(DatagramPacket dPacket) {

		 System.out.println("recieved");


		DataType type = Packet.getPacketType(new String(dPacket.getData()).trim().substring(0, 1));

		if(type.equals(DataType.MESSAGE)){
			PacketTypes p = new PacketTypes();
			PacketTypes.Message m = p.new Message(dPacket.getData());
			System.out.println(m.toString());
		}


	}
	/**
	 * 	Makes a new Datagram object and sends it to the server
	 * @param data bytes of information to be sent
	 */
	 public void sendDataToServer(byte[] message) {
		  DatagramPacket packet = new DatagramPacket(message, message.length, ipAddress, Server.PORTN_NUM);
	        try {
	            clientSocket.send(packet);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}



	public InetAddress getClientAddress(){
	try {
		return InetAddress.getLocalHost();
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
	}


	public int getClientPort(){
		return this.clientSocket.getPort();
	}


}
