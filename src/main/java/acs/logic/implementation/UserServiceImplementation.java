package acs.logic.implementation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import acs.data.Converter;
import acs.data.UserEntity;
import acs.logic.ObjectNotFoundException;
import acs.logic.UserService;
import acs.rest.boundaries.user.UserBoundary;

@Service
public class UserServiceImplementation implements UserService {

	private String projectName;
	private Map<String, UserEntity> usersDatabase;
	private Converter converter;

	@Autowired
	public UserServiceImplementation(Converter converter) {
		this.converter = converter;
	}

	// injection of project name from the spring boot configuration
	@Value("${spring.application.name: generic}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@PostConstruct
	public void init() {
		// since this class is a singleton, we generate a thread safe collection
		this.usersDatabase = Collections.synchronizedMap(new TreeMap<>());
	}

	public UserBoundary createUser(UserBoundary user) {

		UserEntity entity = this.converter.toEntity(user);

		this.usersDatabase.put(user.getUserId().getEmail(), entity); // ??

		return this.converter.fromEntity(entity);
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {

		UserEntity existing = this.usersDatabase.get(userEmail); // ???
		if (existing != null) {
			return this.converter.fromEntity(existing);
		} else {
			throw new ObjectNotFoundException(
					"could not find object by UserDomain: " + userDomain + "or userEmail: " + userEmail);
		}

	}

	@Override // represent enum as string in entity
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		UserEntity existing = this.usersDatabase.get(userEmail); // ??

		if (existing == null) {
			throw new ObjectNotFoundException(
					"could not find object by UserDomain: " + userDomain + "or userEmail: " + userEmail);
		}

		else {
			if (update.getRole() != null) {
				existing.setRole(update.getRole());
				// this.usersDatabase.put(??, existing);
			}
			if (update.getUsername() != null) {
				existing.setUsername(update.getUsername());
				// this.usersDatabase.put(??, existing);
			}
			if (update.getAvatar() != null) {
				existing.setAvatar(update.getAvatar());
				// this.usersDatabase.put(??, existing);
			}

			return this.converter.fromEntity(existing);
		}

	}

	@Override // check for null in arguments
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		return this.usersDatabase // Map<String, UserEntity>
				.values() // Collection<UserEntity>
				.stream() // Stream<UserEntity>
				.map(e -> this.converter.fromEntity(e)) // Stream<UserBoundary>
				.collect(Collectors.toList()); // List<UserBoundary>
	}

	@Override // check for null in arguments
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		this.usersDatabase.clear();
	}

}
