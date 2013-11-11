package rlib.util;

/**
 * Набор дополнительных математических методов.
 * 
 * @author Ronn
 */
public final class ExtMath {

	/** The value PI as a float. (180 degrees) */
	public static final float PI = (float) Math.PI;

	/**
	 * Returns the arc cosine of a value.<br>
	 * Special cases:
	 * <ul>
	 * <li>If fValue is smaller than -1, then the result is PI.
	 * <li>If the argument is greater than 1, then the result is 0.
	 * </ul>
	 * 
	 * @param fValue The value to arc cosine.
	 * @return The angle, in radians.
	 * @see java.lang.Math#acos(double)
	 */
	public static float acos(final float fValue) {

		if(-1.0f < fValue) {

			if(fValue < 1.0f) {
				return (float) Math.acos(fValue);
			}

			return 0.0f;
		}

		return PI;
	}

	/**
	 * Returns cosine of an angle. Direct call to java.lang.Math
	 * 
	 * @see Math#cos(double)
	 * @param v The angle to cosine.
	 * @return the cosine of the angle.
	 */
	public static float cos(final float v) {
		return (float) Math.cos(v);
	}

	/**
	 * Returns 1/sqrt(fValue)
	 * 
	 * @param fValue The value to process.
	 * @return 1/sqrt(fValue)
	 * @see java.lang.Math#sqrt(double)
	 */
	public static float invSqrt(final float fValue) {
		return (float) (1.0f / Math.sqrt(fValue));
	}

	/**
	 * Returns the sine of an angle. Direct call to java.lang.Math
	 * 
	 * @see Math#sin(double)
	 * @param v The angle to sine.
	 * @return the sine of the angle.
	 */
	public static float sin(final float v) {
		return (float) Math.sin(v);
	}

	/**
	 * Returns the square root of a given value.
	 * 
	 * @param fValue The value to sqrt.
	 * @return The square root of the given value.
	 * @see java.lang.Math#sqrt(double)
	 */
	public static float sqrt(final float fValue) {
		return (float) Math.sqrt(fValue);
	}

	private ExtMath() {
		throw new RuntimeException();
	}
}
