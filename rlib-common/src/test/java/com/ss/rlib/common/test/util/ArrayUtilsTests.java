package com.ss.rlib.common.test.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.junit.jupiter.api.Test;

import java.util.Collection;

/**
 * Test of methods in ArrayUtils class.
 *
 * @author JavaSaBr
 */
public class ArrayUtilsTests {

    @Test
    public void testRunInReadLock1() {

        final ConcurrentArray<String> array = ArrayFactory.newConcurrentStampedLockArray(String.class);

        ArrayUtils.runInReadLock(array, strings -> strings.add("Test"));

        assertEquals(0, array.size(), "The size of array should be 0");

        ArrayUtils.runInReadLock(array, strings -> strings.add("Test"));

        assertEquals(0, array.size(), "The size of array should be 0");
    }

    @Test
    public void testRunInReadLock2() {

        final ConcurrentArray<String> array = ArrayFactory.newConcurrentStampedLockArray(String.class);

        ArrayUtils.runInReadLock(array, "test", Collection::add);

        assertEquals(0, array.size(), "The size of array should be 0");

        ArrayUtils.runInReadLock(array, "test2", Collection::add);

        assertEquals(0, array.size(), "The size of array should be 0");
    }

    @Test
    public void testRunInReadLock3() {

        final ConcurrentArray<String> array = ArrayFactory.newConcurrentStampedLockArray(String.class);

        ArrayUtils.runInReadLock(array, "first", "second", (first, second, third) -> first.add(second + third));

        assertEquals(0, array.size(), "The size of array should be 0");

        ArrayUtils.runInReadLock(array, "first2", "second2", (first, second, third) -> first.add(second + third));

        assertEquals(0, array.size(), "The size of array should be 0");
    }

    @Test
    public void testRunInWriteLock1() {

        final ConcurrentArray<String> array = ArrayFactory.newConcurrentStampedLockArray(String.class);

        ArrayUtils.runInWriteLock(array, strings -> strings.add("Test"));

        assertEquals(1, array.size(), "The size of array should be 1");

        ArrayUtils.runInWriteLock(array, strings -> strings.add("Test"));

        assertEquals(2, array.size(), "The size of array should be 1");
    }

    @Test
    public void testRunInWriteLock2() {

        final ConcurrentArray<String> array = ArrayFactory.newConcurrentStampedLockArray(String.class);

        ArrayUtils.runInWriteLock(array, "test", Collection::add);

        assertEquals(1, array.size(), "The size of array should be 1");

        ArrayUtils.runInWriteLock(array, "test2", Collection::add);

        assertEquals(2, array.size(), "The size of array should be 1");
    }

    @Test
    public void testRunInWriteLock3() {

        final ConcurrentArray<String> array = ArrayFactory.newConcurrentStampedLockArray(String.class);

        ArrayUtils.runInWriteLock(array, "first", "second", (first, second, third) -> first.add(second + third));

        assertEquals(1, array.size(), "The size of array should be 1");

        ArrayUtils.runInWriteLock(array, "first2", "second2", (first, second, third) -> first.add(second + third));

        assertEquals(2, array.size(), "The size of array should be 1");
    }

    @Test
    public void testRunInWriteLock4() {

        final ConcurrentArray<String> array = ArrayFactory.newConcurrentStampedLockArray(String.class);

        ArrayUtils.runInWriteLock(array, "first", "second",
                (first, second, third) -> !StringUtils.equals(second, third), (
                        first, second, third) -> first.add(third));

        assertEquals(1, array.size(), "The size of array should be 1");

        ArrayUtils.runInWriteLock(array, "first2", "second2",
                (first, second, third) -> !StringUtils.equals(second, third), (
                        first, second, third) -> first.add(third));

        assertEquals(2, array.size(), "The size of array should be 1");
    }
}
