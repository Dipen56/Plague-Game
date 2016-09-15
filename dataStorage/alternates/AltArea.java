package dataStorage.alternates;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import server.game.world.Area;
import server.game.world.MapElement;
import server.game.world.Obstacle;
import server.game.world.Room;
import server.game.world.TransitionSpace;
import server.game.world.World;

/**
 * This class represents the an alternate version of the Area class, specifically for XML parsing.
 * @author Daniel Anastasi 300145878
 *
 */
public class AltArea{

	/**
	 * The area map.
	 */
	@XmlElement
    protected AltMapElement[][] board;

	// Other fields are for subtyping from copied class

	/**
	 * Record of whether original Area was a subtype of area. Value can be world, room, or none.
	 */
	@XmlElement
	private String subtype = null;

	/**
	 * For room id. -1 if this area is not a room.
	 */
	private int keyID = -1;

	/**
	 * For room field. isLocked. Always false if this area is not a room.
	 */
	@XmlElement
	boolean isLocked = false;
	/**
	 * The exit out of this room.
	 */
	@XmlElement
	private AltTransitionSpace exit;

	public AltArea(Area area){
		if(area == null)
			throw new IllegalArgumentException("Argument is null");
		MapElement[][] board = area.getBoard();
		this.board = new AltMapElement[board.length][board[0].length];
		MapElement me = null;

		//Copies orginal MapElements as AltMapElements.
		for(int row = 0; row < board.length; row++){
			for(int col = 0; col < board[0].length; col++){
				me = board[row][col];
				if(me instanceof Obstacle){
					this.board[row][col] = new AltObstacle((Obstacle)board[row][col]);
				}
				else if(me instanceof TransitionSpace){
					this.board[row][col] = new AltTransitionSpace((TransitionSpace)board[row][col]);
				}
				else{
					continue;//This should not happen.
				}
			}
		}
		if(area instanceof World){
			this.subtype = "world";
		}else if(area instanceof Room){
			this.subtype = "room";
			this.keyID = ((Room)area).getKeyID();
			this.isLocked = ((Room)area).isLocked();
		}
	}

	/**
	 * Only to be called by XML unmarshaller.
	 */
	AltArea(){

	}

	public String getSubtype(){
		return this.subtype;
	}

	/**
	 * Returns a copy of the world object on which this object was based.
	 * @return The copy of the World.
	 */
	public Area getOriginal(){
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
		Area newArea = null;
		if(this.subtype.equals("world")){
			newArea = new World(board);
		}else if(this.subtype.equals("room")){
			newArea = new Room(board, this.keyID, this.isLocked);
		}else{
			newArea = new Area(board);
		}
		return newArea;
	}
}
