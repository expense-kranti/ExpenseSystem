package com.boilerplate.database.interfaces;

import java.io.IOException;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.IncomeTaxEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This interface has methods to manage incometax related data in data store
 * 
 * @author urvij
 *
 */
public interface IIncomeTax {

	/**
	 * This method is used to save income tax related data against a uuid in
	 * data base
	 * 
	 * @param incomeTaxEntity
	 *            contains all the income tax data need to be saved
	 * @return incomeTaxEntity with uuid against which data is saved
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws Exception
	 *             thrown when any exception occurs in saving the
	 *             incometaxentity
	 */
	public IncomeTaxEntity saveIncomeTaxData(IncomeTaxEntity incomeTaxEntity)
			throws JsonParseException, JsonMappingException, IOException, Exception;

	/**
	 * This method is used to get income tax details against a uuid
	 * 
	 * @param uuid
	 *            this is the uuid against which income tax details are to get
	 * @return the income tax entity containing income tax details
	 * @throws NotFoundException
	 *             this exception is thrown when no income tax data is not found
	 */
	public IncomeTaxEntity getIncomeTaxData(String uuid) throws NotFoundException;

	/**
	 * This method is used to save user contacts in income tax details with
	 * matching uuid
	 * 
	 * @param uuid
	 *            the uuid of income tax details against which contacts are to
	 *            be saved
	 * @param field
	 *            the contanct fields to be saved like emailId, firsName,
	 *            phoneNumber
	 * @param value
	 *            the contact values to be saved
	 */
	public void saveUserContacts(String uuid, String field, String value);

}
