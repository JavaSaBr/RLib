package com.ss.rlib.common.geom.util;

import com.ss.rlib.common.geom.Vector3f;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.common.util.ExtMath;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Реализация утильного класса с методами для рассчета координат.
 *
 * @author JavaSaBr
 */
public final class CoordsUtils {

    private static final Logger LOGGER = LoggerManager.getLogger(CoordsUtils.class);

    /**
     * Генерация дуговых позиций.
     *
     * @param x       целевая координата.
     * @param y       целевая координата.
     * @param z       целевая координата.
     * @param heading разворот.
     * @param radius  радиус формирования.
     * @param count   кол-во необходимых позиций.
     * @param degree  положение на окружности центра дуги.
     * @param width   ширина дуги.
     * @return массив позиций.
     */
    @NotNull
    public static Vector3f[] arcCoords(final float x, final float y, final float z, final int heading,
                                       final int radius, final int count, final int degree, final int width) {

        final Vector3f[] vectors = new Vector3f[count];

        final float current = AngleUtils.headingToDegree(heading) - degree;

        final float min = current - width;
        final float max = current + width;

        final float angle = Math.abs(min - max) / count;

        for (int i = 0; i < count; i++) {

            final Vector3f vector = new Vector3f();

            final float radians = AngleUtils.degreeToRadians(min + angle * i);

            final float newX = calcX(x, radius, radians);
            final float newY = calcY(y, radius, radians);

            vector.set(newX, newY, z);

            vectors[i] = vector;
        }

        return vectors;
    }

    /**
     * Рассчет х координаты с учетом дистанции и разворота.
     *
     * @param x        стартовая х координата.
     * @param distance дистанция сдвига.
     * @param radians  направление сдвига.
     * @return новая х координата.
     */
    public static float calcX(final float x, final int distance, final float radians) {
        return x + distance * (float) Math.cos(radians);
    }

    /**
     * Рассчет х координаты с учетом дистанции и разворота.
     *
     * @param x        стартовая х координата.
     * @param distance дистанция сдвига.
     * @param heading  направление сдвига.
     * @return новая х координата.
     */
    public static float calcX(final float x, final int distance, final int heading) {
        return x + distance * (float) Math.cos(AngleUtils.headingToRadians(heading));
    }

    /**
     * Рассчет х координаты с учетом дистанции и разворота.
     *
     * @param x        стартовая х координата.
     * @param distance дистанция сдвига.
     * @param heading  направление сдвига.
     * @param offset   смещение по градусам.
     * @return новая х координата.
     */
    public static float calcX(final float x, final int distance, final int heading, final int offset) {
        return x + distance * (float) Math.cos(AngleUtils.headingToRadians(heading + offset));
    }

    /**
     * Рассчет у координаты с учетом дистанции и разворота.
     *
     * @param y        стартовая у координата.
     * @param distance дистанция сдвига.
     * @param radians  направление сдвига.
     * @return новая у координата.
     */
    public static float calcY(final float y, final int distance, final float radians) {
        return y + distance * (float) Math.sin(radians);
    }

    /**
     * Рассчет у координаты с учетом дистанции и разворота.
     *
     * @param y        стартовая у координата.
     * @param distance дистанция сдвига.
     * @param heading  направление сдвига.
     * @return новая у координата.
     */
    public static float calcY(final float y, final int distance, final int heading) {
        return y + distance * (float) Math.sin(AngleUtils.headingToRadians(heading));
    }

    /**
     * Рассчет у координаты с учетом дистанции и разворота.
     *
     * @param y        стартовая у координата.
     * @param distance дистанция сдвига.
     * @param heading  направление сдвига.
     * @param offset   смещение по градусам.
     * @return новая у координата.
     */
    public static float calcY(final float y, final int distance, final int heading, final int offset) {
        return y + distance * (float) Math.sin(AngleUtils.headingToRadians(heading + offset));
    }

    /**
     * Генерация массива круговых позиций с одинаковым интервлаом.
     *
     * @param x      центральная координата.
     * @param y      центральная координата.
     * @param z      центральная координата.
     * @param radius радиус разброса.
     * @param count  кол-во позиций.
     * @return массив позиций.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static Vector3f[] circularCoords(final float x, final float y, final float z, final int radius, final int count) {

        final Vector3f[] locs = new Vector3f[count];

        final float angle = 360F / count;

        for (int i = 1; i <= count; i++) {

            final Vector3f loc = new Vector3f();

            final float radians = AngleUtils.degreeToRadians(i * angle);

            final float newX = calcX(x, radius, radians);
            final float newY = calcY(y, radius, radians);

            loc.set(newX, newY, z);

            locs[i - 1] = loc;
        }

        return locs;
    }

    /**
     * Получение точек по кругу от указанной точки.
     *
     * @param source массив исходных точек.
     * @param x      координата центра.
     * @param y      координата центра.
     * @param z      координата центра.
     * @param count  кол-во точек.
     * @param radius радиус от центра.
     * @return массив точек.
     */
    @NotNull
    public static Vector3f[] getCircularPoints(@NotNull final Vector3f[] source, final float x, final float y,
                                               final float z, final int count, final int radius) {

        if (count < 1) {
            return source;
        }

        final float angle = 360F / count;

        for (int i = 1; i <= count; i++) {

            final float radians = AngleUtils.degreeToRadians(angle * i);

            final float newX = x + radius * (float) Math.cos(radians);
            final float newY = y + radius * (float) Math.sin(radians);

            final Vector3f point = source[i - 1];

            point.set(newX, newY, z);
        }

        return source;
    }

    /**
     * Рассчет случайной точки.
     *
     * @param loc       the loc
     * @param x         центральная координата.
     * @param y         центральная координата.
     * @param z         центральная координата.
     * @param radiusMin минимальный радиус рандома.
     * @param radiusMax максимальный радиус рандома.
     * @return новая точка.
     */
    @NotNull
    public static Vector3f randomCoords(@NotNull final Vector3f loc, final float x, final float y, final float z,
                                        final int radiusMin, final int radiusMax) {

        final ThreadLocalRandom current = ThreadLocalRandom.current();

        if (radiusMax == 0 || radiusMax < radiusMin) {
            loc.set(x, y, z);
            return loc;
        }

        final int radius = current.nextInt(radiusMin, radiusMax);
        final float radians = AngleUtils.degreeToRadians(current.nextInt(0, 360));

        final float newX = calcX(x, radius, radians);
        final float newY = calcY(y, radius, radians);

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
     * Calculates the squared value of the
     * magnitude of the vector.
     *
     * @param x the X component.
     * @param y the Y component.
     * @return the magnitude squared of the vector.
     */
    public static float lengthSquared(float x, float y) {
        return x * x + y * y;
    }
}
