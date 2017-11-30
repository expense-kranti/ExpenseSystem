package com.boilerplate.service.implemetations;

import java.time.LocalDateTime;
import java.time.Month;

import javax.xml.bind.ValidationException;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AmortizedScheduleDetails;
import com.boilerplate.java.entities.EmiDataEntity;
import com.boilerplate.service.interfaces.IEmiCalculatorService;

/**
 * This class implements the IEmiCalculatorService
 * 
 * @author urvij
 *
 */
public class EmiCalculatorService implements IEmiCalculatorService {

	/**
	 * @see IEmiCalculatorService.emiCalculator
	 */
	@Override
	public EmiDataEntity emiCalculator(EmiDataEntity emiDataEntity) throws ValidationException {

		this.validateInput(emiDataEntity);

		// principal amount of loan taken
		double initialLoanAmount = emiDataEntity.getInitialPrincipalBorrowed() - emiDataEntity.getDownPayment();
		// time period in years for which loan is taken
		float loanPeriodInYears = emiDataEntity.getLoanPeriodInYears();

		// time period in months for which loan is taken
		float loanPeriodInMonths = loanPeriodInYears * 12;
		// this is the loan amount left after paying EMI
		double balanceAtEndOFMonth = initialLoanAmount;
		// interest rate per month
		double interestRatePerMonth;
		// interest rate on which loan was borrowed
		double interestRatePerYear = emiDataEntity.getInterestRatePerYear();
		if (interestRatePerYear != 0) {
			// dividing interest rate per year to get interest rate per month if
			// interest rate per year is not equal to zero
			interestRatePerMonth = interestRatePerYear / 12;
		} else {
			// Getting interest rate per month filled
			interestRatePerMonth = emiDataEntity.getInterestRatePerMonth();
		}

		// interest amount to be paid per month on loan principle
		double interestPerMonth = interestRatePerMonth / 100;

		// this is the EMI amount to be paid in Arrear scheme
		double arrearEmiAmount = 0;

		// checking for loan type for selectively calculating EMIs as required
		switch (emiDataEntity.getLoanType()) {
		case CarLoan:
			arrearEmiAmount = this.calculateEMI(initialLoanAmount, interestPerMonth, loanPeriodInMonths);
			generateAmortizedSchedule(interestPerMonth, initialLoanAmount, loanPeriodInMonths, interestPerMonth,
					arrearEmiAmount, balanceAtEndOFMonth, emiDataEntity);
			break;
		case HomeLoan:
			arrearEmiAmount = this.calculateEMI(initialLoanAmount, interestPerMonth, loanPeriodInMonths);
			generateAmortizedSchedule(interestPerMonth, initialLoanAmount, loanPeriodInMonths, interestPerMonth,
					arrearEmiAmount, balanceAtEndOFMonth, emiDataEntity);
			break;
		case PersonalLoan:
			arrearEmiAmount = this.calculateEMI(initialLoanAmount, interestPerMonth, loanPeriodInMonths);
			this.generateAmortizedSchedule(interestPerMonth, initialLoanAmount, loanPeriodInMonths, interestPerMonth,
					arrearEmiAmount, balanceAtEndOFMonth, emiDataEntity);
			break;

		default:
			arrearEmiAmount = this.calculateEMI(initialLoanAmount, interestPerMonth, loanPeriodInMonths);
			this.generateAmortizedSchedule(interestPerMonth, initialLoanAmount, loanPeriodInMonths, interestPerMonth,
					arrearEmiAmount, balanceAtEndOFMonth, emiDataEntity);
			break;

		}

		// setting the Arrear EMI to be paid for loan taken
		emiDataEntity.setEmi(new Double(Math.round(arrearEmiAmount)).toString());

		return emiDataEntity;

	}

	// this method calculates the Arrear based EMI
	/**
	 * this method calculates the Arrear based EMI
	 * 
	 * @param principalAmount
	 *            The loan amount applicable after deducting down payment(if
	 *            any)
	 * @param interestPerMonth
	 *            The interest to be paid per month
	 * @param loanPeriodInMonths
	 *            loan period in months
	 * @return The EMI calculated
	 */
	public double calculateEMI(double principalAmount, double interestPerMonth, float loanPeriodInMonths) {
		return (principalAmount * interestPerMonth * ((Math.pow((1 + interestPerMonth), loanPeriodInMonths))
				/ (Math.pow((1 + interestPerMonth), loanPeriodInMonths) - 1)));
	}

