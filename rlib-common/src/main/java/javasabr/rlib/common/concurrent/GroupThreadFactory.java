package javasabr.rlib.common.concurrent;

import java.util.concurrent.ThreadFactory;
import javasabr.rlib.common.concurrent.atomic.ReusableAtomicInteger;
import org.jspecify.annotations.NullMarked;

/**
 * The implementation of the {@link ThreadFactory} to create threads in the same thread group.
 *
 * @author JavaSaBr
 */
@NullMarked
public class GroupThreadFactory implements ThreadFactory {

  @NullMarked
  public interface ThreadConstructor {

    Thread create(ThreadGroup group, Runnable runnable, String name);
  }

  private final ReusableAtomicInteger ordinal;
  private final String name;
  private final ThreadGroup group;
  private final ThreadConstructor constructor;

  private final int priority;
  private final boolean daemon;

  public GroupThreadFactory(String name) {
    this(name, Thread::new, Thread.NORM_PRIORITY);
  }

  public GroupThreadFactory(String name, int priority) {
    this(name, Thread::new, priority);
  }

  public GroupThreadFactory(String name, ThreadConstructor constructor, int priority) {
    this(name, constructor, priority, false);
  }

  public GroupThreadFactory(String name, ThreadConstructor constructor, int priority, boolean daemon) {
    this.constructor = constructor;
    this.priority = priority;
    this.name = name;
    this.group = new ThreadGroup(name);
    this.ordinal = new ReusableAtomicInteger();
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
