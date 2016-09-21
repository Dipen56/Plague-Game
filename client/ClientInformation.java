package client;

import java.net.InetAddress;

import server.game.player.Player;

public class ClientInformation {
	
	private int portNum;
	private String username;
	private Player player;
	private InetAddress ipAddress;
	private int clientnum;
	
	
	public ClientInformation(int portNum, String username, InetAddress ip,int num){
		this.portNum = portNum; 
		this.username = username;
		this.ipAddress = ip;
		this.clientnum = num;
	}
	
	public int getPortNum(){
		return portNum;
	}
	public int getClientNum(){
		return clientnum;
	}
	public Player getPlayer(){
		return player;
	}
	public String getUsername(){
		return username;
	}
	public InetAddress getIpAddress(){
		return ipAddress;
	}	
	
}
