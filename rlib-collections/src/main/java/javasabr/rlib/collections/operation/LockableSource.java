package javasabr.rlib.collections.operation;

/**
 * A source that provides locking primitives for thread-safe access.
 *
 * @since 10.0.0
 */
public interface LockableSource {

  /**
   * Acquires a read lock and returns a stamp for later unlock.
   *
   * @return a stamp representing the acquired lock
   * @since 10.0.0
   */
  long readLock();

  /**
   * Releases a read lock using the provided stamp.
   *
   * @param stamp the stamp from readLock()
   * @since 10.0.0
   */
  void readUnlock(long stamp);

  /**
   * Attempts an optimistic read, returning a stamp for validation.
   *
   * @return a stamp for validation
   * @since 10.0.0
   */
  long tryOptimisticRead();

  /**
   * Validates that a lock stamp is still valid.
   *
   * @param stamp the stamp to validate
   * @return true if the stamp is still valid
   * @since 10.0.0
   */
  boolean validateLock(long stamp);

  /**
   * Acquires a write lock and returns a stamp for later unlock.
   *
   * @return a stamp representing the acquired lock
   * @since 10.0.0
   */
  long writeLock();

  /**
   * Releases a write lock using the provided stamp.
   *
   * @param stamp the stamp from writeLock()
   * @since 10.0.0
   */
  void writeUnlock(long stamp);
}
