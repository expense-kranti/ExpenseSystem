package com.boilerplate.database.redis.implementation;

import java.sql.Date;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.java.entities.ReferalEntity;

/**
 * This class have method to manage user referral details
 * 
 * @author shiva
 *
 */
public class RedisReferral extends BaseRedisDataAccessLayer implements IReferral {

	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This variable is used to a prefix for key of user ReferredContact
	 */
	private static final String ReferredExpireContact = "ReferredExpireContact:";

	/**
	 * 
	 */
	private static final String ReferredContact = "ReferredContact:";

	/**
	 * This variable is used to a prefix for key of Campaign
	 */
	private static final String ReferSignUpCount = "ReferSignUpCount:";

	/**
	 * This variable is used to a prefix for key of user Referral
	 */
	private static final String ReferCounter = "ReferCounter:";

	/**
	 * @see IReferral.saveUserReferredContacts
	 */
	@Override
	public void saveUserReferredExpireContacts(ReferalEntity referalEntity) {
		// Run for loop to insert all referral contact to map
		for (Object contact : referalEntity.getReferralContacts()) {
			// Save data to redis
			super.set(
					ReferredExpireContact + referalEntity.getUserReferId() + ":"
							+ referalEntity.getReferralMediumType().toString() + ":" + ((String) contact).toUpperCase(),
					referalEntity.getReferralLink(),
					Integer.valueOf(configurationManager.get("REFERRED_CONTACT_EXPIRATION_TIME_IN_MINUTE")) * 60);
		}
	}

	/**
	 * @see IReferral.getUserReferredExpireContacts
	 */
	@Override
	public String getUserReferredExpireContacts(ReferalEntity referalEntity) {
		// Save data to redis
		return super.get(ReferredExpireContact + referalEntity.getUserReferId() + ":"
				+ referalEntity.getReferralMediumType().toString() + ":"
				+ ((String) referalEntity.getReferralContacts().get(0)).toUpperCase());
	}

	/**
	 * @see IReferral.saveUserReferUUID
	 */
	@Override
	public void saveUserReferUUID(ReferalEntity referalEntity) {
		// Save user's id and refer UUID in hash map
		super.hset(configurationManager.get("AKS_USER_UUID_HASH_BASE_TAG"), referalEntity.getUserId(),
				referalEntity.getUserReferId());
		super.hset(configurationManager.get("AKS_UUID_USER_HASH_BASE_TAG"), referalEntity.getUserReferId(),
				referalEntity.getUserId());
	}

	/**
	 * @see IReferral.saveUserReferUUID
	 */
	@Override
	public String getUserReferUUID(String userId) {
		// Save user's id and refer UUID in hash map
		return super.hget(configurationManager.get("AKS_USER_UUID_HASH_BASE_TAG"), userId);
	}

	/**
	 * @see IReferral.increaseDayCounter
	 */
	@Override
	public void increaseDayCounter(ReferalEntity referalEntity) {
		super.increaseCounter(ReferCounter + referalEntity.getUserReferId() + ":"
				+ referalEntity.getReferralMediumType().toString() + ":" + Date.valueOf(LocalDate.now()));
	}

	/**
	 * @see IReferral.increaseReferSignUpCounter
	 */
	@Override
	public void increaseReferSignUpCounter(ReferalEntity referalEntity) {
		super.increaseCounter(ReferSignUpCount + referalEntity.getUserReferId() + ":"
				+ referalEntity.getReferralMediumType().toString());
	}

	/**
	 * @see IReferral.getDayCount
	 */
	@Override
	public String getDayCount(ReferalEntity referalEntity) {
		return super.get(ReferCounter + referalEntity.getUserReferId() + ":"
				+ referalEntity.getReferralMediumType().toString() + ":" + Date.valueOf(LocalDate.now()));
	}
	
	/**
	 * @see IReferral.createDayCounter
	 */
	@Override
	public String createDayCounter(ReferalEntity referalEntity) {
		return super.set(ReferCounter + referalEntity.getUserReferId() + ":"
				+ referalEntity.getReferralMediumType().toString() + ":" + Date.valueOf(LocalDate.now()));
	}

	/**
	 * @see IReferral.saveUserReferContacts
	 */
	@Override
	public void saveUserReferContacts(ReferalEntity referalEntity) {
		// Run for loop to insert all referral contact to map
		for (Object contact : referalEntity.getReferralContacts()) {
			// Save data to redis
			super.set(
					ReferredContact + referalEntity.getUserReferId() + ":"
							+ referalEntity.getReferralMediumType().toString() + ":" + ((String) contact).toUpperCase(),
					referalEntity.getReferralLink());
		}
	}
}
