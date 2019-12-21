package com.ss.rlib.common.test.util.array;

import static org.junit.jupiter.api.Assertions.*;
import com.ss.rlib.common.concurrent.atomic.AtomicInteger;
import com.ss.rlib.common.test.BaseTest;
import com.ss.rlib.common.util.NumberUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Objects;

/**
 * @author JavaSaBr
 */
public class ConcurrentArrayTest extends BaseTest {

    @Test
    void addTest() {

        var array = ConcurrentArray.ofType(String.class);

        assertEquals(0, array.size());

        array.add("First");

        assertEquals(1, array.size());
        assertEquals(Array.of("First"), array);
        assertEquals(array, Array.of("First"));

        array.addAll(ArrayFactory.toArray("Second", "Third"));

        assertEquals(3, array.size());
        assertEquals(Array.of("First", "Second", "Third"), array);

        array.addAll(ArrayFactory.asArray("Fourth", "Fifth"));

        assertEquals(5, array.size());
        assertEquals(Array.of("First", "Second", "Third", "Fourth", "Fifth"), array);
    }

    @Test
    void removeTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "Fourth");

        assertEquals(4, array.size());

        array.remove("Second");

        assertEquals(3, array.size());
        assertEquals(Array.of("First", "Third", "Fourth"), array);

        array.removeAll(ArrayFactory.asArray("First", "Fourth"));

        assertEquals(1, array.size());
        assertEquals(Array.of("Third"), array);

        array = ConcurrentArray.of("First", "Second", "Third", "Fourth");

        assertEquals(4, array.size());

        array.remove(2);

        assertEquals(3, array.size());
        assertEquals(Array.of("First", "Second", "Fourth"), array);

        array.removeAll(Collections.singletonList("First"));

        assertEquals(2, array.size());
        assertEquals(Array.of("Second", "Fourth"), array);
    }

    @Test
    void fastRemoveTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "Fourth");

        assertEquals(4, array.size());

        array.fastRemove("Second");

        assertEquals(3, array.size());
        assertEquals(Array.of("First", "Fourth", "Third"), array);

        array.fastRemoveAll(ArrayFactory.asArray("First", "Fourth"));

        assertEquals(1, array.size());
        assertEquals(Array.of("Third"), array);

        array = ConcurrentArray.of("First", "Second", "Third", "Fourth");

        assertEquals(4, array.size());

        array.fastRemove(1);

        assertEquals(3, array.size());
        assertEquals(Array.of("First", "Fourth", "Third"), array);

        array.fastRemoveAll(ArrayFactory.toArray("First", "Third"));

        assertEquals(1, array.size());
        assertEquals(Array.of("Fourth"), array);
    }

    @Test
    void removeIfInWriteLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ");

        assertTrue(array.removeIfInWriteLock(String::isBlank));
        assertEquals(3, array.size());

        assertFalse(array.removeIfInWriteLock(Type1.EXAMPLE, (arg, element) -> {
            assertType(arg, Type1.class);
            assertType(element, String.class);
            return false;
        }));

        assertTrue(array.removeIfInWriteLock("Third", String::equals));
        assertEquals(2, array.size());

        array.add("Third");

        assertEquals(3, array.size());

        assertFalse(array.removeIfInWriteLock(
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

        assertTrue(array.removeIfInWriteLock(
            "Second",
            String::hashCode,
            (first, second) -> first.equals(second.hashCode())
        ));

        assertEquals(2, array.size());

        array = ConcurrentArray.of("10", "5", "2", "1");

        assertTrue(array.removeIfConvertedInWriteLock(
            5,
            Integer::parseInt,
            Integer::equals
        ));

        assertEquals(3, array.size());
    }

    @Test
    void anyMatchInReadLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ");

        assertFalse(array.anyMatchInReadLock(Type1.EXAMPLE, (arg, element) -> {
            assertType(arg, Type1.class);
            assertType(element, String.class);
            return false;
        }));

        assertTrue(array.anyMatchInReadLock("Second", String::equals));
        assertFalse(array.anyMatchInReadLock("None", String::equals));

        assertFalse(array.anyMatchInReadLock("None".hashCode(), (arg, element) -> {
            assertIntType(arg);
            assertType(element, String.class);
            return arg == element.hashCode();
        }));

        assertTrue(array.anyMatchInReadLock("Second".hashCode(), (val, string) -> val == string.hashCode()));
        assertFalse(array.anyMatchInReadLock("None".hashCode(), (val, string) -> val == string.hashCode()));

        assertTrue(array.anyMatchConvertedInReadLock(
            "Second".hashCode(),
            String::hashCode,
            Objects::equals
        ));

        assertFalse(array.anyMatchConvertedInReadLock(
            "None".hashCode(),
            String::hashCode,
            Objects::equals
        ));
    }

    @Test
    void findAnyInReadLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ");

        assertNull(array.findAnyInReadLock("None".hashCode(), (arg, element) -> {
            assertIntType(arg);
            assertType(element, String.class);
            return arg == element.hashCode();
        }));

        assertNotNull(array.findAnyInReadLock("Second".hashCode(), (val, string) -> val == string.hashCode()));
        assertNull(array.findAnyInReadLock("None".hashCode(), (val, string) -> val == string.hashCode()));

        assertNotNull(array.findAnyConvertedToIntInReadLock(
            "First".hashCode(),
            String::hashCode,
            NumberUtils::equals
        ));

        assertNotNull(array.findAnyConvertedInReadLock(
            "First".hashCode(),
            String::hashCode,
            Objects::equals
        ));

        Assertions.assertNotNull(array.findAnyConvertedToIntInReadLock(
            "MyValue".hashCode(),
            object -> "MyValue",
            String::hashCode,
            NumberUtils::equals
        ));

        Assertions.assertNull(array.findAnyConvertedToIntInReadLock(
            "MyValue".hashCode(),
            object -> "First",
            String::hashCode,
            NumberUtils::equals
        ));
    }

    @Test
    void runInWriteLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ");

        array.runInWriteLock(object -> object.remove("Second"));

        assertEquals(3, array.size());

        array.runInWriteLock(Type1.EXAMPLE, (arr, arg) -> {
            assertType(arr, Array.class);
            assertType(arg, Type1.class);
        });

        array.runInWriteLock("Third", Array::remove);

        assertEquals(2, array.size());
    }

    @Test
    void runInReadLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ", "Third");

        array.runInReadLock(arr -> assertEquals(2, arr.count(el -> el.equals("Third"))));

        array.runInReadLock("Third", (arr, arg) -> {
            assertType(arr, Array.class);
            assertType(arg, String.class);
            assertEquals(2, arr.count(arg, String::equals));
        });

        array.runInReadLock("Third", (arr, arg) ->
            assertEquals(2, arr.count(arg, String::equals)));

        array.runInReadLock(2, "Third",  (arr, first, second) -> {
            assertType(arr, Array.class);
            assertIntType(first);
            assertType(second, String.class);
            assertEquals(first, arr.count(second, String::equals));
        });
    }

    @Test
    void getInWriteLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ", "Third");

        assertEquals("Third", array.getInWriteLock(arr -> arr.get(2)));
        assertEquals("Second", array.getInWriteLock(1, Array::get));
        assertEquals(
            "FirstSecond",
            array.getInWriteLock(0, 1, (arr, first, second) -> arr.get(first) + arr.get(second))
        );
    }

    @Test
    void getInReadLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ", "Third");

        assertEquals("Third", array.getInReadLock(arr -> arr.get(2)));
        assertEquals("Second", array.getInReadLock(1, Array::get));
        assertEquals(
            "FirstSecond",
            array.getInReadLock(0, 1, (arr, first, second) -> arr.get(first) + arr.get(second))
        );
    }

    @Test
    void forEachInReadLockTest() {

        var array = ConcurrentArray.of("First", "Second", "Third", "  ", "Third");
        var counter = new AtomicInteger(0);

        array.forEachInReadLock(element -> counter.incrementAndGet());

        assertEquals(array.size(), counter.getAndSet(0));

        array.forEachInReadLock(Type1.EXAMPLE, (arg, element) -> {
            assertType(arg, Type1.class);
            assertType(element, String.class);
            counter.incrementAndGet();
        });

        assertEquals(array.size(), counter.getAndSet(0));

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

        assertEquals(array.size(), counter.getAndSet(0));

        array.forEachConvertedInReadLock(Type1.EXAMPLE, String::hashCode, (arg, element) -> {
            assertType(arg, Type1.class);
            assertIntType(element);
            counter.incrementAndGet();
        });

        assertEquals(array.size(), counter.getAndSet(0));

        array.forEachConvertedInReadLock(Type1.EXAMPLE, Type2.EXAMPLE, String::hashCode, (arg1, arg2, element) -> {
            assertType(arg1, Type1.class);
            assertType(arg2, Type2.class);
            assertIntType(element);
            counter.incrementAndGet();
        });

        assertEquals(array.size(), counter.getAndSet(0));

        array.forEachInReadLock(0, Type1.EXAMPLE, (arg1, arg2, element) -> {
            assertIntType(arg1);
            assertType(arg2, Type1.class);
            assertType(element, String.class);
            counter.incrementAndGet();
        });

        assertEquals(array.size(), counter.getAndSet(0));

        array.forEachInReadLock(Type1.EXAMPLE, Type2.EXAMPLE, (arg1, arg2, element) -> {
            assertType(arg1, Type1.class);
            assertType(arg2, Type2.class);
            assertType(element, String.class);
            counter.incrementAndGet();
        });

        assertEquals(array.size(), counter.getAndSet(0));
    }
}
