package dataStorage.alternates;

import javax.xml.bind.annotation.XmlElement;

import server.game.world.MapElement;
import server.game.world.Obstacle;
import server.game.world.Room;
import server.game.world.TransitionSpace;

/**
 * This class represents the an alternate version of the Room class, specifically for XML parsing.
 *
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878.
 *
 */
public class AltRoom {
	/**
	 * The id for the key to this room.
	 */
	@XmlElement
	private int keyID;
	/**
	 * Is true when the room is locked.
	 */
	@XmlElement
	boolean isLocked;
	/**
	 * The exit out of this room.
	 */
	@XmlElement
	private AltTransitionSpace exit;

	/**
	 * The area map.
	 */
	@XmlElement
	protected AltMapElement[][] board;

	public AltRoom(Room room) {
		if(room == null)
			throw new IllegalArgumentException("Argument is null");
		keyID = room.getKeyID();
		isLocked = room.isLocked();
		exit = new AltTransitionSpace(room.getExit());

		MapElement[][] board = room.getBoard();
		this.board = new AltMapElement[board.length][board[0].length];
		//Copies orginal MapElements as AltMapElements.
		for(int row = 0; row < board.length; row++){
			for(int col = 0; col < board[0].length; col++){
				MapElement me = board[row][col];
				if(me instanceof Obstacle){
					this.board[row][col] = new AltObstacle((Obstacle)board[row][col]);
				}
				else if(me instanceof TransitionSpace){
					this.board[row][col] = new AltTransitionSpace((TransitionSpace)board[row][col]);
				}
				else{
					//This should not happen.
				}
			}
		}
	}

	/**
	 * Only to be called by XML parser.
	 */
	public AltRoom(){

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
				if(ame instanceof AltObstacle){
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
		TransitionSpace ts = exit.getOriginal();

		return new Room(board, keyID, isLocked, ts);
	}

}
