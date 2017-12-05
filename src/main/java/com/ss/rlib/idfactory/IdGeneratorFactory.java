package com.ss.rlib.idfactory;

import com.ss.rlib.database.ConnectionFactory;
import com.ss.rlib.idfactory.impl.BitSetIdGenerator;
import com.ss.rlib.idfactory.impl.SimpleIdGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledExecutorService;

/**
 * The factory of IDs generators.
 *
 * @author JavaSaBr
 */
public class IdGeneratorFactory {

    /**
     * Create a new ID generator which uses BitSet and works with DB.
     *
     * @param connectionFactory  the connection factory.
     * @param executorService the executorService service.
     * @param tables          the tables with IDs.
     * @return the new generator.
     */
    public static @NotNull IdGenerator newBitSetIdDBGenerator(@NotNull final ConnectionFactory connectionFactory,
                                                     @NotNull final ScheduledExecutorService executorService,
                                                     @NotNull final String[][] tables) {
        return new BitSetIdGenerator(connectionFactory, executorService, tables);
    }

    /**
     * Create a simple id generator.
     *
     * @param start the start ID.
     * @param end   the last ID.
     * @return the new ID generator.
     */
    public static @NotNull IdGenerator newSimpleIdGenerator(final int start, final int end) {
        return new SimpleIdGenerator(start, end);
    }

    private IdGeneratorFactory() {
        throw new IllegalArgumentException();
    }
}
