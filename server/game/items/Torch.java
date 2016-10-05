package server.game.items;

/**
 * This class represents a torch.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Torch extends Item implements Destroyable, Tradable {

    /**
     * Each torch can burn up to 3 minute.
     */
    private static final int INIT_TIME = 3 * 60;

    /**
     * The "health" of this torch. The number indicates how many seconds left to burn.
     */
    private int timeLimit;

    /**
     * Is this torch lighted?
     */
    private boolean isFlaming;

    /**
     * Constructor
     * 
     * @param description
     *            --- the description
     */
    public Torch(String description) {
        super(description);
        timeLimit = INIT_TIME;
        isFlaming = false;
    }

    /**
     * To be used in game load by XML parser only.
     * 
     * @param description
     *            --- the description of this torch.
     * @param timeLimit
     *            --- Time limit on torch burning.
     * @param isFlaming
     *            --- True if this torch is flaming.
     */
    public Torch(String description, int timeLimit, boolean isFlaming) {
        super(description);
        this.timeLimit = timeLimit;
        this.isFlaming = isFlaming;
    }

    /**
     * Get the current status of this torch, i.e. whether it is flaming.
     *
     * @return --- true if it is burning; or false if it isn't.
     */
    public boolean isFlaming() {
        return isFlaming;
    }

    /**
     * This method sets whether the torch is lighted up or not. It will only take effect
     * when this torch is not used up.
     *
     * @param isFlaming
     *            --- true to light it, false to put it down.
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
        if (timeLimit >= 0) {
            timeLimit--;
            if (timeLimit <= 0) {
                isFlaming = false;
            }
        }
    }

    /**
     * Get the time left of this torch.
     * 
     * @return --- how many seconds left for this torch to burn
     */
    public int getTimeLeft() {
        return timeLimit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (isFlaming ? 1231 : 1237);
        result = prime * result + timeLimit;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Torch other = (Torch) obj;
        if (isFlaming != other.isFlaming)
            return false;
        if (timeLimit != other.timeLimit)
            return false;
        return true;
    }

    @Override
    public String toString() {

        // for easy testing
        return super.toString() + " I reckon it has about " + timeLimit
                + " seconds left to burn.";

        // int minutesLeft = timeLimit / 60;
        // String minutesLeftString = minutesLeft >= 1 ? String.valueOf(minutesLeft)
        // : "less than 1";
        // return super.toString() + " I reckon it has about " + minutesLeftString
        // + " minutes left to burn.";
    }
}
