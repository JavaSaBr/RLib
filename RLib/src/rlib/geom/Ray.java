package rlib.geom;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import rlib.util.pools.Reusable;

/**
 * The implementation of a ray.
 *
 * @author JavaSaBr
 */
public class Ray implements Reusable {

    /**
     * The start point.
     */
    protected Vector start;

    /**
     * The direction of a ray.
     */
    protected Vector direction;

    /**
     * @return the direction of a ray.
     */
    @NotNull
    public final Vector getDirection() {
        return Objects.requireNonNull(direction);
    }

    /**
     * @param direction the direction of a ray.
     */
    public final void setDirection(@NotNull final Vector direction) {
        this.direction = direction;
    }

    /**
     * @return the start point.
     */
    @NotNull
    public final Vector getStart() {
        return Objects.requireNonNull(start);
    }

    /**
     * @param start the start point.
     */
    public final void setStart(@NotNull final Vector start) {
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
