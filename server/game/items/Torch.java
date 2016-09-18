package server.game.items;
<<<<<<< HEAD:server/game/items/Torch.java
=======

import java.awt.image.BufferedImage;
>>>>>>> master:server/game/items/Torch.java

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
    private int timeLimit;
    private boolean isFlaming;

    /**
     *
     * @param description
     * @param sprite
     */
    public Torch(String description) {
        super(description);
        timeLimit = INIT_TIME;
        isFlaming = false;
    }

    /**
     * To be used in game load by XML parser only.
<<<<<<< HEAD:server/game/items/Torch.java
     * 
     * @param Description
     *            of this torch.
     * @param Time
     *            limit on torch burning.
     * @param True
     *            if this torch is flaming.
     */
    public Torch(String description, int timeLimit, boolean isFlaming) {
        super(description);
        this.timeLimit = timeLimit;
        this.isFlaming = isFlaming;
    }

    /**
=======
     * @param Description of this torch.
     * @param Time limit on torch burning.
     * @param True if this torch is flaming.
     */
    public Torch(String description, int timeLimit, boolean isFlaming) {
    	super(description);
		this.timeLimit = timeLimit;
		this.isFlaming = isFlaming;
	}

	/**
>>>>>>> master:server/game/items/Torch.java
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
        if (timeLimit >= 0) {
            timeLimit--;
            if (timeLimit <= 0) {
                isFlaming = false;
            }
        }
    }

    public int getTimeLeft() {
        return timeLimit;
    }
    
    
    

    @Override
<<<<<<< HEAD:server/game/items/Torch.java
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isFlaming ? 1231 : 1237);
        result = prime * result + timeLimit;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
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
=======
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isFlaming ? 1231 : 1237);
		result = prime * result + timeLimit;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
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
>>>>>>> master:server/game/items/Torch.java
    public String toString() {
        int minutesLeft = timeLimit / 60;
        String minutesLeftString = minutesLeft >= 1 ? String.valueOf(minutesLeft)
                : "less than 1";
        return super.toString() + " I reckon it has about " + minutesLeftString
                + " minutes left to burn.";
    }
}
