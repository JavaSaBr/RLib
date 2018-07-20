package com.ss.rlib.common.test.util.dictionary;

import com.ss.rlib.common.util.array.*;
import com.ss.rlib.common.util.dictionary.ObjectDictionary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The list of tests {@link Array}.
 *
 * @author JavaSaBr
 */
public class DictionaryTests {

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
