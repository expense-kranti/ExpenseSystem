package com.boilerplate.java.entities;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class is used to manage update referral contact data
 * 
 * @author shiva
 *
 */
public class UpdateReferralContactDetailsEntity extends BaseEntity
		implements Serializable, ICRMPublishDynamicURl, ICRMPublishEntity {
	/**
	 * This is the contact detail
	 */
	private String contact;

	/**
	 * This is referral uuid
	 */
	private String referralUUID;

	/**
	 * This is the coming user score
	 */
	private String comingUserScore;

	/**
	 * This referred user score
	 */
	private String referredUserScore;

	/**
	 * This is the user coming time
	 */
	private String comingTime;

	/**
	 * This is coming user id
	 */
	private String comingUserId;

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
	 * This method is used to get the coming user score
	 * 
	 * @return the comingUserScore
	 */
	public String getComingUserScore() {
		return comingUserScore;
	}

	/**
	 * This method is used to set the coming user score
	 * 
	 * @param comingUserScore
	 *            the comingUserScore to set
	 */
	public void setComingUserScore(String comingUserScore) {
		this.comingUserScore = comingUserScore;
	}

	/**
	 * This method is used to get the referred user score
	 * 
	 * @return the referredUserScore
	 */
	public String getReferredUserScore() {
		return referredUserScore;
	}

	/**
	 * This method is used to set the referred user score
	 * 
	 * @param referredUserScore
	 *            the referredUserScore to set
	 */
	public void setReferredUserScore(String referredUserScore) {
		this.referredUserScore = referredUserScore;
	}

	/**
	 * This method is used to get the coming user time
	 * 
	 * @return the comingTime
	 */
	public String getComingTime() {
		return comingTime;
	}

	/**
	 * This method is used to set the coming user time
	 * 
	 * @param comingTime
	 *            the comingTime to set
	 */
	public void setComingTime(String comingTime) {
		this.comingTime = comingTime;
	}

	/**
	 * This method is used to get the coming user id
	 * 
	 * @return the comingUserId
	 */
	public String getComingUserId() {
		return comingUserId;
	}

	/**
	 * This method is used to set the coming user id
	 * 
	 * @param comingUserId
	 *            the comingUserId to set
	 */
	public void setComingUserId(String comingUserId) {
		this.comingUserId = comingUserId;
	}

	/**
	 * @see ICRMPublishEntity.createPublishJSON
	 */
	@Override
	public String createPublishJSON(String template) throws UnauthorizedException {
		String retrunValue = template;
		retrunValue = retrunValue.replace("@contact", this.getContact() == null ? "{}" : this.getContact());
		retrunValue = retrunValue.replace("@referralUUID",
				this.getReferralUUID() == null ? "{}" : this.getReferralUUID());
		retrunValue = retrunValue.replace("@comingUserScore",
				this.getComingUserScore() == null ? "{}" : this.getComingUserScore());
		retrunValue = retrunValue.replace("@refferedUserScore",
				this.getReferredUserScore() == null ? "{}" : this.getReferredUserScore());
		retrunValue = retrunValue.replace("@comingTime", this.getComingTime() == null ? "{}" : this.getComingTime());
		retrunValue = retrunValue.replace("@comingUserId",
				this.getComingUserId() == null ? "{}" : this.getComingUserId());
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

	public UpdateReferralContactDetailsEntity() {

	}

	/**
	 * This method is used to construct this entity and set
	 * contact,referralUUID,comingUserScore, referredUserScore,comingTime and
	 * comingUserId
	 * 
	 * @param contact
	 *            this is the contact of referred user
	 * @param referralUUID
	 *            this is the referral UUID
	 * @param comingUserScore
	 *            this is the coming user gain score
	 * @param referredUserScore
	 *            this is the referred user score gain
	 * @param comingTime
	 *            this is the coming user sign up time
	 * @param comingUserId
	 *            this is the coming user id
	 */
	public UpdateReferralContactDetailsEntity(String contact, String referralUUID, String comingUserScore,
			String referredUserScore, String comingTime, String comingUserId) {
		this.contact = contact;
		this.referralUUID = referralUUID;
		this.comingUserScore = comingUserScore;
		this.referredUserScore = referredUserScore;
		this.comingTime = comingTime;
		this.comingUserId = comingUserId;
	}
}
