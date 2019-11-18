# License #
Please see the file called LICENSE.

 [ ![Download](https://api.bintray.com/packages/javasabr/maven/com.spaceshift.rlib.common/images/download.svg) ](https://bintray.com/javasabr/maven/com.spaceshift.rlib.common/_latestVersion)


## How to use for java 11+

#### Gradle

```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/javasabr/maven" 
    }
}

dependencies {
    compile 'com.spaceshift:rlib.common:9.5.0'
    compile 'com.spaceshift:rlib.fx:9.5.0'
    compile 'com.spaceshift:rlib.network:9.5.0'
    compile 'com.spaceshift:rlib.mail:9.5.0'
    compile 'com.spaceshift:rlib.testcontainers:9.5.0'
}
```
    
#### Maven

```xml
<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>bintray-javasabr-maven</id>
        <name>bintray</name>
        <url>https://dl.bintray.com/javasabr/maven</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.spaceshift</groupId>
    <artifactId>rlib.common</artifactId>
    <version>9.5.0</version>
</dependency>
<dependency>
    <groupId>com.spaceshift</groupId>
    <artifactId>rlib.fx</artifactId>
    <version>9.5.0</version>
</dependency>
<dependency>
    <groupId>com.spaceshift</groupId>
    <artifactId>rlib.network</artifactId>
    <version>9.5.0</version>
</dependency>
<dependency>
    <groupId>com.spaceshift</groupId>
    <artifactId>rlib.mail</artifactId>
    <version>9.5.0</version>
</dependency>
<dependency>
    <groupId>com.spaceshift</groupId>
    <artifactId>rlib.testcontainers</artifactId>
    <version>9.5.0</version>
</dependency>

```

## How to use for java 8+

#### Gradle

```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/javasabr/maven" 
    }
}

dependencies {
    compile 'com.spaceshift:rlib.common:7.3.3'
}
```
    
#### Maven

```xml
<repositories>
    <repository>
        <snapshots>;
            <enabled>false</enabled>
        </snapshots>
        <id>bintray-javasabr-maven</id>
        <name>bintray</name>
        <url>https://dl.bintray.com/javasabr/maven</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.spaceshift</groupId>
    <artifactId>rlib.common</artifactId>
    <version>7.3.3</version>
</dependency>
```

## Most interesting parts:
### Fake SMTP Server

```java

    var container = new FakeSMTPTestContainer()
        .withSmtpPassword("pwd")
        .withSmtpUser("test_user");
    
    container.start();
    container.waitForReadyState();
    
    // sending emails to this server
    
    // checking API
    var count = container.getEmailCountFrom("from@test.com")
    // clearing API
    container.deleteEmails();
```

### Classpath Scanner API

```java

    var scanner = ClassPathScannerFactory.newDefaultScanner();
    scanner.setUseSystemClasspath(true);
    scanner.scan();

    var implementations = scanner.findImplements(Collection.class);
    var inherited = scanner.findInherited(AbstractArray.class);
```

### Compiler API

```java

    var javaSource = getClass().getResource("/java/source/TestCompileJavaSource.java");
    
    var compiler = CompilerFactory.newDefaultCompiler();
    var compiled = compiler.compile(javaSource.toURI());
    
    var instance = ClassUtils.newInstance(compiled[0]);
    var method = instance.getClass().getMethod("makeString");
    var result = method.invoke(instance);        
```

### VarTable API

```java

    var vars = VarTable.newInstance();
    vars.set("string", "Hello");
    vars.set("intArray", toIntegerArray(1, 2, 3, 5));
    vars.set("floatStringArray", "1.5,4.2,5.5");
    vars.set("stringEnum", "FLOAT");
    vars.set("enum", ReferenceType.BYTE);

    var string = vars.getString("string");
    var array = vars.getIntegerArray("intArray", "");
    var floatStringArray = vars.getFloatArray("floatStringArray", ",");
    var stringEnum = vars.getEnum("stringEnum", ReferenceType.class);
    var anEnum = vars.getEnum("enum", ReferenceType.class);
    var unsafeGet = vars.get("enum");
```

### Array API

```java

    var array = ArrayFactory.asArray(2, 5, 1, 7, 6, 8, 4);
    array.sort(Integer::compareTo);

    // performance operations
    var unsafe = array.asUnsafe();
    // prepare the wrapped array to have the size
    unsafe.prepareForSize(10);
    unsafe.unsafeAdd(3);
    unsafe.unsafeAdd(9);

    var first = array.first();
    var last = array.last();

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
    Array<Integer> result = IntStream.range(0, 1000)
        .mapToObj(value -> value)
        .collect(ArrayCollectors.toArray(Integer.class));
```

### Concurrent Array API

```java

    var array = ConcurrentArray.<Integer>of(Integer.class);
    var writeStamp = array.writeLock();
    try {
        array.addAll(ArrayFactory.toArray(9, 8, 7, 6, 5, 4, 3));
        array.sort(Integer::compareTo);
    } finally {
        array.writeUnlock(writeStamp);
    }

    var readStamp = array.readLock();
    try {
        var first = array.first();
        var last = array.last();
    } finally {
        array.readUnlock(readStamp);
    }

    var last = array.getInReadLock(Array::last);
    var result = array.getInReadLock(last, (arr, target) -> arr.search(target, Integer::equals));

    array.runInWriteLock(result + 1, Collection::add);
    
    // Stream Collector
    ConcurrentArray<Integer> result = IntStream.range(0, 1000)
        .parallel()
        .mapToObj(value -> value)
        .collect(ArrayCollectors.toConcurrentArray(Integer.class));
```

### Logger API

```java

    // getting logger by class/name
    var logger = LoggerManager.getLogger(getClass());

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

### Mail Sender

```java
    var config = MailSenderConfig.builder()
        .from("from@test.com")
        .host("smtp.test.com")
        .port(smtpPort)
        .password(smtpPassword)
        .username(smtpUser)
        .useAuth(true)
        .enableTtls(true)
        .sslHost("smtp.test.com")
        .build();
    
    var javaxConfig = JavaxMailSender.JavaxMailSenderConfig.builder()
        .executorKeepAlive(120)
        .executorMaxThreads(20)
        .executorMinThreads(1)
        .build();
    
    var sender = new JavaxMailSender(config, javaxConfig);
    sender.send("to@test.com", "Test Subject", "Content");
    sender.sendAsync("to@test.com", "Test Subject", "Content")
        .thenAccept(aVoid -> System.out.println("done!"));
```
### Network API
#### Simple String Echo Server/Client
```java

    var serverNetwork = NetworkFactory.newStringDataServerNetwork();
    var serverAddress = serverNetwork.start();

    serverNetwork.accepted()
        .flatMap(Connection::receivedEvents)
        .subscribe(event -> {
            var message = event.packet.getData();
            System.out.println("Received from client: " + message);
            event.connection.send(new StringWritablePacket("Echo: " + message));
        });

    var clientNetwork = newStringDataClientNetwork();
    clientNetwork.connected(serverAddress)
        .doOnNext(connection -> IntStream.range(10, 100)
            .forEach(length -> connection.send(new StringWritablePacket(StringUtils.generate(length)))))
        .flatMapMany(Connection::receivedEvents)
        .subscribe(event -> System.out.println("Received from server: " + event.packet.getData()));
```
