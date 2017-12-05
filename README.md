# License #
Please see the file called LICENSE.

## How to use

#### Gradle

```groovy

allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    compile 'com.github.JavaSaBr:RLib:6.6.0'
}
```
    
#### Maven

```xml

<repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependency>
        <groupId>com.github.JavaSaBr</groupId>
        <artifactId>RLib</artifactId>
        <version>6.6.0</version>
    </dependency>
```

##Most interesting parts:
### Classpath Scanner API

```java

        final ClassPathScanner scanner = ClassPathScannerFactory.newDefaultScanner();
        scanner.setUseSystemClasspath(true);
        scanner.scan();

        final Array<Class<Collection>> implementations = scanner.findImplements(Collection.class);
        final Array<Class<AbstractArray>> inherited = scanner.findInherited(AbstractArray.class);
        
```

### Compiler API

```java

        final URL javaSource = getClass().getResource("/java/source/TestCompileJavaSource.java");
        
        final Compiler compiler = CompilerFactory.newDefaultCompiler();
        final Class<?>[] compiled = compiler.compile(javaSource.toURI());
        
        final Object instance = ClassUtils.newInstance(compiled[0]);
        final Method method = instance.getClass().getMethod("makeString");
        final Object result = method.invoke(instance);        
```

### VarTable API

```java

        final VarTable vars = VarTable.newInstance();
        vars.set("string", "Hello");
        vars.set("intArray", toIntegerArray(1, 2, 3, 5));
        vars.set("floatStringArray", "1.5,4.2,5.5");
        vars.set("stringEnum", "FLOAT");
        vars.set("enum", ReferenceType.BYTE);

        final String string = vars.getString("string");
        final int[] array = vars.getIntegerArray("intArray", "");
        final float[] floatStringArray = vars.getFloatArray("floatStringArray", ",");
        final ReferenceType stringEnum = vars.getEnum("stringEnum", ReferenceType.class);
        final ReferenceType anEnum = vars.getEnum("enum", ReferenceType.class);
        final ReferenceType unsafeGet = vars.get("enum");
```