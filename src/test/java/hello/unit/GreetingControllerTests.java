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
