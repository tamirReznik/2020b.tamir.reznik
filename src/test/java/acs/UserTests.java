package acs;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.support.BeanDefinitionDsl.Role;
import org.springframework.web.client.RestTemplate;

import acs.data.UserRole;
import acs.rest.boundaries.user.NewUserDetailsBoundary;
import acs.rest.boundaries.user.UserBoundary;



@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserTests {
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
	public void test_Post_Create_New_User_Then_The_Database_Contains_Same_User_With_Same_Id() throws Exception {
		// GIVEN the server is up
		// do nothing
		
		// WHEN I POST new user
		String postedUserId=
				this.restTemplate.postForObject
				(this.url + "/users", new NewUserDetailsBoundary("Sapir@gmail.com", UserRole.PLAYER , "sapir", ":-)"),
						UserBoundary.class).getUserId().toString();
		
		//split the userId to Domain and Email
		String userDomain = postedUserId.substring(0, postedUserId.indexOf('#')); 
		String userEmail = postedUserId.substring(postedUserId.indexOf('#') + 1);
					
		// THEN the database contains a single user with same userId as posted
		String actualUserId= 
				this.restTemplate.getForObject
				(this.url +"/users/login/{userDomain}/{userEmail}", UserBoundary.class,userDomain,userEmail)
				.getUserId().toString();
		
		assertThat(actualUserId)
		.isNotNull()
		.isEqualTo(postedUserId);
	}
	
	
	@Test
	public void test_Post_New_User_Then_The_Database_Contains_As_Single_User() throws Exception {
		// GIVEN the server is up
			// do nothing
				
		// WHEN I POST new user
		this.restTemplate.postForObject(this.url +"/users", new NewUserDetailsBoundary("Sapir@gmail.com", UserRole.PLAYER , "sapir", ":-)"),
				UserBoundary.class);
		
		//THEN the database contains a single message
		assertThat(this.restTemplate.getForObject(this.url + "/admin/users/{adminDomain}/{adminEmail}",
				UserBoundary[].class,"???","????"))
		.hasSize(1);
	}
	
	@Test
	public void test_Post_New_User_Then_The_Database_Contains_User_With_The_Same_User_Attribute_Role() throws Exception{
		// GIVEN the server is up
			// do nothing
		
		// WHEN I POST new user with user role attribute: "PLAYER"
		
		this.restTemplate.postForObject
				(this.url + "/users", new NewUserDetailsBoundary("???", UserRole.PLAYER , "???", "???"),
						UserBoundary.class);
		
		// THEN the database contains a user with the user role attribute "PLAYER"
		UserBoundary[] allUsers = 
				this.restTemplate
					.getForObject(this.url + "/admin/users/{adminDomain}/{adminEmail}", UserBoundary[].class, "???", "??");
		boolean containsPlayerRole = false;
		for (UserBoundary m : allUsers) {
			if (m.getRole().equals(UserRole.PLAYER)) {
				containsPlayerRole = true;
			}
		}
		
		if (!containsPlayerRole) {
			throw new Exception("failed to locate user with proper attribute role");
		}
						
		
	}
	
	@Test
	public void test_Post_New_User_Then_The_Database_Contains_User_With_The_Same_User_Attribute_UserName() throws Exception{
		// GIVEN the server is up
			// do nothing
		
		// WHEN I POST new user with userName attribute: "anna"
		
		this.restTemplate.postForObject
				(this.url + "/users", new NewUserDetailsBoundary("???", UserRole.NONE , "anna", "???"),
						UserBoundary.class);
		
		// THEN the database contains a user with the userName attribute "anna"
		UserBoundary[] allUsers = 
				this.restTemplate
					.getForObject(this.url + "/admin/users/{adminDomain}/{adminEmail}", UserBoundary[].class, "???", "??");
		boolean containsUserNameAnna = false;
		for (UserBoundary m : allUsers) {
			if (m.getUsername().equals("anna")) {
				containsUserNameAnna = true;
			}
		}
		
		if (!containsUserNameAnna) {
			throw new Exception("failed to locate user with proper attribute user name");
		}
						
		
	}
}
