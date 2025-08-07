package javasabr.rlib.common.concurrent.atomic;

import java.io.Serial;
import java.util.concurrent.atomic.AtomicReference;
import javasabr.rlib.common.util.pools.Reusable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The atomic reference with additional methods.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public final class ReusableAtomicReference<V> extends AtomicReference<V> implements Reusable {

  @Serial
  private static final long serialVersionUID = -4058945159519762615L;

  public ReusableAtomicReference() {}

  public ReusableAtomicReference(@Nullable V initialValue) {
    super(initialValue);
  }
}
