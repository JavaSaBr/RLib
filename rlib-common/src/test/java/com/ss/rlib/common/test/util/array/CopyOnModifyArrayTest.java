package com.ss.rlib.common.test.util.array;

import static com.ss.rlib.common.util.array.ArrayFactory.asArray;
import static com.ss.rlib.common.util.array.ArrayFactory.toArray;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CopyOnModifyArrayTest {

    @Test
    void addTest() {

        var array = ArrayFactory.newCopyOnModifyArray(String.class);
        array.add("First");

        Assertions.assertEquals(1, array.size());

        array.add("Second");

        Assertions.assertEquals(2, array.size());

        array.add("Third");

        Assertions.assertEquals(3, array.size());
        Assertions.assertArrayEquals(toArray("First", "Second", "Third"), array.array());
        Assertions.assertEquals("Second", array.get(1));
    }

    @Test
    void addArrayTest() {

        var array = ArrayFactory.newCopyOnModifyArray(String.class);
        array.add("First");
        array.add("Second");
        array.addAll(Array.of("Third", "Fourth"));

        Assertions.assertEquals(4, array.size());
        Assertions.assertArrayEquals(toArray("First", "Second", "Third", "Fourth"), array.array());
        Assertions.assertEquals("Second", array.get(1));

        array.addAll(Array.of("Fifth", "Six"));

        Assertions.assertEquals(6, array.size());
        Assertions.assertEquals("Fifth", array.get(4));
    }

    @Test
    void addCollectionTest() {

        var array = ArrayFactory.newCopyOnModifyArray(String.class);
        array.add("First");
        array.add("Second");
        array.addAll(List.of("Third", "Fourth"));

        Assertions.assertEquals(4, array.size());
        Assertions.assertArrayEquals(toArray("First", "Second", "Third", "Fourth"), array.array());
        Assertions.assertEquals("Second", array.get(1));

        array.addAll(List.of("Fifth", "Six"));

        Assertions.assertEquals(6, array.size());
        Assertions.assertEquals("Fifth", array.get(4));
    }

    @Test
    void addNativeArrayTest() {

        var array = ArrayFactory.newCopyOnModifyArray(String.class);
        array.add("First");
        array.add("Second");
        array.addAll(toArray("Third", "Fourth"));

        Assertions.assertEquals(4, array.size());
        Assertions.assertArrayEquals(toArray("First", "Second", "Third", "Fourth"), array.array());
        Assertions.assertEquals("Second", array.get(1));

        array.addAll(toArray("Fifth", "Six"));

        Assertions.assertEquals(6, array.size());
        Assertions.assertEquals("Fifth", array.get(4));
    }

    @Test
    void arrayTest() {

        var array = ArrayFactory.newCopyOnModifyArray(String.class);
        array.add("First");

        Assertions.assertArrayEquals(toArray("First"), array.array());

        array.add("Second");

        Assertions.assertArrayEquals(toArray("First", "Second"), array.array());

        array.addAll(toArray("Third", "Fourth"));

        Assertions.assertEquals(4, array.size());
        Assertions.assertArrayEquals(toArray("First", "Second", "Third", "Fourth"), array.array());

        array.remove("Second");

        Assertions.assertArrayEquals(toArray("First", "Third", "Fourth"), array.array());

        array.removeAll(asArray("First", "Fourth"));

        Assertions.assertArrayEquals(toArray("Third"), array.array());

        Assertions.assertEquals(1, array.size());
    }

    @Test
    void removeArrayTest() {

        var array = ArrayFactory.newCopyOnModifyArray(String.class);
        array.add("First");
        array.add("Second");
        array.add("Third");
        array.add("Fourth");

        array.removeAll(Array.of("Second", "Third"));

        Assertions.assertEquals(2, array.size());
        Assertions.assertArrayEquals(toArray("First", "Fourth"), array.array());

        array.add("Second");
        array.add("Third");

        array.removeAll(Array.of("First"));

        Assertions.assertArrayEquals(toArray("Fourth", "Second", "Third"), array.array());

        array.removeAll(Array.of("Second", "Third"));

        Assertions.assertArrayEquals(toArray("Fourth"), array.array());

        array.clear();

        Assertions.assertArrayEquals(toArray(), array.array());

        array.add("First");
        array.add("First");
        array.add("First");
        array.add("Second");
        array.add("Second");

        array.removeAll(Array.of("First", "Second"));

        Assertions.assertArrayEquals(toArray("First", "First", "Second"), array.array());
    }

    @Test
    void removeCollectionTest() {

        var array = ArrayFactory.newCopyOnModifyArray(String.class);
        array.add("First");
        array.add("Second");
        array.add("Third");
        array.add("Fourth");

        array.removeAll(List.of("Second", "Third"));

        Assertions.assertEquals(2, array.size());
        Assertions.assertArrayEquals(toArray("First", "Fourth"), array.array());

        array.add("Second");
        array.add("Third");

        array.removeAll(List.of("First"));

        Assertions.assertArrayEquals(toArray("Fourth", "Second", "Third"), array.array());

        array.removeAll(List.of("Second", "Third"));

        Assertions.assertArrayEquals(toArray("Fourth"), array.array());

        array.clear();

        Assertions.assertArrayEquals(toArray(), array.array());

        array.add("First");
        array.add("First");
        array.add("First");
        array.add("Second");
        array.add("Second");

        array.removeAll(List.of("First", "Second"));

        Assertions.assertArrayEquals(toArray("First", "First", "Second"), array.array());
    }

    @Test
    void removeElementTest(){

        var array = ArrayFactory.newCopyOnModifyArray(String.class);
        array.add("First");
        array.add("Second");
        array.add("Third");
        array.add("Fourth");

        array.remove("Second");

        Assertions.assertEquals(3, array.size());
        Assertions.assertArrayEquals(toArray("First", "Third", "Fourth"), array.array());

        array.add("Second");

        array.remove("First");

        Assertions.assertArrayEquals(toArray("Third", "Fourth", "Second"), array.array());

        array.remove("Third");
        array.remove("Second");

        Assertions.assertArrayEquals(toArray("Fourth"), array.array());

        array.clear();

        Assertions.assertArrayEquals(toArray(), array.array());

        array.add("First");
        array.add("First");
        array.add("First");
        array.add("Second");
        array.add("Second");

        array.remove("First");
        array.remove("Second");

        Assertions.assertArrayEquals(toArray("First", "First", "Second"), array.array());
    }

    @Test
    void sortTest() {

        var array = ArrayFactory.newCopyOnModifyArray(Integer.class);
        array.add(10);
        array.add(5);
        array.add(9);
        array.add(1);
        array.sort(Integer::compareTo);

        Assertions.assertArrayEquals(toArray(1, 5, 9, 10), array.array());
    }
}
