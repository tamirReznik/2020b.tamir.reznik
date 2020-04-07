package acs.data;

import acs.TypeEnumRole;
import acs.rest.boundaries.UserIdBoundary;

public class UserEntity {
	private UserIdBoundary userId;
	private TypeEnumRole role;
	private String username;
	private String avatar;
	
	
	public UserEntity() {
	}


	public UserEntity(UserIdBoundary userId, TypeEnumRole role, String username, String avatar) {
		super();
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
