package javasabr.rlib.eventbus.impl;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.collections.array.UnsafeMutableArray;
import javasabr.rlib.collections.dictionary.DictionaryFactory;
import javasabr.rlib.collections.dictionary.MutableRefToRefDictionary;
import javasabr.rlib.eventbus.EventBus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultEventBus<S extends EventBus.TypeIdSet> implements EventBus<S> {

  final MutableRefToRefDictionary<Class<? extends Event<S>>, TypeId<S, ? extends Event<S>>> knownEventTypes;
  final UnsafeMutableArray<UnsafeMutableArray<Consumer<?>>> consumers;
  final Executor asyncExecutor;

  public DefaultEventBus() {
    this(ForkJoinPool.commonPool());
  }

  public DefaultEventBus(Executor asyncExecutor) {
    MutableArray<UnsafeMutableArray<Consumer<?>>> consumers = ArrayFactory.copyOnModifyArray(MutableArray.class);
    this.consumers = consumers.asUnsafe();
    this.knownEventTypes = DictionaryFactory.mutableRefToRefDictionary();
    this.asyncExecutor = asyncExecutor;
  }

  @Override
  public <E extends Event<S>> void subscribe(TypeId<S, E> typeId, Consumer<E> consumer) {
    registerTypeId(typeId);
    consumers.unsafeGet(typeId.id()).add(consumer);
  }

  @Override
  public <E extends Event<S>> void unsubscribe(TypeId<S, E> typeId, Consumer<E> consumer) {
    if (typeId.id() < 0) {
      throw new IllegalArgumentException("Unexpected typeId:[%s]".formatted(typeId));
    }
    if (consumers.size() > typeId.id()) {
      consumers.unsafeGet(typeId.id()).remove(consumer);
    }
  }

  @Override
  public void send(Event<S> event) {
    UnsafeMutableArray<Consumer<?>> eventConsumers;
    try {
      eventConsumers = consumers.unsafeGet(event.typeId().id());
    } catch (ArrayIndexOutOfBoundsException e) {
      registerTypeId(event.typeId());
      send(event);
      return;
    }
    //noinspection unchecked we control it during registering
    for (var consumer : (Consumer<Event<S>>[]) eventConsumers.wrapped()) {
      consumer.accept(event);
    }
  }

  @Override
  public void sendInBackground(Event<S> event) {
    asyncExecutor.execute(() -> send(event));
  }
  
  private synchronized void registerTypeId(TypeId<S, ? extends Event<S>> typeId) {
    if (typeId.id() < 0) {
      throw new IllegalArgumentException("Unexpected typeId:[%s]".formatted(typeId));
    }
    Class<? extends Event<S>> eventType = typeId.eventType();
    TypeId<S, ? extends Event<S>> alreadyRegistered = knownEventTypes.get(eventType);
    if (alreadyRegistered != null && alreadyRegistered != typeId) {
      throw new IllegalStateException(
          "EventType:[%s] is already registered with typeId:[%s], but now received typeId:[%s]".formatted(eventType, alreadyRegistered, typeId));
    }
    // we need synchronize only on write because read is fully thread safe
    while (typeId.id() >= consumers.size()) {
      MutableArray<Consumer<?>> eventConsumers = ArrayFactory.copyOnModifyArray(Consumer.class);
      consumers.add(eventConsumers.asUnsafe());
    }
    knownEventTypes.put(eventType, typeId);
  }
}
