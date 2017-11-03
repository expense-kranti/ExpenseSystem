package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Map;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateMap;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

}
