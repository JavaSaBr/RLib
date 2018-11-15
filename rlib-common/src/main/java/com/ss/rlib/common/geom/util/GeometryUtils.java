package com.ss.rlib.common.geom.util;

/**
 * Набор геометрически методов.
 *
 * @author JavaSaBr
 */
public final class GeometryUtils {

    /**
     * Рассчет расстояния между 2мя точками.
     *
     * @param startX  координата первой точки.
     * @param startY  координата первой точки.
     * @param startZ  координата первой точки.
     * @param targetX координата второй точки.
     * @param targetY координата второй точки.
     * @param targetZ координата второй точки.
     * @return расстояние между точками.
     */
    public static float getDistance(final float startX, final float startY, final float startZ,
                                    final float targetX, final float targetY, final float targetZ) {
        return (float) Math.sqrt(getSquareDistance(startX, startY, startZ, targetX, targetY, targetZ));
    }

    /**
     * Возвращает расстояние от точки до отрезка.
     *
     * @param startX  начальная координата отрезка.
     * @param startY  начальная координата отрезка.
     * @param endX    конечная координата отрезка.
     * @param endY    конечная координата отрезка.
     * @param targetX координата точки.
     * @param targetY координата точки.
     * @return расстояние от точки до отрезка.
     */
    public static float getDistanceToLine(final float startX, final float startY, final float endX,
                                          final float endY, final float targetX, final float targetY) {
        return (float) Math.sqrt(getSquareDistanceToLine(startX, startY, endX, endY, targetX, targetY));
    }

    /**
     * Возвращает расстояние от точки до отрезка.
     *
     * @param startX  начальная координата отрезка.
     * @param startY  начальная координата отрезка.
     * @param startZ  начальная координата отрезка.
     * @param endX    конечная координата отрезка.
     * @param endY    конечная координата отрезка.
     * @param endZ    конечная координата отрезка.
     * @param targetX координата точки.
     * @param targetY координата точки.
     * @param targetZ координата точки.
     * @return расстояние от точки до отрезка.
     */
    public static float getDistanceToLine(final float startX, final float startY, final float startZ,
                                          final float endX, final float endY, final float endZ, final float targetX,
                                          final float targetY, final float targetZ) {
        return (float) Math.sqrt(getSquareDistanceToLine(startX, startY, startZ, endX, endY, endZ, targetX, targetY, targetZ));
    }

    /**
     * Get squared distance distance between two points.
     *
     * @param startX  the start X coordinate.
     * @param startY  the start Y coordinate.
     * @param startZ  the start Z coordinate.
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
            float targetZ
    ) {

        float dx = targetX - startX;
        float dy = targetY - startY;
        float dz = targetZ - startZ;

        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Возвращает квадрат расстояния от точки до отрезка.
     *
     * @param startX  начальная координата отрезка.
     * @param startY  начальная координата отрезка.
     * @param endX    конечная координата отрезка.
     * @param endY    конечная координата отрезка.
     * @param targetX координата точки.
     * @param targetY координата точки.
     * @return квадрат расстояния от точки до отрезка.
     */
    public static float getSquareDistanceToLine(final float startX, final float startY, float endX,
                                                float endY, float targetX, float targetY) {

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
     * @param startX  начальная координата отрезка.
     * @param startY  начальная координата отрезка.
     * @param startZ  начальная координата отрезка.
     * @param endX    конечная координата отрезка.
     * @param endY    конечная координата отрезка.
     * @param endZ    конечная координата отрезка.
     * @param targetX координата точки.
     * @param targetY координата точки.
     * @param targetZ координата точки.
     * @return квадрат расстояния от точки до отрезка.
     */
    public static float getSquareDistanceToLine(final float startX, final float startY, final float startZ,
                                                final float endX, final float endY, final float endZ,
                                                final float targetX, final float targetY, final float targetZ) {

        final float lineX = endX - startX;
        final float lineY = endY - startY;
        final float lineZ = endZ - startZ;

        float pointX = targetX - startX;
        float pointY = targetY - startY;
        float pointZ = targetZ - startZ;

        final float c1 = scalar(pointX, pointY, pointZ, lineX, lineY, lineZ);

        if (c1 < 0F) {
            return squareLength(targetX, targetY, targetZ, startX, startY, startZ);
        }

        final float c2 = scalar(lineX, lineY, lineZ, lineX, lineY, lineZ);

        if (c2 <= c1) {
            return squareLength(targetX, targetY, targetZ, endX, endY, endZ);
        }

        final float b = c1 / c2;

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
    public static float scalar(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
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
    public static float squareLength(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {

        final float dx = x1 - x2;
        final float dy = y1 - y2;
        final float dz = z1 - z2;

        return dx * dx + dy * dy + dz * dz;
    }
}
