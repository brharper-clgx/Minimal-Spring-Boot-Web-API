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
```bash
gradle init
```

If a 'gradlew' file is not created, use the following command:
```bash
gradle wrapper
```

Next, edit the ***build.gradle*** file with the following:
```gradle
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
}
```

## Source Code
Next create the following subdirectory structure for your source code:
```bash
mkdir -p src/main/java/hello
```

Create your first resource representation class:
```
src/main/java/hello/Greeting.java
```

```java
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

```java
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

```java
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

## Try It Out
Run the web app with:
```bash
./gradlew bootrun
```

Open a web browser and visit [http://localhost:8080/greeting](http://localhost:8080/greeting). Your browser should display the following:
```
{"id":1,"content":"Hello, World!"}
```

Change the url to [http://localhost:8080/greeting?name=YourName](http://localhost:8080/greeting?name=YourName) to display:
```
{"id":2,"content":"Hello, YourName!"}
```

## Write Tests
Add a new 'testCompile' dependency to the ```build.gradle``` file:
```gradle
...
dependencies {
    compile("org.springframework.boot:spring-boot-starter-web")
    testCompile('org.springframework.boot:spring-boot-starter-test')
}
```

### Unit Tests
Create a subdirectory structure for your unit tests:

```bash
mkdir -p src/test/java/hello/unit
```

Create a test class:
```src/test/java/hello/unit/GreetingControllerTests.java```

```java
package hello.unit;

import hello.Greeting;
import hello.GreetingController;

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

```bash
./gradlew test -i
```
### Integration Tests
Create a subdirectory structure for your tests:

```basj
mkdir -p src/test/java/hello/integration
```

Create a test class:
```src/test/java/hello/unit/integration/GreetingControllerTests.java```

```java
package hello.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GreetingControllerTests {

	@Autowired
	private MockMvc mvc;

	@LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }
	
	@Test
	public void GreetingConroller_greeting_Mocked_ReturnsOkWithContent() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/greeting").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("{\"id\":1,\"content\":\"Hello, World!\"}")));
	}

	@Test
    public void GreetingConroller_greeting_Integration_ReturnsOkWithContent() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString() + "greeting",
                String.class);
        assertThat(response.getBody(), equalTo("{\"id\":1,\"content\":\"Hello, World!\"}"));
    }
}
```


## Dependency Injection
Let's extract the GreetingController logic into a service and inject it. First create the interface:

```src/java/hello/IGreetingService.java```

```java
package hello;

public interface IGreetingService {

    Greeting getGreeting(String name);
}
```

Next, implement the service:
```src/java/hello/GreetingService.java```

```java
package hello;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class GreetingService implements IGreetingService {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

	@Override
	public Greeting getGreeting(String name) {
        return new Greeting(
            counter.incrementAndGet(), 
            String.format(template, name)
        );
	}

    
}
```

The application needs to be configured to do IoC dependency injection. Create the following file:
```src/java/hello/AppConfig.java```

```java
package hello;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
 
@Configuration
@ComponentScan(basePackages = "hello")
public class AppConfig {
 
}
```

Now change the GreetingController to use @Autowire with constructor injection:
```java
...
@RestController
public class GreetingController {

    private IGreetingService service;

    @Autowired
    public GreetingController(IGreetingService service) {
        this.service = service;
    }

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return service.getGreeting(name);
    }
}
```

### Stubbing Components
Your IDE will probably have let you know that the unit test you wrote is now broken. This is because the GreetingController now requires a parameter in its constructor. If we want to isolate the controller from its dependencies then we should mock the service out. We can use (Mockito)[http://site.mockito.org/].

*Note: the test we're about to write is silly. But, for the sake of illustrating the use of Mockito, bear with me.*

First, modify the ```build.gradle``` files as follows:
```gradle
...
repositories {
    mavenCentral()
    jcenter() 
}
...
dependencies {
    compile("org.springframework.boot:spring-boot-starter-web")
    testCompile('org.springframework.boot:spring-boot-starter-test', 'org.mockito:mockito-core:2.+')
}
```

Then update the unit test file as follows:
```java
...
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GreetingControllerTests {

	@Test
	public void GreetingConroller_greeting_ReturnsGreeting() {
		// Arrange
		String name = "Joe Tester";
		int id = 1;
		GreetingService service = mock(GreetingService.class);
		GreetingController target = new GreetingController(service);

		when(service.getGreeting(name)).thenReturn(new Greeting(id, "Hello, "+ name +"!"));
		
		// Act
		Greeting result = target.greeting(name);

		// Assert
		assertEquals(id, result.getId());
		assertEquals("Hello, "+ name +"!", result.getContent());
	}

}
```