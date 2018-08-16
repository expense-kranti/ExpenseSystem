package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModel;

/**
 * This is the user entity expcted as an input.
 * 
 * @author gaurav.verma.icloud
 *
 */
@ApiModel(value = "A User", description = "This is a user", parent = UpdateUserEntity.class)
public class ExternalFacingUser extends BaseEntity implements Serializable {

	/**
	 * This is the user's Id, this is not the system generated Id, it is the id
	 * created by the user.
	 */
	private String userId;

	/**
	 * This is the middle name of the user
	 */
	private String middleName;

	/**
	 * This is the email of the user
	 */
	public String email;

	/**
	 * This is the first name of the user
	 */
	private String firstName;

	/**
	 * This is the last name of the user
	 */
	private String lastName;

	/**
	 * This is the role name of user
	 */
	private String roleName;
	/**
	 * The roles of the user
	 */
	private List<Role> roles;

	/**
	 * This is the phone number of the customer or user
	 */
	private String phoneNumber;

	/**
	 * This indicates whether user is disabled by admin or not
	 */
	private boolean isActive;

	/**
	 * This is the password of the user
	 */
	private String password;

	/**
	 * This is the user id of the approver
	 */
	private String approverId;

	/**
	 * This is the user id of the super approver
	 */
	private String superApproverId;

	/**
	 * This method gets the email of the user
	 * 
	 * @return The email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * This method sets the email of the user
	 * 
	 * @param email
	 *            The email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * This method gets the first name of the user
	 * 
	 * @return The first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * This method sets the first name of the user
	 * 
	 * @param firstName
	 *            The first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * This method gets the last name of the user
	 * 
	 * @return The last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * This method sets the last name of the user
	 * 
	 * @param lastName
	 *            The last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * This method gets the phone number
	 * 
	 * @return The phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * This method sets the phone number of the user
	 * 
	 * @param phoneNumber
	 *            The phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * This method gets the middle name
	 * 
	 * @return The middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * This method sets the middle name
	 * 
	 * @param middleName
	 *            The middleName
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Gets the role name
	 * 
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * Sets the role name
	 * 
	 * @param roleName
	 *            the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * Gets the roles of the user
	 * 
	 * @return The roles of the user
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * Sets the roles of the user
	 * 
	 * @param roles
	 *            The roles of the user
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * Gets the user Id
	 * 
	 * @return The user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id
	 * 
	 * @param userId
	 *            The user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This method is used to get active status
	 * 
	 * @return
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * This method is used to set is active status
	 * 
	 * @param isActive
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * This method is used to get password
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This method is used to set password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * This method is used to get approver id
	 * 
	 * @return
	 */
	public String getApproverId() {
		return approverId;
	}

	/**
	 * This method is used to set approver id
	 * 
	 * @param approverId
	 */
	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	/**
	 * This method is used to get super approver id
	 * 
	 * @return
	 */
	public String getSuperApproverId() {
		return superApproverId;
	}

	/**
	 * This method is used to set super approver id
	 * 
	 * @param superApproverId
	 */
	public void setSuperApproverId(String superApproverId) {
		this.superApproverId = superApproverId;
	}

	/**
	 * This is the email regex expression
	 */
	public static Pattern emailResxPattern = Pattern
			.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

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
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// check whether first name, last name, email id, userId, password are
		// not null or empty
		if (this.getFirstName() == null || this.getLastName() == null || this.getUserId() == null
				|| this.getPassword() == null || this.getEmail() == null || this.getPhoneNumber() == null)
			throw new ValidationFailedException("UserEntity",
					"First name, Last name, Password, EmailId, Mobile Number and User Id are mandatory", null);
		return true;
	}

}