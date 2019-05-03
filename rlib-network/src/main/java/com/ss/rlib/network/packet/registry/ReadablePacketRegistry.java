package com.ss.rlib.network.packet.registry;

import com.ss.rlib.common.classpath.ClassPathScanner;
import com.ss.rlib.common.classpath.ClassPathScannerFactory;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayCollectors;
import com.ss.rlib.network.annotation.PacketDescription;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.registry.impl.IdBasedReadablePacketRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a registry of readable packets.
 *
 * @author JavaSaBr
 */
public interface ReadablePacketRegistry {

    /**
     * Creates a new empty readable packet registry.
     *
     * @return the new packet registry.
     */
    static @NotNull ReadablePacketRegistry empty() {
        return new IdBasedReadablePacketRegistry();
    }

    /**
     * Creates a new default readable packet registry.
     *
     * @return the new packet registry.
     */
    static @NotNull ReadablePacketRegistry newDefault() {

        var scanner = ClassPathScannerFactory.newDefaultScanner();
        scanner.setUseSystemClasspath(true);
        scanner.scan();

        return of(scanner);
    }

    /**
     * Creates a new default readable packet registry by the result of scanning classpath of the main class.
     *
     * @param mainClass the main class of application.
     * @return the new packet registry.
     */
    static @NotNull ReadablePacketRegistry newDefault(@NotNull Class<?> mainClass) {

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
    static @NotNull ReadablePacketRegistry of(@NotNull ClassPathScanner scanner) {

        var result = scanner.findImplements(ReadablePacket.class)
            .stream()
            .filter(type -> type.getAnnotation(PacketDescription.class) != null)
            .collect(ArrayCollectors.<Class<? extends ReadablePacket>>toArray(Class.class));

        var registry = new IdBasedReadablePacketRegistry();
        registry.register(result);

        return registry;
    }

    /**
     * Creates a new readable packet registry by the array of classes.
     *
     * @param classes the classes array.
     * @return the new packet registry.
     */
    @SafeVarargs
    static @NotNull ReadablePacketRegistry of(@NotNull Class<? extends ReadablePacket>... classes) {
        var registry = new IdBasedReadablePacketRegistry();
        registry.register(classes, classes.length);
        return registry;
    }

    /**
     * Creates a new readable packet registry by the array of classes.
     *
     * @param classes the classes array.
     * @return the new packet registry.
     */
    static @NotNull ReadablePacketRegistry of(@NotNull Array<Class<? extends ReadablePacket>> classes) {
        var registry = new IdBasedReadablePacketRegistry();
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
    @NotNull ReadablePacket findById(int id);
}
