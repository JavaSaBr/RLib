# RLib

A modular Java utility library providing common utilities for classpath scanning, runtime compilation, logging, networking, mail, collections, concurrency, and more.

[![Build Status](https://github.com/JavaSaBr/RLib/actions/workflows/develop.yml/badge.svg)](https://github.com/JavaSaBr/RLib/actions)

## Requirements

- Java 21+
- Gradle 9.1+ (wrapper included)
- Docker (for running Testcontainers-based tests)

## Modules

| Module | Description |
|--------|-------------|
| `rlib-common` | Core utilities and common functionality |
| `rlib-collections` | Extended collection implementations |
| `rlib-compiler` | Runtime Java source compilation API |
| `rlib-concurrent` | Concurrency utilities and helpers |
| `rlib-classpath` | Classpath scanning and class discovery |
| `rlib-functions` | Functional interfaces and utilities |
| `rlib-geometry` | Geometry utilities |
| `rlib-io` | I/O utilities |
| `rlib-reference` | Reference utilities |
| `rlib-reusable` | Object pooling and reusable resources |
| `rlib-logger-api` | Logging API |
| `rlib-logger-impl` | Default logger implementation |
| `rlib-logger-slf4j` | SLF4J logger bridge |
| `rlib-network` | Reactive network API (client/server) |
| `rlib-mail` | Email sending utilities |
| `rlib-testcontainers` | Test utilities including Fake SMTP container |
| `rlib-fx` | JavaFX utilities |
| `rlib-plugin-system` | Plugin system framework |

## Installation

### Gradle

```groovy
repositories {
    maven {
        url "https://gitlab.com/api/v4/projects/37512056/packages/maven"
    }
}

ext {
    rlibVersion = "10.0.alpha10"
}

dependencies {
    implementation "javasabr.rlib:rlib-common:$rlibVersion"
    implementation "javasabr.rlib:rlib-collections:$rlibVersion"
    implementation "javasabr.rlib:rlib-compiler:$rlibVersion"
    implementation "javasabr.rlib:rlib-concurrent:$rlibVersion"
    implementation "javasabr.rlib:rlib-geometry:$rlibVersion"
    implementation "javasabr.rlib:rlib-logger-api:$rlibVersion"
    implementation "javasabr.rlib:rlib-logger-slf4j:$rlibVersion"
    implementation "javasabr.rlib:rlib-plugin-system:$rlibVersion"
    implementation "javasabr.rlib:rlib-reference:$rlibVersion"
    implementation "javasabr.rlib:rlib-reusable:$rlibVersion"
    implementation "javasabr.rlib:rlib-fx:$rlibVersion"
    implementation "javasabr.rlib:rlib-network:$rlibVersion"
    implementation "javasabr.rlib:rlib-mail:$rlibVersion"
    implementation "javasabr.rlib:rlib-testcontainers:$rlibVersion"
}
```

## Usage Examples

### Classpath Scanner API

Scan classpath to discover classes implementing interfaces or extending base classes:

```java
var scanner = ClassPathScannerFactory.newDefaultScanner();
scanner.setUseSystemClasspath(true);
scanner.scan();

var implementations = scanner.findImplements(Collection.class);
var inherited = scanner.findInherited(AbstractArray.class);
```

### Compiler API

Compile Java source code at runtime:

```java
var javaSource = getClass().getResource("/java/source/TestCompileJavaSource.java");

var compiler = CompilerFactory.newDefaultCompiler();
var compiled = compiler.compile(javaSource.toURI());

var instance = ClassUtils.newInstance(compiled[0]);
var method = instance.getClass().getMethod("makeString");
var result = method.invoke(instance);
```

### Logger API

Flexible logging with lazy evaluation and configurable log levels:

```java
// getting logger by class/name
var logger = LoggerManager.getLogger(getClass());

// global enable/disable debug level
LoggerLevel.DEBUG.setEnabled(true);

logger.debug("Simple message");
logger.debug(5, (val) -> "Lazy message with 5: " + val);
logger.debug(5, "Lazy message with 5:%d"::formatted);
logger.debug(5, 10D, (val1, val2) -> "Lazy message with 5: " + val1 + " and 10: " + val2);
logger.debug(5, 10D, "Lazy message with 5:%d and 10:%d"::formatted);

// global disable debug level
LoggerLevel.DEBUG.setEnabled(false);

// local enable debug level only for this logger instance
logger.setEnabled(LoggerLevel.DEBUG, true);
```

### Mail Sender

Send emails synchronously or asynchronously:

```java
var config = MailSenderConfig
    .builder()
    .from("from@test.com")
    .host("smtp.test.com")
    .port(smtpPort)
    .password(smtpPassword)
    .username(smtpUser)
    .useAuth(true)
    .enableTtls(true)
    .sslHost("smtp.test.com")
    .build();

var javaxConfig = JavaxMailSender.JavaxMailSenderConfig
    .builder()
    .executorKeepAlive(120)
    .executorMaxThreads(20)
    .executorMinThreads(1)
    .build();

var sender = new JavaxMailSender(config, javaxConfig);

sender.send("to@test.com", "Test Subject", "Content");
sender
    .sendAsync("to@test.com", "Test Subject", "Content")
    .thenAccept(aVoid -> System.out.println("done!"));
```

### Network API

Reactive network communication with simple client/server setup:

```java
var serverNetwork = NetworkFactory.newStringDataServerNetwork();
var serverAddress = serverNetwork.start();

serverNetwork
    .accepted()
    .flatMap(Connection::receivedEvents)
    .subscribe(event -> {
        var message = event.packet.getData();
        System.out.println("Received from client: " + message);
        event.connection.send(new StringWritablePacket("Echo: " + message));
    });

var clientNetwork = NetworkFactory.newStringDataClientNetwork();
clientNetwork
    .connected(serverAddress)
    .doOnNext(connection -> IntStream
        .range(10, 100)
        .forEach(length -> connection.send(new StringWritablePacket(StringUtils.generate(length)))))
    .flatMapMany(Connection::receivedEvents)
    .subscribe(event -> System.out.println("Received from server: " + event.packet.getData()));
```

### Fake SMTP Server (for testing)

Use a containerized fake SMTP server for integration tests:

```java
var container = new FakeSMTPTestContainer()
    .withSmtpPassword("pwd")
    .withSmtpUser("test_user");

container.start();
container.waitForReadyState();

// sending emails to this server

// checking API
var count = container.getEmailCountFrom("from@test.com");

// clearing API
container.deleteEmails();
```

### Collections API

Extended collections with dictionaries (maps) and arrays optimized for specific use cases:

```java
// Mutable dictionary (map) with object keys
var dictionary = DictionaryFactory.mutableRefToRefDictionary();
dictionary.put("key1", "value1");
dictionary.put("key2", "value2");

var value = dictionary.get("key1");

// Thread-safe dictionary with stamped lock
var lockableDictionary = DictionaryFactory.stampedLockBasedRefToRefDictionary();
var stamp = lockableDictionary.readLock();
try {
  lockableDictionary.put("key", "value");
} finally {
  lockableDictionary.readUnlock(stamp)
}

// Primitive key dictionaries (no boxing overhead)
var intToRefDictionary = DictionaryFactory.mutableIntToRefDictionary();
intToRefDictionary.put(1, "value1");

var longToRefDictionary = DictionaryFactory.mutableLongToRefDictionary();
longToRefDictionary.put(100L, "value2");

// Mutable arrays with type safety
var array = ArrayFactory.mutableArray(String.class);
array.add("element1");
array.add("element2");

// Thread-safe copy-on-write array
var cowArray = ArrayFactory.copyOnModifyArray(String.class);

// Stamped lock based thread-safe array
var lockableArray = ArrayFactory.stampedLockBasedArray(String.class);
```

### Object Pooling

Reusable object pools for reducing GC pressure:

```java
// Create a pool for reusable objects
var pool = PoolFactory.newReusablePool(MyReusableObject.class);

// Take an object from pool (or create new if empty)
var obj = pool.take();

// Use the object...

// Return to pool for reuse
pool.put(obj);

// Thread-safe pool
var lockablePool = PoolFactory.newLockBasePool(MyObject.class);
```

### Plugin System

Dynamic plugin loading and management:

```java
var pluginSystem = PluginSystemFactory.newBasePluginSystem();
pluginSystem.configureAppVersion(new Version("1.0.0"));
pluginSystem.configureEmbeddedPluginPath(Paths.get("plugins/"));

// Async plugin loading
pluginSystem
    .preLoad(ForkJoinPool.commonPool())
    .thenCompose(system -> system.initialize(ForkJoinPool.commonPool()))
    .toCompletableFuture()
    .join();

// Access extension points
var extensionPoint = pluginSystem
    .extensionPointManager()
    .getExtensionPoint(MyExtension.class);
```

## Building

```bash
# Full build with tests
./gradlew clean build

# Build specific module
./gradlew :rlib-common:build

# Run tests only
./gradlew test

# Skip tests
./gradlew clean build -x test
```

## License

Please see the file called [LICENSE](LICENSE).
