package javasabr.rlib.eventbus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.eventbus.EventBus.TypeId;
import javasabr.rlib.eventbus.EventBus.TypeIdFactory;
import org.junit.jupiter.api.Test;

public class DefaultEventBusTest {
  
  public static class TestTypeIdSet implements EventBus.TypeIdSet {
  }
  public static class Test2TypeIdSet implements EventBus.TypeIdSet {
  }
  
  static final TypeIdFactory<TestTypeIdSet> typeIdFactory = EventBusFactory.createTypeIdFactory(TestTypeIdSet.class);
  static final TypeIdFactory<Test2TypeIdSet> typeIdFactory2 = EventBusFactory.createTypeIdFactory(Test2TypeIdSet.class);
  
  public record EventA(String value) implements EventBus.Event<TestTypeIdSet> {
    
    static final TypeId<TestTypeIdSet, EventA> TYPE_ID = typeIdFactory.typeIdOf(EventA.class);
    
    @Override
    public TypeId<TestTypeIdSet, EventA> typeId() {
      return TYPE_ID;
    }
  }

  public record EventB(String value) implements EventBus.Event<TestTypeIdSet> {
    
    static final TypeId<TestTypeIdSet, EventB> TYPE_ID = typeIdFactory.typeIdOf(EventB.class);

    @Override
    public TypeId<TestTypeIdSet, EventB> typeId() {
      return TYPE_ID;
    }
  }

  public record EventC(String value) implements EventBus.Event<TestTypeIdSet> {
    
    static final TypeId<TestTypeIdSet, EventC> TYPE_ID = typeIdFactory.typeIdOf(EventC.class);

    @Override
    public TypeId<TestTypeIdSet, EventC> typeId() {
      return TYPE_ID;
    }
  }

  public record EventD(String value) implements EventBus.Event<Test2TypeIdSet> {

    static final TypeId<Test2TypeIdSet, EventD> TYPE_ID = typeIdFactory2.typeIdOf(EventD.class);

    @Override
    public TypeId<Test2TypeIdSet, EventD> typeId() {
      return TYPE_ID;
    }
  }

  private record CustomTypeId<E extends EventBus.Event<TestTypeIdSet>>(
      Class<E> eventType,
      int id) implements TypeId<TestTypeIdSet, E> {
  }

  public record EventWithNegativeTypeId(String value) implements EventBus.Event<TestTypeIdSet> {

    static final TypeId<TestTypeIdSet, EventWithNegativeTypeId> TYPE_ID =
        new CustomTypeId<>(EventWithNegativeTypeId.class, -1);

    @Override
    public TypeId<TestTypeIdSet, EventWithNegativeTypeId> typeId() {
      return TYPE_ID;
    }
  }

  public record EventWithOutOfRangeTypeId(String value) implements EventBus.Event<TestTypeIdSet> {

    static final TypeId<TestTypeIdSet, EventWithOutOfRangeTypeId> TYPE_ID =
        new CustomTypeId<>(EventWithOutOfRangeTypeId.class, 100);

    @Override
    public TypeId<TestTypeIdSet, EventWithOutOfRangeTypeId> typeId() {
      return TYPE_ID;
    }
  }
  
  @Test
  void shouldCorrectlyDeliverEvents() {
    // given:
    var eventBus = EventBusFactory.createEventBus(typeIdFactory);

    MutableArray<EventA> receivedEventsA1 = ArrayFactory.mutableArray(EventA.class);
    MutableArray<EventA> receivedEventsA2 = ArrayFactory.mutableArray(EventA.class);
    MutableArray<EventB> receivedEventsB1 = ArrayFactory.mutableArray(EventB.class);
    MutableArray<EventC> receivedEventsC1 = ArrayFactory.mutableArray(EventC.class);
    MutableArray<EventC> receivedEventsC2 = ArrayFactory.mutableArray(EventC.class);
    MutableArray<EventC> receivedEventsC3 = ArrayFactory.mutableArray(EventC.class);

    eventBus.subscribe(EventA.TYPE_ID, receivedEventsA1::add);
    eventBus.subscribe(EventA.TYPE_ID, eventA -> {
      receivedEventsA1.add(eventA);
      receivedEventsA2.add(eventA);
    });
    eventBus.subscribe(EventB.TYPE_ID, receivedEventsB1::add);
    eventBus.subscribe(EventC.TYPE_ID, receivedEventsC1::add);
    eventBus.subscribe(EventC.TYPE_ID, receivedEventsC2::add);
    eventBus.subscribe(EventC.TYPE_ID, receivedEventsC3::add);

    // when:
    eventBus.send(new EventA("event_a_1"));
    eventBus.send(new EventA("event_a_2"));
    eventBus.send(new EventB("event_b_1"));
    eventBus.send(new EventC("event_c_1"));
    eventBus.send(new EventC("event_c_2"));
    eventBus.send(new EventC("event_c_3"));
    
    // then:
    assertThat(receivedEventsA1).containsExactly(
        new EventA("event_a_1"),
        new EventA("event_a_1"),
        new EventA("event_a_2"),
        new EventA("event_a_2"));
    assertThat(receivedEventsA2).containsExactly(
        new EventA("event_a_1"),
        new EventA("event_a_2"));
    assertThat(receivedEventsB1).containsExactly(
        new EventB("event_b_1"));
    assertThat(receivedEventsC1).containsExactly(
        new EventC("event_c_1"),
        new EventC("event_c_2"),
        new EventC("event_c_3"));
    assertThat(receivedEventsC2).containsExactly(
        new EventC("event_c_1"),
        new EventC("event_c_2"),
        new EventC("event_c_3"));
    assertThat(receivedEventsC3).containsExactly(
        new EventC("event_c_1"),
        new EventC("event_c_2"),
        new EventC("event_c_3"));
  }

