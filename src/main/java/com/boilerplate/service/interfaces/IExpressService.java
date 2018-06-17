package com.boilerplate.service.interfaces;

import java.io.IOException;

import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ExpressEntity;
import com.boilerplate.java.entities.ReportInputEntity;

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
	 * @throws ValidationFailedException
	 *             thrown when required input not provided
	 */
	public void validateName(ExpressEntity expressEntity) throws PreconditionFailedException, ValidationFailedException;

	/**
	 * This method is used to get the list of name for the given mobile number
	 * 
	 * @param expressEntity
	 *            This is the express entity which contains the mobile number
	 *            for which tha list of names has to be fetched
	 * @return the list of names
	 * @throws ValidationFailedException
	 * @throws IOException
	 */

	public ExpressEntity getNamesByMobileNumber(ExpressEntity expressEntity)
			throws ValidationFailedException, IOException;

	/**
	 * This method is used to get the logged in user details making a call to an
	 * api
	 * 
	 * @return the user details are returned in report input entity
	 */
	public ReportInputEntity getUserDetails();

}
