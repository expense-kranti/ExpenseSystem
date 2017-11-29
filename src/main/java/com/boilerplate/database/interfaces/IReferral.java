package com.boilerplate.database.interfaces;

import com.boilerplate.java.entities.ReferalEntity;

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
	 * This method is used to save all those contact which is referred by user
	 * in current date
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the user
	 *            referral contacts by current date
	 */
	public void saveUserReferredExpireContacts(ReferalEntity referalEntity);

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
	 * This method is used to save user referred contacts
	 * 
	 * @param referalEntity
	 *            this parameter define the referral request type
	 */
	public void saveUserReferContacts(ReferalEntity referalEntity);

	/**
	 * 
	 * @param referalEntity
	 * @return
	 */
	public String createDayCounter(ReferalEntity referalEntity);

}
