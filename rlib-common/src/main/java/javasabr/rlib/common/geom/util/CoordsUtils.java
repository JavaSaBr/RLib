package javasabr.rlib.common.geom.util;

import java.util.concurrent.ThreadLocalRandom;
import javasabr.rlib.common.geom.Vector3f;
import javasabr.rlib.common.util.ExtMath;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import org.jspecify.annotations.NullMarked;

/**
 * Реализация утильного класса с методами для рассчета координат.
 *
 * @author JavaSaBr
 */
@NullMarked
public final class CoordsUtils {

  private static final Logger LOGGER = LoggerManager.getLogger(CoordsUtils.class);

  /**
   * Генерация дуговых позиций.
   *
   * @param x целевая координата.
   * @param y целевая координата.
   * @param z целевая координата.
   * @param heading разворот.
   * @param radius радиус формирования.
   * @param count кол-во необходимых позиций.
   * @param degree положение на окружности центра дуги.
   * @param width ширина дуги.
   * @return массив позиций.
   */
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

  /**
   * Рассчет х координаты с учетом дистанции и разворота.
   *
   * @param x стартовая х координата.
   * @param distance дистанция сдвига.
   * @param radians направление сдвига.
   * @return новая х координата.
   */
  public static float calcX(float x, int distance, float radians) {
    return x + distance * (float) Math.cos(radians);
  }

  /**
   * Рассчет х координаты с учетом дистанции и разворота.
   *
   * @param x стартовая х координата.
   * @param distance дистанция сдвига.
   * @param heading направление сдвига.
   * @return новая х координата.
   */
  public static float calcX(float x, int distance, int heading) {
    return x + distance * (float) Math.cos(AngleUtils.headingToRadians(heading));
  }

  /**
   * Рассчет х координаты с учетом дистанции и разворота.
   *
   * @param x стартовая х координата.
   * @param distance дистанция сдвига.
   * @param heading направление сдвига.
   * @param offset смещение по градусам.
   * @return новая х координата.
   */
  public static float calcX(float x, int distance, int heading, int offset) {
    return x + distance * (float) Math.cos(AngleUtils.headingToRadians(heading + offset));
  }

  /**
   * Рассчет у координаты с учетом дистанции и разворота.
   *
   * @param y стартовая у координата.
   * @param distance дистанция сдвига.
   * @param radians направление сдвига.
   * @return новая у координата.
   */
  public static float calcY(float y, int distance, float radians) {
    return y + distance * (float) Math.sin(radians);
  }

  /**
   * Рассчет у координаты с учетом дистанции и разворота.
   *
   * @param y стартовая у координата.
   * @param distance дистанция сдвига.
   * @param heading направление сдвига.
   * @return новая у координата.
   */
  public static float calcY(float y, int distance, int heading) {
    return y + distance * (float) Math.sin(AngleUtils.headingToRadians(heading));
  }

  /**
   * Рассчет у координаты с учетом дистанции и разворота.
   *
   * @param y стартовая у координата.
   * @param distance дистанция сдвига.
   * @param heading направление сдвига.
   * @param offset смещение по градусам.
   * @return новая у координата.
   */
  public static float calcY(float y, int distance, int heading, int offset) {
    return y + distance * (float) Math.sin(AngleUtils.headingToRadians(heading + offset));
  }

  /**
   * Генерация массива круговых позиций с одинаковым интервлаом.
   *
   * @param x центральная координата.
   * @param y центральная координата.
   * @param z центральная координата.
   * @param radius радиус разброса.
   * @param count кол-во позиций.
   * @return массив позиций.
   */
  @SuppressWarnings("unchecked")
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

  /**
   * Получение точек по кругу от указанной точки.
   *
   * @param source массив исходных точек.
   * @param x координата центра.
   * @param y координата центра.
   * @param z координата центра.
   * @param count кол-во точек.
   * @param radius радиус от центра.
   * @return массив точек.
   */
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

  /**
   * Рассчет случайной точки.
   *
   * @param loc the loc
   * @param x центральная координата.
   * @param y центральная координата.
   * @param z центральная координата.
   * @param radiusMin минимальный радиус рандома.
   * @param radiusMax максимальный радиус рандома.
   * @return новая точка.
   */
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

  /**
   * Calculates the magnitude of the vector.
   *
   * @param x the X component.
   * @param y the Y component.
   * @return the length or magnitude of the vector.
   */
  public static float length(float x, float y) {
    return ExtMath.sqrt(lengthSquared(x, y));
  }

  /**
   * Calculates the squared value of the magnitude of the vector.
   *
   * @param x the X component.
   * @param y the Y component.
   * @return the magnitude squared of the vector.
   */
  public static float lengthSquared(float x, float y) {
    return x * x + y * y;
  }
}
