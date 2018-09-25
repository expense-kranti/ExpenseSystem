package com.boilerplate.database.interfaces;

import java.util.List;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.RoleEntity;
import com.boilerplate.java.entities.SaveRoleEntity;
import com.boilerplate.java.entities.UserRoleEntity;

/**
 * This class has methods for Role related CRUD operations in MySQL
 * 
 * @author ruchi
 *
 */
public interface IRole {

	/**
	 * This method is used to get all the roles that exist in the system
	 * 
	 * @return List of role types
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 */
	public List<RoleEntity> getAllRoles() throws BadRequestException;

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
	 * This method is used to delete roles
	 * 
	 * @param saveRoleEntity
	 *            This is the save role entity
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 */
	public void deleteRoles(List<UserRoleEntity> userRoles) throws BadRequestException;

}
