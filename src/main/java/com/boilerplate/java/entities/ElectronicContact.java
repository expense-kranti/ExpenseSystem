package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * 
 * @author love
 *
 */
public class ElectronicContact extends BaseEntity implements Serializable {
	/**
	 * This is the report tradeline email.
	 */
	private String email;
	/**
	 * This is the report tradeline telephone number.
	 */
	private String telephoneNumber;
	/**
	 *  This is the report tradeline mobile number.
	 */
	private String mobileNumber;
	/**
	 * This gets the report tradeline email.
	 * @return the email address
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * This sets the report tradeline email.
	 * @param email the report tradeline email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * This gets the report tradeline telephone number.
	 * @return the telephone number.
	 */
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	/**
	 * This sets the report tradeline telephone number.
	 * @param telephoneNumber the report tradeline telephone number
	 */
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	/**
	 * This gets the report tradeline mobile number.
	 * @return the mobile number.
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}
	/**
	 * This sets the report tradeline mobile number.
	 * @param mobileNumber the report tradeline mobile number
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}

	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	@Override
	public BaseEntity transformToExternal() {
		return this;
	}

}
