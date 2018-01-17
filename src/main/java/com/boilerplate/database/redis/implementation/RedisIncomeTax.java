package com.boilerplate.database.redis.implementation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.boilerplate.database.interfaces.IIncomeTax;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.Base;
import com.boilerplate.java.entities.IncomeTaxEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	/**
	 * @see IIncomeTax.saveIncomeTaxData
	 */
	@Override
	public IncomeTaxEntity saveIncomeTaxData(IncomeTaxEntity incomeTaxEntity)
			throws JsonParseException, JsonMappingException, IOException {

		// converting incometax entity into hash map to be saved as hashmap in
		// database
		Map<String, String> incomeTaxDataMap = new ObjectMapper().readValue(Base.toJSON(incomeTaxEntity),
				new TypeReference<HashMap<String, String>>() {
				});

		// set whole entity in datastore
		super.hmset(IncomeTax + incomeTaxEntity.getUuid(), incomeTaxDataMap);

		return incomeTaxEntity;
	}

	/**
	 * @see IIncomeTax.getIncomeTaxData
	 */
	@Override
	public IncomeTaxEntity getIncomeTaxData(String uuid) throws NotFoundException {
		Map<String, String> incomeTaxDataMap = super.hgetAll(IncomeTax + uuid);
		if (incomeTaxDataMap.isEmpty()) {
			throw new NotFoundException("IncomeTaxEntity", "No Income Tax data found against given id", null);
		}
		return Base.fromMap(incomeTaxDataMap, IncomeTaxEntity.class);
	}

	/**
	 * @see 
	 */
	@Override
	public void saveUserContacts(String uuid, String field, String value) {
		super.hset(IncomeTax + uuid, field, value);
	}

}
