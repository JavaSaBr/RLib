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
    public void testAddAndGetIntegers() {

        final VarTable vars = VarTable.newInstance();
        vars.set("stringInt", "1234");
        vars.set("objectInt", 222);

        final String stringInt = vars.getString("stringInt");
        final int stringIntAsInt = vars.getInteger("stringInt");
        final int objectIntAsInt = vars.getInteger("objectInt");

        Assertions.assertEquals("1234", stringInt);
        Assertions.assertEquals(1234, stringIntAsInt);
        Assertions.assertEquals(222, objectIntAsInt);
    }

    @Test
    public void testAddAndGetSomeTypes() {

        final VarTable vars = VarTable.newInstance();
        vars.set("string", "Hello");
        vars.set("intArray", ArrayFactory.toIntegerArray(1, 2, 3, 5));
        vars.set("floatStringArray", "1.5,4.2,5.5");
        vars.set("stringEnum", "FLOAT");
        vars.set("enum", ReferenceType.BYTE);

        final String string = vars.getString("string");
        final int[] array = vars.getIntegerArray("intArray", "");
        final float[] floatStringArray = vars.getFloatArray("floatStringArray", ",");
        final ReferenceType stringEnum = vars.getEnum("stringEnum", ReferenceType.class);
        final ReferenceType anEnum = vars.getEnum("enum", ReferenceType.class);
        final ReferenceType unsafeGet = vars.get("enum");

        Assertions.assertEquals("Hello", string);
        Assertions.assertArrayEquals(array, ArrayFactory.toIntegerArray(1, 2, 3, 5));
        Assertions.assertArrayEquals(floatStringArray, ArrayFactory.toFloatArray(1.5F, 4.2F, 5.5F));
        Assertions.assertEquals(ReferenceType.FLOAT, stringEnum);
        Assertions.assertEquals(ReferenceType.BYTE, anEnum);
        Assertions.assertEquals(ReferenceType.BYTE, unsafeGet);
    }
}
