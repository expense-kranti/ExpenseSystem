package com.boilerplate.java.entities;

import java.io.Serializable;
import java.sql.Date;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;

/**
 * This is the ReportTradeLine Entity
 * 
 * @author amit
 *
 */

public class ReportTradeline extends BaseEntity implements Serializable {

	/**
	 * This is the offerEmail
	 */
	private String offerEmail;

	/**
	 * This is the dpd
	 */
	private int DPD;

	/**
	 * This is the organization name
	 */
	private String organizationName;
	/**
	 * This is the product name
	 */
	private String productName;

	/**
	 * This is the reportId
	 */
	private String reportId;
	/**
	 * This is the account number
	 */
	private String accountNumber;

	/**
	 * This is the openning date
	 */
	java.util.Date dateOpened;
	/**
	 * This is the close date
	 */
	java.util.Date dateClosed;
	/**
	 * This is the last payment date
	 */
	java.util.Date dateOfLastPayment;
	/**
	 * This is the acount holderType
	 */
	String accountHolderType;

	/**
	 * This is the current balance
	 */
	double currentBalance;
	/**
	 * This is the reported date
	 */
	java.util.Date dateReported;
	/**
	 * This is the due amount
	 */
	double amountDue;

	/**
	 * This is the user id
	 */
	private String userId;
	/**
	 * This is the occupation
	 */
	private String occupation;

	/**
	 * This is the status of tradeLine report
	 */
	ReportTradelineStatus reportTradelineStatus;

	/**
	 * This is the experian Trade Line status Enun
	 */
	ExperianTradelineStatus experianTradelineStatusEnum;

	/**
	 * This is electronic Contacts
	 */
	BoilerplateList<ElectronicContact> electronicContacts = new BoilerplateList<>();

	/**
	 * This is the the address
	 */
	BoilerplateList<Address> addresses = new BoilerplateList<>();

	/**
	 * This method is used to get the offerEmail
	 * 
	 * @return offerEmail
	 */
	public String getOfferEmail() {
		return offerEmail;
	}

	/**
	 * This method is used to set the offerEmail
	 * 
	 * @param offerEmail
	 */
	public void setOfferEmail(String offerEmail) {
		this.offerEmail = offerEmail;
	}

	/**
	 * This method is used to get the dPD
	 * 
	 * @return dPD
	 */
	public int getDPD() {
		return DPD;
	}

	/**
	 * This method is used to set the dPD
	 * 
	 * @param dPD
	 */
	public void setDPD(int dPD) {
		DPD = dPD;
	}

	/**
	 * This method is used to get the organization name
	 * 
	 * @return organizationName
	 */
	public String getOrganizationName() {
		return organizationName;
	}

	/**
	 * This method is used to set the organization name
	 * 
	 * @param organizationName
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	/**
	 * This method is used to get the product name
	 * 
	 * @return productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * This method is used to set the product name
	 * 
	 * @param productName
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * This method is used to get the reportId
	 * 
	 * @return reportId
	 */
	public String getReportId() {
		return reportId;
	}

	/**
	 * This method is used to set the reportId
	 * 
	 * @param reportId
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	/**
	 * This method is used to get the accountNumber
	 * 
	 * @return accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * This method is used to set the accountNumber
	 * 
	 * @param accountNumber
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * This method is used to get the dateOpened
	 * 
	 * @return dateOpened
	 */
	public java.util.Date getDateOpened() {
		return dateOpened;
	}

	/**
	 * This method is used to set the dateOpened
	 * 
	 * @param dateOpened
	 */
	public void setDateOpened(Date dateOpened) {
		this.dateOpened = dateOpened;
	}

	/**
	 * This method is used to get the dateClosed
	 * 
	 * @return dateClosed
	 */
	public java.util.Date getDateClosed() {
		return dateClosed;
	}

	/**
	 * This method is used to set the dateClosed
	 * 
	 * @param dateClosed
	 */
	public void setDateClosed(Date dateClosed) {
		this.dateClosed = dateClosed;
	}

	/**
	 * This method is used to get the dateOfLastPayment
	 * 
	 * @return dateOfLastPayment
	 */
	public java.util.Date getDateOfLastPayment() {
		return dateOfLastPayment;
	}

	/**
	 * This method is used to set the dateOfLastPayment
	 * 
	 * @param dateOfLastPayment
	 */
	public void setDateOfLastPayment(Date dateOfLastPayment) {
		this.dateOfLastPayment = dateOfLastPayment;
	}

	/**
	 * This method is used to get the accountHolderType
	 * 
	 * @return accountHolderType
	 */
	public String getAccountHolderType() {
		return accountHolderType;
	}

	/**
	 * This method is used to set the accountHolderType
	 * 
	 * @param accountHolderType
	 */
	public void setAccountHolderType(String accountHolderType) {
		this.accountHolderType = accountHolderType;
	}

	/**
	 * This method is used to get the currentBalance
	 * 
	 * @return currentBalance
	 */
	public double getCurrentBalance() {
		return currentBalance;
	}

	/**
	 * This method is used to set the currentBalance
	 * 
	 * @param currentBalance
	 */
	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	/**
	 * This method is used to get the dateReported
	 * 
	 * @return dateReported
	 */
	public java.util.Date getDateReported() {
		return dateReported;
	}

	/**
	 * This method is used to set the dateReported
	 * 
	 * @param date
	 */
	public void setDateReported(java.util.Date date) {
		this.dateReported = date;
	}

	/**
	 * This method is used to get the amountDue
	 * 
	 * @return amountDue
	 */
	public double getAmountDue() {
		return amountDue;
	}

	/**
	 * This method is used to set the amountDue
	 * 
	 * @param amountDue
	 */
	public void setAmountDue(double amountDue) {
		this.amountDue = amountDue;
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
	 * This method is used to get the occupation
	 * 
	 * @return occupation
	 */
	public String getOccupation() {
		return occupation;
	}

	/**
	 * This method is used to set the occupation
	 * 
	 * @param occupation
	 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public ExperianTradelineStatus getExperianTradelineStatusEnum() {
		return experianTradelineStatusEnum;
	}

	public void setExperianTradelineStatusEnum(ExperianTradelineStatus experianTradelineStatusEnum) {
		this.experianTradelineStatusEnum = experianTradelineStatusEnum;
	}

	public BoilerplateList<ElectronicContact> getElectronicContacts() {
		return electronicContacts;
	}

	public void setElectronicContacts(BoilerplateList<ElectronicContact> electronicContacts) {
		this.electronicContacts = electronicContacts;
	}

	/**
	 * This method is used to get the reportTradelineStatus
	 * 
	 * @return reportTradelineStatus
	 */
	public ReportTradelineStatus getReportTradelineStatus() {
		return reportTradelineStatus;
	}

	/**
	 * This method is used to set the reportTradelineStatus
	 * 
	 * @param reportTradelineStatus
	 */
	public void setReportTradelineStatus(ReportTradelineStatus reportTradelineStatus) {
		this.reportTradelineStatus = reportTradelineStatus;
	}

	/**
	 * This method is used to get the addresses
	 * 
	 * @return addresses
	 */
	public BoilerplateList<Address> getAddresses() {
		return addresses;
	}

	/**
	 * This method is used to set the addresses
	 * 
	 * @param addresses
	 */
	public void setAddresses(BoilerplateList<Address> addresses) {
		this.addresses = addresses;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}

	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return this;
	}

}