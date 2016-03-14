package rlib.util.random;

/**
 * Фарика рандоминайзеров.
 *
 * @author Ronn
 */
public final class RandomFactory {

    /**
     * @return создание быстрого псевдо генератора.
     */
    public static Random newFastRandom() {
        return new FastRandom();
    }

    /**
     * @return создание медленного реалистичного генератора.
     */
    public static Random newRealRandom() {
        return new RealRandom();
    }

    private RandomFactory() {
        throw new RuntimeException();
    }
}
