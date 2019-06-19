package com.ss.rlib.network.packet.registry.impl;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.annotation.PacketDescription;
import com.ss.rlib.network.packet.IdBasedReadablePacket;
import com.ss.rlib.network.packet.registry.ReadablePacketRegistry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * The id based implementation of readable packets registry.
 *
 * @author JavaSaBr
 */
public class IdBasedReadablePacketRegistry<R extends IdBasedReadablePacket<R>> implements ReadablePacketRegistry<R> {

    private static final Logger LOGGER = LoggerManager.getLogger(IdBasedReadablePacketRegistry.class);

    /**
     * The packet's type in this registry.
     */
    private final Class<? extends R> type;

    /**
     * The array of packet id to packet instance.
     */
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private volatile R[] idToPacket;

    public IdBasedReadablePacketRegistry(@NotNull Class<? extends R> type) {
        this.idToPacket = ArrayUtils.create(type, 0);
        this.type = type;
    }

    /**
     * Register classes of readable packets.
     *
     * @param classes the classes array.
     * @return the reference to this registry.
     * @throws IllegalArgumentException if found a class without packet description annotation or
     *                                  if found duplication by id.
     */
    public @NotNull IdBasedReadablePacketRegistry<R> register(@NotNull Array<Class<? extends R>> classes) {
        return register(classes.array(), classes.size());
    }

    /**
     * Register classes of readable packets.
     *
     * @param classes the classes array.
     * @return the reference to this registry.
     * @throws IllegalArgumentException if found a class without packet description annotation or
     *                                  if found duplication by id.
     */
    @SafeVarargs
    public final @NotNull IdBasedReadablePacketRegistry<R> register(@NotNull Class<? extends R>... classes) {
        return register(classes, classes.length);
    }

    /**
     * Register classes of readable packets.
     *
     * @param classes the classes array.
     * @param length  the length of the classes.
     * @return the reference to this registry.
     * @throws IllegalArgumentException if found a class without packet description annotation or
     *                                  if found duplication by id.
     */
    public @NotNull IdBasedReadablePacketRegistry<R> register(@NotNull Class<? extends R>[] classes, int length) {

        var incorrectClass = Arrays.stream(classes, 0, length)
            .filter(type -> type.getAnnotation(PacketDescription.class) == null)
            .findFirst();

        if (incorrectClass.isPresent()) {
            throw new IllegalArgumentException("Have found a class " + incorrectClass.get() +
                " without the packet description annotation.");
        }

        var maxId = Arrays.stream(classes, 0, length)
            .map(type -> type.getAnnotation(PacketDescription.class))
            .mapToInt(PacketDescription::id)
            .max()
            .orElseThrow(() -> new IllegalStateException("Not found any packet id"));

        setIdToPacket(Arrays.copyOf(getIdToPacket(), maxId + 1));

        var idToPacket = getIdToPacket();

        for (int i = 0; i < length; i++) {

            var cs = classes[i];

            if (!type.isAssignableFrom(cs)) {
                LOGGER.warning(cs, type,
                    (detected, check) -> "Found incompatibility packet's type: " + detected + " with type: " + check);
                continue;
            }

            var description = cs.getAnnotation(PacketDescription.class);
            var id = description.id();

            if (idToPacket[id] != null) {
                throw new IllegalArgumentException("Have found duplication by id " + id + ", existed packet is " +
                        idToPacket[id].getClass() + ", new packet is " + cs);
            }

            idToPacket[id] = ClassUtils.newInstance(cs);
        }

        return this;
    }

    /**
     * Register a class of readable packet.
     *
     * @param cs the class.
     * @return the reference to this registry.
     * @throws IllegalArgumentException if this class doesn't have {@link PacketDescription},
     *                                  wrong id or some class is already presented with the same id.
     */
    public @NotNull IdBasedReadablePacketRegistry<R> register(@NotNull Class<? extends R> cs) {
        return register(cs, () -> ClassUtils.newInstance(cs));
    }

    /**
     * Register a class of readable packet.
     *
     * @param cs      the class.
     * @param factory the instance factory.
     * @return the reference to this registry.
     * @throws IllegalArgumentException if this class doesn't have {@link PacketDescription},
     *                                  wrong id or some class is already presented with the same id.
     */
    public <P extends R> @NotNull IdBasedReadablePacketRegistry<R> register(
        @NotNull Class<P> cs, @NotNull Supplier<P> factory
    ) {

        var description = cs.getAnnotation(PacketDescription.class);

        if (description == null) {
            throw new IllegalArgumentException("Class " + cs + " doesn't have packet description annotation.");
        }

        var id = description.id();

        if (id < 0) {
            throw new IllegalArgumentException("Class " + cs + " has wrong packet id: " + id);
        }

        var idToPacket = getIdToPacket();

        if (id < idToPacket.length) {
            if (idToPacket[id] != null) {
                throw new IllegalArgumentException("Class " + idToPacket[id].getClass() +
                    " is already has the same id: " + id);
            } else {
                idToPacket[id] = ClassUtils.newInstance(cs);
                return this;
            }
        }

        idToPacket = Arrays.copyOf(getIdToPacket(), id + 1);
        idToPacket[id] = factory.get();

        setIdToPacket(idToPacket);

        return this;
    }

    @Override
    public @NotNull R findById(int id) {

        R[] idToPacket = getIdToPacket();

        if (id < 0 || id >= idToPacket.length) {
            throw new IllegalArgumentException("Not found a packet for the id " + id);
        }

        var packet = idToPacket[id];

        if (packet == null) {
            throw new IllegalArgumentException("Not found a packet for the id " + id);
        }

        return packet;
    }
}
