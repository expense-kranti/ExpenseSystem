package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;

/**
 * This is the the Report input Entity which provides the initial input to fetch
 * a report
 * 
 * @author amit
 *
 */
@ApiModel(value = "This is the report input entity", description = "This is the report input entity", parent = BaseEntity.class)
public class ReportInputEntity extends ExperianDataPublishEntity implements Serializable {

	// This is the map for question and answer
	private BoilerplateMap<String, ExperianQuestionAnswer> question = new BoilerplateMap<>();

	// This holds the current question
	private ExperianQuestionAnswer currentQuestion;

	// This is the proofFiles
	private BoilerplateList<String> proofFiles = new BoilerplateList<>();

	// This the user id
	@JsonIgnore
	private String userId;

	// This is experian session one id.
	private String sessionId1;

	// This is experian session two id.
	private String sessionId2;

	// This is the API URL
	@JsonIgnore
	private String url;

	// This is the jsession id 1
	@JsonIgnore
	private String jSessionId1;

	// This is jsession id 2

	@JsonIgnore
	private String jSessionId2;

	// This is the voucher code given to the customer for the given session.
	@JsonIgnore
	private String voucherCode;

	// This is the voucher expiry for voucher given to the customer for the
	// given
	// session.
	@JsonIgnore
	private java.util.Date voucherExpiry;

	// This is the QuestionCount
	private int questionCount;

	/**
	 * This is the reportFileNameOnDisk
	 */
	private String reportFileNameOnDisk;
	/**
	 * This is the reportVersion
	 */
	private ReportVersion reportVersionEnum;
	/**
	 * This is the reportVersion
	 */
	private String reportNumber;
	/**
	 * This is the reportVersion
	 */
	private String reportFileId;

	/**
	 * This method is used to get the question
	 * 
	 * @return question
	 */
	public BoilerplateMap<String, ExperianQuestionAnswer> getQuestion() {
		return question;
	}

	/**
	 * This method is used to set the question
	 * 
	 * @param question
	 */
	public void setQuestion(BoilerplateMap<String, ExperianQuestionAnswer> question) {
		this.question = question;
	}

	/**
	 * This method is used to get the currentQuestion
	 * 
	 * @return currentQuestion
	 */
	public ExperianQuestionAnswer getCurrentQuestion() {
		return currentQuestion;
	}

	/**
	 * This method is used to set the currentQuestion
	 * 
	 * @param currentQuestion
	 */
	public void setCurrentQuestion(ExperianQuestionAnswer currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	/**
	 * This method is used to get the proofFiles
	 * 
	 * @return proofFiles
	 */
	public BoilerplateList<String> getProofFiles() {
		return proofFiles;
	}

	/**
	 * This method is used to set the proofFiles
	 * 
	 * @param proofFiles
	 */
	public void setProofFiles(BoilerplateList<String> proofFiles) {
		this.proofFiles = proofFiles;
	}

	/**
	 * This method is used to get the userId
	 * 
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * This method is used to set the userId
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This method is used to get the sessionId1
	 * 
	 * @return sessionId1
	 */
	public String getSessionId1() {
		return sessionId1;
	}

	/**
	 * This method is used to set the sessionId1
	 * 
	 * @param sessionId1
	 */
	public void setSessionId1(String sessionId1) {
		this.sessionId1 = sessionId1;
	}

	/**
	 * This method is used to get the sessionId2
	 * 
	 * @return sessionId2
	 */
	public String getSessionId2() {
		return sessionId2;
	}

	/**
	 * This method is used to set the sessionId2
	 * 
	 * @param sessionId2
	 */
	public void setSessionId2(String sessionId2) {
		this.sessionId2 = sessionId2;
	}

	/**
	 * This method is used to get the url
	 * 
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * This method is used to set the url
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * This method is used to get the jSessionId1
	 * 
	 * @return jSessionId1
	 */
	public String getjSessionId1() {
		return jSessionId1;
	}

	/**
	 * This method is used to set the jSessionId1
	 * 
	 * @param jSessionId1
	 */
	public void setjSessionId1(String jSessionId1) {
		this.jSessionId1 = jSessionId1;
	}

	/**
	 * This method is used to get the jSessionId2
	 * 
	 * @return jSessionId2
	 */
	public String getjSessionId2() {
		return jSessionId2;
	}

	/**
	 * This method is used to set the jSessionId2
	 * 
	 * @param jSessionId2
	 */
	public void setjSessionId2(String jSessionId2) {
		this.jSessionId2 = jSessionId2;
	}

	/**
	 * This method is used to get the voucherCode
	 * 
	 * @return voucherCode
	 */
	public String getVoucherCode() {
		return voucherCode;
	}

	/**
	 * This method is used to set the voucherCode
	 * 
	 * @param voucherCode
	 */
	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}

	/**
	 * This method is used to get the voucherExpiry
	 * 
	 * @return voucherExpiry
	 */
	public java.util.Date getVoucherExpiry() {
		return voucherExpiry;
	}

	/**
	 * This method is used to set the voucherExpiry
	 * 
	 * @param voucherExpiry
	 */
	public void setVoucherExpiry(java.util.Date voucherExpiry) {
		this.voucherExpiry = voucherExpiry;
	}

	/**
	 * This method is used to get the questionCount
	 * 
	 * @return questionCount
	 */
	public int getQuestionCount() {
		return questionCount;
	}

	/**
	 * This method is used to set the questionCount
	 * 
	 * @param questionCount
	 */
	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	/**
	 * Gets the reportFileNameOnDisk
	 * 
	 * @return reportFileNameOnDisk
	 */
	public String getReportFileNameOnDisk() {
		return reportFileNameOnDisk;
	}

	/**
	 * Sets the reportFileNameOnDisk
	 * 
	 * @param reportFileNameOnDisk
	 *            to set
	 */
	public void setReportFileNameOnDisk(String reportFileNameOnDisk) {
		this.reportFileNameOnDisk = reportFileNameOnDisk;
	}

	

	public boolean isDontSendKycSms() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Gets the report version enum
	 * 
	 * @return the reportVersionEnum
	 */
	public ReportVersion getReportVersionEnum() {
		return reportVersionEnum;
	}

	/**
	 * Sets the report version enum
	 * 
	 * @param reportVersionEnum
	 *            the reportVersionEnum to set
	 */
	public void setReportVersionEnum(ReportVersion reportVersionEnum) {
		this.reportVersionEnum = reportVersionEnum;
	}

	/**
	 * Gets the report version
	 * 
	 * @return the reportversion number
	 */
	public int getReportVersion() {
		return this.reportVersionEnum.ordinal();
	}

	/**
	 * Sets the report version number
	 * 
	 * @param reportVersion
	 *            to set
	 */
	public void setReportVersion(int reportVersion) {
		this.reportVersionEnum = ReportVersion.values()[reportVersion];
	}

	/**
	 * Gets the report number
	 * 
	 * @return the reportNumber
	 */
	public String getReportNumber() {
		return reportNumber;
	}

	/**
	 * Sets the report number
	 * 
	 * @param reportNumber
	 *            the reportNumber to set
	 */
	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}

	/**
	 * Gets the report file id
	 * 
	 * @return the reportFileId
	 */
	public String getReportFileId() {
		return reportFileId;
	}

	/**
	 * Sets the report file id
	 * 
	 * @param reportFileId
	 *            the reportFileId to set
	 */
	public void setReportFileId(String reportFileId) {
		this.reportFileId = reportFileId;
	}

}
