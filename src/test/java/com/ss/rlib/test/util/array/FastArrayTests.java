package com.ss.rlib.test.util.array;

import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import com.ss.rlib.util.array.UnsafeArray;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Теаст скорости и функционала FastArray.
 *
 * @author JavaSaBr
 */
public class FastArrayTests {

    public void test() {

        final String head = FastArrayTests.class.getSimpleName() + ": ";

        System.out.println(head + "start test FastArray VS ArrayList...");

        testImpl(head);
        testImpl(head);
        testImpl(head);
    }

    private void testImpl(final String head) {

        System.gc();

        final Array<Integer> array = ArrayFactory.newArray(Integer.class);
        final UnsafeArray<Integer> array1 = array.asUnsafe();

        final Array<Integer> added = ArrayFactory.newArray(Integer.class);
        final UnsafeArray<Integer> array2 = added.asUnsafe();

        for (int i = 999_000, length = 1_000_000; i < length; i++) {
            added.add(i);
        }

        final List<Integer> list = new ArrayList<>();

        long time = System.currentTimeMillis();

        array1.prepareForSize(1_000_000);

        for (int i = 0, length = 1_000_000; i < length; i++) {
            array1.unsafeAdd(i);
        }

        System.out.println(head + "test add to FastArray " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();

        for (int i = 0, length = 1_000_000; i < length; i++) {
            list.add(i);
        }

        System.out.println(head + "test add to ArrayList " + (System.currentTimeMillis() - time));

        @SuppressWarnings("unused")
        int count = 0;

        time = System.currentTimeMillis();

        for (final Integer val : array.array()) {

            if (val == null) {
                break;
            }

            count += val.intValue();
        }

        System.out.println(head + "test iterate to FastArray " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();

        for (final Integer val : list) {
            count += val.intValue();
        }

        System.out.println(head + "test iterate to ArrayList " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();

        for (int i = 5000, length = 7000; i < length; i++) {
            array.fastRemove(Integer.valueOf(i));
        }

        System.out.println(head + "test fast remove to FastArray " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();

        for (int i = 5000, length = 7000; i < length; i++) {
            list.remove(Integer.valueOf(i));
        }

        System.out.println(head + "test remove to ArrayList " + (System.currentTimeMillis() - time));

        array.clear();
        array.add(1);
        array.add(1);
        array.add(1);
        array.add(2);
        array.addAll(added);
        array.addAll(array2.trimToSize().array());

        Assertions.assertTrue(array.size() == added.size() * 2 + 4);

        for (int i = 0; i < array.size(); i++) {
            array.get(i).intValue();
        }

        System.out.println(head + "====================");
    }
}
