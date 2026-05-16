package javasabr.rlib.network.packet.impl;

import static javasabr.rlib.common.util.ObjectUtils.notNull;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.packet.ReusableWritablePacket;
import javasabr.rlib.reusable.pool.Pool;
import javasabr.rlib.reusable.pool.PoolFactory;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * The reusable implementation of {@link AbstractWritableNetworkPacket} using the counter to control the life cycle of this
 * packet.
 *
 * @author JavaSaBr
 */
@CustomLog
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractReusableWritableNetworkPacket<C extends Connection<C>>
    extends AbstractWritableNetworkPacket<C> implements ReusableWritablePacket<C> {

  protected static final ThreadLocal<Map<Class<? super ReusableWritablePacket>, Pool<ReusableWritablePacket>>>
      LOCAL_POOLS = ThreadLocal.withInitial(HashMap::new);

  final AtomicInteger counter;

  /**
   * The pool to store this packet after using.
   */
  @Nullable
  volatile Pool<ReusableWritablePacket<C>> pool;
  volatile int barrier;

  int barrierSink;

  public AbstractReusableWritableNetworkPacket() {
    this.counter = new AtomicInteger();
  }

  @Override
  public boolean write(C connection, ByteBuffer buffer) {

    if (counter.get() < 1) {
      log.warn(this, arg ->
          "Attempt to write is already finished packet:[%s] on thread:[%s]".formatted(arg, Thread.currentThread().getName()));
      return false;
    }

    notifyStartedWriting();
    try {
      super.write(connection, buffer);
    } finally {
      notifyFinishedWriting();
    }

    return true;
  }

  /**
   * Notify about started preparing data.
   */
  protected void notifyStartedPreparing() {
    barrierSink = barrier;
  }

  /**
   * Notify about finished preparing data.
   */
  protected void notifyFinishedPreparing() {
    barrier = barrierSink + 1;
  }

  /**
   * Notify about started writing data.
   */
  protected void notifyStartedWriting() {
    barrierSink = barrier;
  }

  /**
   * Notify about finished writing data.
   */
  protected void notifyFinishedWriting() {
    barrier = barrierSink + 1;
  }

  /**
   * Notify about started storing instance to a pool.
   */
  protected void notifyStartedStoring() {
    barrierSink = barrier;
  }

  /**
   * Notify about finished storing instance to a pool.
   */
  protected void notifyFinishedStoring() {
    barrier = barrierSink + 1;
  }

  @Override
  public void complete() {
    if (counter.decrementAndGet() == 0) {
      completeImpl();
    }
  }

  @Override
  public void forceComplete() {
    counter.set(1);
    complete();
  }

  /**
   * Gets thread local pool.
   *
   * @return thread local pool.
   */
  protected Pool<ReusableWritablePacket<C>> threadLocalPool() {
    Pool<ReusableWritablePacket> resultPool = LOCAL_POOLS.get()
        .computeIfAbsent(ClassUtils.unsafeNNCast(getClass()), PoolFactory::newLockBasePool);
    return ClassUtils.unsafeCast(resultPool);
  }

  /**
   * Gets the pool to store used packet.
   *
   * @return the pool to store used packet.
   */
  protected Pool<ReusableWritablePacket<C>> getPool() {

    Pool<ReusableWritablePacket<C>> local = this.pool;
    if (local != null) {
      return local;
    }

    this.pool = threadLocalPool();

    local = this.pool;
    return notNull(local);
  }

  /**
   * Implementation of handling completion of packet sending.
   */
  protected void completeImpl() {
    notifyStartedStoring();
    try {
      getPool().put(this);
    } finally {
      notifyFinishedStoring();
    }
  }

  /**
   * Get a new instance of this packet.
   *
   * @param <T> the result packet's type.
   * @return the new instance.
   */
  public <T extends ReusableWritablePacket<C>> T newInstance() {

    Pool<ReusableWritablePacket<C>> pool = getPool();
    ReusableWritablePacket<C> result = pool.take(getClass(), ClassUtils::newInstance);
    result.setPool(pool);

    return notNull(ClassUtils.unsafeCast(result));
  }

  @Override
  public final void setPool(Pool<ReusableWritablePacket<C>> pool) {
    this.pool = pool;
  }

  @Override
  public final void decreaseSends() {
    counter.decrementAndGet();
  }

  @Override
  public void decreaseSends(int count) {
    counter.accumulateAndGet(count, (left, right) -> left - right);
  }

  @Override
  public void increaseSends() {
    counter.incrementAndGet();
  }

  @Override
  public void increaseSends(int count) {
    counter.addAndGet(count);
  }

  @Override
  public String toString() {
    return "AbstractReusableWritableNetworkPacket{" + "counter=" + counter + "} " + super.toString();
  }
}
