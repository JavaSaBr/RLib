package com.ss.rlib.geom;

import com.ss.rlib.util.pools.Reusable;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of a ray.
 *
 * @author JavaSaBr
 */
public class Ray3f implements Reusable {

    /**
     * The start point of this ray.
     */
    @Nullable
    protected Vector3f start;

    /**
     * The direction of this ray.
     */
    @Nullable
    protected Vector3f direction;

    /**
     * Get the direction.
     *
     * @return the direction.
     */
    public final @Nullable Vector3f getDirection() {
        return direction;
    }

    /**
     * Set the direction.
     *
     * @param direction the direction.
     */
    public final void setDirection(@Nullable final Vector3f direction) {
        this.direction = direction;
    }

    /**
     * Get the start point.
     *
     * @return the start point.
     */
    public final @Nullable Vector3f getStart() {
        return start;
    }

    /**
     * Set the start point.
     *
     * @param start the start point.
     */
    public final void setStart(@Nullable final Vector3f start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "Ray3f{" + "start=" + start + ", direction=" + direction + '}';
    }
}
