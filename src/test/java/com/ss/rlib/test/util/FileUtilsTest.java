package com.ss.rlib.test.util;

import com.ss.rlib.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The test to test file utils.
 *
 * @author JavaSaBr
 */
public class FileUtilsTest {

    @Test
    public void testGetNameByPath() {

        final String path = "/some/folder/some/name.ololo";
        final String path2 = "D:\\some\\folder\\some\\name.ololo";

        Assertions.assertEquals("name.ololo", FileUtils.getName(path, '/'));
        Assertions.assertEquals("name.ololo", FileUtils.getName(path2, '\\'));
    }

    @Test
    public void testGetParentByPath() {

        final String path = "/some/folder/some/name.ololo";
        final String path2 = "D:\\some\\folder\\some\\name.ololo";

        Assertions.assertEquals("/some/folder/some", FileUtils.getParent(path, '/'));
        Assertions.assertEquals("D:\\some\\folder\\some", FileUtils.getParent(path2, '\\'));
    }
}
