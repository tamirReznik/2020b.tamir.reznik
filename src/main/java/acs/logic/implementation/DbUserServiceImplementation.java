package acs.logic.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import acs.dal.UserDao;
import acs.data.Converter;
import acs.data.UserEntity;
import acs.data.UserIdEntity;
import acs.logic.ObjectNotFoundException;
import acs.logic.ServiceTools;
import acs.logic.UserService;
import acs.rest.boundaries.user.UserBoundary;
import java.util.regex.Pattern;

@Service
public class DbUserServiceImplementation implements UserService {
	private String projectName;
	private UserDao userDao;
	private Converter converter;

	@Autowired
	public DbUserServiceImplementation(UserDao userDao, Converter converter) {
		this.userDao = userDao;
		this.converter = converter;
	}

	// injection of project name from the spring boot configuration
	@Value("${spring.application.name: generic}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	@Transactional // (readOnly = false)
	public UserBoundary createUser(UserBoundary user) {

		// Email integrity check
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (!(pat.matcher(user.getUserId().getEmail()).matches()))
			throw new ObjectNotFoundException("Invalid email address");

		ServiceTools.stringValidation(user.getAvatar(), user.getUsername());

		user.getUserId().setDomain(projectName);

		UserEntity entity = this.converter.toEntity(user);

		return this.converter.fromEntity(this.userDao.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userDomain, String userEmail) {
		// UserEntity existing = this.usersDatabase.get(userDomain + "#" + userEmail);
		ServiceTools.stringValidation(userDomain, userEmail);

		UserIdEntity userId = new UserIdEntity(userDomain, userEmail);
		Optional<UserEntity> existing = this.userDao.findById(userId);

		if (existing.isPresent())
			return this.converter.fromEntity(existing.get());
		else
			throw new ObjectNotFoundException(
					"could not find object by UserDomain: " + userDomain + "or userEmail: " + userEmail);

	}

	@Override
	@Transactional // (readOnly = false)
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {

		ServiceTools.stringValidation(userDomain, userEmail);

		UserIdEntity userId = new UserIdEntity(userDomain, userEmail);
		UserEntity existing = this.userDao.findById(userId).orElseThrow(() -> new ObjectNotFoundException(
				"could not find object by UserDomain: " + userDomain + "or userEmail: " + userEmail));

		if (update.getRole() != null) {
			existing.setRole(this.converter.toEntity(update.getRole()));
		}

		if (update.getUsername() != null) {
			existing.setUsername(update.getUsername());
		}

		if (update.getAvatar() != null && !(update.getAvatar().trim().isEmpty())) {
			existing.setAvatar(update.getAvatar());
		}

		// Data Access Object (DAO)
		return this.converter.fromEntity(this.userDao.save(existing));

	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {

		ServiceTools.stringValidation(adminDomain, adminEmail);

		Iterable<UserEntity> allUsers = this.userDao.findAll();

		List<UserBoundary> returnUsers = new ArrayList<>();
		for (UserEntity entity : allUsers)
			returnUsers.add(this.converter.fromEntity(entity)); // map entities to boundaries

		return returnUsers;

	}

	@Override
	@Transactional // (readOnly = false)
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		ServiceTools.stringValidation(adminDomain, adminEmail);
		this.userDao.deleteAll();

	}
}
