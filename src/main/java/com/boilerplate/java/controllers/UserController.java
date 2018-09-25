package com.boilerplate.java.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Constants;
import com.boilerplate.java.entities.AssignApproverEntity;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.SaveRoleEntity;
import com.boilerplate.service.interfaces.IUserRoleService;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.sessions.Session;
import com.boilerplate.sessions.SessionManager;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has methods to operate on users. These include Creation, update
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
	 * This is the autowired instance of IUserRoleService
	 */
	@Autowired
	IUserRoleService userRoleService;

	/**
	 * This api is used to disable a user
	 * 
	 * @param userId
	 *            This is the user id of the user to be disabled
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user not found
	 * @throws Exception
	 *             Throw this exception if exception occurs while updating user
	 */
	@ApiOperation(value = "Disables a user", notes = "The user is unique in the system, The creation date and updated "
			+ "date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, User name or password is empty"),
			@ApiResponse(code = 409, message = "The user does not exist") })
	@RequestMapping(value = "/disableUser", method = RequestMethod.PUT)
	public @ResponseBody void disableUser(@RequestParam String userId)
			throws BadRequestException, NotFoundException, Exception {
		// call the business layer
		userService.disableUser(userId);
	}

	/**
	 * This api is used to enable a user
	 * 
	 * @param userId
	 *            This is the user id of the user to be disabled
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user not found
	 * @throws Exception
	 *             Throw this exception if exception occurs while updating user
	 */
	@ApiOperation(value = "Enables a user", notes = "The user is unique in the system, The creation date and updated "
			+ "date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, User name or password is empty"),
			@ApiResponse(code = 409, message = "The user does not exist") })
	@RequestMapping(value = "/enableUser", method = RequestMethod.PUT)
	public @ResponseBody void enableUser(@RequestParam String userId)
			throws BadRequestException, NotFoundException, Exception {
		// call the business layer
		userService.enableUser(userId);
	}

	/**
	 * This API is used to assign approver to a list of users
	 * 
	 * @param assignApproverEntity
	 *            This is the entity consisting of approver and users
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user not found
	 * @throws Exception
	 *             Throw this exception if exception occurs while updating user
	 */
	@ApiOperation(value = "Assigns approver to a list of users", notes = "The user is unique in the system, The creation date and updated "
			+ "date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, User name or password is empty"),
			@ApiResponse(code = 409, message = "The user does not exist") })
	@RequestMapping(value = "/assignApprover", method = RequestMethod.POST)
	public @ResponseBody void assignApprovers(@RequestBody AssignApproverEntity assignApproverEntity)
			throws BadRequestException, NotFoundException, Exception {
		// call the business layer
		userRoleService.assignApprover(assignApproverEntity);
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
	public @ResponseBody ExternalFacingUser getCurrentUser() throws Exception, NotFoundException, BadRequestException {
		// call the business layer
		return this.userService.getUser(RequestThreadLocal.getSession().getExternalFacingUser().getId());

	}

	/**
	 * This api is used to assign roles to a list of users
	 * 
	 * @param roles
	 *            this is the list of roles
	 * @throws BadRequestException
	 *             throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             throw this exception if user no found
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             roles
	 */
	@ApiOperation(value = "Assign roles to users", notes = "The user is unique in the system, The creation date and updated "
			+ "date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, manadatory fields are missing"),
			@ApiResponse(code = 409, message = "The user does not exist") })
	@RequestMapping(value = "/assignRoles", method = RequestMethod.POST)
	public @ResponseBody void assignRoles(@RequestBody SaveRoleEntity role)
			throws BadRequestException, NotFoundException, Exception {
		// call the business layer
		userRoleService.assignRoles(role);
	}

	/**
	 * This API is used to authenticate user through google SSO
	 * 
	 * @param idToken
	 *            This is the id token provided by google after successful login
	 * @return Session with user
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user is not found
	 * @throws Exception
	 *             throw this exception if any exception occurs while saving a
	 *             new user
	 */
	@ApiOperation(value = "Authenticate using google SSO", notes = "The user is unique in the system, The creation date and updated "
			+ "date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, manadatory fields are missing"),
			@ApiResponse(code = 409, message = "The user does not exist") })
	@RequestMapping(value = "/sSOauthenticate", method = RequestMethod.POST)
	public @ResponseBody Session ssoLogin(@RequestParam String idToken)
			throws BadRequestException, NotFoundException, Exception {
		// Call authentication service to check if user name and password are
		// valid
		Session session = this.userService.authenticateUsingGoogle(idToken);

		// now put sessionId in a cookie, header and also as response back
		super.addHeader(Constants.AuthTokenHeaderKey, session.getSessionId());
		super.addCookie(Constants.AuthTokenCookieKey, session.getSessionId(), sessionManager.getSessionTimeout());

		// add the user id for logging, we have to explicitly do it here only in
		// this
		// case all other cases
		// are handled by HttpRequestInterseptor
		super.addHeader(Constants.X_User_Id, session.getExternalFacingUser().getUserId());
		RequestThreadLocal.setRequest(RequestThreadLocal.getRequestId(), RequestThreadLocal.getHttpRequest(),
				RequestThreadLocal.getHttpResponse(), session);
		return session;
	}

	/**
	 * This API is used to delete roles of a user
	 * 
	 * @param role
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if entity is not found
	 * @throws Exception
	 *             Throw this exception if any exception occurs
	 */
	@ApiOperation(value = "Delete roles of users", notes = "The user is unique in the system, The creation date and updated "
			+ "date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, manadatory fields are missing"),
			@ApiResponse(code = 404, message = "The user does not exist") })
	@RequestMapping(value = "/deleteRoles", method = RequestMethod.POST)
	public @ResponseBody void deleteRoles(@RequestBody SaveRoleEntity role)
			throws BadRequestException, NotFoundException, Exception {
		// call the business layer
		userRoleService.deleteRoles(role);
	}

	/**
	 * This API is used to get all the users(active/inactive)
	 * 
	 * @return List of users
	 * @throws BadRequestException
	 *             Throw this exception if user sends bad request
	 */
	@ApiOperation(value = "Get all users")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public @ResponseBody List<ExternalFacingUser> getAllUsers() throws BadRequestException {
		// call the business layer
		return this.userService.getAllUsers();

	}

}