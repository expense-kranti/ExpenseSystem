package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Date;

import com.boilerplate.exceptions.rest.ValidationFailedException;

public class ReferralContactEntity extends BaseEntity implements Serializable {
	
	/**
	 * This is the user refer Id
	 */
	private String userReferId;
	
	/**
	 * This is the referal link
	 */
	private String referralLink;
	
	/**
	 * This is the referral medium type
	 */
	private String referralMediumType;
	
	/**
	 * This is the referral contact
	 */
	private String contact;
	
	/**
	 * This is the comming user score
	 */
	private String comingUserScore;
	
	/**
	 * This is the reffered user score
	 */
	private String refferedUserScore;
	
	/**
	 * This is the coming user Id
	 */
	private String comingUserId;
	
	/**
	 *  This is the coming user time
	 */
	private Date comingTime;
	
	/**
	 * get the referal user coming time
	 * @return
	 */
	public Date getComingTime() {
		return comingTime;
	}
	
	/**
	 * set the referal user coming time
	 * @param comingTime
	 */
	public void setComingTime(Date comingTime) {
		this.comingTime = comingTime;
	}

	/**
	 * get user refer id
	 * @return
	 */
	public String getUserReferId() {
		return userReferId;
	}

	/**
	 * set user refer id
	 * @param userReferId
	 */
	public void setUserReferId(String userReferId) {
		this.userReferId = userReferId;
	}
	
	/**
	 * get referral link 
	 * @return
	 */
	public String getReferralLink() {
		return referralLink;
	}

	/**
	 * set referral link
	 * @param referralLink
	 */
	public void setReferralLink(String referralLink) {
		this.referralLink = referralLink;
	}

	/**
	 * get the referral medium type
	 * @return
	 */
	public String getReferralMediumType() {
		return referralMediumType;
	}

	/**
	 * set the referral medium type
	 * @param referralMediumType
	 */
	public void setReferralMediumType(String referralMediumType) {
		this.referralMediumType = referralMediumType;
	}

	/**
	 * get the contact
	 * @return
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * set the contact
	 * @param contact
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * get the coming user score
	 * @return
	 */
	public String getComingUserScore() {
		return comingUserScore;
	}

	/**
	 * set the coming user score
	 * @param comingUserScore
	 */
	public void setComingUserScore(String comingUserScore) {
		this.comingUserScore = comingUserScore;
	}

	/**
	 * get the refered user score
	 * @return
	 */
	public String getRefferedUserScore() {
		return refferedUserScore;
	}

	/**
	 * set the refferd user score
	 * @param refferedUserScore
	 */
	public void setRefferedUserScore(String refferedUserScore) {
		this.refferedUserScore = refferedUserScore;
	}

	/**
	 * get the coming user id
	 * @return
	 */
	public String getComingUserId() {
		return comingUserId;
	}

	/**
	 * set the coming user id
	 * @param comingUserId
	 */
	public void setComingUserId(String comingUserId) {
		this.comingUserId = comingUserId;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
