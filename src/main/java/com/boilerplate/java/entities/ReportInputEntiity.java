package com.boilerplate.java.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This method provides the initial input to fetching a report.
 * @author gaurav.verma.icloud
 *
 */
@ApiModel(value="This is the report input entity"
, description="This is the report input entity", parent=BaseEntity.class)
public class ReportInputEntiity extends ExperianDataPublishEntity implements Serializable{
	
	/**
	 * Gets the question map
	 * @return The question map
	 */
	public BoilerplateMap <String, ExperianQuestionAnswer> getQuestion() {
		return question;
	}

	/**
	 * This sets the question map
	 * @param question
	 */
	public void setQuestion(BoilerplateMap <String, ExperianQuestionAnswer> question) {
		this.question = question;
	}

	/**
	 * Gets the current question
	 * @return The current question
	 */
	public ExperianQuestionAnswer getCurrentQuestion() {
		return currentQuestion;
	}

	/**
	 * This sets the current question
	 * @param currentQuestion The current question
	 */
	public void setCurrentQuestion(ExperianQuestionAnswer currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	public FileEntity getReportFileEntity() {
		return reportFileEntity;
	}

	public void setReportFileEntity(FileEntity reportFileEntity) {
		this.reportFileEntity = reportFileEntity;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public String getUserLoginId() {
		return userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	/**
	 * This is the map of question
	 */

	@ApiModelProperty(value="This is the map of question and answers")
	private BoilerplateMap <String, ExperianQuestionAnswer> question = new BoilerplateMap<>();
	
	/**
	 * 
	 */

	@ApiModelProperty(value="This is the current question")
	private ExperianQuestionAnswer currentQuestion;
	
	@ApiModelProperty(value="This is the list of proof files like id and address proof")
	private BoilerplateList<String> proofFiles = new BoilerplateList<>();
	
	/**
	 * Gets the list of proof files
	 * @return The proof files
	 */
	public BoilerplateList<String> getProofFiles() {
		return proofFiles;
	}

	/**
	 * This sets the proof files
	 * @param proofFiles The proof file
	 */
	public void setProofFiles(BoilerplateList<String> proofFiles) {
		this.proofFiles = proofFiles;
	}



	public String getReportNumber() {
		return reportNumber;
	}

	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}

	@JsonIgnore
	private FileEntity reportFileEntity;
	
	@JsonIgnore
	private String userId;
	
	private Report report;
	
	private String userLoginId;
	
	private String reportNumber;
	
	/**
	 * This is experian session one id.
	 */
	private String sessionId1;
	
	/**
	 * This is experian session two id.
	 */
	private String sessionId2;
	
	/**
	 * This method get session 2 id
	 * @return the session 2 id
	 */
	public String getSessionId2() {
		return sessionId2;
	}
	/**
	 * This method set session 2 id
	 * @param sessionId2 the session 2 id
	 */
	public void setSessionId2(String sessionId2) {
		this.sessionId2 = sessionId2;
	}
	/**
	 * This method get session 1 id
	 * @return the session 1 id
	 */
	public String getSessionId1() {
		return sessionId1;
	}
	/**
	 * This method set session 1 id
	 * @param sessionId2 the session 1 id
	 */
	public void setSessionId1(String sessionId1) {
		this.sessionId1 = sessionId1;
	}
	/**
	 * The url for api
	 */
	@JsonIgnore
	private String url;
	
	/**
	 * The jsession id 1
	 */
	@JsonIgnore
	private String jSessionId1;
	
	/**
	 * The jsession id 2
	 */
	@JsonIgnore
	private String jSessionId2;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the jsession id
	 * @return The jsession id
	 */
	public String getjSessionId1() {
		return jSessionId1;
	}

	/**
	 * Sets the jsession id
	 * @param jSessionId1 The jsession id
	 */
	public void setjSessionId1(String jSessionId1) {
		this.jSessionId1 = jSessionId1;
	}

	/**
	 * gets the jsession id 2
	 * @return The jsession id 2
	 */
	public String getjSessionId2() {
		return jSessionId2;
	}

	/**
	 * Sets the jsession id 2
	 * @param jSessionId2 The jsession id 2
	 */
	public void setjSessionId2(String jSessionId2) {
		this.jSessionId2 = jSessionId2;
	}
	/**
	 * This is the voucher code given to the customer for the given session.
	 */
	@JsonIgnore
	private String voucherCode;
	
	
	/**
	 * Gets the voucher code
	 * @return The vocher code
	 */
	public String getVoucherCode() {
		return voucherCode;
	}

	/**
	 * This sets voucher code
	 * @param voucherCode The voucher code
	 */
	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}
	
	/**
	 * This method gets the voucher expiry
	 * @return voucherExpiry The voucherExpiry
	 */
	public java.util.Date getVoucherExpiry() {
		return voucherExpiry;
	}

	/**
	 * This method sets the voucher expiry
	 * @param voucherExpiry The voucherExpiry
	 */
	public void setVoucherExpiry(java.util.Date voucherExpiry) {
		this.voucherExpiry = voucherExpiry;
	}


	/**
	 * This method get the question count
	 * @return the questionCount
	 */
	public int getQuestionCount() {
		return questionCount;
	}

	/**
	 * This method set the question count
	 * @param questionCount the questionCount to set
	 */
	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}
	/**
	 * This method get the dont send kyc sms value
	 * @return the dontSendKycSms
	 */
	public boolean isDontSendKycSms() {
		return dontSendKycSms;
	}

	/**
	 * This method set the dont send kyc sms value
	 * @param dontSendKycSms the dontSendKycSms to set
	 */
	public void setDontSendKycSms(boolean dontSendKycSms) {
		this.dontSendKycSms = dontSendKycSms;
	}

	/**
	 * This is the voucher expiry for voucher given to the customer for the given session.
	 */
	@JsonIgnore
	private java.util.Date voucherExpiry;
	
	
	private int questionCount;
	/**
	 * This is the boolean check for kyc sms send to customer or not
	 */
	private boolean dontSendKycSms;
}
