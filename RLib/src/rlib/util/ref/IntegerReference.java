package rlib.util.ref;

/**
 * Ссылка на тип данных int.
 * 
 * @author Ronn
 */
final class IntegerReference extends AbstractReference {

	/** значение по ссылке */
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
	public void setInt(final int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "IntegerReference [value=" + value + "]";
	}
}
