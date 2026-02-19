package javasabr.rlib.reference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A mutable reference to a long value.
 *
 * @since 10.0.0
 */
@Data
@AllArgsConstructor
@Accessors(fluent = true, chain = false)
public class LongReference extends AbstractReference {
  private long value;
}
