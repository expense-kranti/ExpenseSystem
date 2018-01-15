package com.boilerplate.java.entities;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

import org.h2.util.New;

import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.fasterxml.jackson.annotation.JsonIgnore;

import scala.util.parsing.combinator.testing.Str;

/**
 * This entity represents the reference of user
 * 
 * @author 
 *
 */
public class ReferalEntity extends BaseEntity implements Serializable, ICRMPublishDynamicURl, ICRMPublishEntity {

	/**
	 * This is the list of referred users' contacts
	 */
	private BoilerplateList<String> referralContacts;

	/**
	 * This is the list of referred users' contacts
	 */
	@JsonIgnore
	private BoilerplateList<ReferredContactDetailEntity> referredContact;

	/**
	 * This is the userId
	 */
	@JsonIgnore
	private String userReferId;

	/**
	 * This is the referral link
	 */
	private String referralLink;

	/**
	 * This is the userId
	 */
	@JsonIgnore
	private String userId;

	/**
	 * This is the type of referral medium
	 */
	private UserReferalMediumType referralMediumType;

	public ReferalEntity(UserReferalMediumType mediumType, String userId) {
		this.referralMediumType = mediumType;
		this.userId = userId;
	}

	public ReferalEntity() {
		// TODO Auto-generated constructor stub
	}

	public ReferalEntity(String campaignType, String userId, String userReferId) {
		this.referralMediumType = UserReferalMediumType.valueOf(campaignType);
		this.userId = userId;
		this.userReferId = userReferId;
	}

	/**
	 * This constructor is used construct this entity and set the user id and
	 * user refer id
	 * 
	 * @param userId
	 *            this is the user id need to be set
	 * @param userReferId
	 *            this is the user refer id need to be set
	 */
	public ReferalEntity(String userId, String userReferId) {
		// Set user id
		this.userId = userId;
		// Set user refer id
		this.userReferId = userReferId;
	}

	/**
	 * This method is used to get the referral contacts
	 * 
	 * @return the referralContacts
	 */
	public BoilerplateList<String> getReferralContacts() {
		return referralContacts;
	}

	/**
	 * This method is used to set the referral contacts
	 * 
	 * @param referralContacts
	 *            the referralContacts to set
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
	 * This method is used to get the user refer id
	 * 
	 * @return the userReferId
	 */
	public String getUserReferId() {
		return userReferId;
	}

	/**
	 * This method is used to set the user refer id
	 * 
	 * @param userReferId
	 *            the userReferId to set
	 */
	public void setUserReferId(String userReferId) {
		this.userReferId = userReferId;
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
	 * @see ICRMPublishEntity.createPublishJSON
	 */
	@Override
	public String createPublishJSON(String template) throws UnauthorizedException {
		String retrunValue = template;
		retrunValue = retrunValue.replace("@userId", this.getUserId() == null ? "{}" : this.getUserId());
		retrunValue = retrunValue.replace("@referralUUID",
				this.getUserReferId() == null ? "{}" : this.getUserReferId());
		String referralLink = this.getReferralLink() == null ? "" : this.getReferralLink();
		retrunValue = retrunValue.replace("@referralLink", referralLink);
		retrunValue = retrunValue.replace("@referralContacts",
				this.getReferredContact() == null ? "[]" : this.getReferredContact().toString());
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
	 * This method is used to get the user id
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
	 * This method is used to get the day count
	 * 
	 * @return the dayCount
	 */
	public BoilerplateMap<String, String> getDayCount() {
		return dayCount;
	}

	/**
	 * This method is used to set the day count
	 * 
	 * @param dayCount
	 *            the dayCount to set
	 */
	public void setDayCount(BoilerplateMap<String, String> dayCount) {
		this.dayCount = dayCount;
	}

	/**
	 * This method is used to get the referred contacts
	 * 
	 * @return the referredContact
	 */
	public BoilerplateList<ReferredContactDetailEntity> getReferredContact() {
		return referredContact;
	}

	/**
	 * This method is used to set the referred contacts
	 * 
	 * @param referredContact
	 *            the referredContact to set
	 */
	public void setReferredContact(BoilerplateList<ReferredContactDetailEntity> referredContact) {
		this.referredContact = referredContact;
	}

	private BoilerplateMap<String, String> dayCount;
}
