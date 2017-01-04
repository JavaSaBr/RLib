package rlib.geom.bounding;

import static java.lang.Math.abs;

/**
 * @author JavaSaBr
 */
public class Intersects {

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
