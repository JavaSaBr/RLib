package javasabr.rlib.collection.tree;

import javasabr.rlib.collection.AbstractDictionaryEntry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractRBTreeEntry<V, E extends AbstractDictionaryEntry<V, ?>> extends
    AbstractDictionaryEntry<V, AbstractDictionaryEntry<V, E>> {

    protected static final boolean RED   = false;
    protected static final boolean BLACK = true;

    @Nullable E left;
    @Nullable E right;

    boolean color = BLACK;

    protected AbstractRBTreeEntry(@NotNull V value) {
        super(value);
    }
}
