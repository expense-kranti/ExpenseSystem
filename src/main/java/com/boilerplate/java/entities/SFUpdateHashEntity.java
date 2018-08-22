package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

public class SFUpdateHashEntity extends BaseEntity implements Serializable{

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}
	
	
	/**
	 * This method get the account id
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * This method set the account id
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * This method get the location
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * This method set the location
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * This method get the employmentStatus
	 * @return the employmentStatus
	 */
	public String getEmploymentStatus() {
		return employmentStatus;
	}

	/**
	 * This method set the employmentStatus
	 * @param employmentStatus the employmentStatus to set
	 */
	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	/**
	 * This method get the alternateNumber
	 * @return the alternateNumber
	 */
	public String getAlternateNumber() {
		return alternateNumber;
	}

	/**
	 * This method set the alternateNumber
	 * @param alternateNumber the alternateNumber to set
	 */
	public void setAlternateNumber(String alternateNumber) {
		this.alternateNumber = alternateNumber;
	}

	/**
	 * This method get the dob
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}

	/**
	 * This method set the dob
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}


	/**
	 * This method get the updation date time
	 * @return the updatedDateAndTime
	 */
	public String getUpdatedDateAndTime() {
		return updatedDateAndTime;
	}

	/**
	 * This method set the updation date time
	 * @param updatedDateAndTime the updatedDateAndTime to set
	 */
	public void setUpdatedDateAndTime(String updatedDateAndTime) {
		this.updatedDateAndTime = updatedDateAndTime;
	}


	/**
	 * This is the accountId
	 */
	private String accountId;
	
	/**
	 * This is the user location
	 */
	private String location;
	/**
	 * This is the employmentStatus
	 */
	private String employmentStatus;
	/**
	 * This is the alternateNumber
	 */
	private String alternateNumber;
	/**
	 * This is the dob
	 */
	private String dob;
	
	/**
	 * This is the updatedDateAndTime
	 */
	private String updatedDateAndTime;
	/**
	 * This is the user refer uuid
	 */
	private String referUUID;
	/**
	 * This is the feedbackstatus
	 */
	private String feedbackStatus;

	/**
	 * This method get the refer uuid
	 * @return the referUUID
	 */
	public String getReferUUID() {
		return referUUID;
	}

	/**
	 * This method set the refer uuid
	 * @param referUUID the referUUID to set
	 */
	public void setReferUUID(String referUUID) {
		this.referUUID = referUUID;
	}

	/**
	 * This method get the feedbackStatus
	 * @return the feedbackStatus
	 */
	public String getFeedbackStatus() {
		return feedbackStatus;
	}

	/**
	 * This method set the feedbackStatus
	 * @param feedbackStatus the feedbackStatus to set
	 */
	public void setFeedbackStatus(String feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}
	/**
	 * This method get the total score
	 * @return the totalScore
	 */
	public String getTotalScore() {
		return totalScore;
	}

	/**
	 * This method set the total score
	 * @param totalScore the totalScore to set
	 */
	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}


	/**
	 * This is the total score
	 */
	private String totalScore;

}
