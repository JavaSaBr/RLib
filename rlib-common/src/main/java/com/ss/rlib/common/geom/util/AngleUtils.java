package com.ss.rlib.common.geom.util;

/**
 * Реализация утильного класса с методами по работе с углами.
 *
 * @author JavaSaBr
 */
public final class AngleUtils {

    /**
     * The constant HEADINGS_IN_PI.
     */
    public static final float HEADINGS_IN_PI = 10430.378350470452724949566316381F;
    /**
     * The constant PI.
     */
    public static final float PI = 3.14159265358979323846F;

    /**
     * Расчет разворота в указанные координаты.
     *
     * @param x       начальная координата.
     * @param y       начальная координата.
     * @param targetX целевая координата.
     * @param targetY целевая координата.
     * @return нужный разворот.
     */
    public static int calcHeading(final float x, final float y, final float targetX, final float targetY) {
        return (int) (Math.atan2(y - targetY, x - targetX) * HEADINGS_IN_PI) + 32768;
    }

    /**
     * Расчет относительного положения.
     *
     * @param x       начальная координата.
     * @param y       начальная координата.
     * @param heading разворот.
     * @param targetX целевая координата.
     * @param targetY целевая координата.
     * @return нужный разворот.
     */
    public static int calcHeadingTo(final float x, final float y, final int heading,
                                    final float targetX, final float targetY) {

        int newHeading = calcHeading(x, y, targetX, targetY);

        newHeading = heading - newHeading;

        if (newHeading < 0) {
            newHeading = newHeading + 1 + Integer.MAX_VALUE & 0xFFFF;
        } else if (newHeading > 0xFFFF) {
            newHeading &= 0xFFFF;
        }

        return newHeading;
    }

    /**
     * Конвектирует градус в heading.
     *
     * @param degree кол-во градусов.
     * @return heading направление разворота.
     */
    public static int degreeToHeading(float degree) {
        if (degree < 0) degree += 360f;
        return (int) (degree * 182.044444444f);
    }

    /**
     * Конвектирование градусы в радианы.
     *
     * @param angle кол-во градусов.
     * @return кол -во радианов.
     */
    public static float degreeToRadians(final float angle) {
        return angle * PI / 180F;
    }

    /**
     * Получаем относительный градус между 2 точками.
     *
     * @param startX х координата первой точки.
     * @param startY у координата второй точки.
     * @param endX   х координата второй точки.
     * @param endY   у координата второй точки.
     * @return кол -во градусов.
     */
    public static float getAngleFrom(final float startX, final float startY, final float endX, final float endY) {
        float angle = (float) Math.toDegrees(Math.atan2(startY - endY, startX - endX));
        if (angle <= 0F) angle += 360F;
        return angle;
    }

    /**
     * Конвектирование heading в градусы.
     *
     * @param heading направление разворота.
     * @return кол -во градусов.
     */
    public static float headingToDegree(final int heading) {
        float angle = heading / 182.044444444f;
        if (angle == 0) angle = 360f;
        return angle;
    }

    /**
     * Конвектирование heading в радианы.
     *
     * @param heading направление разворота.
     * @return кол -во радианов.
     */
    public static float headingToRadians(final int heading) {
        float angle = heading / 182.044444444f;
        if (angle == 0) angle = 360f;
        return angle * 3.141592653f / 180f;
    }

    /**
     * Рассчет вхождения в относительную область перед точкой точки.
     *
     * @param x       координата первой точки.
     * @param y       координата первой точки.
     * @param heading направление области.
     * @param targetX координата второй точки.
     * @param targetY координата второй точки.
     * @param width   ширина области.
     * @return входит ли.
     */
    public static boolean isInDegree(final float x, final float y, final int heading,
                                     final float targetX, final float targetY, final int width) {

        int angle = (int) AngleUtils.headingToDegree(calcHeadingTo(x, y, heading, targetX, targetY));
        final int degree = (int) headingToDegree(heading);

        int min = degree - width;
        int max = degree + width;

        if (min < 0) min += 360;
        if (max < 0) max += 360;

        final boolean flag = angle - degree > 180;
        if (flag) angle -= 360;
        if (angle > max) return false;

        angle += 360;

        return angle > min;
    }

    /**
     * Конвектироввание радианов в градусы.
     *
     * @param radians угол в радианах.
     * @return угол в градусах.
     */
    public static float radiansToDegree(final float radians) {
        return radians * 180F / PI;
    }
}
