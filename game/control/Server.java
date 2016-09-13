package game.control;



public class Server {
	
	

	public Server(){
		
//		XmlFunctions XML = new XmlFunctions();
//		Game game = XML.loadFile();
		
		new ThreadServer().start();
		
	}
	
	
	
	public static void main(String args[]){
		new Server();
	}
	
	
	
	
	
}
