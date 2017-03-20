package rlib.network.server.client.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.AsyncConnection;
import rlib.network.NetworkCrypt;
import rlib.network.impl.AbstractConnectionOwner;
import rlib.network.server.client.Client;

/**
 * The base implementation of a client.
 *
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
