package javasabr.rlib.collection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractDictionaryEntry<V, E extends AbstractDictionaryEntry<V, ?>> {
    @Nullable E parent;
    @NotNull V value;
}
