package acs.user;

import acs.TypeEnumRole;

/*
{
	"email":"demo@us.er",
 	"role": "PLAYER",
	"username":"Demo User",
	"avatar":";-)"
}


*/
public class NewUserDetailsBoundary {
	private String email;
	private TypeEnumRole role;
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

	

	public TypeEnumRole getRole() {
		return role;
	}

	public void setRole(TypeEnumRole role) {
		this.role = role;
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
