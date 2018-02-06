package com.boilerplate.database.interfaces;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ReferralContactEntity;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ReferredContactDetailEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This class provide the method for referral related operations regarding data
 * store
 * 
 * @author shiva
 *
 */
public interface IReferral {

	/**
	 * This method is used to save the user refer UUID
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding user like
	 *            user id and user refer UUID
	 */
	public void saveUserReferUUID(ReferalEntity referalEntity);

	/**
	 * This method is used to get the user refer id
	 * 
	 * @param userId
	 *            this is the user id
	 * @return the user refer id
	 */
	public String getUserReferUUID(String userId);

	/**
	 * This method is used to get the user referred contact referral link
	 * 
	 * @param referalEntity
	 * @return referral link
	 */
	public String getUserReferredExpireContacts(ReferalEntity referalEntity);

	/**
	 * This method is used to increase referring day count
	 * 
	 * @param referalEntity
	 *            this parameter define the referral request details
	 */
	public void increaseDayCounter(ReferalEntity referalEntity);

	/**
	 * This method is used to get the user refer day count
	 * 
	 * @param referalEntity
	 *            this parameter define the referral request details
	 * @return refer day count
	 */
	public String getDayCount(ReferalEntity referalEntity);

	/**
	 * This method is used to increase user referring signUp count
	 * 
	 * @param referalEntity
	 *            this parameter define the referral request details
	 */
	public void increaseReferSignUpCounter(ReferalEntity referalEntity);

	/**
	 * 
	 * @param referalEntity
	 * @return
	 */
	public void createDayCounter(ReferalEntity referalEntity, String initialValue);

	/**
	 * This method is used to save all those contact which is referred by user
	 * in current date
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the user
	 *            referral contacts by current date
	 * @param contact
	 *            this parameter contain the refer contact info
	 */
	public void saveUserReferredExpireContacts(ReferalEntity referalEntity, String contact);

	/**
	 * This method is used to save user referred contacts
	 * 
	 * @param referalEntity
	 *            this parameter define the referral request type
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public void saveUserReferContacts(ReferalEntity referalEntity,
			ReferredContactDetailEntity updateReferralContactDetailsEntity)
			throws JsonParseException, JsonMappingException, IOException;

	/**
	 * This method is used to get the refer user
	 * 
	 * @param uuid
	 *            this is the user refer id
	 * @return the referring user id
	 */
	public String getReferUser(String uuid);

	/**
	 * This method is used to create sign up counter
	 * 
	 * @param referalEntity
	 *            this parameter contain the information regarding the referral
	 *            details
	 * @param initialValue
	 *            this is the initial value for counter
	 */
	public void createSignUpCounter(ReferalEntity referalEntity, String initialValue);

	/**
	 * This method is used to get the sign up count
	 * 
	 * @param referalEntity
	 *            this parameter contain the information regarding the referral
	 *            details
	 * @return the sign up count
	 */
	public String getSignUpCount(ReferalEntity referalEntity);

	/**
	 * This method is used to get the user referred contact details
	 * 
	 * @param referalEntity
	 *            this parameter contain the information regarding the referral
	 *            details
	 * @param String
	 *            contact this parameter contains the information about referred
	 *            contact
	 * @return referred contact details all like when referred,refer type etc.
	 */
	public Map<String, String> getUserReferredContacts(ReferalEntity referalEntity, String contact);

	/**
	 * This method is used to delete all referred contacts of the user with
	 * given user referId
	 * 
	 * @param userReferId
	 *            the user refer id of the user whose all referred contacts to
	 *            be deleted
	 */
	public void deleteUserAllReferredContactsData(String userReferId);

	/**
	 * This method is used to get all the referred contact keys with the given
	 * userReferId
	 * 
	 * @param userReferId
	 *            The user refer id of the user against which the keys are to be
	 *            found
	 * @return the Set of all refer contacts keys with the given user refer Id
	 */
	public Set<String> getUserAllReferredContactsKeys(String userReferId);

	/**
	 * This method is used to delete all refer sign up count data of the user
	 * with given user refer id
	 * 
	 * @param userReferId
	 *            The userReferId of the user whose refer sign up count to be
	 *            deleted
	 */
	public void deleteUserAllReferSignUpCountData(String userReferId);

	/**
	 * This method is used to get all refer sign up count keys with the given
	 * user refer id of the user
	 * 
	 * @param userReferId
	 *            The user Refer Id of the user against which keys are to be
	 *            found
	 * @return the Set of all the referSignUpCount keys
	 */
	public Set<String> getUserAllReferSignUpCountKeys(String userReferId);

	/**
	 * This method is used to get all the keys of refer counter with given user
	 * refer id
	 * 
	 * @param userReferId
	 *            The user refer Id of the user whose refer counter keys to be
	 *            found
	 * @return The Set of all the refer counter keys of the user with given
	 *         userReferId of the user
	 */
	public Set<String> getUserAllReferCounterKeys(String userReferId);

	/**
	 * This method is used to get all the referred expire contact keys with
	 * given user refer id of the user
	 * 
	 * @param userReferId
	 *            The user refer id of the user
	 * @return The Set of all the keys of referred expire contacts against given
	 *         userReferId of the user
	 */
	public Set<String> getUserAllReferredExpireContactKeys(String userReferId);

	/**
	 * This method is used to delete user's all refer counter data
	 * 
	 * @param userReferId
	 *            The user refer id of the user whose refer counter data is to
	 *            be deleted
	 */
	public void deleteUserAllReferCounterData(String userReferId);

	/**
	 * This method is used to delete user's all the referred expire contacts
	 * data with userReferId of the user whose referredExpireContactsData to be
	 * deleted
	 * 
	 * @param userReferId
	 *            The user refer id of the user
	 */
	public void deleteUserAllReferredExpireContactsData(String userReferId);

	/**
	 * 
	 * @param referalContact
	 * @throws Exception
	 */
	public void mySqlSaveReferalData(ReferralContactEntity referalContact) throws Exception;

	/**
	 * This method is used to add key in Redis
	 * 
	 * @param referalEntity
	 */
	void addInRedisSet(ReferalEntity referalEntity);

	/**
	 * This method is used to fetch ReferalKey which is stored in redis
	 * 
	 * @return
	 */
	Set<String> fetchUserReferIdsFromRedisSet();

	/**
	 * This method is used to det
	 * 
	 * @param userReferId
	 */
	void deleteItemFromRedisUserReferIdSet(String userReferId);

	/**
	 * This method is use to get ReferredContactDetailEntity from redis database
	 * by its key
	 * 
	 * @param redisReferalKey
	 * @return
	 */
	public ReferredContactDetailEntity getReferredContactDetailEntity(String redisReferalKey);

	/**
	 * This method is used to get User referral link from redis database
	 * 
	 * @param redisReferalExpiredKey
	 * @return
	 */
	public String getUserReferralLink(String redisReferalExpiredKey);

}
