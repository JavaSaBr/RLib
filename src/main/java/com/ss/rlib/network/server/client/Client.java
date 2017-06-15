package com.ss.rlib.network.server.client;

import com.ss.rlib.network.ConnectionOwner;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement a client.
 *
 * @param <A> the type parameter
 * @param <P> the type parameter
 * @author JavaSaBr
 */
public interface Client<A, P> extends ConnectionOwner {

    /**
     * Gets account.
     *
     * @return the client account.
     */
    @Nullable
    A getAccount();

    /**
     * Sets account.
     *
     * @param account the client account.
     */
    void setAccount(@Nullable A account);

    /**
     * Gets owner.
     *
     * @return the owner.
     */
    @Nullable
    P getOwner();

    /**
     * Sets owner.
     *
     * @param owner the owner.
     */
    void setOwner(@Nullable P owner);

    /**
     * Handle successful connect.
     */
    void successfulConnection();
}
