package rlib.util.random;

/**
 * Интерфейс для реализации рандоминайзера.
 *
 * @author Ronn
 */
public interface Random {

    /**
     * Заполнение массива случайными значениями.
     *
     * @param array  заполняемый массив.
     * @param offset отступ в массиве.
     * @param length кол-во заполняемых байтов.
     */
    public void byteArray(byte[] array, int offset, int length);

    /**
     * Рассчет срабатывания шанса.
     *
     * @param chance шанс от 0.0 до 100.0.
     * @return сработал ли шанс.
     */
    public boolean chance(float chance);

    /**
     * Рассчет срабатывания шанса.
     *
     * @param chance шанс от 0 до 100.
     * @return сработал ли шанс.
     */
    public boolean chance(int chance);

    /**
     * Генерация случайного вещественного числа.
     *
     * @return число от 0.0 до 1.0
     */
    public float nextFloat();

    /**
     * Генерация случайного целого числа.
     *
     * @return число от -2.5ккк до 2.5ккк
     */
    public int nextInt();

    /**
     * Возвращает случайное число [0, max].
     *
     * @param max максимальное число.
     * @return случайное число [0, max]
     */
    public int nextInt(int max);

    /**
     * Возвращает случайное число [min, max].
     *
     * @param min минимальное число.
     * @param max максимальное число.
     * @return случайное число [min, max]
     */
    public int nextInt(int min, int max);

    /**
     * Возвращает случайное число [min, max].
     *
     * @param min минимальное число.
     * @param max максимальное число.
     * @return случайное число [min, max]
     */
    public long nextLong(long min, long max);
}
