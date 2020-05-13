package acs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import acs.data.TypeEnum;
import acs.data.UserRole;
import acs.rest.boundaries.element.ElementBoundary;
import acs.rest.boundaries.element.ElementIdBoundary;
import acs.rest.boundaries.element.Location;
import acs.rest.boundaries.user.NewUserDetailsBoundary;
import acs.rest.boundaries.user.UserBoundary;
import acs.rest.boundaries.user.UserIdBoundary;

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
		ElementBoundary eb = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType.name(), "moshe", true,
				new Date(), new Location(), null, null);
		NewUserDetailsBoundary ub = new NewUserDetailsBoundary("demo@us.er", UserRole.MANAGER, "demo1", ":(");
		UserBoundary postedUB = this.restTemplate.postForObject(this.url +"/users", ub, UserBoundary.class);
		
		ElementIdBoundary postedElementId = this.restTemplate
				.postForObject(this.url + "/elements/"+postedUB.getUserId().getDomain()+"/"+postedUB.getUserId().getEmail(), eb, ElementBoundary.class).getElementId();

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
		ElementBoundary eb = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType.name(), "moshe", true,
				new Date(), new Location(), null, null);
		NewUserDetailsBoundary ub = new NewUserDetailsBoundary("demo@us.er", UserRole.MANAGER, "demo1", ":(");
		UserBoundary postedUB = this.restTemplate.postForObject(this.url +"/users", ub, UserBoundary.class);
		
		this.restTemplate.postForObject(this.url + "/elements/"+postedUB.getUserId().getDomain()+"/"+postedUB.getUserId().getEmail(), eb, ElementBoundary.class).getElementId();

		ElementBoundary[] allElements = this.restTemplate.getForObject(this.url + "/elements/aaa/bbb",
				ElementBoundary[].class);
		if (allElements.length != 1)
			throw new Exception("error");
	}

	@Test
	public void test_Create_Two_Elements_Get_Specific_One_And_See_If_ID_Matches() throws Exception {
		ElementBoundary eb1 = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType.name(), "moshe", true,
				new Date(), new Location(), null, null);
		ElementBoundary eb2 = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType.name(), "david", true,
				new Date(), new Location(), null, null);

		NewUserDetailsBoundary ub = new NewUserDetailsBoundary("demo@us.er", UserRole.MANAGER, "demo1", ":(");
		UserBoundary postedUB = this.restTemplate.postForObject(this.url +"/users", ub, UserBoundary.class);
		
		ElementBoundary neweb1 = this.restTemplate
				.postForObject(this.url + "/elements/"+postedUB.getUserId().getDomain()+"/"+postedUB.getUserId().getEmail(), eb1,
				ElementBoundary.class);
		this.restTemplate
		.postForObject(this.url + "/elements/"+postedUB.getUserId().getDomain()+"/"+postedUB.getUserId().getEmail(), eb2, ElementBoundary.class);

		ElementBoundary ebCheck = this.restTemplate.getForObject(this.url + "/elements/aaa/bbb/"
				+ neweb1.getElementId().getDomain() + "/" + neweb1.getElementId().getId(), ElementBoundary.class);

		if (!ebCheck.getElementId().getId().equals(neweb1.getElementId().getId()))
			throw new Exception("error");
	}

	@Test
	public void test_Create_Two_Elements_Delete_All_Elements_And_Check_If_Delete_Succeeded() throws Exception {
		ElementBoundary eb1 = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType.name(), "moshe", true,
				new Date(), new Location(), null, null);
		ElementBoundary eb2 = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType.name(), "david", true,
				new Date(), new Location(), null, null);
		NewUserDetailsBoundary ub = new NewUserDetailsBoundary("demo@us.er", UserRole.MANAGER, "demo1", ":(");
		UserBoundary postedUB = this.restTemplate.postForObject(this.url +"/users", ub, UserBoundary.class);
		this.restTemplate.postForObject(this.url + "/elements/"+postedUB.getUserId().getDomain()+"/"+postedUB.getUserId().getEmail(), eb1, ElementBoundary.class);
		this.restTemplate.postForObject(this.url + "/elements/"+postedUB.getUserId().getDomain()+"/"+postedUB.getUserId().getEmail(), eb2, ElementBoundary.class);

		this.restTemplate.delete(this.url + "/admin/elements/{adminDomain}/{adminEmail}", "???", "??");

		ElementBoundary[] allElements = this.restTemplate.getForObject(this.url + "/elements/aaa/bbb",
				ElementBoundary[].class);
		if (allElements.length != 0)
			throw new Exception("error, delete failed");
	}

	@Test
	public void test_Update_Element_And_Check_If_Update_Succeeded() throws Exception {
		NewUserDetailsBoundary ub = new NewUserDetailsBoundary("demo@us.er", UserRole.MANAGER, "demo1", ":(");
		UserBoundary postedUB = this.restTemplate.postForObject(this.url +"/users", ub, UserBoundary.class);
		ElementBoundary eb = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType.name(), "moshe", true,
				new Date(), new Location(), null, null);
		ElementIdBoundary postedElementId = this.restTemplate
				.postForObject(this.url + "/elements/"+postedUB.getUserId().getDomain()+"/"+postedUB.getUserId().getEmail(), eb, ElementBoundary.class).getElementId();
		eb.setName("new_name");

		this.restTemplate.put(this.url + "/elements/aaa/bbb/{elementDomain}/{elementId}", eb,
				postedElementId.getDomain(), postedElementId.getId());

		ElementBoundary[] allElements = this.restTemplate.getForObject(this.url + "/elements/aaa/bbb",
				ElementBoundary[].class);
		if (!allElements[0].getName().equals("new_name"))
			throw new Exception("error");
	}

	@Test
	public void test_Create_Three_Elements_Bind_Them_And_Validate_Relation() throws Exception {
		// GIVEN the server is up
		// WHEN we create 3 elements
		ElementBoundary parent = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType.name(), "Parent",
				true, new Date(), new Location(0.5, 0.5), null, null);
		ElementBoundary child1 = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType.name(), "child1",
				true, new Date(), new Location(0.5, 0.5), null, null);
		ElementBoundary child2 = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType.name(), "child2",
				true, new Date(), new Location(0.5, 0.5), null, null);

		NewUserDetailsBoundary ub = new NewUserDetailsBoundary("demo@us.er", UserRole.MANAGER, "demo1", ":(");
		UserBoundary postedUB = this.restTemplate.postForObject(this.url +"/users", ub, UserBoundary.class);
		// post them
		ElementBoundary postedChild1Element = this.restTemplate
				.postForObject(this.url +"/elements/"+postedUB.getUserId().getDomain()+"/"+postedUB.getUserId().getEmail(),
						child1,ElementBoundary.class);
		ElementBoundary postedChild2Element = this.restTemplate
				.postForObject(this.url + "/elements/"+postedUB.getUserId().getDomain()+"/"+postedUB.getUserId().getEmail(),
						child2,ElementBoundary.class);
		ElementBoundary postedParentElement = this.restTemplate
				.postForObject(this.url + "/elements/"+postedUB.getUserId().getDomain()+"/"+postedUB.getUserId().getEmail(),
						parent, ElementBoundary.class);

		List<ElementBoundary> allChildBeforeBind = new ArrayList<>();
		allChildBeforeBind.add(postedChild1Element);
		allChildBeforeBind.add(postedChild2Element);

		List<ElementBoundary> allParentsBeforeBind = new ArrayList<>();
		allParentsBeforeBind.add(postedParentElement);

		// bind children to the parent
		this.restTemplate.put(
				this.url + "/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				postedChild1Element.getElementId(), "???", "???", postedParentElement.getElementId().getDomain(),
				postedParentElement.getElementId().getId());
		this.restTemplate.put(
				this.url + "/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				postedChild2Element.getElementId(), "???", "???", postedParentElement.getElementId().getDomain(),
				postedParentElement.getElementId().getId());

		// AND get all children
		ElementBoundary[] allChilds = this.restTemplate.getForObject(
				this.url + "/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				ElementBoundary[].class, "???", "???", postedParentElement.getElementId().getDomain(),
				postedParentElement.getElementId().getId());

		// AND get all parents
		ElementBoundary[] allParents = this.restTemplate.getForObject(
				this.url + "/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/parents",
				ElementBoundary[].class, "???", "???", postedChild1Element.getElementId().getDomain(),
				postedChild1Element.getElementId().getId());

		// THEN we get the same array with the childrens
		assertThat(allChilds).hasSize(allChildBeforeBind.size()).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(allChildBeforeBind);

		// THEN we get the same array with the Parents
		assertThat(allParents).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(allParentsBeforeBind);

	}

	@Test
	public void test_Create_One_Element_Check_For_Empty_Array_In_Childrens_Of_Element() throws Exception {
		// GIVEN the server is up
		ElementBoundary element = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType.name(), "Parent",
				true, new Date(), new Location(0.5, 0.5), null, null);

		NewUserDetailsBoundary ub = new NewUserDetailsBoundary("demo@us.er", UserRole.MANAGER, "demo1", ":(");
		UserBoundary postedUB = this.restTemplate.postForObject(this.url +"/users", ub, UserBoundary.class);

		// WHEN we post the element
		ElementBoundary postedElement = this.restTemplate
				.postForObject(this.url + "/elements/"+postedUB.getUserId().getDomain()+"/"+postedUB.getUserId().getEmail(), element,
				ElementBoundary.class);

		// get all children of the element
		ElementBoundary[] allChilds = this.restTemplate.getForObject(
				this.url + "/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				ElementBoundary[].class, "???", "???", postedElement.getElementId().getDomain(),
				postedElement.getElementId().getId());

		// THEN we get an empty array
		assertThat(allChilds).isEmpty();

	}

	@Test
	public void test_Create_One_Element_Check_For_Empty_Array_In_Parents_Of_Element() throws Exception {
		// GIVEN the server is up
		ElementBoundary element = new ElementBoundary(new ElementIdBoundary(), TypeEnum.actionType.name(), "Parent",
				true, new Date(), new Location(0.5, 0.5), null, null);
		NewUserDetailsBoundary ub = new NewUserDetailsBoundary("demo@us.er", UserRole.MANAGER, "demo1", ":(");
		UserBoundary postedUB = this.restTemplate.postForObject(this.url +"/users", ub, UserBoundary.class);
		
		// WHEN we post the element
		ElementBoundary postedElement = this.restTemplate.postForObject(this.url + "/elements/"+postedUB.getUserId().getDomain()+"/"+postedUB.getUserId().getEmail(),
				element, ElementBoundary.class);

		// get all parents of the element
		ElementBoundary[] allChilds = this.restTemplate.getForObject(
				this.url + "/elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}/parents",
				ElementBoundary[].class, "???", "???", postedElement.getElementId().getDomain(),
				postedElement.getElementId().getId());

		// THEN we get an empty array
		assertThat(allChilds).isEmpty();

	}
}
