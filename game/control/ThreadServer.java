package game.control;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import game.control.Packet.DataType;

public class ThreadServer extends Thread {

	public final int PORTN_NUM = 32768;

	private HashMap<String,ClientInformation> connectedClients;
	private DatagramSocket serversock;

	public ThreadServer() {
		connectedClients = new HashMap<>();
		try {
			serversock = new DatagramSocket(PORTN_NUM);

		} catch (IOException e) {
			e.getMessage();
		}
	}

	public void run() {

		while(true){
		byte[] buffer = new byte[5000];
		DatagramPacket data = new DatagramPacket(buffer, buffer.length);
		try {
			System.out.println("server is up and ready for connections.....");
			serversock.receive(data);
		} catch (IOException e) {
			e.getMessage();
		}
		readPacket(data);
		}
	}

	public void readPacket(DatagramPacket packet) {

		InetAddress address = packet.getAddress();
		byte[] data = packet.getData();
		int portNum = packet.getPort();
		DataType type = Packet.getPacketType(new String(data).substring(0, 1));
		PacketTypes p = new PacketTypes();
		PacketTypes.Message m ;
		String newMessage ;


		if (type.equals(DataType.LOGIN)) {
			PacketTypes.LogIn log = p.new LogIn(data);
			 newMessage = "User " + log.getUserName() + " has joined the game";
			 m  = p.new Message(newMessage.getBytes());
			broadcastToAll(m.getMessage());
			addClient(log.getUserName(), address, portNum);
		} else if (type.equals(DataType.MESSAGE)) {
			broadcastToAll(data);
		}
	}

	synchronized private void addClient(String username, InetAddress hostAddress, int port) {

		int clientNum = connectedClients.size() + 1;
		ClientInformation playerData = new ClientInformation(port, username, hostAddress,clientNum);
		connectedClients.put(username,playerData);
	}

	/**
	 * Sends the message to everyone who is connected to the server
	 * 
	 * @param message
	 */
	synchronized public void broadcastToAll(byte[] message) {
		
		for(ClientInformation client : connectedClients.values()){
		sendData(message, client.getIpAddress(), client.getPortNum());
		}

	}

	/**
	 * 
	 * @param data
	 * @param ipAddress
	 * @param port
	 */
	synchronized public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			serversock.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
