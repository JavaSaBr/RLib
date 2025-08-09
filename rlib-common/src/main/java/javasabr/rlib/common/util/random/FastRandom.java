package javasabr.rlib.common.util.random;

import org.jspecify.annotations.NullMarked;

/**
 * @author JavaSaBr
 */
@NullMarked
public final class FastRandom implements Random {

  /**
   * Генератор чисел.
   */
  private final java.util.Random random;

  /**
   * Instantiates a new Fast random.
   */
  public FastRandom() {
    this.random = new java.util.Random();
  }

  @Override
  public void byteArray(byte [] array, int offset, int length) {
    length += offset;
    for (int i = offset; i < length; i++) {
      array[i] = (byte) nextInt(256);
    }
  }

  @Override
  public boolean chance(float chance) {

    if (chance < 0F) {
      return false;
    } else if (chance > 99.999999F) {
      return true;
    }

    return nextFloat() * nextInt(100) <= chance;
  }

  @Override
  public boolean chance(int chance) {

    if (chance < 1) {
      return false;
    } else if (chance > 99) {
      return true;
    }

    return nextInt(99) <= chance;
  }

  @Override
  public float nextFloat() {
    return random.nextFloat();
  }

  @Override
  public int nextInt() {
    return random.nextInt();
  }

  @Override
  public int nextInt(int max) {
    return random.nextInt(max);
  }

  @Override
  public int nextInt(int min, int max) {
    return min + nextInt(Math.abs(max - min) + 1);
  }

  @Override
  public long nextLong(long min, long max) {
    return min + Math.round(nextFloat() * Math.abs(max - min) + 1);
  }
}
