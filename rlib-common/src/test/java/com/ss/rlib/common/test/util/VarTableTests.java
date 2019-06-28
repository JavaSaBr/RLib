package com.ss.rlib.common.test.util;

import static com.ss.rlib.common.util.array.ArrayFactory.toArray;
import static com.ss.rlib.common.util.array.ArrayFactory.toBooleanArray;
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
    void testAllGets() {

        var vars = new VarTable();
        vars.put("number", 10);
        vars.put("array", toArray(1, 2, 3, 4));
        vars.put("arrayString", "1,2,3,4");
        vars.put("boolean", true);
        vars.put("booleanString", "true");
        vars.put("booleanArray", toBooleanArray(true, false, true));
        vars.put("booleanArrayString", "true, false, true");

        Assertions.assertEquals(vars.<Integer>get("number"), 10);

        Assertions.assertEquals(vars.get("number", Integer.class), 10);
        Assertions.assertThrows(IllegalArgumentException.class, () -> vars.get("number", Double.class));

        Assertions.assertEquals(vars.get("number", Integer.class, 11), 10);
        Assertions.assertEquals(vars.get("not_exist", Integer.class, 11), 11);
        Assertions.assertEquals(vars.get("number", Double.class, 12D), 12D);

        Assertions.assertEquals(vars.getNullable("number", Integer.class, null), 10);
        Assertions.assertNull(vars.getNullable("not_exist", Double.class, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> vars.getNullable("number", Double.class, null));

        Assertions.assertEquals(vars.get("number",11), 10);
        Assertions.assertEquals(vars.get("not_exist",11), 11);
        Assertions.assertThrows(IllegalArgumentException.class, () -> vars.get("number",11D));

        Assertions.assertNull(vars.getNullable("not_exist", null));
        Assertions.assertEquals(vars.getNullable("number",11D), 11D);
        Assertions.assertEquals(vars.getNullable("number",11), 10);
        Assertions.assertEquals(vars.<Integer>getNullable("number",null), 10);
        Assertions.assertThrows(ClassCastException.class, () -> {
            Double d = vars.<Double>getNullable("number",null);
        });

        Assertions.assertArrayEquals(vars.getArray("array", Integer[].class, 1, 3, 3), toArray(1, 2, 3, 4));
        Assertions.assertArrayEquals(vars.getArray("not_exist", Integer[].class, 1, 3, 3), toArray(1, 3, 3));
        Assertions.assertThrows(IllegalArgumentException.class, () -> vars.getArray("array", Double[].class, 1D, 3D, 3D));

        Assertions.assertTrue(vars.getBoolean("boolean"));
        Assertions.assertTrue(vars.getBoolean("booleanString"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> vars.getBoolean("not_exist"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> vars.getBoolean("number"));

        Assertions.assertTrue(vars.getBoolean("boolean", false));
        Assertions.assertTrue(vars.getBoolean("booleanString", false));
        Assertions.assertFalse(vars.getBoolean("not_exist", false));
        Assertions.assertThrows(IllegalArgumentException.class, () -> vars.getBoolean("number", false));

        Assertions.assertArrayEquals(vars.getBooleanArray("booleanArray", ","), toBooleanArray(true, false, true));
        Assertions.assertArrayEquals(vars.getBooleanArray("booleanArrayString", ","), toBooleanArray(true, false, true));
    }

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
