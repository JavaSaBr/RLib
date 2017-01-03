package rlib.geom.bounding;

/**
 * Тип формы из точек.
 *
 * @author JavaSaBr
 */
public enum BoundingType {
    /**
     * Коробка.
     */
    AXIS_ALIGNED_BOX,
    /**
     * Сфера.
     */
    SPHERE,
    /**
     * Пустой.
     */
    EMPTY,
}
