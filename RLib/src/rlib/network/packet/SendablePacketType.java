package rlib.network.packet;

import static java.util.Objects.requireNonNull;
import static rlib.util.ClassUtils.unsafeCast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.Reusable;
import rlib.util.pools.ReusablePool;

/**
 * The list of available types of sendable packets.
 *
 * @author JavaSaBr
 */
public class SendablePacketType<S extends SendablePacket> {

    /**
     * The list of registered sendable packet types.
     */
    @NotNull
    private final static Array<SendablePacketType<?>> REGISTERED_TYPES = ArrayFactory.newArray(SendablePacketType.class);

    /**
     * Register new packet type.
     *
     * @param packetType the packet type.
     */
    private static void register(@NotNull final SendablePacketType<?> packetType) {

        final SendablePacketType result = REGISTERED_TYPES.search(packetType.getId(), (exists, toCheck) ->
                exists.getId() == toCheck);

        if (result != null) {
            throw new RuntimeException(
                    "Have found duplicate packet type id for the " + packetType.getName() + " and " + result.getName());
        }

        REGISTERED_TYPES.add(packetType);
    }

    /**
     * The pool of packets to reuse them.
     */
    @Nullable
    private final ReusablePool<Reusable> pool;

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
     * @param cs the cs of the type.
     * @param id the packet type id.
     */
    public SendablePacketType(@NotNull final Class<? extends S> cs, final int id) {
        this.name = cs.getSimpleName();
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
}
