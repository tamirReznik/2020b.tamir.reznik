package acs.rest.boundaries;

import acs.TypeEnumRole;

/*
{	
	"userId":{
	"domain":"2020b.tamir.reznik",
	"email":"demo@us.er"
	},
 	"role": "PLAYER",
	"username":"Demo User",
	"avatar":";-)"
}


*/
public class UserBoundary {
	private UserIdBoundary userId;
	private TypeEnumRole role;
	private String username;
	private String avatar;

	public UserBoundary() {
	}


	public UserBoundary(UserIdBoundary userId, TypeEnumRole role, String username, String avatar) {
		this.userId = userId;
		this.role = role;
		this.username = username;
		this.avatar = avatar;
	}

	public UserIdBoundary getUserId() {
		return userId;
	}

	public void setUserId(UserIdBoundary userId) {
		this.userId = userId;
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

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
