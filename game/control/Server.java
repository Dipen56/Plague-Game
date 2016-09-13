package game.control;

import server.ThreadServer;

public class Server {
	public static final int PORTN_NUM = 9876;

	

	public Server(){
		
//		XmlFunctions XML = new XmlFunctions();
//		Game game = XML.loadFile();
		
		new ThreadServer().start();
		
	}
	
	
	
	public static void main(String args[]){
		new Server();
	}
	
	
	
	
	
}
