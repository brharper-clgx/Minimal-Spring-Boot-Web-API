// package hello.integration;

// import static org.hamcrest.Matchers.equalTo;
// import static org.junit.Assert.assertThat;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.net.URL;

// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.web.server.LocalServerPort;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

// @RunWith(SpringRunner.class)
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @AutoConfigureMockMvc
// public class GreetingContorllerTests {

// 	@Autowired
// 	private MockMvc mvc;

// 	@LocalServerPort
//     private int port;

//     private URL base;

//     @Autowired
//     private TestRestTemplate template;

//     @Before
//     public void setUp() throws Exception {
//         this.base = new URL("http://localhost:" + port + "/");
//     }
	
// 	@Test
// 	public void GreetingConroller_greeting_Mocked_ReturnsOkWithContent() throws Exception {
// 		mvc.perform(MockMvcRequestBuilders.get("/greeting").accept(MediaType.APPLICATION_JSON))
// 				.andExpect(status().isOk())
// 				.andExpect(content().string(equalTo("{\"id\":1,\"content\":\"Hello, World!\"}")));
// 	}

// 	@Test
//     public void GreetingConroller_greeting_Integration_ReturnsOkWithContent() throws Exception {
//         ResponseEntity<String> response = template.getForEntity(base.toString(),
//                 String.class);
//         assertThat(response.getBody(), equalTo("{\"id\":1,\"content\":\"Hello, World!\"}"));
//     }
// }

