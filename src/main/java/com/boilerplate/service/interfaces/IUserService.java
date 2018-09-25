package com.boilerplate.service.interfaces;

import java.io.IOException;
import java.util.List;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
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
	 * This method is used authenticate a user through google sso login
	 * 
	 * @param idToken
	 *            This is the idToken returned by google SSO
	 * @return Session
	 * @throws IOException
	 *             throw this exception if any exception occurs while db
	 *             operation
	 * @throws BadRequestException
	 *             throw this exception if user sends bad request
	 * @throws Exception
	 *             throw this exception if any exception occurs while fetching
	 *             user
	 */
	public Session authenticateUsingGoogle(String idToken) throws IOException, BadRequestException, Exception;

	/**
	 * This method is used to get all users
	 * 
	 * @return List of users
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 */
	public List<ExternalFacingUser> getAllUsers() throws BadRequestException;

}
