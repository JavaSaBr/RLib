package com.ss.rlib.common.geom;

import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of float matrix 3x3.
 *
 * @author JavaSaBr
 */
public final class Matrix3f implements Reusable {

    public static final Matrix3f ZERO = new Matrix3f(
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
    );

    public static final Matrix3f IDENTITY = new Matrix3f();

    /**
     * Values.
     */
    protected float val_0_0, val_0_1, val_0_2;
    protected float val_1_0, val_1_1, val_1_2;
    protected float val_2_0, val_2_1, val_2_2;

    public Matrix3f() {
        val_0_1 = val_0_2 = val_1_0 = val_1_2 = val_2_0 = val_2_1 = 0;
        val_0_0 = val_1_1 = val_2_2 = 1;
    }

    public Matrix3f(@NotNull Matrix3f matrix) {
        this(matrix.val_0_0, matrix.val_0_1, matrix.val_0_2,
             matrix.val_1_0, matrix.val_1_1, matrix.val_1_2,
             matrix.val_2_0, matrix.val_2_1, matrix.val_2_2);
    }

    public Matrix3f(
            float val_0_0,
            float val_0_1,
            float val_0_2,
            float val_1_0,
            float val_1_1,
            float val_1_2,
            float val_2_0,
            float val_2_1,
            float val_2_2
    ) {
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
     * Multiply the vector by this matrix.
     *
     * @param vector the source vector.
     * @param result the result vector.
     * @return the result.
     */
    public @NotNull Vector3f mult(@NotNull Vector3f vector, @NotNull Vector3f result) {

        float x = vector.x;
        float y = vector.y;
        float z = vector.z;

        result.x = val_0_0 * x + val_0_1 * y + val_0_2 * z;
        result.y = val_1_0 * x + val_1_1 * y + val_1_2 * z;
        result.z = val_2_0 * x + val_2_1 * y + val_2_2 * z;

        return result;
    }

    /**
     * Set the matrix values.
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
    public void set(
            float val_0_0, float val_0_1, float val_0_2,
            float val_1_0, float val_1_1, float val_1_2,
            float val_2_0, float val_2_1, float val_2_2
    ) {
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
     * Set values from the rotation.
     *
     * @param rotation the rotation.
     * @return this updated matrix.
     */
    public @NotNull Matrix3f set(@NotNull Quaternion4f rotation) {
        return rotation.toRotationMatrix(this);
    }

    @Override
    public String toString() {
        return val_0_0 + ", " + val_0_1 + ", " + val_0_2 + "\n" +
                val_1_0 + ", " + val_1_1 + ", " + val_1_2 + "\n" +
                val_2_0 + ", " + val_2_1 + ", " + val_2_2 + "\n";
    }
}
