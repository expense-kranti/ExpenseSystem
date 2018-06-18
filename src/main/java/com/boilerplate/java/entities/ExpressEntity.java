package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This entity is used to represent the user express registration details
 * 
 * @author urvij
 *
 */
public class ExpressEntity extends BaseEntity implements Serializable {

	/**
	 * This is the user fullname
	 */
	private String fullName;
	/**
	 * This is the list of user full name
	 */
	private List<String> fullNameList;
	/**
	 * This is the mobileNumber
	 */
	private String mobileNumber;

	/**
	 * This method is used to get the full name
	 * 
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * This method is used to set the full name
	 * 
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * This method is use to get the list of full name
	 * 
	 * @return the fullNameList
	 */
	public List<String> getFullNameList() {
		return fullNameList;
	}

	/**
	 * This method is used to set the list of full name
	 * 
	 * @param fullNameList
	 *            the fullNameList to set
	 */
	public void setFullNameList(List<String> fullNameList) {
		this.fullNameList = fullNameList;
	}

	/**
	 * This method is used to get the mobile number
	 * 
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * This method is used to set the mobile number
	 * 
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		if (BaseEntity.isNullOrEmpty(this.getMobileNumber()))
			throw new ValidationFailedException("ExpressEntity", "mobile number can not be null or empty", null);
		return true;

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
