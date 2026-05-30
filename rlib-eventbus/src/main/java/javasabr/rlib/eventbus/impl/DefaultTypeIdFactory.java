package javasabr.rlib.eventbus.impl;

import java.util.concurrent.atomic.AtomicInteger;
import javasabr.rlib.collections.dictionary.DictionaryFactory;
import javasabr.rlib.collections.dictionary.MutableRefToRefDictionary;
import javasabr.rlib.eventbus.EventBus.Event;
import javasabr.rlib.eventbus.EventBus.TypeId;
import javasabr.rlib.eventbus.EventBus.TypeIdFactory;
import javasabr.rlib.eventbus.EventBus.TypeIdSet;

public class DefaultTypeIdFactory {

  public static final DefaultTypeIdFactory INSTANCE = new DefaultTypeIdFactory();

  MutableRefToRefDictionary<Class<? extends TypeIdSet>, TypedTypeIdFactory<?>> knownFactories;

  private DefaultTypeIdFactory() {
    this.knownFactories = DictionaryFactory.mutableRefToRefDictionary();
  }

  public synchronized <S extends TypeIdSet> TypeIdFactory<S> typedTypeIdFactory(Class<S> typeIdSetType) {
    //noinspection unchecked
    return (TypeIdFactory<S>) knownFactories.getOrCompute(typeIdSetType, TypedTypeIdFactory::new);
  }

  private static class TypedTypeIdFactory<S extends TypeIdSet> implements TypeIdFactory<S> {

    MutableRefToRefDictionary<Class<?>, TypeIdImpl<?, ?>> knownTypes;
    AtomicInteger typeIdFactory;

    private TypedTypeIdFactory() {
      this.knownTypes = DictionaryFactory.mutableRefToRefDictionary();
      this.typeIdFactory = new AtomicInteger(0);
    }

    @Override
    public synchronized <E extends Event<S>> TypeId<S, E> typeIdOf(Class<E> eventType) {
      TypeIdImpl<?, ?> exist = knownTypes.get(eventType);
      if (exist != null) {
        //noinspection unchecked it's checked during creation
        return (TypeId<S, E>) exist;
      }
      var newTypeId = new TypeIdImpl<>(eventType, typeIdFactory.incrementAndGet());
      knownTypes.put(eventType, newTypeId);
      return newTypeId;
    }
  }

  private record TypeIdImpl<S extends TypeIdSet, E extends Event<S>>(
      Class<E> eventType,
      int id) implements TypeId<S, E> {}
}
