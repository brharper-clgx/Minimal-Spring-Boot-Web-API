# Minimal-Spring-Boot-Web-API
'How to:' for a minimal spring boot web api. (For windows)

## Setup
### Java
- Download the latest  [JDK](http://oracle.com/technetwork/java/javase/downloads/index.html).
    - Make sure you have System Environment Varialbes 'JAVA_HOME' and 'JDK_HOME' which point to the JDK directory (its best to put it in program files).

### Gradle
- Download the latest version of [Gradle](https://gradle.org/releases/)
    - Place the directory containing the Gradle source code in *Program Files*.
    - Add a 'GRADLE_HOME' System Environment Variable which points to this directory.
    - Add the Gradle directory's *bin* folder to your System Path variable.

## Initialize Gradle Project
Use the command 'gradle init' to initialize gradle in your project's root directory:
```
> gradle init
```

If a 'gradlew' file is not created, use the following command:
```
> gradle wrapper
```

Next, edit the ***build.gradle*** file with the following:
```
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.0.3.RELEASE")
    }
}

apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'idea'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

bootJar {
    baseName = 'gs-rest-service'
    version =  '0.1.0'
}

repositories {
    mavenCentral()
}

sourceCompatibility = 1.8
targetCompatibility = 1.8

dependencies {
    compile("org.springframework.boot:spring-boot-starter-web")
    testCompile('org.springframework.boot:spring-boot-starter-test')
}
```

## Source Code
Next create the following subdirectory structure for your source code:
```
> mkdir -p src/main/java/hello
```

Create your first resource representation class:
```
src/main/java/hello/Greeting.java
```
```
package hello;

public class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
```

Now, create a controller:
```src/main/java/hello/GreetingController.java```

```
package hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(
            counter.incrementAndGet(), 
            String.format(template, name)
        );
    }
}
```

Finally, create the main application class:
```src/main/java/hello/Application.java```

```
package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## Test It Out
Run the web app with:
```
> ./gradlew bootrun
```

Open a web browser and visit [localhost:8080/greeting](localhost:8080/greeting). Your browser should display the following:
```
{"id":1,"content":"Hello, World!"}
```

Change the url to [http://localhost:8080/greeting?name=YourName](http://localhost:8080/greeting?name=YourName) to display:
```
{"id":2,"content":"Hello, YourName!"}
```

## Write Tests
Create a subdirectory structure for your tests:

```
> mkdir -p src/test/java/hello
```

Create a test class:
```src/test/java/hello/GreetingControllerTest.java```

```
package hello;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GreetingControllerTests {

	@Test
	public void GreetingConroller_greeting_ReturnsGreeting() {
		// Arrange
		GreetingController target = new GreetingController();
		
		// Act
		Greeting result = target.greeting("Joe Tester");

		// Assert
		assertEquals(1, result.getId());
		assertEquals("Hello, Joe Tester!", result.getContent());
	}

}
```

You can run tests either through your IDE (VS Code has an extension: Java Test Runner). Alternatively, you can run the following command:

```
> ./gradlew test -i
```