package acs.data;



public class UserEntity {
	private UserIdEntity userId;
	private String role;
	private String username;
	private String avatar;

	public UserEntity() {
	}

	public UserEntity(UserIdEntity userId, String role, String username, String avatar) {
		super();
		this.userId = userId;
		this.role = role;
		this.username = username;
		this.avatar = avatar;
	}

	public UserIdEntity getUserId() {
		return userId;
	}

	public void setUserId(UserIdEntity userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
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
