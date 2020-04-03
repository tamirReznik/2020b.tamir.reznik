package acs.logic;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import acs.NameNotFoundException;
import acs.TypeEnumRole;
import acs.rest.boundaries.UserBoundary;
import acs.rest.boundaries.UserIdBoundary;

@Service
public class UserServiceImplementation implements UserService {

	////Anna&Sapir - done
	public UserBoundary createUser(UserBoundary user) {
		user.setUserId(new UserIdBoundary("", user.getUserId().getEmail()));
		user.getRole();//user.setRole(user.getRole());
		user.getUsername();//user.setUsername(user.getUsername());
		user.getAvatar();//user.setAvatar(user.getAvatar());
		return user;
	}
	
	//Anna&Sapir - done
	@Override
	public UserBoundary login(String userDomain, String userEmail) {
		if (userDomain != null && !userDomain.trim().isEmpty() && userEmail != null && !userEmail.trim().isEmpty()) {
			UserBoundary ub = new UserBoundary();
			ub.setUserId(new UserIdBoundary(userDomain, userEmail));
			ub.setRole(TypeEnumRole.PLAYER);
			ub.setUsername("Demo User");
			ub.setAvatar(";-)");
			return ub;
		} else {
			throw new NameNotFoundException("Invalid user name/email");
		}
	}

	@Override
	public UserBoundary updateUser(String UserDomain, String userEmail, UserBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	//Anna&Sapir - done
	@Override
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		return IntStream.range(0, 5) // Stream of Integer
				.mapToObj(i -> login(adminDomain, adminEmail)) // Stream of UserBoundry
				.collect(Collectors.toList()); // List of UserBoundry 
	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub
		
	}

}
