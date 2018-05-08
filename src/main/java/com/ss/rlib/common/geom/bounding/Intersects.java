package com.ss.rlib.common.geom.bounding;

import static java.lang.Math.abs;

/**
 * The type Intersects.
 *
 * @author JavaSaBr
 */
public class Intersects {

    /**
     * Intersects boolean.
     *
     * @param start the start
     * @param dir   the dir
     * @param min   the min
     * @param max   the max
     * @param enter the enter
     * @param exit  the exit
     * @return the boolean
     */
    public static boolean intersects(final float start, final float dir, final float min,
                                     final float max, final float enter, final float exit) {

        if (abs(dir) < 1.0E-8) {
            return start >= min && start <= max;
        }

        final float ooDir = 1.0f / dir;
        float t0 = (min - start) * ooDir;
        float t1 = (max - start) * ooDir;

        if (t0 > t1) {
            final float temp = t1;
            t1 = t0;
            t0 = temp;
        }

        if (t0 > exit || t1 < enter) {
            return false;
        }

        return true;
    }

    private Intersects() {
        throw new RuntimeException();
    }
}
