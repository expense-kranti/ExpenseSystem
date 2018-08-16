package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.AssignApproverEntity;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.sessions.Session;

/**
 * This class provide the services to authenticate ,create and get the user
 * details.
 * 
 * @author ruchi
 *
 */
public interface IUserService {
	/**
	 * This method is used to create a new user
	 * 
	 * @param externalFacingUser
	 *            This is the user to be saved
	 * @return Saved user entity
	 * @throws ValidationFailedException
	 *             Throw this exception if user entity is invalid
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             user
	 */
	public ExternalFacingUser createUser(ExternalFacingUser externalFacingUser)
			throws ValidationFailedException, BadRequestException, Exception;

	/**
	 * This method is used to update user in system
	 * 
	 * @param user
	 *            This is the updated user entity that has to saved
	 * @return Updated user entity
	 * @throws ValidationFailedException
	 *             Throw this exception if user entity is invalid
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             user
	 */
	public ExternalFacingUser updateUser(ExternalFacingUser user)
			throws ValidationFailedException, BadRequestException, Exception;

	/**
	 * This method is used to autenticate a user
	 * 
	 * @param authenitcationRequest
	 *            This is the authentication request
	 * @return Session
	 * @throws UnauthorizedException
	 *             Throw this exception if user is unauthorized
	 * @throws BadRequestException
	 *             Throw this exception if user sends bad request
	 */
	public Session authenticate(AuthenticationRequest authenitcationRequest)
			throws UnauthorizedException, BadRequestException;

	/**
	 * This method is used to get user by user id
	 * 
	 * @param userId
	 *            this is the user id of the user to be fetched
	 * @return User
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 */
	public ExternalFacingUser getUser(String userId) throws BadRequestException;

	/**
	 * This method is used to disable a user
	 * 
	 * @param userId
	 *            this is the user id
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user not found
	 * @throws Exception
	 *             Throw this exception if exception occurs while updating user
	 */
	public void disableUser(String userId) throws BadRequestException, NotFoundException, Exception;

	/**
	 * This method is used to enable user
	 * 
	 * @param userId
	 *            this is the user id
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user not found
	 * @throws Exception
	 *             Throw this exception if exception occurs while updating user
	 */
	public void enableUser(String userId) throws BadRequestException, NotFoundException, Exception;

	/**
	 * This method is used to assign approvers to a list of users
	 * 
	 * @param assignApproverEntity
	 *            This is the entity consisting of approvers and users
	 * @throws ValidationFailedException
	 *             Throw this exception if entity is invalid
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user not found
	 * @throws Exception
	 *             Throw this exception if exception occurs while updating user
	 */
	public void assignApprover(AssignApproverEntity assignApproverEntity)
			throws ValidationFailedException, NotFoundException, BadRequestException, Exception;

}
