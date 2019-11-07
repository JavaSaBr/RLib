package com.ss.rlib.common.test.util.dictionary;

import com.ss.rlib.common.test.BaseTest;
import com.ss.rlib.common.util.dictionary.ConcurrentObjectDictionary;
import com.ss.rlib.common.util.dictionary.ObjectDictionary;
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
}
