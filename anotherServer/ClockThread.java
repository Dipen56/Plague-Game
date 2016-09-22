package anotherServer;

import server.game.Game;

/**
 * This class is used for periodically job.
 * 
 * could be updating rendering panel.
 * 
 * could be updating game status.
 * 
 * needs to be decided.
 * 
 * @author Rafaela (Just put your Id here)
 *
 */
public class ClockThread extends Thread {

    /**
     * The game
     */
    private final Game game;
    /**
     * the period between every update.
     */
    private final int delay;

    /**
     * Constructor
     * 
     * @param delay
     * @param game
     */
    public ClockThread(int delay, Game game) {
        this.game = game;
        this.delay = delay;
    }

    @Override
    public void run() {
        while (true) {
            // Loop forever
            try {
                Thread.sleep(delay);

                // rendering update

            } catch (InterruptedException e) {
                // should never happen
            }
        }
    }

}
