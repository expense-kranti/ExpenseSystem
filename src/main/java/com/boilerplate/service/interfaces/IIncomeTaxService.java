package com.boilerplate.service.interfaces;

import java.io.IOException;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.IncomeTaxEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This class has methods that provide service for Income tax calculation
 * 
 * @author urvij
 *
 */
public interface IIncomeTaxService {

	/**
	 * This method calculates tax with general predefined deductions and not
	 * with other sophisticated deductions invested
	 * 
	 * @return IncomeTaxEntity that contains the close to estimated tax and
	 *         monthly take home salary
	 * @throws ValidationFailedException
	 *             thrown when required input is not provided
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public IncomeTaxEntity calculateSimpleTax(IncomeTaxEntity incomeTaxEntity)
			throws ValidationFailedException, JsonParseException, JsonMappingException, IOException;

	/**
	 * This method is used to get income tax data against a given uuid
	 * 
	 * @return the found income tax data
	 * @throws NotFoundException
	 *             thrown when income tax data is not found against uuid
	 */
	public IncomeTaxEntity getIncomeTaxData(IncomeTaxEntity incomeTaxEntity) throws NotFoundException;

	/**
	 * This method is used to calculate estimated tax with investments being
	 * deducted
	 * 
	 * @param incomeTaxEntity
	 *            it contains the values that are required to calculate income
	 *            tax
	 * @return incometaxentity with estimated tax calculated
	 * @throws NotFoundException
	 *             thrown when income tax data is not found in data store
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws ValidationFailedException thrown when required input is not provided or have discrepancies
	 */
	public IncomeTaxEntity calculateTaxWithInvestments(IncomeTaxEntity incomeTaxEntity)
			throws NotFoundException, JsonParseException, JsonMappingException, IOException, ValidationFailedException;

	/**
	 * This method saves user details against income tax data in data store with
	 * matched uuid
	 * 
	 * @param incomeTaxEntity
	 *            contains user details like emailId, phonenumber, firstname
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void saveIncomeTaxUserDetails(IncomeTaxEntity incomeTaxEntity)
			throws JsonParseException, JsonMappingException, IOException;

}
