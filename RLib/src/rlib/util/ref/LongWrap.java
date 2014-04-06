package rlib.util.ref;

/**
 * Обертка вокруг long.
 * 
 * @author Ronn
 */
final class LongWrap extends AbstractReference {

	/** обернутое значение */
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
		return String.valueOf(value);
	}
}
