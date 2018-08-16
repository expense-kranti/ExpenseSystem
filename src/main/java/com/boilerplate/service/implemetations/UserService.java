package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.AssignApproverEntity;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingUser;
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
	 * @see IUserService.updateUser
	 */
	@Override
	public ExternalFacingUser updateUser(ExternalFacingUser user) throws Exception {
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
		// save user in MySQL database
		return mySqlUser.updateUser(user);
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
		// save user in MySQL database
		return mySqlUser.createUser(user);
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
			// Check if password matches
			if (authenitcationRequest.getPassword() != user.getPassword())
				throw new UnauthorizedException("ExternalFacingUser", "Password did not matchh", null);
			Session session = sessionManager.createNewSession(user);

			return session;
		} catch (Exception nfe) {
			logger.logException("UserService", "authenticate",
					"External Facing User: " + authenitcationRequest.getUserId().toUpperCase()
							+ " With entered Password: " + authenitcationRequest.getPassword() + " not found",
					"Converting this exception to Unauthorized for security", nfe);
			throw new UnauthorizedException("USER", "User name or password incorrect", null);
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
		ExternalFacingUser user = mySqlUser.getUserById(userId);
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
		ExternalFacingUser user = mySqlUser.getUserById(userId);
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
		ExternalFacingUser user = mySqlUser.getUserById(userId);
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
	 * @see IUserService.assignApprover
	 */
	@Override
	public void assignApprover(AssignApproverEntity assignApproverEntity) throws Exception {
		// validate the assignApproverEntity
		assignApproverEntity.validate();
		// check that approver user id exists
		ExternalFacingUser approver = mySqlUser.getUserById(assignApproverEntity.getApproverUserId());
		if (approver == null)
			throw new NotFoundException("AssignApproverEntity", "Approver not found", null);
		// check that super approver user id exists
		ExternalFacingUser superApprover = mySqlUser.getUserById(assignApproverEntity.getSuperApprover());
		if (superApprover == null)
			throw new NotFoundException("AssignApproverEntity", "Super Approver not found", null);
		/**
		 * Yet to implemented- check the roles of the approver and super
		 * approver
		 */
		// for each user in list, fetch the user and set approver and
		// super-approver and save
		for (String userId : assignApproverEntity.getUsers()) {
			try {
				// check if user exists
				ExternalFacingUser user = mySqlUser.getUserById(userId);
				if (user == null)
					throw new NotFoundException("ExternalFacingUser", "User not found while assigning approver", null);
				// set approver id
				user.setApproverId(approver.getId());
				// set super approver id
				user.setSuperApproverId(superApprover.getId());
				mySqlUser.updateUser(user);
			} catch (NotFoundException ex) {
				// Log exception for not found user
				logger.logException("UserService", "assignApprover", "exceptionAssignApprover",
						"User not found while assigning approver, this is the user id :" + userId, ex);
			}
		}
	}

}