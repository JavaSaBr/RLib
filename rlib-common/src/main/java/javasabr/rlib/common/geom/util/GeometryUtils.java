package javasabr.rlib.common.geom.util;

import org.jspecify.annotations.NullMarked;

/**
 * Набор геометрически методов.
 *
 * @author JavaSaBr
 */
@NullMarked
public final class GeometryUtils {

  /**
   * Рассчет расстояния между 2мя точками.
   *
   * @param startX координата первой точки.
   * @param startY координата первой точки.
   * @param startZ координата первой точки.
   * @param targetX координата второй точки.
   * @param targetY координата второй точки.
   * @param targetZ координата второй точки.
   * @return расстояние между точками.
   */
  public static float getDistance(
      float startX,
      float startY,
      float startZ,
      float targetX,
      float targetY,
      float targetZ) {
    return (float) Math.sqrt(getSquareDistance(startX, startY, startZ, targetX, targetY, targetZ));
  }

  /**
   * Возвращает расстояние от точки до отрезка.
   *
   * @param startX начальная координата отрезка.
   * @param startY начальная координата отрезка.
   * @param endX конечная координата отрезка.
   * @param endY конечная координата отрезка.
   * @param targetX координата точки.
   * @param targetY координата точки.
   * @return расстояние от точки до отрезка.
   */
  public static float getDistanceToLine(
      float startX,
      float startY,
      float endX,
      float endY,
      float targetX,
      float targetY) {
    return (float) Math.sqrt(getSquareDistanceToLine(startX, startY, endX, endY, targetX, targetY));
  }

  /**
   * Возвращает расстояние от точки до отрезка.
   *
   * @param startX начальная координата отрезка.
   * @param startY начальная координата отрезка.
   * @param startZ начальная координата отрезка.
   * @param endX конечная координата отрезка.
   * @param endY конечная координата отрезка.
   * @param endZ конечная координата отрезка.
   * @param targetX координата точки.
   * @param targetY координата точки.
   * @param targetZ координата точки.
   * @return расстояние от точки до отрезка.
   */
  public static float getDistanceToLine(
      float startX,
      float startY,
      float startZ,
      float endX,
      float endY,
      float endZ,
      float targetX,
      float targetY,
      float targetZ) {
    return (float) Math.sqrt(getSquareDistanceToLine(
        startX,
        startY,
        startZ,
        endX,
        endY,
        endZ,
        targetX,
        targetY,
        targetZ));
  }

  /**
   * Get squared distance distance between two points.
   *
   * @param startX the start X coordinate.
   * @param startY the start Y coordinate.
   * @param startZ the start Z coordinate.
   * @param targetX the end X coordinate.
   * @param targetY the end Y coordinate.
   * @param targetZ the end Z coordinate.
   * @return the squared distance.
   */
  public static float getSquareDistance(
      float startX,
      float startY,
      float startZ,
      float targetX,
      float targetY,
      float targetZ) {

    float dx = targetX - startX;
    float dy = targetY - startY;
    float dz = targetZ - startZ;

    return dx * dx + dy * dy + dz * dz;
  }

  /**
   * Возвращает квадрат расстояния от точки до отрезка.
   *
   * @param startX начальная координата отрезка.
   * @param startY начальная координата отрезка.
   * @param endX конечная координата отрезка.
   * @param endY конечная координата отрезка.
   * @param targetX координата точки.
   * @param targetY координата точки.
   * @return квадрат расстояния от точки до отрезка.
   */
  public static float getSquareDistanceToLine(
      final float startX,
      final float startY,
      float endX,
      float endY,
      float targetX,
      float targetY) {

    endX -= startX;
    endY -= startY;

    targetX -= startX;
    targetY -= startY;

    float dotprod = targetX * endX + targetY * endY;

    float projlenSq;

    if (dotprod <= 0.0F) {
      projlenSq = 0.0F;
    } else {

      targetX = endX - targetX;
      targetY = endY - targetY;

      dotprod = targetX * endX + targetY * endY;

      if (dotprod <= 0.0F) {
        projlenSq = 0.0F;
      } else {
        projlenSq = dotprod * dotprod / (endX * endX + endY * endY);
      }
    }

    float lenSq = targetX * targetX + targetY * targetY - projlenSq;

    if (lenSq < 0F) {
      lenSq = 0F;
    }

    return lenSq;
  }

  /**
   * Возвращает квадрат расстояния от точки до отрезка.
   *
   * @param startX начальная координата отрезка.
   * @param startY начальная координата отрезка.
   * @param startZ начальная координата отрезка.
   * @param endX конечная координата отрезка.
   * @param endY конечная координата отрезка.
   * @param endZ конечная координата отрезка.
   * @param targetX координата точки.
   * @param targetY координата точки.
   * @param targetZ координата точки.
   * @return квадрат расстояния от точки до отрезка.
   */
  public static float getSquareDistanceToLine(
      float startX,
      float startY,
      float startZ,
      float endX,
      float endY,
      float endZ,
      float targetX,
      float targetY,
      float targetZ) {

    float lineX = endX - startX;
    float lineY = endY - startY;
    float lineZ = endZ - startZ;

    float pointX = targetX - startX;
    float pointY = targetY - startY;
    float pointZ = targetZ - startZ;

    float c1 = scalar(pointX, pointY, pointZ, lineX, lineY, lineZ);

    if (c1 < 0F) {
      return squareLength(targetX, targetY, targetZ, startX, startY, startZ);
    }

    float c2 = scalar(lineX, lineY, lineZ, lineX, lineY, lineZ);

    if (c2 <= c1) {
      return squareLength(targetX, targetY, targetZ, endX, endY, endZ);
    }

    float b = c1 / c2;

    pointX = startX + lineX * b;
    pointY = startY + lineY * b;
    pointZ = startZ + lineZ * b;

    return squareLength(targetX, targetY, targetZ, pointX, pointY, pointZ);
  }

  /**
   * Производит скалярное произведение двух точек.
   *
   * @param x1 координата первой точки.
   * @param y1 координата первой точки.
   * @param z1 координата первой точки.
   * @param x2 координата второй точки.
   * @param y2 координата второй точки.
   * @param z2 координата второй точки.
   * @return произведение двух точек.
   */
  public static float scalar(float x1, float y1, float z1, float x2, float y2, float z2) {
    return x1 * x2 + y1 * y2 + z1 * z2;
  }

  /**
   * Находит квадрат длинны между двумя точками.
   *
   * @param x1 координата первой точки.
   * @param y1 координата первой точки.
   * @param z1 координата первой точки.
   * @param x2 координата второй точки.
   * @param y2 координата второй точки.
   * @param z2 координата второй точки.
   * @return квадрат длинны между точками.
   */
  public static float squareLength(float x1, float y1, float z1, float x2, float y2, float z2) {
    float dx = x1 - x2;
    float dy = y1 - y2;
    float dz = z1 - z2;
    return dx * dx + dy * dy + dz * dz;
  }
}
