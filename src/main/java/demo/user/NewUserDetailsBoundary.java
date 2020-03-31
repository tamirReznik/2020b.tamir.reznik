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
public class NewUserDetailsBoundary {
	private String email;
	private TypeEnumRole typeRole;
	private String username;
	private String avatar;

	public NewUserDetailsBoundary() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avater) {
		this.avatar = avater;
	}

}
