package com.boilerplate.service.implemetations;

import com.boilerplate.database.interfaces.IExpress;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.java.entities.ExpressEntity;
import com.boilerplate.service.interfaces.IExpressService;

/**
 * 
 * @author urvij
 *
 */
public class ExpressService implements IExpressService {

	/**
	 * This is an instance of expressDataAccess
	 */
	IExpress expressDataAccess;

	@Override
	public ExpressEntity validateAndRegisterUser(ExpressEntity expressEntity) throws PreconditionFailedException {
		String fullName = expressEntity.getFullName();
		// get list name from express entity from db for given phone number
		ExpressEntity expressEntityFromDB = expressDataAccess.getUserExpressDetails(expressEntity.getMobileNumber());
		if (expressEntityFromDB != null) {
			expressEntityFromDB.getFullNameList().contains(expressEntity.getFullName());
		} else {
			throw new PreconditionFailedException("ExpressEntity", "No Express Attempt found", null);
		}
	}

}
