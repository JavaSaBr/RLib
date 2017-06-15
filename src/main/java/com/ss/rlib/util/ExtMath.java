package com.ss.rlib.util;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
public final class ExtMath {

    /**
     * The value PI as a float. (180 degrees).
     */
    public static final float PI = (float) Math.PI;

    /**
     * The value PI/2 as a float. (90 degrees)
     */
    public static final float HALF_PI = 0.5f * PI;

    /**
     * Returns the arc cosine of a value.<br> Special cases: <ul><li>If value is smaller than -1, then the result is PI.
     * <li>If the argument is greater than 1, then the result is 0.</ul>
     *
     * @param value The value to arc cosine.
     * @return The angle, in radians.
     * @see java.lang.Math#acos(double) java.lang.Math#acos(double)
     */
    public static float acos(final float value) {

        if (-1.0F < value) {
            if (value < 1.0f) {
                return (float) Math.acos(value);
            }
            return 0.0F;
        }

        return PI;
    }

    /**
     * Returns the arc sine of a value.<br> Special cases: <ul> <li>If value is smaller than -1, then the result is
     * -HALF_PI. <li>If the argument is greater than 1, then the result is HALF_PI. </ul>
     *
     * @param value The value to arc sine.
     * @return the angle in radians.
     * @see java.lang.Math#asin(double) java.lang.Math#asin(double)
     */
    public static float asin(final float value) {

        if (-1.0F < value) {
            if (value < 1.0F) {
                return (float) Math.asin(value);
            }

            return HALF_PI;
        }

        return -HALF_PI;
    }

    /**
     * A direct call to Math.atan2.
     *
     * @param y the y
     * @param x the x
     * @return Math.atan2(y, x) float
     * @see java.lang.Math#atan2(double, double) java.lang.Math#atan2(double, double)
     */
    public static float atan2(final float y, final float x) {
        return (float) Math.atan2(y, x);
    }

    /**
     * Returns cosine of an angle. Direct call to java.lang.Math
     *
     * @param value The angle to cosine.
     * @return the cosine of the angle.
     * @see Math#cos(double) Math#cos(double)
     */
    public static float cos(final float value) {
        return (float) Math.cos(value);
    }

    /**
     * Returns 1/sqrt(value)
     *
     * @param value The value to process.
     * @return 1 /sqrt(value)
     * @see java.lang.Math#sqrt(double) java.lang.Math#sqrt(double)
     */
    public static float invSqrt(final float value) {
        return (float) (1.0f / Math.sqrt(value));
    }

    /**
     * Returns the sine of an angle. Direct call to java.lang.Math
     *
     * @param value The angle to sine.
     * @return the sine of the angle.
     * @see Math#sin(double) Math#sin(double)
     */
    public static float sin(final float value) {
        return (float) Math.sin(value);
    }

    /**
     * Returns the square root of a given value.
     *
     * @param value The value to sqrt.
     * @return The square root of the given value.
     * @see java.lang.Math#sqrt(double) java.lang.Math#sqrt(double)
     */
    public static float sqrt(final float value) {
        return (float) Math.sqrt(value);
    }

    private ExtMath() {
        throw new RuntimeException();
    }
}
