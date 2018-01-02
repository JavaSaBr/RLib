package com.ss.rlib.network.packet;

import static java.util.Objects.requireNonNull;
import static com.ss.rlib.util.ClassUtils.unsafeCast;
import com.ss.rlib.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import com.ss.rlib.util.pools.PoolFactory;
import com.ss.rlib.util.pools.ReusablePool;

/**
 * The list of available types of sendable packets.
 *
 * @param <S> the type parameter
 * @author JavaSaBr
 */
public class SendablePacketType<S extends SendablePacket> {

    /**
     * The list of registered sendable packet types.
     */
    @NotNull
    private final static Array<SendablePacketType<?>> REGISTERED_TYPES = ArrayFactory.newArray(SendablePacketType.class);

    /**
     * Register the new packet type.
     *
     * @param packetType the packet type.
     */
    private static void register(@NotNull final SendablePacketType<?> packetType) {

        final SendablePacketType result = REGISTERED_TYPES.search(packetType.getId(), (exists, toCheck) -> exists.getId() == toCheck);
        if (result != null) {
            throw new RuntimeException( "Have found duplicate packet type id for the " + packetType.getName() + " and " + result.getName());
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

    public SendablePacketType(@NotNull final Class<? extends S> cs, final int id) {
        this.name = cs.getSimpleName();
        this.id = id;
        this.pool = Reusable.class.isAssignableFrom(cs) ? PoolFactory.newConcurrentAtomicARSWLockReusablePool(unsafeCast(cs)) : null;
        register(this);
    }

    /**
     * Get the id.
     *
     * @return the packet type id.
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
        return requireNonNull(pool, "This type is not reusable packet.");
    }
}
