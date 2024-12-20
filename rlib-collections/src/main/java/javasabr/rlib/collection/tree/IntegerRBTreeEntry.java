package javasabr.rlib.collection.tree;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IntegerRBTreeEntry<V> extends AbstractRBTreeEntry<V, IntegerRBTreeEntry<V>> {

    int key;

    public IntegerRBTreeEntry(int key, @NotNull V value) {
        super(value);
        this.key = key;
    }
}
