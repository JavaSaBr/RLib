package javasabr.rlib.geometry.util;

import javasabr.rlib.geometry.Vector3f;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
public final class GeometryUtils {

  public static float getDistance(
      float startX,
      float startY,
      float startZ,
      float targetX,
      float targetY,
      float targetZ) {
    return (float) Math.sqrt(getSquareDistance(startX, startY, startZ, targetX, targetY, targetZ));
  }

  public static float getDistanceToLine(
      float startX,
      float startY,
      float endX,
      float endY,
      float targetX,
      float targetY) {
    return (float) Math.sqrt(getSquareDistanceToLine(startX, startY, endX, endY, targetX, targetY));
  }

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

  public static float scalar(float x1, float y1, float z1, float x2, float y2, float z2) {
    return x1 * x2 + y1 * y2 + z1 * z2;
  }

  public static float squareLength(float x1, float y1, float z1, float x2, float y2, float z2) {
    float dx = x1 - x2;
    float dy = y1 - y2;
    float dz = z1 - z2;
    return dx * dx + dy * dy + dz * dz;
  }

  /**
   * Checks if two vectors are equal within the specified epsilon.
   *
   * @param first the first vector
   * @param second the second vector
   * @param epsilon the tolerance for comparison
   * @return true if the vectors are equal within epsilon
   * @since 10.0.0
   */
  public boolean isEquals(@Nullable Vector3f first, @Nullable Vector3f second, float epsilon) {
    if (first == null || second == null) {
      return false;
    } else {
      return first.equals(second, epsilon);
    }
  }
}
