package com.boilerplate.service.implemetations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IRole;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.RoleEntity;
import com.boilerplate.java.entities.SaveRoleEntity;
import com.boilerplate.java.entities.UserRoleEntity;
import com.boilerplate.java.entities.UserRoleType;
import com.boilerplate.service.interfaces.IUserRoleService;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.sessions.Session;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class Implements the IUserService class
 * 
 * @author ruchi
 *
 */
public class UserService implements IUserService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(UserService.class);

	/**
	 * This is the instance of IUser
	 */
	@Autowired
	IUser mySqlUser;

	/**
	 * This method set the mysqluser
	 * 
	 * @param mySqlUser
	 *            the mySqlUser to set
	 */
	public void setMySqlUser(IUser mySqlUser) {
		this.mySqlUser = mySqlUser;
	}

	/**
	 * This is the autowired instance of IRole
	 */
	@Autowired
	IRole mySqlRole;

	/**
	 * This method is used to set instance of IRole
	 * 
	 * @param mySqlRole
	 */
	public void setMySqlRole(IRole mySqlRole) {
		this.mySqlRole = mySqlRole;
	}

	/**
	 * The autowired instance of session manager
	 */
	@Autowired
	com.boilerplate.sessions.SessionManager sessionManager;

	/**
	 * This sets the session manager
	 * 
	 * @param sessionManager
	 *            The session manager
	 */
	public void setSessionManager(com.boilerplate.sessions.SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	/**
	 * this is the autowired instance of user role service
	 */
	@Autowired
	IUserRoleService userRoleService;

	/**
	 * This method is used to set autowired instance of IUserRoleService
	 * 
	 * @param userRoleService
	 */
	public void setUserRoleService(IUserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This method is used to set the configurationManager
	 * 
	 * @param configurationManager
	 *            the configurationManager to set
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * @see IUserService.getUser
	 */
	@Override
	public ExternalFacingUser getUser(String userId) throws BadRequestException {
		// check if user id is not null
		if (userId == null)
			throw new BadRequestException("ExternalFacingUser", "User id for fetching user is null", null);
		// create a new user enityt
		return mySqlUser.getUser(userId);

	}

	/**
	 * @see IUserService.disableUser
	 */
	@Override
	public void disableUser(String userId) throws Exception {
		if (userId == null)
			throw new BadRequestException("ExternalFacingUser", "User id is null", null);
		// check if user exists
		ExternalFacingUser user = mySqlUser.getUser(userId);
		// check if user id is not null
		if (user == null)
			throw new NotFoundException("ExternalFacingUser", "User not found", null);
		// check if user is already disabled
		if (!user.getIsActive())
			throw new BadRequestException("ExternalFacingUser", "user is already disabled", null);
		// check if user has some roles
		if (user.getRoles() != null && !user.getRoles().isEmpty()) {
			// check if user being disabled is not admin
			List<UserRoleEntity> userRoleMappings = new ArrayList<>();
			userRoleMappings.addAll(user.getRoles());
			// get all roles
			List<RoleEntity> allRoles = mySqlRole.getAllRoles();
			// check if user does not have admin as a role
			for (UserRoleEntity userRoleEntity : userRoleMappings) {
				for (RoleEntity roleEntity : allRoles) {
					if (roleEntity.getRoleName().equals(UserRoleType.ADMIN.toString()))
						if (userRoleEntity.getRoleId().equals(roleEntity.getId()))
							throw new ValidationFailedException("UserRoleEntity",
									"The user you are trying to disable is an admin", null);
				}
			}

		}
		// set active to false
		user.setIsActive(false);
		mySqlUser.updateUser(user);
		// get all the sessions of this user from redis

	}

	/**
	 * @see IUserService.enableUser
	 */
	@Override
	public void enableUser(String userId) throws Exception {
		// check if user id is not null
		if (userId == null)
			throw new BadRequestException("ExternalFacingUser", "User id is null", null);
		// check if user exists
		ExternalFacingUser user = mySqlUser.getUser(userId);
		if (user == null)
			throw new NotFoundException("ExternalFacingUser", "User not found", null);
		// check if user is already disabled
		if (user.getIsActive())
			throw new BadRequestException("ExternalFacingUser", "user is already active", null);
		// set active to false
		user.setIsActive(true);
		mySqlUser.updateUser(user);

	}

	/**
	 * @see IUserService.authenticateUsingGoogle
	 */
	@Override
	public Session authenticateUsingGoogle(String idTokenString) throws Exception {
		// verify the token using url
		HttpResponse httpResponse = HttpUtility.makeHttpRequest(
				"https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + idTokenString, null, null, null, "POST");
		Map<String, Object> responseMap = (new ObjectMapper()).readValue(httpResponse.getResponseBody(), Map.class);
		// check if user belongs to desired hd claim
		// String hdClaim = String.valueOf(responseMap.get("hd"));
		// if (!hdClaim.equals(configurationManager.get("HD_CLAIM")) ||
		// hdClaim.equals(null))
		// throw new UnauthorizedException("ExternalFacingUser", "User does not
		// belong to this organization", null);
		// // get email id from response map
		String email = String.valueOf(responseMap.get("email"));
		// check if user with this email id exists in the system
		ExternalFacingUser user = mySqlUser.getUserById(email);
		// check if user exists in system
		if (user == null) {
			// save the new user
			user = new ExternalFacingUser(email, email, String.valueOf(responseMap.get("given_name")),
					String.valueOf(responseMap.get("family_name")), null, true, null,
					configurationManager.get("DefaultAuthenticationProvider"));
			// save uer in MySQl
			user = mySqlUser.createUser(user);
			// create a new role entity
			SaveRoleEntity saveRoleEntity = new SaveRoleEntity(user.getId(), new ArrayList<>(Arrays.asList("1")));
			// assign default role to user
			userRoleService.assignRoles(saveRoleEntity);
		} else {
			if (user.getIsActive() == false)
				throw new BadRequestException("ExternalFacingUser", "User is disabled, please contact your admin",
						null);
		}
		user = null;
		user = mySqlUser.getUserById(email);
		// check if user has at least 1 role
		if (user.getRoles() == null || user.getRoles().size() == 0)
			throw new BadRequestException("ExternalFacingUser",
					"User does not have any role, please check with your admin", null);

		// get all roles
		List<RoleEntity> allRoles = mySqlRole.getAllRoles();
		List<UserRoleEntity> userRoles = new ArrayList<>();
		userRoles.addAll(user.getRoles());
		user.setRoles(userRoles);
		List<UserRoleType> roleTypes = new ArrayList<>();
		// for each role entity fetched, add it in role list
		for (UserRoleEntity userRoleEntity : userRoles) {
			// for each role
			for (RoleEntity roleEntity : allRoles) {
				if (userRoleEntity.getRoleId().equals(roleEntity.getId()))
					roleTypes.add(UserRoleType.valueOf(roleEntity.getRoleName().toUpperCase()));
			}
		}
		user.setRoleTypes(roleTypes);

		// create a new session with user, return it
		Session session = sessionManager.createNewSession(user);
		return session;
	}

	/**
	 * @see IUserService.getAllUsers
	 */
	@Override
	public List<ExternalFacingUser> getAllUsers() throws BadRequestException {
		return mySqlUser.getAllUsers();
	}

}