package rlib.util;

import java.util.Random;

/**
 * Класс для работы со случайными значениями.
 * 
 * @author Ronn
 */
@Deprecated
public abstract class Rnd {

	/**
	 * Генерирование байтового массива со случайными значениями.
	 * 
	 * @param size размер случайного массива.
	 * @return новый случайный массив.
	 */
	public static byte[] byteArray(final int size) {

		final byte[] result = new byte[size];

		for(int i = 0; i < size; i++) {
			result[i] = (byte) nextInt(256);
		}

		return result;
	}

	/**
	 * Рассчет срабатывания шанса.
	 * 
	 * @param chance шанс от 0.0 до 100.0.
	 * @return сработал ли шанс.
	 */
	public static boolean chance(final float chance) {

		if(chance < 0F) {
			return false;
		}

		if(chance > 99.999999F) {
			return true;
		}

		return nextFloat() * nextInt(100) <= chance;
	}

	/**
	 * Рассчет срабатывания шанса.
	 * 
	 * @param chance шанс от 0 до 100.
	 * @return сработал ли шанс.
	 */
	public static boolean chance(final int chance) {

		if(chance < 1) {
			return false;
		}

		if(chance > 99) {
			return true;
		}

		return nextInt(99) <= chance;
	}

	/**
	 * Генерация случайного вещественного числа.
	 * 
	 * @return число от 0.0 до 1.0
	 */
	public static float nextFloat() {
		return rnd.nextFloat();
	}

	/**
	 * Генерация случайного целого числа.
	 * 
	 * @return число от -2.5ккк до 2.5ккк
	 */
	public static int nextInt() {
		return rnd.nextInt();
	}

	/**
	 * Возвращает случайное число [0, max].
	 * 
	 * @param max максимальное число.
	 * @return случайное число [0, max]
	 */
	public static int nextInt(final int max) {
		return rnd.nextInt(max);
	}

	/**
	 * Возвращает случайное число [min, max].
	 * 
	 * @param min минимальное число.
	 * @param max максимальное число.
	 * @return случайное число [min, max]
	 */
	public static int nextInt(final int min, final int max) {
		return min + nextInt(Math.abs(max - min) + 1);
	}

	/**
	 * Возвращает случайное число [min, max].
	 * 
	 * @param min минимальное число.
	 * @param max максимальное число.
	 * @return случайное число [min, max]
	 */
	public static long nextLong(final long min, final long max) {
		return min + Math.round(nextFloat() * Math.abs(max - min) + 1);
	}

	/**
	 * Потокобезопасное генерирование байтового массива со случайными
	 * значениями.
	 * 
	 * @param size размер случайного массива.
	 * @return новый случайный массив.
	 */
	public static byte[] safeByteArray(final int size) {

		final Random rnd = LOCAL_RANDOM.get();

		final byte[] result = new byte[size];

		for(int i = 0; i < size; i++) {
			result[i] = (byte) rnd.nextInt(256);
		}

		return result;
	}

	/**
	 * Потокобезопасный рассчет срабатывания шанса.
	 * 
	 * @param chance шанс от 0.0 до 100.0.
	 * @return сработал ли шанс.
	 */
	public static boolean safeChance(final float chance) {

		if(chance < 0F) {
			return false;
		}

		if(chance > 99.999999F) {
			return true;
		}

		final Random rnd = LOCAL_RANDOM.get();

		return rnd.nextFloat() * rnd.nextInt(100) <= chance;
	}

	/**
	 * Потокобезопасная генерация случайного вещественного числа.
	 * 
	 * @return число от 0.0 до 1.0
	 */
	public static float safeNextFloat() {
		final Random rnd = LOCAL_RANDOM.get();
		return rnd.nextFloat();
	}

	/**
	 * Потокобезопасная генерация случайного целого числа.
	 * 
	 * @return число от -2.5ккк до 2.5ккк
	 */
	public static int safeNextInt() {
		final Random rnd = LOCAL_RANDOM.get();
		return rnd.nextInt();
	}

	/**
	 * Потокобезопасная генеразция случайного числа в интервале [0, max].
	 * 
	 * @param max максимальное число.
	 * @return случайное число [0, max]
	 */
	public static int safeNextInt(final int max) {
		final Random rnd = LOCAL_RANDOM.get();
		return rnd.nextInt(max);
	}

	/**
	 * ВПотокобезопасная генеразция случайного числа в интервале [min, max].
	 * 
	 * @param min минимальное число.
	 * @param max максимальное число.
	 * @return случайное число [min, max]
	 */
	public static int safeNextInt(final int min, final int max) {
		return min + safeNextInt(Math.abs(max - min) + 1);
	}

	/**
	 * Потокобезопасная генеразция случайного числа в интервале [min, max].
	 * 
	 * @param min минимальное число.
	 * @param max максимальное число.
	 * @return случайное число [min, max]
	 */
	public static long safeNextLong(final long min, final long max) {
		return min + Math.round(safeNextFloat() * Math.abs(max - min) + 1);
	}

	private static final ThreadLocal<Random> LOCAL_RANDOM = new ThreadLocal<Random>() {

		@Override
		protected Random initialValue() {
			return new Random();
		}
	};

	private static final Random rnd = new Random();
}