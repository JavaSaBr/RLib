package com.ss.rlib.test.classpath;

import com.ss.rlib.classpath.ClassPathScanner;
import com.ss.rlib.classpath.ClassPathScannerFactory;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.impl.AbstractArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

/**
 * The list of test to test working with classpath scanner API.
 *
 * @author JavaSaBr
 */
public class ClasspathScannerTests {

    @Test
    public void testSystemClasspathScanner() {

        final ClassPathScanner scanner = ClassPathScannerFactory.newDefaultScanner();
        scanner.setUseSystemClasspath(true);
        scanner.scan();

        final Array<Class<Collection>> implementations = scanner.findImplements(Collection.class);

        Assertions.assertEquals(true, !implementations.isEmpty());

        final Array<Class<AbstractArray>> inherited = scanner.findInherited(AbstractArray.class);

        Assertions.assertEquals(true, !inherited.isEmpty());
    }
}
