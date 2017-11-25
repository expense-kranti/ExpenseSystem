package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;

/**
 * This entity represents the reference of user
 * 
 * @author kranti123
 *
 */
public class ReferalEntity extends BaseEntity implements Serializable {

	/**
	 * This is the list of referred users' contacts
	 */
	private BoilerplateList<String> referralContacts;

	/**
	 * This is user referred contact details
	 */
	private Map<String, Map<String, Map<String, String>>> referredContacts;

	/**
	 * This is the userId
	 */
	private String userId;

	/**
	 * This is the referral link
	 */
	private String referralLink;

	/**
	 * This is the referral UUID
	 */
	private String referralUUID;

	/**
	 * This method is used to get the referred contacts details
	 * 
	 * @return the referredContacts
	 */
	public Map<String, Map<String, Map<String, String>>> getReferredContacts() {
		return referredContacts;
	}

	/**
	 * This method is used to set the referred contacts details
	 * 
	 * @param referredContacts
	 *            the referredContacts to set
	 */
	public void setReferredContacts(Map<String, Map<String, Map<String, String>>> referredContacts) {
		this.referredContacts = referredContacts;
	}

	/**
	 * This is the type of referral medium
	 */
	private UserReferalMediumType referralMediumType;

	/**
	 * This gets the list of referral contacts
	 * 
	 * @return The list of referral contacts
	 */
	public BoilerplateList<String> getReferralContacts() {
		return referralContacts;
	}

	/**
	 * This sets the referral contact list
	 * 
	 * @param referralContacts
	 *            The referral contacts list
	 */
	public void setReferralContacts(BoilerplateList<String> referralContacts) {
		this.referralContacts = referralContacts;
	}

	/**
	 * This gets the type of referral medium
	 * 
	 * @return The referral medium type
	 */
	public UserReferalMediumType getReferralMediumType() {
		return referralMediumType;
	}

	/**
	 * This sets the type of referral medium type
	 * 
	 * @param referralMediumType
	 *            The referral medium type
	 */
	public void setreferralMediumType(UserReferalMediumType referralMediumType) {
		this.referralMediumType = referralMediumType;
	}

	/**
	 * This method is used to get the userId
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * This method is used to set the userId
	 * 
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This method is used to get the referral link
	 * 
	 * @return the referralLink
	 */
	public String getReferralLink() {
		return referralLink;
	}

	/**
	 * This method is used to set the referral link
	 * 
	 * @param referralLink
	 *            the referralLink to set
	 */
	public void setReferralLink(String referralLink) {
		this.referralLink = referralLink;
	}

	/**
	 * This method is used to get the referral uuid
	 * 
	 * @return the referralUUID
	 */
	public String getReferralUUID() {
		return referralUUID;
	}

	/**
	 * This method is used to set the referral uuid
	 * 
	 * @param referralUUID
	 *            the referralUUID to set
	 */
	public void setReferralUUID(String referralUUID) {
		this.referralUUID = referralUUID;
	}

	/**
	 * This method is used to create the UUID
	 * 
	 * @return the UUID
	 */
	public void createUUID(Integer uuidLength) {
		// New instance of random
		Random rand = new Random();
		this.referralUUID = "";
		// Run a for loop to generate a configurations define length uuid
		for (int i = 0; i < uuidLength; i++) {
			// Get random number
			int randomNum = rand.nextInt(26 - 0);
			// Concatenate new char to string
			referralUUID = referralUUID + String.valueOf((char) (randomNum + 97));
		}
	}

	/**
	 * @see BaseEntity.validate()
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// In case user refer zero size referral contacts
		if (this.referralContacts.size() == 0) {
			throw new ValidationFailedException("ReferalEntity", "There is no referred contacts in list", null);
		} // In case user refer more then 10 referral contacts
		else if (this.referralContacts.size() > 10) {
			throw new ValidationFailedException("ReferalEntity", "Reached max limit", null);
		} // In case referral medium type is blank or null
		else if (this.referralMediumType.equals("") || this.referralMediumType == null) {
			throw new ValidationFailedException("ReferalEntity", "Reached max limit", null);
		}
		return true;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}

}
