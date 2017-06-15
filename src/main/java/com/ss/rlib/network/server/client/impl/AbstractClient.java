package com.ss.rlib.network.server.client.impl;

import com.ss.rlib.network.NetworkCrypt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ss.rlib.network.AsyncConnection;
import com.ss.rlib.network.impl.AbstractConnectionOwner;
import com.ss.rlib.network.server.client.Client;

/**
 * The base implementation of a client.
 *
 * @param <A> the type parameter
 * @param <O> the type parameter
 * @author JavaSaBr
 */
public abstract class AbstractClient<A, O> extends AbstractConnectionOwner implements Client<A, O> {

    /**
     * The owner of this connection.
     */
    @Nullable
    protected volatile O owner;

    /**
     * The client account.
     */
    @Nullable
    protected volatile A account;

    /**
     * Instantiates a new Abstract client.
     *
     * @param connection the connection
     * @param crypt      the crypt
     */
    protected AbstractClient(@NotNull final AsyncConnection connection, @NotNull final NetworkCrypt crypt) {
        super(connection, crypt);
    }

    @Nullable
    @Override
    public final A getAccount() {
        return account;
    }

    @Override
    public final void setAccount(@Nullable final A account) {
        this.account = account;
    }

    @Nullable
    @Override
    public final O getOwner() {
        return owner;
    }

    @Override
    public final void setOwner(@Nullable final O owner) {
        this.owner = owner;
    }

    @Override
    public void successfulConnection() {
        LOGGER.info(this, "successful connection.");
    }

    @Override
    public String toString() {
        return "AbstractClient{" + ", owner=" + owner + ", account=" + account + ", crypt=" + crypt + ", closed=" +
                closed + '}';
    }
}
