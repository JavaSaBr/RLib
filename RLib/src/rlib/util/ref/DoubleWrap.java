package rlib.util.ref;

/**
 * Обертка вокруг double.
 * 
 * @author Ronn
 */
final class DoubleWrap extends AbstractReference {

	/** значение */
	private double value;

	@Override
	public double getDouble() {
		return value;
	}

	@Override
	public ReferenceType getReferenceType() {
		return ReferenceType.DOUBLE;
	}

	@Override
	public void setDouble(double value) {
		this.value = value;
	}
}
