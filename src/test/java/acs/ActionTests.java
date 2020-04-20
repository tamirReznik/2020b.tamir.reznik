package acs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.rest.boundaries.action.ActionBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActionTests {
	private RestTemplate restTemplate;
	private String url;
	private int port;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + this.port + "/acs";
	}

	@Test
	public void test_Init_Server_with_5_Actions_When_We_Get_All_Actions_We_Receive_The_Same_Actions() {
		// GIVEN the server is up
		// do nothing
//		this.restTemplate.postForObject(url, new ActionBoundary(actionId, type, element, timeStap, invokedBy, actionAttributes, elementId), responseType)

	}

}
