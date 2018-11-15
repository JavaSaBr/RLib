package com.ss.rlib.network.packet;

import static com.ss.rlib.common.util.array.ArrayCollectors.toArray;
import com.ss.rlib.common.classpath.ClassPathScanner;
import com.ss.rlib.common.classpath.ClassPathScannerFactory;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.network.annotation.PacketDescription;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

/**
 * The registry of readable packets.
 *
 * @author JavaSaBr
 */
public class ReadablePacketRegistry {

    /**
     * Creates a new empty readable packet registry.
     *
     * @return the new packet registry.
     */
    public static @NotNull ReadablePacketRegistry empty() {
        return new ReadablePacketRegistry();
    }

    /**
     * Creates a new default readable packet registry.
     *
     * @return the new packet registry.
     */
    public static @NotNull ReadablePacketRegistry newDefault() {

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
    public static @NotNull ReadablePacketRegistry newDefault(@NotNull Class<?> mainClass) {

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
    public static @NotNull ReadablePacketRegistry of(@NotNull ClassPathScanner scanner) {

        Array<Class<ReadablePacket>> classes = scanner.findImplements(ReadablePacket.class);
        Array<Class<? extends ReadablePacket>> result = classes.stream()
            .filter(type -> type.getAnnotation(PacketDescription.class) != null)
            .collect(toArray(ClassUtils.unsafeCast(Class.class)));

        var registry = new ReadablePacketRegistry();
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
    public static @NotNull ReadablePacketRegistry of(@NotNull Class<? extends ReadablePacket>... classes) {
        var registry = new ReadablePacketRegistry();
        registry.register(classes, classes.length);
        return registry;
    }

    /**
     * Creates a new readable packet registry by the array of classes.
     *
     * @param classes the classes array.
     * @return the new packet registry.
     */
    public static @NotNull ReadablePacketRegistry of(@NotNull Array<Class<? extends ReadablePacket>> classes) {
        var registry = new ReadablePacketRegistry();
        registry.register(classes);
        return registry;
    }

    /**
     * The array packet id to packet instance.
     */
    @NotNull
    private volatile ReadablePacket[] idToPacket;

    public ReadablePacketRegistry() {
        this.idToPacket = new ReadablePacket[0];
    }

    /**
     * Register the classes of readable packets.
     *
     * @param classes the classes array.
     * @throws IllegalArgumentException if found a class without packet description annotation or if found duplication be id.
     */
    private void register(@NotNull Array<Class<? extends ReadablePacket>> classes) {
        register(classes.array(), classes.size());
    }

    /**
     * Register the classes of readable packets.
     *
     * @param classes the classes array.
     * @param length  the length of the classes.
     * @throws IllegalArgumentException if found a class without packet description annotation or if found duplication be id.
     */
    private void register(@NotNull Class<? extends ReadablePacket>[] classes, int length) {

        Optional<Class<? extends ReadablePacket>> incorrectClass = Arrays.stream(classes, 0, length)
            .filter(type -> type.getAnnotation(PacketDescription.class) == null)
            .findFirst();

        if (incorrectClass.isPresent()) {
            throw new IllegalArgumentException("Have found a class " + incorrectClass.get() +
                " without the packet description annotation.");
        }

        int maxId = Arrays.stream(classes, 0, length)
            .map(type -> type.getAnnotation(PacketDescription.class))
            .mapToInt(PacketDescription::id)
            .max().orElseThrow(() -> new IllegalStateException("Not found any packet id"));

        idToPacket = Arrays.copyOf(idToPacket, maxId + 1);

        for (int i = 0; i < length; i++) {

            Class<? extends ReadablePacket> type = classes[i];
            PacketDescription description = type.getAnnotation(PacketDescription.class);
            int id = description.id();

            if (idToPacket[id] != null) {
                throw new IllegalArgumentException("Have found duplication by the id " + id + ", existed packet is " +
                        idToPacket[id].getClass() + ", new packet is " + type);
            }

            idToPacket[id] = ClassUtils.newInstance(type);
        }
    }

    /**
     * Find a packet by the id.
     *
     * @param id the packet id.
     * @return the packet.
     * @throws IllegalArgumentException if can't find a packet by the id.
     */
    public @NotNull ReadablePacket findById(int id) {

        var packet = idToPacket[id];

        if (packet == null) {
            throw new IllegalArgumentException("Not found a packet for the id " + id);
        }

        return packet;
    }
}