	/**
	 * this method generates the amortized schedule that is the details of
	 * principal amount and interest amount paid in each EMI within loan period
	 * and calculates the total interest, total principal paid for the loan
	 * taken
	 * 
	 * @param interest
	 *            this variable is initially set to the interest to be paid per
	 *            month and will change for each month wile calculating
	 *            amortized schedule
	 * @param beginningBalance
	 *            The loan amount to be used to calculate amortized schedule for
	 *            each month
	 * @param loanPeriodInMonths
	 *            loan period in months
	 * @param interestPerMonth
	 *            interest per month
	 * @param arrearEmiAmount
	 *            EMI to be paid
	 * @param balanceAtEndOFMonth
	 *            loan amount remaining after paying EMI at the end of each
	 *            month
	 * @param emiDataEntity
	 *            the emiDataEntity whose some values will be set in the method
	 */
	public void generateAmortizedSchedule(double interest, double beginningBalance,
			float loanPeriodInMonths, double interestPerMonth, double arrearEmiAmount, double balanceAtEndOFMonth,
			EmiDataEntity emiDataEntity) {

		// this is the total interest 
		double totalInterest = 0;
		// this is the total principal 
		double totalPrincipal = 0;
		// this is the principal amount paid in a month
		double principal= 0;
		// taking month of the current date as the starting month of Emi payment
		LocalDateTime dateOfEmiStart = LocalDateTime.now();
		// get the month of emi payment start date
		Month currentMonth = dateOfEmiStart.getMonth();
		// wrap current year integer value to Integer class
		Integer currentYearWrappedObject = new Integer(dateOfEmiStart.getYear());
		// list of amortized schedule details
		BoilerplateList<AmortizedScheduleDetails> amortizedScheduleList = new BoilerplateList<>();
		// this loop calculates and yields amortized schedule
		for (int i = 1; i <= loanPeriodInMonths; i++) {

			// Getting current month value in string
			String currentMonthInString = currentMonth.toString();
			// Getting current year value in string
			String currentYearInString = currentYearWrappedObject.toString();
			if (currentMonth.compareTo(Month.DECEMBER) == 0) {
				currentYearWrappedObject += 1;
			}

			// here beginningBalance (is the changing value which) acts as base
			// loan amount for next month to calculate amortized schedule
			interest = beginningBalance * interestPerMonth;
			// this is the principal part of EMI
			principal = arrearEmiAmount - interest;
			// this is the loan amount remaining
			balanceAtEndOFMonth = balanceAtEndOFMonth - principal;
			// here beginning balance will act as loan amount to calculate next
			// principal and interest
			beginningBalance = balanceAtEndOFMonth;
			// add each month's interest paid in each EMI to calculate total
			// interest paid for loan payment
			totalInterest += interest;
			// calculate total interest paid by adding principal paid per EMI
			totalPrincipal += principal;
			// populate and set amortized schedule details list
			this.populateAndSetAmortizedScheduleDetailList(amortizedScheduleList, emiDataEntity,
					interest, principal, balanceAtEndOFMonth, currentMonthInString,
					currentYearInString);
			// Increasing the value of month by one to sync data with next entry
			// for amortized schedule calculation
			currentMonth = currentMonth.plus(1);

			// set amortized schedule list of emi data entity
			emiDataEntity.setAmortizedScheduleDetailsList(amortizedScheduleList);
		}
		// setting the total Interest paid for loan taken
		emiDataEntity.setTotalInterest(new Double(Math.round(totalInterest)).toString());
		// setting the total payment
		emiDataEntity.setTotalPayment(new Double(Math.round(totalInterest+ totalPrincipal)).toString());

	}

	/**
	 * this method checks for zero values of required fields for calculating EMI
	 * 
	 * @param emiDataEntity
	 *            The emi Data Entity which is expected to hold the values to be
	 *            used in EMI calculations
	 * @throws ValidationException
	 *             Thrown when one or more of the required fields are empty
	 */
	public void validateInput(EmiDataEntity emiDataEntity) throws ValidationException {
		// check if loan amount borrowed is 0
		if (emiDataEntity.getInitialPrincipalBorrowed() == 0) {
			throw new ValidationException("Please fill in the initial principal borrowed fields");
		}
		// check if interest rate per year and interest rate per month is 0
		if (emiDataEntity.getInterestRatePerYear() == 0 && emiDataEntity.getInterestRatePerMonth() == 0) {
			throw new ValidationException("Please fill in one of the interest rates");
		}
		// checks if loan period in years is 0
		if (emiDataEntity.getLoanPeriodInYears() == 0) {
			throw new ValidationException("Please fill in loan period");
		}
	}

	/**
	 * This method populates and set the amortized schedule details list
	 * 
	 * @param amortizedScheduleDetailsList
	 *            Empty list of amortized schedule details
	 * @param emiDataEntity
	 *            The emiData Entity
	 * @param interest
	 *            The interest amount paid in each EMI
	 * @param principal
	 *            The principal amount paid in each EMI
	 * @param balanceAtEndOFMonth
	 *            the loan amount left after EMI paid
	 * @param month
	 *            The month of respective amortized schedule month calculation
	 * @param year
	 *            The year of respective amortized schedule month calculation
	 */
	public void populateAndSetAmortizedScheduleDetailList(
			BoilerplateList<AmortizedScheduleDetails> amortizedScheduleDetailsList, EmiDataEntity emiDataEntity,
			double interest, double principal, double balanceAtEndOFMonth, String month, String year) {
		// instance of amortizedScheduledetails
		AmortizedScheduleDetails amortizedScheduleDetailsForEachMonth = new AmortizedScheduleDetails();

		// set the interest amount paid at the current month of calculation
		amortizedScheduleDetailsForEachMonth
				.setInterest(new Double(Math.round(interest)).toString());
		// set the loan amount remaining at the end of current month of
		// calculation
		amortizedScheduleDetailsForEachMonth
				.setLoanAmountAtEndOfMonth(new Double(Math.round(balanceAtEndOFMonth)).toString());
		// set the principal amount paid at the current month of calculation
		amortizedScheduleDetailsForEachMonth
				.setPrincipal(new Double(Math.round(principal)).toString());
		// set the month
		amortizedScheduleDetailsForEachMonth.setMonth(month);
		// set the year
		amortizedScheduleDetailsForEachMonth.setYear(year);
		// add the amortized schedule details object having all
		amortizedScheduleDetailsList.add(amortizedScheduleDetailsForEachMonth);

	}

}
