package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Encryption;
import com.boilerplate.java.collections.BoilerplateMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "A User", description = "This is a user entity for update", parent = BaseEntity.class)
public class UpdateUserEntity extends BaseEntity {
	@ApiModelProperty(value = "This contains the list of properties to extend the user model", required = true, notes = "The keys should be unique in this system.")
	/**
	 * This is the dictionary of the user meta data.
	 */
	private BoilerplateMap<String, String> userMetaData = new BoilerplateMap<String, String>();

	/**
	 * This method returns the user meta data
	 * 
	 * @return
	 */
	public BoilerplateMap<String, String> getUserMetaData() {
		return userMetaData;
	}

	public void setUserMetaData(BoilerplateMap<String, String> userMetaData) {
		this.userMetaData = userMetaData;
	}

	public String getCrmid() {
		return crmid;
	}

	public void setCrmid(String crmid) {
		this.crmid = crmid;
	}

	private String crmid;
	@JsonIgnore
	/**
	 * This is the password.
	 */
	private String password;

	/**
	 * This method gets the password
	 * 
	 * @return The password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This method sets the password
	 * 
	 * @param password
	 *            The password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * This method hash's the password
	 */
	public void hashPassword() {
		this.password = String.valueOf(Encryption.getHashCode(this.password));
	}

	/**
	 * This method checks if the password is null or empty. Null or empty
	 * passwords are not allowed in the system.
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		if (this.isNullOrEmpty(this.getPassword()))
			throw new ValidationFailedException("User", "Password is null/Empty", null);

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
	 * This gets the use status
	 * 
	 * @return The status of user
	 */
	public int getUserStatus() {
		return userStatus;
	}

	/**
	 * This sets the status of the user
	 * 
	 * @param userStatus
	 */
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	/**
	 * Gets user key
	 * 
	 * @return The key
	 */
	public String getUserKey() {
		return userKey;
	}

	/**
	 * Sets the user key
	 * 
	 * @param userKey
	 *            The key
	 */
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	/**
	 * This is the status of user 0 - disabled 1 - enabled This will be
	 * eventually set into an enum, however for now we are not sure how this
	 * enum will span out. Further as this stands we think this will not be used
	 * by end user.
	 */
	@JsonIgnore
	private int userStatus = 1;

	/**
	 * The user key for email authorization
	 */
	@JsonIgnore
	private String userKey;
	/**
	 * This is the user date of birth
	 */
	private String dateOfBirth;
	/**
	 * This is the user alternate number
	 */
	private String alternateNumber;
	/**
	 * This is the user employment status
	 */
	private EmploymentStatus employmentStatus;

	/**
	 * This method get the date of birth
	 * 
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * This method set the date of birth
	 * 
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * This method get the alternate number
	 * 
	 * @return the alternateNumber
	 */
	public String getAlternateNumber() {
		return alternateNumber;
	}

	/**
	 * This method set the alternate number
	 * 
	 * @param alternateNumber
	 *            the alternateNumber to set
	 */
	public void setAlternateNumber(String alternateNumber) {
		this.alternateNumber = alternateNumber;
	}

	/**
	 * This method get the employment status
	 * 
	 * @return the employmentStatus
	 */
	public EmploymentStatus getEmploymentStatus() {
		return employmentStatus;
	}

	/**
	 * This method set the employment status
	 * 
	 * @param employmentStatus
	 *            the employmentStatus to set
	 */
	public void setEmploymentStatus(EmploymentStatus employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	/**
	 * This method get the location
	 * 
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * This method set the location
	 * 
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * This is the location
	 */
	private String location;

	/**
	 * This is a flag is user reset its password or not
	 */
	private boolean isPasswordChanged;

	/**
	 * This method is used to get the status of user password changed.
	 * 
	 * @return the isPasswordChanged
	 */
	public boolean getIsPasswordChanged() {
		return isPasswordChanged;
	}

	/**
	 * This method is used to set the status of is user password changed.
	 * 
	 * @param isPasswordChanged
	 *            the isPasswordChanged to set
	 */
	public void setIsPasswordChanged(boolean isPasswordChanged) {
		this.isPasswordChanged = isPasswordChanged;
	}

	/**
	 * This method gets the state of the User.
	 * 
	 * @return userState The User State
	 */
	public MethodState getUserState() {
		return userState;
	}

	/**
	 * This method sets the state of the User.
	 * 
	 * @param methodState
	 *            The User State
	 */
	public void setUserState(MethodState methodState) {
		this.userState = methodState;
	}

	/**
	 * Gets the first name
	 * 
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name
	 * 
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name
	 * 
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name
	 * 
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * Gets the hasSkippedEmailInput
	 * 
	 * @return the hasSkippedEmailInput
	 */
	public boolean getHasSkippedEmailInput() {
		return hasSkippedEmailInput;
	}

	/**
	 * Sets the hasSkippedEmailInput
	 * 
	 * @param hasSkippedEmailInput
	 *            the hasSkippedEmailInput to set
	 */
	public void setHasSkippedEmailInput(boolean hasSkippedEmailInput) {
		this.hasSkippedEmailInput = hasSkippedEmailInput;
	}

	/**
	 * This is state of user which tell us about milestones covered by the user.
	 */
	@ApiModelProperty(value = "This is state of the user", required = false)
	private MethodState userState;

	/**
	 * This is the first name of user to be updated later
	 */
	// this is made for new requirement to let user register only with phone
	// number with dummy first name for landing page(credit worthiness)
	// requirement and then update user name later
	private String firstName;
	/**
	 * This is the last name to be updated
	 */
	private String lastName;
	/**
	 * This is the emailId to be updated
	 */
	private String email;
	/**
	 * This is the phonenumber of user
	 */
	private String phoneNumber;

	// this is used to check if user is not interested for input its emailid and
	// will not be shown popup for entering this again
	/**
	 * This is the has skipped email input that tells that user is not
	 * interested in inputing his/her emailid
	 */
	private boolean hasSkippedEmailInput;

}
