package server.game;

import server.dataStorage.alternates.AltGame;

/**
 * The Clock Thread is responsible for producing a consistent "pulse" which is used to
 * update the game state, and refresh the display. Setting the pulse rate too high may
 * cause problems, when the point is reached at which the work done to service a given
 * pulse exceeds the time between pulses.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class ClockThread extends Thread {

    // XXX should be in main class
    public static final int DEFAULT_CLK_PERIOD = 20;

    // XXX may not be any of use
    private static final int DEFAULT_BROADCAST_CLK_PERIOD = 5;

    private final int delay;
    private final Game game;

    // another field, rendering panel.

    /**
     * Constructor
     *
     * TODO this constructor should take another argument additionally, which is the
     * rendering panel.
     *
     * @param delay
     * @param game
     */
    public ClockThread(int delay, Game game) {
        this.delay = delay;
        this.game = game;
    }

    @Override
    public void run() {

        while (true) {
            // Loop forever
            try {
                Thread.sleep(delay);
                game.update();
            } catch (InterruptedException e) {
                // should never happen
            }
        }
    }
}
