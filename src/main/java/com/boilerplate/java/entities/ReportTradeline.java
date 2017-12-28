package com.boilerplate.java.entities;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;

public class ReportTradeline extends BaseEntity implements Serializable, ICRMPublishDynamicURl, ICRMPublishEntity {

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
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
	 * This is the report id
	 */
	String reportId;
	/**
	 * This is the accountNumber
	 */
	String accountNumber;
	/**
	 * This is the highCreditLoanAmount
	 */
	double highCreditLoanAmount;
	/**
	 * This is the repaymentTenure
	 */
	double repaymentTenure;
	/**
	 * This is the dateOpened
	 */
	java.util.Date dateOpened;
	/**
	 * This is the dateClosed
	 */
	java.util.Date dateClosed;
	/**
	 * This is the dateOfLastPayment
	 */
	java.util.Date dateOfLastPayment;
	/**
	 * This is the accountNumber
	 */
	String accountHolderType;

	/**
	 * Gets electronic contacts
	 * 
	 * @return the electronic contacts
	 */
	public BoilerplateList<ElectronicContact> getElectronicContacts() {
		return electronicContacts;
	}

	/**
	 * Sets electronic contacts
	 * 
	 * @param electronicContacts
	 *            to set
	 */
	public void setElectronicContacts(BoilerplateList<ElectronicContact> electronicContacts) {
		this.electronicContacts = electronicContacts;
	}

	/**
	 * This is the lastHistoryDate
	 */
	java.util.Date lastHistoryDate;
	/**
	 * This is the daysPastDue
	 */
	int daysPastDue;
	/**
	 * This is the address
	 */
	String address;
	/**
	 * This is the electronicContacts
	 */
	BoilerplateList<ElectronicContact> electronicContacts = new BoilerplateList<>();
	/**
	 * This is the addresses
	 */
	BoilerplateList<Address> addresses = new BoilerplateList<>();

	/**
	 * Gets the addresses
	 * 
	 * @return the addresses
	 */
	public BoilerplateList<Address> getAddresses() {
		return addresses;
	}

	/**
	 * Sets addresses
	 * 
	 * @param addresses
	 *            to set
	 */
	public void setAddresses(BoilerplateList<Address> addresses) {
		this.addresses = addresses;
	}

	/**
	 * Gets the dayspastdue
	 * 
	 * @return the days past due
	 */
	public int getDaysPastDue() {
		return daysPastDue;
	}

	/**
	 * Sets the daysPastDue
	 * 
	 * @param daysPastDue
	 *            to set
	 */
	public void setDaysPastDue(int daysPastDue) {
		this.daysPastDue = daysPastDue;
	}

	/**
	 * This is the phone
	 */
	String phone;
	/**
	 * This is the settlementAmount
	 */
	double settlementAmount;
	/**
	 * This is the currentBalance
	 */
	double currentBalance;
	/**
	 * This is the dateReported
	 */
	java.util.Date dateReported;
	/**
	 * This is the amountDue
	 */
	double amountDue;
	/**
	 * This is the valueCollateral
	 */
	String valueCollateral;
	/**
	 * This is the typeCollateral
	 */
	String typeCollateral;
	/**
	 * This is the occupation
	 */
	String occupation;
	/**
	 * This is the rateOfIntererst
	 */
	double rateOfIntererst;
	/**
	 * This is the income
	 */
	double income;
	/**
	 * This is the tradeLineXML
	 */
	String tradeLineXML;
	/**
	 * This is the experianTradelineStatusEnum
	 */
	ExperianTradelineStatus experianTradelineStatusEnum;
	/**
	 * This is the getTos
	 */
	boolean getTos;

	/**
	 * Gets the getTos
	 * 
	 * @return the getTos
	 */
	public boolean isGetTos() {
		return getTos;
	}

	/**
	 * Sets the getTos
	 * 
	 * @param getTos
	 *            to set
	 */
	public void setGetTos(boolean getTos) {
		this.getTos = getTos;
	}

	/**
	 * This is the organizationId
	 */
	String organizationId;
	/**
	 * This is the productId
	 */
	String productId;

	/**
	 * Gets the report id
	 * 
	 * @return report id
	 */
	public String getReportId() {
		return reportId;
	}

	/**
	 * Sets the report id
	 * 
	 * @param reportId
	 *            to set
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	/**
	 * Gets the accountNumber
	 * 
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Sets the accountNumber
	 * 
	 * @param accountNumber
	 *            to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	/**
	 * Gets the highCreditLoanAmount
	 * @return the highCreditLoanAmount
	 */
	public double getHighCreditLoanAmount() {
		return highCreditLoanAmount;
	}
	/**
	 * Sets the highCreditLoanAmount
	 * @param highCreditLoanAmount to set
	 */
	public void setHighCreditLoanAmount(double highCreditLoanAmount) {
		this.highCreditLoanAmount = highCreditLoanAmount;
	}
	/**
	 * Gets the repaymentTenure
	 * @return the repaymentTenure
	 */
	public double getRepaymentTenure() {
		return repaymentTenure;
	}
	/**
	 * Sets the repaymentTenure
	 * @param repaymentTenure to set
	 */
	public void setRepaymentTenure(double repaymentTenure) {
		this.repaymentTenure = repaymentTenure;
	}
	/**
	 * 
	 * @return
	 */
	public java.util.Date getDateOpened() {
		return dateOpened;
	}

