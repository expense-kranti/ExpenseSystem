package com.boilerplate.database.interfaces;

import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExpressEntity;
import com.boilerplate.java.entities.Report;

/**
 * This interface has methods to make express user registration and report
 * generation crud operations
 * 
 * @author urvij
 *
 */
public interface IExpress {
	/**
	 * This method is used to get the user names for express login validation
	 * 
	 * @param phoneNumber
	 *            the phone number of the user
	 * @return the express entity containing the list of names
	 */
	public ExpressEntity getUserExpressDetails(String phoneNumber);

	/**
	 * This method is used to save the list of user names into the redis database
	 * 
	 * @param expressEntity
	 *            this entity contains the list of names names and mobile number
	 * @return the expressEntity
	 */
	public ExpressEntity saveUserExpressDetails(ExpressEntity expressEntity);

}
