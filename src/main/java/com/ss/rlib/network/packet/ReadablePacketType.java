package com.ss.rlib.network.packet;

import static java.util.Objects.requireNonNull;
import static com.ss.rlib.util.ClassUtils.unsafeCast;
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
     * The list of registered sendable packet types.
     */
    @NotNull
    private final static Array<ReadablePacketType<?>> REGISTERED_TYPES = ArrayFactory.newArray(ReadablePacketType.class);

    /**
     * The array of packet type id to packet type.
     */
    @NotNull
    private static final ReadablePacketType<?>[] TYPES = ArrayUtils.create(ReadablePacketType.class,
            Short.MAX_VALUE * 2);

    /**
     * Get the type of packet by its packet type id.
     *
     * @param <T> the type parameter
     * @param id  the packet id.
     * @return the type of the packet.
     */
    @NotNull
    public static <T extends ReadablePacket> ReadablePacketType<T> getPacketType(final int id) {
        return requireNonNull(ClassUtils.unsafeCast(TYPES[id]));
    }

    /**
     * Register new packet type.
     *
     * @param packetType the packet type.
     */
    private static void register(@NotNull final ReadablePacketType<?> packetType) {

        final ReadablePacketType result = REGISTERED_TYPES.search(packetType.getId(), (exists, toCheck) ->
                exists.getId() == toCheck);

        if (result != null) {
            throw new RuntimeException(
                    "Have found duplicate packet type id for the " + packetType.getName() + " and " + result.getName());
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

    /**
     * Instantiates a new Readable packet type.
     *
     * @param example the example packet of the type.
     * @param id      the packet type id.
     */
    public ReadablePacketType(@NotNull final R example, final int id) {
        final Class<? extends ReadablePacket> cs = example.getClass();
        this.name = cs.getSimpleName();
        this.example = example;
        this.id = id;
        this.pool = Reusable.class.isAssignableFrom(cs) ?
                PoolFactory.newConcurrentAtomicARSWLockReusablePool(ClassUtils.unsafeCast(cs)) : null;
        register(this);
    }

    /**
     * Gets id.
     *
     * @return the packet type id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name of the type.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Gets pool.
     *
     * @return the pool of packets to reuse them.
     */
    @NotNull
    public final ReusablePool<Reusable> getPool() {
        return requireNonNull(pool, "This type is not reusable packet.");
    }

    /**
     * Create a new instance of this packet type.
     *
     * @return the new instance.
     */
    @NotNull
    public R newInstance() {

        if (!(example instanceof Reusable)) {
            return requireNonNull(ClassUtils.unsafeCast(example.newInstance()));
        }

        return requireNonNull(ClassUtils.unsafeCast(getPool().take(example, packet -> ClassUtils.unsafeCast(packet.newInstance()))));
    }
}