	public void setDateOpened(java.util.Date dateOpened) {
		this.dateOpened = dateOpened;
	}

	public java.util.Date getDateClosed() {
		return dateClosed;
	}

	public void setDateClosed(java.util.Date dateClosed) {
		this.dateClosed = dateClosed;
	}

	public java.util.Date getDateOfLastPayment() {
		return dateOfLastPayment;
	}

	public void setDateOfLastPayment(java.util.Date dateOfLastPayment) {
		this.dateOfLastPayment = dateOfLastPayment;
	}

	public String getAccountHolderType() {
		return accountHolderType;
	}

	public void setAccountHolderType(String accountHolderType) {
		this.accountHolderType = accountHolderType;
	}

	public java.util.Date getLastHistoryDate() {
		return lastHistoryDate;
	}

	public void setLastHistoryDate(java.util.Date lastHistoryDate) {
		this.lastHistoryDate = lastHistoryDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getSettlementAmount() {
		return settlementAmount;
	}

	public void setSettlementAmount(double settlementAmount) {
		this.settlementAmount = settlementAmount;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public java.util.Date getDateReported() {
		return dateReported;
	}

	public void setDateReported(java.util.Date dateReported) {
		this.dateReported = dateReported;
	}

	public double getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(double amountDue) {
		this.amountDue = amountDue;
	}

	public String getValueCollateral() {
		return valueCollateral;
	}

	public void setValueCollateral(String valueCollateral) {
		this.valueCollateral = valueCollateral;
	}

	public String getTypeCollateral() {
		return typeCollateral;
	}

	public void setTypeCollateral(String typeCollateral) {
		this.typeCollateral = typeCollateral;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public double getRateOfIntererst() {
		return rateOfIntererst;
	}

	public void setRateOfIntererst(double rateOfIntererst) {
		this.rateOfIntererst = rateOfIntererst;
	}

	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	public String getTradeLineXML() {
		return tradeLineXML;
	}

	public void setTradeLineXML(String tradeLineXML) {
		this.tradeLineXML = tradeLineXML;
	}

	public ExperianTradelineStatus getExperianTradelineStatusEnum() {
		return experianTradelineStatusEnum;
	}

	public void setExperianTradelineStatusEnum(ExperianTradelineStatus experianTradelineStatusEnum) {
		this.experianTradelineStatusEnum = experianTradelineStatusEnum;
	}

	public int getExperianTradelineStatus() {
		return experianTradelineStatusEnum.ordinal();
	}

	public void setExperianTradelineStatus(int experianTradelineStatus) {
		this.experianTradelineStatusEnum = ExperianTradelineStatus.values()[experianTradelineStatus];
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This is the ReportTradelineStatus
	 */
	ReportTradelineStatus reportTradelineStatus;

	/**
	 * This method gets the ReportTradelineStatus
	 * 
	 * @return reportTradelineStatus The ReportTradelineStatus
	 */
	public ReportTradelineStatus getReportTradelineStatus() {
		return reportTradelineStatus;
	}

	/**
	 * This method sets the ReportTradelineStatus
	 * 
	 * @param reportTradelineStatus
	 *            The ReportTradelineStatus
	 */
	public void setReportTradelineStatus(ReportTradelineStatus reportTradelineStatus) {
		this.reportTradelineStatus = reportTradelineStatus;
	}

	@Override
	public String createPublishJSON(String template) throws UnauthorizedException {
		String retrunValue = template;
		retrunValue = retrunValue.replace("@tradelineStatus", this.getReportTradelineStatus().toString());
		return retrunValue;
	}

	/**
	 * This method get the tradeLineFull name
	 * 
	 * @return the tradelineFullName
	 */
	public String getTradelineFullName() {
		return tradelineFullName;
	}

	/**
	 * This method set the tradeLineFull name
	 * 
	 * @param tradelineFullName
	 *            the tradelineFullName to set
	 */
	public void setTradelineFullName(String tradelineFullName) {
		this.tradelineFullName = tradelineFullName;
	}

	@Override
	public String createPublishUrl(String url) throws UnsupportedEncodingException {
		String returnUrl = url;
		returnUrl = returnUrl.replace("@tradeLineID", URLEncoder.encode(this.getId(), "UTF-8"));
		return returnUrl;
	}

	String tradelineFullName;

	private String crmReportId;

	/**
	 * This method get the crm report id
	 * 
	 * @return the crmReportId
	 */
	public String getCrmReportId() {
		return crmReportId;
	}

	/**
	 * This method set the crm report id
	 * 
	 * @param crmReportId
	 *            the crmReportId to set
	 */
	public void setCrmReportId(String crmReportId) {
		this.crmReportId = crmReportId;
	}

	/**
	 * @return the paymentSource
	 */
	public String getPaymentSource() {
		return paymentSource;
	}

	/**
	 * @param paymentSource
	 *            the paymentSource to set
	 */
	public void setPaymentSource(String paymentSource) {
		this.paymentSource = paymentSource;
	}

	/**
	 * @return the leadEmail
	 */
	public String getLeadEmail() {
		return leadEmail;
	}

	/**
	 * @param leadEmail
	 *            the leadEmail to set
	 */
	public void setLeadEmail(String leadEmail) {
		this.leadEmail = leadEmail;
	}

	private String paymentSource;

	private String leadEmail;

}
