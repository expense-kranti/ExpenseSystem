package com.boilerplate.java.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import com.boilerplate.java.entities.ReportInputEntity;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class ExperianDataPublishEntity extends BaseEntity implements Serializable {

	/**
	 * This is the date format
	 */
	private static DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
	/**
	 * @see super.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		if(this.firstName == null) throw new ValidationFailedException("ReportInputEntity"
				, "First Name is null", null);
		
		if(this.surname == null) throw new ValidationFailedException("ReportInputEntity"
				, "Surame is null", null);
		
		if(this.dateOfBirth == null) throw new ValidationFailedException("ReportInputEntity"
				, "Date of Birth is null", null);
//		try {
//			this.dateOfBirthAsDate = dateFormat.parse(this.dateOfBirth);
//		} catch (ParseException ex) {
//			throw new ValidationFailedException("ReportInputEntity"
//				, ex.toString(), ex);
//		}
		
		if(this.gender == null) throw new ValidationFailedException("ReportInputEntity"
				, "Gender is null", null);
		
		if(this.addressLine1 == null) throw new ValidationFailedException("ReportInputEntity"
				, "Address Line 1 is null", null);
		if(this.city == null) throw new ValidationFailedException("ReportInputEntity"
				, "City is null", null);
		if(this.stateId == null) throw new ValidationFailedException("ReportInputEntity"
				, "State Id is null", null);
		if(this.pinCode == null) throw new ValidationFailedException("ReportInputEntity"
				, "Pin code is null", null);
		if(this.email == null) throw new ValidationFailedException("ReportInputEntity"
				, "Email is null", null);
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		if(pattern.matcher(this.email).matches() == false){
			 throw new ValidationFailedException("ReportInputEntity"
						, "Email is not in the proper email format", null);
		}
//		
//		if(
//			(this.panNumber == null) 			&&
//			(this.passportNumber == null) 		&&
//			(this.voterIdNumber == null)		&&
//			(this.universalIdNumber == null)	&&
//			(this.driverLicenseNumber == null)
//		  ){
//			throw new ValidationFailedException("ReportInputEntity"
//					, "One of Pan number, Passport number, voter Id number or driver license number is a required field", null);
//		}
		return true;
	}

	/**
	 * @see super.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see super.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}	
	
	/**
	 * This is the first name of the customer. This is a mandatory field
	 */
	@ApiModelProperty(value="This is the first name of the user and is a mandatory field")
	private String firstName;
	
	/**
	 * Gets the first name
	 * @return The first name of the user
	 */ 
	public String getFirstName() {
		return firstName;
	}

	/**
	 * This sets the first name of the user
	 * @param firstName The first name of the user
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * This is the middle name
	 */
	@ApiModelProperty(value="This is the middle name of the user")
	private String middleName;

	/**
	 * Gets the middle name
	 * @return The middle name
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * Sets the middle name
	 * @param middleName The middle name
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * The Surname of the user
	 */
	@ApiModelProperty(value="This is the surname of the user")
	private String surname;
	
	/**
	 * Gets the surname of the person
	 * @return The persons surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Sets the surname of the person
	 * @param surname The surname
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * This is the date of birth of the user
	 */
	private String dateOfBirth;
	
	/**
	 * This gets the date of birth
	 * @return The date of birth
	 */
	@ApiModelProperty(value="This is the date of birth of the user in the dd-mmm-yyyy format for example 22-Jan-2012")
	public String getDateOfBirth() {
//		return dateFormat.format(dateOfBirthAsDate);
		return dateOfBirth;
	}

	/**
	 * This sets the date of birth
	 * @param dateOfBirth The date of birth
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		
	}
	
//	@JsonIgnore
//	private java.util.Date dateOfBirthAsDate;
//	
//	/**
//	 * Gets the date of birth in date formar
//	 * @return The date of birth
//	 */
//	public java.util.Date getDateOfBirthAsDate(){
//		return this.dateOfBirthAsDate;
//	}
//	
//	/**
//	 * Sets the date of birth in date format
//	 * @param date The date of birth
//	 */
//	public void set(java.util.Date date){
//		this.dateOfBirthAsDate = date;
//	}
	

	/**
	 * This is the gender of the person
	 */
	@ApiModelProperty(value="This is the gender of the person")
	private String gender;
	
	/**
	 * Gets the gender of the person
	 * @return The gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Sets the gender of the person
	 * @param gender The gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
		this.genderEnum = Gender.valueOf(gender);
	}
	
	/**
	 * This is the gender enum
	 */
	@JsonIgnore
	private Gender genderEnum;

	/**
	 * Gets the gender enum
	 * @return The gender enum
	 */
	public Gender getGenderEnum() {
		return genderEnum;
	}

	/**
	 * Sets the gender enum
	 * @param genderEnum The gender enum
	 */
	public void setGenderEnum(Gender genderEnum) {
		this.genderEnum = genderEnum;
	}
	
	/**
	 * The mobile number
	 */
	@ApiModelProperty(value="This is the mobile number")
	private String mobileNumber;
	
	/**
	 * This is the mobile name
	 * @return The mobile number
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * This sets the mobile number
	 * @param mobileNumber The mobile number
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * This is the first line of address
	 */
	@ApiModelProperty(value="This is the address line 1")
	private String addressLine1;

	/**
	 * Gets the address line 1
	 * @return The address line 1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * Sets the address line
	 * @param addressLine1 The address line
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	/**
	 * This is the address line 2
	 */
	@ApiModelProperty(value="This is the address line 2")
	private String addressLine2;
	
	/**
	 * This gets address line 2
	 * @return The address line 2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * This sets address line 2
	 * @param addressLine2 The address line 2
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	
	/**
	 * This is the city
	 */
	@ApiModelProperty(value="This is the city")
	private String city;
	
	/** 
	 * Gets the city
	 * @return The city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city
	 * @param city The city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * This gets the state id which is a number
	 * @return The id of the state
	 */
	public String getStateId() {
		return stateId;
	}

	/**
	 * Sets the id of the state
	 * @param stateId The state id
	 */
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	
	/**
	 * The state id
	 */
	@ApiModelProperty(value="This is the state id")
	private String stateId;
	
	/**
	 * This is the pin code
	 */
	@ApiModelProperty(value="This is the pin code")
	private String pinCode;
	
	/**
	 * Gets the pin code
	 * @return The pin code
	 */
	public String getPinCode() {
		return pinCode;
	}

	/**
	 * Sets the pin code
	 * @param pinCode The pin code
	 */
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	
	/**
	 * This is the pan number
	 */
	@ApiModelProperty(value="This is the pin code")
	private String panNumber;
	
	/**
	 * This gets the pan number
	 * @return This is the pan number
	 */
	public String getPanNumber() {
		return panNumber;
	}

	/**
	 * Sets the pan number
	 * @param panNumber The pan number
	 */
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	
	/**
	 * This is the passport number
	 */
	@ApiModelProperty(value="This is the passport number")
	private String passportNumber;
	
	/**
	 * Gets the passport number
	 * @return The passport number
	 */
	public String getPassportNumber() {
		return passportNumber;
	}

	/**
	 * Sets the passport number
	 * @param passportNumber The passport number
	 */
	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}
	
	/**
	 * This is the voter id number
	 */
	@ApiModelProperty(value="This is the voter id")
	private String voterIdNumber;
	
	/**
	 * Gets the voter id number
	 * @return The voter id number
	 */
	public String getVoterIdNumber() {
		return voterIdNumber;
	}

	/**
	 * Sets the voter id number
	 * @param voterIdNumber The voter id number
	 */
	public void setVoterIdNumber(String voterIdNumber) {
		this.voterIdNumber = voterIdNumber;
	}

	/**
	 * This is the universal id number
	 */
	@ApiModelProperty(value="This is the universal id number")
	private String universalIdNumber;
	
	/**
	 * Gets the universal id number
	 * @return The universal id number
	 */
	public String getUniversalIdNumber() {
		return universalIdNumber;
	}

	/**
	 * This sets universal id number
	 * @param universalIdNumber The universal id number
	 */
	public void setUniversalIdNumber(String universalIdNumber) {
		this.universalIdNumber = universalIdNumber;
	}

	/**
	 * This is the diver license number
	 */
	@ApiModelProperty(value="This is the driver license number")
	private String driverLicenseNumber;
	
	/**
	 * This gets the driver license number
	 * @return The driver license number
	 */
	public String getDriverLicenseNumber() {
		return driverLicenseNumber;
	}

	/**
	 * This sets the driver license number
	 * @param driverLicenseNumber The driver license number
	 */
	public void setDriverLicenseNumber(String driverLicenseNumber) {
		this.driverLicenseNumber = driverLicenseNumber;
	}
	
	/**
	 * This is the telephone number
	 */
	@ApiModelProperty(value="This is the telephone number")
	private String telephoneNumber;

	/**
	 * Sets the telephone number
	 * @return The telephone number
	 */
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	/**
	 * This sets the telephone number
	 * @param telephoneNumber The telephone number
	 */
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	
	/**
	 * This is the type id for the telephone number
	 */
	@ApiModelProperty(value="This is the telephone type")
	private String telephoneTypeId;	
	
	/**
	 * This gets telephone type id
	 * @return The telephone id
	 */
	public String getTelephoneTypeId() {
		return telephoneTypeId;
	}

	/**
	 * This sets telephone id
	 * @param telephoneTypeId The id
	 */
	public void setTelephoneTypeId(String telephoneTypeId) {
		this.telephoneTypeId = telephoneTypeId;
	}
	


