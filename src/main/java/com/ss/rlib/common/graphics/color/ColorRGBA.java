package com.ss.rlib.common.graphics.color;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * THe implementation of a color class.
 *
 * @author JavaSaBr
 */
public final class ColorRGBA implements Cloneable, Serializable {

    private static final long serialVersionUID = -3342702659372723983L;

    /**
     * The red channel.
     */
    public float r;

    /**
     * The green channel.
     */
    public float g;

    /**
     * The blue channel.
     */
    public float b;

    /**
     * The alpha channel.
     */
    public float a;

    /**
     * Instantiates a new Color rgba.
     */
    public ColorRGBA() {
        r = g = b = a = 1.0f;
    }

    /**
     * Instantiates a new Color rgba.
     *
     * @param rgba the rgba
     */
    public ColorRGBA(@NotNull final ColorRGBA rgba) {
        this.a = rgba.a;
        this.r = rgba.r;
        this.g = rgba.g;
        this.b = rgba.b;
    }

    /**
     * Instantiates a new Color rgba.
     *
     * @param r the r
     * @param g the g
     * @param b the b
     */
    public ColorRGBA(final float r, final float g, final float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1F;
    }

    /**
     * Instantiates a new Color rgba.
     *
     * @param r the r
     * @param g the g
     * @param b the b
     * @param a the a
     */
    public ColorRGBA(final float r, final float g, final float b, final float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Instantiates a new Color rgba.
     *
     * @param array the array
     */
    public ColorRGBA(final float[] array) {
        this.r = array[0];
        this.g = array.length > 1 ? array[1] : 1F;
        this.b = array.length > 2 ? array[2] : 1F;
        this.a = array.length > 3 ? array[3] : 1F;
    }

    /**
     * As int abgr int.
     *
     * @return the int
     */
    public int asIntABGR() {
        return ((int) (a * 255) & 0xFF) << 24 |
                ((int) (b * 255) & 0xFF) << 16 |
                ((int) (g * 255) & 0xFF) << 8 |
                (int) (r * 255) & 0xFF;
    }

    /**
     * As int argb int.
     *
     * @return the int
     */
    public int asIntARGB() {
        return ((int) (a * 255) & 0xFF) << 24 |
                ((int) (r * 255) & 0xFF) << 16 |
                ((int) (g * 255) & 0xFF) << 8 |
                (int) (b * 255) & 0xFF;
    }

    /**
     * As int rgba int.
     *
     * @return the int
     */
    public int asIntRGBA() {
        return ((int) (r * 255) & 0xFF) << 24 |
                ((int) (g * 255) & 0xFF) << 16 |
                ((int) (b * 255) & 0xFF) << 8 |
                (int) (a * 255) & 0xFF;
    }

    @Override
    public boolean equals(final Object o) {

        if (!(o instanceof ColorRGBA)) {
            return false;
        } else if (this == o) {
            return true;
        }

        final ColorRGBA comp = (ColorRGBA) o;

        if (Float.compare(r, comp.r) != 0) {
            return false;
        } else if (Float.compare(g, comp.g) != 0) {
            return false;
        } else if (Float.compare(b, comp.b) != 0) {
            return false;
        } else if (Float.compare(a, comp.a) != 0) {
            return false;
        }

        return true;
    }

    /**
     * From int argb.
     *
     * @param color the color
     */
    public void fromIntARGB(final int color) {
        a = ((byte) (color >> 24) & 0xFF) / 255f;
        r = ((byte) (color >> 16) & 0xFF) / 255f;
        g = ((byte) (color >> 8) & 0xFF) / 255f;
        b = ((byte) color & 0xFF) / 255f;
    }

    /**
     * From int rgba.
     *
     * @param color the color
     */
    public void fromIntRGBA(final int color) {
        r = ((byte) (color >> 24) & 0xFF) / 255f;
        g = ((byte) (color >> 16) & 0xFF) / 255f;
        b = ((byte) (color >> 8) & 0xFF) / 255f;
        a = ((byte) color & 0xFF) / 255f;
    }

    /**
     * Gets alpha.
     *
     * @return the alpha channel.
     */
    public float getAlpha() {
        return a;
    }

    /**
     * Gets blue.
     *
     * @return the blue channel.
     */
    public float getBlue() {
        return b;
    }

    /**
     * Gets green.
     *
     * @return the green channel.
     */
    public float getGreen() {
        return g;
    }

    /**
     * Gets red.
     *
     * @return the red channel.
     */
    public float getRed() {
        return r;
    }

    @Override
    public int hashCode() {
        int hash = 37;
        hash += 37 * hash + Float.floatToIntBits(r);
        hash += 37 * hash + Float.floatToIntBits(g);
        hash += 37 * hash + Float.floatToIntBits(b);
        hash += 37 * hash + Float.floatToIntBits(a);
        return hash;
    }

    /**
     * Interpolate.
     *
     * @param beginColor the begin color
     * @param finalColor the final color
     * @param changeAmnt the change amnt
     */
    public void interpolate(@NotNull final ColorRGBA beginColor, @NotNull final ColorRGBA finalColor, final float changeAmnt) {
        this.r = (1 - changeAmnt) * beginColor.r + changeAmnt * finalColor.r;
        this.g = (1 - changeAmnt) * beginColor.g + changeAmnt * finalColor.g;
        this.b = (1 - changeAmnt) * beginColor.b + changeAmnt * finalColor.b;
        this.a = (1 - changeAmnt) * beginColor.a + changeAmnt * finalColor.a;
    }

    /**
     * Interpolate.
     *
     * @param finalColor the final color
     * @param changeAmnt the change amnt
     */
    public void interpolate(@NotNull final ColorRGBA finalColor, final float changeAmnt) {
        this.r = (1 - changeAmnt) * this.r + changeAmnt * finalColor.r;
        this.g = (1 - changeAmnt) * this.g + changeAmnt * finalColor.g;
        this.b = (1 - changeAmnt) * this.b + changeAmnt * finalColor.b;
        this.a = (1 - changeAmnt) * this.a + changeAmnt * finalColor.a;
    }

    /**
     * Set color rgba.
     *
     * @param r the r
     * @param g the g
     * @param b the b
     * @param a the a
     * @return the color rgba
     */
    public ColorRGBA set(final float r, final float g, final float b, final float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    @Override
    public String toString() {
        return "ColorRGBA{" +
                "red=" + r +
                ", green=" + g +
                ", blue=" + b +
                ", alpha=" + a +
                '}';
    }
}
