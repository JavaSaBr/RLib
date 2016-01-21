package rlib.util.array;

import java.util.Iterator;

/**
 * Интерфейс для реализации расширенного итератора {@link Array}.
 *
 * @author Ronn
 */
public interface ArrayIterator<E> extends Iterator<E> {

    /**
     * Удалить с переносом последнего элемента на место удаленного.
     */
    public void fastRemove();

    /**
     * @return позиция элемента в {@link Array}.
     */
    public int index();

    /**
     * Удалить со сдвигом следующих элементов.
     */
    public void slowRemove();
}
