package com.boilerplate.database.redis.implementation;

import com.boilerplate.database.interfaces.IIncomeTax;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.IncomeTaxEntity;

/**
 * This class is the redis implementation of IIncomeTax to store income tax
 * related data
 * 
 * @author urvij
 *
 */
public class RedisIncomeTax extends BaseRedisDataAccessLayer implements IIncomeTax {

	/**
	 * This is the starting key to be used in redis database to save data
	 * against
	 */
	public static final String IncomeTax = "INCOME_TAX:";

	@Override
	public IncomeTaxEntity saveIncomeTaxData(IncomeTaxEntity incomeTaxEntity) {
		super.set(IncomeTax + incomeTaxEntity.getUuid(), incomeTaxEntity);
		return incomeTaxEntity;
	}

	@Override
	public IncomeTaxEntity getIncomeTaxData(String uuid) throws NotFoundException {
		IncomeTaxEntity incomeTaxEntity = super.get(uuid, IncomeTaxEntity.class);
		if (incomeTaxEntity == null) {
			throw new NotFoundException("IncomeTaxEntity", "No Income Tax data found against given id", null);
		}
		return incomeTaxEntity;
	}
}
