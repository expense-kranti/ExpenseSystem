package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Date;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class holds the data regarding the details of credit enquiry done and is
 * present in Experian Report
 * 
 * @author urvij
 *
 */
public class CreditEnquiry extends BaseEntity implements Serializable {

	/**
	 * This is the date of birth of the cutomer
	 */
	private Date dob;
	/**
	 * This is the pan number
	 */
	private String panNumber;
	/**
	 * This is the passport number
	 */
	private String passport;
	/**
	 * This is the voter id
	 */
	private String voterId;
	/**
	 * This is the driving license number
	 */
	private String drivingLicense;
	/**
	 * This is the email
	 */
	private String emailId;
	/**
	 * This is the mobile number
	 */
	private String mobileNumber;
	/**
	 * This is the address
	 */
	private String address;
	/**
	 * This is the application date
	 */
	private Date applicationDate;
	/**
	 * This is the amount paid for
	 */
	private double amount;
	/**
	 * This is the Credit Institution Name
	 */
	private String creditInstitutionName;
	/**
	 * This is the Search Type
	 */
	private String searchType;
	/**
	 * This is the user phone number
	 */
	private String userMobileNumber;

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the creditInstitutionName
	 */
	public String getCreditInstitutionName() {
		return creditInstitutionName;
	}

	/**
	 * @param creditInstitutionName the creditInstitutionName to set
	 */
	public void setCreditInstitutionName(String creditInstitutionName) {
		this.creditInstitutionName = creditInstitutionName;
	}

	/**
	 * @return the searchType
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * @param searchType the searchType to set
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/**
	 * @return the userMobileNumber
	 */
	public String getUserMobileNumber() {
		return userMobileNumber;
	}

	/**
	 * @param userMobileNumber the userMobileNumber to set
	 */
	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}

	/**
	 * @return the applicationDate
	 */
	public Date getApplicationDate() {
		return applicationDate;
	}

	/**
	 * @param applicationDate
	 *            the applicationDate to set
	 */
	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	/**
	 * @return the dob
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * @return the panNumber
	 */
	public String getPanNumber() {
		return panNumber;
	}

	/**
	 * @param panNumber
	 *            the panNumber to set
	 */
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	/**
	 * @return the passport
	 */
	public String getPassport() {
		return passport;
	}

	/**
	 * @param passport
	 *            the passport to set
	 */
	public void setPassport(String passport) {
		this.passport = passport;
	}

	/**
	 * @return the voterId
	 */
	public String getVoterId() {
		return voterId;
	}

	/**
	 * @param voterId
	 *            the voterId to set
	 */
	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}

	/**
	 * @return the drivingLicense
	 */
	public String getDrivingLicense() {
		return drivingLicense;
	}

	/**
	 * @param drivingLicense
	 *            the drivingLicense to set
	 */
	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
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
