package demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@ExtendWith(SpringExtension.class)
@WebMvcTest(HelloController.class)	
public class HelloControllerTest {
	
	@Autowired
	private MockMvc mvc;

	@Test
	void helloTest() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/hello");
		MvcResult results =mvc.perform(request).andReturn();
		assertEquals("{\"message\":\"Hello World\"}", results.getResponse().getContentAsString());
	}
}
