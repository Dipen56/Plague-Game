package server.game.world;

import dataStorage.alternates.AltWorld;

/**
 * This class represents the logic board of the game.
 *
 * @author Hector (Fang Zhao 300364061), Daniel Anastasi 300145878
 *
 */
public class World extends Area {

    /**
     * Constructor.
     *
     * @param filename
     */
    public World(String filename) {
        super(filename);

    }

    /**
     * Constructor used in test. Probably will be discarded.
     *
     * @param width
     * @param height
     * @param board
     */
    public World(MapElement[][] board) {
        super(board);
    }

    
  
}
