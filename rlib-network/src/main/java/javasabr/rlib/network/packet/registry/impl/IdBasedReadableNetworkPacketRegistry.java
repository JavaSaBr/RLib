package javasabr.rlib.network.packet.registry.impl;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.common.util.ArrayUtils;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.common.util.ObjectUtils;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.annotation.NetworkPacketDescription;
import javasabr.rlib.network.packet.IdBasedReadableNetworkPacket;
import javasabr.rlib.network.packet.registry.ReadableNetworkPacketRegistry;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * Id based implementation of readable network packet's registry.
 *
 * @author JavaSaBr
 */
@CustomLog
@Accessors(fluent = true, chain = false)
@FieldDefaults(level = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class IdBasedReadableNetworkPacketRegistry<
    R extends IdBasedReadableNetworkPacket<C>,
    C extends Connection<C>> implements ReadableNetworkPacketRegistry<R, C> {

  @Getter(AccessLevel.PROTECTED)
  final Class<? extends R> type;

  @Getter(AccessLevel.PROTECTED)
  final @Nullable R[] idToPrototype;

  public IdBasedReadableNetworkPacketRegistry(Class<? extends R> type) {
    this.idToPrototype = ArrayUtils.create(type, 0);
    this.type = type;
  }

  /**
   * Registers classes of readable network packets.
   *
   * @throws IllegalArgumentException if found a class without description annotation or if found duplication by id.
   */
  public IdBasedReadableNetworkPacketRegistry<R, C> register(Array<Class<? extends R>> classes) {
    return register(classes.toArray(), classes.size());
  }

  /**
   * Registers classes of readable network packets.
   *
   * @throws IllegalArgumentException if found a class without description annotation or if found duplication by id.
   * id.
   */
  @SafeVarargs
  public final IdBasedReadableNetworkPacketRegistry<R, C> register(Class<? extends R>... classes) {
    return register(classes, classes.length);
  }

  /**
   * Registers classes of readable network packets.
   *
   * @throws IllegalArgumentException if found a class without description annotation or if found duplication by id.
   */
  public IdBasedReadableNetworkPacketRegistry<R, C> register(Class<? extends R>[] classes, int length) {

    Optional<Class<? extends R>> incorrectClass = Arrays
        .stream(classes, 0, length)
        .filter(type -> type.getAnnotation(NetworkPacketDescription.class) == null)
        .findFirst();

    if (incorrectClass.isPresent()) {
      throw new IllegalArgumentException("Have found class:[%s] without description annotation".formatted(incorrectClass.get()));
    }

    int maxId = Arrays
        .stream(classes, 0, length)
        .map(type -> type.getAnnotation(NetworkPacketDescription.class))
        .mapToInt(NetworkPacketDescription::id)
        .max()
        .orElseThrow(() -> new IllegalStateException("Not found any packet id"));

    @Nullable R[] idToPrototype = ArrayUtils.create(type, maxId + 1);

    for (int i = 0; i < length; i++) {
      Class<? extends R> cs = classes[i];
      if (!type.isAssignableFrom(cs)) {
        log.warn(cs, type, "Found incompatibility packet's type:[%s] with type:[%s]"::formatted);
        continue;
      }

      var description = cs.getAnnotation(NetworkPacketDescription.class);
      var id = description.id();

      R exist = idToPrototype[id];
      if (exist != null) {
        throw new IllegalArgumentException("Found duplication by id:[%d], existed packet is:[%s], new packet is:[%s]"
            .formatted(id, exist.getClass(), cs));
      }

      idToPrototype[id] = ClassUtils.newInstance(cs);
    }

    return new IdBasedReadableNetworkPacketRegistry<>(type, idToPrototype);
  }

  /**
   * Registers a class of readable network packet.
   *
   * @throws IllegalArgumentException if this class doesn't have {@link NetworkPacketDescription}, wrong id or some class is
   * already presented with the same id.
   */
  public IdBasedReadableNetworkPacketRegistry<R, C> register(Class<? extends R> cs) {
    return register(cs, () -> ClassUtils.newInstance(cs));
  }

  /**
   * Registers a class of readable packet.
   *
   * @return the reference to this registry.
   * @throws IllegalArgumentException if this class doesn't have {@link NetworkPacketDescription}, wrong id or some class is
   * already presented with the same id.
   */
  public <P extends R> IdBasedReadableNetworkPacketRegistry<R, C> register(Class<P> cs, Supplier<P> factory) {

    var description = cs.getAnnotation(NetworkPacketDescription.class);
    if (description == null) {
      throw new IllegalArgumentException("Class:[%s] doesn't have description annotation.".formatted(cs));
    }

    var id = description.id();
    if (id < 0) {
      throw new IllegalArgumentException("Class:[%s] has unexpected packet id:[%d]".formatted(cs, id));
    }

    @Nullable R[] idToPrototype = idToPrototype();

    if (id < idToPrototype.length) {
      R exist = idToPrototype[id];
      if (exist != null) {
        throw new IllegalArgumentException("Class:[%s] is already has the same id:[%d]"
            .formatted(exist.getClass(), id));
      } else {
        idToPrototype = ArrayUtils.copyOf(idToPrototype);
        idToPrototype[id] = ClassUtils.newInstance(cs);
        return new IdBasedReadableNetworkPacketRegistry<>(type, idToPrototype);
      }
    }

    idToPrototype = Arrays.copyOf(idToPrototype, id + 1);
    idToPrototype[id] = factory.get();

    return new IdBasedReadableNetworkPacketRegistry<>(type, idToPrototype);
  }

  @Override
  public R resolvePrototypeById(int id) {
    try {
      return ObjectUtils.notNull(idToPrototype[id], id,
          value -> new IllegalArgumentException("Not found a packet for the id " + value));
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Not found prototype for the id " + id);
    }
  }
}
