package client.view;

import client.rendering.Rendering;
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
 * @author Rafaela & Hector
 *
 */
public class ClockThread extends Thread {

    /**
     * The client side renderer
     */
    private final Rendering renderer;
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
    public ClockThread(int delay, Rendering renderer) {
        this.renderer = renderer;
        this.delay = delay;
    }

    @Override
    public void run() {
        while (true) {
            // Loop forever
            try {
                Thread.sleep(delay);

                // rendering update
                renderer.redraw();

            } catch (InterruptedException e) {
                // should never happen
            }
        }
    }

}
