package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This entity contains the details of the amortized schedule for a year that
 * contains principal,interest, total payment done and loan left in a year
 * 
 * @author urvij
 *
 */
public class AmortizedScheduleYearly extends BaseEntity implements Serializable{
	
	/**
	 * This is the interest amount paid in a year
	 */
	private String interestYearly;
	/**
	 * This is the principal amount paid in a year
	 */
	private String principalYearly;
	/**
	 * This is the total payment done in a year
	 */
	private String totalPaymentYearly;
	/**
	 * This is the year of the payment 
	 */
	private String year;
	/**
	 * This is the loan paid in percentage
	 */
	private String loanPaidInPercentage;

	

	/**
	 * Gets the interest in a year
	 * @return the interestYearly
	 */
	public String getInterestYearly() {
		return interestYearly;
	}

	/**
	 * Sets the interest in a year
	 * @param interestYearly the interestYearly to set
	 */
	public void setInterestYearly(String interestYearly) {
		this.interestYearly = interestYearly;
	}

	/**
	 * Gets the principal in a year
	 * @return the principalYearly
	 */
	public String getPrincipalYearly() {
		return principalYearly;
	}

	/**
	 * Sets the principal in a year
	 * @param principalYearly the principalYearly to set
	 */
	public void setPrincipalYearly(String principalYearly) {
		this.principalYearly = principalYearly;
	}

	/**
	 * Gets the total payment in a year
	 * @return the totalPaymentYearly
	 */
	public String getTotalPaymentYearly() {
		return totalPaymentYearly;
	}

	/**
	 * Sets the total payment in a year
	 * @param totalPaymentYearly the totalPaymentYearly to set
	 */
	public void setTotalPaymentYearly(String totalPaymentYearly) {
		this.totalPaymentYearly = totalPaymentYearly;
	}

	/**
	 * Gets the year of calculation
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Sets the year of calculation
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * Gets the loan paid in percentage
	 * @return the loanPaidInPercentage
	 */
	public String getLoanPaidInPercentage() {
		return loanPaidInPercentage;
	}

	/**
	 * Sets the loan paid in percentage
	 * @param loanPaidInPercentage the loanPaidInPercentage to set
	 */
	public void setLoanPaidInPercentage(String loanPaidInPercentage) {
		this.loanPaidInPercentage = loanPaidInPercentage;
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
