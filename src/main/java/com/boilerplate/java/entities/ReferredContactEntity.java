package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Random;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class is used to manage the referral link data
 * 
 * @author shiva
 *
 */
public class ReferredContactEntity extends BaseEntity implements Serializable {

	/**
	 * This is the referral link
	 */
	private String referralLink;

	/**
	 * This is the contact which will receive the referral link
	 */
	private String contact;

	/**
	 * This is the referral UUID
	 */
	private String referralUUID;

	/**
	 * This method is used to get the referral UUID
	 * 
	 * @return the referralUUID
	 */
	public String getReferralUUID() {
		return referralUUID;
	}

	/**
	 * This method is used to set the referral UUID
	 * 
	 * @param referralUUID
	 *            the referralUUID to set
	 */
	public void setReferralUUID(String referralUUID) {
		this.referralUUID = referralUUID;
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
	 * This method is used to get the contact
	 * 
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * This method is used to set the contact
	 * 
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @see BaseEntity.validate
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
}
