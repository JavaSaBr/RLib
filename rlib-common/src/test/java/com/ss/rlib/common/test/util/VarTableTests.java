package com.ss.rlib.common.test.util;

import com.ss.rlib.common.util.VarTable;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.ref.ReferenceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The list of tests {@link VarTable}.
 *
 * @author JavaSaBr
 */
public class VarTableTests {

    @Test
    void testAddAndGetIntegers() {

        var vars = new VarTable();
        vars.put("stringInt", "1234");
        vars.put("objectInt", 222);

        var stringInt = vars.getString("stringInt");
        var stringIntAsInt = vars.getInt("stringInt");
        var objectIntAsInt = vars.getInt("objectInt");

        Assertions.assertEquals("1234", stringInt);
        Assertions.assertEquals(1234, stringIntAsInt);
        Assertions.assertEquals(222, objectIntAsInt);
    }

    @Test
    void testCheckExisting() {

        var vars = new VarTable();
        vars.put("value", "1234");

        Assertions.assertTrue(vars.has("value"));
        Assertions.assertFalse(vars.has("no value"));
    }

    @Test
    void testCopyingAnotherVarsTable() {

        var vars = new VarTable();
        vars.put("val1", "1");
        vars.put("val2", "2");

        var anotherVars = new VarTable();
        anotherVars.put("val3", "3");
        anotherVars.put("val4", "4");

        vars.put(anotherVars);

        Assertions.assertEquals("3", vars.getString("val3"));
        Assertions.assertEquals("4", vars.getString("val4"));

        try {
            vars.put(vars);
            throw new IllegalStateException("Can't copy vars recursive.");
        } catch (IllegalArgumentException e) {
            // it's ok
        }
    }

    @Test
    void testAddAndGetSomeTypes() {

        var vars = new VarTable();
        vars.put("string", "Hello");
        vars.put("intArray", ArrayFactory.toIntArray(1, 2, 3, 5));
        vars.put("floatStringArray", "1.5,4.2,5.5");
        vars.put("stringEnum", "FLOAT");
        vars.put("enum", ReferenceType.BYTE);

        var string = vars.getString("string");
        var array = vars.getIntArray("intArray", "");
        var floatStringArray = vars.getFloatArray("floatStringArray", ",");
        var stringEnum = vars.getEnum("stringEnum", ReferenceType.class);
        var anEnum = vars.getEnum("enum", ReferenceType.class);
        var unsafeGet = vars.get("enum");

        Assertions.assertEquals("Hello", string);
        Assertions.assertArrayEquals(array, ArrayFactory.toIntArray(1, 2, 3, 5));
        Assertions.assertArrayEquals(floatStringArray, ArrayFactory.toFloatArray(1.5F, 4.2F, 5.5F));
        Assertions.assertEquals(ReferenceType.FLOAT, stringEnum);
        Assertions.assertEquals(ReferenceType.BYTE, anEnum);
        Assertions.assertEquals(ReferenceType.BYTE, unsafeGet);
    }
}
