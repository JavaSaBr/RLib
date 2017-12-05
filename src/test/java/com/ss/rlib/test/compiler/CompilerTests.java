package com.ss.rlib.test.compiler;

import com.ss.rlib.compiler.Compiler;
import com.ss.rlib.compiler.CompilerFactory;
import com.ss.rlib.util.ClassUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * The list of tests to work with compiler API.
 *
 * @author JavaSaBr
 */
public class CompilerTests {

    @Test
    public void compileOneSourceTest()
            throws URISyntaxException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        final URL javaSource = getClass().getResource("/java/source/TestCompileJavaSource.java");

        final Compiler compiler = CompilerFactory.newDefaultCompiler();
        final Class<?>[] compiled = compiler.compile(javaSource.toURI());

        Assertions.assertEquals(1, compiled.length);
        Assertions.assertEquals("TestCompileJavaSource", compiled[0].getName());

        final Object instance = ClassUtils.newInstance(compiled[0]);
        final Method method = instance.getClass().getMethod("makeString");
        final Object result = method.invoke(instance);

        Assertions.assertEquals("testString", result);
    }
}
