package server;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.xml.crypto.Data;

import client.ClientInformation;
import server.Packet.DataType;


public class ThreadServer extends Thread {

	private HashMap<String,ClientInformation> connectedClients;
	private DatagramSocket serverSocket;
	private PacketTypes packetType = new PacketTypes();


	public ThreadServer() {
		connectedClients = new HashMap<>();
		try {
			 serverSocket = new DatagramSocket(Server.PORTN_NUM);


		} catch (IOException e) {
			e.getMessage();
		}
	}

	public void run() {
		String consoleMessage;
		System.out.println("server is up and ready for connections.....");
		while(true){
		byte[] buffer = new byte[5000];
		DatagramPacket recievedData = new DatagramPacket(buffer, buffer.length);
		try {
			serverSocket.receive(recievedData);
			consoleMessage = "Client connected - socket address : " + recievedData.getSocketAddress();
			System.out.println(consoleMessage);
			readPacket(recievedData);
		} catch (IOException e) {
			e.getMessage();
		}
		}
	}

	/**
	 * Extracts the type of packet the server had received and calls the appropriate method
	 * to handle the data
	 * @param recievedPacket to be parsed and extracted
	 */
	public void readPacket(DatagramPacket recievedPacket) {

		InetAddress clientIP = recievedPacket.getAddress();
		byte[] data = recievedPacket.getData();
		int portNum = recievedPacket.getPort();
		DataType type = Packet.getPacketType(new String(data).substring(0, 1));
		System.out.println("ip address " + clientIP);

		if (type.equals(DataType.LOGIN))
			handleLogIn(data,clientIP, portNum);
		 else if (type.equals(DataType.MESSAGE))
			{
			PacketTypes.Message recievedMesasge = packetType.new Message(data);

			 System.out.println("type is message");
			 System.out.println("Recieved from " + recievedMesasge.toString());
			//sendData(packetType.new Message(("I have recieved your message client: " + clientIP ).getBytes()).getMessage() , clientIP, portNum);

			broadcastToAll(data);
			}
	}


	/**
	 * Broadcasts to everyone in the network that a new user has joined the game
	 * by making a new Message packet and attaching the name of the player
	 * @param data that contains the username
	 * @param ipAddress of the client
	 * @param portNum port where it came from
	 */
	 public void handleLogIn(byte[] data, InetAddress ipAddress, int portNum){
		PacketTypes.LogIn log = packetType.new LogIn(data);
		String newMessage  = "User " + log.getUserName() + " has joined the game";
		System.out.println(newMessage);
		addClient(log.getUserName(), ipAddress, portNum);
		System.out.println(connectedClients.size());
		sendData(packetType.new Message("You are now connected to the server".getBytes()).getMessage() , ipAddress, portNum);
		broadcastToAll(packetType. new Message(newMessage.getBytes()).getMessage());
	}

	/**
	 * Makes a new client information object and adds it to the list of connected clients
	 * @param username of player
	 * @param hostAddress IP address of player
	 * @param port of the player
	 */

	 private void addClient(String username, InetAddress hostAddress, int port) {
		int clientNum = connectedClients.size() + 1;
		ClientInformation playerData = new ClientInformation(port, username, hostAddress,clientNum);
		connectedClients.put(username,playerData);
	}

	/**
	 * Sends the data to everyone who is connected to the server
	 * @param message bytes of information to be sent
	 */
	 public void broadcastToAll(byte[] message) {
		for(ClientInformation client : connectedClients.values()){
			System.out.println(client.getUsername());
			System.out.println(client.getIpAddress());
		sendData(message, client.getIpAddress(), client.getPortNum());
		}
	}

	/**
	 * 	Makes a new Datagram object and sends it to the target client
	 * @param data bytes of information to be sent
	 * @param ipAddress client ip address
	 * @param port
	 */
	 public void sendData(byte[] data, InetAddress ipAddress, int port) {
		 System.out.println("send data");


		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			serverSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
