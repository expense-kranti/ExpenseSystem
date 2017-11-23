package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Map;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;

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
	BoilerplateList<String> referralContacts;

	/**
	 * This is user referred contact details
	 */
	Map<String, Map<String, String>> referredContacts;

	/**
	 * This method is used to get the referred contacts details
	 * 
	 * @return the referredContacts
	 */
	public Map<String, Map<String, String>> getReferredContacts() {
		return referredContacts;
	}

	/**
	 * This method is used to set the referred contacts details
	 * 
	 * @param referredContacts
	 *            the referredContacts to set
	 */
	public void setReferredContacts(Map<String, Map<String, String>> referredContacts) {
		this.referredContacts = referredContacts;
	}

	/**
	 * This is the type of referral medium
	 */
	UserReferalMediumType referralMediumType;

	/**
	 * This is the map of phoneNumber referral medium type to referralContacts
	 */
	BoilerplateMap<String, String> phoneNumberReferrals;

	/**
	 * This is the map of email referral medium type to referralContacts
	 */
	BoilerplateMap<String, String> emailReferrals;

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
	 * @param referralContact
	 *            The referral contacts list
	 */
	public void setReferralContact(BoilerplateList<String> referralContacts) {
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
	 * This gets the map of phone number referrals
	 * 
	 * @return The map of phone number referrals
	 */
	public BoilerplateMap<String, String> getPhoneNumberReferrals() {
		return phoneNumberReferrals;
	}

	/**
	 * This sets the phone number referral map
	 * 
	 * @param phoneNumberReferrals
	 *            The phone
	 */
	public void setPhoneNumberReferrals(BoilerplateMap<String, String> phoneNumberReferrals) {
		this.phoneNumberReferrals = phoneNumberReferrals;
	}

	/**
	 * This gets the email referral map
	 * 
	 * @return The email referral map
	 */
	public BoilerplateMap<String, String> getEmailReferrals() {
		return emailReferrals;
	}

	/**
	 * This sets the email referral map
	 * 
	 * @param emailReferrals
	 *            The email referral map
	 */
	public void setEmailReferrals(BoilerplateMap<String, String> emailReferrals) {
		this.emailReferrals = emailReferrals;
	}

	/**
	 * @see BaseEntity.validate()
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
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
