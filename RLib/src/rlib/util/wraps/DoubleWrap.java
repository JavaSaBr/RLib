package rlib.util.wraps;

/**
 * Обертка вокруг double.
 * 
 * @author Ronn
 */
final class DoubleWrap extends AbstractWrap {

	/** значение */
	private double value;

	@Override
	public double getDouble() {
		return value;
	}

	@Override
	public WrapType getWrapType() {
		return WrapType.DOUBLE;
	}

	@Override
	public void setDouble(double value) {
		this.value = value;
	}
}
