package demo.user;

import demo.TypeEnumRole;

/*
{
	"email":"demo@us.er",
 	"typeRole": "PLAYER",
	"username":"Demo User",
	"avatar":";-)"
}


*/
public class UserBoundary {
	private UserIdBoundary userId;
	private TypeEnumRole typeRole;
	private String username;
	private String avatar;

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
		return avatar;
	}

	public void setAvater(String avater) {
		this.avatar = avater;
	}

}
