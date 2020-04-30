package acs;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import acs.data.TypeEnum;
import acs.rest.boundaries.action.ActionBoundary;
import acs.rest.boundaries.action.ActionIdBoundary;
import acs.rest.boundaries.user.UserIdBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActionTests {

	private RestTemplate restTemplate;
	private String postUrl;
	private int port;
	private String delete_And_Get_Url;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.postUrl = "http://localhost:" + this.port + "/acs/actions";
		this.delete_And_Get_Url = "http://localhost:" + this.port + "/acs/admin/actions/{adminDomain}/{adminEmail}";
	}

	@BeforeEach
	public void setup() {
		this.restTemplate.delete(delete_And_Get_Url, "adminDomain", "adminEmail");
	}

	@AfterEach
	public void tear_down() {
		this.restTemplate.delete(delete_And_Get_Url, "adminDomain", "adminEmail");
	}

//Test for POST + GET + Service reliability
	@Test
	public void test_Init_Server_with_5_Actions_When_We_Get_All_Actions_We_Receive_The_Same_Actions() {

		// GIVEN the server is up

		// 5 actions on server
		List<ActionBoundary> allActionsInDb = IntStream.range(1, 6).mapToObj(i -> {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			try {

				return new HttpEntity<ActionBoundary>(invoke_Random_ActionBoundary_For_Tests(), headers);
			} catch (JsonProcessingException e) {

				e.printStackTrace();
			}
			return null;

		}).map(entity -> this.restTemplate.postForObject(this.postUrl, entity, ActionBoundary.class))
				.collect(Collectors.toList());

		// GET all actions
		Map<String, String> params = new HashMap<String, String>();
		params.put("adminDomain", "adminDomain");
		params.put("adminEmail", "adminEmail");
		ActionBoundary[] results = this.restTemplate.getForObject(this.delete_And_Get_Url, ActionBoundary[].class,
				params);

		// make sure that the actions on server are match to the actions we got from Get
		// method
		assertThat(results).hasSize(allActionsInDb.size()).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(allActionsInDb);

		this.restTemplate.delete(delete_And_Get_Url, "adminDomain", "adminEmail");
		results = this.restTemplate.getForObject(this.delete_And_Get_Url, ActionBoundary[].class, params);
		assertThat(results).isEmpty();

	}

	public ActionBoundary invoke_Random_ActionBoundary_For_Tests() throws JsonProcessingException {
		Map<String, Object> element = new HashMap<String, Object>();
		Map<String, Object> invokedBy = new HashMap<String, Object>();
		Map<String, Object> actionAttributes = new HashMap<String, Object>();

		element.put("domain", "tamir");
		element.put("id", "6464");

		invokedBy.put("userId", new UserIdBoundary("2020b", "t@gmail.com"));

		actionAttributes.put("key1", "value1");
		actionAttributes.put("key2", "value2");
		actionAttributes.put("key3", "value3");

		return new ActionBoundary(new ActionIdBoundary("unvalid name", null), TypeEnum.actionType, element, new Date(),
				invokedBy, actionAttributes);
	}

}
