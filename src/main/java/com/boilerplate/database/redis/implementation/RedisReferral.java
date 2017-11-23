package com.boilerplate.database.redis.implementation;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.UserReferalMediumType;

public class RedisReferral extends BaseRedisDataAccessLayer implements IReferral {

	/**
	 * This variable is used to a prefix for key of user ReferredContact
	 */
	private static final String ReferralContact = "ReferralContact:";

	/**
	 * @see IReferral.getUserReferredContacts
	 */
	@Override
	public ReferalEntity getUserReferredContacts() {
		ReferalEntity referalEntity = new ReferalEntity();
		// Declare a new map to store the referred data of user
		Map<String, Map<String, String>> userReferredContact = new BoilerplateMap<>();
		// For each type get data
		for (UserReferalMediumType type : UserReferalMediumType.values()) {
			// Get the referred data of user
			Map<String, String> referralData = super.hgetAll(
					ReferralContact + RequestThreadLocal.getSession().getUserId() + ":" + Date.valueOf(LocalDate.now())
							+ ":" + type.toString());
			// Put type of referred medium and its data to map
			userReferredContact.put(type.toString(), referralData);

		}
		// Set the referred data into entity
		referalEntity.setReferredContacts(userReferredContact);
		return referalEntity;
	}

	/**
	 * @see IReferral.setUserReferralContacts
	 */
	@Override
	public void setUserReferralContacts(ReferalEntity referalEntity) {
		// Declare a new map used to hold the referral contacts
		BoilerplateMap<String, String> userReferralContact = new BoilerplateMap<>();
		// Run for loop to insert all referral contact to map
		for (Object o : referalEntity.getReferralContacts()) {
			super.hset(ReferralContact + RequestThreadLocal.getSession().getUserId() + ":"
					+ Date.valueOf(LocalDate.now()) + ":" + referalEntity.getReferralMediumType(), (String) o, "0");
		}

	}

}
