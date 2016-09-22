package dataStorage.alternates;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import server.game.world.Area;
import server.game.world.Chest;
import server.game.world.MapElement;
import server.game.world.Obstacle;
import server.game.world.Room;
import server.game.world.TransitionSpace;

/**
 * This class represents the an alternate version of the Area class, specifically for XML parsing.
 * @author Daniel Anastasi (anastadani 300145878)
 *
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class AltArea{

	/**
	 * The area map.
	 */
	@XmlElement()
    protected AltMapElement[][] board;

	
	/**
	 * A unique identifier for this area.
	 */
	@XmlElement
	public int areaId = -1;
	
	// Other fields are for subtyping from copied class

	/**
	 * Record of whether original Area was a subtype of area. Value can be world, room, or none.
	 */
	@XmlElement
	private String subtype = null;

	/**
	 * For room id. -1 if this area is not a room.
	 */
	@XmlElement
	protected int keyID = -1;

	/**
	 * For room field. isLocked. Always false if this area is not a room.
	 */
	@XmlElement
	boolean isLocked = false;
	
	/**
     * Empty position, which can be used to spawn players.
     */
    private int[][] playerPortals = null;

	public AltArea(Area area){
		if(area == null)
			throw new IllegalArgumentException("Argument is null");
		
		MapElement[][] board = area.getMap();
		this.board = new AltMapElement[board.length][board[0].length];
		MapElement me = null;

		//Copies orginal MapElements as AltMapElements.
		for(int row = 0; row < board.length; row++){
			for(int col = 0; col < board[0].length; col++){
				me = board[row][col];
				if(me instanceof Chest){
					this.board[row][col] = new AltChest((Chest)board[row][col]);
				}
				else if(me instanceof Obstacle){
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
		this.areaId = area.getAreaID();
		
		
		//Player portals
		List<int[]>originalPortals = area.getPlayerPortals();
		playerPortals = new int[originalPortals.size()][originalPortals.get(0).length];
		for(int i = 0; i < originalPortals.size(); i++){
			playerPortals[i] = originalPortals.get(i);
		}
		
		//Copies room specific fields.
		if(area instanceof Room){
			this.subtype = "room";
			this.keyID = ((Room)area).getKeyID();
			this.isLocked = ((Room)area).isLocked();
		}else{
			this.subtype = "none";
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
	 * Returns a copy of the area object on which this object was based.
	 * @return The copy of the World.
	 */
	public Area getOriginal(){
		if(this.areaId == -1)
			throw new RuntimeException("Area ID should not be -1.");
		if(this.board == null)
			throw new RuntimeException("Map should not be null.");
		
		MapElement[][] board = new MapElement[this.board.length][this.board[0].length];
		// Creates copies of the AltMapElements, as MapElements.
		for(int row = 0; row < board.length; row++){
			for(int col = 0; col < board[0].length; col++){
				AltMapElement ame = this.board[row][col];
				if(ame instanceof AltChest){
					board[row][col] = ((AltChest)this.board[row][col]).getOriginal();
				}
				else if(ame instanceof AltScrapPile){
					board[row][col] = ((AltScrapPile)this.board[row][col]).getOriginal();
				}
				else if(ame instanceof AltObstacle){
					board[row][col] = ((AltObstacle)this.board[row][col]).getOriginal();
				}
				else if(ame instanceof AltTransitionSpace){
					board[row][col] = ((AltTransitionSpace)this.board[row][col]).getOriginal();
				}
				else{
					throw new RuntimeException("Type of map element not recognised.");
				}
			}
		}
		
		//player portals
		List<int[]>playerPortals = new ArrayList<>();
		for(int i = 0; i < this.playerPortals.length; i++){
			playerPortals.add(this.playerPortals[i]);
		}
		
		Area newArea = null;
		if(this.subtype.equals("room")){
			newArea = new Room(board, this.areaId, this.keyID, this.isLocked,playerPortals);
		}
		else{
			newArea = new Area(board, this.areaId, playerPortals); 
		}

		newArea.registerPortals();		//Fills the player portals list
		return newArea;
	}
}
