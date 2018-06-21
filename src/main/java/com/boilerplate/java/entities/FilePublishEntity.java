package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class FilePublishEntity extends BaseEntity implements Serializable, ICRMPublishDynamicURl, ICRMPublishEntity {
	/**
	 * This is the pan file url of front
	 */
	private String panUrlFront;
	/**
	 * This is the aadhar file Url Front 
	 */
	private String aadharUrlFront;
	/**
	 * This is the passport file Url Front
	 */
	private String passportUrlFront;
	/**
	 * This is the report file Url Front
	 */
	private String reportUrlFront;
	/**
	 * This is the voterId file url front
	 */
	private String voterIdUrlFront;
	/**
	 * This is the Pan file url of back
	 */
	private String panUrlBack;
	/**
	 * This is the aadhar file Url Back
	 */
	private String aadharUrlBack;
	/**
	 * This is the passport file Url Back
	 */
	private String passportUrlBack;
	/**
	 * This is the report file url of back
	 */
	private String reportUrlBack;
	/**
	 * This is the voterId url of back
	 */
	private String voterIdUrlBack;
	/**
	 * This method gets the pan file url of front
	 * @return The panUrlFront
	 */
	public String getPanUrlFront() {
		return panUrlFront;
	}
	/**
	 * This method dets the pan file url of back
	 * @param panUrlFront The panUrlFront
	 */
	public void setPanUrlFront(String panUrlFront) {
		this.panUrlFront = panUrlFront;
	}
	/**
	 * This method gets the Aadhar file Url of Front
	 * @return The aadharUrlFront
	 */
	public String getAadharUrlFront() {
		return aadharUrlFront;
	}
	/**
	 * This method gets the Aadhar file Url of Front
	 * @param aadharUrlFront
	 */
	public void setAadharUrlFront(String aadharUrlFront) {
		this.aadharUrlFront = aadharUrlFront;
	}
	/**
	 * This method gets the Passport file url of front
	 * @return The passportUrlFront
	 */
	public String getPassportUrlFront() {
		return passportUrlFront;
	}
	/**
	 * This method sets the passport file of url front
	 * @param passportUrlFront
	 */
	public void setPassportUrlFront(String passportUrlFront) {
		this.passportUrlFront = passportUrlFront;
	}
	/**
	 * This method gets the Report file Url of fornt
	 * @return The reportUrlFront
	 */
	public String getReportUrlFront() {
		return reportUrlFront;
	}
	/**
	 * This method sets the report file Url of fornt
	 * @param reportUrlFront The reportUrlFront
	 */
	public void setReportUrlFront(String reportUrlFront) {
		this.reportUrlFront = reportUrlFront;
	}
	/**
	 * This method gets the VoterId file url of the front
	 * @return voterIdUrlFront The voterIdUrlFront
	 */
	public String getVoterIdUrlFront() {
		return voterIdUrlFront;
	}
	/**
	 * This method sets the VoterId file url of the front
	 * @param voterIdUrlFront The voterIdUrlFront
	 */
	public void setVoterIdUrlFront(String voterIdUrlFront) {
		this.voterIdUrlFront = voterIdUrlFront;
	}
	/**
	 * This method gets the pan file url of Back
	 * @return The panUrlBack
	 */
	public String getPanUrlBack() {
		return panUrlBack;
	}
	/**
	 * This method sets the pan file url of back
	 * @param panUrlBack The panUrlBack
	 */
	public void setPanUrlBack(String panUrlBack) {
		this.panUrlBack = panUrlBack;
	}
	/**
	 * This method gets the Aadhar file url of back
	 * @return The aadharUrlBack
	 */
	public String getAadharUrlBack() {
		return aadharUrlBack;
	}
	/**
	 * This method sets the Aadhar file Url of the back
	 * @param aadharUrlBack The aadharUrlBack
	 */
	public void setAadharUrlBack(String aadharUrlBack) {
		this.aadharUrlBack = aadharUrlBack;
	}
	/**
	 * This method gets the Passport file url of the back
	 * @return The passportUrlBack
	 */
	public String getPassportUrlBack() {
		return passportUrlBack;
	}
	/**
	 * This method sets the passport file url of the back
	 * @param passportUrlBack
	 */
	public void setPassportUrlBack(String passportUrlBack) {
		this.passportUrlBack = passportUrlBack;
	}
	/**
	 * This method gets the report file url of the back
	 * @return The reportUrlBack
	 */
	public String getReportUrlBack() {
		return reportUrlBack;
	}
	/**
	 * This method sets the Report file url 0f the back
	 * @param reportUrlBack The reportUrlBack
	 */
	public void setReportUrlBack(String reportUrlBack) {
		this.reportUrlBack = reportUrlBack;
	}
	/**
	 * This method gets the voterId file url of the back
	 * @return The voterIdUrlBack
	 */
	public String getVoterIdUrlBack() {
		return voterIdUrlBack;
	}
	/**
	 * This method sets the voterId file url of the back
	 * @param voterIdUrlBack The voterIdUrlBack
	 */
	public void setVoterIdUrlBack(String voterIdUrlBack) {
		this.voterIdUrlBack = voterIdUrlBack;
	}
	/**
	 * @see ICRMPublishEntity.createPublishJSON
	 */
	@Override
	public String createPublishJSON(String template) {
		
		String retrunValue = template;	
		retrunValue = retrunValue.replace("@panUrlFront", this.getPanUrlFront() == null ? "" : this.getPanUrlFront());
		retrunValue = retrunValue.replace("@aadharUrlFront", this.getAadharUrlFront() == null ? "" : this.getAadharUrlFront());
		retrunValue = retrunValue.replace("@passportUrlFront", this.getPassportUrlFront() == null ? "" : this.getPassportUrlFront());
		retrunValue = retrunValue.replace("@reportUrlFront", this.getReportUrlFront() == null ? "" : this.getReportUrlFront());
		retrunValue = retrunValue.replace("@voterIdUrlFront", this.getVoterIdUrlFront() == null ? "" : this.getVoterIdUrlFront());
		retrunValue = retrunValue.replace("@electricityBillUrlFront", this.getElectricityBillUrlFront() == null ? "" : this.getElectricityBillUrlFront());
		retrunValue = retrunValue.replace("@panUrlBack", this.getPanUrlBack() == null ? "" : this.getPanUrlBack());
		retrunValue = retrunValue.replace("@aadharUrlBack", this.getAadharUrlBack() == null ? "" : this.getAadharUrlBack());
		retrunValue = retrunValue.replace("@passportUrlBack", this.getPassportUrlBack() == null ? "" : this.getPassportUrlBack());
		retrunValue = retrunValue.replace("@reportUrlBack", this.getReportUrlBack() == null ? "" : this.getReportUrlBack());
		retrunValue = retrunValue.replace("@voterIdUrlBack", this.getVoterIdUrlBack() == null ? "" : this.getVoterIdUrlBack());
		retrunValue = retrunValue.replace("@electricityBillUrlBack", this.getElectricityBillUrlBack() == null ? "" : this.getElectricityBillUrlBack());
		return retrunValue;
	}
	
	/**
	 * Validates
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * @see  BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}
	
	/**
	 * @see  BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}
	
	/**
	 * This method creates the dynamic crm publish url.
	 * @return returnUrl The publish url  string
	 */
	@Override
	public String createPublishUrl(String url) {
		String returnUrl = url;
		returnUrl = returnUrl.replace("@crmRecordID", this.getCrmRecordID());
		return returnUrl;
	}
	/**
	 * This method gets the crmRecordID
	 * @return The crmRecordID
	 */
	public String getCrmRecordID() {
		return crmRecordID;
	}
	/**
	 * This method sets the crmRecordID
	 * @param crmRecordID The crmRecordID
	 */
	public void setCrmRecordID(String crmRecordID) {
		this.crmRecordID = crmRecordID;
	}
	/**
	 * This is the CRM record Id
	 */
	@JsonIgnore
	private String crmRecordID;
	
	/**
	 * This is the userId
	 */
	@JsonIgnore
	private String userId;
	
	/**
	 * This method sets the UserId
	 * @param userId The UserId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This method gets the UserId
	 * @return The UserId
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * THis method gets the electricityBillUrlFront
	 * @return The electricityBillUrlFront
	 */
	public String getElectricityBillUrlFront() {
		return electricityBillUrlFront;
	}
	
	/**
	 * This method sets the electricityBillUrlFront
	 * @param electricityBillUrlFront The electricityBillUrlFront
	 */
	public void setElectricityBillUrlFront(String electricityBillUrlFront) {
		this.electricityBillUrlFront = electricityBillUrlFront;
	}
	
	/**
	 * THis method gets the electricity Bill Url Back
	 * @return The electricityBillUrlBack
	 */
	public String getElectricityBillUrlBack() {
		return electricityBillUrlBack;
	}
	
	/**
	 * THis method sets the electricity Bill Url Back
	 * @param electricityBillUrlBack The electricityBillUrlBack
	 */
	public void setElectricityBillUrlBack(String electricityBillUrlBack) {
		this.electricityBillUrlBack = electricityBillUrlBack;
	}
	
	/**
	 * This is the electricity Bill Url Front
	 */
	private String electricityBillUrlFront;
	
	/**
	 * This is the electricity Bill Url Back
	 */
	private String electricityBillUrlBack;
}
