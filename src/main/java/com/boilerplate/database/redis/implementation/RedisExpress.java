package com.boilerplate.database.redis.implementation;

import com.boilerplate.database.interfaces.IExpress;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExpressEntity;
import com.boilerplate.java.entities.Report;

public class RedisExpress extends BaseRedisDataAccessLayer implements IExpress {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(RedisExpress.class);

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

	/**
	 * @see IExpress.saveUserExpressDetails
	 */
	@Override
	public ExpressEntity saveUserExpressDetails(ExpressEntity expressEntity) {
		logger.logInfo("ExpressService", "saveUserExpressDetails", "express entity is as:" + expressEntity, null);
		super.set(User + Express + PHONE_NUMBER + expressEntity.getMobileNumber(), expressEntity);
		return expressEntity;

	}

}
