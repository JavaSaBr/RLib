package rlib.geom;

/**
 * Интерфейс для реализации позиции с вещесвенным координатами.
 *
 * @author JavaSaBr
 */
public interface GamePoint {

    /**
     * @return направление позиции.
     */
    public int getHeading();

    /**
     * @param heading направление позиции.
     * @return this.
     */
    public GamePoint setHeading(int heading);

    /**
     * @return х координата.
     */
    public float getX();

    /**
     * @param x координата.
     * @return this.
     */
    public GamePoint setX(float x);

    /**
     * @return y координата.
     */
    public float getY();

    /**
     * @param y координата.
     * @return this.
     */
    public GamePoint setY(float y);

    /**
     * @return z координата.
     */
    public float getZ();

    /**
     * @param z координата.
     * @return this.
     */
    public GamePoint setZ(float z);

    /**
     * @param x координата.
     * @param y координата.
     * @param z координата.
     * @return this.
     */
    public GamePoint setXYZ(float x, float y, float z);

    /**
     * @param x       координата.
     * @param y       координата.
     * @param z       координата.
     * @param heading направление разварота.
     * @return this.
     */
    public GamePoint setXYZH(float x, float y, float z, int heading);
}
