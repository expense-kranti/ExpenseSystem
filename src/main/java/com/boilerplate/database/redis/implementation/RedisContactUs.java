package com.boilerplate.database.redis.implementation;

import java.util.UUID;

import com.boilerplate.database.interfaces.IContactUs;
import com.boilerplate.java.entities.ContactUsEntity;

/**
 * This class is for storing contact us queries in redis
 * @author mohit
 *
 */
public class RedisContactUs extends BaseRedisDataAccessLayer implements IContactUs{

	/**
	 * This is the contact us root tag
	 */
	private static final String ContactUsRootTag = "CONTACT_US_QUERIES:";
	
	/**
	 * @see IContactUs.saveContactUsQueries
	 */
	@Override
	public ContactUsEntity saveContactUsQueries(ContactUsEntity contactUsEntity) {
		super.set(ContactUsRootTag+contactUsEntity.getContactPersonMobileNumber(), contactUsEntity);
		return contactUsEntity;
	}
	
}
