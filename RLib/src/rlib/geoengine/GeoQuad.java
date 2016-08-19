package rlib.geoengine;

/**
 * Модель квадрата гео.
 *
 * @author JavaSaBr
 */
public final class GeoQuad {

    /**
     * Индекс квадрата по X.
     */
    private final int x;

    /**
     * Индекс квадрата по Y.
     */
    private final int y;

    /**
     * Высота квадрата.
     */
    private float height;

    public GeoQuad(final int x, final int y, final float height) {
        this.x = x;
        this.y = y;
        this.height = height;
    }

    /**
     * @return высота квадрата.
     */
    public final float getHeight() {
        return height;
    }

    /**
     * @param height высота квадрата.
     */
    public final void setHeight(final float height) {
        this.height = height;
    }

    /**
     * @return индекс квадрата по X.
     */
    public final int getX() {
        return x;
    }

    /**
     * @return индекс квадрата по Y.
     */
    public final int getY() {
        return y;
    }


    @Override
    public String toString() {
        return "GeoQuad{" +
                "x=" + x +
                ", y=" + y +
                ", height=" + height +
                '}';
    }
}
