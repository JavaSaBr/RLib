package com.ss.rlib.idfactory.impl;

import com.ss.rlib.database.ConnectFactory;
import com.ss.rlib.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.BitSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.ss.rlib.database.DBUtils;
import com.ss.rlib.idfactory.IdGenerator;
import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.util.ArrayUtils;
import com.ss.rlib.util.array.IntegerArray;
import com.ss.rlib.util.dictionary.DictionaryFactory;
import com.ss.rlib.util.dictionary.IntegerDictionary;

/**
 * THe BitSet implementation of a ID generator.
 *
 * @author JavaSaBr
 */
public final class BitSetIdGenerator implements IdGenerator, Runnable {

    private static final Logger LOGGER = LoggerManager.getLogger(BitSetIdGenerator.class);

    /**
     * The first ID.
     */
    public static final int FIRST_ID = 0x10000000;

    /**
     * The last ID.
     */
    public static final int LAST_ID = 0x7FFFFFFF;

    /**
     * The count of free ID.s
     */
    public static final int FREE_ID_SIZE = LAST_ID - FIRST_ID;

    /**
     * The executor service.
     */
    @NotNull
    private final ScheduledExecutorService executorService;

    /**
     * The connection factory.
     */
    @NotNull
    private final ConnectFactory connectFactory;

    /**
     * The tables.
     */
    @NotNull
    private final String[][] tables;

    /**
     * The free IDs set.
     */
    private volatile BitSet freeIds;

    /**
     * The count of free IDs.
     */
    private AtomicInteger freeIdCount;

    /**
     * The next free IDs.
     */
    private AtomicInteger nextFreeId;

    /**
     * Instantiates a new Bit set id generator.
     *
     * @param connectFactory  the connect factory
     * @param executorService the executor service
     * @param tables          the tables
     */
    public BitSetIdGenerator(@NotNull final ConnectFactory connectFactory,
                             @NotNull final ScheduledExecutorService executorService,
                             @NotNull final String[][] tables) {
        this.executorService = executorService;
        this.connectFactory = connectFactory;
        this.tables = tables;
    }

    @Override
    public synchronized int getNextId() {

        final int newID = nextFreeId.get();

        freeIds.set(newID);
        freeIdCount.decrementAndGet();

        int nextFree = freeIds.nextClearBit(newID);

        if (nextFree < 0) {
            nextFree = freeIds.nextClearBit(0);
        }

        if (nextFree < 0) {
            if (freeIds.size() < FREE_ID_SIZE) {
                increaseBitSetCapacity();
            } else {
                throw new NullPointerException("Ran out of valid Id's.");
            }
        }

        nextFreeId.set(nextFree);

        return newID + FIRST_ID;
    }

    /**
     * Increase bit set capacity.
     */
    protected synchronized void increaseBitSetCapacity() {

        final BitSet newBitSet = new BitSet(PrimeFinder.nextPrime(usedIds() * 11 / 10));
        newBitSet.or(freeIds);

        freeIds = newBitSet;
    }

    @Override
    public void prepare() {

        try {

            freeIds = new BitSet(PrimeFinder.nextPrime(100000));
            freeIds.clear();

            freeIdCount = new AtomicInteger(FREE_ID_SIZE);

            final IntegerDictionary<String> useIds = DictionaryFactory.newIntegerDictionary();
            final IntegerArray clearIds = ArrayFactory.newIntegerArray();
            final IntegerArray extractedIds = ArrayFactory.newIntegerArray();

            Connection con = null;
            Statement statement = null;
            ResultSet rset = null;
            try {

                con = connectFactory.getConnection();
                statement = con.createStatement();

                for (final String[] table : tables) {

                    rset = statement.executeQuery("SELECT " + table[1] + " FROM " + table[0]);

                    while (rset.next()) {

                        final int objectId = rset.getInt(1);

                        if (!useIds.containsKey(objectId)) {
                            extractedIds.add(objectId);
                            useIds.put(objectId, table[0]);
                        } else {
                            clearIds.add(objectId);
                            LOGGER.warning("recurrence was found and '" + objectId + "' in the table `" + table[0] + "`, which is already in the table `" + useIds.get(objectId) + "`.");
                        }
                    }

                    if (!clearIds.isEmpty()) {

                        DBUtils.close(rset);

                        for (final int id : clearIds.array()) {
                            statement.executeUpdate("DELETE FROM " + table[0] + " WHERE " + table[1] + " = " + id + " LIMIT 1");
                        }
                    }
                }
            } finally {
                DBUtils.close(con, statement, rset);
            }

            final int[] extracted = new int[extractedIds.size()];

            for (int i = 0, length = extractedIds.size(); i < length; i++) {
                extracted[i] = extractedIds.get(i);
            }

            LOGGER.info("extracted " + extracted.length + " ids.");

            ArrayUtils.sort(extracted);

            for (final int objectId : extracted) {

                final int id = objectId - FIRST_ID;

                if (id < 0) {
                    LOGGER.warning("objectId " + objectId + " in DB is less than minimum ID of " + FIRST_ID + ".");
                    continue;
                }

                freeIds.set(objectId - FIRST_ID);
                freeIdCount.decrementAndGet();
            }

        } catch (final Exception e) {
            LOGGER.warning(e);
        }

        nextFreeId = new AtomicInteger(freeIds.nextClearBit(0));
        executorService.scheduleAtFixedRate(this, 300000, 300000, TimeUnit.MILLISECONDS);
        LOGGER.info(freeIds.size() + " id's available.");
    }

    /**
     * Reaching bit set capacity boolean.
     *
     * @return the boolean
     */
    protected synchronized boolean reachingBitSetCapacity() {
        return PrimeFinder.nextPrime(usedIds() * 11 / 10) > freeIds.size();
    }

    @Override
    public synchronized void releaseId(final int objectId) {
        if (objectId - FIRST_ID < 0) {
            LOGGER.warning("release objectID " + objectId + " failed (< " + FIRST_ID + ")");
        } else {
            freeIds.clear(objectId - FIRST_ID);
            freeIdCount.incrementAndGet();
        }
    }

    @Override
    public void run() {
        if (reachingBitSetCapacity()) {
            increaseBitSetCapacity();
        }
    }

    /**
     * Size int.
     *
     * @return the count of free IDs.
     */
    public synchronized int size() {
        return freeIdCount.get();
    }

    @Override
    public int usedIds() {
        return size() - FIRST_ID;
    }
}