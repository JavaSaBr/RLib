package javasabr.rlib.collections.operation.impl;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import javasabr.rlib.collections.operation.LockableOperations;
import javasabr.rlib.collections.operation.LockableSource;
import javasabr.rlib.functions.BiObjToBoolFunction;
import javasabr.rlib.functions.ObjIntFunction;
import javasabr.rlib.functions.TriConsumer;
import javasabr.rlib.functions.TriFunction;

public record DefaultLockableOperations<S extends LockableSource>(S source)
    implements LockableOperations<S> {

  @Override
  public <R> R getInReadLock(Function<S, R> function) {
    long stamp = source.readLock();
    try {
      return function.apply(source);
    } finally {
      source.readUnlock(stamp);
    }
  }

  @Override
  public <R, F> R getInReadLock(F arg1, BiFunction<S, F, R> function) {
    long stamp = source.readLock();
    try {
      return function.apply(source, arg1);
    } finally {
      source.readUnlock(stamp);
    }
  }

  @Override
  public <R> R getInReadLock(int arg1, ObjIntFunction<S, R> function) {
    long stamp = source.readLock();
    try {
      return function.apply(source, arg1);
    } finally {
      source.readUnlock(stamp);
    }
  }

  @Override
  public <R, A, B> R getInReadLock(A arg1, B arg2, TriFunction<S, A, B, R> function) {
    long stamp = source.readLock();
    try {
      return function.apply(source, arg1, arg2);
    } finally {
      source.readUnlock(stamp);
    }
  }

  @Override
  public <A> boolean getBooleanInReadLock(A arg1, BiObjToBoolFunction<S, A> function) {
    long stamp = source.readLock();
    try {
      return function.apply(source, arg1);
    } finally {
      source.readUnlock(stamp);
    }
  }

  @Override
  public <A> void inReadLock(A arg1, BiConsumer<S, A> function) {
    long stamp = source.readLock();
    try {
      function.accept(source, arg1);
    } finally {
      source.readUnlock(stamp);
    }
  }

  @Override
  public <R, F> R getInWriteLock(F arg1, BiFunction<S, F, R> function) {
    long stamp = source.writeLock();
    try {
      return function.apply(source, arg1);
    } finally {
      source.writeUnlock(stamp);
    }
  }

  @Override
  public <R, A, B> R getInWriteLock(A arg1, B arg2, TriFunction<S, A, B, R> function) {
    long stamp = source.writeLock();
    try {
      return function.apply(source, arg1, arg2);
    } finally {
      source.writeUnlock(stamp);
    }
  }

  @Override
  public void inWriteLock(Consumer<S> function) {
    long stamp = source.writeLock();
    try {
      function.accept(source);
    } finally {
      source.writeUnlock(stamp);
    }
  }

  @Override
  public <A> void inWriteLock(A arg1, BiConsumer<S, A> function) {
    long stamp = source.writeLock();
    try {
      function.accept(source, arg1);
    } finally {
      source.writeUnlock(stamp);
    }
  }

  @Override
  public <A, B> void inWriteLock(A arg1, B arg2, TriConsumer<S, A, B> function) {
    long stamp = source.writeLock();
    try {
      function.accept(source, arg1, arg2);
    } finally {
      source.writeUnlock(stamp);
    }
  }
}
