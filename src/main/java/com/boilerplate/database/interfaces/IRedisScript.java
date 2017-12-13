package com.boilerplate.database.interfaces;

import java.util.Set;

import com.boilerplate.java.entities.ReferalEntity;

/**
 * This class provide the method for Script related operations regarding redis
 * 
 * @author shiva
 *
 */
public interface IRedisScript {

	/**
	 * This method is used to get all users keys
	 * 
	 * @return list of keys
	 */
	public Set<String> getAllUserKeys();

	
	/**
	 * This method gets the details of referrals by user on a medium
	 * @param referId
	 * @return ReferalEntity contains details of referrals
	 */
	public ReferalEntity getReferDetails(String userReferId, String referMedium);
}
