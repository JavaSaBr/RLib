package com.ss.rlib.common.test.util.array;

import com.ss.rlib.common.concurrent.atomic.AtomicInteger;
import com.ss.rlib.common.test.BaseTest;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class ConcurrentArrayTest extends BaseTest {

    @Test
    void removeIfInWriteLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ");

        Assertions.assertTrue(array.removeIfInWriteLock(String::isBlank));
        Assertions.assertEquals(3, array.size());

        Assertions.assertFalse(array.removeIfInWriteLock(Type1.EXAMPLE, (arg, element) -> {
            assertType(arg, Type1.class);
            assertType(element, String.class);
            return false;
        }));

        Assertions.assertTrue(array.removeIfInWriteLock("Third", String::equals));
        Assertions.assertEquals(2, array.size());

        array.add("Third");

        Assertions.assertEquals(3, array.size());

        Assertions.assertFalse(array.removeIfInWriteLock(
            Type1.EXAMPLE,
            arg -> {
                assertType(arg, Type1.class);
                return arg;
            },
            (arg, element) -> {
                assertType(arg, Type1.class);
                assertType(element, String.class);
                return false;
            }
        ));

        Assertions.assertTrue(array.removeIfInWriteLock(
            "Second",
            String::hashCode,
            (first, second) -> first.equals(second.hashCode())
        ));

        Assertions.assertEquals(2, array.size());
    }

    @Test
    void anyMatchInReadLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ");

        Assertions.assertFalse(array.anyMatchInReadLock(Type1.EXAMPLE, (arg, element) -> {
            assertType(arg, Type1.class);
            assertType(element, String.class);
            return false;
        }));

        Assertions.assertTrue(array.anyMatchInReadLock("Second", String::equals));
        Assertions.assertFalse(array.anyMatchInReadLock("None", String::equals));

        Assertions.assertFalse(array.anyMatchInReadLock("None".hashCode(), (arg, element) -> {
            assertIntType(arg);
            assertType(element, String.class);
            return arg == element.hashCode();
        }));

        Assertions.assertTrue(array.anyMatchInReadLock("Second".hashCode(), (val, string) -> val == string.hashCode()));
        Assertions.assertFalse(array.anyMatchInReadLock("None".hashCode(), (val, string) -> val == string.hashCode()));
    }

    @Test
    void findAnyInReadLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ");

        Assertions.assertNull(array.findAnyInReadLock("None".hashCode(), (arg, element) -> {
            assertIntType(arg);
            assertType(element, String.class);
            return arg == element.hashCode();
        }));

        Assertions.assertNotNull(array.findAnyInReadLock("Second".hashCode(), (val, string) -> val == string.hashCode()));
        Assertions.assertNull(array.findAnyInReadLock("None".hashCode(), (val, string) -> val == string.hashCode()));
    }

    @Test
    void runInWriteLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ");

        array.runInWriteLock(object -> object.remove("Second"));

        Assertions.assertEquals(3, array.size());

        array.runInWriteLock(Type1.EXAMPLE, (arr, arg) -> {
            assertType(arr, Array.class);
            assertType(arg, Type1.class);
        });

        array.runInWriteLock("Third", Array::remove);

        Assertions.assertEquals(2, array.size());
    }

    @Test
    void runInReadLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ", "Third");

        array.runInReadLock(arr -> Assertions.assertEquals(2, arr.count(el -> el.equals("Third"))));

        array.runInReadLock("Third", (arr, arg) -> {
            assertType(arr, Array.class);
            assertType(arg, String.class);
            Assertions.assertEquals(2, arr.count(arg, String::equals));
        });

        array.runInReadLock("Third", (arr, arg) ->
            Assertions.assertEquals(2, arr.count(arg, String::equals)));
    }

    @Test
    void getInWriteLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ", "Third");

        Assertions.assertEquals("Third", array.getInWriteLock(arr -> arr.get(2)));
        Assertions.assertEquals("Second", array.getInWriteLock(1, Array::get));
    }

    @Test
    void getInReadLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ", "Third");

        Assertions.assertEquals("Third", array.getInReadLock(arr -> arr.get(2)));
        Assertions.assertEquals("Second", array.getInReadLock(1, Array::get));
    }

    @Test
    void forEachInReadLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ", "Third");
        var counter = new AtomicInteger(0);

        array.forEachInReadLock(element -> counter.incrementAndGet());

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEachInReadLock(Type1.EXAMPLE, (arg, element) -> {
            assertType(arg, Type1.class);
            assertType(element, String.class);
            counter.incrementAndGet();
        });

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEachInReadLock(
            Type1.EXAMPLE,
            arg -> {
                assertType(arg, Type1.class);
                return arg;
            },
            (arg, element) -> {
                assertType(arg, Type1.class);
                assertType(element, String.class);
                counter.incrementAndGet();
            }
        );

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEachConvertedInReadLock(Type1.EXAMPLE, String::hashCode, (arg, element) -> {
            assertType(arg, Type1.class);
            assertIntType(element);
            counter.incrementAndGet();
        });

        Assertions.assertEquals(array.size(), counter.getAndSet(0));
    }
}
