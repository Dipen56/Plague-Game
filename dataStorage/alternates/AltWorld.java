package dataStorage.alternates;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import server.game.world.Area;
import server.game.world.Chest;
import server.game.world.MapElement;
import server.game.world.Obstacle;
import server.game.world.Position;
import server.game.world.TransitionSpace;
import server.game.world.World;

/**
 * This class represents the an alternate version of the World class, specifically for XML parsing.
 *
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AltWorld extends AltArea{

	public AltWorld(Area area){
		super(area);
	}

	/**
	 * Only to be called by XML unmarshaller.
	 */
	AltWorld(){

	}

	/**
	 * Returns a copy of the world object on which this object was based.
	 * @return The copy of the World.
	 */
	public World getOriginal(){
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
		World newArea = new World(board);
		newArea.registerPortals();			//Fills the player portals list
		return newArea;
	}


	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){
		StringBuffer b = new StringBuffer("");
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[0].length; j++){
				b.append(board[i][j]);
			}
		}
		return b.toString();
	}
}
