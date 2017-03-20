package rlib.network.packet;

import static java.util.Objects.requireNonNull;
import static rlib.util.ClassUtils.unsafeCast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.Reusable;
import rlib.util.pools.ReusablePool;

/**
 * The list of available types of readable packets.
 *
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
     * @param id the packet id.
     * @return the type of the packet.
     */
    @NotNull
    public static <T extends ReadablePacket> ReadablePacketType<T> getPacketType(final int id) {
        return requireNonNull(unsafeCast(TYPES[id]));
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
     * @param example the example packet of the type.
     * @param id      the packet type id.
     */
    public ReadablePacketType(@NotNull final R example, final int id) {
        final Class<? extends ReadablePacket> cs = example.getClass();
        this.name = cs.getSimpleName();
        this.example = example;
        this.id = id;
        this.pool = Reusable.class.isAssignableFrom(cs) ?
                PoolFactory.newConcurrentAtomicARSWLockReusablePool(unsafeCast(cs)) : null;
        register(this);
    }

    /**
     * @return the packet type id.
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name of the type.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
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
            return requireNonNull(unsafeCast(example.newInstance()));
        }

        return requireNonNull(unsafeCast(getPool().take(example, packet -> unsafeCast(packet.newInstance()))));
    }
}
