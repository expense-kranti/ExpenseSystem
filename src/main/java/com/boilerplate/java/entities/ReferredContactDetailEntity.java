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
public class ReferredContactDetailEntity extends BaseEntity
		implements Serializable, ICRMPublishDynamicURl, ICRMPublishEntity {
	/**
	 * This is the contact detail
	 */
	private String contact;

	/**
	 * This is the coming user score
	 */
	private String comingUserScore;

	/**
	 * This referred user score
	 */
	private String refferedUserScore;

	/**
	 * This is the user coming time
	 */
	private String comingTime;

	/**
	 * This is coming user id
	 */
	private String comingUserId;
	
	/**
	 * this is the referalId
	 */
	private String refralId;
	
	/**
	 * this method is used to get referral id
	 * @return
	 */
	public String getReferalId(){
		return refralId;
	}
	
	/**
	 * this method is to set the referalId
	 * @param referalId
	 */
	public void setReferalId(String referalId){
		this.refralId = referalId;
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
	public String getrefferedUserScore() {
		return refferedUserScore;
	}

	/**
	 * This method is used to set the referred user score
	 * 
	 * @param referredUserScore
	 *            the referredUserScore to set
	 */
	public void setrefferedUserScore(String referredUserScore) {
		this.refferedUserScore = referredUserScore;
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
		retrunValue = retrunValue.replace("@comingUserScore",
				this.getComingUserScore() == null ? "{}" : this.getComingUserScore());
		retrunValue = retrunValue.replace("@refferedUserScore",
				this.getrefferedUserScore() == null ? "{}" : this.getrefferedUserScore());
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

	/**
	 * This is a simple constructor
	 */
	public ReferredContactDetailEntity() {

	}

	/**
	 * This method is used to construct this entity and set
	 * contact,referralUUID,comingUserScore, referredUserScore,comingTime and
	 * comingUserId
	 * 
	 * @param contact
	 *            this is the contact of referred user
	 * @param comingUserScore
	 *            this is the coming user gain score
	 * @param referredUserScore
	 *            this is the referred user score gain
	 * @param comingTime
	 *            this is the coming user sign up time
	 * @param comingUserId
	 *            this is the coming user id
	 */
	public ReferredContactDetailEntity(String contact, String comingUserScore, String referredUserScore,
			String comingTime, String comingUserId) {
		this.contact = contact;
		this.comingUserScore = comingUserScore;
		this.refferedUserScore = referredUserScore;
		this.comingTime = comingTime;
		this.comingUserId = comingUserId;
	}

	/**
	 * This method is used to construct this entity and set contact and
	 * referring time
	 * 
	 * @param contact
	 *            this is the contact of referred user
	 * @param creationTime
	 *            this is the creation time
	 */
	public ReferredContactDetailEntity(String contact, String creationTime) {
		this.stringCreationDate = creationTime;
		this.contact = contact;
	}

}
