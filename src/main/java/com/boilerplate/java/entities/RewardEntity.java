package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class represents the entity that contains details of the reward winning
 * user details like name, contact number(phone number), email id
 * 
 * @author urvij
 *
 */
public class RewardEntity extends BaseEntity implements Serializable {

	/**
	 * This is the email regex expression
	 */
	public static Pattern emailResxPattern = Pattern
			.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	/**
	 * This is the name of the reward winning user
	 */
	private String name;
	/**
	 * This is the phonenumber of the reward winning user
	 */
	private String phoneNumber;
	/**
	 * This is the email id of the reward winning user
	 */
	private String email;

	/**
	 * Gets the name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the phone number
	 * 
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets the phone number
	 * 
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Gets the email
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email
	 * 
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {

		if (this.isNullOrEmpty(this.getName())) {
			throw new ValidationFailedException("RewardEntity", "Name is null/Empty", null);
		}
		if (this.isNullOrEmpty(this.getEmail())) {
			throw new ValidationFailedException("RewardEntity", "Email is null/Empty", null);
		}
		Matcher matcher = emailResxPattern.matcher(this.getEmail());
		if (matcher.matches() == false) {
			throw new ValidationFailedException("User", "Email format is incorrect", null);
		}
		if (this.isNullOrEmpty(this.getPhoneNumber())) {
			throw new ValidationFailedException("RewardEntity", "PhoneNumber is null/Empty", null);
		}
		if (this.getPhoneNumber().contains(":"))
			throw new ValidationFailedException("User", "':' doesn't allows in Phone Number", null);
		return true;
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
