package com.ss.rlib.network.test;

import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.network.annotation.PacketDescription;
import com.ss.rlib.network.impl.DefaultConnection;
import com.ss.rlib.network.packet.IdBasedReadablePacket;
import com.ss.rlib.network.packet.impl.AbstractIdBasedReadablePacket;
import com.ss.rlib.network.packet.impl.DefaultReadablePacket;
import com.ss.rlib.network.packet.registry.impl.IdBasedReadablePacketRegistry;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class IdBasedReadablePacketRegistryTest {

    @NoArgsConstructor
    @PacketDescription(id = 1)
    public static class Impl1 extends DefaultReadablePacket {
    }

    @NoArgsConstructor
    @PacketDescription(id = 2)
    public static class Impl2 extends DefaultReadablePacket {
    }

    @NoArgsConstructor
    @PacketDescription(id = 3)
    public static class Impl3 extends DefaultReadablePacket {
    }

    @NoArgsConstructor
    private static class PrivateBase extends AbstractIdBasedReadablePacket<DefaultConnection, PrivateBase> {
    }

    @NoArgsConstructor
    @PacketDescription(id = 1)
    private static class PrivateImpl1 extends PrivateBase {
    }

    @NoArgsConstructor
    @PacketDescription(id = 10)
    private static class PrivateImpl2 extends PrivateBase {
    }

    @NoArgsConstructor
    public static class PublicBase extends AbstractIdBasedReadablePacket<DefaultConnection, PublicBase> {
    }

    @NoArgsConstructor
    @PacketDescription(id = 1)
    public static class PublicImpl1 extends PublicBase {
    }

    @NoArgsConstructor
    @PacketDescription(id = 5)
    public static class PublicImpl2 extends PublicBase {
    }

    @Test
    Object shouldBeCreated() {
        return new IdBasedReadablePacketRegistry<>(IdBasedReadablePacket.class);
    }

    @Test
    void shouldRegister3PacketsByArray() {

        var registry = new IdBasedReadablePacketRegistry<>(IdBasedReadablePacket.class)
            .register(ArrayFactory.asArray(Impl1.class, Impl2.class, Impl3.class));

        Assertions.assertTrue(registry.findById(1) instanceof Impl1);
        Assertions.assertTrue(registry.findById(2) instanceof Impl2);
        Assertions.assertTrue(registry.findById(3) instanceof Impl3);
    }

    @Test
    void shouldRegister3PacketsByVarargs() {

        var registry = new IdBasedReadablePacketRegistry<>(IdBasedReadablePacket.class)
            .register(Impl1.class, Impl2.class, Impl3.class);

        Assertions.assertTrue(registry.findById(1) instanceof Impl1);
        Assertions.assertTrue(registry.findById(2) instanceof Impl2);
        Assertions.assertTrue(registry.findById(3) instanceof Impl3);
    }

    @Test
    void shouldRegister3PacketsBySingle() {

        var registry = new IdBasedReadablePacketRegistry<>(IdBasedReadablePacket.class)
            .register(Impl1.class)
            .register(Impl2.class)
            .register(Impl3.class);

        Assertions.assertTrue(registry.findById(1) instanceof Impl1);
        Assertions.assertTrue(registry.findById(2) instanceof Impl2);
        Assertions.assertTrue(registry.findById(3) instanceof Impl3);
    }

    @Test
    void shouldRegister2PrivatePacketsBySingle() {

        var registry = new IdBasedReadablePacketRegistry<>(PrivateBase.class)
            .register(PrivateImpl1.class, PrivateImpl1::new)
            .register(PrivateImpl2.class, PrivateImpl2::new);

        Assertions.assertTrue(registry.findById(1) instanceof PrivateImpl1);
        Assertions.assertTrue(registry.findById(10) instanceof PrivateImpl2);
    }

    @Test
    void shouldNotAcceptWrongTypes() {

        var array = ArrayFactory.asArray(
            PrivateImpl1.class,
            PrivateImpl2.class,
            PublicImpl1.class,
            PublicImpl2.class
        );

        var registry = new IdBasedReadablePacketRegistry<>(PublicBase.class)
            .register(ClassUtils.<Array<Class<? extends PublicBase>>>unsafeNNCast(array));

        Assertions.assertTrue(registry.findById(1) instanceof PublicImpl1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> registry.findById(2));
        Assertions.assertTrue(registry.findById(5) instanceof PublicImpl2);
    }
}
