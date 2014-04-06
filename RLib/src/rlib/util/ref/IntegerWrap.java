package rlib.util.ref;

/**
 * Обертка вокруг int.
 * 
 * @author Ronn
 */
final class IntegerWrap extends AbstractReference {

	/** обернутое значение */
	private int value;

	@Override
	public int getInt() {
		return value;
	}

	@Override
	public ReferenceType getReferenceType() {
		return ReferenceType.INTEGER;
	}

	@Override
	public void setInt(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
