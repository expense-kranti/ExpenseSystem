package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This entity contains the details of the amortized schedule that contains
 * principal paid, interest paid, and loan amount at the end of month remaining
 * after payment of each EMI
 * 
 * @author urvij
 *
 */
public class AmortizedScheduleDetails extends BaseEntity implements Serializable{
	/**
	 * This is the interest amount paid at that month
	 */
	private String interest;
	/**
	 * This is the principal amount paid at that month
	 */
	private String principal;
	/**
	 * This is the loan amount left after payment of EMI at the end of each
	 * month
	 */
	private String loanLeft;
	/**
	 * This is the month of amortized schedule calculation
	 */
	private String month;
	/**
	 * This is the year of amortized schedule calculation
	 */
    private String year;
    /**
     * This is the loan paid to date in percentage
     */
	private String loanPaidInPercentage;
	/**
	 * This is the total payment done in a month
	 */
	private String totalPaymentPerMonth;
	
	/**
	 * Gets the interest amount paid at each month in EMI
	 * @return The interest amount paid at each month in EMI
	 */
	public String getInterest() {
		return interest;
	}
	/**
	 * Sets the interest amount paid at each month in EMI
	 * @param interest The interest amount paid at each month in EMI
	 */
	public void setInterest(String interest) {
		this.interest = interest;
	}
	/**
	 * Gets the principal amount paid at each month in EMI
	 * @return  The principal amount paid at each month in EMI
	 */
	public String getPrincipal() {
		return principal;
	}
	/**
	 * Sets the principal amount paid at each month in EMI
	 * @param principal The principal amount paid at each month in EMI
	 */
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	/**
	 * Gets the loan amount 
	 * @return The loan amount at end of month
	 */
	public String getLoanLeft() {
		return loanLeft;
	}
	/**
	 * Sets the loan amount
	 * @param loanAmountAtEndOfMonth The loan amount at end of month
	 */
	public void setLoanLeft(String loanLeft) {
		this.loanLeft = loanLeft;
	}
	/**
	 * Gets the Month
	 * @return The month
	 */
	public String getMonth() {
		return month;
	}
	/**
	 * Sets the month
	 * @param month The month
	 */
	public void setMonth(String month) {
		this.month = month;
	}
	/**
	 * Gets the year
	 * @return The year
	 */
	public String getYear() {
		return year;
	}
	/**
	 * Sets the year
	 * @param year The year
	 */
	public void setYear(String year) {
		this.year = year;
	}
	
	/**
	 * Gets loan paid to date in percentage
	 * @return the loanPaidInPercentage
	 */
	public String getLoanPaidInPercentage() {
		return loanPaidInPercentage;
	}
	/**
	 * Sets loan paid to date in percentage
	 * @param loanPaidInPercentage the loanPaidInPercentage to set
	 */
	public void setLoanPaidInPercentage(String loanPaidInPercentage) {
		this.loanPaidInPercentage = loanPaidInPercentage;
	}
	/**
	 * Gets the total payment done per month
	 * @return The total payment per month
	 */
	public String getTotalPaymentPerMonth() {
		return totalPaymentPerMonth;
	}
	/**
	 * Sets the total payment done per month
	 * @param totalPaymentPerMonth The total payment done per month
	 */
	public void setTotalPaymentPerMonth(String totalPaymentPerMonth) {
		this.totalPaymentPerMonth = totalPaymentPerMonth;
	}
	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}
	/**
     * @see BaseEntity.transformToInternal
     */
	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
