package com.boilerplate.database.interfaces;

import java.util.List;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.RoleEntity;

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

	public RoleEntity getRoleById();
}
