package com.boilerplate.java.entities;

/**
 * This entity contains the details of the amortized schedule that contains
 * principal paid, interest paid, and loan amount at the end of month remaining
 * after payment of each EMI
 * 
 * @author urvij
 *
 */
public class AmortizedScheduleDetails {
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
	private String loanAmountAtEndOfMonth;
	/**
	 * This is the month of amortized schedule calculation
	 */
	private String month;
	/**
	 * This is the year of amortized schedule calculation
	 */
    private String year;
	
	
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
	public String getLoanAmountAtEndOfMonth() {
		return loanAmountAtEndOfMonth;
	}
	/**
	 * Sets the loan amount
	 * @param loanAmountAtEndOfMonth The loan amount at end of month
	 */
	public void setLoanAmountAtEndOfMonth(String loanAmountAtEndOfMonth) {
		this.loanAmountAtEndOfMonth = loanAmountAtEndOfMonth;
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

}
