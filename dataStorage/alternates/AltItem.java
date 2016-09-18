package dataStorage.alternates;

/**
 * Used as a supertype over alternate classes to game items. Intended for use with XML parsing only.
 * @author anastadani
 *
 */
public class AltItem {
	AltItem(){

	}

	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){
		if(this instanceof AltAntidote)
			return ((AltAntidote)this).toString();
		if(this instanceof AltKey)
			return ((AltKey)this).toString();
		if(this instanceof AltTorch)
			return ((AltTorch)this).toString();
		return null;
	}
}
