package com.boilerplate.service.implemetations;

import com.boilerplate.database.interfaces.IExpress;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.java.entities.ExpressEntity;
import com.boilerplate.service.interfaces.IExpressService;

/**
 * This
 * 
 * @author urvij
 *
 */
public class ExpressService implements IExpressService {

	/**
	 * This is an instance of expressDataAccess
	 */
	IExpress expressDataAccess;

	/**
	 * Sets the expressDataAccess
	 * 
	 * @param expressDataAccess
	 *            the expressDataAccess to set
	 */
	public void setExpressDataAccess(IExpress expressDataAccess) {
		this.expressDataAccess = expressDataAccess;
	}

	/**
	 * @see IExpressService.validateAndRegisterUser
	 */
	@Override
	public void validateName(ExpressEntity expressEntity) throws PreconditionFailedException {

		// get list name from express entity from db for given phone number
		ExpressEntity expressEntityFromDB = expressDataAccess.getUserExpressDetails(expressEntity.getMobileNumber());
		if (expressEntityFromDB != null) {
			expressEntityFromDB.getFullNameList().contains(expressEntity.getFullName());
			expressEntityFromDB.setFullName(expressEntity.getFullName());
			// save express entity with the correct full name set

		} else {
			throw new PreconditionFailedException("ExpressEntity", "No initial Express Attempt found", null);
		}
	}

}
