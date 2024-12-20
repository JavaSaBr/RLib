package javasabr.rlib.collection.tree;

import javasabr.rlib.collection.Dictionary;
import javasabr.rlib.collection.IntKey;
import javasabr.rlib.collection.IntegerDictionary;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntegerTreeDictionary<V> implements IntegerDictionary<V> {

    IntegerRBTreeEntry<V> root;
    int size;

    @Override
    public @Nullable V put(int key, @NotNull V value) {

        var node = root;
        if (node == null) {
            root = new IntegerRBTreeEntry<>(key, value);
            size = 1;
            return null;
        }

        int compare;
        IntegerRBTreeEntry<V> parent;
        do {
            parent = node;
            compare = key - node.key();
            if (compare < 0) {
                node = node.left();
            } else if (compare > 0) {
                node = node.right();
            } else {
                V old = node.value();
                node.value(value);
                return old;
            }
        } while (node != null);



        TreeMap.Entry<K,V> e = new TreeMap.Entry<>(key, value, parent);
        if (compare < 0)
            parent.left = e;
        else
            parent.right = e;
        fixAfterInsertion(e);
        size++;
        modCount++;
        return null;

        return IntegerDictionary.super.put(key, value);
    }

    @Override
    public void copyTo(@NotNull Dictionary<? super IntKey, ? super V> dictionary) {

    }

    @Override
    public @NotNull Iterator<V> iterator() {
        return null;
    }
}
