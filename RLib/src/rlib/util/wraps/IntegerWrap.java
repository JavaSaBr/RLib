package rlib.util.wraps;

/**
 * Обертка вокруг int.
 * 
 * @author Ronn
 */
final class IntegerWrap extends AbstractWrap {

	/** обернутое значение */
	private int value;

	@Override
	public int getInt() {
		return value;
	}

	@Override
	public WrapType getWrapType() {
		return WrapType.INTEGER;
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
