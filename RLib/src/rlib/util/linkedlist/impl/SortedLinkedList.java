package rlib.util.linkedlist.impl;

/**
 * Реализация {@link FastLinkedList} с сортировкой при вставке нового элемента.
 *
 * @author JavaSaBr
 */
public class SortedLinkedList<E extends Comparable<E>> extends FastLinkedList<E> {

    private static final long serialVersionUID = -8115760928469233254L;

    public SortedLinkedList(final Class<?> type) {
        super(type);
    }

    @Override
    public boolean add(final E element) {

        if (element == null) {
            throw new RuntimeException("element is null.");
        }

        for (Node<E> node = getFirstNode(); node != null; node = node.getNext()) {

            final E item = node.getItem();

            if (element.compareTo(item) < 0) {
                insertBefore(node, element);
                return true;
            }
        }

        linkLast(element);
        return true;
    }
}
