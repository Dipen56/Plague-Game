package game.items;

import javax.swing.ImageIcon;

public class Torch extends Item implements Breakable, Tradable {

    private static final int INIT_TIME = 100;
    private int timeLimit;
    private boolean isFlaming;

    /**
     * 
     * @param description
     * @param sprite
     */
    public Torch(String description, ImageIcon sprite) {
        super(description, sprite);
        timeLimit = INIT_TIME;
        isFlaming = false;
    }

    /**
     * Get the current status of this torch, i.e. whether it is flaming.
     * 
     * @return
     */
    public boolean isFlaming() {
        return isFlaming;
    }

    /**
     * This method sets whether the torch is lighted up or not. It will only take effect
     * when this torch is not used up.
     * 
     * @param isFlaming
     */
    public void setIsFlaming(boolean isFlaming) {
        if (timeLimit > 0) {
            this.isFlaming = isFlaming;
        }
    }

    /**
     * This method burns the torch (time left decreased by 1)
     */
    public void Burn() {
        timeLimit--;
        if (timeLimit <= 0) {
            this.isFlaming = false;
        }
    }

}
