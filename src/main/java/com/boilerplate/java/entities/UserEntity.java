package com.boilerplate.java.entities;

import javax.xml.bind.ValidationException;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This is the User entity
 * 
 * @author ruchi
 *
 */
@ApiModel(value = "A User", description = "This is a user", parent = BaseEntity.class)
public class UserEntity extends BaseEntity {

	/**
	 * This is the first name of the user
	 */
	private String firstName;

	/**
	 * This is the last name of the user
	 */
	private String lastName;

	/**
	 * This is the password of the user
	 */
	private String password;

	/**
	 * This is the email id of the user
	 */
	private String emailId;

	/**
	 * This is the mobile number of the user
	 */
	private String mobile;

	/**
	 * This is the middlename of the user
	 */
	private String middleName;

	/**
	 * This is the user id
	 */
	private String userId;

	/**
	 * This is the role of the user
	 */
	private String role;

	/**
	 * this means the user is active or disabled by the admin
	 */
	private boolean isActive;

	/**
	 * Thjis method is used to get middle name of the user
	 * 
	 * @return
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * This method is used to set middle name of the user
	 * 
	 * @param middleName
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * this method is used to get user id
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * This method is used to set user id
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This method is used to get role
	 * 
	 * @return
	 */
	public String getRole() {
		return role;
	}

	/**
	 * This method is used to set role
	 * 
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * This method is used to get isActive status
	 * 
	 * @return
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * This method is used to set isActive status
	 * 
	 * @param isActive
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * This method is used to get the first name of the user
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * This method is used to set the first name
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * This method is used to set the last name
	 * 
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * This method is used to set the last name
	 * 
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * This method is used to get the password
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This method is used to set the password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * This method is used to get the email id
	 * 
	 * @return
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * This method is used to set the email id
	 * 
	 * @param emailId
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * This method is used to get the mobile number
	 * 
	 * @return
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * This method is used to set the mobile number
	 * 
	 * @param mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		// check whether first name, last name, email id, userId, password are
		// not null or empty
		if (this.getFirstName() == null || this.getLastName() == null || this.getUserId() == null
				|| this.getPassword() == null || this.getEmailId() == null || this.getMobile() == null)
			throw new ValidationFailedException("UserEntity",
					"First name, Last name, Password, EmailId, Mobile Number and User Id are mandatory", null);
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
