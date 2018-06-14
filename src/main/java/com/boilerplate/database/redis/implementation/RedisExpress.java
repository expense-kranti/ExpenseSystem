package com.boilerplate.database.redis.implementation;

import com.boilerplate.database.interfaces.IExpress;
import com.boilerplate.java.entities.ExpressEntity;

public class RedisExpress extends BaseRedisDataAccessLayer implements IExpress {

	/**
	 * This is the key used to represent user key
	 */
	private static final String User = "USER:";
	/**
	 * This is the key used to represent express key
	 */
	private static final String Express = "EXPRESS:";
	/**
	 * This is the key used to represent phonenumber key
	 */
	private static final String PHONE_NUMBER = "PHONE_NUMBER:";

	/**
	 * @see IExpress.getUserExpressDetails
	 */
	@Override
	public ExpressEntity getUserExpressDetails(String mobileNumber) {
		return super.get(User + Express + PHONE_NUMBER + mobileNumber, ExpressEntity.class);
	}

}
