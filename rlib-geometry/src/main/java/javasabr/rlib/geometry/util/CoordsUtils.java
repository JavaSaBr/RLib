package javasabr.rlib.geometry.util;

import java.util.concurrent.ThreadLocalRandom;
import javasabr.rlib.common.util.ExtMath;
import javasabr.rlib.geometry.Vector3f;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;

/**
 * Utility methods for coordinate calculations and transformations.
 *
 * @since 10.0.0
 */
public final class CoordsUtils {

  private static final Logger LOGGER = LoggerManager.getLogger(CoordsUtils.class);

  public static Vector3f[] arcCoords(
      float x,
      float y,
      float z,
      int heading,
      int radius,
      int count,
      int degree,
      int width) {

    Vector3f[] vectors = new Vector3f[count];
    float current = AngleUtils.headingToDegree(heading) - degree;

    float min = current - width;
    float max = current + width;

    float angle = Math.abs(min - max) / count;

    for (int i = 0; i < count; i++) {

      Vector3f vector = new Vector3f();

      float radians = AngleUtils.degreeToRadians(min + angle * i);

      float newX = calcX(x, radius, radians);
      float newY = calcY(y, radius, radians);

      vector.set(newX, newY, z);

      vectors[i] = vector;
    }

    return vectors;
  }

  public static float calcX(float x, int distance, float radians) {
    return x + distance * (float) Math.cos(radians);
  }

  public static float calcX(float x, int distance, int heading) {
    return x + distance * (float) Math.cos(AngleUtils.headingToRadians(heading));
  }

  public static float calcX(float x, int distance, int heading, int offset) {
    return x + distance * (float) Math.cos(AngleUtils.headingToRadians(heading + offset));
  }

  public static float calcY(float y, int distance, float radians) {
    return y + distance * (float) Math.sin(radians);
  }

  public static float calcY(float y, int distance, int heading) {
    return y + distance * (float) Math.sin(AngleUtils.headingToRadians(heading));
  }

  public static float calcY(float y, int distance, int heading, int offset) {
    return y + distance * (float) Math.sin(AngleUtils.headingToRadians(heading + offset));
  }

  public static Vector3f[] circularCoords(float x, float y, float z, int radius, int count) {

    Vector3f[] locs = new Vector3f[count];
    float angle = 360F / count;

    for (int i = 1; i <= count; i++) {

      Vector3f loc = new Vector3f();
      float radians = AngleUtils.degreeToRadians(i * angle);

      float newX = calcX(x, radius, radians);
      float newY = calcY(y, radius, radians);

      loc.set(newX, newY, z);

      locs[i - 1] = loc;
    }

    return locs;
  }

  public static Vector3f[] getCircularPoints(
      Vector3f[] source,
      float x,
      float y,
      float z,
      int count,
      int radius) {

    if (count < 1) {
      return source;
    }

    float angle = 360F / count;

    for (int i = 1; i <= count; i++) {

      float radians = AngleUtils.degreeToRadians(angle * i);

      float newX = x + radius * (float) Math.cos(radians);
      float newY = y + radius * (float) Math.sin(radians);

      Vector3f point = source[i - 1];

      point.set(newX, newY, z);
    }

    return source;
  }

  public static Vector3f randomCoords(
      Vector3f loc,
      float x,
      float y,
      float z,
      int radiusMin,
      int radiusMax) {

    ThreadLocalRandom current = ThreadLocalRandom.current();

    if (radiusMax == 0 || radiusMax < radiusMin) {
      loc.set(x, y, z);
      return loc;
    }

    int radius = current.nextInt(radiusMin, radiusMax);
    float radians = AngleUtils.degreeToRadians(current.nextInt(0, 360));

    float newX = calcX(x, radius, radians);
    float newY = calcY(y, radius, radians);

    loc.set(newX, newY, z);

    return loc;
  }

  public static float length(float x, float y) {
    return ExtMath.sqrt(lengthSquared(x, y));
  }

  public static float lengthSquared(float x, float y) {
    return x * x + y * y;
  }
}
