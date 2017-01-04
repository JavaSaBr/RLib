package rlib.util.random;

import org.jetbrains.annotations.NotNull;

/**
 * The factory of randoms implementations.
 *
 * @author JavaSaBr
 */
public final class RandomFactory {

    @NotNull
    public static Random newFastRandom() {
        return new FastRandom();
    }

    private RandomFactory() {
        throw new RuntimeException();
    }
}
