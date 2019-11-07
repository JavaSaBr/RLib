package com.ss.rlib.common.test;

import org.jetbrains.annotations.NotNull;

public class BaseTest {

    protected static class Type1 {
        public static final Type1 EXAMPLE = new Type1();
    }

    protected static class Type2 {
        public static final Type2 EXAMPLE = new Type2();
    }

    public <T> void assertType(@NotNull T object, @NotNull Class<T> type) {
        if (!type.isInstance(object)) {
            throw new ClassCastException();
        }
    }

    public void assertIntType(@NotNull Integer val) {}

    public void assertLongType(@NotNull Long val) {}

    public void assertFloatType(@NotNull Float val) {}
}
