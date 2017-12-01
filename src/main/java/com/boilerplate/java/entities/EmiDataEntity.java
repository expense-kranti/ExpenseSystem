package com.boilerplate.java.entities;

import java.io.Serializable;

import javax.xml.bind.ValidationException;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;

/**
 * This entity contains the data related to EMI like loan amount borrowed, it's
 * interest rate, loan period. Data about amortized schedule like interests and
 * principals paid in each month EMI with its year.
 * 
 * @author urvij
 *
 */
public class EmiDataEntity extends BaseEntity implements Serializable {
	/**
	 * This is the principal amount of loan borrowed
	 */
	private double initialPrincipalBorrowed;
	/**
	 * This is the loan period in years
	 */
	private int loanPeriodInYears;
	/**
	 * This is the loan period in months
	 */
	private int loanPeriodInMonths;
	/**
	 * This is the interest rate per year or per annum for which loan is taken
	 */
	private double interestRatePerYear;
	/**
	 * This is the interest rate per month for which loan is taken
	 */
	private double interestRatePerMonth;
	/**
	 * This is the loan type of loan taken- car loan or home loan or personal
	 * loan etc
	 */
	private LoanType loanType;
	/**
	 * This is the down payment paid for loan
	 */
	private double downPayment;
	/**
	 * This is the amortized schedule data list
	 */
	private BoilerplateList<AmortizedScheduleDetails> amortizedScheduleDetailsList;
	/**
	 * This is the calculated EMI to be paid
	 */
	private String emi;
	/**
	 * This is the calculated total interest calculated to be paid for loan
	 * taken
	 */
	private String totalInterest;
	/**
	 * This is the calculated total payment(principal + interest) to be done for
	 * the loan
	 */
	private String totalPayment;

	/**
	 * Gets the principal borrowed in a loan
	 * 
	 * @return The principal amount borrowed in loan
	 */
	public double getInitialPrincipalBorrowed() {
		return initialPrincipalBorrowed;
	}

	/**
	 * Sets the initial principal borrowed in a loan
	 * 
	 * @param initialPrincipalBorrowed
	 *            The principal amount borrowed in a loan
	 */
	public void setInitialPrincipalBorrowed(double initialPrincipalBorrowed) {
		this.initialPrincipalBorrowed = initialPrincipalBorrowed;
	}

	/**
	 * Gets the loan period in years for which loan is taken
	 * 
	 * @return The loan period in years of loan
	 */
	public int getLoanPeriodInYears() {
		return loanPeriodInYears;
	}

	/**
	 * Sets the loan period in years for which loan is taken
	 * 
	 * @param loanPeriodInYears
	 *            The loan period/tenure in years
	 */
	public void setLoanPeriodInYears(int loanPeriodInYears) {
		this.loanPeriodInYears = loanPeriodInYears;
	}

	/**
	 * Gets the loan period in months for which loan is taken
	 * 
	 * @return The loan period in months of loan
	 */
	public int getLoanPeriodInMonths() {
		return loanPeriodInMonths;
	}

	/**
	 * Sets the loan period in months for which loan is taken
	 * 
	 * @param loanPeriodInMonths
	 */
	public void setLoanPeriodInMonths(int loanPeriodInMonths) {
		this.loanPeriodInMonths = loanPeriodInMonths;
	}

	/**
	 * Gets the interest rate per year or per annum applied on loan
	 * 
	 * @return The interest rate per year or per annum applied on loan
	 */
	public double getInterestRatePerYear() {
		return interestRatePerYear;
	}

	/**
	 * Sets the interest rate per year or per annum applied on loan
	 * 
	 * @param interestRatePerYear
	 *            The interest rate per year or per annum applied on loan
	 */
	public void setInterestRatePerYear(double interestRatePerYear) {
		this.interestRatePerYear = interestRatePerYear;
	}

	/**
	 * Gets the interest rate per month applied on loan taken or borrowed
	 * 
	 * @return The interest rate applied per month
	 */
	public double getInterestRatePerMonth() {
		return interestRatePerMonth;
	}

	/**
	 * Sets the interest rate per month applied on loan taken or borrowed
	 * 
	 * @param interestRatePerMonth
	 *            The interest rate applied per month
	 */
	public void setInterestRatePerMonth(double interestRatePerMonth) {
		this.interestRatePerMonth = interestRatePerMonth;
	}

	/**
	 * Gets the loan type of the loan taken
	 * 
	 * @return The loan type of the loan taken
	 */
	public LoanType getLoanType() {
		return loanType;
	}

	/**
	 * Sets the loan type of the loan taken
	 * 
	 * @param loanType
	 *            The loan type of the loan taken
	 */
	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
	}

	/**
	 * Gets the down payment for loan
	 * 
	 * @return The down payment
	 */
	public double getDownPayment() {
		return downPayment;
	}

	/**
	 * Sets the down payment paid for loan
	 * 
	 * @param downPayment
	 *            The down payment
	 */
	public void setDownPayment(double downPayment) {
		this.downPayment = downPayment;
	}

	/**
	 * Gets the amortizedScheduleDetails list
	 * 
	 * @return The amortizedscheduleDetails list
	 */
	public BoilerplateList<AmortizedScheduleDetails> getAmortizedScheduleDetailsList() {
		return amortizedScheduleDetailsList;
	}

	/**
	 * Sets the amortizedScheduleDetails list
	 * 
	 * @param amortizedSchedulePerMonth
	 *            The amortized scheduled data details list
	 */
	public void setAmortizedScheduleDetailsList(
			BoilerplateList<AmortizedScheduleDetails> amortizedScheduleDetailsList) {
		this.amortizedScheduleDetailsList = amortizedScheduleDetailsList;
	}

	/**
	 * Gets the EMI to be paid
	 * 
	 * @return The EMI to be paid
	 */
	public String getEmi() {
		return emi;
	}

	/**
	 * Sets the EMI to be paid
	 * 
	 * @param emi
	 *            The EMI to be paid
	 */
	public void setEmi(String emi) {
		this.emi = emi;
	}

	/**
	 * Gets the total interest to be paid
	 * 
	 * @return The total interest to be paid
	 */
	public String getTotalInterest() {
		return totalInterest;
	}

	/**
	 * Sets the total interest to be paid
	 * 
	 * @param totalInterest
	 *            The total interest to be paid
	 */
	public void setTotalInterest(String totalInterest) {
		this.totalInterest = totalInterest;
	}

	/**
	 * Gets the total payment to be done
	 * 
	 * @return The total payment to be paid
	 */
	public String getTotalPayment() {
		return totalPayment;
	}

	/**
	 * Sets the total payment to be done
	 * 
	 * @param totalPayment
	 *            The total payment to be paid
	 */
	public void setTotalPayment(String totalPayment) {
		this.totalPayment = totalPayment;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// check if loan amount borrowed is 0
		if (this.getInitialPrincipalBorrowed() == 0) {
			throw new ValidationFailedException("EmiDataEntity", "Initial principal borrowed is equal to zero", null);
		}
		// check if interest rate per year and interest rate per month is 0
		if (this.getInterestRatePerYear() == 0 && this.getInterestRatePerMonth() == 0) {
			throw new ValidationFailedException("EmiDataEntity",
					"One of the interest rates per year or interest rate per month should not be zero", null);
		}
		// checks if loan period in years and loan period in months is 0
		if (this.getLoanPeriodInYears() == 0 && this.getLoanPeriodInMonths() == 0) {
			throw new ValidationFailedException("EmiDataEntity",
					"One of the loan period in years or in months should not be zero", null);
		}
		return true;
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
