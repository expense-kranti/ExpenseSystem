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
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the fullNameList
	 */
	public List<String> getFullNameList() {
		return fullNameList;
	}

	/**
	 * @param fullNameList
	 *            the fullNameList to set
	 */
	public void setFullNameList(List<String> fullNameList) {
		this.fullNameList = fullNameList;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		if (this.getMobileNumber() == null || this.getMobileNumber().isEmpty())
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
