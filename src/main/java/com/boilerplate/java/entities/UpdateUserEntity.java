package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Encryption;
import com.boilerplate.java.collections.BoilerplateMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="A User", description="This is a user entity for update", parent=BaseEntity.class)
public class UpdateUserEntity extends BaseEntity{
	@ApiModelProperty(value="This contains the list of properties to extend the user model"
			,required=true,notes="The keys should be unique in this system.")
	/**
	 * This is the dictionary of the user meta data.
	 */
	private BoilerplateMap<String,String> userMetaData = new BoilerplateMap<String, String>(); 
	/**
	 * This method returns the user meta data
	 * @return
	 */
	public BoilerplateMap<String, String> getUserMetaData() {
		return userMetaData;
	}

	public void setUserMetaData(BoilerplateMap<String, String> userMetaData) {
		this.userMetaData = userMetaData;
	}

	@JsonIgnore
	/**
	 * This is the password.
	 */
	private String password;
	/**
	 * This method gets the password
	 * @return The password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This method sets the password
	 * @param password The password
	 */
	public void setPassword(String password){
		this.password= password;
	}
	
	/**
	 * This method hash's the password
	 */
	public void hashPassword(){
		this.password = String.valueOf(Encryption.getHashCode(this.password));
	}

	/**
	 * This method checks if the password is null or empty. Null or empty
	 * passwords are not allowed in the system.
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		if(this.getPassword() ==null) throw new ValidationFailedException(
				"User","Password is null/Empty",null);
		if(this.getPassword().equals("")) throw new ValidationFailedException(
				"User","Password is null/Empty",null);
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
	 * @return The status of user
	 */
	public int getUserStatus() {
		return userStatus;
	}

	/**
	 * This sets the status of the user
	 * @param userStatus
	 */
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	/**
	 * Gets user key
	 * @return The key
	 */
	public String getUserKey() {
		return userKey;
	}

	/**
	 * Sets the user key
	 * @param userKey The key
	 */
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	/**
	 * This is the status of user
	 * 0 - disabled
	 * 1 - enabled
	 * This will be eventually set into an enum, however for now we are not sure
	 * how this enum will span out.
	 * Further as this stands we think this will not be used by end user.
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
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * This method set the date of birth
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * This method get the alternate number
	 * @return the alternateNumber
	 */
	public String getAlternateNumber() {
		return alternateNumber;
	}

	/**
	 * This method set the alternate number
	 * @param alternateNumber the alternateNumber to set
	 */
	public void setAlternateNumber(String alternateNumber) {
		this.alternateNumber = alternateNumber;
	}

	/**
	 * This method get the employment status
	 * @return the employmentStatus
	 */
	public EmploymentStatus getEmploymentStatus() {
		return employmentStatus;
	}

	/**
	 * This method set the employment status
	 * @param employmentStatus the employmentStatus to set
	 */
	public void setEmploymentStatus(EmploymentStatus employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
	
	

	

	



	
}