  @Test
  void shouldThrowExceptionWhenSendingEventWithNegativeTypeId() {
    // given:
    var eventBus = EventBusFactory.createEventBus(typeIdFactory);

    // when/then:
    assertThatThrownBy(() -> eventBus.send(new EventWithNegativeTypeId("event")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Unexpected typeId");
  }

  @Test
  void shouldThrowExceptionWhenUnregisteringConsumerWithNegativeTypeId() {
    // given:
    var eventBus = EventBusFactory.createEventBus(typeIdFactory);

    // when/then:
    assertThatThrownBy(
            () -> eventBus.unsubscribe(EventWithNegativeTypeId.TYPE_ID, event -> {}))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Unexpected typeId");
  }

  @Test
  void shouldIgnoreUnregisterForOutOfRangeTypeId() {
    // given:
    var eventBus = EventBusFactory.createEventBus(typeIdFactory);
    MutableArray<EventA> receivedEventsA = ArrayFactory.mutableArray(EventA.class);
    eventBus.subscribe(EventA.TYPE_ID, receivedEventsA::add);

    // when:
    eventBus.unsubscribe(EventWithOutOfRangeTypeId.TYPE_ID, event -> {});
    eventBus.send(new EventA("event_a_1"));

    // then:
    assertThat(receivedEventsA).containsExactly(new EventA("event_a_1"));
  }

  @Test
  void shouldNotFailWhenSendingEventWithOutOfRangeTypeId() {
    // given:
    var eventBus = EventBusFactory.createEventBus(typeIdFactory);

    // when/then:
    assertThatCode(() -> eventBus.send(new EventWithOutOfRangeTypeId("event")))
        .doesNotThrowAnyException();
  }

  @Test
  void shouldUnsubscribe() {
    // given:
    var eventBus = EventBusFactory.createEventBus(typeIdFactory);
    MutableArray<EventA> receivedEventsA = ArrayFactory.mutableArray(EventA.class);
    Consumer<EventA> consumer = receivedEventsA::add;
    eventBus.subscribe(EventA.TYPE_ID, consumer);

    // when:
    eventBus.unsubscribe(EventA.TYPE_ID, consumer);
    eventBus.send(new EventA("event_a_1"));

    // then:
    assertThat(receivedEventsA).isEmpty();
  }

  @Test
  void shouldIgnoreDuplicateUnregister() {
    // given:
    var eventBus = EventBusFactory.createEventBus(typeIdFactory);
    MutableArray<EventA> firstConsumerEvents = ArrayFactory.mutableArray(EventA.class);
    MutableArray<EventA> secondConsumerEvents = ArrayFactory.mutableArray(EventA.class);
    Consumer<EventA> firstConsumer = firstConsumerEvents::add;
    Consumer<EventA> secondConsumer = secondConsumerEvents::add;
    eventBus.subscribe(EventA.TYPE_ID, firstConsumer);
    eventBus.subscribe(EventA.TYPE_ID, secondConsumer);

    // when:
    eventBus.unsubscribe(EventA.TYPE_ID, firstConsumer);
    eventBus.unsubscribe(EventA.TYPE_ID, firstConsumer);
    eventBus.send(new EventA("event_a_1"));

    // then:
    assertThat(firstConsumerEvents).isEmpty();
    assertThat(secondConsumerEvents).containsExactly(new EventA("event_a_1"));
  }

  @Test
  void shouldThrowExceptionForConflictingTypeIdOfSameEventType() {
    // given:
    var eventBus = EventBusFactory.createEventBus(typeIdFactory);
    TypeId<TestTypeIdSet, EventA> conflictingTypeId = new CustomTypeId<>(EventA.class, 1001);
    eventBus.subscribe(EventA.TYPE_ID, event -> {});

    // when/then:
    assertThatThrownBy(() -> eventBus.subscribe(conflictingTypeId, event -> {}))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("already registered");
  }

  @Test
  void shouldDeliverEventsAsync() throws Exception {
    // given:
    var eventBus = EventBusFactory.createEventBus(typeIdFactory);
    MutableArray<EventA> receivedEventsA = ArrayFactory.mutableArray(EventA.class);
    CountDownLatch latch = new CountDownLatch(1);
    eventBus.subscribe(EventA.TYPE_ID, event -> {
      receivedEventsA.add(event);
      latch.countDown();
    });

    // when:
    eventBus.sendInBackground(new EventA("event_a_1"));

    // then:
    assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();
    assertThat(receivedEventsA).containsExactly(new EventA("event_a_1"));
  }

  @Test
  void shouldDeliverEventsForSparseTypeId() {
    // given:
    var eventBus = EventBusFactory.createEventBus(typeIdFactory);
    MutableArray<EventWithOutOfRangeTypeId> receivedEvents = ArrayFactory.mutableArray(EventWithOutOfRangeTypeId.class);
    eventBus.subscribe(EventWithOutOfRangeTypeId.TYPE_ID, receivedEvents::add);

    // when:
    eventBus.send(new EventWithOutOfRangeTypeId("event"));

    // then:
    assertThat(receivedEvents).containsExactly(new EventWithOutOfRangeTypeId("event"));
  }
}
