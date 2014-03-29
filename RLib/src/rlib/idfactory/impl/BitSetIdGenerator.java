package rlib.idfactory.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.BitSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import rlib.database.ConnectFactory;
import rlib.database.DBUtils;
import rlib.idfactory.IdGenerator;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ArrayUtils;
import rlib.util.SafeTask;
import rlib.util.array.ArrayFactory;
import rlib.util.array.IntegerArray;
import rlib.util.table.IntKey;
import rlib.util.table.Table;
import rlib.util.table.TableFactory;

/**
 * Модель фабрики ид основаной на BitSet
 * 
 * @author Ronn
 */
public final class BitSetIdGenerator implements IdGenerator, SafeTask {

	/** логгер */
	private static final Logger LOGGER = LoggerManager.getLogger(BitSetIdGenerator.class);

	/** первый ид */
	public static final int FIRST_ID = 0x10000000;
	/** последний ид */
	public static final int LAST_ID = 0x7FFFFFFF;
	/** кол-во свободных ид */
	public static final int FREE_ID_SIZE = LAST_ID - FIRST_ID;

	/** серв для отложенного исполнения */
	private final ScheduledExecutorService executor;
	/** фабрика подключений к БД */
	private final ConnectFactory connects;

	/** свободные иды */
	private volatile BitSet freeIds;

	/** кол-во свободных ид */
	private AtomicInteger freeIdCount;
	/** следующие свободные ид */
	private AtomicInteger nextFreeId;

	/** список извлекаемых таблиц и полей */
	private final String[][] tables;

	public BitSetIdGenerator(ConnectFactory connects, ScheduledExecutorService executor, String[][] tables) {
		this.executor = executor;
		this.connects = connects;
		this.tables = tables;
	}

	@Override
	public synchronized int getNextId() {

		int newID = nextFreeId.get();

		freeIds.set(newID);
		freeIdCount.decrementAndGet();

		int nextFree = freeIds.nextClearBit(newID);

		if(nextFree < 0) {
			nextFree = freeIds.nextClearBit(0);
		}

		if(nextFree < 0) {
			if(freeIds.size() < FREE_ID_SIZE) {
				increaseBitSetCapacity();
			} else {
				throw new NullPointerException("Ran out of valid Id's.");
			}
		}

		nextFreeId.set(nextFree);

		return newID + FIRST_ID;
	}

	/**
	 * Увеличение и обновление бит сета.
	 */
	protected synchronized void increaseBitSetCapacity() {

		BitSet newBitSet = new BitSet(PrimeFinder.nextPrime(usedIds() * 11 / 10));
		newBitSet.or(freeIds);

		freeIds = newBitSet;
	}

	@Override
	public void prepare() {

		try {

			freeIds = new BitSet(PrimeFinder.nextPrime(100000));
			freeIds.clear();

			freeIdCount = new AtomicInteger(FREE_ID_SIZE);

			if(tables != null) {

				Table<IntKey, String> useIds = TableFactory.newIntegerTable();

				IntegerArray clearIds = ArrayFactory.newIntegerArray();
				IntegerArray extractedIds = ArrayFactory.newIntegerArray();

				Connection con = null;
				Statement statement = null;
				ResultSet rset = null;

				try {

					con = connects.getConnection();
					statement = con.createStatement();

					for(String[] table : tables) {

						rset = statement.executeQuery("SELECT " + table[1] + " FROM " + table[0]);

						while(rset.next()) {

							int objectId = rset.getInt(1);

							if(!useIds.containsKey(objectId)) {
								extractedIds.add(objectId);
								useIds.put(objectId, table[0]);
							} else {
								clearIds.add(objectId);
								LOGGER.warning("recurrence was found and '" + objectId + "' in the table `" + table[0] + "`, which is already in the table `" + useIds.get(objectId) + "`.");
							}
						}

						if(!clearIds.isEmpty()) {

							DBUtils.closeResultSet(rset);

							for(int id : clearIds.array()) {
								statement.executeUpdate("DELETE FROM " + table[0] + " WHERE " + table[1] + " = " + id + " LIMIT 1");
							}
						}
					}
				} finally {
					DBUtils.closeDatabaseCSR(con, statement, rset);
				}

				int[] extracted = new int[extractedIds.size()];

				for(int i = 0, length = extractedIds.size(); i < length; i++) {
					extracted[i] = extractedIds.get(i);
				}

				LOGGER.info("extracted " + extracted.length + " ids.");

				ArrayUtils.sort(extracted);

				for(int objectId : extracted) {

					int id = objectId - FIRST_ID;

					if(id < 0) {
						LOGGER.warning("objectId " + objectId + " in DB is less than minimum ID of " + FIRST_ID + ".");
						continue;
					}

					freeIds.set(objectId - FIRST_ID);
					freeIdCount.decrementAndGet();
				}
			}
		} catch(Exception e) {
			LOGGER.warning(e);
		}

		nextFreeId = new AtomicInteger(freeIds.nextClearBit(0));
		executor.scheduleAtFixedRate(this, 300000, 300000, TimeUnit.MILLISECONDS);
		LOGGER.info(freeIds.size() + " id's available.");
	}

	protected synchronized boolean reachingBitSetCapacity() {
		return PrimeFinder.nextPrime(usedIds() * 11 / 10) > freeIds.size();
	}

	@Override
	public synchronized void releaseId(int objectId) {
		if(objectId - FIRST_ID < 0) {
			LOGGER.warning("release objectID " + objectId + " failed (< " + FIRST_ID + ")");
		} else {
			freeIds.clear(objectId - FIRST_ID);
			freeIdCount.incrementAndGet();
		}
	}

	@Override
	public void runImpl() {
		if(reachingBitSetCapacity()) {
			increaseBitSetCapacity();
		}
	}

	/**
	 * @return уол-во свободных ид.
	 */
	public synchronized int size() {
		return freeIdCount.get();
	}

	@Override
	public int usedIds() {
		return size() - FIRST_ID;
	}
}