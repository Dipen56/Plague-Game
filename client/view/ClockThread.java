package client.view;

/**
 * This class is used for periodically updating GUI and Renderer.
 * 
 * @author Rafaela & Hector
 *
 */
public class ClockThread extends Thread {

    /**
     * The pointer to controller
     */
    private ClientUI controller;

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
    public ClockThread(int delay, ClientUI controller) {
        this.controller = controller;
        this.delay = delay;
    }

    @Override
    public void run() {
        while (true) {
            // Loop forever
            try {
                Thread.sleep(delay);

                // update Renderer and GUI.
                controller.updateRenderAndGui();

            } catch (InterruptedException e) {
                // should never happen
            }
        }
    }

}
