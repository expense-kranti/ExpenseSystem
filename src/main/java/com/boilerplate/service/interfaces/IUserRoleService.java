package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.AssignApproverEntity;
import com.boilerplate.java.entities.SaveRoleEntity;

/**
 * This interface has methods for user roles handling
 * 
 * @author ruchi
 *
 */
public interface IUserRoleService {

	/**
	 * This method is used to save list of user roles in mySQL
	 * 
	 * @param userRoles
	 *            This is the list of user roles
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             user roles
	 */
	public void assignRoles(SaveRoleEntity userRoles) throws Exception;

	/**
	 * this method is used to assign approver and super-approver to a list of
	 * users
	 * 
	 * @param assignApproverEntity
	 *            This is the assignApproverEntity
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             approvers
	 */
	public void assignApprover(AssignApproverEntity assignApproverEntity) throws Exception;

	/**
	 * This method is used to delete roles of a user
	 * 
	 * @param saveRoleEntity
	 *            This entity contains role ids and user id of the user
	 * @throws ValidationFailedException
	 *             Throw this exception if entity is invalid
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception role/user is not found
	 */
	public void deleteRoles(SaveRoleEntity saveRoleEntity)
			throws ValidationFailedException, BadRequestException, NotFoundException;

}
