package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This is the user entity expected as an input.
 * 
 * @author gaurav.verma.icloud
 *
 */
@ApiModel(value = "A User", description = "This is a user", parent = BaseEntity.class)
public class ExternalFacingUser extends BaseEntity implements Serializable {

	/**
	 * This is the default constructor
	 */
	public ExternalFacingUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This is the parameterized constructor
	 * 
	 * @param userId
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param roles
	 * @param isActive
	 * @param password
	 * @param approverId
	 * @param superApproverId
	 * @param financeId
	 * @param authenticationProvider
	 */
	public ExternalFacingUser(String userId, String email, String firstName, String lastName,
			List<UserRoleEntity> roles, boolean isActive, String approverId, String authenticationProvider) {
		super();
		this.userId = userId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roles = roles;
		this.isActive = isActive;
		this.approverId = approverId;
		this.authenticationProvider = authenticationProvider;
	}

	/**
	 * This is the user's Id, this is not the system generated Id, it is the id
	 * created by the user.
	 */
	@ApiModelProperty(value = "This is the id of the user", required = true, notes = "This is the id of the user")
	private String userId;

	/**
	 * This is the email of the user
	 */
	@ApiModelProperty(value = "This is the email of the user", required = true, notes = "This is the email of the user")
	public String email;

	/**
	 * This is the first name of the user
	 */
	@ApiModelProperty(value = "This is the firstName of the user", required = true, notes = "This is the firstName of the user")
	private String firstName;

	/**
	 * This is the last name of the user
	 */
	@ApiModelProperty(value = "This is the lastName of the user", required = true, notes = "This is the lastName of the user")
	private String lastName;

	/**
	 * The roles of the user
	 */
	@ApiModelProperty(value = "This is the list of roles of the user", required = true, notes = "This is the list of roles of the user")
	private List<UserRoleEntity> roles;

	/**
	 * This indicates whether user is disabled by admin or not
	 */
	@ApiModelProperty(value = "This is the active status of the user", required = true, notes = "This is the active status of the user")
	private boolean isActive;

	/**
	 * This is the user id of the approver
	 */
	@ApiModelProperty(value = "This is the approverId of the user", required = true, notes = "This is the approverId of the user")
	private String approverId;

	/**
	 * This is the authentication provider. Default means that the user is
	 * authenticated by the user name and password. A user may use SSO and other
	 * authentication providers like facebook,\ google and others.
	 */
	@ApiModelProperty(value = "This is the authenticationProvider of the user", required = true, notes = "This is the authenticationProvider of the user")
	private String authenticationProvider;

	/**
	 * This is the list of roles set by getting roles from db for the user in
	 * question
	 */
	@ApiModelProperty(value = "This is the list fo role types", required = true, notes = "This is the list fo role types")
	private List<UserRoleType> roleTypes;

	/**
	 * This method is used to getRoles
	 * 
	 * @return
	 */
	public List<UserRoleEntity> getRoles() {
		return roles;
	}

	/**
	 * This method is used to set roles
	 * 
	 * @param roles
	 */
	public void setRoles(List<UserRoleEntity> roles) {
		this.roles = roles;
	}

	/**
	 * This method is used to get role types
	 * 
	 * @return
	 */
	public List<UserRoleType> getRoleTypes() {
		return roleTypes;
	}

	/**
	 * This method is used to set role types
	 * 
	 * @param roleTypes
	 */
	public void setRoleTypes(List<UserRoleType> roleTypes) {
		this.roleTypes = roleTypes;
	}

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
	 * this method is used to get authentication provider
	 * 
	 * @return
	 */
	public String getAuthenticationProvider() {
		return authenticationProvider;
	}

	/**
	 * This method is used to set authentication provider
	 * 
	 * @param authenticationProvider
	 */
	public void setAuthenticationProvider(String authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
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
		if (this.isNullOrEmpty(this.getFirstName()))
			throw new ValidationFailedException("User", "First Name is null/Empty", null);
		if (this.isNullOrEmpty(this.getLastName()))
			throw new ValidationFailedException("User", "Last Name is null/Empty", null);
		if (this.isNullOrEmpty(this.getUserId()))
			throw new ValidationFailedException("User", "User Id is null/Empty", null);
		if (this.isNullOrEmpty(this.getEmail()))
			throw new ValidationFailedException("User", "Email Id is null/Empty", null);
		// check email id format
		Matcher matcher = emailResxPattern.matcher(this.getEmail());
		if (matcher.matches() == false) {
			throw new ValidationFailedException("ExternalFacingUser", "Email format is incorrect", null);
		}
		// check if user id and email are same
		if (!this.getUserId().equals(this.getEmail()))
			throw new ValidationFailedException("ExternalFacingUser", "Email id and user id should be same", null);
		return true;
	}

}