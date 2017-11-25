package com.boilerplate.database.redis.implementation;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.CampaignType;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.UserReferalMediumType;

/**
 * This class have method to manage user referral details
 * 
 * @author shiva
 *
 */
public class RedisReferral extends BaseRedisDataAccessLayer implements IReferral {

	/**
	 * This variable is used to a prefix for key of user ReferredContact
	 */
	private static final String ReferredContact = "ReferredContact:";

	/**
	 * This variable is used to a prefix for key of Campaign
	 */
	private static final String Campaign = "Campaign:";

	/**
	 * This variable is used to a prefix for key of user Referral
	 */
	private static final String UserReferral = "UserReferral:";

	/**
	 * @see IReferral.getUserReferredContacts
	 */
	@Override
	public ReferalEntity getUserReferredContacts() {
		ReferalEntity referalEntity = new ReferalEntity();
		// Declare a new map to store the referred data of user
		Map<String, Map<String, String>> userReferredContact = new BoilerplateMap<>();
		// For each type get data
		for (UserReferalMediumType referType : UserReferalMediumType.values()) {
			// Get the referred data of user
			Map<String, String> referralData = super.hgetAll(
					ReferredContact + RequestThreadLocal.getSession().getUserId() + ":" + Date.valueOf(LocalDate.now())
							+ ":" + referType.toString());
			// Put type of referred medium and its data to map
			userReferredContact.put(referType.toString(), referralData);

		}
		// Set the referred data into entity
		referalEntity.setReferredContacts(userReferredContact);
		return referalEntity;
	}

	/**
	 * @see IReferral.saveUserReferredContacts
	 */
	@Override
	public void saveUserReferredContacts(ReferalEntity referalEntity) {
		// Declare a new map used to hold the referral contacts
		BoilerplateMap<String, String> userReferralContact = new BoilerplateMap<>();
		// Run for loop to insert all referral contact to map
		for (Object o : referalEntity.getReferralContacts()) {
			super.hset(ReferredContact + RequestThreadLocal.getSession().getUserId() + ":"
					+ Date.valueOf(LocalDate.now()) + ":" + referalEntity.getReferralMediumType(), (String) o,
					referalEntity.getReferralLink());
		}
	}

	/**
	 * @see IReferral.getTodayReferredContactsCount
	 */
	@Override
	public Integer getTodayReferredContactsCount(UserReferalMediumType referralMediumType) {
		// Get the referred data of user
		Map<String, String> referralData = super.hgetAll(ReferredContact + RequestThreadLocal.getSession().getUserId()
				+ ":" + Date.valueOf(LocalDate.now()) + ":" + referralMediumType.toString());
		// Return map size
		return referralData.size();
	}

	/**
	 * @see IReferral.saveUserReferralDetail
	 */
	@Override
	public void saveUserReferralDetail(ReferalEntity referalEntity) {
		// Declare a new map used to hold the referral contacts
		BoilerplateMap<String, String> userReferralContact = new BoilerplateMap<>();
		// Run for loop to insert all referral contact to map
		for (Object o : referalEntity.getReferralContacts()) {
			super.hset(UserReferral + referalEntity.getUserId() + ":" + referalEntity.getReferralUUID(),
					referalEntity.getUserId(), referalEntity.getReferralLink());
		}
	}

	/**
	 * @see IReferral.saveReferralDetail
	 */
	@Override
	public void saveReferralDetail(ReferalEntity referalEntity) {
		// Declare a new map used to hold the referral contacts
		BoilerplateMap<String, String> userReferralContact = new BoilerplateMap<>();
		// Run for loop to insert all referral contact to map
		for (Object o : referalEntity.getReferralContacts()) {
			super.hset(
					Campaign + CampaignType.valueOf("Refer").toString() + ":" + referalEntity.getReferralMediumType()
							+ ":" + referalEntity.getReferralUUID(),
					referalEntity.getUserId(), referalEntity.getReferralLink());
		}
	}

}
