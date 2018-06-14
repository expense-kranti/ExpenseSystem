package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.java.entities.ExpressEntity;

public interface IExpressService {
	/**
	 * This method provides services to validate the user input name
	 * 
	 * @param expressEntity
	 *            contains the user name to check for validation and mobile
	 *            number of user
	 * @return expressEntity contains the details of user express attempt like
	 *         mobile number
	 * @throws PreconditionFailedException
	 *             thrown when no express attempt found for mobilenumber for
	 *             validating name
	 */
	public ExpressEntity validateAndRegisterUser(ExpressEntity expressEntity) throws PreconditionFailedException;

}