//	/**
//	 * Sets the date of birth as date
//	 * @param dateOfBirthAsDate The date of birth
//	 */
//	public void setDateOfBirthAsDate(java.util.Date dateOfBirthAsDate) {
//		this.dateOfBirthAsDate = dateOfBirthAsDate;
//	}

	/**
	 * Gets the email
	 * @return The email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email
	 * @param email The email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public State getStateEnum() {
		return stateEnum;
	}

	public void setStateEnum(State stateEnum) {
		this.stateEnum = stateEnum;
	}

	@JsonIgnore
	/**
	 * This is the state of application
	 */
	public String stateOfTransaction;
	
	
	
	/**
	 * Gets the state of transaction
	 * @return
	 */
	public String getStateOfTransaction(){
		return this.stateEnum.toString();
	}

	
	/**
	 * The email of the user
	 */
	@ApiModelProperty(value="This is the email of the user")
	private String email;

	/**
	 * This is the stage 1 id
	 */
	@JsonIgnore
	private String stage1Id;
	


	/**
	 * This method Gets stage 1 id
	 * @return The stage 1 id
	 */
	public String getStage1Id() {
		return stage1Id;
	}

	/**
	 * This method sets the stage 1 id
	 * @param stage1Id This is the stage 1 id
	 */
	public void setStage1Id(String stage1Id) {
		this.stage1Id = stage1Id;
	}
	
	/**
	 * The stage 2 id
	 */
	@JsonIgnore
	private String stage2Id;
	
	/**
	 * This method Gets the stage 2 id
	 * @return The id
	 */
	public String getStage2Id() {
		return stage2Id;
	}

	/**
	 * This method sets the stage 2 id 
	 * @param stage2Id The stage 2 id
	 */
	public void setStage2Id(String stage2Id) {
		this.stage2Id = stage2Id;
	}
	
	/**
	 * This is the experian status
	 */
	@JsonIgnore
	private String experianStatus;
	
	/**
	 * This method gets the experian status
	 * @return The experianStatus
	 */
	public String getExperianStatus() {
		return experianStatus;
	}

	/**
	 * This method sets the experian status
	 * @param experianStatus The experianStatus
	 */
	public void setExperianStatus(String experianStatus) {
		this.experianStatus = experianStatus;
	}
	public enum State{
		Empty,
		SessionSetup,
		Question,
		Report,
		CreditReportEmpty,
		IncorrectAnswerGiven,
		InsufficientQuestions
	}
	
	/**
	 * This is the experian Attempt Date
	 */
	private String experianAttemptDate;
	/**
	 * This is the state of application
	 */
