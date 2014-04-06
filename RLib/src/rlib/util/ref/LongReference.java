package rlib.util.ref;

/**
 * Ссылка на тип данных long.
 * 
 * @author Ronn
 */
final class LongReference extends AbstractReference {

	/** значение по ссылке */
	private long value;

	@Override
	public long getLong() {
		return value;
	}

	@Override
	public ReferenceType getReferenceType() {
		return ReferenceType.LONG;
	}

	@Override
	public void setLong(long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "LongReference [value=" + value + "]";
	}
}
