package javasabr.rlib.network.packet.impl;

import static javasabr.rlib.common.util.ObjectUtils.notNull;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import javasabr.rlib.common.concurrent.atomic.ReusableAtomicInteger;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.common.util.pools.Pool;
import javasabr.rlib.common.util.pools.PoolFactory;
import javasabr.rlib.network.packet.ReusableWritablePacket;
import org.jspecify.annotations.Nullable;

/**
 * The reusable implementation of {@link AbstractWritablePacket} using the counter to control the life cycle of this
 * packet.
 *
 * @author JavaSaBr
 */
public abstract class AbstractReusableWritablePacket extends AbstractWritablePacket implements ReusableWritablePacket {

  protected static final ThreadLocal<Map<Class<? super ReusableWritablePacket>, Pool<ReusableWritablePacket>>> LOCAL_POOLS = ThreadLocal.withInitial(
      HashMap::new);

  protected final ReusableAtomicInteger counter;

  /**
   * The pool to store this packet after using.
   */
  protected volatile @Nullable Pool<ReusableWritablePacket> pool;
  protected volatile int barrier;

  protected int barrierSink;

  public AbstractReusableWritablePacket() {
    this.counter = new ReusableAtomicInteger();
  }

  @Override
  public boolean write(ByteBuffer buffer) {

    if (counter.get() < 1) {
      LOGGER.warning(
          this,
          arg -> "Attempt to write is already finished packet " + arg + " on thread " + Thread
              .currentThread()
              .getName());
      return false;
    }

    notifyStartedWriting();
    try {
      super.write(buffer);
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
   * Get thread local pool.
   *
   * @return thread local pool.
   */
  protected Pool<ReusableWritablePacket> getThreadLocalPool() {
    Class<ReusableWritablePacket> packetClass = ClassUtils.unsafeNNCast(getClass());
    return LOCAL_POOLS
        .get()
        .computeIfAbsent(packetClass, PoolFactory::newConcurrentStampedLockReusablePool);
  }

  @Override
  public void reuse() {
    this.pool = getThreadLocalPool();
  }

  /**
   * Get the pool to store used packet.
   *
   * @return the pool to store used packet.
   */
  protected Pool<ReusableWritablePacket> getPool() {

    Pool<ReusableWritablePacket> local = this.pool;

    if (local != null) {
      return local;
    }

    this.pool = getThreadLocalPool();

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
  public <T extends ReusableWritablePacket> T newInstance() {

    Pool<ReusableWritablePacket> pool = getPool();
    ReusableWritablePacket result = pool.take(getClass(), ClassUtils::newInstance);
    result.setPool(pool);

    return notNull(ClassUtils.unsafeCast(result));
  }

  @Override
  public final void setPool(Pool<ReusableWritablePacket> pool) {
    this.pool = pool;
  }

  @Override
  public final void decreaseSends() {
    counter.decrementAndGet();
  }

  @Override
  public void decreaseSends(int count) {
    counter.subAndGet(count);
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
    return "AbstractReusableSendablePacket{" + "counter=" + counter + "} " + super.toString();
  }
}
