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

	// This is the offerEmail
	private String offerEmail;

	// This is the dpd
	private int DPD;

	// This is the organization name
	private String organizationName;
	// This is the product name
	private String productName;

	// This is the reportId
	private String reportId;
	// This is the account number
	private String accountNumber;

	// This is the openning date
	Date dateOpened;
	// This is the close date
	Date dateClosed;
	// This is the last payment date
	Date dateOfLastPayment;
	// This is the acount holderType
	String accountHolderType;

	// This is the current balance
	double currentBalance;
	// This is the reported date
	Date dateReported;
	// This is the due amount
	double amountDue;

	// This is the user id
	private String userId;
	// This is the occupation
	private String occupation;

	// This is the status of tradeLine report
	ReportTradelineStatus reportTradelineStatus;

	// This is the experian Trade Line status Enun
	ExperianTradelineStatus experianTradelineStatusEnum;

	// This is electronic Contacts
	BoilerplateList<ElectronicContact> electronicContacts = new BoilerplateList<>();

	// This is the the address
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
	public Date getDateOpened() {
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
	public Date getDateClosed() {
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
	public Date getDateOfLastPayment() {
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
	public Date getDateReported() {
		return dateReported;
	}

	/**
	 * This method is used to set the dateReported
	 * 
	 * @param dateReported
	 */
	public void setDateReported(Date dateReported) {
		this.dateReported = dateReported;
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
// public class ReportTradeline extends BaseEntity implements Serializable,
// ICRMPublishDynamicURl, ICRMPublishEntity {
//
// /**
// * @see BaseEntity.validate
// */
// @Override
// public boolean validate() throws ValidationFailedException {
// return true;
// }
//
// /**
// * @see BaseEntity.transformToInternal
// */
// @Override
// public BaseEntity transformToInternal() {
// return this;
// }
//
// /**
// * @see BaseEntity.transformToExternal
// */
// @Override
// public BaseEntity transformToExternal() {
// return this;
// }
//
// /**
// * This is the report id
// */
// String reportId;
// /**
// * This is the accountNumber
// */
// String accountNumber;
// /**
// * This is the highCreditLoanAmount
// */
// double highCreditLoanAmount;
// /**
// * This is the repaymentTenure
// */
// double repaymentTenure;
// /**
// * This is the dateOpened
// */
// java.util.Date dateOpened;
// /**
// * This is the dateClosed
// */
// java.util.Date dateClosed;
// /**
// * This is the dateOfLastPayment
// */
// java.util.Date dateOfLastPayment;
// /**
// * This is the accountNumber
// */
// String accountHolderType;
//
// /**
// * Gets electronic contacts
// *
// * @return the electronic contacts
// */
// public BoilerplateList<ElectronicContact> getElectronicContacts() {
// return electronicContacts;
// }
//
// /**
// * Sets electronic contacts
// *
// * @param electronicContacts
// * to set
// */
// public void setElectronicContacts(BoilerplateList<ElectronicContact>
// electronicContacts) {
// this.electronicContacts = electronicContacts;
// }
//
// /**
// * This is the lastHistoryDate
// */
// java.util.Date lastHistoryDate;
// /**
// * This is the daysPastDue
// */
// int daysPastDue;
// /**
// * This is the address
// */
// String address;
// /**
// * This is the electronicContacts
// */
// BoilerplateList<ElectronicContact> electronicContacts = new
// BoilerplateList<>();
// /**
// * This is the addresses
// */
// BoilerplateList<Address> addresses = new BoilerplateList<>();
//
// /**
// * Gets the addresses
// *
// * @return the addresses
// */
// public BoilerplateList<Address> getAddresses() {
// return addresses;
// }
//
// /**
// * Sets addresses
// *
// * @param addresses
// * to set
// */
// public void setAddresses(BoilerplateList<Address> addresses) {
// this.addresses = addresses;
// }
//
// /**
// * Gets the dayspastdue
// *
// * @return the days past due
// */
// public int getDaysPastDue() {
// return daysPastDue;
// }
//
// /**
// * Sets the daysPastDue
// *
// * @param daysPastDue
// * to set
// */
// public void setDaysPastDue(int daysPastDue) {
// this.daysPastDue = daysPastDue;
// }
//
// /**
// * This is the phone
// */
// String phone;
// /**
// * This is the settlementAmount
// */
// double settlementAmount;
// /**
// * This is the currentBalance
// */
// double currentBalance;
// /**
// * This is the dateReported
// */
// java.util.Date dateReported;
// /**
// * This is the amountDue
// */
// double amountDue;
// /**
// * This is the valueCollateral
// */
// String valueCollateral;
// /**
// * This is the typeCollateral
// */
// String typeCollateral;
// /**
// * This is the occupation
// */
// String occupation;
// /**
// * This is the rateOfIntererst
// */
// double rateOfIntererst;
// /**
// * This is the income
// */
// double income;
// /**
// * This is the tradeLineXML
// */
// String tradeLineXML;
// /**
// * This is the experianTradelineStatusEnum
// */
// ExperianTradelineStatus experianTradelineStatusEnum;
// /**
// * This is the getTos
// */
// boolean getTos;
//
// /**
// * Gets the getTos
// *
// * @return the getTos
// */
// public boolean isGetTos() {
// return getTos;
// }
//
// /**
// * Sets the getTos
// *
// * @param getTos
// * to set
// */
// public void setGetTos(boolean getTos) {
// this.getTos = getTos;
// }
//
// /**
// * This is the organizationId
// */
// String organizationId;
// /**
// * This is the productId
// */
// String productId;
//
// /**
// * Gets the report id
// *
// * @return report id
// */
// public String getReportId() {
// return reportId;
// }
//
// /**
// * Sets the report id
// *
// * @param reportId
// * to set
// */
// public void setReportId(String reportId) {
// this.reportId = reportId;
// }
//
// /**
// * Gets the accountNumber
// *
// * @return the accountNumber
// */
// public String getAccountNumber() {
// return accountNumber;
// }
//
// /**
// * Sets the accountNumber
// *
// * @param accountNumber
// * to set
// */
// public void setAccountNumber(String accountNumber) {
// this.accountNumber = accountNumber;
// }
//
// /**
// * Gets the highCreditLoanAmount
// *
// * @return the highCreditLoanAmount
// */
// public double getHighCreditLoanAmount() {
// return highCreditLoanAmount;
// }
//
// /**
// * Sets the highCreditLoanAmount
// *
// * @param highCreditLoanAmount
// * to set
// */
// public void setHighCreditLoanAmount(double highCreditLoanAmount) {
// this.highCreditLoanAmount = highCreditLoanAmount;
// }
//
// /**
// * Gets the repaymentTenure
// *
// * @return the repaymentTenure
// */
// public double getRepaymentTenure() {
// return repaymentTenure;
// }
//
// /**
// * Sets the repaymentTenure
// *
// * @param repaymentTenure
// * to set
// */
// public void setRepaymentTenure(double repaymentTenure) {
// this.repaymentTenure = repaymentTenure;
// }
//
// /**
// * Gets the dateOpened
// *
// * @return the dateOpened
// */
// public java.util.Date getDateOpened() {
// return dateOpened;
// }
//
// /**
// * Sets the dateOpened
// *
// * @param dateOpened
// * to set
// */
// public void setDateOpened(java.util.Date dateOpened) {
// this.dateOpened = dateOpened;
// }
//
// /**
// * Gets date closed
// *
// * @return the date closed
// */
// public java.util.Date getDateClosed() {
// return dateClosed;
// }
//
// /**
// * Sets the dateClosed
// *
// * @param dateClosed
// * to set
// */
// public void setDateClosed(java.util.Date dateClosed) {
// this.dateClosed = dateClosed;
// }
//
// /**
// * Gets the dateOfLastPayment
// *
// * @return the dateOfLastPayment
// */
// public java.util.Date getDateOfLastPayment() {
// return dateOfLastPayment;
// }
//
// /**
// * Sets the dateOfLastPayment
// *
// * @param dateOfLastPayment
// * to set
// */
// public void setDateOfLastPayment(java.util.Date dateOfLastPayment) {
// this.dateOfLastPayment = dateOfLastPayment;
// }
//
// /**
// * Gets the accountHolderType
// *
// * @return the accountHolderType
// */
// public String getAccountHolderType() {
// return accountHolderType;
// }
//
// /**
// * Sets the accountHolderType
// *
// * @param accountHolderType
// * to set
// */
// public void setAccountHolderType(String accountHolderType) {
// this.accountHolderType = accountHolderType;
// }
//
// /**
// * Gets the lastHistoryDate
// *
// * @return the lastHistoryDate
// */
// public java.util.Date getLastHistoryDate() {
// return lastHistoryDate;
// }
//
// /**
// * Sets the lastHistoryDate
// *
// * @param lastHistoryDate
// * to set
// */
// public void setLastHistoryDate(java.util.Date lastHistoryDate) {
// this.lastHistoryDate = lastHistoryDate;
// }
//
// /**
// * Gets the address
// *
// * @return the address
// */
// public String getAddress() {
// return address;
// }
//
// /**
// * Sets the address
// *
// * @param address
// * to set
// */
// public void setAddress(String address) {
// this.address = address;
// }
//
// /**
// * Gets the phone
// *
// * @return the phone
// */
// public String getPhone() {
// return phone;
// }
//
// /**
// * Sets the phone
// *
// * @param phone
// * to set
// */
// public void setPhone(String phone) {
// this.phone = phone;
// }
//
// /**
// * Gets the settlementAmount
// *
// * @return the settlementAmount
// */
// public double getSettlementAmount() {
// return settlementAmount;
// }
//
// /**
// * Sets the settlementAmount
// *
// * @param settlementAmount
// * to set
// */
// public void setSettlementAmount(double settlementAmount) {
// this.settlementAmount = settlementAmount;
// }
//
// /**
// * Gets the currentBalance
// *
// * @return the currentBalance
// */
// public double getCurrentBalance() {
// return currentBalance;
// }
//
// /**
// * Sets the currentBalance
// *
// * @param currentBalance
// * to set
// */
// public void setCurrentBalance(double currentBalance) {
// this.currentBalance = currentBalance;
// }
//
// /**
// * Gets the date reported
// *
// * @return the date reported
// */
// public java.util.Date getDateReported() {
// return dateReported;
// }
//
// /**
// * Sets the dateReported
// *
// * @param dateReported
// * to set
// */
// public void setDateReported(java.util.Date dateReported) {
// this.dateReported = dateReported;
// }
//
// /**
// * Gets the amountDue
// *
// * @return the amountDue
// */
// public double getAmountDue() {
// return amountDue;
// }
//
// /**
// * Sets the amountDue
// *
// * @param amountDue
// * to set
// */
// public void setAmountDue(double amountDue) {
// this.amountDue = amountDue;
// }
//
// /**
// * Gets the valueCollateral
// *
// * @return the valueCollateral
// */
// public String getValueCollateral() {
// return valueCollateral;
// }
//
// public void setValueCollateral(String valueCollateral) {
// this.valueCollateral = valueCollateral;
// }
//
// /**
// * Gets the valueCollateral
// *
// * @return the valueCollateral
// */
// public String getTypeCollateral() {
// return typeCollateral;
// }
//
// public void setTypeCollateral(String typeCollateral) {
// this.typeCollateral = typeCollateral;
// }
//
// /**
// * Gets the occupation
// *
// * @return the occupation
// */
// public String getOccupation() {
// return occupation;
// }
//
// /**
// * Sets the occupation
// *
// * @param occupation
// * to set
// */
// public void setOccupation(String occupation) {
// this.occupation = occupation;
// }
//
// /**
// * Gets the rateOfIntererst
// *
// * @return rateOfIntererst
// */
// public double getRateOfIntererst() {
// return rateOfIntererst;
// }
//
// /**
// * Sets the rateOfIntererst
// *
// * @param rateOfIntererst
// * to set
// */
// public void setRateOfIntererst(double rateOfIntererst) {
// this.rateOfIntererst = rateOfIntererst;
// }
//
// /**
// * Gets the income
// *
// * @return the income
// */
// public double getIncome() {
// return income;
// }
//
// /**
// * Sets the income
// *
// * @param income
// * to set
// */
// public void setIncome(double income) {
// this.income = income;
// }
//
// /**
// * Gets the tradeLineXML
// *
// * @return the tradeLineXML
// */
// public String getTradeLineXML() {
// return tradeLineXML;
// }
//
// /**
// * Sets the tradeLineXML
// *
// * @param tradeLineXML
// * to set
// */
// public void setTradeLineXML(String tradeLineXML) {
// this.tradeLineXML = tradeLineXML;
// }
//
// /**
// * Gets the experianTradelineStatusEnum
// *
// * @return the experianTradelineStatusEnum
// */
// public ExperianTradelineStatus getExperianTradelineStatusEnum() {
// return experianTradelineStatusEnum;
// }
//
// /**
// * Sets the experianTradelineStatusEnum
// *
// * @param experianTradelineStatusEnum
// * to set
// */
// public void setExperianTradelineStatusEnum(ExperianTradelineStatus
// experianTradelineStatusEnum) {
// this.experianTradelineStatusEnum = experianTradelineStatusEnum;
// }
//
// /**
// * Gets the experianTradelineStatusEnum
// *
// * @return the experianTradelineStatusEnum
// */
// public int getExperianTradelineStatus() {
// return experianTradelineStatusEnum.ordinal();
// }
//
// public void setExperianTradelineStatus(int experianTradelineStatus) {
// this.experianTradelineStatusEnum =
// ExperianTradelineStatus.values()[experianTradelineStatus];
// }
//
// /**
// * Gets the organizationId
// *
// * @return the organizationId
// */
// public String getOrganizationId() {
// return organizationId;
// }
//
// /**
// * Sets the organizationId
// *
// * @param organizationId
// * to set
// */
// public void setOrganizationId(String organizationId) {
// this.organizationId = organizationId;
// }
//
// /**
// * Gets the productId
// *
// * @return the productId
// */
// public String getProductId() {
// return productId;
// }
//
// /**
// * Sets the productId
// *
// * @param productId
// * to set
// */
// public void setProductId(String productId) {
// this.productId = productId;
// }
//
// /**
// * This is the userId
// */
// private String userId;
//
// /**
// * Gets the userId
// *
// * @return the userId
// */
// public String getUserId() {
// return userId;
// }
//
// /**
// * Sets the userId
// *
// * @param userId
// * to set
// */
// public void setUserId(String userId) {
// this.userId = userId;
// }
//
// /**
// * This is the ReportTradelineStatus
// */
// ReportTradelineStatus reportTradelineStatus;
//
// /**
// * This method gets the ReportTradelineStatus
// *
// * @return reportTradelineStatus The ReportTradelineStatus
// */
// public ReportTradelineStatus getReportTradelineStatus() {
// return reportTradelineStatus;
// }
//
// /**
// * This method sets the ReportTradelineStatus
// *
// * @param reportTradelineStatus
// * The ReportTradelineStatus
// */
// public void setReportTradelineStatus(ReportTradelineStatus
// reportTradelineStatus) {
// this.reportTradelineStatus = reportTradelineStatus;
// }
//
// @Override
// public String createPublishJSON(String template) throws UnauthorizedException
// {
// String retrunValue = template;
// retrunValue = retrunValue.replace("@tradelineStatus",
// this.getReportTradelineStatus().toString());
// return retrunValue;
// }
//
// /**
// * This method get the tradeLineFull name
// *
// * @return the tradelineFullName
// */
// public String getTradelineFullName() {
// return tradelineFullName;
// }
//
// /**
// * This method set the tradeLineFull name
// *
// * @param tradelineFullName
// * the tradelineFullName to set
// */
// public void setTradelineFullName(String tradelineFullName) {
// this.tradelineFullName = tradelineFullName;
// }
//
// @Override
// public String createPublishUrl(String url) throws
// UnsupportedEncodingException {
// String returnUrl = url;
// returnUrl = returnUrl.replace("@tradeLineID", URLEncoder.encode(this.getId(),
// "UTF-8"));
// return returnUrl;
// }
//
// /**
// * This is the tradelinefullname
// */
// String tradelineFullName;
// /**
// * This is the crmReportId
// */
// private String crmReportId;
//
// /**
// * This method get the crm report id
// *
// * @return the crmReportId
// */
// public String getCrmReportId() {
// return crmReportId;
// }
//
// /**
// * This method set the crm report id
// *
// * @param crmReportId
// * the crmReportId to set
// */
// public void setCrmReportId(String crmReportId) {
// this.crmReportId = crmReportId;
// }
//
// /**
// * @return the paymentSource
// */
// public String getPaymentSource() {
// return paymentSource;
// }
//
// /**
// * @param paymentSource
// * the paymentSource to set
// */
// public void setPaymentSource(String paymentSource) {
// this.paymentSource = paymentSource;
// }
//
// /**
// * @return the leadEmail
// */
// public String getLeadEmail() {
// return leadEmail;
// }
//
// /**
// * @param leadEmail
// * the leadEmail to set
// */
// public void setLeadEmail(String leadEmail) {
// this.leadEmail = leadEmail;
// }
//
// /**
// * This is the paymentSource
// */
// private String paymentSource;
// /**
// * This is the leadEmail
// */
// private String leadEmail;
//
// }
