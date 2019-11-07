package com.ss.rlib.common.test.util.array;

import com.ss.rlib.common.util.array.ConcurrentArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class ConcurrentArrayTest {

    @Test
    void removeIfInWriteLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ");

        Assertions.assertTrue(array.removeIfInWriteLock(String::isBlank));
        Assertions.assertEquals(3, array.size());

        Assertions.assertTrue(array.removeIfInWriteLock("Third", String::equals));
        Assertions.assertEquals(2, array.size());

        array.add("Third");

        Assertions.assertEquals(3, array.size());

        Assertions.assertTrue(array.removeIfInWriteLock(
            "Second",
            String::hashCode,
            (first, second) -> first.equals(second.hashCode())
        ));

        Assertions.assertEquals(2, array.size());
    }

    @Test
    void anyMatchInReadLock() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ");

        Assertions.assertNotNull(array.anyMatchInReadLock("Second", String::equals));
        Assertions.assertNull(array.anyMatchInReadLock("None", String::equals));

        Assertions.assertNotNull(array.anyMatchInReadLock("Second".hashCode(), (str, val) -> str.hashCode() == val));
        Assertions.assertNull(array.anyMatchInReadLock("None".hashCode(), (str, val) -> str.hashCode() == val));
    }
}
