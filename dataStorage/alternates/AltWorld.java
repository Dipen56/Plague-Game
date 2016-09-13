package dataStorage.alternates;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import server.game.world.Area;
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
public class AltWorld {
	/**
	 * The area map.
	 */
	@XmlElement
	protected AltMapElement[][] board;


	public AltWorld(World world){
		if(world == null)
			throw new IllegalArgumentException("Argument is null");
		MapElement[][] board = world.getBoard();
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
	 * Only to be called by XML unmarshaller.
	 */
	public AltWorld(){

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
		return new World(board);
	}
}
