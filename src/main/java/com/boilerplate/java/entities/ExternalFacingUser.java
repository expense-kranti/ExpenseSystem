package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Encryption;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This is the user entity expcted as an input.
 * 
 * @author gaurav.verma.icloud
 *
 */
@ApiModel(value = "A User", description = "This is a user", parent = UpdateUserEntity.class)
public class ExternalFacingUser extends UpdateUserEntity implements Serializable {

	@ApiModelProperty(value = "This is the id of the user.", required = true, notes = "The id of the user is unique in the system, it is analogous to user name")
	/**
	 * This is the user's Id, this is not the system generated Id, it is the id
	 * created by the user.
	 */
	private String userId;

	@ApiModelProperty(value = "This is the authenication provider of the user. This value is set to Default if not specified", required = true, notes = "The legal values include Default")
	/**
	 * This is the authentication provider. Default means that the user is
	 * authenticated by the user name and password. A user may use SSO and other
	 * authentication providers like facebook,\ google and others.
	 */
	private String authenticationProvider;

	@ApiModelProperty(value = "This is the id as in external system, "
			+ "if the provider is Default then id and external system id are same", required = false)
	/**
	 * This is the id of the user in external system, it is defaulted to the id
	 * if the authentication provider is Default
	 */
	private String externalSystemId;

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
		this.userId = userId.toUpperCase();
	}

	/**
	 * This returns the autheinctaion provider.
	 * 
	 * @return The authentication provider.
	 */
	public String getAuthenticationProvider() {
		return authenticationProvider;
	}

	/**
	 * This sets the authentication provider
	 * 
	 * @param The
	 *            authenticationProvider
	 */
	public void setAuthenticationProvider(String authenticationProvider) {
		this.authenticationProvider = authenticationProvider.toUpperCase();
	}

	/**
	 * returns the external system id
	 * 
	 * @return The external system id
	 */
	public String getExternalSystemId() {
		return externalSystemId;
	}

	/**
	 * Theis sets the external system id
	 * 
	 * @param externalSystemId
	 */
	public void setExternalSystemId(String externalSystemId) {
		this.externalSystemId = externalSystemId;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		super.validate();
		// The idea is that user name or password should not be null
		if (this.isNullOrEmpty(this.getUserId()))
			throw new ValidationFailedException("User", "UserId is null/Empty", null);
		if (this.getUserId() == null)
			throw new ValidationFailedException("User", "UserId is null/Empty", null);
		if (this.isNullOrEmpty(this.getFirstName()))
			throw new ValidationFailedException("User", "First Name is null/Empty", null);
		// if (this.isNullOrEmpty(this.getLastName()))
		// throw new ValidationFailedException("User", "Last Name is
		// null/Empty", null);
		if (!this.isNullOrEmpty(this.getEmail())) {
			if (this.getEmail() == null) {
				throw new ValidationFailedException("User", "Email is null/Empty", null);
			}
			Matcher matcher = emailResxPattern.matcher(this.getEmail());
			if (matcher.matches() == false) {
				throw new ValidationFailedException("User", "Email format is incorrect", null);
			}
		}
		if (this.isNullOrEmpty(this.getPhoneNumber()))
			throw new ValidationFailedException("User", "Phone Number null/Empty", null);
		if (this.getPhoneNumber().contains(":"))
			throw new ValidationFailedException("User", "':' doesn't allows in Phone Number", null);
		return true;
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
	 * This is the email of the user
	 */
	@ApiModelProperty(value = "This is the email of the user", required = true)
	public String email;

	/**
	 * This is the first name of the user
	 */
	@ApiModelProperty(value = "This is the first name of the user", required = true)
	private String firstName;

	/**
	 * This is the last name of the user
	 */
	@ApiModelProperty(value = "This is the last name of the user", required = true)
	private String lastName;

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
	 * This methodgets the first name of the user
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
	 * This is the phone number of the customer or user
	 */
	@ApiModelProperty(value = "This is the phone number of the user", required = true)
	private String phoneNumber;

	/**
	 * This is the referalSource use for providing referal
	 */
	@ApiModelProperty(value = "The referal source", required = false)
	private String referalSource;

	/**
	 * THis method gets the referalSource
	 * 
	 * @return The referalSource
	 */
	public String getReferalSource() {
		return referalSource;
	}

	/**
	 * This method sets the referalSource
	 * 
	 * @param referalSource
	 *            The referalSource
	 */
	public void setReferalSource(String referalSource) {
		this.referalSource = referalSource;
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
	 * This method gets the dsaId
	 * 
	 * @return The dsaId
	 */
	public String getDsaId() {
		return dsaId;
	}

	/**
	 * This method sets the dsaId
	 * 
	 * @param dsaId
	 *            The dsaId
	 */
	public void setDsaId(String dsaId) {
		this.dsaId = dsaId;
	}

	@ApiModelProperty(value = "This is location of user", required = false)

	/**
	 * This method gets the experianRequestUniqueKey
	 * 
	 * @return The experianRequestUniqueKey
	 */
	public String getExperianRequestUniqueKey() {
		return experianRequestUniqueKey;
	}

	/**
	 * This method sets the experianRequestUniqueKey
	 * 
	 * @param experianRequestUniqueKey
	 *            The experianRequestUniqueKey
	 */
	public void setExperianRequestUniqueKey(String experianRequestUniqueKey) {
		this.experianRequestUniqueKey = experianRequestUniqueKey;
	}

	/**
	 * This is the middleName of the customer/user
	 */
	private String middleName;

	/**
	 * This is the dsaId of DSA
	 */
	@ApiModelProperty(value = "This is dsaId", required = false)
	private String dsaId;

	/**
	 * This is the otpList
	 */

	private BoilerplateList<Integer> otpList;

	/**
	 * This method gets the otpList
	 * 
	 * @return The otpList
	 */
	public BoilerplateList<Integer> getOtpList() {
		return otpList;
	}

	/**
	 * This method sets the otpList
	 * 
	 * @param otpList
	 *            The otpList
	 */
	public void setOtpList(BoilerplateList<Integer> otpList) {
		this.otpList = otpList;
	}

	/*
	 * The email used to communicate with experian
	 */
	@ApiModelProperty(value = "The email used to communicate with experian", required = true)
	private String experianRequestUniqueKey;

	/*
	 * The isDefaultPassword boolean used to set default password corresponding
	 * to user
	 */
	@ApiModelProperty(value = "The email used to communicate with experian", required = false)
	private boolean isDefaultPassword;

	/**
	 * This method gets the isDefaultPassword boolean value
	 * 
	 * @return The isDefaultPassword
	 */
	public boolean isDefaultPassword() {
		return isDefaultPassword;
	}

	/**
	 * This method sets the isDefaultPassword boolean
	 * 
	 * @param isDefaultPassword
	 *            The isDefaultPassword
	 */
	public void setDefaultPassword(boolean isDefaultPassword) {
		this.isDefaultPassword = isDefaultPassword;
	}

	/**
	 * This is the experian new user Id.
	 */
	private String experianNewUserId;

	/**
	 * This method get the experian new user id.
	 * 
	 * @return experianNewUserId
	 */
	public String getExperianNewUserId() {
		return experianNewUserId;
	}

	/**
	 * This method sety the experian new user id.
	 * 
	 * @param experianNewUserId
	 */
	public void setExperianNewUserId(String experianNewUserId) {
		this.experianNewUserId = experianNewUserId;
	}

	/**
	 * This method get the approved
	 * 
	 * @return the approved
	 */
	public String getApproved() {
		return approved;
	}

	/**
	 * This method set the approved
	 * 
	 * @param approved
	 *            the approved to set
	 */
	public void setApproved(String approved) {
		this.approved = approved;
	}

	/**
	 * This method get the value of disableForReport
	 * 
	 * @return the disableForReport
	 */
	public String getDisableForReport() {
		return disableForReport;
	}

	/**
	 * This method set the value of disableForReport
	 * 
	 * @param disableForReport
	 *            the disableForReport to set
	 */
	public void setDisableForReport(String disableForReport) {
		this.disableForReport = disableForReport;
	}

	private String approved;

	/**
	 * We are taking string because for null check.
	 */
	private String disableForReport;

	/**
	 * This is the campaign type
	 */
	private String campaignType;
	/**
	 * This is the campaign source
	 */
	private String campaignSource;

	/**
	 * This method get the campaign type
	 * 
	 * @return the campaignType
	 */
	public String getCampaignType() {
		return campaignType;
	}

	/**
	 * This method set the campaign type
	 * 
	 * @param campaignType
	 *            the campaignType to set
	 */
	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	/**
	 * This method get the campaign source
	 * 
	 * @return the campaignSource
	 */
	public String getCampaignSource() {
		return campaignSource;
	}

	/**
	 * This method set the campaign source
	 * 
	 * @param campaignSource
	 *            the campaignSource to set
	 */
	public void setCampaignSource(String campaignSource) {
		this.campaignSource = campaignSource;
	}

	/**
	 * This method get the campaign uuid
	 * 
	 * @return the campaignUUID
	 */
	public String getCampaignUUID() {
		return campaignUUID;
	}

	/**
	 * This method set the campaign uuid
	 * 
	 * @param campaignUUID
	 *            the campaignUUID to set
	 */
	public void setCampaignUUID(String campaignUUID) {
		this.campaignUUID = campaignUUID;
	}

	/**
	 * This method is used to get the user refer Id
	 * 
	 * @return the userReferId
	 */
	public String getUserReferId() {
		return userReferId;
	}

	/**
	 * This method is used to set the user refer Id
	 * 
	 * @param userReferId
	 *            the userReferId to set
	 */
	public void setUserReferId(String userReferId) {
		this.userReferId = userReferId;
	}

	/**
	 * This is the campaign UUID
	 */
	private String campaignUUID;

	/**
	 * This is the user refer id
	 */
	private String userReferId;

	/**
	 * This is used to check if user has sent feedback
	 */
	private boolean isFeedBackSubmitted;

	/**
	 * This gets the isFeedback submitted boolean value
	 * 
	 * @return The boolean value of isFeedbacksubmitted
	 */
	public boolean getIsFeedBackSubmitted() {
		return isFeedBackSubmitted;
	}

	/**
	 * This sets the isFeedback submitted boolean value
	 * 
	 * @param isFeedBackSubmitted
	 *            The boolean value of isFeedBackSubmitted
	 */
	public void setIsFeedBackSubmitted(boolean isFeedBackSubmitted) {
		this.isFeedBackSubmitted = isFeedBackSubmitted;
	}

	/**
	 * This method get the user total score
	 * 
	 * @return the totalScore
	 */
	public String getTotalScore() {
		return totalScore;
	}

	/**
	 * This method set the user total score
	 * 
	 * @param totalScore
	 *            the totalScore to set
	 */
	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	/**
	 * Gets the incometaxuuid
	 * 
	 * @return the incomeTaxUuid
	 */
	public String getIncomeTaxUuid() {
		return incomeTaxUuid;
	}

	/**
	 * Sets the incometaxuuid
	 * 
	 * @param incomeTaxUuid
	 *            the incomeTaxUuid to set
	 */
	public void setIncomeTaxUuid(String incomeTaxUuid) {
		this.incomeTaxUuid = incomeTaxUuid;
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
	 * Gets the rank
	 * 
	 * @return the rank
	 */
	public String getRank() {
		return rank;
	}

	/**
	 * Sets the rank
	 * 
	 * @param rank
	 *            the rank to set
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}

	/**
	 * Gets the totalScoreInDouble
	 * 
	 * @return the totalScoreInDouble
	 */
	public double getTotalScoreInDouble() {
		return totalScoreInDouble;
	}

	/**
	 * Sets the totalScoreInDouble
	 * 
	 * @param totalScoreInDouble
	 *            the totalScoreInDouble to set
	 */
	public void setTotalScoreInDouble(double totalScoreInDouble) {
		this.totalScoreInDouble = totalScoreInDouble;
	}

	/**
	 * This is the user total score
	 */
	private String totalScore;

	/**
	 * This is the user refer score
	 */
	private double totalReferScore;

	/**
	 * Gets the total refer score
	 * 
	 * @return the total refer score
	 */
	public double getTotalReferScore() {
		return totalReferScore;
	}

	/**
	 * Sets the total refer score
	 * 
	 * @param totalReferScore
	 *            This is the total refer score
	 */
	public void setTotalReferScore(double totalReferScore) {
		this.totalReferScore = totalReferScore;
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
	 * @return the increaseScore
	 */
	public boolean isIncreaseScore() {
		return increaseScore;
	}

	/**
	 * @param increaseScore
	 *            the increaseScore to set
	 */
	public void setIncreaseScore(boolean increaseScore) {
		this.increaseScore = increaseScore;
	}

	/**
	 * Gets the report input id
	 * 
	 * @return the reportInputId
	 */
	public String getReportInputId() {
		return reportInputId;
	}

	/**
	 * Sets the report input id
	 * 
	 * @param reportInputId
	 *            the reportInputId to set
	 */
	public void setReportInputId(String reportInputId) {
		this.reportInputId = reportInputId;
	}

	/**
	 * This is the income tax uuid used to refer to income tax data for the user
	 */
	private String incomeTaxUuid;
	/**
	 * This is the role name of user
	 */
	private String roleName;
	/**
	 * This is the user's rank for assessments
	 */
	private String rank;
	/**
	 * This is the user total score in double for saving in MySQL
	 */
	private double totalScoreInDouble;
	/**
	 * This is the has skipped email input that tells that user is not
	 * interested in inputing his/her emailid
	 */
	private boolean hasSkippedEmailInput;

	private boolean increaseScore;
	/**
	 * This is the reportinput id
	 */
	private String reportInputId;

}