package com.ss.rlib.network.packet;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.ss.rlib.util.ArrayUtils;
import com.ss.rlib.util.ClassUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import com.ss.rlib.util.pools.PoolFactory;
import com.ss.rlib.util.pools.Reusable;
import com.ss.rlib.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The list of available types of readable packets.
 *
 * @param <R> the type parameter
 * @author JavaSaBr
 */
public class ReadablePacketType<R extends ReadablePacket> {

    /**
     * The list of registered readable packet types.
     */
    @NotNull
    private final static Array<ReadablePacketType<?>> REGISTERED_TYPES = ArrayFactory.newArray(ReadablePacketType.class);

    /**
     * The map of packet ids to packet types.
     */
    @NotNull
    private static final ReadablePacketType<?>[] TYPES = ArrayUtils.create(ReadablePacketType.class, Short.MAX_VALUE * 2);

    /**
     * Get the packet type by the packet id.
     *
     * @param id  the packet id.
     * @return the type of the packet.
     */
    public static <T extends ReadablePacket> @NotNull ReadablePacketType<T> getPacketType(final int id) {
        return notNull(ClassUtils.unsafeCast(TYPES[id]));
    }

    /**
     * Register a new packet type.
     *
     * @param packetType the packet type.
     */
    private static void register(@NotNull final ReadablePacketType<?> packetType) {

        final ReadablePacketType result = REGISTERED_TYPES.search(packetType.getId(), (exists, toCheck) -> exists.getId() == toCheck);
        if (result != null) {
            throw new RuntimeException("Have found duplicate packet type id for the " + packetType.getName() + " and " + result.getName());
        }

        REGISTERED_TYPES.add(packetType);
        TYPES[packetType.getId()] = packetType;
    }

    /**
     * The pool of packets to reuse them.
     */
    @Nullable
    private final ReusablePool<Reusable> pool;

    /**
     * The example.
     */
    @NotNull
    private final R example;

    /**
     * The name of the type.
     */
    @NotNull
    private final String name;

    /**
     * The packet type id.
     */
    private final int id;

    public ReadablePacketType(@NotNull final R example, final int id) {
        final Class<? extends ReadablePacket> cs = example.getClass();
        this.name = cs.getSimpleName();
        this.example = example;
        this.id = id;
        this.pool = Reusable.class.isAssignableFrom(cs) ? PoolFactory.newConcurrentAtomicARSWLockReusablePool(ClassUtils.unsafeCast(cs)) : null;
        register(this);
    }

    /**
     * Get the packet id.
     *
     * @return the packet id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the name.
     *
     * @return the name of the type.
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Get the pool.
     *
     * @return the pool of packets to reuse them.
     */
    public @NotNull ReusablePool<Reusable> getPool() {
        return notNull(pool, "This type is not reusable packet.");
    }

    /**
     * Create a new instance of a packet.
     *
     * @return the new instance.
     */
    public @NotNull R newInstance() {
        if (!(example instanceof Reusable)) {
            return notNull(ClassUtils.unsafeCast(example.newInstance()));
        }
        return notNull(ClassUtils.unsafeCast(getPool().take(example, packet -> ClassUtils.unsafeCast(packet.newInstance()))));
    }
}
