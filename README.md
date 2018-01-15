# License #
Please see the file called LICENSE.

## How to use

#### Gradle

```groovy

repositories {
        maven { url 'https://jitpack.io' }
    }

dependencies {
    compile 'com.github.JavaSaBr:RLib:6.7.4'
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
        <version>6.7.4</version>
    </dependency>
```

## Most interesting parts:
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

### Array API

```java

    final Array<Integer> array = ArrayFactory.asArray(2, 5, 1, 7, 6, 8, 4);
    array.sort(Integer::compareTo);

    // performance operations
    final UnsafeArray<Integer> unsafe = array.asUnsafe();
    // prepare the wrapped array to have the size
    unsafe.prepareForSize(10);
    unsafe.unsafeAdd(3);
    unsafe.unsafeAdd(9);

    final Integer first = array.first();
    final Integer last = array.last();

    // remove the element with saving ordering
    array.slowRemove(1);
    // remove the element without saving ordering
    array.fastRemove(1);

    // search API
    Integer searched = array.search(integer -> integer == 2);
    searched = array.search(2, (el, arg) -> el == arg);

    // foreach API
    array.forEach(5, (el, arg) -> System.out.println(el + arg));
    array.forEach(5, 7, (el, firstArg, secondArg) -> System.out.println(el + firstArg + secondArg));
    
    // Stream Collector
    final Array<Integer> result = IntStream.range(0, 1000)
                    .mapToObj(value -> value)
                    .collect(ArrayCollectors.simple(Integer.class));
```

### Concurrent Array API

```java

    final ConcurrentArray<Integer> array = ArrayFactory.newConcurrentAtomicARSWLockArray(Integer.class);
    final long writeStamp = array.writeLock();
    try {
        array.addAll(ArrayFactory.toArray(9, 8, 7, 6, 5, 4, 3));
        array.sort(Integer::compareTo);
    } finally {
        array.writeUnlock(writeStamp);
    }

    final long readStamp = array.readLock();
    try {
        final Integer first = array.first();
        final Integer last = array.last();
    } finally {
        array.readUnlock(readStamp);
    }

    final Integer last = ArrayUtils.getInReadLock(array, Array::last);
    final Integer result = ArrayUtils.getInReadLock(array, last,
            (arr, target) -> arr.search(target, Integer::equals));

    ArrayUtils.runInWriteLock(array, result + 1, Collection::add);
    
    // Stream Collector
    final ConcurrentArray<Integer> result = IntStream.range(0, 1000)
                    .parallel()
                    .mapToObj(value -> value)
                    .collect(ArrayCollectors.concurrent(Integer.class));
```

### Logger API

```java

    // getting logger by class/name
    final Logger logger = LoggerManager.getLogger(getClass());

    // global enable/disable debug level
    LoggerLevel.DEBUG.setEnabled(true);
    
    logger.debug("Simple message");
    logger.debug(5, (val) -> "Lazy message with 5: " + val);
    logger.debug(5, 10D, (val1, val2) -> "Lazy message with 5: " + val1 + " and 10: " + val2);
    logger.debug("", "Message with a string owner.");
    logger.debug("", 5, (val) -> "Lazy message with 5: " + val);
    logger.debug("", 5, 10D, (val1, val2) -> "Lazy message with 5: " + val1 + " and 10: " + val2);
    
    // global disable debug level
    LoggerLevel.DEBUG.setEnabled(false);
    
    // local enable debug level only for this logger instance
    logger.setEnabled(LoggerLevel.DEBUG, true);
    
    // show debug message
    logger.debug("Showed");
```

### Network API

```java

    public static class ServerPackets {

        @PacketDescription(id = 1)
        public static class MessageRequest extends AbstractReadablePacket {

            @Override
            protected void readImpl(@NotNull final ConnectionOwner owner, @NotNull final ByteBuffer buffer) {
                final String message = readString(buffer);
                System.out.println("Server: received \"" + message + "\"");
                owner.sendPacket(new MessageResponse("Response of " + message));
            }
        }
        
        @PacketDescription(id = 2)
        public static class MessageResponse extends AbstractWritablePacket {

            @NotNull
            private final String message;

            public MessageResponse(@NotNull final String message) {
                this.message = message;
            }

            @Override
            protected void writeImpl(@NotNull final ByteBuffer buffer) {
                super.writeImpl(buffer);
                writeString(buffer, message);
            }
        }
    }

    public static class ClientPackets {

        @PacketDescription(id = 1)
        public static class MessageRequest extends AbstractWritablePacket {

            @NotNull
            private final String message;

            public MessageRequest(@NotNull final String message) {
                this.message = message;
            }

            @Override
            protected void writeImpl(@NotNull final ByteBuffer buffer) {
                super.writeImpl(buffer);
                writeString(buffer, message);
            }
        }

        @PacketDescription(id = 2)
        public static class MessageResponse extends AbstractReadablePacket {

            @Override
            protected void readImpl(@NotNull final ConnectionOwner owner, @NotNull final ByteBuffer buffer) {
                final String message = readString(buffer);
                System.out.println("client: received \"" + message + "\"");
            }
        }
    }
    
    final InetSocketAddress address = new InetSocketAddress(2222);

    serverNetwork = NetworkFactory.newDefaultAsyncServerNetwork(ReadablePacketRegistry.of(ServerPackets.MessageRequest.class));
    serverNetwork.bind(address);
    
    clientNetwork = NetworkFactory.newDefaultAsyncClientNetwork(ReadablePacketRegistry.of(ClientPackets.MessageResponse.class));
    clientNetwork.connect(address);
    
    final Server server = clientNetwork.getCurrentServer();
    server.sendPacket(new ClientPackets.MessageRequest("Test client message"));
```