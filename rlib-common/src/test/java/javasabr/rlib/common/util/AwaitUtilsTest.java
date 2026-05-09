package javasabr.rlib.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link AwaitUtils} methods.
 *
 * @author crazyrokr
 */
public class AwaitUtilsTest {

  @Test
  void shouldAwaitCondition() throws InterruptedException {
    // given
    var condition = new AtomicBoolean(false);
    var thread = new Thread(() -> {
      try {
        Thread.sleep(100);
        condition.set(true);
      } catch (InterruptedException e) {
        // ignore
      }
    });

    // when
    thread.start();
    boolean result = AwaitUtils.await(500, ChronoUnit.MILLIS, condition::get);

    // then
    assertThat(result).isTrue();
  }

  @Test
  void shouldTimeoutIfConditionNotMet() throws InterruptedException {
    // given
    var condition = new AtomicBoolean(false);

    // when
    boolean result = AwaitUtils.await(100, ChronoUnit.MILLIS, condition::get);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void shouldAwaitWithChronoUnit() throws InterruptedException {
    // given
    var condition = new AtomicBoolean(false);
    var thread = new Thread(() -> {
      try {
        Thread.sleep(100);
        condition.set(true);
      } catch (InterruptedException e) {
        // ignore
      }
    });

    // when
    thread.start();
    boolean result = AwaitUtils.await(1, ChronoUnit.SECONDS, condition::get);

    // then
    assertThat(result).isTrue();
  }
}
