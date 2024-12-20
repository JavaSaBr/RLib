package javasabr.rlib.collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@Accessors(fluent = true)
public abstract class AbstractDictionaryEntry<V> {
    protected V value;
}
