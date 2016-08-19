package rlib.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс для работы со случайными значениями.
 *
 * @author JavaSaBr
 */
public final class Rnd {

    /**
     * Генерирование байтового массива со случайными значениями.
     *
     * @param size размер случайного массива.
     * @return новый случайный массив.
     */
    public static byte[] byteArray(final int size) {

        final ThreadLocalRandom random = ThreadLocalRandom.current();

        final byte[] result = new byte[size];

        for (int i = 0; i < size; i++) {
            result[i] = (byte) nextInt(random, 256);
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

        if (chance < 0F) {
            return false;
        } else if (chance > 99.999999F) {
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

        if (chance < 1) {
            return false;
        } else if (chance > 99) {
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
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextFloat();
    }

    /**
     * Генерация случайного целого числа.
     *
     * @return число от -2.5ккк до 2.5ккк
     */
    public static int nextInt() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt();
    }

    /**
     * Возвращает случайное число [0, max].
     *
     * @param max максимальное число.
     * @return случайное число [0, max]
     */
    public static int nextInt(final int max) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(max);
    }

    /**
     * Возвращает случайное число [0, max].
     *
     * @param max максимальное число.
     * @return случайное число [0, max]
     */
    public static int nextInt(final ThreadLocalRandom random, final int max) {
        return random.nextInt(max);
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

    private Rnd() {
        throw new RuntimeException();
    }
}