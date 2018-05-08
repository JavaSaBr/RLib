package com.ss.rlib.common.geom;

import com.ss.rlib.common.util.pools.Reusable;
import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of float matrix 3x3.
 *
 * @author JavaSaBr
 */
public final class Matrix3f implements Reusable {

    /**
     * The constant ZERO.
     */
    public static final Matrix3f ZERO = new Matrix3f(0, 0, 0, 0, 0, 0, 0, 0, 0);
    /**
     * The constant IDENTITY.
     */
    public static final Matrix3f IDENTITY = new Matrix3f();

    /**
     * New instance matrix 3 f.
     *
     * @return the matrix 3 f
     */
    @NotNull
    public static Matrix3f newInstance() {
        return new Matrix3f();
    }

    /**
     * New instance matrix 3 f.
     *
     * @param val_0_0 the val 0 0
     * @param val_0_1 the val 0 1
     * @param val_0_2 the val 0 2
     * @param val_1_0 the val 1 0
     * @param val_1_1 the val 1 1
     * @param val_1_2 the val 1 2
     * @param val_2_0 the val 2 0
     * @param val_2_1 the val 2 1
     * @param val_2_2 the val 2 2
     * @return the matrix 3 f
     */
    @NotNull
    public static Matrix3f newInstance(final float val_0_0, final float val_0_1, final float val_0_2,
                                       final float val_1_0, final float val_1_1, final float val_1_2,
                                       final float val_2_0, final float val_2_1, final float val_2_2) {
        return new Matrix3f(val_0_0, val_0_1, val_0_2, val_1_0, val_1_1, val_1_2, val_2_0, val_2_1, val_2_2);
    }

    /**
     * Values.
     */
    protected float val_0_0, /**
     * The Val 0 1.
     */
    val_0_1, /**
     * The Val 0 2.
     */
    val_0_2;
    /**
     * The Val 1 0.
     */
    protected float val_1_0, /**
     * The Val 1 1.
     */
    val_1_1, /**
     * The Val 1 2.
     */
    val_1_2;
    /**
     * The Val 2 0.
     */
    protected float val_2_0, /**
     * The Val 2 1.
     */
    val_2_1, /**
     * The Val 2 2.
     */
    val_2_2;

    /**
     * Instantiates a new Matrix 3 f.
     */
    public Matrix3f() {
        val_0_1 = val_0_2 = val_1_0 = val_1_2 = val_2_0 = val_2_1 = 0;
        val_0_0 = val_1_1 = val_2_2 = 1;
    }

    private Matrix3f(final float val_0_0, final float val_0_1, final float val_0_2,
                     final float val_1_0, final float val_1_1, final float val_1_2,
                     final float val_2_0, final float val_2_1, final float val_2_2) {
        this.val_0_0 = val_0_0;
        this.val_0_1 = val_0_1;
        this.val_0_2 = val_0_2;
        this.val_1_0 = val_1_0;
        this.val_1_1 = val_1_1;
        this.val_1_2 = val_1_2;
        this.val_2_0 = val_2_0;
        this.val_2_1 = val_2_1;
        this.val_2_2 = val_2_2;
    }

    /**
     * Change all values to absolute.
     */
    public void absoluteLocal() {
        val_0_0 = Math.abs(val_0_0);
        val_0_1 = Math.abs(val_0_1);
        val_0_2 = Math.abs(val_0_2);
        val_1_0 = Math.abs(val_1_0);
        val_1_1 = Math.abs(val_1_1);
        val_1_2 = Math.abs(val_1_2);
        val_2_0 = Math.abs(val_2_0);
        val_2_1 = Math.abs(val_2_1);
        val_2_2 = Math.abs(val_2_2);
    }

    /**
     * Multiply a vector by this matrix.
     *
     * @param vector the source vector.
     * @param result the result vector.
     * @return the result.
     */
    @NotNull
    public Vector3f mult(@NotNull final Vector3f vector, @NotNull final Vector3f result) {

        final float x = vector.x;
        final float y = vector.y;
        final float z = vector.z;

        result.x = val_0_0 * x + val_0_1 * y + val_0_2 * z;
        result.y = val_1_0 * x + val_1_1 * y + val_1_2 * z;
        result.z = val_2_0 * x + val_2_1 * y + val_2_2 * z;

        return result;
    }

    /**
     * Set.
     *
     * @param val_0_0 the val 0 0
     * @param val_0_1 the val 0 1
     * @param val_0_2 the val 0 2
     * @param val_1_0 the val 1 0
     * @param val_1_1 the val 1 1
     * @param val_1_2 the val 1 2
     * @param val_2_0 the val 2 0
     * @param val_2_1 the val 2 1
     * @param val_2_2 the val 2 2
     */
    public void set(final float val_0_0, final float val_0_1, final float val_0_2,
                    final float val_1_0, final float val_1_1, final float val_1_2,
                    final float val_2_0, final float val_2_1, final float val_2_2) {
        this.val_0_0 = val_0_0;
        this.val_0_1 = val_0_1;
        this.val_0_2 = val_0_2;
        this.val_1_0 = val_1_0;
        this.val_1_1 = val_1_1;
        this.val_1_2 = val_1_2;
        this.val_2_0 = val_2_0;
        this.val_2_1 = val_2_1;
        this.val_2_2 = val_2_2;
    }

    /**
     * Set values from a rotation.
     *
     * @param rotation the rotation.
     * @return this updated matrix.
     */
    @NotNull
    public Matrix3f set(@NotNull final Quaternion4f rotation) {
        return rotation.toRotationMatrix(this);
    }

    @Override
    public String toString() {
        return val_0_0 + ", " + val_0_1 + ", " + val_0_2 + "\n" +
                val_1_0 + ", " + val_1_1 + ", " + val_1_2 + "\n" +
                val_2_0 + ", " + val_2_1 + ", " + val_2_2 + "\n";
    }
}
