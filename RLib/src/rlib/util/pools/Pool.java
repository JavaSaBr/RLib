package rlib.util.pools;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Интерфейс для реализации объектоного пула. Используется при подходе к переиспользованию объектов
 * для облегчения нагрузки на GC. Создается с помощью {@link PoolFactory}
 *
 * @author Ronn
 * @see PoolFactory
 */
public interface Pool<E> {

    /**
     * @return пустой ли пул.
     */
    public boolean isEmpty();

    /**
     * Положить объект в пул.
     *
     * @param object объект, который хотим положить.
     */
    public void put(E object);

    /**
     * Удаление из пула объекта.
     *
     * @param object удаляемый объект.
     */
    public void remove(E object);

    /**
     * Взять из пула объект.
     *
     * @return объект из пула.
     */
    public E take();

    /**
     * Взять из пула объект или же создать новый.
     *
     * @param factory фабрика объектов.
     * @return объект из пула либо новй объект.
     */
    public default E take(final Supplier<E> factory) {
        final E take = take();
        return take != null ? take : factory.get();
    }

    /**
     * Взять из пула объект или же создать новый с использованием дополнительного аргумента.
     *
     * @param argument дополнительный аргумент.
     * @param factory  фабрика объектов.
     * @return объект из пула либо новй объект.
     */
    public default <T> E take(final T argument, final Function<T, E> factory) {
        final E take = take();
        return take != null ? take : factory.apply(argument);
    }

    /**
     * Взять из пула объект или же создать новый с использованием дополнительных аргументов.
     *
     * @param first   дополнительный аргумент.
     * @param second  дополнительный аргумент.
     * @param factory фабрика объектов.
     * @return объект из пула либо новй объект.
     */
    public default <F, S> E take(final F first, S second, final BiFunction<F, S, E> factory) {
        final E take = take();
        return take != null ? take : factory.apply(first, second);
    }
}
