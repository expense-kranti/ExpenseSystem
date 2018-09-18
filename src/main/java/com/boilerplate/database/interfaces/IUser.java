package com.boilerplate.database.interfaces;

import java.util.List;
import java.util.Map;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.SaveRoleEntity;
import com.boilerplate.java.entities.UserRoleEntity;

/**
 * This has interfaces for user management
 * 
 * @author gaurav
 *
 */
public interface IUser {

	/**
	 * This method is used to save a new user in the database
	 *
	 * @param userEntity
	 *            This is the user entity to be saved
	 * @return Saved user entity
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             user
	 */
	public ExternalFacingUser createUser(ExternalFacingUser userEntity) throws Exception;

	/**
	 * This method is used to fetch user on the basis of mobile number or user
	 * id
	 * 
	 * @param mobile
	 *            This is the mobile number
	 * @param emailId
	 *            This is the email id
	 * @return User entity if found
	 * @throws BadRequestException
	 *             Throw this exception if user sends bad request
	 */
	public ExternalFacingUser getExistingUser(String mobile, String emailId) throws BadRequestException;

	/**
	 * This method is used to update an existing user in the system
	 * 
	 * @param userEntity
	 *            This is the updated user enityt to be saved
	 * @return Saved user entity
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             user
	 */
	public ExternalFacingUser updateUser(ExternalFacingUser userEntity) throws Exception;

	/**
	 * This method is used to get user by user id
	 * 
	 * @param userId
	 *            this is the user d of the user
	 * @return User entity
	 * @throws BadRequestException
	 *             Throw this exception if user sends bad request
	 * 
	 */
	public ExternalFacingUser getUserById(String userId) throws BadRequestException;

	/**
	 * This method is used to get user by id
	 * 
	 * @param id
	 *            This is the id of the user
	 * @return User entity
	 * @throws BadRequestException
	 *             Throw this exception if user sends bad request
	 * 
	 */
	public ExternalFacingUser getUser(String id) throws BadRequestException;

	/**
	 * This method is used to save user roles
	 * 
	 * @param userRoles
	 *            this is the list of user role entities
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             user
	 */
	public void saveUserRoles(SaveRoleEntity userRoles) throws Exception;

	/**
	 * This method is used to get roles for the given user id
	 * 
	 * @param userId
	 *            This is the user id
	 * @return Return of user roles
	 * @throws BadRequestException
	 *             Throw this exception if user sends abad request
	 */
	public List<UserRoleEntity> getUserRoles(String userId) throws BadRequestException;

	/**
	 * This method is used to get all finance users
	 * 
	 * @return List of finance users
	 * @throws BadRequestException
	 *             Throw this exception if user senda bad request
	 */
	public List<Map<String, Object>> getFinanceUsers() throws BadRequestException;

}
