package com.boilerplate.service.interfaces;

import java.io.IOException;
import java.text.ParseException;

import com.boilerplate.exceptions.rest.NotFoundException;
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
	 *            for which the list of names has to be fetched
	 * @return the list of names
	 * @throws ValidationFailedException
	 *             This exception is occurred when the mobile number is null or
	 *             empty
	 * @throws IOException
	 *             thrown when IOException occurs (while making request to the
	 *             api for getting name)
	 * @throws PreconditionFailedException
	 *             thrown when no name found for the given mobile number
	 */

	public ExpressEntity getNamesByMobileNumber(ExpressEntity expressEntity)
			throws ValidationFailedException, IOException, PreconditionFailedException;

	/**
	 * This method is used to get the logged in user details making a call to an
	 * api
	 * 
	 * @return the user details are returned in report input entity
	 * @throws IOException
	 *             thrown when IOException occurs (while making request to the
	 *             api for getting name)
	 * @throws PreconditionFailedException
	 *             thrown when no details found for logged in user's mobile
	 *             number and full name
	 * @throws ValidationFailedException
	 *             thrown when user full name is not found in db for logged in
	 *             user
	 * @throws NotFoundException
	 *             thrown when no express attempt found for logged in user
	 * @throws ParseException
	 *             thrown when exception occurs in parsing the string to date
	 */
	public ReportInputEntity getUserDetails() throws IOException, PreconditionFailedException,
			ValidationFailedException, NotFoundException, ParseException;

}
