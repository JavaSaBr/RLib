package javasabr.rlib.common.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * A thread factory that creates threads within a named thread group.
 *
 * @since 10.0.0
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class GroupThreadFactory implements ThreadFactory {

  /**
   * Functional interface for custom thread construction.
   *
   * @since 10.0.0
   */
  public interface ThreadConstructor {

    /**
     * Creates a new thread.
     *
     * @param group the thread group
     * @param runnable the runnable to execute
     * @param name the thread name
     * @return the new thread
     */
    Thread create(ThreadGroup group, Runnable runnable, String name);
  }

  AtomicInteger ordinal;
  String name;
  ThreadGroup group;
  ThreadConstructor constructor;

  int priority;
  boolean daemon;

  /**
   * Creates a factory with normal priority threads.
   *
   * @param name the base name for threads
   */
  public GroupThreadFactory(String name) {
    this(name, Thread::new, Thread.NORM_PRIORITY);
  }

  /**
   * Creates a factory with specified priority threads.
   *
   * @param name the base name for threads
   * @param priority the thread priority
   */
  public GroupThreadFactory(String name, int priority) {
    this(name, Thread::new, priority);
  }

  /**
   * Creates a factory with custom thread constructor and priority.
   *
   * @param name the base name for threads
   * @param constructor the thread constructor
   * @param priority the thread priority
   */
  public GroupThreadFactory(String name, ThreadConstructor constructor, int priority) {
    this(name, constructor, priority, false);
  }

  /**
   * Creates a factory with full configuration.
   *
   * @param name the base name for threads
   * @param constructor the thread constructor
   * @param priority the thread priority
   * @param daemon whether threads should be daemon threads
   */
  public GroupThreadFactory(String name, ThreadConstructor constructor, int priority, boolean daemon) {
    this.constructor = constructor;
    this.priority = priority;
    this.name = name;
    this.group = new ThreadGroup(name);
    this.ordinal = new AtomicInteger();
    this.daemon = daemon;
  }

  @Override
  public Thread newThread(Runnable runnable) {
    var thread = constructor.create(group, runnable, name + "-" + ordinal.incrementAndGet());
    thread.setPriority(priority);
    thread.setDaemon(daemon);
    return thread;
  }
}
