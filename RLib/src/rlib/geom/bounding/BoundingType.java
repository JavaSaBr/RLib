package rlib.geom.bounding;

/**
 * Тип формы из точек.
 *
 * @author Ronn
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
