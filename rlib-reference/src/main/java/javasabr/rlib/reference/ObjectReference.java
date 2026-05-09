package javasabr.rlib.reference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jspecify.annotations.Nullable;

/**
 * A mutable reference to an object value.
 *
 * @param <T> the type of the referenced object
 * @since 10.0.0
 */

@Data
@AllArgsConstructor
@Accessors(fluent = true, chain = false)
public class ObjectReference<T> extends AbstractReference {

  @Nullable
  private T value;
}
