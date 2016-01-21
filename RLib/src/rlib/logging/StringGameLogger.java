package rlib.logging;

import rlib.concurrent.lock.LockFactory;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;

/**
 * Модель строкового логера игровых событий.
 *
 * @author Ronn
 */
public class StringGameLogger implements GameLogger {

    private static final Logger LOGGER = LoggerManager.getLogger(StringGameLogger.class);

    /**
     * синхронизатор
     */
    private final Lock lock;
    /**
     * кэш записей
     */
    private final Array<String> cache;
    /**
     * форматтер времени
     */
    private final SimpleDateFormat timeFormat;
    /**
     * текущая дата
     */
    private final Date date;
    /**
     * средство записи в фаил
     */
    private final Writer out;

    protected StringGameLogger(final File outFile) throws IOException {
        this.timeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.date = new Date();

        if (!outFile.exists()) {
            outFile.createNewFile();
        }

        this.out = new FileWriter(outFile);
        this.lock = LockFactory.newLock();
        this.cache = ArrayFactory.newArray(String.class);
    }

    @Override
    public void finish() {
        lock.lock();
        try {
            writeCache();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void write(final String text) {
        lock.lock();
        try {

            if (cache.size() > 1000) {
                writeCache();
            }

            date.setTime(System.currentTimeMillis());
            cache.add(timeFormat.format(date) + ": " + text + "\n");

        } finally {
            lock.unlock();
        }
    }

    @Override
    public void writeCache() {
        try {

            final String[] array = cache.array();

            for (int i = 0, length = cache.size(); i < length; i++) {
                out.write(array[i]);
            }

            cache.clear();
            out.flush();

        } catch (final IOException e) {
            LOGGER.warning(e);
        }
    }
}