//	@JsonIgnore
	private State stateEnum;
	
	public ExperianDataPublishEntity getExperianDataByReportInputEntity(ReportInputEntity ReportInputEntity){
	this.setAddressLine1(ReportInputEntity.getAddressLine1());
	this.setAddressLine2(ReportInputEntity.getAddressLine2());
	this.setCity(ReportInputEntity.getCity());
	this.setDateOfBirth(ReportInputEntity.getDateOfBirth());
	this.setDriverLicenseNumber(ReportInputEntity.getDriverLicenseNumber());
	this.setEmail(ReportInputEntity.getEmail());
	this.setFirstName(ReportInputEntity.getFirstName());
	this.setMiddleName(ReportInputEntity.getMiddleName());
	this.setMobileNumber(ReportInputEntity.getMobileNumber());
	this.setPinCode(ReportInputEntity.getPinCode());
	this.setPanNumber(ReportInputEntity.getPanNumber());
	this.setPassportNumber(ReportInputEntity.getPassportNumber());
	this.setGender(ReportInputEntity.getGender());
	this.setGenderEnum(ReportInputEntity.getGenderEnum());
	this.setSurname(ReportInputEntity.getSurname());
	this.setStateId(ReportInputEntity.getStateId());
	this.setStateEnum(ReportInputEntity.getStateEnum());
	this.setTelephoneTypeId(ReportInputEntity.getTelephoneTypeId());
	this.setTelephoneNumber(ReportInputEntity.getTelephoneNumber());
	this.setVoterIdNumber(ReportInputEntity.getVoterIdNumber());
	this.setUniversalIdNumber(ReportInputEntity.getUniversalIdNumber());
	this.setStage1Id(ReportInputEntity.getStage1Id());
	this.setExperianStatus(ReportInputEntity.getExperianStatus());
	this.setExperianAttemptDate(ReportInputEntity.getExperianAttemptDate());
	return this;
}

/**
 * This method gets the experian attempt date
 * @return The experianAttemptDate
 */
public String getExperianAttemptDate() {
	return experianAttemptDate;
}

/**
 * This method sets the experian attempt date
 * @param experianAttemptDate The experianAttemptDate
 */
public void setExperianAttemptDate(String experianAttemptDate) {
	this.experianAttemptDate = experianAttemptDate;
}

}
