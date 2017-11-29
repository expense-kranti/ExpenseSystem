package com.boilerplate.java.entities;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This entity represents the reference of user
 * 
 * @author kranti123
 *
 */
public class ReferalEntity extends BaseEntity implements Serializable, ICRMPublishDynamicURl, ICRMPublishEntity {

	/**
	 * This is the list of referred users' contacts
	 */
	private BoilerplateList<ReferralLinkEntity> referralContacts;

	/**
	 * This is user referred contact details
	 */
	private Map<String, Map<String, Map<String, String>>> referredContacts;

	/**
	 * This is the userId
	 */
	@JsonIgnore
	private String userId;

	/**
	 * This is the referral UUID
	 */
	@JsonIgnore
	private String referralUUID;

	/**
	 * This is the referral link
	 */
	private String referralLink;

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
	public BoilerplateList<ReferralLinkEntity> getReferralContacts() {
		return referralContacts;
	}

	/**
	 * This sets the referral contact list
	 * 
	 * @param referralContacts
	 *            The referral contacts list
	 */
	public void setReferralContacts(BoilerplateList<ReferralLinkEntity> referralContacts) {
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

	/**
	 * This method is used to construct this entity and set campaign type ,uuid
	 * and userId
	 * 
	 * @param mediumType
	 *            this is the refer medium type
	 * @param uuid
	 *            this is the referral code uuid
	 * @param userId
	 *            this is the user id of those which refer its friend
	 */
	public ReferalEntity(String mediumType, String uuid, String userId) {
		this.referralUUID = uuid;
		this.referralMediumType = UserReferalMediumType.valueOf(mediumType);
		this.userId = userId;
	}

	/**
	 * This method is used to construct this entity and set campaign type and
	 * userId
	 * 
	 * @param mediumType
	 *            this is the refer medium type
	 * @param userId
	 *            this is the user id of those which refer its friend
	 */
	public ReferalEntity(UserReferalMediumType mediumType, String userId) {
		this.referralMediumType = mediumType;
		this.userId = userId;
	}

	/**
	 * This is a simple constructor
	 */
	public ReferalEntity() {

	}

	/**
	 * @see ICRMPublishEntity.createPublishJSON
	 */
	@Override
	public String createPublishJSON(String template) throws UnauthorizedException {
		String retrunValue = template;
		retrunValue = retrunValue.replace("@userId", this.getUserId() == null ? "{}" : this.getUserId());
		retrunValue = retrunValue.replace("@referralUUID",
				this.getReferralUUID() == null ? "{}" : this.getReferralUUID());
		retrunValue = retrunValue.replace("@referralContacts",
				this.getReferralContacts() == null ? "{}" : this.getReferralContacts().toString());
		retrunValue = retrunValue.replace("@type",
				this.getReferralMediumType().toString() == null ? "{}" : this.getReferralMediumType().toString());
		return retrunValue;
	}

	/**
	 * @see ICRMPublishDynamicURl.createPublishUrl
	 */
	@Override
	public String createPublishUrl(String url) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method get the referral link
	 * 
	 * @return the referralLink
	 */
	public String getReferralLink() {
		return referralLink;
	}

	/**
	 * This method set the referral link
	 * 
	 * @param referralLink
	 *            the referralLink to set
	 */
	public void setReferralLink(String referralLink) {
		this.referralLink = referralLink;
	}

}
