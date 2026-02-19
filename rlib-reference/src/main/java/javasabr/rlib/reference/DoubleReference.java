package javasabr.rlib.reference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A mutable reference to a double value.
 *
 * @since 10.0.0
 */
@Data
@AllArgsConstructor
@Accessors(fluent = true, chain = false)
public class DoubleReference extends AbstractReference {
  private double value;
}
