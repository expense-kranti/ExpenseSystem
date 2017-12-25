package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ManageUserEntity;
import com.boilerplate.java.entities.UpdateUserEntity;
import com.boilerplate.java.entities.UpdateUserPasswordEntity;
import com.boilerplate.sessions.Session;

/**
 * This class provide the services to authenticate ,create and get the user
 * details.
 * 
 * @author shiva
 *
 */
public interface IUserService {
	/**
	 * This method is used to create an external facing user.
	 * 
	 * @param externalFacingUser
	 *            The instance of the user to be created.
	 * @throws ValidationFailedException
	 *             If the user name or password are empty
	 * @throws ConflictException
	 *             This is thrown if the user already exists in the database for
	 *             the given provider
	 * @return a user which has been created
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	public ExternalFacingUser create(ExternalFacingUser externalFacingUser)
			throws ValidationFailedException, ConflictException, NotFoundException, BadRequestException;

	/**
	 * This method takes an authentication request and generates a session if
	 * the user is authenticated
	 * 
	 * @param authenitcationRequest
	 *            The authentication request
	 * @return A session object
	 * @throws UnauthorizedException
	 *             if the user name password combination is incorrect
	 */
	public Session authenticate(AuthenticationRequest authenitcationRequest) throws UnauthorizedException;

	/**
	 * This method returns the user with given user id.
	 * 
	 * @param userId
	 *            The id of the user in format provider:userid, if no provider
	 *            is specified it is defaulted to default
	 * @return A user id
	 * @throws NotFoundException
	 *             If no user is found with given details
	 */
	public ExternalFacingReturnedUser get(String userId) throws NotFoundException, BadRequestException;

	/**
	 * This method normalize the userId
	 * 
	 * @param userId
	 *            this parameter define the userId
	 * @return the normalized userId
	 */
	String normalizeUserId(String userId);

	/**
	 * This method returns a user with given id
	 * 
	 * @param userId
	 *            The id of the user
	 * @param encryptPasswordString
	 *            True if the user password to be encryptied into a message
	 *            string false if the password is to be sent as is
	 * @return The user entity
	 * @throws NotFoundException
	 *             If the user is not found @ throws BadRequestException If the
	 *             userId is not provided
	 */
	ExternalFacingReturnedUser get(String userId, boolean encryptPasswordString)
			throws NotFoundException, BadRequestException;

	public void logout(String sessionId);

	/**
	 * This method resets the password
	 * 
	 * @param externalFacingUser
	 *            The entity to be updated
	 * @return An instance of user
	 * @throws ValidationFailedException
	 *             If validation fails
	 * @throws ConflictException
	 *             If there is a data conflict
	 * @throws NotFoundException
	 *             If the user is not found
	 * @throws UnauthorizedException
	 *             If the user is not logged in.
	 * @throws BadRequestException
	 *             If the user information is not provided
	 */
	public ExternalFacingReturnedUser automaticPasswordReset(ExternalFacingUser externalFacingUser)
			throws ValidationFailedException, ConflictException, NotFoundException, UnauthorizedException,
			BadRequestException;

	/**
	 * This method is used to update a user.
	 * 
	 * @param userId
	 *            The id of the user to be updated
	 * @param updateUserEntity
	 *            The entity of the user.
	 * @return The updated user entity
	 * @throws ValidationFailedException
	 *             If any validation fails
	 * @throws ConflictException
	 *             If there is a conflict in updating entity due to a DB
	 *             constraint
	 * @throws NotFoundException
	 *             If the user is not found.
	 */
	public ExternalFacingReturnedUser update(String userId, UpdateUserEntity updateUserEntity)
			throws ValidationFailedException, ConflictException, NotFoundException, BadRequestException;

	/**
	 * This method updates the password of user
	 * 
	 * @param updateUserPasswordEntity
	 *            The entity to be updated
	 * @return An instance of user
	 * @throws ValidationFailedException
	 *             If validation fails
	 * @throws ConflictException
	 *             If there is a data conflict
	 * @throws NotFoundException
	 *             If the user is not found
	 * @throws UnauthorizedException
	 *             If the user is not logged in.
	 * @throws BadRequestException
	 *             If the user information is not provided
	 */
	public ExternalFacingReturnedUser update(UpdateUserPasswordEntity updateUserPasswordEntity)
			throws ValidationFailedException, ConflictException, NotFoundException, UnauthorizedException,
			BadRequestException;

	public String getReferUserId(String uuid);

	/**
	 * This method is used to delete User from database with given user id
	 * 
	 * @param manageUserEntity
	 *            it contains the user id of the user, used to find user in
	 *            database to delete that user entries.
	 * @throws NotFoundException
	 *             thrown when user with given id not found
	 * @throws UnauthorizedException
	 *             thrown when user is not authorized to delete user and its
	 *             data
	 * @throws BadRequestException
	 *             throw this exception in case of request is not valid
	 * @throws ValidationFailedException
	 *             thrown if userId is null or empty
	 */
	public void deleteUser(ManageUserEntity manageUserEntity)
			throws NotFoundException, UnauthorizedException, BadRequestException, ValidationFailedException;

	/**
	 * This method is used to check the user existence with the given user id
	 * 
	 * @return boolean false if user does not exist
	 * 
	 * @param externalUserFacing
	 *            it contains user id of the user to check existence for
	 * @throws ValidationFailedException
	 *             thrown if user id is null or empty
	 * @throws ConflictException
	 *             thrown when user with given user id exists
	 */
	public boolean checkUserExistence(ExternalFacingUser externalFacingUser)
			throws ValidationFailedException, ConflictException;

}
