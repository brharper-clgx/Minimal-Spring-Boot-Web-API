package hello.unit;
import hello.Greeting;
import hello.GreetingController;
import hello.GreetingService;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
