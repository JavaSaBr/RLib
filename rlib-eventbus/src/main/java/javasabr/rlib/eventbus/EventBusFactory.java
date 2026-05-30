package javasabr.rlib.eventbus;

import javasabr.rlib.eventbus.EventBus.TypeIdFactory;
import javasabr.rlib.eventbus.EventBus.TypeIdSet;
import javasabr.rlib.eventbus.impl.DefaultEventBus;
import javasabr.rlib.eventbus.impl.DefaultTypeIdFactory;

/**
 * Provides factory methods for creating event bus API components.
 *
 * @since 10.0.0
 */
public class EventBusFactory {

  /**
   * Creates a type-id factory for the provided type-id namespace.
   *
   * @param typeIdSetType the type-id namespace class
   * @param <S> the type of type-id set
   * @return a type-id factory for the namespace
   * @since 10.0.0
   */
  public static <S extends TypeIdSet> TypeIdFactory<S> createTypeIdFactory(Class<S> typeIdSetType) {
    return DefaultTypeIdFactory.INSTANCE.typedTypeIdFactory(typeIdSetType);
  }

  /**
   * Creates an event bus instance.
   * The default implementation uses a shared background executor for {@link EventBus#sendInBackground(EventBus.Event)}.
   *
   * @param typeIdFactory the type-id factory associated with this bus API
   * @param <S> the type of type-id set
   * @return a new event bus
   * @since 10.0.0
   */
  public static <S extends TypeIdSet> EventBus<S> createEventBus(@SuppressWarnings("unused") TypeIdFactory<S> typeIdFactory) {
    return new DefaultEventBus<>();
  }
}
