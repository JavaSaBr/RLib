package com.ss.rlib.common.test.util.array;

import com.ss.rlib.common.concurrent.atomic.AtomicInteger;
import com.ss.rlib.common.test.BaseTest;
import com.ss.rlib.common.util.NumberUtils;
import com.ss.rlib.common.util.array.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * The list of tests {@link Array}.
 *
 * @author JavaSaBr
 */
public class ArrayTest extends BaseTest {

    @Test
    void ofTest() {

        var array = ArrayFactory.asArray("First", "Second", "Third", "  ");
        array.add("Temp");
        array.remove("Temp");

        var copy = Array.of(array);

        Assertions.assertEquals(array, copy);

        var array2 = Array.of("First", "Second", "Third", "  ");

        Assertions.assertEquals(array, array2);

        var single = ArrayFactory.asArray("First");
        var single2 = Array.of("First");

        Assertions.assertEquals(single, single2);
    }

    @Test
    void removeIfTest() {

        var array = ArrayFactory.asArray("First", "Second", "Third", "  ");

        Assertions.assertTrue(array.removeIf(String::isBlank));
        Assertions.assertEquals(3, array.size());

        Assertions.assertFalse(array.removeIf(Type1.EXAMPLE, (arg, element) -> {
            assertType(arg, Type1.class);
            assertType(element, String.class);
            return false;
        }));

        Assertions.assertTrue(array.removeIf("Third", String::equals));
        Assertions.assertEquals(2, array.size());

        array.add("Third");

        Assertions.assertEquals(3, array.size());

        Assertions.assertFalse(array.removeIf(
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

        Assertions.assertTrue(array.removeIf(
            "Second",
            String::hashCode,
            (first, second) -> first.equals(second.hashCode())
        ));

        Assertions.assertEquals(2, array.size());

        array = ArrayFactory.asArray("10", "5", "2", "1");

        Assertions.assertTrue(array.removeIfConverted(
            5,
            Integer::parseInt,
            Integer::equals
        ));

        Assertions.assertEquals(3, array.size());
    }

    @Test
    void toArrayTest() {

        var array = ArrayFactory.asArray("First", "Second", "Third", "  ");

        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "Second", "Third", "  "),
            array.toArray()
        );

        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "Second", "Third", "  "),
            array.toArray(String.class)
        );

        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "Second", "Third", "  "),
            array.toArray(new String[0])
        );

        //noinspection ToArrayCallWithZeroLengthArrayArgument
        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "Second", "Third", "  "),
            array.toArray(new String[array.size()])
        );
    }

    @Test
    void removeTest() {

        var array = ArrayFactory.asArray("First", "Second", "Third", "  ");

        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "Second", "Third", "  "),
            array.toArray()
        );

        array.remove("Second");

        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "Third", "  "),
            array.toArray()
        );

        array.remove("  ");

        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "Third"),
            array.toArray()
        );

        array.remove("First");

        Assertions.assertArrayEquals(
            ArrayFactory.toArray("Third"),
            array.toArray()
        );

        array = ArrayFactory.asArray("First", "Second", "Third", "  ");

        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "Second", "Third", "  "),
            array.toArray()
        );

        array.remove(1);

        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "Third", "  "),
            array.toArray()
        );

        array.remove(2);

        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "Third"),
            array.toArray()
        );

        array.remove(0);

        Assertions.assertArrayEquals(
            ArrayFactory.toArray("Third"),
            array.toArray()
        );
    }

    @Test
    void findAnyTest() {

        var array = ArrayFactory.asArray("First", "Second", "Third", "  ");

        Assertions.assertNotNull(array.findAny("Second"::equals));

        Assertions.assertNull(array.findAny(Type1.EXAMPLE, (arg, element) -> {
            assertType(arg, Type1.class);
            assertType(element, String.class);
            return false;
        }));

        Assertions.assertNotNull(array.findAny("Third", String::equals));

        Assertions.assertNull(array.findAnyR(Type1.EXAMPLE, (element, arg) -> {
            assertType(element, String.class);
            assertType(arg, Type1.class);
            return false;
        }));

        Assertions.assertNotNull(array.findAnyR("Third", String::equals));

        Assertions.assertNull(array.findAny(Type1.EXAMPLE, Type2.EXAMPLE, (arg1, arg2, element) -> {
            assertType(arg1, Type1.class);
            assertType(arg2, Type2.class);
            assertType(element, String.class);
            return false;
        }));

        Assertions.assertNotNull(array.findAny("First", "Second",
            (first, second, element) -> first.equals(element) || second.equals(element)));

        Assertions.assertNotNull(array.findAny("First".hashCode(), (num, element) -> {
            assertIntType(num);
            assertType(element, String.class);
            return num == element.hashCode();
        }));

        Assertions.assertNotNull(array.findAny("First".hashCode(), (num, element) -> num == element.hashCode()));

        Assertions.assertNotNull(array.findAnyL("First".hashCode(), (num, element) -> {
            assertLongType(num);
            assertType(element, String.class);
            return num == element.hashCode();
        }));

        Assertions.assertNotNull(array.findAnyL("First".hashCode(), (num, element) -> num == element.hashCode()));

        Assertions.assertNotNull(array.findAnyConvertedToInt(
            "First".hashCode(),
            String::hashCode,
            NumberUtils::equals
        ));

        Assertions.assertNotNull(array.findAnyConvertedToInt(
            "MyValue".hashCode(),
            object -> "MyValue",
            String::hashCode,
            NumberUtils::equals
        ));

        Assertions.assertNull(array.findAnyConvertedToInt(
            "MyValue".hashCode(),
            object -> "First",
            String::hashCode,
            NumberUtils::equals
        ));

        Assertions.assertNotNull(array.findAnyConverted(
            "First".hashCode(),
            String::hashCode,
            Objects::equals
        ));
    }

    @Test
    void anyMatchTest() {

        var array = ArrayFactory.asArray("First", "Second", "Third", "  ");

        Assertions.assertFalse(array.anyMatch(Type1.EXAMPLE, (arg, element) -> {
            assertType(arg, Type1.class);
            assertType(element, String.class);
            return false;
        }));

        Assertions.assertTrue(array.anyMatch("Second", String::equals));
        Assertions.assertFalse(array.anyMatch("None", String::equals));

        Assertions.assertFalse(array.anyMatch("None".hashCode(), (arg, element) -> {
            assertIntType(arg);
            assertType(element, String.class);
            return arg == element.hashCode();
        }));

        Assertions.assertTrue(array.anyMatch("Second".hashCode(), (num, element) -> num == element.hashCode()));
        Assertions.assertFalse(array.anyMatch("None".hashCode(), (num, element) -> num == element.hashCode()));

        Assertions.assertFalse(array.anyMatchR(Type1.EXAMPLE, (element, arg) -> {
            assertType(element, String.class);
            assertType(arg, Type1.class);
            return false;
        }));

        Assertions.assertTrue(array.anyMatchR("Second".hashCode(), (element, num) -> element.hashCode() == num));
        Assertions.assertFalse(array.anyMatchR("None".hashCode(), (element, num) -> element.hashCode() == num));

        Assertions.assertTrue(array.anyMatchConverted("Second".hashCode(), String::hashCode, Objects::equals));
        Assertions.assertFalse(array.anyMatchConverted("None".hashCode(), String::hashCode, Objects::equals));
    }

    @Test
    void replaceTest() {

        var array = ArrayFactory.asArray("First", "Second", "Third", "  ");

        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "Second", "Third", "  "),
            array.toArray()
        );

        array.replace(1, "NotSecond");

        Assertions.assertEquals(4, array.size());
        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "NotSecond", "Third", "  "),
            array.toArray()
        );

        array.replace(3, "NotEmpty");

        Assertions.assertEquals(4, array.size());
        Assertions.assertArrayEquals(
            ArrayFactory.toArray("First", "NotSecond", "Third", "NotEmpty"),
            array.toArray()
        );
    }

    @Test
    void countTest() {

        var array = ArrayFactory.asArray("First", "Second", "First", "Third");

        Assertions.assertEquals(2, array.count("First"::equals));
        Assertions.assertEquals(1, array.count("Second"::equals));

        Assertions.assertEquals(2, array.count("First", String::equals));
        Assertions.assertEquals(1, array.count("Second", String::equals));

        Assertions.assertEquals(2, array.countR("First", String::equals));
        Assertions.assertEquals(1, array.countR("Second", String::equals));
    }

    @Test
    void forEachTest() {

        var array = Array.of("First", "Second", "Third", "  ", "Third");
        var counter = new AtomicInteger(0);

        array.forEach(element -> counter.incrementAndGet());

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEachFiltered("First"::equals, el -> counter.incrementAndGet());

        Assertions.assertEquals(1, counter.getAndSet(0));

        array.forEach(Type1.EXAMPLE, (arg, element) -> {
            assertType(arg, Type1.class);
            assertType(element, String.class);
            counter.incrementAndGet();
        });

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEachR(Type1.EXAMPLE, (element, arg) -> {
            assertType(arg, Type1.class);
            assertType(element, String.class);
            counter.incrementAndGet();
        });

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEachConverted(Type1.EXAMPLE, String::getBytes, (arg, converted) -> {
            assertType(arg, Type1.class);
            assertType(converted, byte[].class);
            counter.incrementAndGet();
        });

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEachConverted(Type1.EXAMPLE, Type2.EXAMPLE, String::getBytes, (arg1, arg2, converted) -> {
            assertType(arg1, Type1.class);
            assertType(arg2, Type2.class);
            assertType(converted, byte[].class);
            counter.incrementAndGet();
        });

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEach("Arg", String::getBytes, (arg, element) -> {
            assertType(arg, byte[].class);
            assertType(element, String.class);
            counter.incrementAndGet();
        });

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEachFiltered(Type1.EXAMPLE,
            (arg, element) -> {
                assertType(arg, Type1.class);
                assertType(element, String.class);
                return "Second".equals(element);
            },
            (arg, element) -> {
                assertType(arg, Type1.class);
                assertType(element, String.class);
                counter.incrementAndGet();
            }
        );

        Assertions.assertEquals(1, counter.getAndSet(0));

        array.forEach(Type1.EXAMPLE, Type2.EXAMPLE, (arg1, arg2, element) -> {
            assertType(arg1, Type1.class);
            assertType(arg2, Type2.class);
            assertType(element, String.class);
            counter.incrementAndGet();
        });

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEachR(Type1.EXAMPLE, Type2.EXAMPLE, (element, arg1, arg2) -> {
            assertType(element, String.class);
            assertType(arg1, Type1.class);
            assertType(arg2, Type2.class);
            counter.incrementAndGet();
        });

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEachFiltered(
            Type1.EXAMPLE,
            Type2.EXAMPLE,
            (arg1, arg2, element) -> {
                assertType(arg1, Type1.class);
                assertType(arg2, Type2.class);
                assertType(element, String.class);
                return "First".equals(element) || "Second".equals(element);
            },
            (arg1, arg2, element) -> {
                assertType(arg1, Type1.class);
                assertType(arg2, Type2.class);
                assertType(element, String.class);
                counter.incrementAndGet();
            }
        );

        Assertions.assertEquals(2, counter.getAndSet(0));

        array.forEachL(1L, Type1.EXAMPLE, (arg1, arg2, element) -> {
            assertLongType(arg1);
            assertType(arg2, Type1.class);
            assertType(element, String.class);
            counter.incrementAndGet();
        });

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEachF(1F, Type1.EXAMPLE, (arg1, arg2, element) -> {
            assertFloatType(arg1);
            assertType(arg2, Type1.class);
            assertType(element, String.class);
            counter.incrementAndGet();
        });

        Assertions.assertEquals(array.size(), counter.getAndSet(0));

        array.forEach(0, Type2.EXAMPLE, (arg1, arg2, element) -> {
            assertIntType(arg1);
            assertType(arg2, Type2.class);
            assertType(element, String.class);
            counter.incrementAndGet();
        });

        Assertions.assertEquals(array.size(), counter.getAndSet(0));
    }

    @Test
    void fastRemoveAllTest() {

        var array = ArrayFactory.asArray("First", "Second", "Third", "  ", "55", "66", "22", "22", "11");
        var toRemove = ArrayFactory.asArray("First", "Third", "66", "22");
        var result = ArrayFactory.asArray("Second", "  ", "55", "11");
        result.sort(String::compareTo);

        array.fastRemoveAll(toRemove);
        array.sort(String::compareTo);

        Assertions.assertEquals(result, array);
    }

    @Test
    void copyToTest() {

        var toCopy = ArrayFactory.asArray("123", "321", "555");
        var toCombine = ArrayFactory.asArray("First", "Second");
        var result = ArrayFactory.asArray("First", "Second", "123", "321", "555");

        toCopy.copyTo(toCombine);

        Assertions.assertEquals(result, toCombine);
    }
}
