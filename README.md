# License #
Please see the file called LICENSE.

## How to use

#### Gradle


```
#!groovy

allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    compile 'com.github.JavaSaBr:RLib:6.0.0-SNAPSHOT'
}
```

    
#### Maven

```
#!xml


<repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependency>
        <groupId>com.github.JavaSaBr</groupId>
        <artifactId>RLib</artifactId>
        <version>6.0.0-SNAPSHOT</version>
    </dependency>
```