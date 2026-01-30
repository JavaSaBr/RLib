package javasabr.rlib.network;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javasabr.rlib.collections.array.Array;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.network.annotation.NetworkPacketDescription;
import javasabr.rlib.network.impl.DefaultConnection;
import javasabr.rlib.network.packet.IdBasedReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.AbstractIdBasedReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.DefaultReadableNetworkPacket;
import javasabr.rlib.network.packet.registry.impl.IdBasedReadableNetworkPacketRegistry;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class IdBasedReadableNetworkPacketRegistryTest {

  @NoArgsConstructor
  @NetworkPacketDescription(id = 1)
  public static class Impl1 extends DefaultReadableNetworkPacket {}

  @NoArgsConstructor
  @NetworkPacketDescription(id = 2)
  public static class Impl2 extends DefaultReadableNetworkPacket {}

  @NoArgsConstructor
  @NetworkPacketDescription(id = 3)
  public static class Impl3 extends DefaultReadableNetworkPacket {}

  @NoArgsConstructor
  private static class PrivateBase extends AbstractIdBasedReadableNetworkPacket<DefaultConnection> {}

  @NoArgsConstructor
  @NetworkPacketDescription(id = 1)
  private static class PrivateImpl1 extends PrivateBase {}

  @NoArgsConstructor
  @NetworkPacketDescription(id = 10)
  private static class PrivateImpl2 extends PrivateBase {}

  @NoArgsConstructor
  public static class PublicBase extends AbstractIdBasedReadableNetworkPacket<DefaultConnection> {}

  @NoArgsConstructor
  @NetworkPacketDescription(id = 1)
  public static class PublicImpl1 extends PublicBase {}

  @NoArgsConstructor
  @NetworkPacketDescription(id = 5)
  public static class PublicImpl2 extends PublicBase {}

  @Test
  void shouldBeCreated() {
    assertThatCode(
        () -> new IdBasedReadableNetworkPacketRegistry<>(IdBasedReadableNetworkPacket.class))
        .doesNotThrowAnyException();
  }

  @Test
  void shouldRegister3PacketsByArray() {
    var registry = new IdBasedReadableNetworkPacketRegistry<>(IdBasedReadableNetworkPacket.class)
        .register(Array.typed(Class.class, Impl1.class, Impl2.class, Impl3.class));

    assertThat(registry.resolvePrototypeById(1))
        .isInstanceOf(Impl1.class);
    assertThat(registry.resolvePrototypeById(2))
        .isInstanceOf(Impl2.class);
    assertThat(registry.resolvePrototypeById(3))
        .isInstanceOf(Impl3.class);
  }

  @Test
  void shouldRegister3PacketsByVarargs() {
    var registry = new IdBasedReadableNetworkPacketRegistry<>(IdBasedReadableNetworkPacket.class)
        .register(Impl1.class, Impl2.class, Impl3.class);

    assertThat(registry.resolvePrototypeById(1))
        .isInstanceOf(Impl1.class);
    assertThat(registry.resolvePrototypeById(2))
        .isInstanceOf(Impl2.class);
    assertThat(registry.resolvePrototypeById(3))
        .isInstanceOf(Impl3.class);
  }

  @Test
  void shouldRegister3PacketsBySingle() {
    var registry = new IdBasedReadableNetworkPacketRegistry<>(IdBasedReadableNetworkPacket.class)
        .register(Impl1.class)
        .register(Impl2.class)
        .register(Impl3.class);

    assertThat(registry.resolvePrototypeById(1))
        .isInstanceOf(Impl1.class);
    assertThat(registry.resolvePrototypeById(2))
        .isInstanceOf(Impl2.class);
    assertThat(registry.resolvePrototypeById(3))
        .isInstanceOf(Impl3.class);
  }

  @Test
  void shouldRegister2PrivatePacketsBySingle() {
    var registry = new IdBasedReadableNetworkPacketRegistry<>(PrivateBase.class)
        .register(PrivateImpl1.class, PrivateImpl1::new)
        .register(PrivateImpl2.class, PrivateImpl2::new);

    assertThat(registry.resolvePrototypeById(1))
        .isInstanceOf(PrivateImpl1.class);
    assertThat(registry.resolvePrototypeById(10))
        .isInstanceOf(PrivateImpl2.class);
  }

  @Test
  void shouldNotAcceptWrongTypes() {

    var array = Array.typed(
        Class.class,
        PrivateImpl1.class,
        PrivateImpl2.class,
        PublicImpl1.class,
        PublicImpl2.class);

    var registry = new IdBasedReadableNetworkPacketRegistry<>(PublicBase.class)
        .register(ClassUtils.<Array<Class<? extends PublicBase>>>unsafeNNCast(array));

    assertThat(registry.resolvePrototypeById(1))
        .isInstanceOf(PublicImpl1.class);
    assertThatThrownBy(() -> registry.resolvePrototypeById(2))
        .isInstanceOf(IllegalArgumentException.class);
    assertThat(registry.resolvePrototypeById(5))
        .isInstanceOf(PublicImpl2.class);
  }
}
