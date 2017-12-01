package com.boilerplate.service.implemetations;

import java.time.LocalDateTime;
import java.time.Month;

import javax.xml.bind.ValidationException;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AmortizedScheduleDetails;
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

	/**
	 * @see IEmiCalculatorService.emiCalculator
	 */
	@Override
	public EmiDataEntity emiCalculator(EmiDataEntity emiDataEntity) throws ValidationFailedException {
		// validating inputs
		emiDataEntity.validate();
		// principal of loan
		double initialLoanAmount = emiDataEntity.getInitialPrincipalBorrowed() - emiDataEntity.getDownPayment();
		// loan tenure in years
		int loanPeriodInYears = (int) emiDataEntity.getLoanPeriodInYears();
		// loan tenure in months
		int loanPeriodInMonths;
		if (loanPeriodInYears != 0) {
			// Getting loan period in months take closest upper value
			loanPeriodInMonths = loanPeriodInYears * 12;
			// loanPeriodInMonths = (int)Math.ceil(loanPeriodInYears * 12);
		} else {
			// Getting loan period in months filled take closest upper value
			loanPeriodInMonths = emiDataEntity.getLoanPeriodInMonths();
			// loanPeriodInMonths =
			// (int)Math.ceil(emiDataEntity.getLoanPeriodInMonths());
		}
		// loan amount left after paying EMI
		double balanceAtEndOFMonth = initialLoanAmount;
		// interest rate per month
		double interestRatePerMonth;
		// interest rate per year
		double interestRatePerYear = emiDataEntity.getInterestRatePerYear();
		if (interestRatePerYear != 0) {
			// Getting interest rate per month
			interestRatePerMonth = interestRatePerYear / 12;
		} else {
			// Getting interest rate per month filled
			interestRatePerMonth = emiDataEntity.getInterestRatePerMonth();
		}
		// interest per month on loan principal
		double interestPerMonth = interestRatePerMonth / 100;
		// this is the EMI amount to be paid in Arrear scheme
		double arrearEmiAmount = 0;
		// checking for loan type for selectively calculating EMIs as required
		switch (emiDataEntity.getLoanType()) {
		case CarLoan:
			// calculate Arrear based EMI
			arrearEmiAmount = this.calculateEMI(initialLoanAmount, interestPerMonth, loanPeriodInMonths);
			// calculate amortized schedule
			this.generateAmortizedSchedule(interestPerMonth, initialLoanAmount, loanPeriodInMonths,
					interestPerMonth, arrearEmiAmount, balanceAtEndOFMonth, emiDataEntity);
			break;
		case HomeLoan:
			// calculate Arrear based EMI
			arrearEmiAmount = this.calculateEMI(initialLoanAmount, interestPerMonth, loanPeriodInMonths);
			// calculate amortized schedule
			this.generateAmortizedSchedule(interestPerMonth, initialLoanAmount, loanPeriodInMonths, interestPerMonth,
					arrearEmiAmount, balanceAtEndOFMonth, emiDataEntity);
			break;
		case PersonalLoan:
			// calculate Arrear based EMI
			arrearEmiAmount = this.calculateEMI(initialLoanAmount, interestPerMonth, loanPeriodInMonths);
			// calculate amortized schedule
			this.generateAmortizedSchedule(interestPerMonth, initialLoanAmount, loanPeriodInMonths, interestPerMonth,
					arrearEmiAmount, balanceAtEndOFMonth, emiDataEntity);
			break;
		default:
			// calculate Arrear based EMI
			arrearEmiAmount =  this.calculateEMI(initialLoanAmount, interestPerMonth, loanPeriodInMonths);
			// calculate amortized schedule
			this.generateAmortizedSchedule(interestPerMonth, initialLoanAmount, loanPeriodInMonths, interestPerMonth,
					arrearEmiAmount, balanceAtEndOFMonth, emiDataEntity);
			break;

		}

		// setting the Arrear EMI to be paid for loan taken to emidataentity
		emiDataEntity.setEmi(String.valueOf((int) Math.round(arrearEmiAmount)));

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
	 * @return The EMI calculated
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
	 *            the emiDataEntity whose some values will be set in the method
	 */
	public void generateAmortizedSchedule(double interest, double beginningBalance, int loanPeriodInMonths,
			double interestPerMonth, double arrearEmiAmount, double balanceAtEndOFMonth, EmiDataEntity emiDataEntity) {
		// this is the total interest, total principal
		double totalInterest = 0, totalPrincipal = 0, principal = 0;
		// this is starting month of Emi payment
		LocalDateTime dateOfEmiStart = LocalDateTime.now();
		// get the month of emi payment start date
		Month currentMonth = dateOfEmiStart.getMonth();
		// wrap/box current year integer value
		Integer currentYearWrappedObject = new Integer(dateOfEmiStart.getYear());
		// list of amortized schedule details for each month
		BoilerplateList<AmortizedScheduleDetails> amortizedScheduleList = new BoilerplateList<>();
		// this loop calculates and yields amortized schedule
		for (int i = 1; i <= loanPeriodInMonths + 1; i++) {
			// check if loan period about to ended then set EMI principal to pay
			// to zero
			if (i == loanPeriodInMonths + 1) {
				interest = 0;
				principal = 0;
				this.populateAndSetAmortizedScheduleDetailList(amortizedScheduleList, emiDataEntity, interest, principal,
						balanceAtEndOFMonth, currentMonth.toString(), currentYearWrappedObject.toString());
				break;
			}
			// here beginningBalance acts as base loan amount for next month
			interest = beginningBalance * interestPerMonth;
			// this is the principal part of EMI
			principal = arrearEmiAmount - interest;
			// this is the loan amount remaining after each EMI payment
			balanceAtEndOFMonth = balanceAtEndOFMonth - principal;
			// here beginningBalance acts as loan amount for next month
			beginningBalance = balanceAtEndOFMonth;
			// populate and set amortized schedule details list per month
			this.populateAndSetAmortizedScheduleDetailList(amortizedScheduleList, emiDataEntity, interest, principal,
					balanceAtEndOFMonth, currentMonth.toString(), currentYearWrappedObject.toString());

			// calculate total interest
			totalInterest += interest;
			// calculate total principal
			totalPrincipal += principal;

			if (currentMonth.compareTo(Month.DECEMBER) == 0) {
				// increase year by one
				currentYearWrappedObject += 1;
			}
			// Increasing the value of month by one for next month calculation
			currentMonth = currentMonth.plus(1);
		}
		// set amortized schedule list of emi data entity
		emiDataEntity.setAmortizedScheduleDetailsList(amortizedScheduleList);
		// setting the total Interest paid for loan taken
		emiDataEntity.setTotalInterest(String.valueOf((int) totalInterest));
		// setting the total payment
		emiDataEntity.setTotalPayment(String.valueOf((int) Math.round(totalInterest + totalPrincipal)));

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
		amortizedScheduleDetailsForEachMonth.setInterest(String.valueOf((int)Math.round(interest)));
		// set the loan amount remaining at the end of current month of
		// calculation
		amortizedScheduleDetailsForEachMonth.setLoanLeft(String.valueOf((int)Math.round(balanceAtEndOFMonth)));
		// set the principal amount paid at the current month of calculation
		amortizedScheduleDetailsForEachMonth.setPrincipal(String.valueOf((int)Math.round(principal)));
		// set the month
		amortizedScheduleDetailsForEachMonth.setMonth(month);
		// set the year
		amortizedScheduleDetailsForEachMonth.setYear(year);
		// add the amortized schedule details object having all
		amortizedScheduleDetailsList.add(amortizedScheduleDetailsForEachMonth);
	}

}
