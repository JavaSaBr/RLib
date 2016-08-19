package rlib.geom;

import rlib.util.pools.Reusable;

/**
 * Реализация модели луча.
 *
 * @author JavaSaBr
 */
public class Ray implements Reusable {

    /**
     * Стартовая точка луча.
     */
    protected Vector start;

    /**
     * Вектор направления луча.
     */
    protected Vector direction;

    /**
     * @return точка направления луча.
     */
    public final Vector getDirection() {
        return direction;
    }

    /**
     * @param direction точка направления луча.
     */
    public final void setDirection(final Vector direction) {
        this.direction = direction;
    }

    /**
     * @return start точка старта луча.
     */
    public final Vector getStart() {
        return start;
    }

    /**
     * @param start точка старта луча.
     */
    public final void setStart(final Vector start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "start=" + start +
                ", direction=" + direction +
                '}';
    }
}
