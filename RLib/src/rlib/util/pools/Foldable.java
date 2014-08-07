package rlib.util.pools;

/**
 * Интерфейс для реализации методов по финалзации и переинициализации
 * используемых объектов совместо с {@link FoldablePool}.
 * 
 * @see FoldablePool
 * @author Ronn
 */
public interface Foldable {

	/**
	 * Подготовка объекта к складыванию в объектный пул.
	 */
	public default void finalyze() {
	}

	/**
	 * Подготовка объекта к использованию после вытаскивания из объектного пула.
	 */
	public default void reinit() {
	}

	/**
	 * Завершение испольозвания объекта.
	 */
	public default void release() {
	}
}
