package javasabr.rlib.common.util.dictionary;

import javasabr.rlib.common.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConcurrentObjectDictionaryTest extends BaseTest {

    @Test
    void runInWriteLockTest() {

        var dictionary = ConcurrentObjectDictionary.ofType(
            String.class,
            Integer.class
        );

        Assertions.assertEquals(0, dictionary.size());

        dictionary.runInWriteLock(dic -> {
            dic.put("1", 1);
            dic.put("2", 2);
            dic.put("3", 3);
        });

        Assertions.assertEquals(3, dictionary.size());

        dictionary.runInWriteLock("4", (dic, arg) -> {
            assertType(dic, ConcurrentObjectDictionary.class);
            assertType(arg, String.class);
            dic.put(arg, Integer.valueOf(arg));
        });

        Assertions.assertEquals(4, dictionary.size());

        dictionary.runInWriteLock("5", 5, ObjectDictionary::put);

        Assertions.assertEquals(5, dictionary.size());

        dictionary.runInWriteLock("6", 6, (dic, arg1, arg2) -> {
            assertType(dic, ConcurrentObjectDictionary.class);
            assertType(arg1, String.class);
            assertType(arg2, Integer.class);
        });
    }

    @Test
    void runInReadLockTest() {

        var dictionary = ConcurrentObjectDictionary.ofType(
            String.class,
            Integer.class
        );

        dictionary.runInReadLock("4", (dic, arg) -> {
            assertType(dic, ConcurrentObjectDictionary.class);
            assertType(arg, String.class);
            dic.get(arg);
        });
    }

    @Test
    void getInWriteLockTest() {

        var dictionary = ConcurrentObjectDictionary.ofType(
            String.class,
            Integer.class
        );

        dictionary.runInWriteLock(dic -> {
            dic.put("1", 1);
            dic.put("2", 2);
            dic.put("3", 3);
        });

        Integer val1 = dictionary.getInWriteLock("1", (dic, arg) -> {
            assertType(dic, ConcurrentObjectDictionary.class);
            assertType(arg, String.class);
            return dic.get(arg);
        });

        Assertions.assertEquals(1, val1);

        Integer val2 = dictionary.getInWriteLock("2", Type1.EXAMPLE, (dic, arg1, arg2) -> {
            assertType(dic, ConcurrentObjectDictionary.class);
            assertType(arg1, String.class);
            assertType(arg2, Type1.class);
            return dic.get(arg1);
        });

        Assertions.assertEquals(2, val2);
    }

    @Test
    void getInReadLockTest() {

        var dictionary = ConcurrentObjectDictionary.ofType(
            String.class,
            Integer.class
        );

        dictionary.runInWriteLock(dic -> {
            dic.put("1", 1);
            dic.put("2", 2);
            dic.put("3", 3);
        });

        Integer val1 = dictionary.getInReadLock("1", (dic, arg) -> {
            assertType(dic, ConcurrentObjectDictionary.class);
            assertType(arg, String.class);
            return dic.get(arg);
        });

        Assertions.assertEquals(1, val1);

        Integer val2 = dictionary.getInReadLock("2", Type1.EXAMPLE, (dic, arg1, arg2) -> {
            assertType(dic, ConcurrentObjectDictionary.class);
            assertType(arg1, String.class);
            assertType(arg2, Type1.class);
            return dic.get(arg1);
        });

        Assertions.assertEquals(2, val2);
    }
}
