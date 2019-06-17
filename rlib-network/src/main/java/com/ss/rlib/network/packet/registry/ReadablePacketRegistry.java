package com.ss.rlib.network.packet.registry;

import com.ss.rlib.common.classpath.ClassPathScanner;
import com.ss.rlib.common.classpath.ClassPathScannerFactory;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayCollectors;
import com.ss.rlib.network.annotation.PacketDescription;
import com.ss.rlib.network.packet.IdBasedReadablePacket;
import com.ss.rlib.network.packet.registry.impl.IdBasedReadablePacketRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a registry of readable packets.
 *
 * @author JavaSaBr
 */
public interface ReadablePacketRegistry<R extends IdBasedReadablePacket> {

    /**
     * Create a new empty readable packet registry.
     *
     * @return the new packet registry.
     */
    static @NotNull ReadablePacketRegistry<IdBasedReadablePacket> empty() {
        return new IdBasedReadablePacketRegistry<>(IdBasedReadablePacket.class);
    }

    /**
     * Create a new empty readable packet registry.
     *
     * @param type the packet's type.
     * @return the new packet registry.
     */
    static <T extends IdBasedReadablePacket> @NotNull ReadablePacketRegistry<T> empty(@NotNull Class<T> type) {
        return new IdBasedReadablePacketRegistry<>(type);
    }

    /**
     * Create a new default readable packet registry.
     *
     * @return the new packet registry.
     */
    static @NotNull ReadablePacketRegistry<IdBasedReadablePacket> newDefault() {

        var scanner = ClassPathScannerFactory.newDefaultScanner();
        scanner.setUseSystemClasspath(true);
        scanner.scan();

        return of(scanner);
    }

    /**
     * Create a new default readable packet registry by the result of scanning classpath of the main class.
     *
     * @param mainClass the main class of application.
     * @return the new packet registry.
     */
    static @NotNull ReadablePacketRegistry<IdBasedReadablePacket> newDefault(@NotNull Class<?> mainClass) {

        var scanner = ClassPathScannerFactory.newManifestScanner(mainClass);
        scanner.setUseSystemClasspath(false);
        scanner.scan();

        return of(scanner);
    }

    /**
     * Creates a new default readable packet registry by the classpath scanner.
     *
     * @param scanner the classpath scanner.
     * @return the new packet registry.
     */
    static @NotNull ReadablePacketRegistry<IdBasedReadablePacket> of(@NotNull ClassPathScanner scanner) {

        var result = scanner.findImplements(IdBasedReadablePacket.class)
            .stream()
            .filter(type -> type.getAnnotation(PacketDescription.class) != null)
            .collect(ArrayCollectors.<Class<? extends IdBasedReadablePacket>>toArray(Class.class));

        var registry = new IdBasedReadablePacketRegistry<>(IdBasedReadablePacket.class);
        registry.register(result);

        return registry;
    }

    /**
     * Create a new readable packet registry by array of classes.
     *
     * @param type    the base packet's type.
     * @param classes the classes array.
     * @return the new packet registry.
     */
    @SafeVarargs
    static <T extends IdBasedReadablePacket> @NotNull ReadablePacketRegistry<T> of(
        @NotNull Class<T> type,
        @NotNull Class<? extends T>... classes
    ) {
        var registry = new IdBasedReadablePacketRegistry<>(type);
        registry.register(classes, classes.length);
        return registry;
    }

    /**
     * Create a new readable packet registry by array of classes.
     *
     * @param type    the base packet's type.
     * @param classes the classes array.
     * @return the new packet registry.
     */
    static <T extends IdBasedReadablePacket> @NotNull ReadablePacketRegistry<T> of(
        @NotNull Class<T> type,
        @NotNull Array<Class<? extends T>> classes
    ) {

        var registry = new IdBasedReadablePacketRegistry<>(type);
        registry.register(classes);

        return registry;
    }

    /**
     * Find a packet by the id.
     *
     * @param id the packet id.
     * @return the packet.
     * @throws IllegalArgumentException if can't find a packet by the id.
     */
    @NotNull R findById(int id);
}
