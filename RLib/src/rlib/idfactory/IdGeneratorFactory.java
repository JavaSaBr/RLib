package rlib.idfactory;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledExecutorService;

import rlib.database.ConnectFactory;
import rlib.idfactory.impl.BitSetIdGenerator;
import rlib.idfactory.impl.SimpleIdGenerator;

/**
 * The factory of IDs generators.
 *
 * @author JavaSaBr
 */
public class IdGeneratorFactory {

    /**
     * Create a new ID generator which uses BitSet and works with DB.
     *
     * @param connectFactory  the connection factory.
     * @param executorService the executorService service.
     * @param tables          the tables with IDs.
     * @return the new generator.
     */
    @NotNull
    public static IdGenerator newBitSetIdDBGenerator(@NotNull final ConnectFactory connectFactory,
                                                     @NotNull final ScheduledExecutorService executorService,
                                                     @NotNull final String[][] tables) {
        return new BitSetIdGenerator(connectFactory, executorService, tables);
    }

    /**
     * Create a simple id generator.
     *
     * @param start the start ID.
     * @param end   the last ID.
     * @return the new ID generator.
     */
    @NotNull
    public static IdGenerator newSimpleIdGenerator(final int start, final int end) {
        return new SimpleIdGenerator(start, end);
    }

    private IdGeneratorFactory() {
        throw new IllegalArgumentException();
    }
}
