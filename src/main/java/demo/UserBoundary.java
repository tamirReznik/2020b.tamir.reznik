package demo;


/*
{
	"userId":{
			"domain":"2020b.demo",
			"email":"demo@us.er"
		},
	"role":"PLAYER",
	"username":"Demo User",
	"avater":";-)"
}


*/
public class UserBoundary {
	private UserIdBoundary userId;
	private TypeEnumRole typeRole;
	private String username;
	private String avater;
	
	public UserBoundary() {
	}

	public UserIdBoundary getUserId() {
		return userId;
	}

	public void setUserId(UserIdBoundary userId) {
		this.userId = userId;
	}

	public TypeEnumRole getTypeRole() {
		return typeRole;
	}

	public void setTypeRole(TypeEnumRole typeRole) {
		this.typeRole = typeRole;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvater() {
		return avater;
	}

	public void setAvater(String avater) {
		this.avater = avater;
	}

	
	
	
	
}
