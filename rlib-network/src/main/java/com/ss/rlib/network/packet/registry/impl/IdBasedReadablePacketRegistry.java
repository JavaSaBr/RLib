package com.ss.rlib.network.packet.registry.impl;

import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.network.annotation.PacketDescription;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.registry.ReadablePacketRegistry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * The id based implementation of readable packets registry.
 *
 * @author JavaSaBr
 */
public class IdBasedReadablePacketRegistry implements ReadablePacketRegistry {

    /**
     * The array packet id to packet instance.
     */
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private volatile ReadablePacket[] idToPacket;

    public IdBasedReadablePacketRegistry() {
        this.idToPacket = new ReadablePacket[0];
    }

    /**
     * Register the classes of readable packets.
     *
     * @param classes the classes array.
     * @throws IllegalArgumentException if found a class without packet description annotation or if found duplication be id.
     */
    public void register(@NotNull Array<Class<? extends ReadablePacket>> classes) {
        register(classes.array(), classes.size());
    }

    /**
     * Register the classes of readable packets.
     *
     * @param classes the classes array.
     * @param length  the length of the classes.
     * @throws IllegalArgumentException if found a class without packet description annotation or if found duplication be id.
     */
    public void register(@NotNull Class<? extends ReadablePacket>[] classes, int length) {

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
            .max().orElseThrow(() -> new IllegalStateException("Not found any packet id"));

        setIdToPacket(Arrays.copyOf(getIdToPacket(), maxId + 1));

        var idToPacket = getIdToPacket();

        for (int i = 0; i < length; i++) {

            var type = classes[i];
            var description = type.getAnnotation(PacketDescription.class);
            var id = description.id();

            if (idToPacket[id] != null) {
                throw new IllegalArgumentException("Have found duplication by the id " + id + ", existed packet is " +
                        this.idToPacket[id].getClass() + ", new packet is " + type);
            }

            idToPacket[id] = ClassUtils.newInstance(type);
        }
    }

    @Override
    public @NotNull ReadablePacket findById(int id) {

        var packet = idToPacket[id];

        if (packet == null) {
            throw new IllegalArgumentException("Not found a packet for the id " + id);
        }

        return packet;
    }
}
