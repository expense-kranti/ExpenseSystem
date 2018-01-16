package com.boilerplate.database.interfaces;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.IncomeTaxEntity;

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
	 */
	public IncomeTaxEntity saveIncomeTaxData(IncomeTaxEntity incomeTaxEntity);

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

}
