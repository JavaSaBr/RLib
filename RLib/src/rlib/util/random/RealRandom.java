package rlib.util.random;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import rlib.util.NumberUtils;

import static java.lang.Math.abs;
import static java.lang.Math.round;

/**
 * Реализация медленного рандоминайзера с очень естественным рандомом.
 *
 * @author Ronn
 */
public final class RealRandom implements Random {

    private final ThreadLocal<byte[]> LOCAL_BUFFER = ThreadLocal.withInitial(() -> new byte[8]);

    /**
     * Генератор чисел.
     */
    private final SecureRandom secureRandom;

    /**
     * Генератор чисел.
     */
    private final java.util.Random random;

    /**
     * Счетчик использований.
     */
    private volatile int counter;

    public RealRandom() {
        this.random = new java.util.Random();

        try {
            this.secureRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void byteArray(final byte[] array, final int offset, int length) {

        length += offset;

        for (int i = offset; i < length; i++) {
            array[i] = (byte) nextInt(256);
        }
    }

    @Override
    public boolean chance(final float chance) {

        if (chance < 0F) {
            return false;
        } else if (chance > 99.999999F) {
            return true;
        }

        return nextFloat() * nextInt(100) <= chance;
    }

    @Override
    public boolean chance(final int chance) {

        if (chance < 1) {
            return false;
        } else if (chance > 99) {
            return true;
        }

        return nextInt(99) <= chance;
    }

    @Override
    public float nextFloat() {
        increaseUse();
        return random.nextFloat();
    }

    @Override
    public int nextInt() {
        increaseUse();
        return random.nextInt();
    }

    @Override
    public int nextInt(final int max) {
        increaseUse();
        return random.nextInt(max);
    }

    @Override
    public int nextInt(final int min, final int max) {
        increaseUse();
        return min + nextInt(abs(max - min) + 1);
    }

    @Override
    public long nextLong(final long min, final long max) {
        increaseUse();
        return min + round(nextFloat() * abs(max - min) + 1);
    }

    public void increaseUse() {
        counter++;

        if (counter < 8) return;

        counter = 0;

        final byte[] bytes = LOCAL_BUFFER.get();

        secureRandom.nextBytes(bytes);

        final long makeLong = NumberUtils.makeLong(bytes);

        random.setSeed(makeLong);
    }
}
