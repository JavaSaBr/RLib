package javasabr.rlib.collections.operation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import javasabr.rlib.collections.operation.impl.DefaultLockableOperations;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DefaultLockableOperationsTest {

  private static class TestLockableSource implements LockableSource {

    int readLocks;
    int writeLocks;

    @Override
    public long readLock() {
      readLocks++;
      return 0;
    }

    @Override
    public void readUnlock(long stamp) {
      readLocks--;
    }

    @Override
    public long tryOptimisticRead() {
      readLocks++;
      return 0;
    }

    @Override
    public boolean validateLock(long stamp) {
      return false;
    }

    @Override
    public long writeLock() {
      writeLocks++;
      return 0;
    }

    @Override
    public void writeUnlock(long stamp) {
      writeLocks--;
    }
  }

  @ParameterizedTest
  @MethodSource("generateOperations")
  void shouldGetInReadLock(DefaultLockableOperations<TestLockableSource> operations) {
    // when:
    var result = operations.getInReadLock(source -> {
      assertThat(source.writeLocks).isEqualTo(0);
      assertThat(source.readLocks).isEqualTo(1);
      return "test";
    });

    // then:
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(operations.source().readLocks).isEqualTo(0);
    assertThat(result).isEqualTo("test");
  }

  @ParameterizedTest
  @MethodSource("generateOperations")
  void shouldGetInReadLock2(DefaultLockableOperations<TestLockableSource> operations) {
    // when:
    var result = operations.getInReadLock("_arg", (source, arg1) -> {
      assertThat(source.writeLocks).isEqualTo(0);
      assertThat(source.readLocks).isEqualTo(1);
      return "test" + arg1;
    });

    // then:
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(operations.source().readLocks).isEqualTo(0);
    assertThat(result).isEqualTo("test_arg");
  }

  @ParameterizedTest
  @MethodSource("generateOperations")
  void shouldGetInReadLock3(DefaultLockableOperations<TestLockableSource> operations) {
    // when:
    var result = operations.getInReadLock(15, (source, arg1) -> {
      assertThat(source.writeLocks).isEqualTo(0);
      assertThat(source.readLocks).isEqualTo(1);
      return "test" + arg1;
    });

    // then:
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(operations.source().readLocks).isEqualTo(0);
    assertThat(result).isEqualTo("test15");
  }

  @ParameterizedTest
  @MethodSource("generateOperations")
  void shouldGetInReadLock4(DefaultLockableOperations<TestLockableSource> operations) {
    // when:
    var result = operations.getInReadLock("arg1_", "_arg2", (source, arg1, arg2) -> {
      assertThat(source.writeLocks).isEqualTo(0);
      assertThat(source.readLocks).isEqualTo(1);
      return arg1 + "test" + arg2;
    });

    // then:
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(operations.source().readLocks).isEqualTo(0);
    assertThat(result).isEqualTo("arg1_test_arg2");
  }

  @ParameterizedTest
  @MethodSource("generateOperations")
  void shouldGetInReadLock5(DefaultLockableOperations<TestLockableSource> operations) {
    // when:
    var result = operations.getInReadLock(true, (source, arg1) -> {
      assertThat(source.writeLocks).isEqualTo(0);
      assertThat(source.readLocks).isEqualTo(1);
      return "test" + arg1;
    });

    // then:
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(operations.source().readLocks).isEqualTo(0);
    assertThat(result).isEqualTo("testtrue");
  }

  @ParameterizedTest
  @MethodSource("generateOperations")
  void shouldInReadLock(DefaultLockableOperations<TestLockableSource> operations) {
    // given:
    var capture = new AtomicReference<String>();

    // when:
    operations.inReadLock("test_arg", (source, arg1) -> {
      assertThat(source.writeLocks).isEqualTo(0);
      assertThat(source.readLocks).isEqualTo(1);
      capture.set("test_" + arg1);
    });

    // then:
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(operations.source().readLocks).isEqualTo(0);
    assertThat(capture.get()).isEqualTo("test_test_arg");
  }

  @ParameterizedTest
  @MethodSource("generateOperations")
  void shouldGetWriteLock(DefaultLockableOperations<TestLockableSource> operations) {
    // when:
    var result = operations.getInWriteLock("_arg", (source, arg1) -> {
      assertThat(source.writeLocks).isEqualTo(1);
      assertThat(source.readLocks).isEqualTo(0);
      return "test" + arg1;
    });

    // then:
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(result).isEqualTo("test_arg");
  }

  @ParameterizedTest
  @MethodSource("generateOperations")
  void shouldGetWriteLock2(DefaultLockableOperations<TestLockableSource> operations) {
    // when:
    var result = operations.getInWriteLock("arg1_", "_arg2", (source, arg1, arg2) -> {
      assertThat(source.writeLocks).isEqualTo(1);
      assertThat(source.readLocks).isEqualTo(0);
      return arg1 + "test" + arg2;
    });

    // then:
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(result).isEqualTo("arg1_test_arg2");
  }

  @ParameterizedTest
  @MethodSource("generateOperations")
  void shouldInWriteLock(DefaultLockableOperations<TestLockableSource> operations) {
    // given:
    var capture = new AtomicReference<String>();

    // when:
    operations.inWriteLock(source -> {
      assertThat(source.writeLocks).isEqualTo(1);
      assertThat(source.readLocks).isEqualTo(0);
      capture.set("test");
    });

    // then:
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(operations.source().readLocks).isEqualTo(0);
    assertThat(capture.get()).isEqualTo("test");
  }

  @ParameterizedTest
  @MethodSource("generateOperations")
  void shouldInWriteLock2(DefaultLockableOperations<TestLockableSource> operations) {
    // given:
    var capture = new AtomicReference<String>();

    // when:
    operations.inWriteLock("test_arg", (source, arg1) -> {
      assertThat(source.writeLocks).isEqualTo(1);
      assertThat(source.readLocks).isEqualTo(0);
      capture.set("test_" + arg1);
    });

    // then:
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(operations.source().readLocks).isEqualTo(0);
    assertThat(capture.get()).isEqualTo("test_test_arg");
  }

  @ParameterizedTest
  @MethodSource("generateOperations")
  void shouldInWriteLock3(DefaultLockableOperations<TestLockableSource> operations) {
    // given:
    var capture = new AtomicReference<String>();

    // when:
    operations.inWriteLock("arg1_", "_arg2", (source, arg1, arg2) -> {
      assertThat(source.writeLocks).isEqualTo(1);
      assertThat(source.readLocks).isEqualTo(0);
      capture.set(arg1 + "test" + arg2);
    });

    // then:
    assertThat(operations.source().writeLocks).isEqualTo(0);
    assertThat(operations.source().readLocks).isEqualTo(0);
    assertThat(capture.get()).isEqualTo("arg1_test_arg2");
  }

  private static Stream<Arguments> generateOperations() {
    return Stream.of(Arguments.of(new DefaultLockableOperations<>(new TestLockableSource())));
  }
}
