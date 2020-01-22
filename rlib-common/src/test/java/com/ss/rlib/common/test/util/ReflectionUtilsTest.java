package com.ss.rlib.common.test.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.ss.rlib.common.util.ReflectionUtils;
import org.junit.jupiter.api.Test;

public class ReflectionUtilsTest {

    private static class Type1 {

        private String field1;
        private int field2;

        private static class Inner1 {
            private String field1;
            private int field2;
        }

        private static class Inner2 extends Inner1 {
            private int field3;
            private Object field4;
        }
    }

    private static class Type2 extends Type1 {
        private int field3;
        private Object field4;
    }

    @Test
    void getAllDeclaredFieldsTest() {

        var allFields = ReflectionUtils.getAllDeclaredFields(Type1.class);

        assertNotNull(allFields.findAny(object -> object.getName().equals("field1")));
        assertNotNull(allFields.findAny(object -> object.getName().equals("field2")));
        assertNull(allFields.findAny(object -> object.getName().equals("field3")));

        allFields = ReflectionUtils.getAllDeclaredFields(Type2.class);

        assertNotNull(allFields.findAny(object -> object.getName().equals("field1")));
        assertNotNull(allFields.findAny(object -> object.getName().equals("field2")));
        assertNotNull(allFields.findAny(object -> object.getName().equals("field3")));
        assertNotNull(allFields.findAny(object -> object.getName().equals("field4")));
        assertNull(allFields.findAny(object -> object.getName().equals("field5")));

        allFields = ReflectionUtils.getAllDeclaredFields(Type1.Inner1.class);

        assertNotNull(allFields.findAny(object -> object.getName().equals("field1")));
        assertNotNull(allFields.findAny(object -> object.getName().equals("field2")));
        assertNull(allFields.findAny(object -> object.getName().equals("field3")));

        allFields = ReflectionUtils.getAllDeclaredFields(Type1.Inner2.class);

        assertNotNull(allFields.findAny(object -> object.getName().equals("field1")));
        assertNotNull(allFields.findAny(object -> object.getName().equals("field2")));
        assertNotNull(allFields.findAny(object -> object.getName().equals("field3")));
        assertNotNull(allFields.findAny(object -> object.getName().equals("field4")));
        assertNull(allFields.findAny(object -> object.getName().equals("field5")));
    }
}
