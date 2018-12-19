package com.ss.rlib.common.test.util.dictionary;

import static com.ss.rlib.common.util.dictionary.DictionaryCollectors.toLongDictionary;
import static com.ss.rlib.common.util.dictionary.DictionaryFactory.newLongDictionary;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.FastLongDictionary;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

/**
 * The list of tests {@link FastLongDictionary}.
 *
 * @author JavaSaBr
 */
public class FastLongDictionaryTests {

    @Test
    void testFastLongDictionary() {

        var dictionary = DictionaryFactory.<String>newLongDictionary();
        dictionary.put(5, "5");
        dictionary.put(6, "6");

        assertEquals(2, dictionary.size());

        dictionary.put(7, "7");

        assertEquals(3, dictionary.size());
        assertEquals("7", dictionary.get(7));

        assertEquals("8", dictionary.getOrCompute(8, () -> "8"));
        assertEquals("9", dictionary.getOrCompute(9, key -> "9"));

        assertEquals(5, dictionary.size());

        assertEquals("8", dictionary.remove(8));

        assertEquals(4, dictionary.size());
    }

    @Test
    void testEmptyLongDictionary() {

        var emptyDictionary = LongDictionary.<String>empty();

        assertThrows(IllegalStateException.class, () -> emptyDictionary.put(1, "1"));
        assertThrows(IllegalStateException.class, () -> emptyDictionary.remove(1));
        assertThrows(IllegalStateException.class, () -> emptyDictionary.put(newLongDictionary()));
        assertThrows(IllegalStateException.class, emptyDictionary::clear);
    }

    @Test
    void testFastLongDictionaryOf() {

        var dictionary = LongDictionary.<String>of(1, "val1", 2, "val2", 3, "val3");

        assertEquals(3, dictionary.size());
        assertEquals("val1", dictionary.get(1));
        assertEquals("val3", dictionary.get(3));


        assertThrows(IllegalArgumentException.class, () -> LongDictionary.of("Key1", 1, "Key2", 2, "Key3"));
        assertThrows(IllegalArgumentException.class, () -> LongDictionary.of("Key1"));
    }

    @Test
    void testFastLongDictionaryCollector() {

        var dictionary = List.of(1, 2, 3, 4, 5)
            .stream()
            .collect(toLongDictionary(Function.identity(), Function.identity()));

        assertEquals(5, dictionary.size());
        assertEquals(3, (int) dictionary.get(3));
    }
}
