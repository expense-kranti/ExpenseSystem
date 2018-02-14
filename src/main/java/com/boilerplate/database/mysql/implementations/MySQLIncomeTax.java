package com.boilerplate.database.mysql.implementations;

import com.boilerplate.database.interfaces.IIncomeTax;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.IncomeTaxEntity;

/**
 * This class is used to manage income tax data in MySQL database by
 * implementing methods from IIncomeTax
 * 
 * @author urvij
 *
 */
public class MySQLIncomeTax extends MySQLBaseDataAccessLayer implements IIncomeTax {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(MySQLIncomeTax.class);

	/**
	 * @see IIncomeTax.saveIncomeTaxData
	 */
	@Override
	public IncomeTaxEntity saveIncomeTaxData(IncomeTaxEntity incomeTaxEntity) throws Exception {
		// save income tax data in MySQL
		try {
			super.create(incomeTaxEntity);
		} catch (Exception ex) {
			logger.logException("MySQLIncomeTax", "saveIncomeTaxData", "Catch block",
					" IncomeTax calculating UserId : " + incomeTaxEntity.getUserId() + " Exception message is : "
							+ ex.getMessage() + " Exception Cause is : " + ex.getCause().toString(),
					ex);
			throw ex;
		}
		return incomeTaxEntity;
	}

	/**
	 * @see IIncomeTax.getIncomeTaxData
	 */
	@Override
	public IncomeTaxEntity getIncomeTaxData(String uuid) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see IIncomeTax.saveUserContacts
	 */
	@Override
	public void saveUserContacts(String uuid, String field, String value) {
		// TODO Auto-generated method stub

	}

}
