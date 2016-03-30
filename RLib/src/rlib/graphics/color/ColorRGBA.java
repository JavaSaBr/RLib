package rlib.graphics.color;

import java.io.Serializable;

/**
 * @author Ronn
 */
public final class ColorRGBA implements Cloneable, Serializable {

    private static final long serialVersionUID = -3342702659372723983L;

    public float r;
    public float g;
    public float b;
    public float a;

    public ColorRGBA() {
        r = g = b = a = 1.0f;
    }

    public ColorRGBA(final ColorRGBA rgba) {
        this.a = rgba.a;
        this.r = rgba.r;
        this.g = rgba.g;
        this.b = rgba.b;
    }

    public ColorRGBA(final float r, final float g, final float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1F;
    }

    public ColorRGBA(final float r, final float g, final float b, final float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public ColorRGBA(final float[] array) {
        this.r = array[0];
        this.g = array.length > 1 ? array[1] : 1F;
        this.b = array.length > 2 ? array[2] : 1F;
        this.a = array.length > 3 ? array[3] : 1F;
    }

    public int asIntABGR() {
        final int abgr = ((int) (a * 255) & 0xFF) << 24 | ((int) (b * 255) & 0xFF) << 16 | ((int) (g * 255) & 0xFF) << 8 | (int) (r * 255) & 0xFF;
        return abgr;
    }

    public int asIntARGB() {
        final int argb = ((int) (a * 255) & 0xFF) << 24 | ((int) (r * 255) & 0xFF) << 16 | ((int) (g * 255) & 0xFF) << 8 | (int) (b * 255) & 0xFF;
        return argb;
    }

    public int asIntRGBA() {
        final int rgba = ((int) (r * 255) & 0xFF) << 24 | ((int) (g * 255) & 0xFF) << 16 | ((int) (b * 255) & 0xFF) << 8 | (int) (a * 255) & 0xFF;
        return rgba;
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

    public void fromIntARGB(final int color) {
        a = ((byte) (color >> 24) & 0xFF) / 255f;
        r = ((byte) (color >> 16) & 0xFF) / 255f;
        g = ((byte) (color >> 8) & 0xFF) / 255f;
        b = ((byte) color & 0xFF) / 255f;
    }

    public void fromIntRGBA(final int color) {
        r = ((byte) (color >> 24) & 0xFF) / 255f;
        g = ((byte) (color >> 16) & 0xFF) / 255f;
        b = ((byte) (color >> 8) & 0xFF) / 255f;
        a = ((byte) color & 0xFF) / 255f;
    }

    public float getAlpha() {
        return a;
    }

    public float getBlue() {
        return b;
    }

    public float getGreen() {
        return g;
    }

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

    public void interpolate(final ColorRGBA beginColor, final ColorRGBA finalColor, final float changeAmnt) {
        this.r = (1 - changeAmnt) * beginColor.r + changeAmnt * finalColor.r;
        this.g = (1 - changeAmnt) * beginColor.g + changeAmnt * finalColor.g;
        this.b = (1 - changeAmnt) * beginColor.b + changeAmnt * finalColor.b;
        this.a = (1 - changeAmnt) * beginColor.a + changeAmnt * finalColor.a;
    }

    public void interpolate(final ColorRGBA finalColor, final float changeAmnt) {
        this.r = (1 - changeAmnt) * this.r + changeAmnt * finalColor.r;
        this.g = (1 - changeAmnt) * this.g + changeAmnt * finalColor.g;
        this.b = (1 - changeAmnt) * this.b + changeAmnt * finalColor.b;
        this.a = (1 - changeAmnt) * this.a + changeAmnt * finalColor.a;
    }

    public ColorRGBA set(final float r, final float g, final float b, final float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }
}
