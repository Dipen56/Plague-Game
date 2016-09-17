package dataStorage.alternates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import server.game.world.Area;
import server.game.world.Chest;
import server.game.world.MapElement;
import server.game.world.Obstacle;
import server.game.world.Room;
import server.game.world.TransitionSpace;
import server.game.world.World;

/**
 * This class represents the an alternate version of the Room class, specifically for XML parsing.
 *
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AltRoom extends AltArea{
	
	/**
	 * The id for the key to this room.
	 */
	//@XmlElement
	//private int keyID;
	/**
	 * Is true when the room is locked.
	 */
	//@XmlElement
	//boolean isLocked;


	public AltRoom(Area area) {
		super(area);						
		/*keyID = ((Room)area).getKeyID();
		isLocked = ((Room)area).isLocked();
		*/
	}

	/**
	 * Only to be called by XML parser.
	 */
	AltRoom(){

	}

	/**
	 * Creates a copy of the original Room which this object was based on.
	 * @return The Room copy.
	 */
	public Room getOriginal(){
		int i = this.board.length;		//testline
		int j = this.board[0].length; 	//testline
		MapElement[][] board = new MapElement[this.board.length][this.board[0].length];
		// Creates copies of the AltMapElements, as MapElements.
		for(int row = 0; row < board.length; row++){
			for(int col = 0; col < board[0].length; col++){
				AltMapElement ame = this.board[row][col];
				if(ame instanceof AltChest){
					board[row][col] = ((AltChest)this.board[row][col]).getOriginal();
				}
				else if(ame instanceof AltObstacle){
					board[row][col] = ((AltObstacle)this.board[row][col]).getOriginal();
				}
				else if(ame instanceof AltTransitionSpace){
					board[row][col] = ((AltTransitionSpace)this.board[row][col]).getOriginal();
				}
				
				else{
					//This should not happen.
				}
			}
		}
		
		//Fills the player portals list
		Room newArea = new Room(board, keyID, isLocked);
		newArea.registerPortals();			//Fills the player portals list
		return newArea;
	}
	

}
