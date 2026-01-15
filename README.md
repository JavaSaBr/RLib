# License #

Please see the file called LICENSE.

## How to use for java 21+

#### Gradle

```groovy
repositories {
    maven {
        url  "https://gitlab.com/api/v4/projects/37512056/packages/maven" 
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
var count = container.getEmailCountFrom("from@test.com");

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
var method = instance
    .getClass()
    .getMethod("makeString");
var result = method.invoke(instance);        
```

### Logger API

```java
// getting logger by class/name
var logger = LoggerManager.getLogger(getClass());

// global enable/disable debug level
LoggerLevel.DEBUG.setEnabled(true);

logger.debug("Simple message");
logger.debug(5, (val) -> "Lazy message with 5: " + val);
logger.debug(5, "Lazy message with 5:%d"::formated);
logger.debug(5, 10D, (val1, val2) -> "Lazy message with 5: " + val1 + " and 10: " + val2);
logger.debug(5, 10D, "Lazy message with 5:%d and 10:%d"::formatted);

// global disable debug level
LoggerLevel.DEBUG.setEnabled(false);

// local enable debug level only for this logger instance
logger.setEnabled(LoggerLevel.DEBUG, true);
```

### Mail Sender

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

sender.send("to@test.com","Test Subject","Content");
sender
    .sendAsync("to@test.com","Test Subject","Content")
    .thenAccept(aVoid -> System.out.println("done!"));
```

### Network API

#### Simple String Echo Server/Client

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
        .range(10,100)
        .forEach(length -> connection.send(new StringWritablePacket(StringUtils.generate(length)))))
    .flatMapMany(Connection::receivedEvents)
    .subscribe(event -> System.out.println("Received from server: " + event.packet.getData()));
```
