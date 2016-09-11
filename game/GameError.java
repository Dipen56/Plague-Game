package game;

/**
 * This class represents a game error. As the server needs to run without crash, it is not
 * encouraged to throw game errors.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
@SuppressWarnings("serial")
public class GameError extends RuntimeException {

    public GameError(String string) {
        super(string);
    }

}
