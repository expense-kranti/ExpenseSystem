package com.boilerplate.service.implemetations;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Encryption;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.SaveRoleEntity;
import com.boilerplate.java.entities.UserRoleEntity;
import com.boilerplate.java.entities.UserRoleType;
import com.boilerplate.service.interfaces.IUserRoleService;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.sessions.Session;

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
	 * @see IUserService.updateUser
	 */
	@Override
	public ExternalFacingUser updateUser(ExternalFacingUser user) throws Exception {
		// check admin authentication
		// validate the input user entity
		user.validate();
		// check if user entity does not contain id
		if (user.getId() == null)
			throw new BadRequestException("UserEntity", "Id should not be null", null);
		// check if user with same email id or mobile number does not exist
		if (mySqlUser.getExistingUser(user.getPhoneNumber(), user.getEmail()) == null)
			throw new BadRequestException("UserEntity", "USer does not exist", null);
		// set active status to true
		user.setIsActive(true);
		// hash password
		user.hashPassword();
		// save user in MySQL database
		user = mySqlUser.updateUser(user);
		return user;
	}

	/**
	 * @see IUserService.createUser
	 */
	@Override
	public ExternalFacingUser createUser(ExternalFacingUser user)
			throws ValidationFailedException, BadRequestException, Exception {
		// validate the input user entity
		user.validate();
		// check if user entity does not contain id
		if (user.getId() != null)
			throw new BadRequestException("UserEntity", "Id should be null", null);
		// check if user with same email id or mobile number does not exist
		if (mySqlUser.getExistingUser(user.getPhoneNumber(), user.getEmail()) != null)
			throw new BadRequestException("UserEntity", "USer already exists with the given mobile number or email id",
					null);
		// set active status to true
		user.setIsActive(true);
		// hash password
		user.hashPassword();
		// save user in MySQL database
		user = mySqlUser.createUser(user);
		// create a new save role entity
		SaveRoleEntity roleEntity = new SaveRoleEntity(user.getId(),
				new ArrayList<UserRoleType>(Arrays.asList(UserRoleType.Employee)));
		// assign default/Employee role to user
		userRoleService.assignRoles(roleEntity);
		return user;
	}

	/**
	 * @see IUserService.authenticate
	 */
	@Override
	public Session authenticate(AuthenticationRequest authenitcationRequest)
			throws UnauthorizedException, BadRequestException {

		ExternalFacingUser user = null;
		try {
			user = mySqlUser.getUserById(authenitcationRequest.getUserId());
			// check if user exists
			if (user == null)
				throw new NotFoundException("ExternalFacingUser", "User not found", null);
			// check if user is active or not
			if (!user.getIsActive())
				throw new BadRequestException("ExternalFacingUser", "User is inactive", null);
			// hash the password in authentication request
			String hashedPassword = String.valueOf(Encryption.getHashCode(authenitcationRequest.getPassword()));
			// Check if password matches
			if (!user.getPassword().equals(hashedPassword)) {
				logger.logInfo("UserService", "authenticate", "exceptionAuthenticate",
						"incoming password : " + authenitcationRequest.getPassword() + "hashed db password"
								+ user.getPassword() + "hashed incoming password" + hashedPassword);
				throw new UnauthorizedException("User", "User name or password	 incorrect", null);
			}
			// get user roles
			List<UserRoleEntity> userRoles = mySqlUser.getUserRoles(user.getId());
			// create a role list for user
			List<UserRoleType> roles = new ArrayList<>();
			// for each role entity fetched, add it in role list
			for (UserRoleEntity userRoleEntity : userRoles) {
				roles.add(userRoleEntity.getRole());
			}
			// set roles in user
			user.setRoles(roles);
			// create a new session with user, return it
			Session session = sessionManager.createNewSession(user);

			return session;
		} catch (Exception nfe) {
			logger.logInfo("UserService", "authenticate", "exceptionAuthenticate", "incoming password : "
					+ authenitcationRequest.getPassword() + "hashed db password" + user.getPassword());
			logger.logException("UserService", "authenticate",
					"External Facing User: " + authenitcationRequest.getUserId().toUpperCase()
							+ " With entered Password: " + authenitcationRequest.getPassword() + " not found",
					"Converting this exception to Unauthorized for security", nfe);
			throw new UnauthorizedException("User", "User name or password is incorrect" + "incoming password : "
					+ authenitcationRequest.getPassword() + "hashed db password" + user.getPassword(), null);
		}

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
		ExternalFacingUser user = mySqlUser.getUser(userId);
		return user;
	}

	/**
	 * @see IUserService.disableUser
	 */
	@Override
	public void disableUser(String userId) throws Exception {
		// check if user id is not null
		if (userId == null)
			throw new BadRequestException("ExternalFacingUser", "User id is null", null);
		// check if user exists
		ExternalFacingUser user = mySqlUser.getUser(userId);
		if (user == null)
			throw new NotFoundException("ExternalFacingUser", "User not found", null);
		// check if user is already disabled
		if (!user.getIsActive())
			throw new BadRequestException("ExternalFacingUser", "user is already disabled", null);
		// set active to false
		user.setIsActive(false);
		mySqlUser.updateUser(user);

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
	public Session authenticateUsingGoogle(String idTokenString) throws GeneralSecurityException, IOException {
		// HttpTransport httpTransport = new NetHttpTransport();
		// GoogleIdTokenVerifier verifier = new
		// GoogleIdTokenVerifier.Builder(httpTransport,
		// new JacksonFactory().getDefaultInstance())
		// // Specify the CLIENT_ID of the app that accesses the
		// // backend:
		// .setAudience(Collections.singletonList(
		// "428760649180-n99urcelfgmp4iiab879907qoigsgjkg.apps.googleusercontent.com"))
		// // Or, if multiple clients access the backend:
		// // .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2,
		// // CLIENT_ID_3))
		// .build();
		//
		// // (Receive idTokenString by HTTPS POST)
		//
		// GoogleIdToken idToken = verifier.verify(idTokenString);
		// if (idToken != null) {
		// Payload payload = idToken.getPayload();
		//
		// // Print user identifier
		// String userId = payload.getSubject();
		// System.out.println("User ID: " + userId);
		//
		// // Get profile information from payload
		// String email = payload.getEmail();
		// boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
		// String name = (String) payload.get("name");
		// String locale = (String) payload.get("locale");
		// String familyName = (String) payload.get("family_name");
		// String givenName = (String) payload.get("given_name");
		// String hdClaim = (String) payload.getHostedDomain();
		//
		// // Use or store profile information
		// // ...
		// return null;
		//
		// } else {
		// System.out.println("Invalid ID token.");
		// }
		return null;
	}

}