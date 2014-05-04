package rlib.util;

/**
 * Набор дополнительных математических методов.
 * 
 * @author Ronn
 */
public final class ExtMath {

	/** конвертированное в float число PI */
	public static final float PI = (float) Math.PI;

	/**
	 * Получение арккосинуса указанного значения. Если указанное значение меньше
	 * -1, то возвратиться число ПИ, если же больше 1, то вернеться 0.
	 * 
	 * @param value интересуемое значение.
	 * @return угол в радианах.
	 */
	public static float acos(final float value) {

		if(-1.0F < value) {

			if(value < 1.0F) {
				return (float) Math.acos(value);
			}

			return 0.0F;
		}

		return PI;
	}

	/**
	 * Возвращает косинус угла указанного значения. Прямой вызов
	 * {@link Math#cos(double)}
	 * 
	 * @see Math#cos(double)
	 * @param value значение угла для вычисление косинуса.
	 * @return косинус указанного угла.
	 */
	public static float cos(final float value) {
		return (float) Math.cos(value);
	}

	/**
	 * Получение обратного корня указанного числа.
	 * 
	 * @param value интересуемое число.
	 * @return 1 / sqrt(value).
	 */
	public static float invSqrt(final float value) {
		return (1.0F / sqrt(value));
	}

	/**
	 * Получение синуса указанного угла, прямой вызов {@link Math#sin(double)}.
	 * 
	 * @see Math#sin(double)
	 * @param value интересуемое значение угла.
	 * @return синус указанного угла.
	 */
	public static float sin(final float value) {
		return (float) Math.sin(value);
	}

	/**
	 * Получение корня указанного значения, прямой вызов
	 * {@link Math#sqrt(double)}.
	 * 
	 * @param value интересуемое значение.
	 * @return корень указанного значения.
	 * @see Math#sqrt(double)
	 */
	public static float sqrt(final float value) {
		return (float) Math.sqrt(value);
	}

	private ExtMath() {
		throw new RuntimeException();
	}
}
