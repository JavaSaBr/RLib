package rlib.geom;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import rlib.util.pools.Reusable;

/**
 * The implementation of a ray.
 *
 * @author JavaSaBr
 */
public class Ray3f implements Reusable {

    /**
     * The start point.
     */
    protected Vector3f start;

    /**
     * The direction of a ray.
     */
    protected Vector3f direction;

    /**
     * @return the direction of a ray.
     */
    @NotNull
    public final Vector3f getDirection() {
        return Objects.requireNonNull(direction);
    }

    /**
     * @param direction the direction of a ray.
     */
    public final void setDirection(@NotNull final Vector3f direction) {
        this.direction = direction;
    }

    /**
     * @return the start point.
     */
    @NotNull
    public final Vector3f getStart() {
        return Objects.requireNonNull(start);
    }

    /**
     * @param start the start point.
     */
    public final void setStart(@NotNull final Vector3f start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "Ray3f{" +
                "start=" + start +
                ", direction=" + direction +
                '}';
    }
}
