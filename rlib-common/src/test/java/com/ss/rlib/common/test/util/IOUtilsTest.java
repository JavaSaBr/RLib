package com.ss.rlib.common.test.util;

import com.ss.rlib.common.util.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * @author JavaSaBr
 */
class IOUtilsTest {

    @Test
    void shouldConvertInputStreamToString() {

        var original = "test string";
        var source = new ByteArrayInputStream(original.getBytes(StandardCharsets.UTF_8));

        Assertions.assertEquals(original, IOUtils.toString(source), "result string should be the same");
    }

    @Test
    void shouldConvertSupplierOfInputStreamToString() {

        var original = "test string";

        Assertions.assertEquals(
            original,
            IOUtils.toString(() -> new ByteArrayInputStream(original.getBytes(StandardCharsets.UTF_8))),
            "result string should be the same"
        );
    }

    @Test
    void shouldThrowUncheckedIOExceptionDuringConvertingInputStreamToString() {
        Assertions.assertThrows(UncheckedIOException.class, () -> {
            IOUtils.toString(new InputStream() {
                @Override
                public int read() throws IOException {
                    throw new IOException("test");
                }
            });
        });
    }

    @Test
    void shouldThrowUncheckedIOExceptionDuringConvertingSupplierOfInputStreamToString() {
        Assertions.assertThrows(UncheckedIOException.class, () -> {
            IOUtils.toString(() -> new InputStream() {
                @Override
                public int read() throws IOException {
                    throw new IOException("test");
                }
            });
        });
    }

    @Test
    void shouldThrowRuntimeExceptionDuringConvertingSupplierOfInputStreamToString() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            IOUtils.toString(() -> {
                throw new RuntimeException("test");
            });
        });
    }
}
