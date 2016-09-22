package dataStorage.alternates;

/**
 * Used as a supertype over alternate classes to game items. Intended for use with XML parsing only.
 * @author Daniel Anastasi (anastadani 300145878)
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
