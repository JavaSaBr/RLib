package javasabr.rlib.geometry.util;

import org.jspecify.annotations.NullMarked;

/**
 * Utility methods for angle conversions and calculations.
 *
 * @since 10.0.0
 */
@NullMarked
public final class AngleUtils {

  /**
   * Number of headings in PI radians.
   */
  public static final float HEADINGS_IN_PI = 10430.378350470452724949566316381F;

  /**
   * The value of PI.
   */
  public static final float PI = 3.14159265358979323846F;

  public static int calcHeading(float x, float y, float targetX, float targetY) {
    return (int) (Math.atan2(y - targetY, x - targetX) * HEADINGS_IN_PI) + 32768;
  }

  public static int calcHeadingTo(float x, float y, int heading, float targetX, float targetY) {

    int newHeading = calcHeading(x, y, targetX, targetY);
    newHeading = heading - newHeading;

    if (newHeading < 0) {
      newHeading = newHeading + 1 + Integer.MAX_VALUE & 0xFFFF;
    } else if (newHeading > 0xFFFF) {
      newHeading &= 0xFFFF;
    }

    return newHeading;
  }

  public static int degreeToHeading(float degree) {
    if (degree < 0) {
      degree += 360f;
    }
    return (int) (degree * 182.044444444f);
  }

  public static float degreeToRadians(float angle) {
    return angle * PI / 180F;
  }

  public static float getAngleFrom(float startX, float startY, float endX, float endY) {
    float angle = (float) Math.toDegrees(Math.atan2(startY - endY, startX - endX));
    if (angle <= 0F) {
      angle += 360F;
    }
    return angle;
  }

  public static float headingToDegree(int heading) {
    float angle = heading / 182.044444444f;
    if (angle == 0) {
      angle = 360f;
    }
    return angle;
  }

  public static float headingToRadians(int heading) {
    float angle = heading / 182.044444444f;
    if (angle == 0) {
      angle = 360f;
    }
    return angle * 3.141592653f / 180f;
  }

  public static boolean isInDegree(float x, float y, int heading, float targetX, float targetY, int width) {

    int angle = (int) AngleUtils.headingToDegree(calcHeadingTo(x, y, heading, targetX, targetY));
    final int degree = (int) headingToDegree(heading);

    int min = degree - width;
    int max = degree + width;

    if (min < 0) {
      min += 360;
    }
    if (max < 0) {
      max += 360;
    }

    final boolean flag = angle - degree > 180;
    if (flag) {
      angle -= 360;
    }
    if (angle > max) {
      return false;
    }

    angle += 360;

    return angle > min;
  }

  public static float radiansToDegree(float radians) {
    return radians * 180F / PI;
  }
}
