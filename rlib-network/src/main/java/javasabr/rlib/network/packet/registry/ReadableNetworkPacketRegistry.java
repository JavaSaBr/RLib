package javasabr.rlib.network.packet.registry;

import javasabr.rlib.classpath.ClassPathScanner;
import javasabr.rlib.classpath.ClassPathScannerFactory;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayCollectors;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.annotation.NetworkPacketDescription;
import javasabr.rlib.network.packet.IdBasedReadableNetworkPacket;
import javasabr.rlib.network.packet.registry.impl.IdBasedReadableNetworkPacketRegistry;

/**
 * Interface to implement a registry of readable network packets.
 *
 * @param <R> the readable packet type
 * @param <C> the connection type
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface ReadableNetworkPacketRegistry<R extends IdBasedReadableNetworkPacket<C>, C extends Connection<C>> {

  /**
   * Creates a new empty readable packet registry.
   *
   * @return an empty registry
   * @since 10.0.0
   */
  static ReadableNetworkPacketRegistry<?, ?> empty() {
    return new IdBasedReadableNetworkPacketRegistry<>(IdBasedReadableNetworkPacket.class);
  }

  /**
   * Creates a new empty readable packet registry with the specified type.
   *
   * @param <R> the readable packet type
   * @param <C> the connection type
   * @param type the packet type class
   * @return an empty registry
   * @since 10.0.0
   */
  static <
      R extends IdBasedReadableNetworkPacket<C>,
      C extends Connection<C>> ReadableNetworkPacketRegistry<R, C> empty(Class<R> type) {
    return new IdBasedReadableNetworkPacketRegistry<>(type);
  }

  /**
   * Creates a new classpath scanning based readable packet registry.
   *
   * @param <R> the readable packet type
   * @param <C> the connection type
   * @param baseType the base packet type class
   * @return a registry populated with discovered packets
   * @since 10.0.0
   */
  static <
      R extends IdBasedReadableNetworkPacket<C>,
      C extends Connection<C>> ReadableNetworkPacketRegistry<R, C> classPathBased(Class<R> baseType) {
    var scanner = ClassPathScannerFactory.newDefaultScanner();
    scanner.useSystemClassPath(true);
    scanner.scan();
    return scannerBased(baseType, scanner);
  }

  /**
   * Creates a new classpath scanning based readable packet registry by scanning the main class.
   *
   * @param <R> the readable packet type
   * @param <C> the connection type
   * @param baseType the base packet type class
   * @param mainClass the main class to scan from
   * @return a registry populated with discovered packets
   * @since 10.0.0
   */
  static <
      R extends IdBasedReadableNetworkPacket<C>,
      C extends Connection<C>> ReadableNetworkPacketRegistry<R, C> classPathBased(
          Class<R> baseType,
          Class<?> mainClass) {
    var scanner = ClassPathScannerFactory.newManifestScanner(mainClass);
    scanner.useSystemClassPath(false);
    scanner.scan();
    return scannerBased(baseType, scanner);
  }

  /**
   * Creates a new readable packet registry from a classpath scanner.
   *
   * @param <R> the readable packet type
   * @param <C> the connection type
   * @param baseType the base packet type class
   * @param scanner the classpath scanner
   * @return a registry populated with discovered packets
   * @since 10.0.0
   */
  static <
      R extends IdBasedReadableNetworkPacket<C>,
      C extends Connection<C>> ReadableNetworkPacketRegistry<R, C> scannerBased(
          Class<R> baseType,
          ClassPathScanner scanner) {
    Array<Class<? extends R>> result = scanner
        .findImplementations(IdBasedReadableNetworkPacket.class)
        .stream()
        .filter(type -> type.getAnnotation(NetworkPacketDescription.class) != null)
        .map(ClassUtils::<Class<R>>unsafeNNCast)
        .collect(ArrayCollectors.toArray(Class.class));
    return new IdBasedReadableNetworkPacketRegistry<>(baseType)
        .register(result);
  }

  /**
   * Creates a new readable packet registry from varargs classes.
   *
   * @param <R> the readable packet type
   * @param <C> the connection type
   * @param type the packet type class
   * @param classes the packet classes to register
   * @return a registry with registered packets
   * @since 10.0.0
   */
  @SafeVarargs
  static <
      R extends IdBasedReadableNetworkPacket<C>,
      C extends Connection<C>> ReadableNetworkPacketRegistry<R, C> of(
      Class<R> type,
      Class<? extends R>... classes) {
    return new IdBasedReadableNetworkPacketRegistry<>(type)
        .register(classes, classes.length);
  }

  /**
   * Creates a new readable packet registry from varargs classes with explicit connection type.
   *
   * @param <R> the readable packet type
   * @param <C> the connection type
   * @param type the packet type class
   * @param connectionType the connection type class
   * @param classes the packet classes to register
   * @return a registry with registered packets
   * @since 10.0.0
   */
  @SafeVarargs
  static <
      R extends IdBasedReadableNetworkPacket<C>,
      C extends Connection<C>> ReadableNetworkPacketRegistry<R, C> of(
      Class<?> type,
      Class<C> connectionType,
      Class<? extends R>... classes) {
    return new IdBasedReadableNetworkPacketRegistry<>(ClassUtils.<Class<R>>unsafeCast(type))
        .register(classes, classes.length);
  }

  /**
   * Creates a new readable packet registry from an array of classes.
   *
   * @param <R> the readable packet type
   * @param <C> the connection type
   * @param type the packet type class
   * @param classes the array of packet classes to register
   * @return a registry with registered packets
   * @since 10.0.0
   */
  static <
      R extends IdBasedReadableNetworkPacket<C>,
      C extends Connection<C>> ReadableNetworkPacketRegistry<R, C> of(
      Class<R> type,
      Array<Class<? extends R>> classes) {
    return new IdBasedReadableNetworkPacketRegistry<>(type)
        .register(classes);
  }

  /**
   * Resolves a network packet prototype based on packet ID.
   *
   * @param id the network packet ID
   * @return the resolved prototype
   * @throws IllegalArgumentException if no prototype found for the ID
   * @since 10.0.0
   */
  R resolvePrototypeById(int id);
}
