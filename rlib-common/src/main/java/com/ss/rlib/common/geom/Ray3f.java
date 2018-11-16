package com.ss.rlib.common.geom;

import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of a ray.
 *
 * @author JavaSaBr
 */
public class Ray3f implements Reusable {

    /**
     * The start point of this ray.
     */
    @NotNull
    private final Vector3f start;

    /**
     * The direction of this ray.
     */
    @NotNull
    private final Vector3f direction;
    
    /**
     * Construct new ray in start point and specified direction.
     * 
     * @param origin start point
     * @param direction direction
     */
    public Ray3f(@NotNull Vector3f origin, @NotNull Vector3f direction) {
        this.start = origin;
        this.direction = direction;
    }
    
    /**
     * Construct empty ray in zero point and zero direction.
     */
    public Ray3f() {
        this(new Vector3f(), new Vector3f());
    }

    /**
     * Get the direction.
     *
     * @return the direction.
     */
    public final @NotNull Vector3f getDirection() {
        return direction;
    }

    /**
     * Set the direction.
     *
     * @param direction the direction.
     */
    public final void setDirection(@NotNull Vector3f direction) {
        this.direction.set(direction);
    }

    /**
     * Get the start point.
     *
     * @return the start point.
     */
    public final @NotNull Vector3f getStart() {
        return start;
    }

    /**
     * Set the start point.
     *
     * @param start the start point.
     */
    public final void setStart(@NotNull Vector3f start) {
        this.start.set(start);
    }

    @Override
    public String toString() {
        return "Ray3f{" + "start=" + start + ", direction=" + direction + '}';
    }
}
