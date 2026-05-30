package javasabr.rlib.eventbus;

import javasabr.rlib.eventbus.EventBus.TypeIdFactory;
import javasabr.rlib.eventbus.EventBus.TypeIdSet;
import javasabr.rlib.eventbus.impl.DefaultEventBus;
import javasabr.rlib.eventbus.impl.DefaultTypeIdFactory;

public class EventBusFactory {

  public static <S extends TypeIdSet> TypeIdFactory<S> createTypeIdFactory(Class<S> typeIdSetType) {
    return DefaultTypeIdFactory.INSTANCE.typedTypeIdFactory(typeIdSetType);
  }

  public static <S extends TypeIdSet> EventBus<S> eventBus(@SuppressWarnings("unused") TypeIdFactory<S> typeIdFactory) {
    return new DefaultEventBus<>();
  }
}
