package com.boilerplate.database.redis.implementation;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.java.Base;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ReferralContactEntity;
import com.boilerplate.java.entities.ReferredContactDetailEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	 * This is the blogUser key used to migrate redis data to mySql to to save
	 * in redis.sadd()
	 */
	private static final String ReferalKeyForSet = "AKS_REFERALCONTACTS_MYSQL";

	/**
	 * @see IReferral.saveUserReferredContacts
	 */
	@Override
	public void saveUserReferredExpireContacts(ReferalEntity referalEntity, String contact) {
		// save the contact detail with expiry time
		super.set(
				ReferredExpireContact + referalEntity.getUserReferId() + ":"
						+ referalEntity.getReferralMediumType().toString() + ":" + (contact).toUpperCase(),
				referalEntity.getReferralLink(),
				Integer.valueOf(configurationManager.get("REFERRED_CONTACT_EXPIRATION_TIME_IN_MINUTE")) * 60);

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
	 * @see IReferral.saveUserReferUUID
	 */
	@Override
	public String getReferUser(String uuid) {
		// Save user's id and refer UUID in hash map
		return super.hget(configurationManager.get("AKS_UUID_USER_HASH_BASE_TAG"), uuid);
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
	 * @see IReferral.createDayCounter
	 */
	@Override
	public void createSignUpCounter(ReferalEntity referalEntity, String initialValue) {
		super.set(
				ReferSignUpCount + referalEntity.getUserReferId() + ":"
						+ referalEntity.getReferralMediumType().toString(),
				initialValue,
				Integer.valueOf(configurationManager.get("REFERRED_CONTACT_EXPIRATION_TIME_IN_MINUTE_FOR_ONE_DAY"))
						* 60);
	}

	/**
	 * @see IReferral.getSignUpCount
	 */
	@Override
	public String getSignUpCount(ReferalEntity referalEntity) {
		return super.get(ReferSignUpCount + referalEntity.getUserReferId() + ":"
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
	public void createDayCounter(ReferalEntity referalEntity, String initialValue) {
		super.set(
				ReferCounter + referalEntity.getUserReferId() + ":" + referalEntity.getReferralMediumType().toString()
						+ ":" + Date.valueOf(LocalDate.now()),
				initialValue,
				Integer.valueOf(configurationManager.get("REFERRED_CONTACT_EXPIRATION_TIME_IN_MINUTE_FOR_ONE_DAY"))
						* 60);
	}

	/**
	 * @see IReferral.saveUserReferContacts
	 */
	@Override
	public void saveUserReferContacts(ReferalEntity referalEntity,
			ReferredContactDetailEntity referredContactDetailEntity)
			throws JsonParseException, JsonMappingException, IOException {

		Map<String, String> expressEntityHashMap = new ObjectMapper().readValue(referredContactDetailEntity.toJSON(),
				new TypeReference<HashMap<String, String>>() {
				});
		super.hmset(ReferredContact + referalEntity.getUserReferId() + ":"
				+ referalEntity.getReferralMediumType().toString() + ":"
				+ (referredContactDetailEntity.getContact()).toUpperCase(), expressEntityHashMap);

	}

	/**
	 * @see IReferral.getUserReferredContacts
	 */
	@Override
	public Map<String, String> getUserReferredContacts(ReferalEntity referalEntity, String contact) {

		return super.hgetAll(ReferredContact + referalEntity.getUserReferId() + ":"
				+ referalEntity.getReferralMediumType().toString() + ":" + contact.toUpperCase());

	}

	/**
	 * @see IReferral.deleteUserAllReferredContacts
	 */
	@Override
	public void deleteUserAllReferredContactsData(String userReferId) {
		Set<String> keys = this.getUserAllReferredContactsKeys(userReferId);
		for (String key : keys) {
			super.del(key);
		}
	}

	/**
	 * @see IReferral.deleteUserAllReferSignUpCount
	 */
	@Override
	public void deleteUserAllReferSignUpCountData(String userReferId) {
		Set<String> keys = this.getUserAllReferSignUpCountKeys(userReferId);
		for (String key : keys) {
			super.del(key);
		}
	}

	/**
	 * @see IReferral.deleteUserAllReferCounterData
	 */
	@Override
	public void deleteUserAllReferCounterData(String userReferId) {
		Set<String> referCounterKeys = this.getUserAllReferCounterKeys(userReferId);
		for (String key : referCounterKeys) {
			super.del(key);
		}
	}

	/**
	 * @see IReferral.deleteUserAllReferredExpireContactsData
	 */
	@Override
	public void deleteUserAllReferredExpireContactsData(String userReferId) {
		Set<String> referredExpireContactKeys = this.getUserAllReferredExpireContactKeys(userReferId);
		for (String key : referredExpireContactKeys) {
			super.keys(key);
		}
	}

	/**
	 * @see IReferral.getUserAllReferSignCountKeys
	 */
	@Override
	public Set<String> getUserAllReferSignUpCountKeys(String userReferId) {
		return super.keys(ReferSignUpCount + userReferId + "*");
	}

	/**
	 * @see IReferral.getUserAllReferCounterKeys
	 */
	@Override
	public Set<String> getUserAllReferCounterKeys(String userReferId) {
		return super.keys(ReferCounter + userReferId + "*" + "*");
	}

	/**
	 * @see IReferral.getUserAllReferredExpireContactKeys
	 */
	@Override
	public Set<String> getUserAllReferredExpireContactKeys(String userReferId) {
		return super.keys(ReferredExpireContact + userReferId + "*" + "*");
	}

	/**
	 * @see IReferral.getUserAllReferredExpireContactKeys
	 */
	@Override
	public Set<String> getAllReferredExpireContactKeys() {
		return super.keys(ReferredExpireContact + "*" + "*" + "*");
	}

	/**
	 * @see IReferral.getUSerAllReferredContactsKeys
	 */
	@Override
	public Set<String> getUserAllReferredContactsKeys(String userReferId) {
		return super.keys(ReferredContact + userReferId + "*" + "*");
	}

	/**
	 * @see IReferral.addInRedisSet
	 */
	@Override
	public void addInRedisSet(ReferalEntity referalEntity, ReferredContactDetailEntity referredContactDetailEntity) {
		super.sadd(ReferalKeyForSet,
				referalEntity.getUserReferId() + ":" + referalEntity.getReferralMediumType().toString() + ":"
						+ (referredContactDetailEntity.getContact()).toUpperCase());
	}

	/**
	 * @see IReferral.addInRedisSet
	 */
	@Override
	public void addInRedisSet(String userReferId, String referralMediumType, String contact) {
		super.sadd(ReferalKeyForSet, userReferId + ":" + referralMediumType + ":" + (contact).toUpperCase());
	}

	/**
	 * @see IReferral.addInRedisSetInReferredExpiredContact
	 */
	@Override
	public void addInRedisSetInReferredExpiredContact(String userReferId, String referralMediumType, String contact) {
		super.sadd(ReferredExpireContact, userReferId + ":" + referralMediumType + ":" + (contact).toUpperCase());
	}

	/**
	 * @see IReferral.fetchUserIdsFromRedisSet
	 */
	@Override
	public Set<String> fetchUserReferIdsFromRedisSet() {
		return super.smembers(ReferalKeyForSet);
	}

	/**
	 * @see IReferral.deleteRedisUserIdSet
	 */
	@Override
	public void deleteItemFromRedisUserReferIdSet(String userReferId) {
		super.srem(ReferalKeyForSet, userReferId);
	}

	/**
	 * @see IRedisAssessment.getAllMonthlyScoreKeys
	 */
	@Override
	public Set<String> getAllReferredContactKeys() {
		return super.keys(ReferredContact + "*" + "*");
	}

	@Override
	public ReferredContactDetailEntity getReferredContactDetailEntity(String redisReferalKey) {
		Map<String, String> referalContactMapData = super.hgetAll(ReferredContact + redisReferalKey);
		return Base.fromMap(referalContactMapData, ReferredContactDetailEntity.class);
	}

	@Override
	public void mySqlSaveReferalData(ReferralContactEntity referalContactList) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getUserReferralLink(String redisReferalExpiredKey) {
		// TODO Auto-generated method stub
		return super.get(ReferredExpireContact + redisReferalExpiredKey);
	}

}
