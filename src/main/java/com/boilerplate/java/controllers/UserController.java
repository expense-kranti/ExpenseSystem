package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Constants;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ManageUserEntity;
import com.boilerplate.java.entities.UpdateUserEntity;
import com.boilerplate.java.entities.UpdateUserPasswordEntity;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.sessions.Session;
import com.boilerplate.sessions.SessionManager;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has methods to operate on users. These inlcude Creation, update
 * and get user along with authenticate operation
 * 
 * @author gaurav
 *
 */
@Api(description = "This api has controllers for User CRUD operations", value = "User API's", basePath = "/user")
@Controller
public class UserController extends BaseController {

	/**
	 * This is the instance of the user service
	 */
	@Autowired
	IUserService userService;

	/**
	 * This is a new instance of session manager.
	 */
	@Autowired
	SessionManager sessionManager;

	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * This method is used to create a user.
	 * 
	 * @externalFacingUser This is the user to be created.
	 * @throws ValidationFailedException
	 *             This exception is thrown if the user name or password is
	 *             blank
	 * @throws ConflictException
	 *             This exception is thrown if the user already exists in the
	 *             system
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	@ApiOperation(value = "Creates a new User entity in the system", notes = "The user is unique in the system, The creation date and updated "
			+ "date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, User name or password is empty"),
			@ApiResponse(code = 409, message = "The user already exists in the system for the provider") })
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public @ResponseBody ExternalFacingUser createUser(@RequestBody ExternalFacingUser externalFacingUser)
			throws ValidationFailedException, ConflictException, NotFoundException, BadRequestException {
		// call the business layer
		ExternalFacingUser userReturned = userService.create(externalFacingUser);
		return userReturned;
	}

	/**
	 * This method authenticates a user with a given user name and password
	 * 
	 * @param authenticationRequest
	 *            The request for authenticate
	 * @return This returns a session entity of the user. This method also sets
	 *         a header and cookie for session id.
	 * @throws UnauthorizedException
	 *             This exception is thrown if the user name password is
	 *             incorrect
	 */
	@ApiOperation(value = "Authenticates a user by passing user name, password for default provider", notes = "The user is unique in the system for a given provider."
			+ "The user may passed as a user id or as DEFAULT:userId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public @ResponseBody Session authenticate(@RequestBody AuthenticationRequest authenticationRequest)
			throws UnauthorizedException {

		// Call authentication service to check if user name and password are
		// valid
		Session session = this.userService.authenticate(authenticationRequest);

		// now put sessionId in a cookie, header and also as response back
		super.addHeader(Constants.AuthTokenHeaderKey, session.getSessionId());

		boolean isDSA = (authenticationRequest.getUserId()).contains("DSA:");
		if (isDSA)
			super.addCookie(Constants.DsaAuthTokenCookieKey, session.getSessionId(),
					sessionManager.getSessionTimeout());
		else
			super.addCookie(Constants.AuthTokenCookieKey, session.getSessionId(), sessionManager.getSessionTimeout());

		// add the user id for logging, we have to explictly do it here only in
		// this
		// case all other cases
		// are handeled by HttpRequestInterseptor
		super.addHeader(Constants.X_User_Id, session.getExternalFacingUser().getUserId());
		RequestThreadLocal.setRequest(RequestThreadLocal.getRequestId(), RequestThreadLocal.getHttpRequest(),
				RequestThreadLocal.getHttpResponse(), session);
		return session;
	}

	/**
	 * Gets the currently logged in user
	 * 
	 * @return The user
	 * @throws NotFoundException
	 *             If user is not found
	 */
	@ApiOperation(value = "Gets currently logged in user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public @ResponseBody ExternalFacingReturnedUser getCurrentUser()
			throws Exception, NotFoundException, BadRequestException {
		return this.userService.get(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody void logout() throws Exception {
		// find session Id
		String sessionId = RequestThreadLocal.getSession().getSessionId();

		// call logout on service
		this.userService.logout(sessionId);
		// remove the same from cookies etc
		super.addCookie(Constants.AuthTokenCookieKey, sessionId, 1);
	}

	/**
	 * This method is used to automatcially update the user password
	 * 
	 * @externalFacingUser This entity of user
	 * @throws ValidationFailedException
	 *             This exception is thrown if the user name or password is
	 *             blank
	 * @throws ConflictException
	 *             This exception is thrown if the user already exists in the
	 *             system
	 * @throws UnauthorizedException
	 *             If the user is not authorized
	 * @throws NotFoundException
	 *             If the user is not found
	 * @throws BadRequestException
	 *             If the user information is not provided
	 * 
	 */
	@ApiOperation(value = "Updates the password of the user given the proper userId.", notes = "Only User Id (which equals phone number).")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, User name or password is empty"),
			@ApiResponse(code = 409, message = "The user already exists in the system for the provider") })
	@RequestMapping(value = "/user/passwordReset", method = RequestMethod.POST)
	public @ResponseBody ExternalFacingReturnedUser automaticPasswordReset(
			@RequestBody ExternalFacingUser externalFacingUser) throws ValidationFailedException, ConflictException,
			NotFoundException, UnauthorizedException, BadRequestException {
		// call the business layer
		return userService.automaticPasswordReset(externalFacingUser);
	}

	/**
	 * This method is used to update the password of the user who is logged in.
	 * 
	 * @param updateUserPasswordEntity
	 *            The password to be updated
	 * @return The user
	 */
	@ApiOperation(value = "This api updated the password of the user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/user/", method = RequestMethod.PUT)
	public @ResponseBody ExternalFacingReturnedUser update(
			@RequestBody UpdateUserPasswordEntity updateUserPasswordEntity) throws Exception, ValidationFailedException,
			ConflictException, NotFoundException, UnauthorizedException, BadRequestException {
		return this.userService.update(updateUserPasswordEntity);
	}

	/**
	 * This method updates a user who is logged in.
	 * 
	 * @param updateUserEntity
	 *            The user entity to be updated.
	 * @return The user
	 * @throws NotFoundException
	 *             If user is not found
	 */
	@ApiOperation(value = "Updates the logged in user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/user/self", method = RequestMethod.PUT)
	public @ResponseBody ExternalFacingReturnedUser updateLoggedInUser(@RequestBody UpdateUserEntity updateUserEntity)
			throws Exception, ValidationFailedException, ConflictException, NotFoundException, BadRequestException {
		String userId = super.getSession().getExternalFacingUser().getUserId();
		return this.userService.update(userId, updateUserEntity);
	}

	/**
	 * This method is used to delete the user from database
	 * 
	 * @param manageUserEntity
	 *            it contains the user id of the user to delete
	 * @throws NotFoundException
	 *             thrown when user with given id not found
	 * @throws BadRequestException
	 *             throw this exception in case of request is not valid
	 * @throws UnauthorizedException
	 *             thrown when user is not authorized to delete user and its
	 *             data
	 * @throws ValidationFailedException
	 *             thrown if userId is null or empty
	 */
	@ApiOperation(value = "Deletes a user with given userId", notes = "only user id is required(which equals phone number)")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody void deleteUserAndData(@RequestBody ManageUserEntity manageUserEntity)
			throws NotFoundException, UnauthorizedException, BadRequestException, ValidationFailedException {
		userService.deleteUser(manageUserEntity);
	}

	/**
	 * This method is used to check the existence of user with the given id
	 * 
	 * @return false if user with given id does not exist
	 * 
	 * @param externalFacingUser
	 *            this entity contains the user id to checked against user
	 *            existence
	 * @throws ValidationFailedException
	 *             thrown when userId is null or empty
	 * @throws ConflictException
	 *             thrown when user with given user id already exists
	 */
	@ApiOperation(value = "/checkUserExistence", notes = "only user id is required(which equals phone number)")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/checkUserExistence", method = RequestMethod.POST)
	public @ResponseBody boolean checkUserExistence(@RequestBody ExternalFacingUser externalFacingUser)
			throws ValidationFailedException, ConflictException {
		return userService.checkUserExistence(externalFacingUser);
	}

	/**
	 * This method updates a user
	 * 
	 * @param updateUserEntity
	 *            The user entity to be updated.
	 * @return The updated user
	 * @throws NotFoundException
	 *             If user is not found
	 */
	@ApiOperation(value = "Updates A user", notes = "needs phoneNumber of user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/user/updateAUser", method = RequestMethod.PUT)
	public @ResponseBody ExternalFacingReturnedUser updateAUser(@RequestBody UpdateUserEntity updateUserEntity)
			throws Exception, ValidationFailedException, ConflictException, NotFoundException, BadRequestException {
		return this.userService.updateAUser(updateUserEntity);
	}

}