package rlib.util.pools;

/**
 * Интерфейс для реализации объектоного пула, в котором хранятся объекты реализующий интерфейс
 * {@link Reusable}. Используется при подходе с умным переиспользованием объектов для уменьшения
 * нагрузки на GC. Создается с помощью {@link PoolFactory}.
 *
 * @author Ronn
 */
public interface ReusablePool<E extends Reusable> extends Pool<E> {

}
