package com.boilerplate.database.interfaces;

import java.io.IOException;

import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.UpdateReferralContactDetailsEntity;
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
	public void createDayCounter(ReferalEntity referalEntity,String initialValue);
	/**
	 * This method is used to save all those contact which is referred by user
	 * in current date
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the user
	 *            referral contacts by current date
	 * @param contact this parameter contain the refer contact info
	 */
	void saveUserReferredExpireContacts(ReferalEntity referalEntity,
			String contact);
	/**
	 * This method is used to save user referred contacts
	 * 
	 * @param referalEntity
	 *            this parameter define the referral request type
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	void saveUserReferContacts(ReferalEntity referalEntity,
			UpdateReferralContactDetailsEntity updateReferralContactDetailsEntity) throws JsonParseException, JsonMappingException, IOException;

}
