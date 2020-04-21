package acs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
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
	//
	@BeforeEach
	public void setup() {
	
	}
	
	@AfterEach
	public void teardown() {
		this.restTemplate
			.delete(this.url + "/admin/users/{adminDomain}/{adminEmail}","???","??");
	}
	
	@Test
	public void testContext() {
		
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
	public void test_Post_New_User_Then_The_Database_Contains_A_Single_User() throws Exception {
		// GIVEN the server is up
			// do nothing
				
		// WHEN I POST new user
		this.restTemplate.postForObject(this.url +"/users", new NewUserDetailsBoundary("Sapir@gmail.com", UserRole.PLAYER , "sapir", ":-)"),
				UserBoundary.class);
		
		//THEN the database contains a single message
		assertThat(this.restTemplate.getForObject(this.url + "/admin/users/{adminDomain}/{adminEmail}",
				UserBoundary[].class,"???","??"))
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
		
		// THEN the database contains a user with user role attribute "PLAYER"
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
				(this.url + "/users", new NewUserDetailsBoundary("???", UserRole.PLAYER , "anna", "???"),
						UserBoundary.class);
		
		// THEN the database contains a user with userName attribute "anna"
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
	
	@Test
	public void test_Post_New_User_Then_The_Database_Contains_User_With_The_Same_User_Attribute_Avatar() throws Exception{
		// GIVEN the server is up
			// do nothing
		
		// WHEN I POST new user with avatar attribute: ":-))"
		
		this.restTemplate.postForObject
				(this.url + "/users", new NewUserDetailsBoundary("???", UserRole.PLAYER , "???", ":-))"),
						UserBoundary.class);
		
		// THEN the database contains a user with user avatar attribute ":-))"
		UserBoundary[] allUsers = 
				this.restTemplate
					.getForObject(this.url + "/admin/users/{adminDomain}/{adminEmail}", UserBoundary[].class, "???", "??");
		boolean containsAvatarAttribute = false;
		for (UserBoundary m : allUsers) {
			if (m.getAvatar().equals(":-))")) {
				containsAvatarAttribute = true;
			}
		}
		
		if (!containsAvatarAttribute) {
			throw new Exception("failed to locate user with proper attribute avatar");
		}
						
		
	}
	

//	@Test 
//	public void test_Put_Update_User_Attribute_Role_To_MANAGER_Then_Role_Is_Updated_In_The_DataBase() throws Exception{
//	// GIVEN the server is up
//		// do nothing
//		
//	// AND the database contains a single user with role: PLAYER	
//	UserBoundary boundaryOnServer=
//			this.restTemplate.postForObject
//			(this.url + "/users", new NewUserDetailsBoundary("sapir@gmail.com", UserRole.PLAYER , "sapir", ":-))"),
//			UserBoundary.class);
//	
//	String postedUserId = boundaryOnServer.getUserId().toString();
//	String userDomain = postedUserId.substring(0, postedUserId.indexOf('#')); 
//	String userEmail = postedUserId.substring(postedUserId.indexOf('#') + 1);
//				
//	UserIdBoundary uib = new UserIdBoundary(userDomain,userEmail);
//	
//	// WHEN I PUT with update of role to be "MANAGER"
//	UserBoundary update = new UserBoundary();
//	update.setRole(UserRole.MANAGER);
//	this.restTemplate
//	.put(this.url + "/users/{userDomain}/{userEmail}", update, userDomain, userEmail);
//	
//	// THEN the database contains a user with same id and role: MANAGER
//	assertThat(this.restTemplate
//		.getForObject(this.url + "/users/login/{userDomain}/{userEmail}", UserBoundary.class , userDomain, userEmail))
//		.extracting("userId","role") //??
//		.containsExactly(postedUserId, update.getRole()); //??
//	}
	
	
	
	@Test
	public void test_Init_Server_With_3_Users_When_We_Get_All_Users_We_Receive_The_Same_Users_Initialized() throws Exception {
		// GIVEN the server is up
		// AND the server contains 3 users
		List<UserBoundary> allUsersInDb = 
		  IntStream.range(1, 4)
		  .mapToObj(i -> ("email" + i))
			.map(user->new NewUserDetailsBoundary(user, UserRole.PLAYER , "sapir", ":-)"))
			.map(boundary-> this.restTemplate
						.postForObject(
								this.url+ "/users", 
								boundary, 
								UserBoundary.class))
			.collect(Collectors.toList()); 
		
		System.err.println(allUsersInDb.size());
		for (UserBoundary m : allUsersInDb) {
					System.err.println(m);
				}
		// WHEN I GET /admin/users/{adminDomain}/{adminEmail}
		UserBoundary[] allUsers = 
				this.restTemplate
					.getForObject(this.url + "/admin/users/{adminDomain}/{adminEmail}", UserBoundary[].class, "???", "??");
		for (UserBoundary m : allUsers) {
			System.err.println(m);
		}
		// THEN The server returns the same 3 users initialized
		assertThat(allUsers)
			.hasSize(allUsersInDb.size())
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactlyInAnyOrderElementsOf(allUsersInDb);
	}
	
	
	@Test
	public void test_The_Database_Is_Empty_By_Default() throws Exception {
		// GIVEN the server is up
		
		// WHEN I GET for all users
		UserBoundary[] actual = this.restTemplate
				.getForObject(this.url+ "/admin/users/{adminDomain}/{adminEmail}", UserBoundary[].class, "???", "??");
		
		// THEN the returned value is an empty array
		assertThat(actual).isEmpty();
	}
}
