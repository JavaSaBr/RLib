package javasabr.rlib.common.util.dictionary;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javasabr.rlib.common.util.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class ConcurrentStampedLockObjectDictionaryTest {

    @Test
    void writeLockTest() {

        var dictionary = DictionaryFactory.<String, Integer>newConcurrentStampedLockObjectDictionary();
        dictionary.runInWriteLock(dic -> {
            dic.put("Key1", 1);
            dic.put("Key2", 2);
            dic.put("Key3", 3);
            dic.put("Key4", 4);
        });

        long stamp = dictionary.writeLock();

        Assertions.assertNotEquals(0, stamp);

        var pendingWrite = CompletableFuture.runAsync(() -> dictionary.runInWriteLock(dic -> dic.put("Key5", 5)));

        Utils.tryGet(() -> pendingWrite.get(10, TimeUnit.MILLISECONDS));

        Assertions.assertEquals(4, dictionary.size());

        dictionary.writeUnlock(stamp);

        pendingWrite.join();

        Assertions.assertEquals(5, dictionary.size());

        stamp = dictionary.readLock();

        Assertions.assertNotEquals(0, stamp);

        var pendingWrite2 = CompletableFuture.runAsync(() -> dictionary.runInWriteLock(dic -> dic.put("Key6", 6)));

        Utils.tryGet(() -> pendingWrite2.get(10, TimeUnit.MILLISECONDS));

        Assertions.assertEquals(5, dictionary.size());

        dictionary.readUnlock(stamp);

        pendingWrite2.join();

        Assertions.assertEquals(6, dictionary.size());
    }

    @Test
    void writeReadLockTest() {

        var dictionary = DictionaryFactory.<String, Integer>newConcurrentStampedLockObjectDictionary();
        dictionary.runInWriteLock(dic -> {
            dic.put("Key1", 1);
            dic.put("Key2", 2);
            dic.put("Key3", 3);
            dic.put("Key4", 4);
        });

        long stamp = dictionary.readLock();

        Assertions.assertNotEquals(0, stamp);

        var asyncRead = CompletableFuture.supplyAsync(() -> dictionary.getInReadLock("Key1", ObjectDictionary::get));
        var result = Utils.uncheckedGet(() -> asyncRead.get(100, TimeUnit.MILLISECONDS));

        Assertions.assertEquals(dictionary.get("Key1"), result);

        dictionary.readUnlock(stamp);
    }
}
