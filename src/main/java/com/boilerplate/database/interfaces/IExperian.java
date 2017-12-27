package com.boilerplate.database.interfaces;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.Voucher;

public interface IExperian {
	/**
	 * Creates a list of vouchers in the database
	 * @param vouchers
	 */
	public void create(BoilerplateList<Voucher> vouchers);
	
	/**
	 * Gets the voucher code for the user
	 * @param userId The user id
	 * @param sessionId The session id
	 */
	public Voucher getVoucherCode(String userId, String sessionId)  throws NotFoundException;
	
	/**
	 * Logs information of experian flow to the database
	 * @param userId The id of the use
	 * @param purpose The purpose of request
	 * @param httpContent The http content
	 */
	public void logToExperianDatabase(String userId,String purpose,String httpContent);
}
