package javasabr.rlib.compiler;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.net.URISyntaxException;
import java.util.Comparator;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayCollectors;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerManager;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class CompilerTests {

  static {
    LoggerManager.configureDefault(LoggerLevel.DEBUG, true);
  }

  @Test
  void compileTest()
      throws URISyntaxException, NoSuchMethodException, InvocationTargetException, IllegalAccessException,
      InstantiationException {

    // given:
    var javaSources = Array.of(
        getClass().getResource("/java/source/TestCompileDependency.java").toURI(),
        getClass().getResource("/java/source/TestCompileJavaSource.java").toURI(),
        getClass().getResource("/java/source/TestCompileRecord.java").toURI());

    // when:
    var compiler = CompilerFactory.newDefaultCompiler();
    var compiledClasses = compiler.compileByUrls(javaSources)
        .stream()
        .sorted(Comparator.comparing(Class::getName))
        .collect(ArrayCollectors.toArray(Class.class));

    // then:
    assertThat(compiledClasses.size()).isEqualTo(3);

    assertThat(compiledClasses.get(0).getName())
        .isEqualTo("TestCompileDependency");
    assertThat(compiledClasses.get(1).getName())
        .isEqualTo("TestCompileJavaSource");
    assertThat(compiledClasses.get(2).getName())
        .isEqualTo("TestCompileRecord");

    // when:
    var instance1 = ClassUtils.newInstance(compiledClasses.get(0));
    var method = instance1
        .getClass()
        .getMethod("calcInteger");
    var result = method.invoke(instance1);

    // then:
    assertThat(result).isEqualTo(5);

    // when:
    var instance2 = ClassUtils.newInstance(compiledClasses.get(1));
    method = instance2
        .getClass()
        .getMethod("makeString");
    result = method.invoke(instance2);

    // then:
    assertThat(result).isEqualTo("testString");

    // when:
    Record instance3 = (Record) ClassUtils
        .tryGetConstructor(compiledClasses.get(2), compiledClasses.get(0), String.class)
        .newInstance(instance1, "recordString");

    // then:
    RecordComponent[] recordComponents = instance3
        .getClass()
        .getRecordComponents();

    assertThat(recordComponents[0].getAccessor().invoke(instance3))
        .isEqualTo(instance1);
    assertThat(recordComponents[1].getAccessor().invoke(instance3))
        .isEqualTo("recordString");
  }
}
