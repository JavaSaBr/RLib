package com.ss.rlib.common.test.util.dictionary;

import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.ObjectDictionary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The list of tests {@link ObjectDictionary}.
 *
 * @author JavaSaBr
 */
public class ObjectDictionaryTests {

    @Test
    public void testFastObjectDictionary() {

        ObjectDictionary<String, Integer> dictionary = DictionaryFactory.newObjectDictionary();
        dictionary.put("Key5", 5);
        dictionary.put("Key6", 6);

        Assertions.assertEquals(2, dictionary.size());

        dictionary.put("Key7", 7);

        Assertions.assertEquals(3, dictionary.size());
        Assertions.assertEquals(7, (int) dictionary.get("Key7"));

        Assertions.assertEquals(8, (int) dictionary.getOrCompute("Key8", () -> 8));
        Assertions.assertEquals(9, (int) dictionary.getOrCompute("Key9", key -> 9));

        Assertions.assertEquals(5, dictionary.size());

        Assertions.assertEquals(8, (int) dictionary.remove("Key8"));

        Assertions.assertEquals(4, dictionary.size());
    }

    @Test
    public void testObjectDictionaryOf() {

        ObjectDictionary<String, Integer> dictionary = ObjectDictionary.of(
                "Key1", 1,
                "Key2", 2,
                "Key3", 3
        );

        Assertions.assertEquals(3, dictionary.size());
    }

    @Test
    public void testObjectDictionaryOfIncorrectArg() {

        try {

            ObjectDictionary.of(
                "Key1", 1,
                "Key2", 2,
                "Key3"
            );

            throw new RuntimeException();

        } catch (IllegalArgumentException e) {
        }

        try {
            ObjectDictionary.of("Key1");
            throw new RuntimeException();
        } catch (IllegalArgumentException e) {
        }
    }
}
