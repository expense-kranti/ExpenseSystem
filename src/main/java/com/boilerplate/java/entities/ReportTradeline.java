package com.boilerplate.java.entities;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;

public class ReportTradeline extends BaseEntity implements Serializable,ICRMPublishDynamicURl,ICRMPublishEntity{


	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}

	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	@Override
	public BaseEntity transformToExternal() {
		return this;
	}
	
	String reportId;
	
	String accountNumber;
	
	double highCreditLoanAmount;
	
	double repaymentTenure;
	
	java.util.Date dateOpened;
	
	java.util.Date dateClosed;
	
	java.util.Date dateOfLastPayment;
	
	String accountHolderType;
	
	public BoilerplateList<ElectronicContact> getElectronicContacts() {
		return electronicContacts;
	}

	public void setElectronicContacts(BoilerplateList<ElectronicContact> electronicContacts) {
		this.electronicContacts = electronicContacts;
	}

	java.util.Date lastHistoryDate;
	
	int daysPastDue;
	
	String address;
	
	BoilerplateList<ElectronicContact> electronicContacts = new BoilerplateList<>();
	
	BoilerplateList<Address> addresses = new BoilerplateList<>();
	
	

	public BoilerplateList<Address> getAddresses() {
		return addresses;
	}

	

	public void setAddresses(BoilerplateList<Address> addresses) {
		this.addresses = addresses;
	}

	public int getDaysPastDue() {
		return daysPastDue;
	}

	public void setDaysPastDue(int daysPastDue) {
		this.daysPastDue = daysPastDue;
	}

	String phone;
	
	double settlementAmount;
	
	double currentBalance;
	
	java.util.Date dateReported;
	
	double amountDue;
	
	String valueCollateral;
	
	String typeCollateral;
	
	String occupation;
	
	double rateOfIntererst;
	
	double income;
	
	String tradeLineXML;
	
	ExperianTradelineStatus experianTradelineStatusEnum;
	
	boolean getTos;
	
	
	
	public boolean isGetTos() {
		return getTos;
	}

	public void setGetTos(boolean getTos) {
		this.getTos = getTos;
	}

	String organizationId;
	
	String productId;
	
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public double getHighCreditLoanAmount() {
		return highCreditLoanAmount;
	}

	public void setHighCreditLoanAmount(double highCreditLoanAmount) {
		this.highCreditLoanAmount = highCreditLoanAmount;
	}

	public double getRepaymentTenure() {
		return repaymentTenure;
	}

	public void setRepaymentTenure(double repaymentTenure) {
		this.repaymentTenure = repaymentTenure;
	}

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
	 * @return reportTradelineStatus The ReportTradelineStatus
	 */
	public ReportTradelineStatus getReportTradelineStatus() {
		return reportTradelineStatus;
	}

	/**
	 * This method sets the ReportTradelineStatus
	 * @param reportTradelineStatus The ReportTradelineStatus
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
	 * @return the tradelineFullName
	 */
	public String getTradelineFullName() {
		return tradelineFullName;
	}

	/**
	 * This method set  the tradeLineFull name
	 * @param tradelineFullName the tradelineFullName to set
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
	 * @return the crmReportId
	 */
	public String getCrmReportId() {
		return crmReportId;
	}

	/**
	 * This method set the crm report id
	 * @param crmReportId the crmReportId to set
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
	 * @param paymentSource the paymentSource to set
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
	 * @param leadEmail the leadEmail to set
	 */
	public void setLeadEmail(String leadEmail) {
		this.leadEmail = leadEmail;
	}

	

	private String paymentSource;
	
	private String leadEmail;
	

}
