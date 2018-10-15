package com.ss.rlib.common.test.util.array;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * The list of tests {@link Array}.
 *
 * @author JavaSaBr
 */
public class ArrayTests {

    @Test
    public void testFastArray() {

        Array<Integer> array = ArrayFactory.asArray(2, 5, 1, 7, 6, 8, 4);

        // sorting
        array.sort(Integer::compareTo);

        Assertions.assertArrayEquals(array.toArray(Integer.class),
                ArrayFactory.toArray(1, 2, 4, 5, 6, 7, 8));

        // performace operations
        UnsafeArray<Integer> unsafe = array.asUnsafe();
        unsafe.prepareForSize(10);
        unsafe.unsafeAdd(3);
        unsafe.unsafeAdd(9);
        unsafe.unsafeAdd(10);

        Assertions.assertEquals(10, array.size());

        // additional API
        Integer first = array.first();
        Integer last = array.last();

        array.slowRemove(1);
        array.fastRemove(1);

        Integer searched = array.findAny(integer -> integer == 2);
        searched = array.findAny(2, (el, arg) -> el == arg);

        array.forEach(5, (el, arg) ->
                System.out.println(el + arg));

        array.forEach(5, 7, (el, firstArg, secondArg) ->
                System.out.println(el + firstArg + secondArg));
    }

    @Test
    public void testAtomicARSWLockArray() {

        ConcurrentArray<Integer> array = ArrayFactory.newConcurrentAtomicARSWLockArray(Integer.class);
        long writeStamp = array.writeLock();
        try {

            array.addAll(ArrayFactory.toArray(9, 8, 7, 6, 5, 4, 3));
            array.sort(Integer::compareTo);

        } finally {
            array.writeUnlock(writeStamp);
        }

        long readStamp = array.readLock();
        try {

            Assertions.assertArrayEquals(array.toArray(Integer.class),
                    ArrayFactory.toArray(3, 4, 5, 6, 7, 8, 9));

            final Integer first = array.first();
            final Integer last = array.last();

            Assertions.assertEquals(3, (int) first);
            Assertions.assertEquals(9, (int) last);

        } finally {
            array.readUnlock(readStamp);
        }

        Integer last = ArrayUtils.getInReadLock(array, Array::last);
        Integer result = ArrayUtils.getInReadLock(array, last,
                (arr, target) -> arr.findAny(target, Integer::equals));

        ArrayUtils.runInWriteLock(array, result + 1, Collection::add);

        Assertions.assertEquals(10, (int) array.last());
    }

    @Test
    public void testArrayCollector() {

        Array<Integer> result = IntStream.range(0, 1000)
                .mapToObj(value -> value)
                .collect(ArrayCollectors.toArray(Integer.class));

        Assertions.assertEquals(1000, result.size());

        ConcurrentArray<Integer> concurrentArray = IntStream.range(0, 1000)
                .parallel()
                .mapToObj(value -> value)
                .collect(ArrayCollectors.toConcurrentArray(Integer.class));

        Assertions.assertEquals(1000, concurrentArray.size());

        Array<Number> numbers = IntStream.range(0, 1000)
                .mapToObj(value -> value)
                .collect(ArrayCollectors.toArray(Number.class));

        Assertions.assertEquals(1000, numbers.size());

        Array<Collection<?>> collections = IntStream.range(0, 1000)
                .mapToObj(value -> new ArrayList<>(1))
                .collect(ArrayCollectors.toArray(Collection.class));

        Assertions.assertEquals(1000, collections.size());
    }

    @Test
    public void testCopyOnModifyArray() {

        Array<Integer> array = ArrayFactory.newCopyOnModifyArray(Integer.class);
        array.addAll(ArrayFactory.toArray(9, 8, 7, 6, 5, 4, 3));
        array.add(7);
        array.addAll(Arrays.asList(5, 6, 7));
        array.addAll(Array.of(6, 7, 8));
        array.sort(Integer::compareTo);

        Assertions.assertArrayEquals(array.toArray(Integer.class),
                ArrayFactory.toArray(3, 4, 5, 5, 6, 6, 6, 7, 7, 7, 7, 8, 8, 9));

        Integer first = array.first();
        Integer last = array.last();

        Assertions.assertEquals(14, array.size());
        Assertions.assertEquals(3, (int) first);
        Assertions.assertEquals(9, (int) last);
    }
}
