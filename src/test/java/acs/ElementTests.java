package acs;

import java.util.Date;

import javax.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import acs.data.TypeEnum;
import acs.rest.boundaries.element.ElementBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;
import acs.rest.boundaries.element.Location;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementTests {
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

	@AfterEach
	public void teardown() {
		this.restTemplate.delete(this.url + "/admin/elements/{adminDomain}/{adminEmail}", "???", "??");
	}

	@Test
	public void testContext() {

	}

	@Test
	public void test_Create_New_Element_And_Check_If_DB_Contatins_Same_ElementID() throws Exception {
		ElementBoundary eb = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType, "moshe", true,
				new Date(), new Location(), null, null);
		ElementIdBoundary postedElementId = this.restTemplate
				.postForObject(this.url + "/elements/aaa/bbb", eb, ElementBoundary.class).getElementId();

		ElementBoundary[] allElements = this.restTemplate.getForObject(this.url + "/elements/aaa/bbb",
				ElementBoundary[].class);
		boolean exist = false;
		for (ElementBoundary element : allElements)
			if (postedElementId.getId().equals(element.getElementId().getId()))
				exist = true;
		if (!exist)
			throw new Exception("not found");

	}

	@Test
	public void test_Create_New_Element_And_Check_If_DB_Contatins_Exactly_One_Element() throws Exception {
		ElementBoundary eb = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType, "moshe", true,
				new Date(), new Location(), null, null);
		this.restTemplate.postForObject(this.url + "/elements/aaa/bbb", eb, ElementBoundary.class).getElementId();

		ElementBoundary[] allElements = this.restTemplate.getForObject(this.url + "/elements/aaa/bbb",
				ElementBoundary[].class);
		if (allElements.length != 1)
			throw new Exception("error");
	}

	@Test // this test doesn't work yet!
	public void test_Create_Two_Elements_Get_Specific_One_And_See_If_ID_Matches() throws Exception {
		ElementBoundary eb1 = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType, "moshe", true,
				new Date(), new Location(), null, null);
		ElementBoundary eb2 = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType, "david", true,
				new Date(), new Location(), null, null);

		ElementBoundary neweb1 = this.restTemplate.postForObject(this.url + "/elements/aaa/bbb", eb1, ElementBoundary.class);
		this.restTemplate.postForObject(this.url + "/elements/aaa/bbb", eb2, ElementBoundary.class);
		
		ElementBoundary ebCheck = this.restTemplate.getForObject(
				this.url + "/elements/aaa/bbb/"+neweb1.getElementId().getDomain()+"/"+ neweb1.getElementId().getId(), ElementBoundary.class);
		
		if (!ebCheck.getElementId().getId().equals(neweb1.getElementId().getId()))
			throw new Exception("error");
	}

	@Test
	public void test_Create_Two_Elements_Delete_All_Elements_And_Check_If_Delete_Succeeded() throws Exception {
		ElementBoundary eb1 = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType, "moshe", true,
				new Date(), new Location(), null, null);
		ElementBoundary eb2 = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType, "david", true,
				new Date(), new Location(), null, null);

		this.restTemplate.postForObject(this.url + "/elements/aaa/bbb", eb1, ElementBoundary.class);
		this.restTemplate.postForObject(this.url + "/elements/aaa/bbb", eb2, ElementBoundary.class);

		this.restTemplate.delete(this.url + "/admin/elements/{adminDomain}/{adminEmail}", "???", "??");

		ElementBoundary[] allElements = this.restTemplate.getForObject(this.url + "/elements/aaa/bbb",
				ElementBoundary[].class);
		if (allElements.length != 0)
			throw new Exception("error, delete failed");
	}

	@Test
	public void test_Update_Element_And_Check_If_Update_Succeeded() throws Exception {
		ElementBoundary eb = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType, "moshe", true,
				new Date(), new Location(), null, null);
		ElementIdBoundary postedElementId = this.restTemplate
				.postForObject(this.url + "/elements/aaa/bbb", eb, ElementBoundary.class).getElementId();
		eb.setName("new_name");

		this.restTemplate.put(this.url + "/elements/aaa/bbb/{elementDomain}/{elementId}", eb,
				postedElementId.getDomain(), postedElementId.getId());

		ElementBoundary[] allElements = this.restTemplate.getForObject(this.url + "/elements/aaa/bbb",
				ElementBoundary[].class);
		if (!allElements[0].getName().equals("new_name"))
			throw new Exception("error");
	}
}
