package com.boilerplate.database.interfaces;

import java.util.Set;

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
}
