package rlib.util.linkedlist;

import java.io.Serializable;
import java.util.Deque;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import rlib.function.LongBiObjectConsumer;
import rlib.function.TripleConsumer;
import rlib.function.TriplePredicate;
import rlib.util.linkedlist.impl.Node;
import rlib.util.pools.Reusable;

/**
 * Интерфей с для реализации связанного списка. Главное преймущество, это переиспользование узлов
 * списка и быстрая итерация с уменьшением нагрузки на GC. Создаются с помощью {@link
 * LinkedListFactory}. <p>
 * <pre>
 * for(Node&lt;E&gt; node = getFirstNode(); node != null; node = node.getNext()) {
 * 	? item = node.getItem();
 * 	// handle item
 * }
 * </pre>
 *
 * @author JavaSaBr
 */
public interface LinkedList<E> extends Deque<E>, Cloneable, Serializable, Reusable {

    @Override
    default void forEach(final Consumer<? super E> consumer) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            consumer.accept(node.getItem());
        }
    }

    /**
     * Итерирование списка.
     *
     * @param predicate фильтр элементов.
     * @param consumer  функция для обработки элементов.
     */
    default void forEach(final Predicate<E> predicate, final Consumer<? super E> consumer) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            final E item = node.getItem();
            if (predicate.test(item)) consumer.accept(item);
        }
    }

    /**
     * Итерирование списка с дополнительным аргументом.
     *
     * @param argument дополнительный аргумент.
     * @param consumer функция для обработки элементов.
     */
    default <T> void forEach(final T argument, final BiConsumer<T, E> consumer) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            consumer.accept(argument, node.getItem());
        }
    }

    /**
     * Итерирование списка с дополнительным аргументом.
     *
     * @param argument  дополнительный аргумент.
     * @param predicate фильтр элементов.
     * @param consumer  функция для обработки элементов.
     */
    default <T> void forEach(final T argument, final BiPredicate<E, T> predicate, final BiConsumer<T, E> consumer) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            final E item = node.getItem();
            if (predicate.test(item, argument)) consumer.accept(argument, item);
        }
    }

    /**
     * Итерирование массива с дополнительным аргументом.
     *
     * @param first     первый аргумент.
     * @param second    второй аргумент.
     * @param predicate фильтр элементов.
     * @param consumer  функция для обработки элементов.
     */
    default <F, S> void forEach(final F first, final S second, final TriplePredicate<E, F, S> predicate, final TripleConsumer<F, S, E> consumer) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            final E item = node.getItem();
            if (predicate.test(item, first, second)) consumer.accept(first, second, item);
        }
    }

    /**
     * Итерирование массива с двумя дополнительными аргументомами.
     *
     * @param first    первый дополнительный аргумент.
     * @param second   второй дополнительный аргумент.
     * @param consumer функция для обработки элементов.
     */
    default <F, S> void forEach(final F first, final S second, final TripleConsumer<F, S, E> consumer) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            consumer.accept(first, second, node.getItem());
        }
    }

    /**
     * Итерирование массива с двумя дополнительными аргументомами.
     *
     * @param first    первый дополнительный аргумент.
     * @param second   второй дополнительный аргумент.
     * @param consumer функция для обработки элементов.
     */
    default <F> void forEach(final long first, final F second, final LongBiObjectConsumer<F, E> consumer) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            consumer.accept(first, second, node.getItem());
        }
    }

    /**
     * Применить функцию замены всех элементов.
     *
     * @param function применяемая функция.
     */
    default void apply(final Function<? super E, ? extends E> function) {
        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
            node.setItem(function.apply(node.getItem()));
        }
    }

    /**
     * Получение элемента по номеру в списке.
     *
     * @param index номер в списке.
     * @return искомый элемент.
     */
    E get(int index);

    /**
     * @return первый узел списка.
     */
    Node<E> getFirstNode();

    /**
     * @return последний узел списка.
     */
    Node<E> getLastNode();

    /**
     * @param object интересуемый объект.
     * @return номер его в списке.
     */
    int indexOf(Object object);

    /**
     * Returns the (non-null) Node at the specified element index.
     */
    default Node<E> node(int index) {

        final int size = size();

        if (index < (size >> 1)) {

            Node<E> node = getFirstNode();

            for (int i = 0; i < index; i++) {
                node = node.getNext();
            }

            return node;

        } else {

            Node<E> node = getLastNode();

            for (int i = size() - 1; i > index; i--) {
                node = node.getPrev();
            }

            return node;
        }
    }

    /**
     * Блокировка изменения массива на время чтения его.
     */
    default void readLock() {
    }

    /**
     * Разблокировка изменения массива.
     */
    default void readUnlock() {
    }

    /**
     * Удаление элемента из списка по индексу.
     *
     * @param index индекс элемента.
     * @return удаленный элемент.
     */
    default E remove(int index) {
        return unlink(node(index));
    }

    /**
     * Получание с удалением первого элемента.
     *
     * @return первый элемент в очереди.
     */
    E take();

    /**
     * Удаление узла в списке.
     *
     * @param node удаляемый узел.
     * @return удаленный элемент из узла.
     */
    E unlink(Node<E> node);

    /**
     * Блокировка чтений для изменения массива.
     */
    default void writeLock() {
    }

    /**
     * Разблокировка чтения массива.
     */
    default void writeUnlock() {
    }
}
