package com.boilerplate.database.redis.implementation;

import java.util.Set;

import com.boilerplate.database.interfaces.IRedisScript;

public class RedisScript  extends BaseRedisDataAccessLayer implements IRedisScript {
	
	/**
	 * This is the main key of user in redis data base
	 */
	private static final String User = "USER:";
	
	/**
	 * @see IRedisScript.getAllUserKeys
	 */
	@Override
	public Set<String> getAllUserKeys() {
		Set<String> listOfUserKey = super.keys(User);
		return listOfUserKey;		
	}

}
