package dataStorage.adapters;

/**
 * Used as a supertype over alternate classes to game items. Intended for use with XML parsing only.
 * @author Daniel Anastasi (anastadani 300145878)
 *
 */
public class ItemAdapter {
	ItemAdapter(){

	}

	/**
	 * Returns a string representation of this object's fields.
	 */
	public String toString(){
		if(this instanceof AntidoteAdapter)
			return ((AntidoteAdapter)this).toString();
		if(this instanceof KeyAdapter)
			return ((KeyAdapter)this).toString();
		if(this instanceof TorchAdapter)
			return ((TorchAdapter)this).toString();
		return null;
	}
}
