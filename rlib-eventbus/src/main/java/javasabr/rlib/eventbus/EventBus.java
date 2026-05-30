package javasabr.rlib.eventbus;

import java.util.function.Consumer;

/**
 * Provides a typed event bus for subscribing and publishing events by {@link TypeId}.
 * Implementations are expected to support concurrent subscribe, unsubscribe, and send operations.
 *
 * @param <S> the type of type-id set
 * @since 10.0.0
 */
public interface EventBus<S extends EventBus.TypeIdSet> {

  /**
   * Subscribes a consumer to events identified by the provided type id.
   *
   * @param typeId the event type id
   * @param consumer the consumer of events
   * @param <E> the type of event
   * @since 10.0.0
   */
  <E extends Event<S>> void subscribe(TypeId<S, E> typeId, Consumer<E> consumer);

  /**
   * Unsubscribes a consumer from events identified by the provided type id.
   *
   * @param typeId the event type id
   * @param consumer the consumer of events
   * @param <E> the type of event
   * @since 10.0.0
   */
  <E extends Event<S>> void unsubscribe(TypeId<S, E> typeId, Consumer<E> consumer);

  /**
   * Sends an event to all subscribed consumers in the current thread.
   * This method does not enqueue work and returns only after dispatch is finished.
   * Consumer exceptions are propagated to the caller.
   *
   * @param event the event to send
   * @since 10.0.0
   */
  void send(Event<S> event);

  /**
   * Schedules sending an event on the background executor.
   * This method returns immediately after scheduling.
   * Delivery order between different background sends is not guaranteed and depends on the executor policy.
   * Consumer exceptions are handled on the background thread.
   *
   * @param event the event to send
   * @since 10.0.0
   */
  void sendInBackground(Event<S> event);

  /**
   * Marks a type-id namespace used to isolate event buses by event family.
   *
   * @since 10.0.0
   */
  interface TypeIdSet {
  }

  /**
   * Creates stable {@link TypeId} instances for event types within one type-id namespace.
   *
   * @param <S> the type of type-id set
   * @since 10.0.0
   */
  interface TypeIdFactory<S extends TypeIdSet> {

    /**
     * Returns a type id for the specified event type.
     *
     * @param eventType the event class
     * @param <E> the type of event
     * @return the type id of the event class
     * @since 10.0.0
     */
    <E extends Event<S>> TypeId<S, E> typeIdOf(Class<E> eventType);
  }

  /**
   * Represents an identity of one event type within a {@link TypeIdSet}.
   *
   * @param <S> the type of type-id set
   * @param <E> the type of event
   * @since 10.0.0
   */
  interface TypeId<S extends TypeIdSet, E extends Event<S>> {

    /**
     * Returns the event class associated with this type id.
     *
     * @return the event class
     * @since 10.0.0
     */
    Class<E> eventType();

    /**
     * Returns the numeric id used for fast lookup in event bus internals.
     *
     * @return the numeric event type id
     * @since 10.0.0
     */
    int id();
  }

  /**
   * Represents an event published to an {@link EventBus}.
   *
   * @param <S> the type of type-id set
   * @since 10.0.0
   */
  interface Event<S extends TypeIdSet> {

    /**
     * Returns the type id of this event.
     *
     * @return the type id of this event
     * @since 10.0.0
     */
    TypeId<S, ? extends Event<S>> typeId();
  }
}
