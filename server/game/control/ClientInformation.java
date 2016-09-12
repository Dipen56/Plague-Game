package server.game.control;

import java.net.InetAddress;

import server.game.player.Player;

public class ClientInformation {
	
	private int portNum;
	private String username;
	private Player player;
	private InetAddress ipAddress;
	
	
	public ClientInformation(int portNum, String username, Player player, InetAddress ip){
		this.portNum = portNum; 
		this.username = username;
		this.player = player;
		this.ipAddress = ip;
	}
	
	public int getPortNum(){
		return portNum;
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
