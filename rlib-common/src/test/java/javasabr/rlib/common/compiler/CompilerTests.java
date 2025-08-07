package javasabr.rlib.common.compiler;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import javasabr.rlib.common.util.ClassUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The list of tests to work with compiler API.
 *
 * @author JavaSaBr
 */
public class CompilerTests {

    @Test
    void compileTest()
        throws URISyntaxException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        var javaSource = getClass()
            .getResource("/java/source/TestCompileJavaSource.java");

        var compiler = CompilerFactory.newDefaultCompiler();
        var compiled = compiler.compile(javaSource.toURI());

        Assertions.assertEquals(1, compiled.length);
        Assertions.assertEquals("TestCompileJavaSource", compiled[0].getName());

        var instance = ClassUtils.newInstance(compiled[0]);
        var method = instance.getClass().getMethod("makeString");
        var result = method.invoke(instance);

        Assertions.assertEquals("testString", result);
    }
}
