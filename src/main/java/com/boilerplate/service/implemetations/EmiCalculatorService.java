package com.boilerplate.service.implemetations;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.Month;

import javax.xml.bind.ValidationException;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AmortizedScheduleDetails;
import com.boilerplate.java.entities.AmortizedScheduleYearly;
import com.boilerplate.java.entities.EmiDataEntity;
import com.boilerplate.service.interfaces.IEmiCalculatorService;

/**
 * This class implements the IEmiCalculatorService interface
 * 
 * @author urvij
 *
 */
public class EmiCalculatorService implements IEmiCalculatorService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(EmiCalculatorService.class);
	// format of double values in two places decimal
	DecimalFormat df = new DecimalFormat("#.##");

	/**
	 * @see IEmiCalculatorService.emiCalculator
	 */
	@Override
	public EmiDataEntity emiCalculator(EmiDataEntity emiDataEntity) throws ValidationFailedException {
		// validating inputs
		emiDataEntity.validate();

		// actual loan amount left after paying down payment
		double initialLoanAmount = emiDataEntity.getInitialPrincipalBorrowed() - emiDataEntity.getDownPayment();
		int loanPeriodInYears = (int) emiDataEntity.getLoanPeriodInYears();
		int loanPeriodInMonths;
		if (loanPeriodInYears != 0) {
			// Calculate loan period in months
			loanPeriodInMonths = loanPeriodInYears * 12;
		} else {
			// Get loan period in months if given
			loanPeriodInMonths = emiDataEntity.getLoanPeriodInMonths();
		}
		// loan amount left after paying EMI
		double balanceAtEndOFMonth = initialLoanAmount;
		double interestRatePerMonth;
		double interestRatePerYear = emiDataEntity.getInterestRatePerYear();
		if (interestRatePerYear != 0) {
			// Calculate interest rate per month
			interestRatePerMonth = interestRatePerYear / 12;
		} else {
			// Get interest rate per month if given
			interestRatePerMonth = emiDataEntity.getInterestRatePerMonth();
		}
		// interest per month
		double interestPerMonth = interestRatePerMonth / 100;
		// EMI to be paid
		double arrearEmiAmount = 0;
		// check for loan type
		switch (emiDataEntity.getLoanType()) {
		case CarLoan:
			arrearEmiAmount = this.calculateEMI(initialLoanAmount, interestPerMonth, loanPeriodInMonths);
			this.generateAmortizedSchedule(interestPerMonth, initialLoanAmount, loanPeriodInMonths, interestPerMonth,
					arrearEmiAmount, balanceAtEndOFMonth, emiDataEntity);
			break;
		case HomeLoan:
			arrearEmiAmount = this.calculateEMI(initialLoanAmount, interestPerMonth, loanPeriodInMonths);
			this.generateAmortizedSchedule(interestPerMonth, initialLoanAmount, loanPeriodInMonths, interestPerMonth,
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

		// set Arrear EMI to be paid to emidataentity
		emiDataEntity.setEmi(df.format(arrearEmiAmount));

		return emiDataEntity;

	}

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
	 * @return The calculated EMI
	 */
	public double calculateEMI(double principalAmount, double interestPerMonth, int loanPeriodInMonths) {
		// this is the formula for calculating EMI
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
	 *            It is initially set to the interest to be paid per month and
	 *            will change for each month while calculating amortized
	 *            schedule
	 * @param beginningBalance
	 *            The loan amount to be used to calculate amortized schedule for
	 *            each month
	 * @param loanPeriodInMonths
	 *            loan period in months
	 * @param interestPerMonth
	 *            interest per month
	 * @param arrearEmiAmount
	 *            EMI to be paid in arrear based scheme
	 * @param balanceAtEndOFMonth
	 *            loan amount remaining after paying EMI at the end of each
	 *            month
	 * @param emiDataEntity
	 *            amortizedSchedule,totalPayment,totalInterest will be
	 *            calculated and set in emiDataEntity
	 */
	public void generateAmortizedSchedule(double interest, double beginningBalance, int loanPeriodInMonths,
			double interestPerMonth, double arrearEmiAmount, double balanceAtEndOFMonth, EmiDataEntity emiDataEntity) {

		double totalInterest = 0, totalPrincipal = 0, principal = 0, initialLoanAmount = beginningBalance,
				interestThisYear = 0, principalThisYear = 0;
		// Starting month of Emi payment
		LocalDateTime dateOfEmiStart = LocalDateTime.now();
		Month currentMonth = dateOfEmiStart.getMonth();
		int currentYear = dateOfEmiStart.getYear();
		// list of amortized schedule details for each month
		BoilerplateList<AmortizedScheduleDetails> amortizedScheduleList = new BoilerplateList<>();
		// list of amortized schedule per year
		BoilerplateList<AmortizedScheduleYearly> amortizedScheduleYearlyList = new BoilerplateList<>();
		AmortizedScheduleYearly amortizedScheduleOfaYear = new AmortizedScheduleYearly();

		// this loop calculates and yields amortized schedule
		for (int i = 1; i <= loanPeriodInMonths + 1; i++) {

			int yearOfCalculation = currentYear;
			// check if loan period is ended
			if (i == loanPeriodInMonths + 1) {
				interest = 0;
				principal = 0;
				this.calculateAndSetAmortizedScheduleDetailList(initialLoanAmount, amortizedScheduleList, interest,
						principal, balanceAtEndOFMonth, currentMonth.toString(), String.valueOf(currentYear));

				break;
			}
			// here beginningBalance acts as base loan amount for next month
			interest = beginningBalance * interestPerMonth;
			// principal part of EMI
			principal = arrearEmiAmount - interest;
			// loan amount left after each EMI payment
			balanceAtEndOFMonth = balanceAtEndOFMonth - principal;
			// here beginningBalance acts as loan amount for next month
			beginningBalance = balanceAtEndOFMonth;

			amortizedScheduleOfaYear.setYear(String.valueOf(currentYear));

			// calculate and set amortized schedule details list per month
			this.calculateAndSetAmortizedScheduleDetailList(initialLoanAmount, amortizedScheduleList, interest,
					principal, balanceAtEndOFMonth, currentMonth.toString(), String.valueOf(currentYear));

			totalInterest += interest;
			totalPrincipal += principal;

			interestThisYear += interest;
			principalThisYear += principal;

			if ((currentMonth.compareTo(Month.DECEMBER) == 0)) {
				//calculate set amortized scheduler for each year
				amortizedScheduleOfaYear.setInterestYearly(df.format(interestThisYear));
				amortizedScheduleOfaYear.setPrincipalYearly(df.format(principalThisYear));
				amortizedScheduleOfaYear.setTotalPaymentYearly(df.format(interestThisYear + principalThisYear));
				amortizedScheduleOfaYear.setLoanPaidInPercentage(
						df.format(((initialLoanAmount - balanceAtEndOFMonth) / initialLoanAmount) * 100));
				if (balanceAtEndOFMonth <= 0) {
					amortizedScheduleOfaYear.setLoanLeftAtYearEnd(df.format(Math.abs(balanceAtEndOFMonth)));			
				} else {
					amortizedScheduleOfaYear.setLoanLeftAtYearEnd(df.format(balanceAtEndOFMonth));
				}
				amortizedScheduleOfaYear.setYear(String.valueOf(currentYear));
				amortizedScheduleYearlyList.add(amortizedScheduleOfaYear);
				amortizedScheduleOfaYear = new AmortizedScheduleYearly();
				interestThisYear = 0;
				principalThisYear = 0;
                
				currentYear += 1;
			}
			//this check is for last year which has months less than 12
			if (i == loanPeriodInMonths) {
				//calculate set amortized scheduler for each year
				amortizedScheduleOfaYear.setInterestYearly(df.format(interestThisYear));
				amortizedScheduleOfaYear.setPrincipalYearly(df.format(principalThisYear));
				amortizedScheduleOfaYear.setTotalPaymentYearly(df.format(interestThisYear + principalThisYear));
				amortizedScheduleOfaYear.setLoanPaidInPercentage(
						df.format(((initialLoanAmount - balanceAtEndOFMonth) / initialLoanAmount) * 100));
				if (balanceAtEndOFMonth <= 0) {
					amortizedScheduleOfaYear.setLoanLeftAtYearEnd(df.format(Math.abs(balanceAtEndOFMonth)));			
				} else {
					amortizedScheduleOfaYear.setLoanLeftAtYearEnd(df.format(balanceAtEndOFMonth));
				}
				
				amortizedScheduleYearlyList.add(amortizedScheduleOfaYear);
			}

			// Increasing the value of month by one for next month calculation
			currentMonth = currentMonth.plus(1);
		}
		// set amortized schedule list of emi data entity
		emiDataEntity.setAmortizedScheduleDetailsList(amortizedScheduleList);
		emiDataEntity.setAmortizedScheduleYearlyList(amortizedScheduleYearlyList);
		emiDataEntity.setTotalInterest(df.format(totalInterest));
		emiDataEntity.setTotalPayment(df.format(totalInterest + totalPrincipal));

	}

	/**
	 * This method populates and set the amortized schedule details list
	 * 
	 * @param initialLoan
	 *            The initial loan
	 * @param amortizedScheduleDetailsList
	 *            Empty list of amortized schedule details
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
	public void calculateAndSetAmortizedScheduleDetailList(double initialLoan,
			BoilerplateList<AmortizedScheduleDetails> amortizedScheduleDetailsList, double interest, double principal,
			double balanceAtEndOFMonth, String month, String year) {

		// instance of amortizedScheduledetails
		AmortizedScheduleDetails amortizedScheduleDetailsForEachMonth = new AmortizedScheduleDetails();

		// set the interest,principal amount,loan left paid,total payment done
		// at the current month
		// of calculation
		amortizedScheduleDetailsForEachMonth.setInterest(df.format((interest)));
		if (balanceAtEndOFMonth <= 0) {
			amortizedScheduleDetailsForEachMonth.setLoanLeft(df.format(Math.abs(balanceAtEndOFMonth)));
		} else {
			amortizedScheduleDetailsForEachMonth.setLoanLeft(df.format(balanceAtEndOFMonth));
		}
		amortizedScheduleDetailsForEachMonth.setPrincipal(df.format(principal));
		amortizedScheduleDetailsForEachMonth.setMonth(month);
		amortizedScheduleDetailsForEachMonth.setYear(year);
		amortizedScheduleDetailsForEachMonth.setTotalPaymentPerMonth(df.format(interest + principal));
		// calculate and set the loan paid to date in percentage
		amortizedScheduleDetailsForEachMonth
				.setLoanPaidInPercentage(df.format(((initialLoan - balanceAtEndOFMonth) / initialLoan) * 100));
		amortizedScheduleDetailsList.add(amortizedScheduleDetailsForEachMonth);

	}

}
