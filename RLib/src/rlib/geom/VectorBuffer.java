package rlib.geom;

/**
 * Интерфейс для реализации контейнера векторов для промежуточных вычислений.
 *
 * @author Ronn
 */
public interface VectorBuffer {

    /**
     * @return выдача вектора для рассчетов.
     */
    public Vector getNextVector();
}
