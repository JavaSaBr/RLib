package javasabr.rlib.eventbus;

import java.util.function.Consumer;

public interface EventBus<S extends EventBus.TypeIdSet> {
  
  <E extends Event<S>> void registerConsumer(TypeId<S, E> typeId, Consumer<E> consumer);
  
  <E extends Event<S>> void unregisterConsumer(TypeId<S, E> typeId, Consumer<E> consumer);
  
  void sendSync(Event<S> event);
  
  void sendAsync(Event<S> event);
  
  interface TypeIdSet {
  }
  
  interface TypeIdFactory<S extends TypeIdSet> {
    
    <E extends Event<S>> TypeId<S, E> createFor(Class<E> eventType);
  }
  
  interface TypeId<S extends TypeIdSet, E extends Event<S>> {
    
    Class<E> eventType();
    int id();
  }
  
  interface Event<S extends TypeIdSet> {

    TypeId<S, ? extends Event<S>> typeId();
  }
}
